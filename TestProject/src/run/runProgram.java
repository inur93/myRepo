package run;
import java.util.Scanner;

import assignments.Pythagoras;


public class runProgram {

	public static void main(String[] args){
	Pythagoras pyth = new Pythagoras();
	Scanner scan = new Scanner(System.in);
	boolean success = false;
	double a = 0;
	double b = 0;
	while(true){
	do{
		System.out.println("a:");
		String aValue = scan.nextLine();
		
		try{
			a = Double.valueOf(aValue);
			success = true;
		}catch(NumberFormatException nfe){
			success = false;
		}
	}while(!success);
	
	do{
	System.out.println("b:");
	String bValue = scan.nextLine();
	try{
		b = Double.valueOf(bValue);
		success = true;
	}catch(NumberFormatException nfe){
		success = false;
	}
	}while(!success);
	
	double c = pyth.calcHypotenuse(a, b);
	System.out.println("c=" + c);
	System.out.println("quit? (y/n)");
	String quitValue = scan.nextLine();
	if(quitValue.equalsIgnoreCase("y")){
		break;
	}
	}
	
	scan.close();
}
}
