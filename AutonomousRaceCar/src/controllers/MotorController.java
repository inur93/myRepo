package controllers;

import remoteController.RemoteReceiver.SteeringMotor;
import globals.Const;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;

public class MotorController {

	private volatile EV3LargeRegulatedMotor drivingMotor;
	private volatile EV3LargeRegulatedMotor transmissionMotor;
	private volatile EV3MediumRegulatedMotor steeringMotor;
	private int currentSpeed;

	public enum DrivingMotorStates{FORWARD, REVERSE, BRAKE}
	private volatile DrivingMotorStates drivingState;

	public MotorController(){
		this.drivingState = DrivingMotorStates.BRAKE;
		if(!Const.TEST_MODE){
			this.drivingMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort(Const.driveMotorPort));
			this.transmissionMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort(Const.transmissionMotorPort));
			this.steeringMotor = new EV3MediumRegulatedMotor(LocalEV3.get().getPort(Const.steeringWheelMotorPort));
			//		this.drivingMotor.setAcceleration(Const.motorAcceleration);
			this.transmissionMotor.setAcceleration(Const.MAX_ACCELERATION);
		}

	}



	public void shiftUp(int angle){
		Const.SPEED_INC = 0;
		if(Const.TEST_MODE){
			System.out.println("shiftUp rotateto: " + angle);
		}else{
			this.transmissionMotor.rotateTo(angle/2, false);
			setSpeed(Const.MAX_SPEED/2);
			Const.SPEED_INC = Const.SPEED_INC_DEFAULT;
			this.transmissionMotor.rotateTo(angle, false);
		}
	}

	public void shiftDown(int angle){
		Const.SPEED_DEC = 0;
		if(Const.TEST_MODE){
			System.out.println("shiftDown rotateto: " + angle);
		}else{
			this.transmissionMotor.rotate(angle/2, false);
			setSpeed(Const.MAX_SPEED/2);
			Const.SPEED_DEC = Const.SPEED_DEC_DEFAULT;
			this.transmissionMotor.rotateTo(angle, false);
		}		
	}

	public int getRPM(){
		return this.currentSpeed;
	}

	public void increaseSpeed(){
		this.currentSpeed += Const.SPEED_INC;
		//		System.out.println("currentrpm: " + this.currentSpeed);
		if(this.currentSpeed > Const.MAX_SPEED){
			this.currentSpeed = Const.MAX_SPEED;
		}
		setSpeed(this.currentSpeed);

	}

	public void decreaseSpeed(){
		this.currentSpeed -= Const.SPEED_DEC;
		//		if(!Const.TEST_MODE){
		//		this.drivingMotor.setAcceleration(Const.SPEED_DEC_ACC);
		//		}
//		System.out.println("decreasing..." + this.currentSpeed);
		if(this.currentSpeed < 0){
			this.currentSpeed = 0;
		}
		setSpeed(this.currentSpeed);
	}

	public void forward(){
		this.drivingState = DrivingMotorStates.FORWARD;
		setSpeed(Const.START_SPEED);
	}

	public void backward(){
		this.drivingState = DrivingMotorStates.REVERSE;
		setSpeed(Const.START_SPEED);
	}

	public void stop(){
		this.drivingState = DrivingMotorStates.BRAKE;
		setSpeed(0);
	}

	public void setSpeed(int speed){
		this.currentSpeed = speed;
		if(Const.TEST_MODE){
			System.out.println("set speed: " + this.currentSpeed);
		}else{
			this.drivingMotor.setSpeed(this.currentSpeed);
		}
		switch(this.drivingState){
		case FORWARD:
			if(Const.TEST_MODE){
				System.out.println("driving forward");
			}else{
				this.drivingMotor.backward();
			}
			break;
		case REVERSE:
			if(Const.TEST_MODE){
				System.out.println("driving reverse");
			}else{
				this.drivingMotor.forward();
			}
			break;
		case BRAKE:
			if(Const.TEST_MODE){
				System.out.println("driving stopped");
			}else{
				this.drivingMotor.stop(true);
			}
			break;
		}
//		System.out.println("speed: " + this.currentSpeed);
	}

	public boolean isGoingForward(){
		if(this.drivingState.equals(DrivingMotorStates.FORWARD)) return true;
		else return false;
	}

	public boolean isGoingBackward(){
		if(this.drivingState.equals(DrivingMotorStates.REVERSE)) return true;
		else return false;
	}

	private void sleep(long msec){
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
		}
	}


	public void setSteeringWheel(SteeringMotor state){
		int angle = 0;
		switch(state){
		case LEFT:
			angle = Const.STEERING_LEFT_ANGLE;
			break;
		case RIGHT:
			angle = Const.STEERING_RIGHT_ANGLE;
			break;
		case STRAIGH:
			angle = Const.STEERING_STRAIGHT;
			break;
		}
		if(Const.TEST_MODE){
			System.out.println("steering angle: " + angle);
		}else{
			this.steeringMotor.rotateTo(angle, true);
		}
	}
}
