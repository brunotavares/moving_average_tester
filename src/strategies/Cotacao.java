package strategies;


/**
 * Classe que representa uma cotação diária de determinado ativo.
 * 
 * @author Bruno Tavares
 */
public class Cotacao {

//atributos
	
	/**
	 * Preço de Abertura
	 */
	private float precoAbertura; 
	
	/**
	 * Preço de Abertura
	 */
	private float precoFechamento; 
	
	/**
	 * Preço Máximo
	 */
	private float precoMaximo;
	
	/**
	 * Preço de Mínimo
	 */
	private float precoMinimo; 
	
	/**
	 * Data
	 */
	private String data;
	
	/**
	 * Código do ativo
	 */
	private String ativo;
	
//inicializadores
	
	public Cotacao(String ativo, String data, float precoAbertura, float precoFechamento, float precoMaximo, float precoMinimo) 
	{
		this.ativo = ativo;
		this.data = data;
		this.precoAbertura = precoAbertura;
		this.precoFechamento = precoFechamento;
		this.precoMaximo = precoMaximo;
		this.precoMinimo = precoMinimo;
	}
	
	public Cotacao(String ativo, String data, String precoAbertura, String precoFechamento, String precoMaximo, String precoMinimo) 
	{
		this.ativo = ativo;
		this.data = data;
		this.precoAbertura = Float.parseFloat(precoAbertura);
		this.precoFechamento = Float.parseFloat(precoFechamento);
		this.precoMaximo = Float.parseFloat(precoMaximo);
		this.precoMinimo = Float.parseFloat(precoMinimo);
	}
	
//acessores	
	
	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public float getPrecoAbertura() {
		return precoAbertura;
	}
	
	public void setPrecoAbertura(float precoAbertura) {
		this.precoAbertura = precoAbertura;
	}

	public float getPrecoFechamento() {
		return precoFechamento;
	}

	public void setPrecoFechamento(float precoFechamento) {
		this.precoFechamento = precoFechamento;
	}

	public float getPrecoMaximo() {
		return precoMaximo;
	}

	public void setPrecoMaximo(float precoMaximo) {
		this.precoMaximo = precoMaximo;
	}

	public float getPrecoMinimo() {
		return precoMinimo;
	}

	public void setPrecoMinimo(float precoMinimo) {
		this.precoMinimo = precoMinimo;
	}
}
