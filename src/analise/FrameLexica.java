package analise;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import principal.*;

/** Classe que implementa o padrao singleton para a analise lexica*/
public class FrameLexica {
	private static JFrame frame=null;
	private static JLabel label;
	/** Area de texto que ira armazenar os resultados da analise lexica*/
	public static JTextArea textAreaResultado;
	private String resultado="";
	private String info="";
	private static FrameLexica instance = null;
	/** Classe que implementa o padrao singleton para a analise lexica*/	
	public FrameLexica(UI interfaceAux, String txtResultado, String erros){
		setResultado(erros);
		setInfo(txtResultado);
		frame = new JFrame("Resultado da Análise Léxica");
		Container Lexcontainer = new Container();                
		textAreaResultado = new JTextArea(12,30);	
		label = new JLabel("     "+getInfo()+"     ");
		JButton op1, op2;	
		JScrollPane LexscrollPane = new JScrollPane();
		Lexcontainer = frame.getContentPane();
		Lexcontainer.setLayout( new FlowLayout() );                   	   
		
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		if(StartLexica.erros == 0){
			label.setForeground(Color.decode("#006600"));
		}
		else{
			label.setForeground(Color.red);
		}
		Lexcontainer.add(label);
		
		textAreaResultado.setFont(Font.decode("Verdana"));
		textAreaResultado.setSelectionColor(Color.decode("#10377C"));
		
		if(StartLexica.erros != 0){        	
			textAreaResultado.setForeground(Color.decode("#003366"));			
			textAreaResultado.setText(getResultado());
			label.setText(getInfo());
		}
		else{
			textAreaResultado.setForeground(Color.decode("#003366"));     						
			textAreaResultado.setText(" Não foi detectado nenhum erro léxico");
			label.setText("                Não contém erros léxicos                ");
		}                
		
		op1 = new JButton( "   Visualizar Erros   " );
		op2 = new JButton( "   Classificar Tokens   " );
		Lexcontainer.add(textAreaResultado);  	
		LexscrollPane = new JScrollPane( textAreaResultado, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );        
		op1.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent actionEvent){  					
						if(StartLexica.erros != 0){  				          						
							textAreaResultado.setForeground(Color.decode("#003366")); 
							textAreaResultado.setText(getResultado());
							label.setText(getInfo());
						}
						else{  				        
							textAreaResultado.setForeground(Color.decode("#003366"));     						
							textAreaResultado.setText(" Não foi detectado nenhum erro léxico");
							label.setText("                Não contém erros léxicos                ");
						}
					}
				}
		);
		op2.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent actionEvent){  				        
						textAreaResultado.setForeground(Color.decode("#003366"));                    
						Token token = StartLexica.tk;                    
						String resulta = "";				    
						while(token != null){				    	
							resulta = resulta + "Representação: " + token.getRepr() + "\n";
							resulta = resulta + "> Classe: " + String.valueOf(token.getClas()) + " - "+ token.getClasse()+ "\n";
							resulta = resulta + "> Posição: (" + (token.getLinha()-1)+ " , "+ token.getColuna()+")\n";
							resulta = resulta + "--------------------------------------------------------\n";
							token = token.getNext();
						}  					
						textAreaResultado.setText(resulta);
					}
				}
		);
		
		Lexcontainer.add(op1);
		Lexcontainer.add(op2);
		frame.setSize( 350, 300 );
		Lexcontainer.setVisible( true );
		Lexcontainer.add( LexscrollPane );
		
		textAreaResultado.setEditable( false );
		textAreaResultado.setFocusable(false);		
		frame.setResizable(false);        
		
		frame.setLocationRelativeTo(interfaceAux);		
		frame.setVisible(true);

	}
	/** Metodo que verifica se ja existe uma instancia criada.
	 *  Se tiver, apenas atualiza os valores
	 *  Se a instance for "null", entao e criada uma nova instancia*/
	public static FrameLexica getInstance(UI interfaceAux, String txtResultado, String erros){
		if(instance == null)
			instance = new FrameLexica(interfaceAux,txtResultado,erros);
		else{
			instance.setResultado(erros);
			instance.setInfo(txtResultado);
			label.setText(txtResultado);
			textAreaResultado.setText(erros);
			
			frame.setVisible(true);
			frame.requestFocus();
		}
		return instance;
	}
	/** seta se a analise teve sucesso*/
	public void setInfo(String info) {
		this.info = info;		
	}
	/** retorna informacao sobre o sucesso da analise*/
	public String getInfo() {
		return info;
	}
	/** seta o resultado da analise lexica*/
	public void setResultado(String resultado) {
		this.resultado = resultado;
		
	}
	/** retorna o resultado obtido da analise lexica*/
	public String getResultado() {
		return resultado;
	}
}
