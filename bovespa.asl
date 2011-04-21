/** 
 *	TCC - Sistema Multi-Agentes Aplicado à Estratégias do Mercado de Capitais
 *	@author Bruno Tavares
 *	@date 12/04/2011 
 */
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

-!pregao : true.

//percepcoes
+executando : true <- !pregao.

-executando : true 
	<- .print("finalizando programa");
		finalizar_programa.
