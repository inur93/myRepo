package controllers;

import globals.Const;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.EV3ColorSensor;

public class AutoTransmission implements Runnable{


	private EV3ColorSensor colorSensor;
	private volatile MotorController motorController;

	private volatile int currentGear;
	private volatile int prevGear; // for test
	private volatile boolean isRunning = true;

	public AutoTransmission(MotorController motorController){
		this.motorController = motorController;
		if(!Const.TEST_MODE){
			this.colorSensor = new EV3ColorSensor(LocalEV3.get().getPort(Const.colorSensorPort));
		}
	}
	@Override
	public synchronized void run() {
		initTransmission();
		int speed = 0;
		System.out.println("transmission started.");
		while(isRunning){
			prevGear = currentGear;
			do{
				speed = this.motorController.getRPM();
				if(Const.TEST_MODE) {
					System.out.println("transmission sleeping.." + isRunning);
				}
				sleep(100);
			}
			while(isRunning && speed < Const.MAX_SPEED && speed > Const.MIN_SPEED);

			if(!isRunning) break;

			if(speed >= Const.MAX_SPEED && currentGear < 3){
				shiftUp();

			}else if(speed <= Const.MIN_SPEED && currentGear > 1){
				shiftDown();
			}
			if(this.currentGear != this.prevGear){
				System.out.println("current gear: " + currentGear);
			}
		}
		System.out.println("auto transmission stopped.");
	}

	public void shiftDown() {
		this.currentGear--;
		switch(currentGear){
		case 1:
			this.motorController.shiftDown(Const.FIRST_GEAR);
			break;
		case 2:
			this.motorController.shiftDown(Const.SECOND_GEAR);
			break;
		case 3:
			this.motorController.shiftDown(Const.THIRD_GEAR);
			break;
		default:
			System.out.println("gear not recognized: " + currentGear);
			break;
		}
	}
	public void shiftUp() {
		this.currentGear++;
		switch(currentGear){
		case 1:
			this.motorController.shiftUp(Const.FIRST_GEAR);
			break;
		case 2:
			this.motorController.shiftUp(Const.SECOND_GEAR);
			break;
		case 3:
			this.motorController.shiftUp(Const.THIRD_GEAR);
			break;
		default:
			System.out.println("gear not recognized: " + currentGear);
			break;
		}		
	}
	private void initTransmission() {
		if(Const.TEST_MODE){
			this.currentGear = 2;
			return;
		}
		int gear_id = this.colorSensor.getColorID();
		switch(gear_id){
		case Const.FIRST_GEAR_COLOR_ID:
			Const.FIRST_GEAR = Const.GEAR_ANGLE_OFFSET_0;
			Const.SECOND_GEAR = Const.GEAR_ANGLE_OFFSET_1;
			Const.THIRD_GEAR = Const.GEAR_ANGLE_OFFSET_2;
			this.currentGear = 1;
			break;
		case Const.SECOND_GEAR_COLOR_ID:
			Const.FIRST_GEAR = Const.GEAR_ANGLE_OFFSET_N1;
			Const.SECOND_GEAR = Const.GEAR_ANGLE_OFFSET_0;
			Const.THIRD_GEAR = Const.GEAR_ANGLE_OFFSET_1;
			this.currentGear = 2;
			break;
		case Const.THIRD_GEAR_COLOR_ID:
			Const.FIRST_GEAR = Const.GEAR_ANGLE_OFFSET_N2;
			Const.SECOND_GEAR = Const.GEAR_ANGLE_OFFSET_N1;
			Const.THIRD_GEAR = Const.GEAR_ANGLE_OFFSET_0;
			this.currentGear = 3;
			break;
		default:
			System.out.println("unknown color id");
			break;
		}
	}

	public int getCurrentGear(){
		return this.currentGear;
	}

	private void sleep(long millisec){
		try {
			Thread.sleep(millisec);
		} catch (InterruptedException e) {
		}
	}

	public void enable(){
		System.out.println("isrunning: " + isRunning);
		if(this.isRunning) return;
	
//		System.out.println("enabling...");
		this.isRunning = true;
		new Thread(this).start();
	}

	public void disable(){
		System.out.println("isrunning: " + isRunning);
		if(!this.isRunning) return;
		
//		System.out.println("disabling...");
		this.isRunning = false;
	}
}
