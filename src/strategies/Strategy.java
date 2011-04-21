package strategies;

import java.util.List;

import strategies.enums.Decision;


/**
 * Interface a ser implementada por classes de estratégias para investimentos.
 * @author Bruno Tavares
 */
public interface Strategy {

	/**
	 * Decide se deve {@link Decision comprar, vender ou manter} as ações em carteira
	 * @param cotacoes Dados das cotações registradas
	 * @return {@link Decision decisão}
	 */
	public Decision getDecisao(List<Quote> cotacoes, Object[] strategyState);
}
