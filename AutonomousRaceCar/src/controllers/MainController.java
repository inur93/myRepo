package controllers;

import lejos.hardware.Button;
import remoteController.RemoteReceiver;

public class MainController implements Runnable{
	
	private MotorController motorController;
	private RemoteReceiver remote;
	
	public static void main(String[] args){
		new Thread(new MainController()).start();
	}
	
	public MainController(){
		this.motorController = new MotorController(false);
		this.remote = new RemoteReceiver(this.motorController);
		
	}

	@Override
	public void run() {
		System.out.println("starting...");
		new Thread(this.remote).start();
		System.out.println("ready to go!");
		Button.waitForAnyPress();
		this.motorController.terminate();
		this.remote.interrupt();
	}

}
