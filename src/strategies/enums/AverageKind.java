package strategies.enums;

public enum AverageKind {
	SIMPLE, EXPONENCIAL;
	
	@Override
	public String toString() 
	{
		if (this == SIMPLE) 
			return "Simples";
		else 
			return "Exponencial";
	}
}