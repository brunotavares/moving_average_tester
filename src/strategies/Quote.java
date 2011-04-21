package strategies;

import misc.Config;

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
	
//initializers
	
	public Quote(String stock, String date, String openPrice, String closePrice, String maximumPrice, String minimumPrice) 
	{
		this.stock = stock;
		this.date = date;
		this.openPrice = Float.parseFloat(openPrice);
		this.closePrice = Float.parseFloat(closePrice);
		this.maximumPrice = Float.parseFloat(maximumPrice);
		this.minimumPrice = Float.parseFloat(minimumPrice);
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
	
//other methods
	
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
	
	public static Quote valueOf(String string)
	{
		String[] data = string.split("\\|");
		return new Quote(data[0], data[1], data[2], data[3], data[4], data[5]);
	}
}
