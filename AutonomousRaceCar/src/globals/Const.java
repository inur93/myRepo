package globals;

public class Const {

	public static final String irSensorPort = "S3";
	public static final String colorSensorPort = "S1";
	public static final String steeringWheelMotorPort = "A";
	public static final String driveMotorPort = "B";
	public static final String transmissionMotorPort = "C";

	
	public static final int motorAcceleration = 50;
	public static final int MAX_ACCELERATION = 6000;
	public static final int MAX_SPEED = 720;
	public static final int MIN_SPEED = 50;
	public static final int MOTOR_STOP_TIME = 3600;
	
	public static final int START_SPEED = MIN_SPEED+10;
	public static final int SPEED_DEC = 10;
	public static final int SPEED_INC = 20;
	
	public static final int SPEED_DEC_ACC = 300;
	
	public static final int STEERING_LEFT_ANGLE = 70;
	public static final int STEERING_RIGHT_ANGLE = -70;
	public static final int STEERING_STRAIGHT = 0;
	
	public static int FIRST_GEAR = -540;
	public static int SECOND_GEAR = 0;
	public static int THIRD_GEAR = 540;
	
	public static final int GEAR_ANGLE_OFFSET = 580;
	public static final int GEAR_ANGLE_OFFSET_N2 = 2*GEAR_ANGLE_OFFSET;
	public static final int GEAR_ANGLE_OFFSET_N1 = GEAR_ANGLE_OFFSET;
	public static final int GEAR_ANGLE_OFFSET_0 = 0;
	public static final int GEAR_ANGLE_OFFSET_1 = -GEAR_ANGLE_OFFSET;
	public static final int GEAR_ANGLE_OFFSET_2 = -2*GEAR_ANGLE_OFFSET;
	
	public static final int FIRST_GEAR_COLOR_ID = 0;
	public static final int SECOND_GEAR_COLOR_ID = 13;
	public static final int THIRD_GEAR_COLOR_ID = 7;
	
	public static final boolean TEST_MODE = false;
	
	// gear 540 degrees
	
	// gear1:0, gear2:13, gear3:7
}
