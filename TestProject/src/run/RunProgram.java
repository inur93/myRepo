package run;
import java.util.Scanner;

import Interfaces.IPythagoras;
import assignments.Pythagoras;


public class RunProgram {

	public static void main(String[] args){
		Pythagoras pyth = new Pythagoras();
		RunProgram prog = new RunProgram();
		Scanner scan = new Scanner(System.in);

		prog.runCalcHypotenuse(pyth, scan);

		scan.close();
	}
	private void runCalcBaseline(IPythagoras pyth, Scanner scan){
		boolean success = false;
		double c = 0;
		double b = 0;
		while(true){
			// her biðji eg um c
			do{
				System.out.print("c:");
				String cValue = scan.nextLine();
				try{
					c= Double.valueOf(cValue);
					success = true;
				}catch(NumberFormatException eg){
					success = false;
				}
			}while(!success);

			do{
				System.out.print("b:");
				String bValue = scan.nextLine();
				try{
					b = Double.valueOf(bValue);
					success = true;
							
				}catch(NumberFormatException eg){
					success = false;
				}
				}while (!success);
				double a = pyth.calcBaseline(c, b);
				System.out.println("a=" +a);
				System.out.println("quit?? (y/n)");
				String quitValue = scan.nextLine();
				if(quitValue.equalsIgnoreCase("y")){
					break;
			}


		}
	}
	private void runCalcHypotenuse(IPythagoras pyth, Scanner scan){
		boolean success = false;
		double a = 0;
		double b = 0;
		while(true){
			do{
				System.out.print("a:");

				String aValue = scan.nextLine();

				try{
					a = Double.valueOf(aValue);
					success = true;
				}catch(NumberFormatException nfe){
					success = false;
				}
			}while(!success);

			do{
				System.out.print("b:");
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
	}
}
