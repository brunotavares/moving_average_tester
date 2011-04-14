/** 
 *	TCC - Sistema Multi-Agentes Aplicado à Estratégias do Mercado de Capitais
 *	@author Bruno Tavares
 *	@date 12/04/2011 
 */
!pregao.

+!pregao
	<-	.print("abri o pregao");
		abertura_pregao;
		.print("aguardei 6 horas");
		.wait(1000);
		.print("fechei o pregao");
		fechar_pregao;
		after_market.
