package principal;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import analise.Delimitadores;
/** Concrete Strategy de Fazer Highlight em Portugol */
public class StrategyConcretePortugol implements StrategyHighlight {
	/** Concrete Strategy de Fazer Highlight em Portugol */
	public StrategyConcretePortugol(){
		
	}
	public void fazerHighlight(JTextPane aux){
		areaDeTexto = aux;
		int posAtual = areaDeTexto.getCaretPosition();
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
			if( in == '\''){
				int i = areaDeTexto.getText().indexOf( '\'', inPos+1 );
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
			
			// varre o token ate o final
			while ( ! delimit.isDelimitador( in ) ){
				if(inPos < areaDeTexto.getText().length()-1)
				in = nextChar();
			}
			
			fim = inPos;
			// decrementa inPos para realisar proxima pesquisa a partir do delinitador
			inPos = (inPos - 1);		
			
			//setando as cores da PR
			String palavra = areaDeTexto.getText().substring( inicio, fim );
			if(palavra.equals("variaveis") || palavra.equals("inicio") || palavra.equals("fim") || palavra.equals("modulo")){				
				areaDeTexto.select(inicio, fim);
				append(inicio, fim, areaDeTexto.getSelectedText(),0);
			}
			else if(palavra.equals("inteiro") || palavra.equals("real") || palavra.equals("logico") || palavra.equals("cadeia") || palavra.equals("caractere") || palavra.equals("ler") || palavra.equals("escrever") || palavra.equals("se") || palavra.equals("senao") || palavra.equals("entao") || palavra.equals("enquanto") || palavra.equals("faca") || palavra.equals("repita") || palavra.equals("ate") || palavra.equals("vetor") || palavra.equals("de")){
				areaDeTexto.select(inicio, fim);
				append(inicio, fim, areaDeTexto.getSelectedText(),1);
			}			
			
		}	
		areaDeTexto.setCaretPosition(posAtual);
	}
	
	/** Funcção que percorre o codigo */
	public static char nextChar(){			
		inPos = inPos + 1;	
		char ch = areaDeTexto.getText().charAt( inPos );
		return ch;
	}
	
	/** Função que faz aplica os tipos de cores dependendo de qual eh a palavra reservada */	
	public static void append( int inicio, int fim, String copiada, int flag ) {
			AttributeSet colorido = BOLD_BLACK;
			AttributeSet coloridoAzul = AZUL;
			AttributeSet coloridoVermelho = VERMELHO;
			AttributeSet coloridoVerde = VERDE;
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
			else if(flag == 3){//comentario
				areaDeTexto.setCharacterAttributes( coloridoVerde, false );
				String textoColoridoVerde = areaDeTexto.getText().substring( inicio,fim );			
				areaDeTexto.select( inicio, fim );			
				areaDeTexto.replaceSelection( textoColoridoVerde );
			}			
			
			areaDeTexto.setCharacterAttributes( normal, false );			
		}
	/** Posicao de verificacao inicial do highlight*/
	public static int inPos = -1;
	/** caracter atual*/
	public static char in = '@';
	/** coluna atual*/
	public static int colun = 0 ;
	/** referecia a area de texto de Interface*/
	public static JTextPane areaDeTexto;
	/** DEFINICAO DE ESTILOS DE DOC */
	static SimpleAttributeSet BOLD_BLACK = new SimpleAttributeSet();
	static SimpleAttributeSet AZUL = new SimpleAttributeSet();
	static SimpleAttributeSet BLACK = new SimpleAttributeSet();
	static SimpleAttributeSet VERDE = new SimpleAttributeSet();
	static SimpleAttributeSet VERMELHO = new SimpleAttributeSet();	
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
			
		    StyleConstants.setForeground(VERDE, Color.decode("#008000"));
		    StyleConstants.setBold(VERDE, false);		    
		    StyleConstants.setFontFamily(VERDE, "Courier New");
		    StyleConstants.setFontSize(VERDE, 14);		    
		    
		    StyleConstants.setForeground(VERMELHO, Color.red);
		    StyleConstants.setBold(VERMELHO, false);		    
		    StyleConstants.setFontFamily(VERMELHO, "Courier New");
		    StyleConstants.setFontSize(VERMELHO, 14);	
	}
}
