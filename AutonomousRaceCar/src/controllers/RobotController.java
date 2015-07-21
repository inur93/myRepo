package controllers;

import globals.Const;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.SampleProvider;

public class RobotController {

	public enum Direction{ROTATE, FORWARD, BACKWARD};
	private EV3ColorSensor colorSensor;
	private RemoteController remoteController;
	private Direction currentDirection = Direction.FORWARD;

	private SampleProvider iRSSampleProvider;
	private MotorController motorController;
	

	public RobotController(){

		// get a port instance
				Port colorSensorPort = LocalEV3.get().getPort(Const.colorSensorPort);
				Port irSensorPort = LocalEV3.get().getPort(Const.irSensorPort);
		// Get an instance of the Ultrasonic EV3 sensor
				this.colorSensor = new EV3ColorSensor(colorSensorPort);
					// get an instance of this sensor in measurement mode
//				this.iRSSampleProvider= sensor.getDistanceMode();
		this.motorController = new MotorController(false);
		this.remoteController = new RemoteController(new EV3IRSensor(irSensorPort), this.motorController);
		new Thread(this.remoteController).start();

	}

	public static void main(String[] args){
		RobotController rc = new RobotController();
//		rc.run();

	}
	private void sleep(long millisec){
		try {
			Thread.sleep(millisec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void run() {
//		int currentSpeed = 0;
//		System.out.println(motorController.getMaxSpeed());
//		motorController.forward();
//		sleep(100);
//
//		
//		while(motorController.getRPM() < Const.MAX_SPEED){
//			System.out.println("rpm: " + motorController.getRPM());
//			currentSpeed = motorController.getRPM();
//			if(Button.readButtons() == Button.ID_DOWN){
//				break;
//			}
//			// time to get to max and to stop: 7191 ms
//			// time to stop: 3600 ms
//		}
//		long startTime = System.currentTimeMillis();
//		System.out.println("max speed: " + currentSpeed);
//		motorController.stop();
//		while(motorController.getRPM() > 0){
//			
//		}
//		System.out.println("time: " + (System.currentTimeMillis() - startTime));
//		motorController.forward();
		float[] sample = new float[this.colorSensor.sampleSize()];
		while(true){
			
			this.colorSensor.fetchSample(sample, 0);
			System.out.println("id: " + this.colorSensor.getColorID());
			System.out.println("idmode: " + this.colorSensor.getColorIDMode());
			for(float f : sample){
				System.out.println(f);
			}
			Button.waitForAnyPress();
			if(Button.readButtons() == Button.ID_LEFT){
				motorController.shiftDown(false);
//				motorController.rotate(360, false);
			}else if(Button.readButtons() == Button.ID_RIGHT){
				motorController.shiftUp(false);
//				motorController.rotate(-360, false);
			}else{
				break;
			}

		}
		//1: 7
		//2: 0
		//3: 3
		motorController.terminate();
		//		while(true){
		//
		//			Direction dir = findDirection();
		//			if(!dir.equals(currentDirection)){
		//				motorController.stop();
		//			}
		//			this.currentDirection = dir;
		//			switch (dir) {
		//			case FORWARD:
		//				this.motorController.forward();
		//				break;
		//			case ROTATE:
		//				this.motorController.rotate(180, true);
		//				break;
		//			case BACKWARD:
		//				this.motorController.backward();
		//				break;
		//			default:
		//				break;
		//			}
		//
		//			if(Button.readButtons() == Button.ID_DOWN){
		//				break;
		//			}
		//			delay(150);
		//		}
		//		this.sensor.close();
	}

	//TODO add alternate code for direction right
	public Direction findDirection() throws NullPointerException{

		// initialize an array of floats for fetching samples. 
		// Ask the SampleProvider how long the array should be
		float[] sampleCenter = new float[this.iRSSampleProvider.sampleSize()];

		iRSSampleProvider.fetchSample(sampleCenter, 0);

		float valueCenter = sampleCenter[0];

		if(Float.isInfinite(valueCenter) || valueCenter > 40){
			return Direction.FORWARD;
		}else if(valueCenter > 20 && valueCenter < 40){
			return Direction.ROTATE;
		}else{
			return Direction.BACKWARD;
		}
	}

	private void delay(long msec){
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
