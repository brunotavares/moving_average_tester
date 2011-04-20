package mas;

import jason.asSemantics.Agent;
import jason.asSyntax.Literal;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Logger;

import misc.Debug;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

public class BovespaAgent extends BaseAgent {

//atributos
	
	private Logger logger = Logger.getLogger("bovespa");
	private HashMap<String, Vector<String[]>> cotacoes;
	private int ciclo, limiteCotacoes;
	private String[] ativos;

//inits
	
	@Override
	public void initAg()
	{
		logger.info(Debug.getTime() + " - initAg init");
		
		super.initAg();
		this.cotacoes = new HashMap<String, Vector<String[]>>();
		this.ativos = new String[]{"PETR4", "VALE5"};
		this.ciclo = 1;
		
		this.initCotacoes();
		
		logger.info(Debug.getTime() + " - initAg end");
	}
	
	private void initCotacoes()
	{
		//settings
		WorkbookSettings settings = new WorkbookSettings();
		settings.setSuppressWarnings(true); 
		
		//para cada ativo
		for(int i = 0 ; i < this.ativos.length ; i++)
		{
			Workbook workbook = null;
			//abre planilha
			try {
				workbook = Workbook.getWorkbook(new File("cotacoes/"+this.ativos[i]+".xls"), settings);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			Sheet sheet = workbook.getSheet(0);
			
			//inicializa vetor
			Vector<String[]> cotacoesAtivo = new Vector<String[]>();
			
			//guarda o limite
			this.limiteCotacoes = sheet.getRows() - 1;
			
			//itera sobre linhas
			for(int j = 1 ; j < sheet.getRows() ; j++)
			{
				//extrai data
				String data 		= sheet.getCell(0,j).getContents();
				String abertura		= sheet.getCell(1,j).getContents();
				String fechamento	= sheet.getCell(2,j).getContents();
				String maxima		= sheet.getCell(3,j).getContents();
				String minima		= sheet.getCell(4,j).getContents();
				
				//arruma data
				String[] dataArray = data.split("/");
				String ano = dataArray[2].split(" ")[0];
				if(ano.length() == 2){
					ano = "20" + ano;
				}
				data = dataArray[0] + "/" + dataArray[1] + "/" + ano;
				
				//senão coloca no vetor
				cotacoesAtivo.add(new String[]{
					data,
					abertura.replace(",", "."),
					fechamento.replace(",", "."),
					maxima.replace(",", "."),
					minima.replace(",", "."),
				});
			}
			
			//fecha planilha
			workbook.close();
			
			//adiciona vetor no hashmap
			this.cotacoes.put(this.ativos[i], cotacoesAtivo);
		}
		
		logger.info(Debug.getTime() + " - stocks loaded");
	}

//listeners
	
	public synchronized void onAbrirPregao()
	{
		boolean primeiro = true;
		StringBuilder cotacoes = new StringBuilder(this.ativos.length);
		String[] cotacao;
		String data = "";
		
		//para cada ativo
		for(int i = 0 ; i < this.ativos.length ; i++)
		{
			//busca dados do dia
			cotacao = this.cotacoes.get(this.ativos[i]).get(this.ciclo);
			data =  cotacao[0];
				
			if(primeiro)
			{
				primeiro = false;
			}
			else
			{
				cotacoes.append(",");
			}
			
			cotacoes.append(this.ativos[i]);
			cotacoes.append("|");
			cotacoes.append(data);
			cotacoes.append("|");		
			cotacoes.append(cotacao[1]);
			cotacoes.append("|");
			cotacoes.append(cotacao[2]);
			cotacoes.append("|");
			cotacoes.append(cotacao[3]);
			cotacoes.append("|");
			cotacoes.append(cotacao[4]);
		}

		//define crenca
		this.addBel(Literal.parseLiteral("cotacoes(\""+cotacoes+"\")"));
		
		logger.info(Debug.getTime() + " - daily stocks loaded " + data);
		
		//aumenta ciclo
		this.ciclo++;
	
		logger.info(Debug.getTime() + " - ciclo="+ this.ciclo + ", limiteCotacoes=" + this.limiteCotacoes);
		//verifica se acabou as cotacoes
		if(this.ciclo >= this.limiteCotacoes)
		{
			this.delBel(Literal.parseLiteral("executando"));
			return;
		}
	}
	
//demais métodos
	
	public static boolean isBovespa(Agent agente)
	{
		return isBovespa(agente.getASLSrc());
	}
	
	public static boolean isBovespa(String nome)
	{
		return nome.equals("bovespa.asl") || nome.equals("bovespa");
	}
}
