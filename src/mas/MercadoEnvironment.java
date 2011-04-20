package mas;
// Environment code for project mercadoCapitais.mas2j

import gui.Frame;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import jason.runtime.Settings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import misc.Config;
import misc.Debug;
import strategies.Strategy;


public class MercadoEnvironment extends Environment {

	private Logger logger = Logger.getLogger("mercado");
	private int quantidadeInvestidores = 0;
	private int signals = 0;
	private List<Object[]> resultados = new ArrayList<Object[]>();
	private static MercadoEnvironment instance;
	private Frame frame;
	
//acessores
	
	public void setQuantidadeInvestidores(int quantidadeInvestidores) {
		this.quantidadeInvestidores = quantidadeInvestidores;
	}
	
//overrides
	
	@Override
	public void init(String[] args) 
	{
		//super
		super.init(args);
		
		//singleton instance
		instance = this;
		
		//configs
		Config.INVESTORS_QUANTITY = Integer.valueOf(args[0]);

		//gui
		this.frame = new Frame(this);
	}
	
    @Override
    public boolean executeAction(String agName, Structure actionTerm) 
    {
    	String action = actionTerm.getFunctor();
    	
    	this.logger.info(Debug.getTime() + " - "+ action +" action");
    	
    	if (action.equals("pregao_fechado")) 
    	{
    		this.take(this.getEnvironmentInfraTier().getRuntimeServices().getAgentsQty() - 1);
    		this.logger.info(Debug.getTime() + " - investidores atuando " + this.signals);
    		return true;
    	}
    	
    	if(action.equals("investimento_finalizado"))
    	{
    		this.release();
    		return true;
    	}
    	
    	if(action.equals("finalizar_programa"))
    	{
    		this.take(this.getEnvironmentInfraTier().getRuntimeServices().getAgentsQty() - 1);
    		this.addPercept(Literal.parseLiteral("programa_finalizado"));
    		return true;
    	}
    	
    	return false;
    }

//listeners

    public void onFinalizarPrograma()
    {
    	//busca todos agentes
    	Set<String> nomesAgentes = this.getEnvironmentInfraTier().getRuntimeServices().getAgentsNames();
    	Iterator<String> it = nomesAgentes.iterator();
    	String nomeAgente, ativo;
    	List<Literal> percepcoes;
    	Iterator<Literal> itp;
    	Literal percepcao;
    	String[] capitalAtivo;
    	float capital;
    	List<Object[]> resultados = new ArrayList<Object[]>();
    	
    	while(it.hasNext())
    	{
    		//guarda nome
    		nomeAgente = it.next();
    		
    		//busca percepcoes
    		percepcoes = this.getPercepts(nomeAgente);
    		itp = percepcoes.iterator();
    		
    		while(itp.hasNext())
    		{
    			percepcao = itp.next();
    			
    			if(percepcao.getFunctor().equals("capital"))
    			{
    				capitalAtivo = percepcao.getTerm(0).toString().replace("\"","").split(",");
    				ativo = capitalAtivo[0];
    				capital = Float.parseFloat(capitalAtivo[1]);
    				
    				resultados.add(new Object[]{
    						nomeAgente,
    						ativo,
    						capital
    				});
    			}
    		}
    	}
    }
    
//demais métodos
    
    public void incrementaInvestidores() 
    {
		this.quantidadeInvestidores++;
	}
    
    public synchronized void take(int signals) 
    {
      this.signals = signals;
      this.notify();
    }

    public synchronized void release()
    {
    	if(this.signals > 0)
    	{
    		this.logger.info(Debug.getTime() + " - -1 investidor atuando " + this.signals);
    		this.signals--;
    	}
    	
    	if(this.signals == 0)
    	{
    		this.logger.info(Debug.getTime() + " - investidores_finalizados");
    		Literal ciclo_acabou = Literal.parseLiteral("ciclo_acabou");
    		this.removePercept(ciclo_acabou);
    		this.addPercept(ciclo_acabou);
			return;
    	}
    }
    
    public synchronized void releaseFinal()
    {
    	if(this.signals > 0)
    	{
    		this.logger.info(Debug.getTime() + " - -1 investidor atuando " + this.signals);
    		this.signals--;
    	}
    	
    	if(this.signals == 0)
    	{
    		logger.info("resultados prontos");
    	}
    }

    public synchronized void adicionaResultado(Object[] resultado)
    {
    	resultados.add(resultado);
    }
    
    public synchronized void avisaInvestidorTerminou()
    {
    	this.releaseFinal();
    }
    
    public static MercadoEnvironment getInstance()
    {
    	return instance;
    }
    private int investorID = 1;
    
	public void createInvestor(Strategy strategy) 
	{
		if(strategy == null){
			return;
		}
		
		Settings settings = new Settings();
		settings.addOption("strategy", strategy);
		
		try
		{
			this.getEnvironmentInfraTier().getRuntimeServices().createAgent(
					"investidor" + (this.investorID++), // agent name
					"investidor.asl",       			// AgentSpeak source
					null,            					// default agent class
					null,            					// default architecture class
					null,            					// default belief base parameters
					settings
			);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}