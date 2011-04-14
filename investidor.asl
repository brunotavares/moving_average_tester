/** 
 *	TCC - Sistema Multi-Agentes Aplicado à Estratégias do Mercado de Capitais
 *	@author Bruno Tavares
 *	@date 12/04/2011 
 */
+pregao_fechado : true 
	<-	.print("percebi que o pregao fechou, vou consultar as cotacoes a bovespa");
		.send(bovespa, askOne, cotacoes(A)).
		
+cotacoes(A)[source(B)] 
	<- .print("recebi as cotacoes de ",B).
		
		
