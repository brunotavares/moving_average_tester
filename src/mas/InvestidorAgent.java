package mas;

import jason.asSemantics.Agent;
import jason.asSyntax.Literal;
import jason.bb.BeliefBase;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import strategies.Cotacao;
import strategies.PriceCrossStrategy;
import strategies.Custodia;
import strategies.Strategy;
import strategies.enums.Decisao;
import strategies.enums.TipoMedia;

import misc.Debug;


public class InvestidorAgent extends BaseAgent {
	
//atributos
	
	private Logger logger = Logger.getLogger("investidor");
	private float capital, capitalInicial, capitalFinal;
	private static int id = 1;
	private HashMap<String, Vector<Cotacao>> cotacoes;
	private HashMap<String, Custodia> custodia;
	private HashMap<String, Strategy> estrategia;

//inits
	
	public InvestidorAgent()
	{
		super();
		this.capital = this.capitalInicial = this.capitalFinal = 10000;
		this.estrategia = new HashMap<String, Strategy>();
		this.custodia = new HashMap<String, Custodia>();
	}
	
//listeners

	public void onInvestir()
	{
		logger.info(Debug.getTime() + " - onInvestir init");
		
	 	BeliefBase bb = this.getBB();
	 	Iterator<Literal> it = bb.iterator();
	 	Literal cotacoesLiteral;
	 	String[] ativos = null;
	 	String[] dadosCotacao = null;
	 	boolean firstTime = this.cotacoes == null;
	 	Vector<Cotacao> vectorCotacao;
	 	Decisao decisao;
	 	Custodia custodiaAtivo;
	 	Strategy estrategia;
	 	
	 	if(this.cotacoes == null){
	 		this.cotacoes = new HashMap<String, Vector<Cotacao>>();
	 	}
	 	
	 	//busca cotaçoes na bb
	 	while(it.hasNext())
	 	{
	 		cotacoesLiteral = it.next();
	 		 
	 		if(cotacoesLiteral.getFunctor().equals("cotacoes"))
	 		{
	 			ativos = cotacoesLiteral.getTerm(0).toString().replace("\"","").split(",");
	 			break;
	 		}
	 	}
	 	
	 	//para cada ativo
	 	if(cotacoes != null)
	 	{
	 		for(int i = 0 ; i < ativos.length ; i++)
	 		{
	 			//tira os dados
	 			dadosCotacao = ativos[i].split("\\|");
	 			
	 			//cria cotacao
	 			Cotacao cotacao = new Cotacao(
	 					dadosCotacao[0], 
	 					dadosCotacao[1], 
	 					dadosCotacao[2], 
	 					dadosCotacao[3], 
	 					dadosCotacao[4], 
	 					dadosCotacao[5]
	             );
	 			
	 			//inicializa array
	 			if(firstTime)
	 			{
	 				vectorCotacao = new Vector<Cotacao>();
	 				custodiaAtivo = new Custodia(dadosCotacao[0], logger.getName());
	 				estrategia = new PriceCrossStrategy(TipoMedia.SIMPLES, 20);
	 				
	 				this.cotacoes.put(dadosCotacao[0], vectorCotacao);
	 				this.custodia.put(dadosCotacao[0], custodiaAtivo);
	 				this.estrategia.put(dadosCotacao[0], estrategia);
	 			}
	 			else
	 			{
	 				vectorCotacao = this.cotacoes.get(dadosCotacao[0]);
	 				custodiaAtivo = this.custodia.get(dadosCotacao[0]);
	 				estrategia = this.estrategia.get(dadosCotacao[0]);
	 			}
	 			
	 			//adiciona
	 			vectorCotacao.add(cotacao);
	 			
	 			//verifica se decisao
	 			logger.info("buscando decisão para " + dadosCotacao[0]);
	 			decisao = estrategia.getDecisao(vectorCotacao);
	 			
	 			custodiaAtivo.setUltimoPrecoFechamento(cotacao.getPrecoFechamento());
	 			
	 			if(decisao == Decisao.COMPRAR)
	 			{
	 				custodiaAtivo.comprar();
	 			}
	 			else if(decisao == Decisao.VENDER)
	 			{
	 				custodiaAtivo.vender();
	 			}
	 			
	 			//atualiza percepcao de capital
	 			this.addBel(Literal.parseLiteral("capital(\""+custodiaAtivo.getAtivo()+"\","+custodiaAtivo.getCapitalFinal()+")"));
	 		}
	 	}
	 	
	 	logger.info(Debug.getTime() + " - onInvestir end");
	}
	
	public void onCalcularCapital()
	{
		Collection<Custodia> custodias = this.custodia.values();
		Iterator<Custodia> it = custodias.iterator();
		Custodia custodia;
		float capitalFinal, diferenca;
		float capitalInicial = 100000;
		
		while(it.hasNext())
		{
			custodia = it.next();
			
			capitalFinal = custodia.getCapitalFinal();
			diferenca = (capitalFinal*100/capitalInicial) - 100;
			
			MercadoEnvironment.getInstance().adicionaResultado(new Object[]{
					this.getTS().getUserAgArch().getAgName(),
					custodia.getAtivo(),
					capitalFinal,
					diferenca
			});
		}
		
		MercadoEnvironment.getInstance().avisaInvestidorTerminou();
	}
		
//acessores
	
	public float getCapital() {
		return capital;
	}

	public float getCapitalInicial() {
		return capitalInicial;
	}

	public float getCapitalFinal() {
		return capitalFinal;
	}
	
//demais metodos
	
	public static String getId()
	{
		return "investidor-" + (id++);	
	}
	
	public static boolean isInvestidor(Agent agente)
	{
		return isInvestidor(agente.getASLSrc());
	}
	
	public static boolean isInvestidor(String nome)
	{
		return nome.equals("investidor.asl");
	}
}