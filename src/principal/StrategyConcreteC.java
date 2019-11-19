package principal;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import analise.Delimitadores;

/** Concrete Strategy de Fazer Highlight em Java */
public class StrategyConcreteC implements StrategyHighlight {
	/** Concrete Strategy de Fazer Highlight em Java */
	public StrategyConcreteC(){
		
	}
	public void fazerHighlight(JTextPane aux){		
		areaDeTexto = aux;
		inPos = -1;
		
		while(inPos < areaDeTexto.getText().length()){	
			int inicio = inPos+1;
			int fim;
			Delimitadores delimit = new Delimitadores();
			
			in = nextChar();
			if ( inPos >= areaDeTexto.getText().length()-1 ) // arquivo terminou
				break;
			
			// pula espacos em brancos
			while ( delimit.isDelimitadorClassico( in ) ){
				in = nextChar();
				if ( inPos >= areaDeTexto.getText().length()-1 ) // arquivo terminou
					break;
				inicio++;
			}             
			
			// trata a ocorrencia de aspas simples
			if( in == '"'){
				int i = areaDeTexto.getText().indexOf( '"', inPos+1 );
				if( i > inPos ){
					int j = areaDeTexto.getText().indexOf( '\n', inPos+1 );
					if (  j > i  ){
						inPos = i;
						colun = colun + ( inPos - inicio );
						//faz highlight
						areaDeTexto.select(inicio+1, inPos);
						append(inicio+1, inPos, areaDeTexto.getSelectedText(),2);
					}				
				}	
				else{
					continue;
				}
			}
			
			// trata a ocorrencia de comentario
			if( in == '/'){
				if(inPos < areaDeTexto.getText().length() && areaDeTexto.getText().charAt(inPos+1) == '/'){
					while(areaDeTexto.getText().charAt(inPos) != '\n')
						inPos = (inPos + 1);
				}
				areaDeTexto.select(inicio, inPos);
				append(inicio, inPos, areaDeTexto.getSelectedText(),3);
			}
			
			// faz inPos apontar para o delimitador Token
			if( delimit.isDelimitadorToken( in ) ){
				inPos = (inPos + 1);
			}
			
			
			int testLoop = 0;
			// varre o token ate o final
			while ( ! delimit.isDelimitador( in ) ){
				testLoop++;
				if(testLoop > 100)
					break;
				if(inPos < areaDeTexto.getText().length()-1)
					in = nextChar();
			}
			
			fim = inPos;
			// decrementa inPos para realisar proxima pesquisa a partir do delinitador			
			inPos = (inPos - 1);			
			
			
			//setando as cores da PR
			String palavra = areaDeTexto.getText().substring( inicio, fim );
			if(palavra.equals("int") || palavra.equals("void") || palavra.equals("float") || palavra.equals("string") || palavra.equals("double") || palavra.equals("function") || palavra.equals("for") || palavra.equals("while") || palavra.equals("repeat") || palavra.equals("do")|| palavra.equals("return")){				
				areaDeTexto.select(inicio, fim);
				append(inicio, fim, areaDeTexto.getSelectedText(),0);
			}
			if(palavra.equals("cin") || palavra.equals("cout") || palavra.equals("printf") || palavra.equals("scanf") || palavra.equals("puts") || palavra.equals("gets") || palavra.equals("if") || palavra.equals("else") || palavra.equals("unsigned") || palavra.equals("ifdef")){
				areaDeTexto.select(inicio, fim);
				append(inicio, fim, areaDeTexto.getSelectedText(),1);
			}	
			if(palavra.equals("import") || palavra.equals("main")){
				areaDeTexto.select(inicio, fim);
				append(inicio, fim, areaDeTexto.getSelectedText(),3);
			}	
			if(palavra.equals("iostream") || palavra.equals("complex")){
				areaDeTexto.select(inicio, fim);
				append(inicio, fim, areaDeTexto.getSelectedText(),2);
			}	
			if(palavra.equals("public") || palavra.equals("private")){
				areaDeTexto.select(inicio, fim);
				append(inicio, fim, areaDeTexto.getSelectedText(),4);
			}	
			
		}					
	}
	
	/** Func��o que percorre o codigo */
	public static char nextChar(){			
		inPos = inPos + 1;	
		char ch = areaDeTexto.getText().charAt( inPos );
		return ch;
	}
	
	/** Fun��o que faz aplica os tipos de cores dependendo de qual eh a palavra reservada */	
	public static void append( int inicio, int fim, String copiada, int flag ) {
			AttributeSet colorido = BOLD_BLACK;
			AttributeSet coloridoAzul = AZUL;
			AttributeSet coloridoVermelho = VERMELHO;
			AttributeSet coloridoVerde = VERDE;
			AttributeSet coloridoRoxo = ROXO;
			AttributeSet normal = BLACK;
			
			if(flag == 0){//variaveis,inicio,fim.
				areaDeTexto.setCharacterAttributes( colorido, false );
				String textoColorido = areaDeTexto.getText().substring( inicio,fim );			
				areaDeTexto.select( inicio, fim );			
				areaDeTexto.replaceSelection( textoColorido );
			}
			else if(flag == 1){//palavra reservada
				areaDeTexto.setCharacterAttributes( coloridoAzul, false );
				String textoColoridoAzul = areaDeTexto.getText().substring( inicio,fim );			
				areaDeTexto.select( inicio, fim );			
				areaDeTexto.replaceSelection( textoColoridoAzul );
			}
			else if(flag == 2){//String
				areaDeTexto.setCharacterAttributes( coloridoVermelho, false );
				String textoColoridoVermelho = areaDeTexto.getText().substring( inicio,fim );			
				areaDeTexto.select( inicio, fim );			
				areaDeTexto.replaceSelection( textoColoridoVermelho );
			}
			else if(flag == 3){//include
				areaDeTexto.setCharacterAttributes( coloridoVerde, false );
				String textoColoridoVerde = areaDeTexto.getText().substring( inicio,fim );			
				areaDeTexto.select( inicio, fim );			
				areaDeTexto.replaceSelection( textoColoridoVerde );
			}			
			else if(flag == 4){//using namespace
				areaDeTexto.setCharacterAttributes( coloridoRoxo, false );
				String textocoloridoRoxo = areaDeTexto.getText().substring( inicio,fim );			
				areaDeTexto.select( inicio, fim );			
				areaDeTexto.replaceSelection( textocoloridoRoxo );
			}	
			
			areaDeTexto.setCharacterAttributes( normal, false );			
		}
	/** Posicao de verificacao inicial do highlight*/
	public static int inPos = -1;
	/** caracter atual*/
	public static char in = '@';
	/** coluna atual*/
	public static int colun = 0 ;
	/** referecia da area de texto de Interface*/
	public static JTextPane areaDeTexto;
	/** DEFINICAO DE ESTILOS DE DOC */
	static SimpleAttributeSet BOLD_BLACK = new SimpleAttributeSet();
	static SimpleAttributeSet AZUL = new SimpleAttributeSet();
	static SimpleAttributeSet BLACK = new SimpleAttributeSet();
	static SimpleAttributeSet VERDE = new SimpleAttributeSet();
	static SimpleAttributeSet VERMELHO = new SimpleAttributeSet();
	static SimpleAttributeSet ROXO = new SimpleAttributeSet();
	static {		    
		    StyleConstants.setForeground(BOLD_BLACK, Color.black);
		    StyleConstants.setBold(BOLD_BLACK, true);
		    StyleConstants.setFontFamily(BOLD_BLACK, "Courier New");
		    StyleConstants.setFontSize(BOLD_BLACK, 14);
		    
		    StyleConstants.setForeground(AZUL, Color.blue);
		    StyleConstants.setBold(AZUL, false);
		    StyleConstants.setFontFamily(AZUL, "Courier New");
		    StyleConstants.setFontSize(AZUL, 14);		    
	
		    StyleConstants.setForeground(BLACK, Color.black);
		    StyleConstants.setBold(BLACK, false);		    
		    StyleConstants.setFontFamily(BLACK, "Courier New");
		    StyleConstants.setFontSize(BLACK, 14);
			
		    StyleConstants.setForeground(VERDE, Color.decode("#00CC00"));
		    StyleConstants.setBold(VERDE, true);		    
		    StyleConstants.setFontFamily(VERDE, "Courier New");
		    StyleConstants.setFontSize(VERDE, 14);		    
		    
		    StyleConstants.setForeground(VERMELHO, Color.red);
		    StyleConstants.setBold(VERMELHO, false);		    
		    StyleConstants.setFontFamily(VERMELHO, "Courier New");
		    StyleConstants.setFontSize(VERMELHO, 14);	
		    
		    StyleConstants.setForeground(ROXO, Color.decode("#6633CC"));
		    StyleConstants.setBold(ROXO, true);		    
		    StyleConstants.setFontFamily(ROXO, "Courier New");
		    StyleConstants.setFontSize(ROXO, 14);
	}
}
