package mas;

import jason.RevisionFailedException;
import jason.asSemantics.Agent;
import jason.asSyntax.Literal;

import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import misc.Config;
import misc.Debug;
import strategies.Custody;
import strategies.Quote;
import strategies.Result;
import strategies.Strategy;
import strategies.enums.Decision;

public class InvestorAgent extends Agent {
	
//attributes
	
	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(InvestorAgent.class.getName());
	
	/**
	 * Stock Quotes (e.g. (Quote) stockQuotes[45][42])
	 */
	private Vector<Quote>[] stockQuotes;
	
	/**
	 * Strategy
	 */
	private Strategy strategy;
	
	/**
	 * State strategy for each stock, managed by strategy instance
	 */
	private Object[][] strategyStates;
	
	/**
	 * Stock's custody
	 */
	private Custody[] custody;
	
	/**
	 * Agent's name
	 */
	private String agentName; 
	
//initializers
	
	@SuppressWarnings("unchecked")
	@Override
	public void initAg() 
	{
		super.initAg();
		
		this.custody = new Custody[Config.STOCKS.length];
		this.strategyStates = new Object[Config.STOCKS.length][];
		this.stockQuotes = new Vector[Config.STOCKS.length];
		this.strategy = (Strategy) (this.getTS().getSettings().getUserParameters().get("strategy"));
		this.agentName = getTS().getUserAgArch().getAgName();
	}
	
//listeners
	
	public void onInvestir()
	{
		logger.info(Debug.getTime() + " - onInvestir init");
		
		Quote[] quotes = this.getQuotesFromBB();
	 	Decision decision;
	 	Vector<Quote> stockQuotes;
	 	
	 	//for each stock
 		for(int i = 0 ; i < Config.STOCKS.length ; i++)
 		{
 			Quote quote = quotes[i];
 			
 			if(this.custody[i] == null){
 				this.custody[i] = new Custody(quote.getStock(), agentName);
 			}
 			
 			if(this.stockQuotes[i] == null){
 				this.stockQuotes[i] = new Vector<Quote>();
 			}
 			
 			if(this.strategyStates[i] == null){
 				this.strategyStates[i] = new Object[10];
 			}
 			
 			stockQuotes = this.stockQuotes[i];
 			stockQuotes.add(quote);
 			
 			//get decision
 			logger.info("buscando decisão para " + quote.getStock());
 			decision = this.strategy.getDecisao(stockQuotes, this.strategyStates[i]);
 			
 			//update close price
 			this.custody[i].setUltimoPrecoFechamento(quote.getClosePrice());
 			
 			//buy or sell
 			if(decision == Decision.BUY)
 			{
 				this.custody[i].comprar();
 			}
 			else if(decision == Decision.SELL)
 			{
 				this.custody[i].vender();
 			}
 			
 			//update capital belief
 			try {
				this.addBel(Literal.parseLiteral("capital(\""+quote.getStock()+"\","+this.custody[i].getCapitalFinal()+")"));
			} 
 			catch (RevisionFailedException e) 
 			{
				e.printStackTrace();
			}
 		}
	 	
	 	logger.info(Debug.getTime() + " - onInvestir end");
	}
	
	public void onCalcularCapital()
	{
		Custody custodia;
		float capitalFinal, diferenca;
		float capitalInicial = 100000;
		
		for(int i = 0 ; i < Config.STOCKS.length ; i++)
		{
			custodia = this.custody[i];
			
			capitalFinal = custodia.getCapitalFinal();
			diferenca = (capitalFinal*100/capitalInicial) - 100;
			
			MercadoEnvironment.getInstance().publishResult(new Result(
				this.getTS().getUserAgArch().getAgName(),
				custodia.getAtivo(),
				capitalFinal,
				diferenca,
				this.strategy.toString()
			));
		}
		
		MercadoEnvironment.getInstance().investorFinished();
	}
		
//other methods
	
	private Quote[] getQuotesFromBB()
	{
	 	Iterator<Literal> it = this.getBB().iterator();
	 	Literal literal;
	 	Quote[] quotes = new Quote[Config.STOCKS.length];
	 	String[] stocks;
	 	
	 	while(it.hasNext())
	 	{
	 		literal = it.next();
	 		 
	 		if(literal.getFunctor().equals("cotacoes"))
	 		{
	 			stocks = literal.getTerm(0).toString().replace("\"","").split(",");
	 			
	 			for(int i = 0 ; i < stocks.length ; i++){
	 				quotes[i] = Quote.valueOf(stocks[i]);
	 			}
	 		}
	 	}
	 	
	 	return quotes;
	}
	
	public static boolean isInvestor(Agent agente)
	{
		return isInvestor(agente.getASLSrc());
	}
	
	public static boolean isInvestor(String nome)
	{
		return nome.equals("investor.asl");
	}
}