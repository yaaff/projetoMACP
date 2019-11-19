package analise;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Hashtable;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;
import principal.UI;

/** Classe que implementa o padrao SingleTon para geracao de codigo c++*/
public class FrameGeracao {	
	private static JFrame frame=null;
	private static JLabel label;
	/** Frame que ir� receber os resultados que serao mostrados ao usuario*/
	public JTextPane  txtAreaResultadoErros;
	private String resultados="";
	private String info="";
	private static FrameGeracao instance = null;
	
	/** Classe que implementa o padrao SingleTon para geracao de codigo c++*/    
	public FrameGeracao(UI interfaceAux, String lblResultado, String erros)
	{
		setResultado(erros);
		setInfo(lblResultado);
		frame = new JFrame("Resultado da Geracao de Codigo");           
		Container Lexcontainer = new Container();  		
		FlowLayout layout = new FlowLayout();					       			   	     		                       		
		
		txtAreaResultadoErros = new JTextPane();
		AttributeSet normal = BLACK;
		txtAreaResultadoErros.setCharacterAttributes( normal, false );			   
				
		txtAreaResultadoErros.setSelectionColor(Color.decode("#10377C"));        
		txtAreaResultadoErros.setSelectedTextColor(Color.white);		
			setTabs( txtAreaResultadoErros, 2 );		        	
		
		
		label = new JLabel("     "+getInfo()+"     ");
		JButton op1, op2;	
		
		JScrollPane LexscrollPane = new JScrollPane();
		Lexcontainer = frame.getContentPane();                   	   		
		   
		label.setFont(label.getFont().deriveFont(Font.BOLD));        
		label.setForeground(Color.decode("#006600"));
				
		Action actions[] = txtAreaResultadoErros.getActions();
	    	    
	    Action copyAction = findAction(actions, DefaultEditorKit.copyAction);
	    op1 = new JButton( copyAction );
		op1.setText(" Copiar Codigo Selecionado ");		
		
		op2 = new JButton( " Exportar código... " );
		
		LexscrollPane = new JScrollPane( txtAreaResultadoErros, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		
		Dimension tam = new Dimension(400,300);
		LexscrollPane.setPreferredSize(tam)	;		
			
		op2.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent actionEvent) {
						try {
							SalvarCodigoGerado(txtAreaResultadoErros.getText());
						} catch (IOException e) {						
							e.printStackTrace();
						}
					}
				}
		);
		Lexcontainer.setLayout( layout );		
			Lexcontainer.add(label);			    
			Lexcontainer.add(op1);
			Lexcontainer.add(op2);			
			Lexcontainer.add( LexscrollPane );	
			txtAreaResultadoErros.setText(getResultado());			
			
		frame.setSize( 450, 400 );
		Lexcontainer.setVisible( true );
				
		txtAreaResultadoErros.setEditable( false );		
		txtAreaResultadoErros.setSelectionColor(Color.decode("#10377C"));
		txtAreaResultadoErros.setSelectedTextColor(Color.decode("#FFFFFF"));		
		txtAreaResultadoErros.setFocusable(true);		
		frame.setResizable(true);        
		frame.setLocationRelativeTo(interfaceAux);
		frame.setVisible(true);      
		txtAreaResultadoErros.requestFocus(true);
		
	}
	
	/** Metodo de listener dos controles de texto copiar, colar, recortar */
	public static Action findAction(Action actions[], String key) {
	    Hashtable commands = new Hashtable();
	    for (int i = 0; i < actions.length; i++) {
	      Action action = actions[i];
	      commands.put(action.getValue(Action.NAME), action);
	    }
	    return (Action) commands.get(key);
	  }
	
	/** Metodo que implementa o padrao singleton para gerar somente uma instancia de geracao de codigo */
	public static FrameGeracao getInstance(UI interfaceAux, String txtResultado, String erros){
		if(instance == null)
			instance = new FrameGeracao(interfaceAux,txtResultado,erros);
		else{
			instance.setResultado(erros);
			instance.setInfo(txtResultado);
			
			instance.txtAreaResultadoErros.setText(erros);
			label.setText(txtResultado);
			
			frame.setVisible(true);
			frame.requestFocus();			
		}
		return instance;
	}
	
	/** Filtro para abrir arquivos .txt */ 
	class FiltroGeracao extends javax.swing.filechooser.FileFilter {
		public boolean accept(File file) {
			String filename = file.getName();
			return filename.endsWith(".cpp");
		}
		public String getDescription() {
			return "*.cpp";
		}
	}   
	
	/** M�todo para salvar arquivos */
	public void SalvarCodigoGerado(String codigo) throws IOException  {  
		if(!(codigo.contains("\r\n")))
			codigo = codigo.replaceAll("\n","\r\n");
		
		JFileChooser abrirArquivo = new JFileChooser("C:\\UFMA-CP\\fontes-cpp");    	
		abrirArquivo.addChoosableFileFilter(new FiltroGeracao());    	    	    	   	    
		int escolha = abrirArquivo.showSaveDialog(frame);    	
		File arquivo;
		
		if(escolha == JFileChooser.CANCEL_OPTION){	
			return; 
		}
		
		else if ( escolha == JFileChooser.APPROVE_OPTION ) {
			arquivo = abrirArquivo.getSelectedFile();           
			if (arquivo.exists()) {
				int response = JOptionPane.showConfirmDialog (frame,
						"Esta pasta ja contém um arquivo chamado "+arquivo.getName()+
						"\nDeseja substituir o arquivo existente? ","Confirmar substituicao de arquivo",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				
				if ( response == JOptionPane.CANCEL_OPTION) 
					return;            
				else if(response == JOptionPane.OK_OPTION){  
					//FormatacaoTextoCarrega();
					UI.arquivoAux = arquivo;
					UI.caminho = arquivo.getAbsolutePath(); //pegando caminho do arquivo
					UI.pasta = arquivo.getCanonicalPath();
					UI.pasta = UI.pasta.replace(".txt", ".exe");
					if(!arquivo.getName().endsWith(".cpp")){                		  
						FileOutputStream out = new FileOutputStream(arquivo+".cpp");	            		
						PrintStream p = new PrintStream(out);	            
						p.write(codigo.getBytes());	 	            		
						p.close();                  		
					}
					else if(arquivo.getName().endsWith(".cpp")){	                		                	
						FileOutputStream out = new FileOutputStream(arquivo);
						PrintStream p = new PrintStream(out);
						p.write(codigo.getBytes());
						p.close();
					}	               	                
				}
			}
			else{            	
				UI.caminho = arquivo.getAbsolutePath(); //pegando caminho do arquivo
				UI.pasta = arquivo.getCanonicalPath();
				UI.pasta = UI.pasta.replace(".txt", ".exe");
				if(!arquivo.getName().endsWith(".cpp")){                		              		
					FileOutputStream out = new FileOutputStream(arquivo+".cpp");	            		
					PrintStream p = new PrintStream(out);	            
					p.write(codigo.getBytes());	 	            		
					p.close();                  		
				}
				else if(arquivo.getName().endsWith(".cpp")){             		            		
					FileOutputStream out = new FileOutputStream(arquivo);
					PrintStream p = new PrintStream(out);
					p.write(codigo.getBytes());
					p.close();
				}	  
			}
		}
	}
	/** Seta a informacao se a geracao deu certo ou se ocorreu algum erro na geracao de codigo*/
	public void setInfo(String info) {
		this.info = info;		
	}
	/**Retorna a informacao de sucesso ou fracasso sobre a geracao de codigo*/
	public String getInfo() {
		return info;
	}
	/**seta o resultado a area de texto: 
	 * se ocorreu com sucesso seta o codigo gerado
	 * se tiveram falhas seta as falhas ocorridas*/
	public void setResultado(String resultados) {
		this.resultados = resultados;
		
	}
	/** retorna o resultado para ser inserido a area de texto*/
	public String getResultado() {
		return resultados;
	}
	
	/** Setando o tamanho do TabSize do JtextPane */
	public void setTabs( JTextPane textPane, int charactersPerTab)
	{
		FontMetrics fm = textPane.getFontMetrics( textPane.getFont() );
		int charWidth = fm.charWidth( 'w' );
		int tabWidth = charWidth * charactersPerTab;
 
		TabStop[] tabs = new TabStop[10];
 
		for (int j = 0; j < tabs.length; j++)
		{
			int tab = j + 1;
			tabs[j] = new TabStop( tab * tabWidth );
		}
 
		TabSet tabSet = new TabSet(tabs);
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setTabSet(attributes, tabSet);
		int length = textPane.getDocument().getLength();
		textPane.getStyledDocument().setParagraphAttributes(0, length, attributes, false);
	}
	
	static SimpleAttributeSet BLACK = new SimpleAttributeSet();
	static{
	    StyleConstants.setForeground(BLACK, Color.black);
	    StyleConstants.setBold(BLACK, false);		    
	    StyleConstants.setFontFamily(BLACK, "Courier New");
	    StyleConstants.setFontSize(BLACK, 14);
	}	
}
