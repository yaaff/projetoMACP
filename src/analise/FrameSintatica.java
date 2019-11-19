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

import principal.UI;
/** Cria o container para visualizacao dos resultados da analise sintatica */ 
public class FrameSintatica {
	private static JFrame frame=null;
	private static JLabel label;
	/** Area onde sera armazenado o resultado da analise sintatica*/
	public static JTextArea  txtAreaResultados;
	private String resultado="";
	private String erros="";
	private static FrameSintatica instance = null;	
	public static String Clrf = "\n";	
	/** gramatica utilizada na analise sintatica*/
	public static String gramatica =  "prog -> dec incio list_cmd fim ."+ Clrf +
								"dec -> variaveis list_decl"+Clrf +
								"list_decl -> lista  :  tipo_var  ;  list_decl  | ?"+Clrf +
								"lista -> 'identificador' list_var"+Clrf +
								"list_var -> ,  'identificador'  list_var  |  ?"+Clrf +
								"tipo_var -> tipo |  vetor [ 'numero inteiro' ..   'numero inteiro' ] de tipo"+Clrf +
								"tipo -> inteiro  |  real  |  logico  |  cadeia  |  caractere"+Clrf +
								"list_cmd -> cmd  ;  list_cmd  | ?"+Clrf +
								"cmd -> ler lista2  |  escrever lista3 |  enquanto expr faca cmd  |"+Clrf +
								"repita list_cmd ate expr  | se expr entao cmd  cmd2  |"+Clrf +          
								"[[ list_cmd ]]  |  'identificador' id2  <- exp  cmd2 -> senao cmd  |  ?"+Clrf +
								"lista2 -> 'identificador' list_var2"+Clrf +
								"list_var2 -> ,  lista2  |  [  indice ] list_var2  |  ?"+Clrf +
								"lista3 -> 'identificador' list_var3  |  'literal' list_var3  |  ?"+Clrf +
								"list_var3 -> ,  lista3  |  [  indice ] list_var3  |  ?"+Clrf +
								"indice -> 'numero inteiro'  |  'identificador' id2"+Clrf +
								"exp -> verdadeiro  |  falso  |  'literal'  |  termo exp2"+Clrf +
								"termo -> fator termo2"+Clrf +
								"termo2 -> op_mult fator termo2  |  ?"+Clrf +
								"fator -> (  exp  )  |  id"+Clrf +
								"id -> 'numero'  |  'identificador'  id2"+Clrf +
								"id2 ->  [ indice ]  |  ?"+Clrf +
								"exp2 -> op_aditivo termo exp2  |  ?"+Clrf +
								"op_aditivo -> + |  -"+Clrf +
								"op_mult -> *  |  /  |  %  |  #"+Clrf +
								"expr -> exp op_relacional exp expr2"+Clrf +
								"expr2 -> op_logico expr  |  ?"+Clrf +
								"op_logico -> e  |  ou"+Clrf +
								"op_relacional ->  =  |  <>  |  >=  |  <=  |  >  |  < ";
	
	/** Cria o container para visualizacao dos resultados da analise sintatica */ 
	public FrameSintatica(UI interfaceAux, String txtResultado, String erros)
	{
		setResultado(erros);
		setErros(txtResultado);
		frame = new JFrame("Resultado da Analise Sintatica");           
		Container Lexcontainer = new Container();                
		txtAreaResultados = new JTextArea(12,30);	
		label = new JLabel("     "+getErros()+"     ");
		JButton op1, op2;	
		JScrollPane LexscrollPane = new JScrollPane();
		Lexcontainer = frame.getContentPane();
		Lexcontainer.setLayout( new FlowLayout() );                   	   
		txtAreaResultados.setTabSize(2);
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		
		if(StartGeraCode.erroSintatico == ""){
			label.setForeground(Color.decode("#006600"));			
			txtAreaResultados.setForeground(Color.decode("#003366"));   			
		}
		else{
			label.setForeground(Color.red);
			txtAreaResultados.setForeground(Color.red); 
		}
		txtAreaResultados.setText(getResultado());	
		label.setText(getErros());	
		
		Lexcontainer.add(label);
		
		txtAreaResultados.setFont(Font.decode("Verdana"));
		txtAreaResultados.setSelectionColor(Color.decode("#10377C"));
	
		op1 = new JButton( "   Visualizar Erro   " );
		op2 = new JButton( "   Gramatica   " );
		Lexcontainer.add(txtAreaResultados);  	
		LexscrollPane = new JScrollPane( txtAreaResultados, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );        
		op1.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent actionEvent){  					
						if(StartGeraCode.erroSintatico != ""){
							txtAreaResultados.setForeground(Color.decode("#003366")); 
						}
						else{  				        
							txtAreaResultados.setForeground(Color.decode("#003366"));     							
						}
						txtAreaResultados.setText(getResultado());
						label.setText(getErros());
					}
				}
		);
		op2.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent actionEvent){  				        
						txtAreaResultados.setForeground(Color.decode("#003366"));                                                              
						String resulta = "";				    
						resulta = gramatica;
						txtAreaResultados.setText(resulta);
					}
				}
		);
		
		Lexcontainer.add(op1);
		Lexcontainer.add(op2);
		frame.setSize( 350, 300 );
		Lexcontainer.setVisible( true );
		Lexcontainer.add( LexscrollPane );
		
		txtAreaResultados.setEditable( false );
		txtAreaResultados.setFocusable(true);
		frame.setResizable(false);        
		frame.setLocationRelativeTo(interfaceAux);
		frame.setVisible(true);
	}
	/** Metodo que verifica se ja existe ou nao instancia de FrameSintatica criada*/
	public static FrameSintatica getInstance(UI interfaceAux, String txtResultado, String erros){
		if(instance == null)
			instance = new FrameSintatica(interfaceAux,txtResultado,erros);
		else{
			instance.setResultado(erros);
			instance.setErros(txtResultado);
			label.setText(txtResultado);
			txtAreaResultados.setText(erros);
			
			frame.setVisible(true);
			frame.requestFocus();
		}
		return instance;
	}
	/** seta os erros da analise sintatica*/
	public void setErros(String erros) {
		this.erros = erros;		
	}
	/** retorna os erros da analise sintaticas*/
	public String getErros() {
		return erros;
	}
	/** seta o resultado da analise*/
	public void setResultado(String resultado) {
		this.resultado = resultado;		
	}
	/** retorna o resultado da analise sintatica*/
	public String getResultado() {
		return resultado;
	}
}
