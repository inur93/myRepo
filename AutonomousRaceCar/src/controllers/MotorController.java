package controllers;

import globals.Const;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class MotorController {
	
	private volatile EV3LargeRegulatedMotor drivingMotor;
	private volatile EV3LargeRegulatedMotor transmissionMotor;
	private volatile Thread transmissionThread;
	private volatile int currentGear = 1;
	
	public MotorController(boolean autoTransmission){
		this.drivingMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort(Const.leftMotorPort));
		this.transmissionMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort(Const.transmissionMotorPort));
		this.drivingMotor.setAcceleration(Const.motorAcceleration);
		this.transmissionMotor.setAcceleration(Const.MAX_ACCELERATION);
		if(autoTransmission){
			this.transmissionThread = new Thread(new AutoTransmission(this));
			this.transmissionThread.start();
		}
	}
	
	public void forward(){
		this.drivingMotor.setAcceleration(100);
		this.drivingMotor.forward();
	}
	
	public void backward(){
		this.drivingMotor.setAcceleration(100);
		this.drivingMotor.backward();
	}
	
	public void shiftUp(boolean immReturn){
		stop();
		this.transmissionMotor.rotate(360, immReturn);
		sleep(Const.MOTOR_STOP_TIME/3);
		
		currentGear++;
//		forward();
		
	}
	
	public void shiftDown(boolean immReturn){
		currentGear--;
		this.transmissionMotor.rotate(-360, immReturn);
	}
	public void rotate(int degrees, boolean immediateReturn){
		this.drivingMotor.rotate(degrees, immediateReturn);
	}
	public int getCurrentGear(){
		return this.currentGear;
	}
	
	public int getRPM(){
		return this.drivingMotor.getRotationSpeed();
	}

	
	public void stop(){
		this.drivingMotor.stop(true);
		
	}
	
	public void terminate(){
		this.transmissionThread.interrupt();
	}
	
	public float getMaxSpeed(){
		return this.drivingMotor.getMaxSpeed();
	}
	
	public void sleep(long millisec){
		try {
			Thread.sleep(millisec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
