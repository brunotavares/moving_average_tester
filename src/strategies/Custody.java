package strategies;

import java.util.logging.Logger;

public class Custody {

	private String ativo;
	private float capitalInicial;
	private float capital;
	private int quantidadeAcoes;
	private Logger logger;
	private float ultimoPrecoFechamento;
	
	public Custody(String ativo, String agentName) 
	{
		super();
		this.ativo = ativo;
		this.capitalInicial = this.capital = 100000;
		this.logger = Logger.getLogger(agentName);
		this.ultimoPrecoFechamento = 0f;
	}
	
	public String getAtivo() {
		return ativo;
	}
	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}
	public float getCapitalInicial() {
		return capitalInicial;
	}
	public void setCapitalInicial(float capitalInicial) {
		this.capitalInicial = capitalInicial;
	}
	
	public float getCapitalFinal() 
	{
		float capitalInvestido = 0;
		if(this.quantidadeAcoes > 0)
		{
			capitalInvestido = this.quantidadeAcoes * this.ultimoPrecoFechamento;
		}
		
		return this.capital + capitalInvestido;
	}
	
	public float getCapital() {
		return capital;
	}
	public void setCapital(float capital) {
		this.capital = capital;
	}
	public int getQuantidadeAcoes() {
		return quantidadeAcoes;
	}
	public void setQuantidadeAcoes(int quantidadeAcoes) {
		this.quantidadeAcoes = quantidadeAcoes;
	}
	public void setUltimoPrecoFechamento(float ultimoPrecoFechamento) {
		this.ultimoPrecoFechamento = ultimoPrecoFechamento;
	}

	public void comprar()
	{
		//calcula quantas acoes dá para comprar
		int quantidadeAcoes = (int) (this.capital / ultimoPrecoFechamento);
		
		if(quantidadeAcoes <= 0){
			return;
		}
		
		//aumenta numero de ações
		this.quantidadeAcoes += quantidadeAcoes;
		
		//diminui o capital
		this.capital -= (quantidadeAcoes * ultimoPrecoFechamento);
		
		logger.info(quantidadeAcoes + " stocks bought at " + ultimoPrecoFechamento + ", having now $" + this.capital);
	}
	
	public void vender()
	{
		if(this.quantidadeAcoes <= 0){
			return;
		}
		
		//aumenta capital
		this.capital += this.quantidadeAcoes * ultimoPrecoFechamento;
		
		logger.info(quantidadeAcoes + " stocks sould at " + ultimoPrecoFechamento + ", having now $" + this.capital);
		
		//zera
		this.quantidadeAcoes = 0;
	}
}