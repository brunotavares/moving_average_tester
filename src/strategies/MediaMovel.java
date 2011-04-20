package strategies;

import java.util.List;

/**
 * Encapsula lógica para cálculo de média móveis simples e exponenciais.
 * @author Bruno Tavares
 */
public abstract class MediaMovel {

//métodos
	
	/**
	 * Calcula o valor de uma média móvel dada uma lista de cotações e um período.
	 * @return Valor da média. Caso a lista não seja suficiente (período da média é maior 
	 * que quantidade de elementos) é retornado 0.00.
	 */
	public static float calcular(int periodo, List<Cotacao> cotacoes)
	{
		if(cotacoes.size() < periodo){
			return 0f;
		}
		
		float soma = 0f;
		
		for(int i = cotacoes.size() - 1, l = 0 ; i != -1 && l < periodo ; l++, i-- )
		{
			soma += cotacoes.get(i).getPrecoFechamento();
		}
		
		return soma/periodo;
	}
	
	/**
	 * Especialização de Média Móvel regida por MMEx = ME(x-1) + K x {Fech(x) – ME(x-1)} <ul>
	 * <li>MMEx representa a média móvel exponencial no dia x</li>
	 * <li>ME(x-1) representa a média móvel exponencial no dia x-1</li>
	 * <li>N é o número de dias para os quais se quer o cálculo</li>
	 * <li>K é constante = {2 ÷ (N+1)}</li></ul>
	 * @return Valor da média móvel exponencial
	 */
	public static float calcularExponencial(int periodo, List<Cotacao> cotacoes) 
	{
		float k = 2 / (periodo/1);
		
		float media = 0, mediaAnterior = 0;
		int i;
		
		if(cotacoes.size() < periodo)
		{
			i = 0;
		}else{
			i = cotacoes.size() - periodo;
		}
		
		for(int l = cotacoes.size(); i < l ; i++)
		{
			if(mediaAnterior == 0)
			{
				media = mediaAnterior = cotacoes.get(i).getPrecoFechamento();
			}
			else
			{
				media = mediaAnterior + k * (cotacoes.get(i).getPrecoFechamento() - mediaAnterior);
			}
		}
		
		return media;
	}
}