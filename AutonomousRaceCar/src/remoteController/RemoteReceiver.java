package remoteController;

import globals.Const;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3IRSensor;
import controllers.AutoTransmission;
import controllers.MotorController;

public class RemoteReceiver implements Runnable{

	private enum DrivingStates{FORWARD, REVERSE, BRAKE, FLOAT}
	public enum SteeringMotor{LEFT, RIGHT, STRAIGH}
	public enum GearState{MANUAL, AUTO}
	public enum GearMotorState{SHIFT_UP, SHIFT_DOWN}

	private volatile DrivingStates drivingStateCurrent = DrivingStates.BRAKE;
	private volatile SteeringMotor steeringStateCurrent = SteeringMotor.STRAIGH;
	private volatile GearState gearStateCurrent = GearState.AUTO;

//	private volatile SteeringMotor steeringStatePrev = SteeringMotor.STRAIGH;

	private volatile MotorController motorController;
	private volatile AutoTransmission transmission;
	private EV3IRSensor receiver;
	private volatile boolean terminate = false;
	private boolean buttonActive = false;

	public RemoteReceiver(MotorController motorController, AutoTransmission transmission) {
		this.motorController = motorController;
		this.transmission = transmission;
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
			if(channel == Const.DRIVING_CHANNEL || channel == -1){
				setStandardStates(command);
			}
			if(channel == Const.GEAR_CHANNEL || channel == -1){
				setManualGearState(command);
			}
			if(channel == 3 && command == 9){
				System.exit(0);
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
		GearMotorState gearCommand = null;
		
		switch(command){
		case 1:
			gearCommand = GearMotorState.SHIFT_UP;
			break;
		case 2:
			gearCommand = GearMotorState.SHIFT_DOWN;
			break;
		case 3:
			gearCommand = GearMotorState.SHIFT_UP;
			break;
		case 4:
			gearCommand = GearMotorState.SHIFT_DOWN;
			break;
		case 9:
			if(buttonActive) break;
			
			
			gearStateCurrent = gearStateCurrent.equals(GearState.AUTO) ? GearState.MANUAL : GearState.AUTO;
			if(gearStateCurrent.equals(GearState.AUTO)){
				System.out.println("auto enabled");
				this.transmission.enable();
			}else if(gearStateCurrent.equals(GearState.MANUAL)){
				System.out.println("auto disabled");
				this.transmission.disable();
			}
			buttonActive = true;
			break;
		default:
			buttonActive = false;
			break;
		}
		
//		System.out.println("toggle auto gear: " + buttonActive);

		if(gearCommand != null) execGearCommand(gearCommand);

	}

	private void execCommands() {
		execDrivingCommand();
		execSteeringCommand();

//		steeringStatePrev = steeringStateCurrent;
	}

	private void execGearCommand(GearMotorState command) {
		switch(command){
		case SHIFT_DOWN:
			this.transmission.shiftDown();
			break;
		case SHIFT_UP:
			this.transmission.shiftUp();
			break;
		}
	}

	private void execSteeringCommand() {
//		if(steeringStateCurrent.equals(steeringStatePrev)) return;
//		else{
			this.motorController.setSteeringWheel(steeringStateCurrent);
//		}
	}

	private void execDrivingCommand(){
		if(drivingStateCurrent.equals(DrivingStates.FLOAT)){
			motorController.decreaseSpeed();
		}else if(drivingStateCurrent.equals(DrivingStates.BRAKE)){
			motorController.stop();
		}
		else if(drivingStateCurrent.equals(DrivingStates.FORWARD)){
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
