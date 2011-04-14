package src.mas;

import jason.asSemantics.ActionExec;
import jason.asSemantics.Agent;
import jason.asSyntax.Literal;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

public class BovespaAgent extends Agent {

//atributos
	
	private Logger logger = Logger.getLogger("bovespa");
	private HashMap<String, Vector<String[]>> cotacoes;
	private int cotacaoIndex;
	private String[] ativos;

//inits
	
	@Override
	public void initAg()
	{
		super.initAg();
		this.cotacoes = new HashMap<String, Vector<String[]>>();
		this.ativos = new String[]{"PETR4", "VALE5"};
		
		this.initCotacoes();
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
		
		logger.info("cotações carregadas");
	}

//overrides
	
	@Override
	public ActionExec selectAction(List<ActionExec> arg0) 
	{
		ActionExec actionExec = super.selectAction(arg0);
		String functor = actionExec.getActionTerm().getFunctor();
	
		if(functor.equals("abertura_pregao"))
		{
			this.aberturaPregaoAction();
		}
		
		return actionExec;
	}
	
//actions
	
	/*
	 * Carrega a cotação do dia
	 */
	public void aberturaPregaoAction()
	{
		String data = "", cotacoes = "";
		
		//para cada ativo
		for(int i = 0 ; i < this.ativos.length ; i++)
		{
			//busca dados do dia
			String[] cotacao = this.cotacoes.get(this.ativos[i]).get(this.cotacaoIndex);
			data = cotacao[0];
			
			if(!cotacoes.isEmpty())
			{
				cotacoes += ",";
			}
			
			cotacoes += this.ativos[i] + "|" +
						cotacao[0] +"|"+
						cotacao[1] +"|"+
						cotacao[2] +"|"+
						cotacao[3] +"|"+
						cotacao[4];
		}

		//define crenca
		Literal crencaCotacao = Literal.parseLiteral("cotacoes(\""+cotacoes+"\")");	
		
		//adiciona ao agente
		try {
			this.addBel(crencaCotacao);
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		logger.info("consultei cotações do dia " + data);
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
