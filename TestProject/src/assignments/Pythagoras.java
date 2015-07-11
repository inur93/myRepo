package assignments;

import Interfaces.IPythagoras;

public class Pythagoras implements IPythagoras {

	public double calcHypotenuse(double a, double b) {
		double c = Math.sqrt(Math.pow(a, 2)+Math.pow(b,2));
		return c;
	}

}
