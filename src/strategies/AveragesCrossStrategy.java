package strategies;

import java.util.List;
import java.util.logging.Logger;

import strategies.enums.Decision;
import strategies.enums.AveragePosition;
import strategies.enums.AverageKind;

/**
 * Strategy of cross averages. Given two distinct averages, one slow and another fast,
 * buy and sell signals are generated when they cross each other. 
 * @author Bruno Tavares
 */
public class AveragesCrossStrategy implements Strategy {

	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(AveragesCrossStrategy.class.getName());
	
	/**
	 * Period for calculate slow average value
	 */
	private int periodSlow;
	
	/**
	 * Period for calculate fast average value
	 */
	private int periodFast;
	
	/**
	 * Slow Average kind
	 */
	private AverageKind averageKindSlow;
	
	/**
	 * Fast Average kind
	 */
	private AverageKind averageKindFast;
	
//initializers
	
	public AveragesCrossStrategy(AverageKind averageKindSlow, AverageKind averageKindFast, int periodSlow, int periodFast)
	{
		this.periodSlow = periodSlow;
		this.periodFast = periodFast;
		this.averageKindSlow = averageKindSlow;
		this.averageKindFast = averageKindFast;
	}
	
//overrides
	
	@Override
	public Decision getDecision(List<Quote> quotes, Object[] strategyState) 
	{
		float 	lastClosePrice = quotes.size() != 0 ? quotes.get(quotes.size()-1).getClosePrice() : 0f,
				slowAverage = MovingAverage.calculate(this.averageKindSlow, this.periodSlow, quotes),
				fastAverage = MovingAverage.calculate(this.averageKindFast, this.periodFast, quotes);
		
		AveragePosition fastPosition = (AveragePosition) strategyState[0];
		
		logger.info("getDecisao posicao="+fastPosition+", ultimoFechamento="+lastClosePrice+", mediaMovelRapida="+fastAverage+" ,mediaMovelLenta="+slowAverage);
			
		if(fastAverage != 0 && slowAverage != 0)
		{
			if(fastPosition == AveragePosition.UNDER && fastAverage > slowAverage )
			{
				strategyState[0] = AveragePosition.ABOVE;
				return Decision.BUY;
			}
			else if(fastPosition == AveragePosition.ABOVE && fastAverage <= slowAverage)
			{
				strategyState[0] = AveragePosition.UNDER;
				return Decision.SELL;
			}
			else if(fastPosition == null)
			{
				strategyState[0] = fastAverage > slowAverage ? AveragePosition.ABOVE : AveragePosition.UNDER;
			}
		}
		
		return Decision.KEEP;
	}

	@Override
	public String toString() 
	{
		return "Cruzamento de Médias " + this.averageKindFast.toString() + " N("+this.periodFast+") " + this.averageKindSlow.toString() + " N("+this.periodSlow+")";
	}
}