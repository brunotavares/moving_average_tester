package strategies;

import java.util.List;
import java.util.logging.Logger;

import strategies.enums.AverageKind;
import strategies.enums.Decision;

/**
 * Strategy of cross averages. Given two distinct averages, one slow and another fast,
 * buy and sell signals are generated when they cross each other. 
 * @author Bruno Tavares
 */
public class TripleCrossStrategy implements Strategy {

	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(TripleCrossStrategy.class.getName());
	
	/**
	 * Period for calculate slow average value
	 */
	private int periodSlow;
	
	/**
	 * Period for calculate fast average value
	 */
	private int periodFast;
	
	/**
	 * Period for calculate normal average value
	 */
	private int periodNormal;
	
	/**
	 * Slow Average kind
	 */
	private AverageKind averageKindSlow;
	
	/**
	 * Normal Average kind
	 */
	private AverageKind averageKindNormal;
	
	/**
	 * Fast Average kind
	 */
	private AverageKind averageKindFast;
	
//initializers
	
	public TripleCrossStrategy(AverageKind averageKindFast, int periodFast, AverageKind averageKindNormal, int periodNormal, AverageKind averageKindSlow, int periodSlow)
	{
		this.periodSlow = periodSlow;
		this.periodFast = periodFast;
		this.periodNormal = periodNormal;
		this.averageKindSlow = averageKindSlow;
		this.averageKindFast = averageKindFast;
		this.averageKindNormal = averageKindNormal;
	}
	
//overrides
	
	@Override
	public Decision getDecision(List<Quote> quotes, Object[] strategyState) 
	{
		float 	lastClosePrice = quotes.size() != 0 ? quotes.get(quotes.size()-1).getClosePrice() : 0f,
				slowAverage 	= MovingAverage.calculate(this.averageKindSlow, this.periodSlow, quotes),
				normalAverage 	= MovingAverage.calculate(this.averageKindNormal, this.periodNormal, quotes),
				fastAverage 	= MovingAverage.calculate(this.averageKindFast, this.periodFast, quotes);
		
		Decision status = (Decision) strategyState[0];
		
		logger.info("getDecisao ultimoFechamento="+lastClosePrice+", mediaMovelRapida="+fastAverage+" ,mediaMovelLenta="+slowAverage);
		
		if(fastAverage != 0 && normalAverage != 0 && slowAverage != 0)
		{
			if(fastAverage > normalAverage && normalAverage > slowAverage && status != Decision.BUY)
			{
				strategyState[0] = Decision.BUY;
				return Decision.BUY;
			}
			else if(fastAverage < normalAverage && normalAverage < slowAverage && status != Decision.SELL)
			{
				strategyState[0] = Decision.SELL;
				return Decision.SELL;
			}
		}           
		return Decision.KEEP;
	}

	@Override
	public String toString() 
	{
		return "Cruzamento Triplo " + 
			this.averageKindFast.toString() + " N("+this.periodFast+") " + 
			this.averageKindNormal.toString() + " N("+this.periodNormal+") "+
			this.averageKindSlow.toString() + " N("+this.periodSlow+")";
	}
}