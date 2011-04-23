package mas;

import jason.RevisionFailedException;
import jason.asSemantics.Agent;
import jason.asSyntax.Literal;

import java.io.File;
import java.util.Vector;
import java.util.logging.Logger;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import misc.Config;
import misc.Debug;
import misc.Text;
import strategies.Quote;

/**
 * Bovespa Agent (bovespa.als) class
 * @author Bruno Tavares
 */
public class BovespaAgent extends Agent {

//attributes
	
	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(BovespaAgent.class.getName());
	
	/**
	 * Stock Quotes (e.g. (Quote) stockQuotes[45][42])
	 */
	private Object[][] stockQuotes;
	
	/**
	 * Pointer to current quote day. Increased on every auction.
	 */
	private int quotesPointer;
	
	/**
	 * True when quotesPointer reaches the limited on Config.QUOTES_QUANTITY
	 */
	private boolean finished;
	
//initializers
	
	/**
	 * Initializes Agent, pointers, and loading quotes
	 */
	@Override
	public void initAg()
	{
		logger.info(Debug.getTime() + " - initAg init");
		
		super.initAg();
		this.quotesPointer = 0;
		this.initQuotes();
		
		logger.info(Debug.getTime() + " - initAg end");
	}
	
	/**
	 * Load excel quote files (.xls) and parsers data into quotes, loading stockQuotes
	 * array
	 */
	private void initQuotes()
	{
		String stock, date, openPrice, closePrice, maximumPrice, minimumPrice, year;
		String[] dateArray;
		
		try{
			//get files
			String[] quotesFiles = new File(Config.QUOTES_FOLDER).list();
			
			if(quotesFiles == null){
				throw new Exception(Text.EXCEPTION_QUOTES_FOLDER_NOT_FOUND);
			}
			
			//validate extension
			Vector<String> quotesFilesTemp = new Vector<String>(quotesFiles.length);
			
			for(int i = 0 ; i < quotesFiles.length ; i++)
			{
				if(quotesFiles[i].substring(quotesFiles[i].length() - 4).equals(".xls"))
				{
					quotesFilesTemp.add(quotesFiles[i]);
				}
			}
			quotesFiles = new String[quotesFilesTemp.size()];
			quotesFiles = quotesFilesTemp.toArray(quotesFiles);
	
			//settings
			WorkbookSettings settings = new WorkbookSettings();
			settings.setSuppressWarnings(true); 
			
			//initialize vectos
			Config.STOCKS = new String[quotesFiles.length];
			
			//for each file
			for(int i = 0 ; i < quotesFiles.length ; i++)
			{
				stock = quotesFiles[i].replace(".xls", ""); 
				
				//open excel sheet
				Workbook workbook = Workbook.getWorkbook(new File(Config.QUOTES_FOLDER + "/" + quotesFiles[i]), settings);
				Sheet sheet = workbook.getSheet(0);
				
				//init configs and arrays
				Config.STOCKS[i] = stock;
				Config.QUOTES_QUANTITY = sheet.getRows() - 1;
				
				if(stockQuotes == null){
					stockQuotes = new Object[quotesFiles.length][Config.QUOTES_QUANTITY];
				}
				
				//for each line
				for(int j = 1 ; j <= Config.QUOTES_QUANTITY ; j++)
				{
					//extract data
					date 		= sheet.getCell(0,j).getContents();
					openPrice	= sheet.getCell(1,j).getContents().replace(",", ".");
					closePrice	= sheet.getCell(2,j).getContents().replace(",", ".");
					maximumPrice= sheet.getCell(3,j).getContents().replace(",", ".");
					minimumPrice= sheet.getCell(4,j).getContents().replace(",", ".");
					
					//fix data issues
					dateArray = date.split("/");
					year = dateArray[2].split(" ")[0];
					if(year.length() == 2){
						year = "20" + year;
					}
					date = dateArray[0] + "/" + dateArray[1] + "/" + year;
					
					//add to vector
					stockQuotes[i][j-1] = new Quote(stock, date, openPrice, closePrice, maximumPrice, minimumPrice);
				}
				
				workbook.close();
			}
			
			logger.info(Debug.getTime() + " - stocks loaded");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

//assessors
	
	public boolean isFinished() {
		return finished;
	}
	
	public Object[][] getStockQuotes() {
		return stockQuotes;
	}
	
//listeners
	

	/**
	 * Load daily quote using quotesPointer, adds to beliefs base, and then
	 * increase quotesPointer
	 */
	public synchronized void onAbrirPregao()
	{
		if(this.quotesPointer < Config.QUOTES_QUANTITY)
		{
			StringBuilder quotesString = new StringBuilder(Config.STOCKS.length);
			Quote quote;
			String date = "";
			
			//for each stock
			for(int i = 0 ; i < Config.STOCKS.length ; i++)
			{
				//get quotes
				quote = (Quote) stockQuotes[i][this.quotesPointer];
					
				if(i == 0)
				{
					date = quote.getDate();
				}
				else
				{
					quotesString.append(",");
				}
				
				quotesString.append(quote.toString());
			}
	
			//define belief
			try {
				this.addBel(Literal.parseLiteral("cotacoes(\""+quotesString+"\")"));
			} 
			catch (RevisionFailedException e) 
			{
				e.printStackTrace();
			}
			
			logger.info(Debug.getTime() + " - daily stocks loaded " + date);
			
			//increase pointer
			this.quotesPointer++;
		
			logger.info(Debug.getTime() + " - ciclo="+ this.quotesPointer + ", limiteCotacoes=" + Config.QUOTES_QUANTITY);
		}
		else
		{
			this.finished = true;
			MercadoEnvironment.getInstance().removePercept("bovespa", Literal.parseLiteral("executando"));
			logger.info(Debug.getTime() + " - removing executando belief");
		}
	}
	
//other methods
	
	/**
	 * @see isBovespa
	 */
	public static boolean isBovespa(Agent agent)
	{
		return agent == null ? false : isBovespa(agent.getASLSrc());
	}
	
	/**
	 * Verifies if a given agent is Bovespa agent.
	 */
	public static boolean isBovespa(String name)
	{
		return name == null ? false : name.equals("bovespa.asl") || name.equals("bovespa");
	}
}