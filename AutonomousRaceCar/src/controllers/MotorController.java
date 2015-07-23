package controllers;

import remoteController.RemoteReceiver;
import remoteController.RemoteReceiver.DrivingMotor;
import remoteController.RemoteReceiver.GearMotor;
import remoteController.RemoteReceiver.SteeringMotor;
import globals.Const;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.robotics.RegulatedMotorListener;

public class MotorController {
	
	private volatile EV3LargeRegulatedMotor drivingMotor;
	private volatile EV3LargeRegulatedMotor transmissionMotor;
	private volatile EV3MediumRegulatedMotor steeringMotor;
	private volatile Thread transmissionThread;
	private volatile AutoTransmission autoTransmission;
	private volatile int currentGear = 1;
	private int currentSpeed;
	private boolean test = true;
	
	private boolean goingForward = true;
	
	public MotorController(boolean autoTransmission){
		this.drivingMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort(Const.driveMotorPort));
		this.transmissionMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort(Const.transmissionMotorPort));
		this.steeringMotor = new EV3MediumRegulatedMotor(LocalEV3.get().getPort(Const.steeringWheelMotorPort));
		this.drivingMotor.setAcceleration(Const.motorAcceleration);
		this.transmissionMotor.setAcceleration(Const.MAX_ACCELERATION);
		
//		this.autoTransmission = new AutoTransmission(this);
//		this.transmissionThread = new Thread(this.autoTransmission);
//		this.transmissionThread.start();
//		if(autoTransmission){
//			this.autoTransmission.enable();
//		}else{
//			this.autoTransmission.disable();
//		}
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
		
		if(!test) this.transmissionThread.interrupt();
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

//	public boolean isAutoGear() {
//		return false;
//	}

	public void setAutoGear(boolean b) {
		if(test) return;
		if(b){
			this.autoTransmission.enable();
		}else{
			this.autoTransmission.disable();
		}
	}
	
	public void increaseSpeed(){
		this.currentSpeed += Const.SPEED_INC;
		if(this.currentSpeed > Const.MAX_SPEED){
			this.currentSpeed = Const.MAX_SPEED;
		}
//		System.out.println("speed: " + this.drivingMotor.getRotationSpeed());
		setSpeed(this.currentSpeed);
	
	}
	
	public void decreaseSpeed(){
		this.currentSpeed -= Const.SPEED_DEC;
		this.drivingMotor.setAcceleration(Const.SPEED_DEC_ACC);
		if(this.currentSpeed < 0){
			this.currentSpeed = 0;
		}
//		System.out.println("speed: " + this.drivingMotor.getRotationSpeed());
		setSpeed(this.currentSpeed);
	}
	
	public void setSpeed(int speed){
		this.currentSpeed = speed;
		this.drivingMotor.setSpeed(this.currentSpeed);
		
		if(this.goingForward){
//			System.out.println("forward");
			this.drivingMotor.backward();
		}else{
//			System.out.println("backward");
			this.drivingMotor.forward();
		}
	}
	
	public boolean isGoingForward(){
		return this.goingForward;
	}
	
	public void forward(){
		this.goingForward = true;
		setSpeed(Const.START_SPEED);
	}
	
	public void backward(){
		this.goingForward = false;
		setSpeed(Const.START_SPEED);
	}
	
	public void setSteeringWheel(SteeringMotor state){
		switch(state){
		case LEFT:
			this.steeringMotor.rotateTo(Const.STEERING_LEFT_ANGLE, true);
			break;
		case RIGHT:
			this.steeringMotor.rotateTo(Const.STEERING_RIGHT_ANGLE, true);
			break;
		case STRAIGH:
			this.steeringMotor.rotateTo(Const.STEERING_STRAIGHT, true);
			break;
		}
	}
	
	

//	public void execCommands(DrivingMotor drivingState,
//			SteeringMotor steeringState, GearMotor gearState) {
//		
//		
//	}
	
	

}
