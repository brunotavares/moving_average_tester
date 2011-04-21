package strategies;

public class Result {
	private String agentName, stock, strategy;
	private float capital, profitability;
	 
	public Result(String agentName, String stock, float capital, float profitability, String strategy) {
		super();
		this.agentName = agentName;
		this.stock = stock;
		this.capital = capital;
		this.profitability = profitability;
		this.strategy = strategy;
	}
	
	public String getAgentName() {
		return agentName;
	}
	public String getStock() {
		return stock;
	}
	public float getCapital() {
		return capital;
	}
	public float getProfitability() {
		return profitability;
	}
	public String getStrategy() {
		return strategy;
	}
}