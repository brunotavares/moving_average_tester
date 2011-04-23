package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import jason.JasonException;
import jason.asSemantics.Agent;
import jason.asSyntax.Literal;

import java.util.Iterator;

import mas.BovespaAgent;
import misc.Config;

import org.junit.BeforeClass;
import org.junit.Test;

import strategies.Quote;

public class TestBovespaAgent {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		Config.QUOTES_FOLDER = "test-quotes";
	}
	
	@Test
	public void testInitQuotes()
	{
		BovespaAgent agent = new BovespaAgent();
		assertNull(agent.getStockQuotes());
		
		agent.initAg();
		Object[][] quotes = agent.getStockQuotes();
		Quote quote = (Quote) quotes[0][0];
		
		assertEquals(quote.getStock(),"TEST1");
		assertEquals(quote.getClosePrice(),50,0f);
	}
	
	@Test
	public void testAbrirPregao() throws JasonException
	{
		BovespaAgent agentBovespa = new BovespaAgent();
		agentBovespa.initAg("bovespa.asl");
		
		Literal cotacao = getBeliefFromAgent(agentBovespa, "cotacoes");
		assertNull(cotacao);
		
		agentBovespa.onAbrirPregao();
		cotacao = getBeliefFromAgent(agentBovespa, "cotacoes");
		assertEquals(cotacao.getTerm(0).toString(),"\"TEST1|1/1/2011|5.0|50.0|5.0|5.0\"");
		
		agentBovespa.onAbrirPregao();
		agentBovespa.onAbrirPregao();
		agentBovespa.onAbrirPregao();
		cotacao = getBeliefFromAgent(agentBovespa, "cotacoes");
		assertEquals(cotacao.getTerm(0).toString(),"\"TEST1|4/1/2011|5.0|20.0|5.0|5.0\"");
	}
	
	@Test
	public void testIsBovespa() throws JasonException
	{
		BovespaAgent agentBovespa = new BovespaAgent();
		agentBovespa.initAg("bovespa.asl");
		
		assertFalse(BovespaAgent.isBovespa(new Agent()));
		assertFalse(BovespaAgent.isBovespa("notBovespa"));
		assertTrue(BovespaAgent.isBovespa(agentBovespa));
		assertTrue(BovespaAgent.isBovespa("bovespa"));
	}
	
	private static Literal getBeliefFromAgent(Agent agent, String belief)
	{
		Iterator<Literal> it = agent.getBB().iterator();
	 	Literal literal;
	 	
	 	while(it.hasNext())
	 	{
	 		literal = it.next();
	 		 
	 		if(literal.getFunctor().equals(belief))
	 		{
	 			return literal;
	 		}
	 	}
	 	
	 	return null;
	}
	
}