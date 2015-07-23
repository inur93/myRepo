package remoteController;

import globals.Const;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3IRSensor;
import controllers.MotorController;

public class RemoteReceiver implements Runnable{

	//	public enum RemoteEvent{FORWARD, REVERSE, TURN_LEFT, TURN_RIGHT}
	public enum DrivingMotor{FORWARD, REVERSE, STOP}
	public enum SteeringMotor{LEFT, RIGHT, STRAIGH}
	public enum GearMotor{SHIFT_UP, SHIFT_DOWN, MANUAL, AUTO}

	private volatile DrivingMotor drivingStateCurrent = DrivingMotor.STOP;
	private volatile SteeringMotor steeringStateCurrent = SteeringMotor.STRAIGH;
	private volatile GearMotor gearStateCurrent = GearMotor.AUTO;

	private volatile DrivingMotor drivingStatePrev = DrivingMotor.STOP;
	private volatile SteeringMotor steeringStatePrev = SteeringMotor.STRAIGH;
	private volatile GearMotor gearStatePrev = GearMotor.AUTO;

	private MotorController motorController;
	private EV3IRSensor receiver;
	private volatile boolean stop = false;

	public RemoteReceiver(MotorController motorController) {
		this.motorController = motorController;
		Port port = LocalEV3.get().getPort(Const.irSensorPort);
		this.receiver = new EV3IRSensor(port);
	}

	@Override
	public void run() {
		this.stop = false;
		int channel = -1; 
		int command = 0;
		while(!stop){
			byte[] cmds = new byte[4];
			this.receiver.getRemoteCommands(cmds, 0, cmds.length);
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
	};

	private void resetStates() {

		drivingStateCurrent = DrivingMotor.STOP;
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
			gearStateCurrent = gearStateCurrent.SHIFT_DOWN;
			break;
		case 9:
			gearStateCurrent = gearStateCurrent.equals(GearMotor.AUTO) ? GearMotor.MANUAL : GearMotor.AUTO;
		default:
			break;
		}

		execCommands();

	}

	private void execCommands() {
		//		motorController.execCommands(drivingStateCurrent, steeringStateCurrent, gearStateCurrent);

		execDrivingCommand();
		execSteeringCommand();
		execGearCommand();

		drivingStatePrev = drivingStateCurrent;
		steeringStatePrev = steeringStateCurrent;
		gearStatePrev = gearStateCurrent;
	}

	private void execGearCommand() {
		if(gearStateCurrent.equals(GearMotor.AUTO)){
			this.motorController.setAutoGear(true);
		}else if(gearStateCurrent.equals(GearMotor.MANUAL)){
			this.motorController.setAutoGear(false);
		}else{

		}
	}

	private void execSteeringCommand() {
		if(steeringStateCurrent.equals(steeringStatePrev)) return;
		else{
			this.motorController.setSteeringWheel(steeringStateCurrent);
		}
	}

	private void execDrivingCommand(){
		if(drivingStateCurrent.equals(DrivingMotor.STOP)){
			motorController.decreaseSpeed();
		}else{
			if(drivingStatePrev.equals(DrivingMotor.STOP)){
				motorController.increaseSpeed();
			}else if(drivingStateCurrent.equals(DrivingMotor.FORWARD)){
				if(motorController.isGoingForward()){
					motorController.increaseSpeed();
				}else{
					motorController.forward();
				}
			}else if(drivingStateCurrent.equals(DrivingMotor.REVERSE)){
				if(!motorController.isGoingForward()){
					motorController.increaseSpeed();
				}else{
					motorController.backward();
				}
			}
		}
	}

	private void setStandardStates(int command){
		switch(command){
		case 1: //forward
			drivingStateCurrent = DrivingMotor.FORWARD;
			break;
		case 2: // reverse
			drivingStateCurrent = DrivingMotor.REVERSE;
			break;
		case 3: // turn left
			steeringStateCurrent = SteeringMotor.LEFT;
			break;
		case 4: // turn right
			steeringStateCurrent = SteeringMotor.RIGHT;
			break;
		case 5: // forward and turn left
			drivingStateCurrent = DrivingMotor.FORWARD;
			steeringStateCurrent = SteeringMotor.LEFT;
			break;
		case 6: // forward and turn right
			drivingStateCurrent = DrivingMotor.FORWARD;
			steeringStateCurrent = SteeringMotor.RIGHT;
			break;
		case 7: // reverse and turn left
			drivingStateCurrent = DrivingMotor.REVERSE;
			steeringStateCurrent = SteeringMotor.LEFT;
			break;
		case 8: // reverse and turn right
			drivingStateCurrent = DrivingMotor.REVERSE;
			steeringStateCurrent = SteeringMotor.RIGHT;
			break;
		default:
			break;
		}
		execCommands();
	}

	public synchronized void interrupt(){
		this.stop = true;
	}

	private void sleep(long msec){
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {

		}
	}

}
