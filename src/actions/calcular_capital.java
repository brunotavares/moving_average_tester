// Internal action code for project mercadoCapitais.mas2j

package actions;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;
import mas.InvestorAgent;
import misc.Debug;

@SuppressWarnings("serial")
public class calcular_capital extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
    {
    	if(InvestorAgent.isInvestor(ts.getAg()))
    	{
    		ts.getAg().getLogger().info(Debug.getTime() + " - calcular_capital action init");
    		
    		InvestorAgent agente = (InvestorAgent) ts.getAg();
    		agente.onCalcularCapital();
    		
    		ts.getAg().getLogger().info(Debug.getTime() + " - calcular_capital action end");
    		return true;
    	}
    	
    	return false;
    }
}