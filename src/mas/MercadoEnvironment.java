package mas;

import gui.Frame;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import jason.runtime.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import misc.Config;
import misc.Debug;
import strategies.Result;
import strategies.Strategy;

public class MercadoEnvironment extends Environment {

	private Logger logger = Logger.getLogger(MercadoEnvironment.class.getName());
	private int signals;
	private List<Result> results;
	private static MercadoEnvironment instance;
	private Frame frame;
	private int investorID;
	
//overrides
	
	@Override
	public void init(String[] args) 
	{
		//super
		super.init(args);
		
		//attributes
		this.results = new ArrayList<Result>();
		this.signals = 0;
		this.investorID = 1;
		instance = this;
		
		//configs
		Config.INVESTORS_QUANTITY = Integer.valueOf(args[0]);

		//gui
		this.frame = new Frame();
	}
	
    @Override
    public boolean executeAction(String agName, Structure actionTerm) 
    {
    	String action = actionTerm.getFunctor();
    	
    	this.logger.info(Debug.getTime() + " - "+ action +" action");
    	
    	if (action.equals("pregao_fechado")) 
    	{
    		this.assignSignals(this.getEnvironmentInfraTier().getRuntimeServices().getAgentsQty() - 1);
    		this.logger.info(Debug.getTime() + " - investidores atuando " + this.signals);
    		return true;
    	}
    	
    	if(action.equals("investimento_finalizado"))
    	{
    		this.releaseInvestFinished();
    		return true;
    	}
    	
    	if(action.equals("finalizar_programa"))
    	{
    		this.assignSignals(this.getEnvironmentInfraTier().getRuntimeServices().getAgentsQty() - 1);
    		this.addPercept(Literal.parseLiteral("programa_finalizado"));
    		return true;
    	}
    	
    	return false;
    }

//acessors
    
    public List<Result> getResults() {
		return results;
	}
    
//other methods
    
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
					"investor" + (this.investorID++), 	// agent name
					"investor.asl",       				// AgentSpeak source
					"mas.InvestorAgent",            	// default agent class
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
    
    public void initAuction()
    {
    	this.addPercept("bovespa", Literal.parseLiteral("executando"));
    }
    
    private synchronized void assignSignals(int signals) 
    {
      this.signals = signals;
      this.notify();
    }

    public synchronized void releaseInvestFinished()
    {
    	if(this.signals > 0)
    	{
    		this.logger.info(Debug.getTime() + " - -1 investidor atuando " + this.signals);
    		this.signals--;
    	}
    	
    	if(this.signals == 0)
    	{
    		this.logger.info(Debug.getTime() + " - investidores_finalizados");
    		Literal cycle_finished = Literal.parseLiteral("ciclo_acabou");
    		this.removePercept(cycle_finished);
    		this.addPercept(cycle_finished);
    	}
    }
    
    public synchronized void releaseExecutionFinished()
    {
    	if(this.signals > 0)
    	{
    		this.logger.info(Debug.getTime() + " - -1 investidor atuando " + this.signals);
    		this.signals--;
    	}
    	
    	if(this.signals == 0)
    	{
    		logger.info("resultados prontos");
    		this.frame.onResultsPublished();
    	}
    }

    public synchronized void publishResult(Result result)
    {
    	results.add(result);
    }
    
    public synchronized void investorFinished()
    {
    	this.releaseExecutionFinished();
    }
    
    public static MercadoEnvironment getInstance()
    {
    	return instance;
    }
}