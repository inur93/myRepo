package controllers;

import java.util.Scanner;

import globals.Const;
import lejos.hardware.Button;
import remoteController.RemoteReceiver;

public class MainController implements Runnable{

	private volatile MotorController motorCtrl;
	private volatile AutoTransmission autoTransmission;
	private volatile RemoteReceiver remote;

	public static void main(String[] args){
		new Thread(new MainController()).start();
	}

	public MainController(){

		this.motorCtrl = new MotorController();
		this.autoTransmission = new AutoTransmission(this.motorCtrl);
		new Thread(this.autoTransmission).start();
		
		this.remote = new RemoteReceiver(this.motorCtrl, this.autoTransmission);
		new Thread(this.remote).start();

	}

	@Override
	public void run() {
		System.out.println("starting...");
		System.out.println("ready to go!");
		if(!Const.TEST_MODE){
		Button.waitForAnyPress();
		}else{
			Scanner s = new Scanner(System.in);
			String msg = s.nextLine();
			System.out.println("quitting: " + msg);
			s.close();
		}
		System.out.println("terminating...");
		this.remote.terminate();
		this.autoTransmission.disable();
		System.exit(0);

	}

}
