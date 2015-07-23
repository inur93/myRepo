package controllers;

import globals.Const;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.EV3ColorSensor;

public class AutoTransmission implements Runnable{


	private EV3ColorSensor colorSensor;
	private MotorController motorController;
	private volatile boolean isRunning = true;

	public AutoTransmission(MotorController motorController){
		this.motorController = motorController;
		this.colorSensor = new EV3ColorSensor(LocalEV3.get().getPort(Const.colorSensorPort));
	}
	@Override
	public void run() {
		while(isRunning){
			
			while(this.motorController.getRPM() < Const.MAX_SPEED){}
			if(this.motorController.getCurrentGear() < 3){
				this.motorController.shiftUp(true);
			}
			
		}
	}

	private int findGear(){
		return this.colorSensor.getColorID();
	}

	private void sleep(long millisec){
		try {
			Thread.sleep(millisec);
		} catch (InterruptedException e) {
		}
	}
	
	public void enable(){
		isRunning = true;
		run();
	}
	
	public void disable(){
		isRunning = false;
	}
}
