package mas;

import jason.RevisionFailedException;
import jason.asSemantics.Agent;
import jason.asSyntax.Literal;

public class BaseAgent extends Agent {

//overrides
	
	@Override
	public boolean delBel(Literal bel) 
	{
		try{
			return super.delBel(bel);
		}
		catch (RevisionFailedException e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean addBel(Literal bel)
	{
		try{
			return super.addBel(bel);
		}
		catch (RevisionFailedException e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
}