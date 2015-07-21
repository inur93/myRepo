package controllers;

import java.util.ArrayList;

import lejos.ev3.tools.EV3Control;
import lejos.hardware.Button;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.remote.ev3.EV3Request;

public class RemoteController implements Runnable{
	
	private EV3IRSensor remoteController;
	private MotorController motorController;
	
	public RemoteController(EV3IRSensor remoteController, MotorController motorController){
		this.remoteController = remoteController;
		this.motorController = motorController;
	}

	@Override
	public void run() {
		float[] sample = new float[this.remoteController.getSeekMode().sampleSize()];
		while(true){
//		this.remoteController.fetchSample(sample, 0);
			this.remoteController.setCurrentMode("Seek");
//			System.out.println("current mode: " + this.remoteController.getCurrentMode());
//			System.out.println("mode count: " + this.remoteController.getModeCount());
//			ArrayList<String> modes = this.remoteController.getAvailableModes();
//			for(String s : modes){
//				System.out.println(s);
//			}
//			System.out.println("name: " + this.remoteController.getName());
//			System.out.println("seekmode: " + this.remoteController.getSeekMode().sampleSize());
//			System.out.println("sample size: " + this.remoteController.sampleSize());
	
			this.remoteController.fetchSample(sample, 0);
			EV3Control control = new EV3Control();
			EV3Request req = new EV3Request();
//			req.
			int count = 0;
			for(float f : sample){
				System.out.print(f + " ");
				count++;
				if(count >= 4){
					count = 0;
					System.out.println();
				}
			}
			
		Button.waitForAnyPress();
		if(Button.readButtons() == Button.ID_ESCAPE){
			break;
		}
		}
	}

}
