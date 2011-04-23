package strategies;

import java.util.HashMap;


/**
 * Classe que representa uma cotação diária de determinado ativo.
 * @author Bruno Tavares
 */
public class Quote {

//attributes
	
	/**
	 * Preço de Abertura
	 */
	private float openPrice;
	
	/**
	 * Preço de Abertura
	 */
	private float closePrice; 
	
	/**
	 * Preço Máximo
	 */
	private float maximumPrice;
	
	/**
	 * Preço de Mínimo
	 */
	private float minimumPrice; 
	
	/**
	 * Data
	 */
	private String date;
	
	/**
	 * Código do ativo
	 */
	private String stock;
	
	/**
	 * Arithmetical averages values for this quote in different periods
	 */
	private HashMap<Integer, Float> arithmeticalAverage;
	
	/**
	 * Exponetial averages values for this quote in different periods
	 */
	private HashMap<Integer, Float> exponentialAverage;
	
//initializers
	
	public Quote(String stock, String date, String openPrice, String closePrice, String maximumPrice, String minimumPrice) 
	{
		this.stock = stock;
		this.date = date;
		this.openPrice = Float.parseFloat(openPrice);
		this.closePrice = Float.parseFloat(closePrice);
		this.maximumPrice = Float.parseFloat(maximumPrice);
		this.minimumPrice = Float.parseFloat(minimumPrice);
		this.arithmeticalAverage = new HashMap<Integer, Float>();
		this.exponentialAverage = new HashMap<Integer, Float>();
	}
	
//assessors	
	
	public String getStock() {
		return stock;
	}
	public String getDate() {
		return date;
	}
	public float getOpenPrice() {
		return openPrice;
	}
	public float getClosePrice() {
		return closePrice;
	}
	public float getMaximumPrice() {
		return maximumPrice;
	}
	public float getMinimumPrice() {
		return minimumPrice;
	}

//overrides
	
	@Override
	public String toString() 
	{
		return 	this.stock + "|" +
				this.getDate() + "|" +
				this.getOpenPrice() + "|" +
				this.getClosePrice() + "|" +
				this.getMaximumPrice() + "|" +
				this.getMinimumPrice();
	}
	
//other methods
	
	public float getArithmeticalAverage(int period) {
		return arithmeticalAverage.containsKey(period) ? arithmeticalAverage.get(period) : 0f;
	}

	public float getExponentialAverage(int period) {
		return exponentialAverage.containsKey(period) ? exponentialAverage.get(period) : 0f;
	}
	
	public void setArithmeticalAverage(int period, float value){
		arithmeticalAverage.put(period, value);
	}
	
	public void setExponentialAverage(int period, float value){
		exponentialAverage.put(period, value);
	}
	
	public static Quote valueOf(String string)
	{
		String[] data = string.split("\\|");
		return new Quote(data[0], data[1], data[2], data[3], data[4], data[5]);
	}
}
