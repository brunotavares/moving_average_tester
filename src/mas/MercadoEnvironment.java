package src.mas;
// Environment code for project mercadoCapitais.mas2j

import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;

import java.util.logging.Logger;

import src.gui.Frame;

public class MercadoEnvironment extends Environment {

	private Logger logger = Logger.getLogger("mercado");

	public MercadoEnvironment()
	{
		//cria GUI
		new Frame();
	}
	
//overrides

    @Override
    public boolean executeAction(String agName, Structure actionTerm) 
    {
    	String action = actionTerm.getFunctor();
    	
    	if (action.equals("fechar_pregao") && BovespaAgent.isBovespa(agName)) 
    	{
    		logger.info("publiquei percepcao pregao_fechado");
    		this.addPercept(Literal.parseLiteral("pregao_fechado"));
    	}
    	
    	return true;
    }
}
