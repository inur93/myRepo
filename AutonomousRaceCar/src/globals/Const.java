package globals;

import lejos.hardware.motor.Motor;

public class Const {

	public static final String irSensorPort = "S3";
	public static final String colorSensorPort = "S1";
	public static final String steeringWheelMotorPort = "A";
	public static final String driveMotorPort = "B";
	public static final String transmissionMotorPort = "C";

	
	public static final int motorAcceleration = 50;
	public static final int MAX_ACCELERATION = 6000;
	public static final int MAX_SPEED = 540;
	public static final int MOTOR_STOP_TIME = 3600;
	
	public static final int START_SPEED = 30;
	public static final int SPEED_DEC = 200;
	public static final int SPEED_INC = 10;
	
	public static final int SPEED_DEC_ACC = 300;
	
	public static final int STEERING_LEFT_ANGLE = 70;
	public static final int STEERING_RIGHT_ANGLE = -70;
	public static final int STEERING_STRAIGHT = 0;
	
	// gear 540 degrees
	
	// gear1:0, gear2:13, gear3:7
}
