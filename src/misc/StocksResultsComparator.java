package misc;

import java.util.Comparator;

import strategies.Result;

/**
 * Sort results objects [agentName, stock, capital, profitability] by 
 * stock, profitability, and by agent name. 
 */
public class StocksResultsComparator implements Comparator<Result> {

	@Override
	public int compare(Result result1, Result result2) 
	{
		String 	agentName1 = result1.getAgentName(),
				agentName2 = result2.getAgentName(),
				stock1 = result1.getStock(), 
				stock2 = result2.getStock();
		
		float 	profitability1 = result1.getProfitability(),
				profitability2 = result2.getProfitability();
		
		int compareStock= stock1.compareTo(stock2);
		
		//same stock, sort by profitability
		if(compareStock == 0)
		{
			//same profitability, sort by name
			if(profitability1 == profitability2)
			{
				return agentName1.compareTo(agentName2);
			}
			
			return 	profitability1 > profitability2 ? -1 : 1; 
		}
		
		return compareStock;
	}
}