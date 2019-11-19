package analise;


/* Trabalho do Curso de Ci�ncia da Computa��o
 * Disciplina: Compiladores
 * Prof�.: Carlos Salles
 * Alunos: Daniel Lima Gomes Junior - CP03122-53
 *         Leandro Sousa Marques - CP03105-52
 */
/** Realiza a An�lise L�xica */
public class StartLexica{
	
	/**
	 * 
	 * @uml.property name="tk"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	/** Par�metro de refer�ncia para a lista de Tokens */
	public static Token tk = new Token();
	
	private String fonte = "";
	private int inPos = -1;
	private char in = '@';
	private  int linh = 2 ;
	private  int colun = 0 ;
	private String resultado = "";
	/** Par�metro de refer�ncia para o n�mero de erros */
	public static int erros = 0;
	
	
	/** Construtor da classe StartLex. Este m�todo realisa a an�lise l�xica do c�dico fonte 'font' passado como par�metro para an�lise */    
	public StartLexica( String font ){	
		Token token = new Token();
		tk = token;
		Token proximo = new Token();
		
		String aux = "";
		
		fonte = removeComent( font ) + "\n";        
		
		token = nextToken(); // cria uma lista de tokens com o primero token do fonte
		proximo = token;
		//continua a iplementa��o da linta at� o ultimo token do c�digo fonte
		while ( proximo != null ){
			proximo.setNext( nextToken() );
			proximo = proximo.getNext();
		}        
		
		proximo = token;
		// Realiza a an�lise dos tokens gerados
		while ( proximo != null ){
			String cl = proximo.isTokenValido();
			// realisa a classifica��o do token
			if( cl != null ){
				proximo.setClasse( cl );
				proximo.setClas();
			}
			// verifica se o token � um token inv�lido
			if( cl == null ){
				erros++;
				aux = aux + "Representação "+proximo.getRepr() + "" +
				"\n> ( Linha: " + String.valueOf( proximo.getLinha() - 1 ) + 
				"  Coluna: " + String.valueOf( proximo.getColuna() ) + 
				" )\n--------------------------------------------------------\n";               
			}
			proximo = proximo.getNext();            
		}
		resultado = resultado + aux ; // resultado dos tokens inv�lidos
		tk = token;
	}
	
	public Token getTk(){
		return tk;
	}
	
	//  ---------------------------------------------------------	
	/**M�todo que recebe o c�digo fonte da an�lise e retorna 
	 um string com o c�digo fonte sem os coment�ris */
	private String removeComent( String fon ){
		int i = 0;
		String aux = "";
		
		for( i = 0; i < fon.length() ; ){
			// pula os coment�rios
			if( ( fon.charAt( i ) == '/' ) && ( fon.charAt( i+1 ) == '/') ){
				while( fon.charAt( i ) != '\n' )
					i++;
			}		
			else{
				aux = aux.concat( fon.substring( i, i+1 ) );      
				i++;
			}
		}
		return (aux);
		
	}
	
	//  ---------------------------------------------------------
	/**
	 * Retorna um string com o resultado da an�lise l�xica.
	 * O resultado cont�m os toques inv�lidos.
	 * 
	 * @uml.property name="resultado"
	 */
	public String getResultado() {
		return resultado;
	}
	
	
	
	//  ---------------------------------------------------------
	/**M�todo que obt�m o pr�ximo token do c�digo fonte*/
	public Token nextToken(){
		
		int inicio = inPos+1;
		int fim;
		Delimitadores delimit = new Delimitadores();
		
		in = nextChar();
		if ( inPos == fonte.length()-1 ) // arquivo terminou
			return null;
		
		// pula espacos em brancos
		while ( delimit.isDelimitadorClassico( in ) ){
			in = nextChar();
			if ( inPos == fonte.length()-1 ) // arquivo terminou
				return null;
			inicio++;
		}             
		
		// trata a ocorrencia de aspas simples
		if( in == '\''){
			int i = fonte.indexOf( '\'', inPos+1 );
			if( i > inPos ){
				int j = fonte.indexOf( '\n', inPos+1 );
				if (  j > i  ){
					inPos = i;
					colun = colun + ( inPos - inicio );
				}
			}
		}
		
		// faz inPos apontar para o delimitador Token
		if( delimit.isDelimitadorToken( in ) ){
			inPos++;
			colun++;
			tk.setColuna( colun );
		}
		
		// varre o token ate o final
		while ( ! delimit.isDelimitador( in ) ){ 
			in = nextChar();
		}
		
		fim = inPos;
		// decrementa inPos para realisar proxima pesquisa a partir do delinitador
		inPos--;
		colun--;
		
		if( in == '\n'){
			linh--;
			tk.setLinha( linh );
		}
		
		char c = fonte.charAt( inPos );
		if(  c == '<' || c == '>' || c == '[' || c == ']' || c == '.' ){
			if( tk.isTokenDuplo( fonte.substring( inPos, inPos+2 ) ) ){
				inPos++;
				fim++;
				colun += 2;
				tk.setColuna( colun );
				colun--;
			}
			
		}
		
		tk.setRepr( fonte.substring( inicio, fim ) ); 
		tk.setColuna( tk.getColuna() - tk.getRepr().length() );
		
		Token tkn = new Token();
		tkn.setToken(tk);
		return tkn;
		
	}    
	//  ---------------------------------------------------------
	/**M�todo para obter o pr�ximo caractere da an�lise sint�tica e 
	 estabelecer a pose��o do �ltimo caractere do token no c�digo fonte */
	private char nextChar(){
		inPos++;
		char ch = fonte.charAt( inPos );
		if( ch == '\n' ){
			tk.setLinha( linh );
			linh++;
			colun++;
			tk.setColuna( colun );
			colun = 0;
		}
		else{
			tk.setLinha( linh );
			colun ++;
			tk.setColuna( colun );
		}
		return ch;
	}
	
	//  ---------------------------------------------------------
	
	/**
	 * Retorna o numero de tokens inv�lidos do c�digo 
	 * fonte que foi realizada a an�lise l�xica
	 * 
	 */
	public int getErros() {
		return erros;
	}
	
	private Token token;
	
	/**
	 * Retorna um token da lista de tokens 	
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * Inclui um token da lista de tokens 
	 */
	public void setToken(Token token) {
		this.token = token;
	}
		
	private Delimitadores delimitadores;
	
	/** retorna os delimitadores*/
	public Delimitadores getDelimitadores() {
		return delimitadores;
	}
	
	/**
	 *  
	 * Seta os delimitadores 
	 */
	public void setDelimitadores(Delimitadores delimitadores) {
		this.delimitadores = delimitadores;
	}
	
}