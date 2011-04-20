/** 
 *	TCC - Sistema Multi-Agentes Aplicado à Estratégias do Mercado de Capitais
 *	@author Bruno Tavares
 *	@date 12/04/2011 
 */
+cotacoes(A)[source(B)] 
	<- 	.abolish(capital(C,D));
		actions.investir;
		investimento_finalizado.

+pregao_iniciado : true
	<- 	.abolish(pregao_finalizado);
		.abolish(cotacoes(_)).
	
+pregao_finalizado : true
	<- .abolish(pregao_iniciado);
		.send(bovespa, askOne, cotacoes(A)).
		
+programa_finalizado : true
	<- actions.calcular_capital.
