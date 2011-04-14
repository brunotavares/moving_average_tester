package src.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
 
public class Frame extends JFrame implements ActionListener, ChangeListener {

	private static final long serialVersionUID = -1837978148962289782L;
	private JCheckBox chbEstrategia1, chbEstrategia2, chbEstrategia3;
	private JSlider sldEstrategia1Media, sldEstrategia2Media1, sldEstrategia2Media2;
	private JLabel lblEstrategia1Media, lblEstrategia2Media1, lblEstrategia2Media2;
	private JSpinner spnEstrategia1Variacao, spnEstrategia2Variacao1, spnEstrategia2Variacao2;
	
//inits
	
	public Frame()
	{
		//frame
		this.setTitle("TCC Bruno Tavares - Sistema Multi-Agentes Aplicado à Estratégias do Mercado de Capitais");
		this.setSize(600, 400);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		this.getContentPane().add(this.initPainelEstrategias());
		this.getContentPane().add(this.initPainelMedias());
		this.getContentPane().add(this.initBotaoIniciar());
		this.setVisible(true);
	}
	
	private Component initPainelEstrategias()
	{
		//fieldset estratégias
		JPanel panelEstrategias = new JPanel();
		panelEstrategias.setBorder(BorderFactory.createTitledBorder("Estratégias"));
		panelEstrategias.setLayout(new BoxLayout(panelEstrategias, BoxLayout.PAGE_AXIS));
		panelEstrategias.setPreferredSize(new Dimension(this.getContentPane().getWidth(), 300));
		
		//checkboxes
		this.chbEstrategia1 = new JCheckBox("Cruzamento de média com preço", true);
		this.chbEstrategia2 = new JCheckBox("Cruzamento entre 2 médias", true);
		this.chbEstrategia3 = new JCheckBox("Cruzamento triplo de médias", true);
		this.chbEstrategia1.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.chbEstrategia2.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.chbEstrategia3.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		//adiciona
		panelEstrategias.add(this.chbEstrategia1);
		panelEstrategias.add(this.initPainelEstrategias1Configuracoes());
		panelEstrategias.add(this.chbEstrategia2);
		panelEstrategias.add(this.initPainelEstrategias2Configuracoes(1));
		panelEstrategias.add(this.initPainelEstrategias2Configuracoes(2));
		panelEstrategias.add(this.chbEstrategia3);
		
		//retorna
		return panelEstrategias;
	}
	
	private Component initPainelEstrategias1Configuracoes()
	{
		//painel configuracao
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);
		
		JPanel panelConfig = new JPanel();
		panelConfig.setLayout(layout);
	
		JLabel lblDe = new JLabel("Média de");
		this.lblEstrategia1Media = new JLabel("20");
		JLabel lblAte = new JLabel("dias, com variação +-");
		
		this.sldEstrategia1Media = new JSlider(JSlider.HORIZONTAL, 10, 100, 20);
		this.sldEstrategia1Media.addChangeListener(this);
		this.sldEstrategia1Media.setMajorTickSpacing(10);
		this.sldEstrategia1Media.setMinorTickSpacing(1);
		this.sldEstrategia1Media.setPaintTicks(true);
		this.sldEstrategia1Media.setPaintLabels(true);
		
		this.spnEstrategia1Variacao = new JSpinner(new SpinnerNumberModel(5,2,10,1));
		
		//align
		panelConfig.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblDe.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.sldEstrategia1Media.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.lblEstrategia1Media.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblAte.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.spnEstrategia1Variacao.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		//adiciona
		panelConfig.add(lblDe);
		panelConfig.add(this.sldEstrategia1Media);
		panelConfig.add(this.lblEstrategia1Media);
		panelConfig.add(lblAte);
		panelConfig.add(this.spnEstrategia1Variacao);
		
		//retorna
		return panelConfig;
	}
	
	private Component initPainelEstrategias2Configuracoes(int panelIndex)
	{
		//painel configuracao
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);
		JPanel panelConfig = new JPanel();
		panelConfig.setLayout(layout);
		
		JLabel lblDe;
		JLabel lblAte = new JLabel("dias, com variação +-");
		
		if(panelIndex == 1)
		{
			lblDe = new JLabel("Média rápida de");
			
			this.sldEstrategia2Media1 = new JSlider(JSlider.HORIZONTAL, 10, 30, 20);
			this.sldEstrategia2Media1.addChangeListener(this);
			this.sldEstrategia2Media1.setMajorTickSpacing(10);
			this.sldEstrategia2Media1.setMinorTickSpacing(1);
			this.sldEstrategia2Media1.setPaintTicks(true);
			this.sldEstrategia2Media1.setPaintLabels(true);
			
			this.spnEstrategia2Variacao1 = new JSpinner(new SpinnerNumberModel(5,2,10,1));
			this.lblEstrategia2Media1 = new JLabel("20");
			
			this.sldEstrategia2Media1.setAlignmentX(Component.LEFT_ALIGNMENT);
			this.spnEstrategia2Variacao1.setAlignmentX(Component.LEFT_ALIGNMENT);
			this.lblEstrategia2Media1.setAlignmentX(Component.LEFT_ALIGNMENT);
			
			panelConfig.add(lblDe);
			panelConfig.add(this.sldEstrategia2Media1);
			panelConfig.add(this.lblEstrategia2Media1);
			panelConfig.add(lblAte);
			panelConfig.add(this.spnEstrategia2Variacao1);
		}
		else
		{
			lblDe = new JLabel("Média lenta de  ");
			this.sldEstrategia2Media2 = new JSlider(JSlider.HORIZONTAL, 30, 100, 40);
			this.sldEstrategia2Media2.addChangeListener(this);
			this.sldEstrategia2Media2.setMajorTickSpacing(10);
			this.sldEstrategia2Media2.setMinorTickSpacing(1);
			this.sldEstrategia2Media2.setPaintTicks(true);
			this.sldEstrategia2Media2.setPaintLabels(true);
			
			this.spnEstrategia2Variacao2 = new JSpinner(new SpinnerNumberModel(5,2,10,1));
			this.lblEstrategia2Media2 = new JLabel("40");
			
			this.sldEstrategia2Media2.setAlignmentX(Component.LEFT_ALIGNMENT);
			this.lblEstrategia2Media2.setAlignmentX(Component.LEFT_ALIGNMENT);
			this.spnEstrategia2Variacao2.setAlignmentX(Component.LEFT_ALIGNMENT);
			
			panelConfig.add(lblDe);
			panelConfig.add(this.sldEstrategia2Media2);
			panelConfig.add(this.lblEstrategia2Media2);
			panelConfig.add(lblAte);
			panelConfig.add(this.spnEstrategia2Variacao2);
		}
		
		//align
		panelConfig.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblDe.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblAte.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		
		//retorna
		return panelConfig;
	}
	
	private Component initPainelMedias()
	{
		//medias
		JPanel panelMedias = new JPanel();
		panelMedias.setBorder(BorderFactory.createTitledBorder("Médias Consideradas"));
		panelMedias.setLayout(new BoxLayout(panelMedias, BoxLayout.PAGE_AXIS));
		
		/*
		JCheckBox chbSimples = new JCheckBox("Média Móvel Simples - MMS", true);
		JCheckBox chbExponencial = new JCheckBox("Média Móvel Exponencial - MME", true);
		
		panelMedias.add(chbSimples);
		panelMedias.add(chbExponencial);*/
		
		return panelMedias;
	}
	
	private Component initBotaoIniciar()
	{
		JButton btnIniciar = new JButton("Iniciar");
		btnIniciar.setActionCommand("iniciar");
		btnIniciar.addActionListener(this);	
		
		return btnIniciar;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals("Iniciar"))
		{
			this.onBtnIniciarClick();
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent e) 
	{
		if(e.getSource() == this.sldEstrategia1Media)
		{
			this.lblEstrategia1Media.setText(this.sldEstrategia1Media.getValue() + "");
		}
		else if(e.getSource() == this.sldEstrategia2Media1)
		{
			this.lblEstrategia2Media1.setText(this.sldEstrategia2Media1.getValue() + "");
		}
		else if(e.getSource() == this.sldEstrategia2Media2)
		{
			this.lblEstrategia2Media2.setText(this.sldEstrategia2Media2.getValue() + "");
		}
	}

	private void onBtnIniciarClick()
	{
		
	}

	
}