// Internal action code for project mercadoCapitais.mas2j

package actions;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;
import mas.BovespaAgent;
import misc.Debug;

@SuppressWarnings("serial")
public class abrir_pregao extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
    	if(BovespaAgent.isBovespa(ts.getAg()))
    	{
    		BovespaAgent agente = (BovespaAgent) ts.getAg();
    		
    		if(!agente.isFinished())
    		{
    			ts.getAg().getLogger().info("===================== " + Debug.getTime() + " - abrir_pregao action init =====================");
    			agente.onAbrirPregao();
    			ts.getAg().getLogger().info(Debug.getTime() + " - abrir_pregao action end");
    			
    			return true;
    		}
    	}
    	
    	return false;
    }
}