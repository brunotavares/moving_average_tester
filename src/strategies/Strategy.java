package strategies;

import java.util.List;

import strategies.enums.Decisao;


/**
 * Interface a ser implementada por classes de estratégias para investimentos.
 * @author Bruno Tavares
 */
public interface Strategy {

	/**
	 * Decide se deve {@link Decisao comprar, vender ou manter} as ações em carteira
	 * @param cotacoes Dados das cotações registradas
	 * @return {@link Decisao decisão}
	 */
	public Decisao getDecisao(List<Cotacao> cotacoes);
}
