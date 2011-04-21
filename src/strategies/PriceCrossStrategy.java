package strategies;

import java.util.List;
import java.util.logging.Logger;

import strategies.enums.Decision;
import strategies.enums.AveragePosition;
import strategies.enums.AverageKind;


/**
 * Estratégia de cruzamento de média móvel com preços. Dada uma média móvel de N períodos
 * caso o preço de fechamento diário cruze a média de baixo para cima, é gerado sinal de compra.
 * Caso o preço cruze de cima para baixo é gerado sinal de venda.
 * 
 * @author Bruno Tavares
 */
public class PriceCrossStrategy implements Strategy {

	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(PriceCrossStrategy.class.getName());
	
	/**
	 * Período para cálculo da média
	 */
	private int period;
	
	/**
	 * O tipo da média
	 */
	private AverageKind averageKind;
	
//initializers
	
	public PriceCrossStrategy(AverageKind averageKind, int period)
	{
		this.period = period;
		this.averageKind = averageKind;
	}
	
//overrides
	
	@Override
	public Decision getDecisao(List<Quote> cotacoes, Object[] strategyState) 
	{
		float ultimoFechamento = cotacoes.size() != 0 ? cotacoes.get(cotacoes.size()-1).getClosePrice() : 0f;
		float mediaMovel = this.calcularMedia(cotacoes);
		AveragePosition position = (AveragePosition) strategyState[0];
		
		logger.info("getDecisao posicao="+position+", ultimoFechamento="+ultimoFechamento+", mediaMovel="+mediaMovel);
		
		if(position == AveragePosition.UNDER && mediaMovel > ultimoFechamento )
		{
			strategyState[0] = AveragePosition.ABOVE;
			return Decision.BUY;
		}
		else if(position == AveragePosition.ABOVE && mediaMovel < ultimoFechamento)
		{
			strategyState[0] = AveragePosition.UNDER;
			return Decision.SELL;
		}
		else if(position == null)
		{
			strategyState[0] = mediaMovel > ultimoFechamento ? AveragePosition.ABOVE : AveragePosition.UNDER;
		}
		              
		return Decision.KEEP;
	}

	@Override
	public String toString() 
	{
		return "Cruzamento de Preços " + this.averageKind.toString() + " N("+this.period+")";
	}
	
//other methods
	
	/**
	 * Calcula a média dependendo do tipo (simples ou exponencial)
	 * @param cotacoes
	 * @return valor da média
	 */
	private float calcularMedia(List<Quote> cotacoes)
	{
		return this.averageKind == AverageKind.SIMPLE ? 
				MediaMovel.calcular(this.period, cotacoes) :
				MediaMovel.calcularExponencial(this.period, cotacoes);
	}
}
