/** 
 *	TCC - Sistema Multi-Agentes Aplicado � Estrat�gias do Mercado de Capitais
 *	@author Bruno Tavares
 *	@date 12/04/2011 
 */
//crencas
executando.
//!pregao.

//planos
+!pregao : executando
	<-	.abolish(cotacoes(_));
		actions.abrir_pregao;
		.broadcast(tell,pregao_iniciado);
		pregao_fechado;
		.abolish(ciclo_acabou);
		.broadcast(tell,pregao_finalizado);
		while(not ciclo_acabou){}
		!pregao.
		
-!pregao : true 
	<- .print("finalizando programa");
		finalizar_programa.
	
