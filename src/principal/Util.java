package principal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
/** Classe que implementa os utilitarios de busca: "Busca por Palavra" e "Procura Linha"*/
public class Util  {
	/** referencia da area de texto da Interface*/
	public static JTextPane areaDeTexto;
	private static JFrame frameBusca = null;
	private static JFrame frameLine=null;
	private static JTextField texto_busca;	
	private static JRadioButton desdeInicio, desdeAgora;
	private static JButton botao_buscaProximo;
	private static boolean verificaDesdeInicio = false;
	private static String previous = "";
	/** Logotipo do programa*/
	public static String icon = "img/icon.png";	
	
	/** Classe que realiza busca de palavras */     
	public static void busca(JTextPane aux){
	if( (frameBusca == null) || (!frameBusca.isShowing()) )
	{
		areaDeTexto = aux;
		//bool que verifica se a pessoa quer fazer a busca desde o inicio				
		frameBusca = new JFrame("Procurar...");  		
		Container Lexcontainer = new Container();           
   				
		texto_busca = new JTextField(20);
		final JLabel labelPesquisa = new JLabel("");
		labelPesquisa.setForeground(Color.red);
		botao_buscaProximo = new JButton("     Buscar     ");
		desdeAgora = new JRadioButton("A partir deste ponto                                          ");
		desdeInicio = new JRadioButton("Desde o in�cio                                               ");				
		desdeAgora.setToolTipText("Realiza a busca a partir da posi��o atual do cursor");
		desdeInicio.setToolTipText("Realiza a busca desde o in�cio do c�digo");
		desdeInicio.setSelected(true);
		verificaDesdeInicio = true;
		Lexcontainer = frameBusca.getContentPane();
		                	   
		
		JPanel painel_head = new JPanel();
		painel_head.setLayout(new FlowLayout());
		painel_head.add(new JLabel("Encontrar:"));
		painel_head.add(texto_busca);
		
		JPanel painel_body = new JPanel();
		painel_body.setLayout(new BorderLayout());
		painel_body.add(desdeAgora, BorderLayout.NORTH);
		painel_body.add(desdeInicio, BorderLayout.SOUTH);		
			
		JPanel painel_butao = new JPanel();
		painel_butao.setLayout(new BorderLayout());
		painel_butao.add(botao_buscaProximo, BorderLayout.CENTER);
		
		JPanel painel_feedback = new JPanel();
		painel_feedback.setLayout(new FlowLayout());		
		painel_feedback.add(labelPesquisa);

		Lexcontainer.setLayout( new FlowLayout() );		
		Lexcontainer.add(painel_head );
		Lexcontainer.add(painel_body);		
		Lexcontainer.add(painel_feedback);
		Lexcontainer.add(painel_butao);
		Box.createGlue();
		
		botao_buscaProximo.addActionListener(				
				new ActionListener(){									
					
					public void actionPerformed(ActionEvent actionEvent){
						labelPesquisa.setText("");
						if(verificaDesdeInicio == true){
							areaDeTexto.setCaretPosition(0);
							verificaDesdeInicio = false;
							desdeInicio.setSelected(false);
							desdeAgora.setSelected(true);
						}
						
						int k = 0;
						if( texto_busca.getText().length() == 0 ){
							JOptionPane.showMessageDialog(frameBusca,
									"O campo de busca est� vazio!",
									MACP.version,JOptionPane.WARNING_MESSAGE);
							}					
						else{
						 	int posAtual = areaDeTexto.getCaretPosition(), posInicial = 0;
						 	String palavra = texto_busca.getText().toString();
						 	
						 	for(int i = posAtual; i < areaDeTexto.getText().length(); i++){								 	
						 		if(palavra.charAt(0) == areaDeTexto.getText().charAt(i)){
						 			posInicial = i;
						 			k = i;
						 			for(int j=0; j<palavra.length(); j++){								 				
						 				if(palavra.charAt(j) == areaDeTexto.getText().charAt(k)){
						 					k++;								 													 						
						 				}
						 				else{
						 					k = -1;		
						 					labelPesquisa.setText("String n�o encontrada");
						 					break;
						 				}								 				
						 			}
						 			if( k != -1){						 				
						 				if(previous.equals(areaDeTexto.getSelectedText()))
						 					labelPesquisa.setText("String n�o encontrada");
						 				else
						 					labelPesquisa.setText("");
						 				
							 				areaDeTexto.select( posInicial, k);
							 				previous = areaDeTexto.getSelectedText(); 							 											 										 				
							 				break;						 				
						 			}						 			
						 		}
						 		else{
						 			labelPesquisa.setText("String n�o encontrada");
						 		}
						 	}						 	
						 }//fim else
					}					
				}
		);
		desdeAgora.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent actionEvent){  					                   
						verificaDesdeInicio = false;
						desdeInicio.setSelected(false);
						desdeAgora.setSelected(true);
					}
				}
		);
		desdeInicio.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent actionEvent){  					                   
						verificaDesdeInicio = true;
						desdeInicio.setSelected(true);
						desdeAgora.setSelected(false);
					}
				}
		);				
						
		frameBusca.setSize( 280, 180 );
		Lexcontainer.setVisible( true );				
		frameBusca.setResizable(false);        
		frameBusca.setLocationRelativeTo(frameBusca);
		frameBusca.setAlwaysOnTop(true);
		frameBusca.setVisible(true);  
		texto_busca.getRootPane().setDefaultButton(botao_buscaProximo);
	}
	else
	{
		frameBusca.requestFocus();
	}
	}
	
	/** Classe que realiza a a��o Ir para linha... */
	public static void gotoLine(JTextPane aux){
	if( (frameLine == null) || (!frameLine.isShowing()) )
	{
		areaDeTexto = aux;
//		bool que verifica se a pessoa quer fazer a busca desde o inicio				
		frameLine = new JFrame("Ir para a linha...");		
		Container Lexcontainer = new Container();           				
		
		texto_busca = new JTextField(15);
		botao_buscaProximo = new JButton("Ir");		
					
		Lexcontainer = frameLine.getContentPane();
		Lexcontainer.setLayout( new FlowLayout() );                   	   
										
		Lexcontainer.add(texto_busca);
		Lexcontainer.add(botao_buscaProximo);		
		botao_buscaProximo.addActionListener(				
				new ActionListener(){
					public void actionPerformed(ActionEvent actionEvent){
						int contadorDeLinhas = 0;						 							 	
					 	int caretPos = 0;
					 	
						if(verificaDesdeInicio == true){
							areaDeTexto.setCaretPosition(0);
							verificaDesdeInicio = false;
							desdeInicio.setSelected(false);
							desdeAgora.setSelected(true);
						}
//						int k = 0;
						if( texto_busca.getText().length() == 0 ){
							JOptionPane.showMessageDialog(frameLine,
									"O campo de busca est� vazio!",
									MACP.version,JOptionPane.ERROR_MESSAGE);
							}
					
						 else{							
						 try{
						 	int pos = Integer.parseInt(texto_busca.getText().toString());							
						 	int Total = UI.getLineCount();
						 	
						 	if(( pos <= Total )&&( pos > 0 )){
						 		for(int i=0; i<areaDeTexto.getText().length(); i++){
						 			if(areaDeTexto.getText().charAt(i) == '\n'){
						 				contadorDeLinhas++;
						 				if(contadorDeLinhas == pos){
						 					break;
						 				}
						 			}
						 			else{
						 				caretPos++;
						 			}
						 		}
						 		if(pos == 1)
						 			areaDeTexto.setCaretPosition(0);
						 		else if (pos == Total)
						 			areaDeTexto.setCaretPosition(caretPos);
						 		else
						 			areaDeTexto.setCaretPosition(caretPos+contadorDeLinhas-1);
						 		
						 		frameLine.setVisible(false);  
						 		areaDeTexto.requestFocus();
						 	}						 	
						 	else{
						 		JOptionPane.showMessageDialog(frameLine,
										"A linha da busca n�o existe!",
										MACP.version,JOptionPane.ERROR_MESSAGE);
						 	}
						 }
						 catch(NumberFormatException numero){
									JOptionPane.showMessageDialog(frameLine,
											"Formato de n�mero inv�lido!",
											MACP.version,JOptionPane.ERROR_MESSAGE);
						 }
						 }//fim else
					}
				}				
		);
				
		frameLine.setSize( 270, 70 );
		Lexcontainer.setVisible( true );				
		frameLine.setResizable(false);        		
		frameLine.setLocationRelativeTo(frameLine);
		frameLine.setAlwaysOnTop(true);
		frameLine.setVisible(true);  
		//setando o botao como submit do formulario de busca
		texto_busca.getRootPane().setDefaultButton(botao_buscaProximo);
	}
	else
	{
		frameLine.requestFocus();
	}
	}
}
