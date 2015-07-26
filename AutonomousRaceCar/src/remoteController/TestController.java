package remoteController;

import globals.Const;

import java.awt.Event;
import java.util.ArrayList;

import controllers.MotorController;
import lejos.ev3.tools.EV3Control;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.internal.ev3.EV3Battery;
import lejos.remote.ev3.EV3Request;

public class TestController implements Runnable{
	
	private EV3IRSensor remoteController;
	private MotorController motorController;
	
	public TestController(EV3IRSensor remoteController, MotorController motorController){
		this.remoteController = remoteController;
		this.motorController = motorController;
	}
	public TestController(){
		this.remoteController = new  EV3IRSensor(LocalEV3.get().getPort(Const.irSensorPort));
	}
	
	public static void main(String[] args){
		new TestController().run();
	}

	@Override
	public void run() {
		EV3LargeRegulatedMotor gearMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort(Const.transmissionMotorPort));
		EV3LargeRegulatedMotor driveMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort(Const.driveMotorPort));
		EV3ColorSensor colorSensor = new EV3ColorSensor(LocalEV3.get().getPort(Const.colorSensorPort));
		int speed = 100;
//		float[] sample = new float[this.remoteController.getSeekMode().sampleSize()];
		while(true){
//		this.remoteController.fetchSample(sample, 0);
//			this.remoteController.setCurrentMode("Seek");
//			System.out.println("current mode: " + this.remoteController.getCurrentMode());
//			System.out.println("mode count: " + this.remoteController.getModeCount());
//			ArrayList<String> modes = this.remoteController.getAvailableModes();
//			for(String s : modes){
//				System.out.println(s);
//			}
//			System.out.println("name: " + this.remoteController.getName());
//			System.out.println("seekmode: " + this.remoteController.getSeekMode().sampleSize());
//			System.out.println("sample size: " + this.remoteController.sampleSize());
			
//			int loop = 0;
//			while(loop < 10){
//			byte[] cmds = new byte[4];
//			this.remoteController.getRemoteCommands(cmds, 0, cmds.length);
//			
//			
//		     
//		      int channel = -1;
//		         
//		      // Figure out which channel is active:
//		      int command = 0;
//		      for(int i=0; i < 4; i++) 
//		      {
//		         if(cmds[i] > 0) 
//		         {
//		            channel = i;
//		            command = cmds[i];
//		         }
//		      }
//		      
//		      System.out.println("IRSignal: channel: " + channel + " move:" + command);
//		      try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		      loop++;
//		}
//		   remoteController.close();
//			this.remoteController.fetchSample(sample, 0);
//			EV3Control control = new EV3Control();
//			EV3Request req = new EV3Request();
////			req.
//			int count = 0;
//			for(float f : sample){
//				System.out.print(f + " ");
//				count++;
//				if(count >= 4){
//					count = 0;
//					System.out.println();
//				}
//			}
			
//			motor.setAcceleration(200);
//			motor.forward();
		driveMotor.forward();
		Button.waitForAnyPress();
		if(Button.readButtons() == Button.ID_DOWN){
//			motor.setAcceleration(-200);
//			sleep(4000);
//			motor.rotate(540, false);
			driveMotor.setSpeed(speed);
			speed += 100;
		}
		if(Button.readButtons() == Button.ID_UP){
//			motor.setAcceleration(-200);
//			sleep(4000);
//			gearMotor.rotate(-540, false);
			driveMotor.setSpeed((float)100.0);
		}
		
		if(Button.readButtons() == Button.ID_ESCAPE){
			break;
		}
		float[] sample = new float[colorSensor.sampleSize()];
		colorSensor.fetchSample(sample, 0);
		for(float f : sample){
			System.out.print(f + ",");
		}
		}
	}
	
	private void sleep(long msec){
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
