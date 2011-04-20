package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import mas.MercadoEnvironment;
import misc.Config;
import misc.Text;
import strategies.PriceCrossStrategy;
import strategies.Strategy;
import strategies.enums.TipoMedia;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
 
public class Frame extends JFrame implements ActionListener {

	private static final long serialVersionUID = -1837978148962289782L;
	private MercadoEnvironment environment;
	private JPanel agentsPane;
	
	private final int FRAME_WIDTH = 800;
	private final int FRAME_HEIGHT = 600;
	
//inits
	
	public Frame(MercadoEnvironment environment)
	{
		this.environment = environment;

		//panel target
		JPanel target = new JPanel();
		target.setLayout(new BorderLayout());
		
		//panel agents
		target.add(this.initAgentsPane(), BorderLayout.CENTER);
		
		//panel reference
		JPanel south = new JPanel();
		south.setLayout(new BorderLayout());
		south.add(this.initReferencePane(),BorderLayout.CENTER);
		target.add(south, BorderLayout.SOUTH);
		
		//button
		JButton btnInit = new JButton("Iniciar");
		btnInit.setActionCommand("iniciar");
		btnInit.addActionListener(this);	
		south.add(btnInit, BorderLayout.SOUTH);
		
		//scroll pane
		JScrollPane scrollPane = new JScrollPane(target);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		//frame
		this.setTitle(Text.FRAME_TITLE);
		this.setSize(this.FRAME_WIDTH, this.FRAME_HEIGHT);
		this.setExtendedState(this.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	private Component initAgentsPane()
	{
		agentsPane = new JPanel();
		agentsPane.setBorder(BorderFactory.createTitledBorder(Text.TITLE_AGENTS_PANEL));
		agentsPane.setLayout(new GridLayout(Config.INVESTORS_QUANTITY, 1));
		JComboBox comboStrategies;
		
		for(int i = 1 ; i <= Config.INVESTORS_QUANTITY ; i++)
		{
			JPanel agentPane = new JPanel();
			agentPane.setLayout(new FlowLayout(FlowLayout.LEADING));
			
			if(i != 1){
				agentPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
			}
			
			comboStrategies = createCombo("comboStrategies",new Object[]{
				Text.STRATEGY_PRICE_CROSSING,
				Text.STRATEGY_AVERAGE_CROSSING,
				Text.STRATEGY_TRIPLE_CROSSING
			});
			
			agentPane.add(new JLabel(i+"."));
			agentPane.add(comboStrategies);
			
			this.onComboStrategiesSelect(comboStrategies);
			
			agentsPane.add(agentPane);
		}
		
		return agentsPane;
	}

	private Component initReferencePane()
	{
		JPanel referencePane = new JPanel();
		referencePane.setBorder(BorderFactory.createTitledBorder(Text.REFERENCE));
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEADING);
		referencePane.setLayout(layout);
		
		JLabel label = new JLabel();
		label.setText(Text.REFERENCE_HTML);
		
		referencePane.add(label);
		return referencePane;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals("iniciar"))
		{
			this.onBtnInitClick();
		}
		else if(e.getActionCommand().equals("comboStrategies"))
		{
			this.onComboStrategiesSelect((JComboBox)e.getSource());
		}
	}
	
	private void onComboStrategiesSelect(JComboBox comboStrategies)
	{
		//remove items
		JPanel agentPane = (JPanel) comboStrategies.getParent();
		Component[] cmps = agentPane.getComponents();
		JTextField txtPeriod;
		
		for(int i = 2; i < cmps.length ; i++)
		{
			agentPane.remove(cmps[i]);
		}
		
		String strategy = (String) comboStrategies.getSelectedItem();
		
		int averageQuantity = strategy == Text.STRATEGY_PRICE_CROSSING ? 1 :
			strategy == Text.STRATEGY_AVERAGE_CROSSING ? 2 : 3;
		
	    for(int i = 0 ; i < averageQuantity ; i++)
	    {
	    	agentPane.add(
				createCombo("comboAverageKind" + i,new Object[]{
					Text.AVERAGE_SIMPLE,
					Text.AVERAGE_EXPONENCIAL
				})
			);
	    	
	    	txtPeriod = new JTextField(3);
	    	txtPeriod.setText("20");
	    	agentPane.add(txtPeriod);
	    }
	    
	    agentPane.revalidate();
	    agentPane.repaint();
	}

	private void onBtnInitClick()
	{
		Component[] cmps, agentsPanelLine = agentsPane.getComponents();
		String strategyRef;
		Strategy strategy;
		
		//for each agent config
		for(int i = 0 ; i < agentsPanelLine.length ; i++)
		{
			//build strategy
			cmps = ((Container) agentsPanelLine[i]).getComponents();
			strategyRef = (String) ((JComboBox) cmps[1]).getSelectedItem();
			strategy = null;
			
			if(strategyRef == Text.STRATEGY_PRICE_CROSSING)
			{
				strategy = new PriceCrossStrategy(
					this.getAverageKind((String) ((JComboBox) cmps[2]).getSelectedItem()), 
					Integer.valueOf((String) ((JTextField) cmps[3]).getText())
				);
			}
			else if(strategyRef == Text.STRATEGY_AVERAGE_CROSSING)
			{
				
			}
			else if(strategyRef == Text.STRATEGY_TRIPLE_CROSSING)
			{
				
			}
			
			//create agent
			this.environment.createInvestor(strategy); 
		}
	}
	
	private JComboBox createCombo(String actionCommand, Object[] data) 
	{
		JComboBox combo = new JComboBox(data);
		
		combo.setSelectedIndex(0);
		combo.setActionCommand(actionCommand);
		combo.addActionListener(this);
		
		return combo;
	}
	
	private TipoMedia getAverageKind(String averageKind)
	{
		if(averageKind == Text.AVERAGE_EXPONENCIAL)
		{
			return TipoMedia.SIMPLES;
		}
		else if(averageKind == Text.AVERAGE_SIMPLE)
		{
			return TipoMedia.EXPONENCIAL;
		}
		
		throw new NotImplementedException();
	}
}