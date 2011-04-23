/**
 * 
 */
package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import strategies.MovingAverage;
import strategies.Quote;

/**
 * @author Bruno Tavares
 *
 */
public class TestMovingAverageCalculation {

	public static List<Quote> testQuotes;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		testQuotes = new ArrayList<Quote>();
		testQuotes.add(new Quote("TEST1", "01/01/2011", "22.25", "22.25", "22.25", "22.25"));
		testQuotes.add(new Quote("TEST1", "02/01/2011", "22.78", "22.78", "22.78", "22.78"));
		testQuotes.add(new Quote("TEST1", "03/01/2011", "22.64", "22.64", "22.64", "22.64"));
		testQuotes.add(new Quote("TEST1", "04/01/2011", "22.20", "22.20", "22.20", "22.20"));
		testQuotes.add(new Quote("TEST1", "05/01/2011", "21.99", "21.99", "21.99", "21.99"));
		testQuotes.add(new Quote("TEST1", "06/01/2011", "22.31", "22.31", "22.31", "22.31"));
		testQuotes.add(new Quote("TEST1", "07/01/2011", "22.60", "22.60", "22.60", "22.60"));
		testQuotes.add(new Quote("TEST1", "08/01/2011", "22.15", "22.15", "22.15", "22.15"));
		testQuotes.add(new Quote("TEST1", "09/01/2011", "22.24", "22.24", "22.24", "22.24"));
		testQuotes.add(new Quote("TEST1", "10/01/2011", "22.17", "22.17", "22.17", "22.17"));
		testQuotes.add(new Quote("TEST1", "11/01/2011", "22.16", "22.16", "22.16", "22.16"));
		testQuotes.add(new Quote("TEST1", "12/01/2011", "21.80", "21.80", "21.80", "21.80"));
		testQuotes.add(new Quote("TEST1", "13/01/2011", "21.85", "21.85", "21.85", "21.85")); 
		testQuotes.add(new Quote("TEST1", "14/01/2011", "22.16", "22.16", "22.16", "22.16"));
	}
	
	@Test
	public final void testCalculateSimple() 
	{
		//null
		assertEquals(0, MovingAverage.calculateSimple(0, null), 0);
		
		//negative
		assertEquals(0, MovingAverage.calculateSimple(-10, null), 0);
		
		//less quotes than period
		assertEquals(0, MovingAverage.calculateSimple(6, testQuotes.subList(0, 5)), 0);
		
		//other valid values
		assertEquals(22.14, MovingAverage.calculateSimple(10, testQuotes), 0.009);
		assertEquals(22.03, MovingAverage.calculateSimple(5, testQuotes), 0.009);
		assertEquals(22.01, MovingAverage.calculateSimple(2, testQuotes), 0.009);
		
		//same value, test history
		assertEquals(22.01, MovingAverage.calculateSimple(2, testQuotes), 0.009);
	}
	
	@Test
	public final void testCalculateExponencial() 
	{
		//null
		assertEquals(0, MovingAverage.calculateExponential(0, null), 0);
		
		//negative
		assertEquals(0, MovingAverage.calculateExponential(-10, null), 0);
		
		//less quotes than period
		assertEquals(22.30, MovingAverage.calculateExponential(10, testQuotes.subList(0, 5)), 0.009);
		
		//other valid values
		assertEquals(22.12, MovingAverage.calculateExponential(10, testQuotes), 0.009);
		assertEquals(22.05, MovingAverage.calculateExponential(5, testQuotes), 0.009);
		assertEquals(22.06, MovingAverage.calculateExponential(2, testQuotes), 0.009);
		
		//same value, test history
		assertEquals(22.12, MovingAverage.calculateExponential(10, testQuotes), 0.009);
	}
}