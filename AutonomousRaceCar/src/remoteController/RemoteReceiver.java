package remoteController;

import globals.Const;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3IRSensor;
import controllers.MainController;
import controllers.MotorController;

public class RemoteReceiver implements Runnable{

	private enum DrivingStates{FORWARD, REVERSE, BRAKE, FLOAT}
	public enum SteeringMotor{LEFT, RIGHT, STRAIGH}
	public enum GearMotor{SHIFT_UP, SHIFT_DOWN, MANUAL, AUTO}

	private volatile DrivingStates drivingStateCurrent = DrivingStates.BRAKE;
	private volatile SteeringMotor steeringStateCurrent = SteeringMotor.STRAIGH;
	private volatile GearMotor gearStateCurrent = GearMotor.AUTO;

	private volatile SteeringMotor steeringStatePrev = SteeringMotor.STRAIGH;

	private volatile MotorController motorController;
	private volatile MainController mainController;
	private EV3IRSensor receiver;
	private volatile boolean terminate = false;

	public RemoteReceiver(MotorController motorController, MainController mainController) {
		this.motorController = motorController;
		this.mainController = mainController;
		if(!Const.TEST_MODE){
		Port port = LocalEV3.get().getPort(Const.irSensorPort);
		this.receiver = new EV3IRSensor(port);
		}
	}

	@Override
	public void run() {
		
		
		this.terminate = false;
		int channel = -1; 
		int command = 0;
		System.out.println("remote starting...");
		while(!terminate){
			byte[] cmds = new byte[4];
			if(Const.TEST_MODE){
				cmds[0] = (byte) (Math.random()*10);
			}else{
				this.receiver.getRemoteCommands(cmds, 0, cmds.length);
			}
			// 			1	LU 						FO
			//			2	LD						RE
			//			3	RU						LE
			//			4	RD						RI
			//
			//			10 LU LD					NOTHING
			//			11 RU RD					NOTHING
			//			5 LU RU						FO LE
			//			6 LU RD						FO RI
			//			7 LD RU						RE LE
			//			8 LD RD						RE RI
			//			
			// reset values   
			channel = -1;
			command = 0;
			// Figure out which channel is active:     
			for(int i=0; i < 4; i++) 
			{
				if(cmds[i] > 0) 
				{
					channel = i;
					command = cmds[i];
					break;
				}
			}
			if(channel == 0){
				setStandardStates(command);
			}else if(channel == 1 || channel == -1){
				setManualGearState(command);
			}
			sleep(50);
			resetStates();
		}
	}

	private void resetStates() {

		drivingStateCurrent = DrivingStates.FLOAT;
		steeringStateCurrent = SteeringMotor.STRAIGH;
	}

	private void setManualGearState(int command) {
		switch(command){
		case 1:
			gearStateCurrent = GearMotor.SHIFT_UP;
			break;
		case 2:
			gearStateCurrent = GearMotor.SHIFT_DOWN;
			break;
		case 3:
			gearStateCurrent = GearMotor.SHIFT_UP;
			break;
		case 4:
			gearStateCurrent = GearMotor.SHIFT_DOWN;
			break;
		case 9:
			gearStateCurrent = gearStateCurrent.equals(GearMotor.AUTO) ? GearMotor.MANUAL : GearMotor.AUTO;
		default:
			break;
		}

		execCommands();

	}

	private void execCommands() {
		execDrivingCommand();
		execSteeringCommand();
		execGearCommand();

		steeringStatePrev = steeringStateCurrent;
	}

	private void execGearCommand() {
		if(gearStateCurrent.equals(GearMotor.AUTO)){
			this.mainController.setAutoGear(true);
		}else if(gearStateCurrent.equals(GearMotor.MANUAL)){
			this.mainController.setAutoGear(false);
		}
	}

	private void execSteeringCommand() {
		if(steeringStateCurrent.equals(steeringStatePrev)) return;
		else{
			this.motorController.setSteeringWheel(steeringStateCurrent);
		}
	}

	private void execDrivingCommand(){
		if(drivingStateCurrent.equals(DrivingStates.FLOAT)){
			motorController.decreaseSpeed();
		}else if(drivingStateCurrent.equals(DrivingStates.BRAKE)){
			motorController.stop();
		}
		else if(drivingStateCurrent.equals(DrivingStates.FORWARD)){
//			System.out.println("forward: " + motorController.isGoingForward());
//			System.out.println("backward: " + motorController.isGoingBackward());
			if(motorController.isGoingForward()){
				motorController.increaseSpeed();
			}else{
				motorController.forward();
			}
		}else if(drivingStateCurrent.equals(DrivingStates.REVERSE)){
			if(motorController.isGoingBackward()){
				motorController.increaseSpeed();
			}else{
				motorController.backward();
			}
		}
	}

	private void setStandardStates(int command){
		switch(command){
		case 1: //forward
			drivingStateCurrent = DrivingStates.FORWARD;
			break;
		case 2: // reverse
			drivingStateCurrent = DrivingStates.REVERSE;
			break;
		case 3: // turn left
			steeringStateCurrent = SteeringMotor.LEFT;
			break;
		case 4: // turn right
			steeringStateCurrent = SteeringMotor.RIGHT;
			break;
		case 5: // forward and turn left
			drivingStateCurrent = DrivingStates.FORWARD;
			steeringStateCurrent = SteeringMotor.LEFT;
			break;
		case 6: // forward and turn right
			drivingStateCurrent = DrivingStates.FORWARD;
			steeringStateCurrent = SteeringMotor.RIGHT;
			break;
		case 7: // reverse and turn left
			drivingStateCurrent = DrivingStates.REVERSE;
			steeringStateCurrent = SteeringMotor.LEFT;
			break;
		case 8: // reverse and turn right
			drivingStateCurrent = DrivingStates.REVERSE;
			steeringStateCurrent = SteeringMotor.RIGHT;
			break;
		case 9:
			drivingStateCurrent = DrivingStates.BRAKE;
		default:
			break;
		}
		execCommands();
	}

	public void terminate(){
		this.terminate = true;
	}

	private void sleep(long msec){
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {

		}
	}

}
