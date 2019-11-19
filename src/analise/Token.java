package analise;


/* Projeto MACP - Compilador Portugol / portugol.sourceforge.net
 * @authors: Daniel Lima Gomes Junior - CP03122-53
 *          Leandro Sousa Marques - CP03105-52 
 */
/**
 * Classe que cria e reconhece os tokens armazenados em buffer
 */
public class Token {
	
	/**
	 * 
	 * @uml.property name="next"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private Token next;
	
	private int clas;
	private String classe;
	private String repr;
	private int linha;
	private int coluna;
	private String nomeArquivo;
	
	/** Registro dos tokens */
	public Token() {
		next = null;
		clas = 0;
		classe = "Erro Lexico";
		repr = "";
		linha = 1;
		coluna = 1;
		nomeArquivo = "";
	}
	/** Altera o conteudo do token */
	public void setToken( Token tkn ){
		classe = tkn.getClasse();
		repr = tkn.getRepr();
		linha = tkn.getLinha();
		coluna = tkn.getColuna();
		nomeArquivo = tkn.getNomeArquivo();
		next = tkn.getNext();
	}
	
	/**
	 * 
	 * @uml.property name="next"
	 */
	/** Altera a referencia para o proximo token */
	public void setNext(Token tkn) {
		next = tkn;
	}
	
	/**
	 * 
	 * @uml.property name="classe"
	 */
	/** Altera a classe do token */
	public void setClasse(String cl) {
		classe = cl;
	}
	
	/** Altera a representacao inteira da classe do token  */
	public void setClas(int cl) {
		clas = cl;
	}
	
	/** Altera a representacao inteira da classe do token  */
	public void setClas() {
		clas = reprInteira();
	}
	
	/**
	 * 
	 * @uml.property name="repr"
	 */
	/** Altera a representacao do token */
	public void setRepr(String rep) {
		repr = rep;
	}
	
	/**
	 * 
	 * @uml.property name="linha"
	 */
	/** Altera a linha do token */
	public void setLinha(int lin) {
		linha = lin;
	}
	
	/**
	 * 
	 * @uml.property name="coluna"
	 */
	/** Altera a coluna do token */
	public void setColuna(int col) {
		coluna = col;
	}
	
	/**
	 * 
	 * @uml.property name="nomeArquivo"
	 */
	/** Guarda o nome do arquivo carregado com o codigo fonte */
	public void setNomeArquivo(String nArq) {
		nomeArquivo = nArq;
	}
	
	/**
	 * 
	 * @uml.property name="next"
	 */
	/** Obtem uma referencia do proximo token */
	public Token getNext() {
		return next;
	}
	
	/**
	 * 
	 * @uml.property name="classe"
	 */
	/** Obtem a classe do token */
	public String getClasse() {
		return classe;
	}
	
	/** Obtem um inteiro que representa classe do token */
	public int getClas() {
		return clas;
	}
	
	/**
	 * 
	 * @uml.property name="repr"
	 */
	/** Obtem a representacao do token */
	public String getRepr() {
		return repr;
	}
	
	/**
	 * 
	 * @uml.property name="linha"
	 */
	/** Obtem a linha do token */
	public int getLinha() {
		return linha;
	}
	
	/**
	 * 
	 * @uml.property name="coluna"
	 */
	/** Obtem a coluna do token */
	public int getColuna() {
		return coluna;
	}
	
	/**
	 * 
	 * @uml.property name="nomeArquivo"
	 */
	/** Obtem o nome do arquivo que contem o codigo fonte */
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	
	
	/** Verifica se a representacao do String argumento eh um dos tokens de dois
	 simbolos ( [[, ]], <-, <>, <=, >= e .. ).
	 Retorna verdadeiro ou falso*/
        //alterado em 23/03/2012 - daniellimahp
	public boolean isTokenDuplo( String st ){
		String s = "[[? ]]? <= >= <>? ..� <-";
		if( s.indexOf( st ) != -1 )
			return true;
		else
			return false;
	}
	/** Verifica se a representacao eh um token duplo */
	public boolean isTokenDuplo(){
		return isTokenDuplo(repr);
	}
	/** Verifica se a representacao do token eh uma palavra reservada da
	 linguagen. Retorna verdadeiro ou falso*/
	public int isPalavraReservada(){
		String palavrasReservadas[] = {"variaveis","inteiro","real","logico",
				"cadeia","caractere","inicio","fim","ler","escrever","se","entao",
				"senao","enquanto","faca","repita","ate","vetor","de"};
		
		String palavrasReservadas2[] = {"para", "passo","modulo"};//adicionado apos a elaboracao
		for ( int i = 0; i < 3; i++ ){					// do projeto, por isso que a 
			if( repr.equals( palavrasReservadas2[i] ) ) // classificacao esta fora da
				return i+81;							// faixa padrao
		}
		for ( int i = 0; i < 19; i++ ){
			if( repr.equals( palavrasReservadas[i] ) )
				return i+1;
		}
		return -1;      
	}
	
	/** Verifica se a representacao do token eh uma palavra reservada da
	 linguagen sendo uma funcao. Retorna verdadeiro ou falso*/
	public int isFuncao(){
		String funcao[] = {"pausa","novalinha","raiz","trunc","quad","abs","arred"};
		
		for ( int i = 0; i < 7; i++ ){
			if( repr.equals( funcao[i] ) )
				return 91+i;
		}
		return -1;      
	}
	
	/** Verifica se a representacao do token representa um identificador.
	 Retorna verdadeiro ou falso*/
	public int isIdentificador(){
		char c;
		
		c = repr.charAt( 0 );
		if( Character.isLetter( c ) && repr.length() <= 32 ){
			for( int i = 1; i < repr.length(); i++ ){
				c = repr.charAt( i );
				if( !( Character.isDigit( c ) || Character.isLetter( c ) || c == '_' ) ){
					return -1;
				}
			}
		}
		else
			return -1;
		
		return 20;
	}
	/** Verifica se a representacao do token representa um Numero Inteiro. */
	public int isNumeroInteiro(){
		for ( int i = 0; i < repr.length(); i++ )
			if( !Character.isDigit( repr.charAt( i ) ) )
				return -1;
		return 21;
	}
	/** Verifica se a representacao do token representa um Numero Real. */
	public int isNumeroReal(){
		if( isNumeroInteiro()==21 && getNext().isPontuador()==52 && getNext().getNext().isNumeroInteiro()==21){
			
			if(  getColuna()+getRepr().length() == getNext().getColuna() &&
					getNext().getColuna() + 1 == getNext().getNext().getColuna() ){
				
				getNext().setRepr( "." + getNext().getNext().getRepr() );
				setRepr( getRepr() + getNext().getRepr() );				
				setNext( getNext().getNext().getNext() );
				setClas( 30 );
				setClasse("Numero Real");
				return 30;
			}
		}
		return -1;
	}
	/** Verifica se a representacao do token representa uma sequencia
	 de digitos. Retorna verdadeiro ou falso*/
	public int isNumero(){
		int num;
		
		num = isNumeroReal();
		if( num != -1 )
			return num;
		
		num = isNumeroInteiro();
		
		return num;
	}
	
	/** Verifica se a representacao do token eh uma das quatro operacooes
	 b�sicas. Retorna verdadeiro ou falso*/
	public int isOperadorAritimetico(){
		String st = new String( "+-*/#%" );
		int i = st.indexOf( repr );
		if( repr.length() == 1 && i != -1 )
			return 61+i;
		
		return -1;
	}
	
	/** Verifica se a representacao do token eh um operador relacional.
	 Retorna verdadeiro ou falso*/
	public int isOperadorRelacional(){
		String st[] = {"=","<>","<",">","<=",">="};
		
		for( int i = 0; i < 6; i++ )
			if( repr.equals( st[i] ) )
				return 31+i;
			
		return -1;
	}
	/** Verifica se a representacao do token eh um operador logico.
	 Retorna verdadeiro ou falso*/
	public int isOperadorLogico(){
		String st[] = { "verdadeiro","falso","nao","e","ou" };
		
		for ( int i = 0; i < 5; i++ )
			if( repr.equals( st[i] ) )
				return 41+i;
			
		return -1;
	}
	/** Verifica se a representacao do token eh um operador atribucional:
	 menor, menos( <- ). Retorna verdadeiro ou falso*/
	public int isOperadorAtribucional(){
		String st =  "<-";
		
		if( repr.equals( st ) )
			return 22;
		
		return -1;
	}
	/** Verifica se a representacao do token eh um delimitador de bloco de
	 comandos abre ou fecha cochetes duplos. Retorna verdadeiro ou falso*/
	public int isDelimitadorDeBlocoDeComandos(){
		String st[] = {"[[","]]" };
		
		for ( int i = 0; i < 2; i++ )
			if( repr.equals( st[i] ) )
				return 23+i;
			
		return -1;
	}
	/** Verifica se a representacao do token eh um delimitador de preferencia:
	 abre ou fecha parentese. Retorna verdadeiro ou falso*/
	public int isDelimitadorDePreferencia(){
		String st[] = { "(",")" };
		
		for ( int i = 0; i < 2; i++ )
			if( repr.equals( st[i] ) )
				return 25+i;
			
		return -1;
	}
	
	/**Verifica se o token eh um delimitador de vetor: abre cochete
	 *ou fecha cochete. Reorna Verdadeiro ou falso.*/
	public int isDelimitadorDeVetor(){
		String st = new String( "[ ] " );
		int i = st.indexOf( repr );
		if( i == 0 )
			return 27;
		if( i == 2 )
			return 28;
		
		return -1;
	}
	
	/**Verifica se a representacao do token eh um dos simbolos de pontuacao.
	 Retorna verdadeiro ou falso - true or false*/
	public int isPontuador(){
		String st[] = { ";",",",".","..",":" };
		
		for ( int i = 0; i < 5; i++ )
			if( repr.equals( st[i] ) )
				return 51+i;
			
		return -1;
	}
	
	/**Verifica se a representacao do token eh uma cadeia de caracteres entre
	 aspas simples. Retorna verdadeiro ou falso - true or false.*/
	public int isLiteral(){
		
		if ( repr.charAt( 0 ) =='\'' && repr.charAt( repr.length() - 1 ) == '\'')
			return 29;
		
		return -1;
	}
	
	/**Verefica se a representacao do token eh a representacao de um token valido.
	 Retorna um string com a identificacao da classe do token, caso o token
	 seja v�lido. Retorna null, caso o token seja invalido*/
	public String isTokenValido(){
		
		if( (isPalavraReservada() > 0 && isPalavraReservada() < 20) ||
				(isPalavraReservada() > 80 && isPalavraReservada() < 84) )
			return "Palavra Reservada";
		if( isFuncao() > 90 && isFuncao() < 93 )
			return "fun��o";
		if( isNumero() == 21 )
			return "Numero inteiro";
		if( getClas() == 30 )
			return "Numero Real";
		if( isOperadorLogico() > 40 && isOperadorLogico() < 46)
			return "Operador L�gico";
		if( isIdentificador() == 20 )
			return "Identificador";
		if( isOperadorAritimetico() > 60 && isOperadorAritimetico() < 67 )
			return "Operador Aritm�tico";
		if( isOperadorRelacional() > 30 && isOperadorRelacional() < 37 )
			return "Operador Relacional";
		if( isOperadorAtribucional() == 22 )
			return "Operador Atribucional";
		if( isDelimitadorDeVetor() == 27 || isDelimitadorDeVetor() == 28 )
			return "Delimitador de Vetor";
		if( isDelimitadorDeBlocoDeComandos()== 23 || isDelimitadorDeBlocoDeComandos()== 24 )
			return "Delimitador de Bloco de Comandos";
		if( isDelimitadorDePreferencia() == 25 || isDelimitadorDePreferencia() ==26 )
			return "Delimitador de Preferencia";
		if( isPontuador() > 50 && isPontuador() < 56)
			return "Pontuador";
		if( isLiteral() == 29 )
			return "Literal";
		
		return null;
	}
	
	/** retorna a representa para um tokem com determinada 
	 * representa��o String */
	public int reprInteira(){
		
		int i = isPalavraReservada();
		if (i != -1 )
			return i;
		
		i = isFuncao();
		if (i != -1 )
			return i;
		
		
		i = isNumero();
		if (i != -1 )
			return i;
		
		i = getClas(); // verifica se � n�mero real
		if (i == 30 )
			return i;
		
		i = isOperadorLogico();
		if (i != -1 )
			return i;
		
		i = isIdentificador();
		if (i != -1 )
			return i;
		
		i = isOperadorAritimetico();
		if (i != -1 )
			return i;
		
		i = isOperadorRelacional();
		if (i != -1 )
			return i;
		
		i = isOperadorAtribucional();
		if (i != -1 )
			return i;
		
		i = isDelimitadorDeVetor();
		if (i != -1 )
			return i;
		
		i = isDelimitadorDeBlocoDeComandos();
		if (i != -1 )
			return i;
		
		i = isDelimitadorDePreferencia();
		if (i != -1 )
			return i;
		
		i = isPontuador();
		if (i != -1 )
			return i;
		
		i = isLiteral();
		if (i != -1 ) 
			return i;
		
		return -1;
	}
	
	/** Classe que retorna as caracteristicas do token */
	public String toString(){
		String str = "Classe: " + String.valueOf( classe ) + "   Representacao: " + repr + 
		"   Posicao: " + String.valueOf( linha ) + ":" + String.valueOf( coluna ) + " -> "; 
		return str;
	}
	
	/**
	 *  
	 * @uml.property name="startLex"
	 * @uml.associationEnd inverse="token:Compilador.StartLex" multiplicity="(0 1)"
	 * @uml.association name="Lista_tokens"
	 */
	private StartLexica lex;
	
	/**
	 * Chamada para a analise lexica 
	 * @uml.property name="startLex"
	 */
	public StartLexica getStartLex() {
		return lex;
	}
	
	/**
	 *  
	 * @uml.property name="startLex"
	 */
	public void setStartLex(StartLexica lex) {
		this.lex = lex;
	}
	
}