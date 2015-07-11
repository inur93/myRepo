package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import assignments.Pythagoras;
import Interfaces.IPythagoras;

public class PythagorasTest {
	
	private IPythagoras pythagoras;
	
	@Before
	public void init(){
		this.pythagoras = new Pythagoras();
	}

	@Test
	public void testCalcHypotenuse(){
		double result = this.pythagoras.calcHypotenuse(3, 4);
		Assert.assertSame("a=3, b=4", 5, result);
	}
}
