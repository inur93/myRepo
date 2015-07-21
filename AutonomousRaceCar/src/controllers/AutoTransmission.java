package controllers;

import globals.Const;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.EV3ColorSensor;

public class AutoTransmission implements Runnable{


	private EV3ColorSensor colorSensor;
	private MotorController motorController;

	public AutoTransmission(MotorController motorController){
		this.motorController = motorController;
//		this.colorSensor = new EV3ColorSensor(LocalEV3.get().getPort(Const.colorSensorPort));
	}
	@Override
	public void run() {
		while(true){
			System.out.println("current gear: " + this.motorController.getCurrentGear());
			if(Button.readButtons() == Button.ID_DOWN){
				break;
			}
			while(this.motorController.getRPM() < Const.MAX_SPEED){}
			if(this.motorController.getCurrentGear() < 3){
				this.motorController.shiftUp(true);
			}
			
		}
	}

	private int findGear(){
		this.colorSensor.getColorID();
		return 0;
	}

	private void sleep(long millisec){
		try {
			Thread.sleep(millisec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
