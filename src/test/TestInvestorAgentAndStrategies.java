package test;

import static org.junit.Assert.assertEquals;
import jason.JasonException;
import jason.architecture.AgArch;
import jason.asSemantics.Circumstance;
import jason.asSemantics.TransitionSystem;
import jason.asSyntax.Literal;
import jason.runtime.Settings;
import mas.BovespaAgent;
import mas.InvestorAgent;
import misc.Config;

import org.junit.BeforeClass;
import org.junit.Test;

import strategies.AveragesCrossStrategy;
import strategies.Custody;
import strategies.PriceCrossStrategy;
import strategies.Quote;
import strategies.enums.AverageKind;

public class TestInvestorAgentAndStrategies {
	
	public static BovespaAgent bovespaAgent;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		Config.QUOTES_FOLDER = "test-quotes";
		
		//setup bovespa and quotes
		bovespaAgent = new BovespaAgent();
		bovespaAgent.initAg("bovespa.asl");
	}
	
	@Test
	public void testInvestPriceCross() throws JasonException
	{
		//setup investor and strategy
		Settings settings = new Settings();
		settings.addOption("strategy", new PriceCrossStrategy(AverageKind.SIMPLE, 5));
		
		InvestorAgent investor = new InvestorAgent();
		new TransitionSystem(investor, new Circumstance(), settings, new AgArch());
		investor.initAg("investor.asl");
		
		Object[][] quotes = bovespaAgent.getStockQuotes();
		
		//run quotes
		for(int i = 0 ; i < Config.QUOTES_QUANTITY ; i++)
		{
			Quote quote = (Quote) quotes[0][i];
			
			investor.addBel(Literal.parseLiteral("cotacoes(\""+quote.toString()+"\")"));
			investor.onInvestir();
			investor.delBel(Literal.parseLiteral("cotacoes(_)"));
		}
		
		Custody custody = investor.getCustody()[0];
		assertEquals(444410.00, custody.getCapitalFinal(), 0.009);
	}
	
	@Test
	public void testInvestAverageCross() throws JasonException
	{
		//setup investor and strategy
		Settings settings = new Settings();
		settings.addOption("strategy", new AveragesCrossStrategy(AverageKind.SIMPLE, AverageKind.SIMPLE, 10, 5));
		
		InvestorAgent investor = new InvestorAgent();
		new TransitionSystem(investor, new Circumstance(), settings, new AgArch());
		investor.initAg("investor.asl");
		
		Object[][] quotes = bovespaAgent.getStockQuotes();
		
		//run quotes
		for(int i = 0 ; i < Config.QUOTES_QUANTITY ; i++)
		{
			Quote quote = (Quote) quotes[0][i];
			
			investor.addBel(Literal.parseLiteral("cotacoes(\""+quote.toString()+"\")"));
			investor.onInvestir();
			investor.delBel(Literal.parseLiteral("cotacoes(_)"));
		}
		
		Custody custody = investor.getCustody()[0];
		assertEquals(9275.00, custody.getCapitalFinal(), 0.009);
	}
}