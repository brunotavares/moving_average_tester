package strategies;

import java.util.List;
import java.util.logging.Logger;

import strategies.enums.Decisao;
import strategies.enums.PosicaoMedia;
import strategies.enums.TipoMedia;


/**
 * Estratégia de cruzamento de média móvel com preços. Dada uma média móvel de N períodos
 * caso o preço de fechamento diário cruze a média de baixo para cima, é gerado sinal de compra.
 * Caso o preço cruze de cima para baixo é gerado sinal de venda.
 * 
 * @author Bruno Tavares
 */
public class PriceCrossStrategy implements Strategy {

	/**
	 * Período para cálculo da média
	 */
	private int period;
	
	/**
	 * O tipo da média
	 */
	private TipoMedia averageKind;
	
	/**
	 * Posição da média resultante da última decisão
	 */
	private PosicaoMedia position;

	private Logger logger;
	
//inicializadores
	
	public PriceCrossStrategy(TipoMedia averageKind, int period)
	{
		this.period = period;
		this.averageKind = averageKind;
	}
	
//demais métodos
	
	@Override
	public Decisao getDecisao(List<Cotacao> cotacoes) 
	{
		float ultimoFechamento = cotacoes.size() != 0 ? 
				cotacoes.get(cotacoes.size()-1).getPrecoFechamento() : 0f;
		
		float mediaMovel = this.calcularMedia(cotacoes);
		
		logger.info("getDecisao posicao="+this.position+", ultimoFechamento="+ultimoFechamento+", mediaMovel="+mediaMovel);
		
		if(this.position == PosicaoMedia.ABAIXO && mediaMovel > ultimoFechamento )
		{
			this.position = PosicaoMedia.ACIMA;
			return Decisao.COMPRAR;
		}
		else if(this.position == PosicaoMedia.ACIMA && mediaMovel < ultimoFechamento)
		{
			this.position = PosicaoMedia.ABAIXO;
			return Decisao.VENDER;
		}
		else if(this.position == null)
		{
			this.position = mediaMovel > ultimoFechamento ? PosicaoMedia.ACIMA : PosicaoMedia.ABAIXO;
		}
		
		return Decisao.MANTER;
	}
	
	/**
	 * Calcula a média dependendo do tipo (simples ou exponencial)
	 * @param cotacoes
	 * @return valor da média
	 */
	private float calcularMedia(List<Cotacao> cotacoes)
	{
		return this.averageKind == TipoMedia.SIMPLES ? 
				MediaMovel.calcular(this.period, cotacoes) :
				MediaMovel.calcularExponencial(this.period, cotacoes);
	}
}
