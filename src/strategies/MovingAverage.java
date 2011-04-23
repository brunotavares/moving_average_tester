package strategies;

import java.util.List;

import strategies.enums.AverageKind;

/**
 * Encapsula lógica para cálculo de média móveis simples e exponenciais.
 * @author Bruno Tavares
 */
public abstract class MovingAverage {

//métodos
	
	public static float calculate(AverageKind averageKind, int periodo, List<Quote> cotacoes)
	{
		return 	averageKind == AverageKind.SIMPLE ? calculateSimple(periodo, cotacoes) :
				averageKind == AverageKind.EXPONENCIAL ? calculateExponential(periodo, cotacoes) : 0;
	}
	
	/**
	 * Calcula o valor de uma média móvel dada uma lista de cotações e um período.
	 * @return Valor da média. Caso a lista não seja suficiente (período da média é maior 
	 * que quantidade de elementos) é retornado 0.00.
	 */
	public static float calculateSimple(int period, List<Quote> quotes)
	{
		if(quotes == null || period <= 0 || quotes.size() < period){
			return 0f;
		}
		
		Quote quote = quotes.get(quotes.size()-1); 
		
		//try to get previous value
		float average = quote.getArithmeticalAverage(period);
		
		//otherwise, calculate
		if(average == 0)
		{
			float sum = 0f;
		
			for(int i = quotes.size() - 1, l = 0 ; i != -1 && l < period ; l++, i-- )
			{
				sum += quotes.get(i).getClosePrice();
			}
			
			quote.setArithmeticalAverage(period, average = sum/period);
		}
		
		return average;
	}
	
	/**
	 * Especialização de Média Móvel regida por MMEx = ME(x-1) + K x {Fech(x) – ME(x-1)} <ul>
	 * <li>MMEx representa a média móvel exponencial no dia x</li>
	 * <li>ME(x-1) representa a média móvel exponencial no dia x-1</li>
	 * <li>N é o número de dias para os quais se quer o cálculo</li>
	 * <li>K é constante = {2 ÷ (N+1)}</li></ul>
	 * @return Valor da média móvel exponencial
	 */
	public static float calculateExponential(int period, List<Quote> quotes) 
	{
		if(quotes == null || period <= 0){
			return 0f;
		}
		
		return getExponentialAverage(quotes.size()-1, period, quotes, 2f / (period+1f));
	}

	private static float getExponentialAverage(int index, int period, List<Quote> quotes, float k)
	{
		//get quote
		Quote quote = quotes.get(index);
		
		//try to get exponential average
		float average = quote.getExponentialAverage(period);
		
		//otherwise, calculate
		if(average == 0)
		{
			if(index == 0 || quotes.get(index - 1) == null) //if quote is the first, return the price
			{
				average = quote.getClosePrice();
			}
			else
			{
				float previousAverage = getExponentialAverage(index-1, period, quotes, k);
				average = (quote.getClosePrice() - previousAverage) * k + previousAverage;
			}
			
			quote.setExponentialAverage(period, average);
		}
		
		return average;
	}
}