package analise;

import java.util.Hashtable;
import java.util.StringTokenizer;


/* Trabalho do Curso de Ci�ncia da Computa��o
 * Disciplina: Compiladores
 * Prof�.: Carlos Salles
 * Alunos: Daniel Lima Gomes Junior - CP03122-53
 * 		   Leandro Sousa Marques - CP03105-52
 */

/**
 * Realiza a Gera��o de C�digo 
 */

public class StartSintatica {
	/** Identificador de retorno de erro da An�lise Sintatica */
	public static String erroSintatico = "";
	/** Retorna o codigo gerado apos a execucao da analise sintatica */
	public static String codigoGerado = "";
	private String codigoAux = "";	
	private int ident = 0;
	private Hashtable tabelaHash;
	private VariavelDeclarada variavel;
	/** Lista de tokens sobre a qual ser� feita a an�lise sint�tica */
	public Token tk;
	
	
	/** Realiza a An�lise Sintatica */
	public StartSintatica( Token tk ) {		
		tabelaHash = new Hashtable();
		codigoGerado = "";
		this.tk = tk;
		variavel = new VariavelDeclarada();
		erroSintatico = prog();
	}
	
	/**
	 * M�todo para realizar identa��o do c�digo gerado
	 * 
	 */
	private void identa() {
		for( int i = 1; i <= ident; i++)
			codigoGerado += "    ";		
	}
	
	/**
	 * M�todo para obter o string erroSintatico
	 * 
	 */
	public String getErroSintatico() {
		return erroSintatico;
	}
	
	/** M�todo para retornar a linha onde ocorreu o primeiro erro */
	private String erro( String err ){		
		return "Erro linha " + String.valueOf( tk.getLinha() - 1 ) + ", coluna "
				+ String.valueOf( tk.getColuna() + "\n	" + err );
	}
	
	/** M�todo para a regra de produ��o
	 * PROG -> DEC incio LIST_CMD fim. */
	private String prog(){
		String err = "";
		
		if (tk.getNext() == null )
			return err = erro("C�digo Incompleto");
		
		err = dec();// chama o m�todo dec(tk) para o n�o-terminal DEC
		if ( err != "" )
			return err;
		
		if ( tk.getClas() == 7 && tk.getNext() != null ){//consome o terminal "inicio"
			tk = tk.getNext();
			codigoGerado += "main(void){";
		}
		else{			
				err = erro("Estrutura \"variaveis (...) inicio (...) fim.\" incorreta.");
			return err;
		}
		
		err = list_cmd();// chama o m�todo list_cmd(tk) para o n�o terminal LIST_CMD
		if ( err != "" )
			return err;
		
		String posicaoFim = "";
		
		if ( tk.getClas() == 8 ){//consome o terminal "fim"
			posicaoFim = erro("Esperando \".\"");
			tk = tk.getNext();
			codigoGerado += "\n}";
		}
		else{
			
			err = erro("Esperando \"fim\".");
			return err;
		}		
		
		if ( tk == null )
			return err = posicaoFim;
		if ( tk.getClas() == 53 ) //consome o terminal "."
			tk = tk.getNext();
		else
			return err = erro("Esperando \".\"");
		
		if( tk != null ){
			err = erro("Estrutura \" " + tk.getRepr() + "\" inv�lido.");
			return err;
		}
		return err;
	}
	
	/** M�todo para a regra de produ��o
	 * DEC -> variaveis LIST_DECL             */
	private String dec(){
		String err = "";
		codigoGerado += "#include <iostream.h>" +
		"\nusing namespace std;\n\n";
		
		if( tk.getClas() == 1 ){ // consome o terminal "variaveis"
			tk = tk.getNext();			
		}
		else			
			return err;
		
		err = list_decl();
		if( err!= "")
			return err;
		
		return err;
	}
	
	/** M�todo para a regra de produ��o
	 * LIST_DECL -> LISTA : TIPO_VAR ; LIST_DECL | e        */
	private String list_decl(){
		String err = "";
		
		if ( tk.getNext() == null )
			return err = erro("Estrutura \"variaveis (...) inicio (...) fim.\" incorreta.");
		if( tk.getClas() == 7 ) //transi��o vazia (7 = inicio)
			return err;
		
		err = lista();// reconhece a lista de vari�veis
		if( err != "" )
			return err;
		
		if( tk.getClas() == 55 ){// consome o terminal ":"
			tk = tk.getNext();			
		}
		else{
			err = erro("Esperando \",\" ou \":\"");
			return err;
		}
		
		err = tipo_var();// verica se as variaveis s�o de tipo v�lido
		if( err != "" )
			return err;
		
		if( tk.getClas() == 51 && tk.getNext() != null){// verifica se h� o terminal ";" no fim da declara��o
			codigoGerado += codigoAux + ";\n";
			tk = tk.getNext();			
			codigoAux = "";	
		}
		else{
			if(tk.getNext() == null)
				return err = erro("Estrutura \"variaveis (...) inicio (...) fim.\" incorreta.");
			
			return err = erro("Esperando \";\"");
		}
		err = list_decl(); // verifica se h� mais declara��es
		
		return err;
	}
	
	/** M�todo para a regra de produ��o
	 * LISTA -> "identificador" LIST_VAR   */
	private String lista(){
		String err ="";
		
		if( tk.getClas() == 20 ){// verifica se a variavel � representada por um identificador
			codigoAux += tk.getRepr();						
			tk = tk.getNext();			
		}
		else
			return err = erro("Identificador \""+ tk.getRepr() + "\" inválido.");
		
		err = list_var();// verifica se h� mais de uma vari�vel na declara��o
		
		return err;
	}
	
	/** M�todo para a regra de produ��o
	 * LIST_VAR -> , "identificador" LIST_VAR | e       */
	private String list_var(){
		String err = "";
		
		if( tk.getClas() == 52 ){// verivica se as vari�veis s�o separadas por virgula
			codigoAux += tk.getRepr();
			tk = tk.getNext();
		}
		else          // trasi��o vazia
			return "";
		
		if( tk.getClas() == 20 ){// verifica se a variavel � representada por um identificador
			codigoAux += tk.getRepr();
			tk = tk.getNext();
		}
		else
			return err = erro("Identificador \""+ tk.getRepr() + "\" inválido.");
		
		err = list_var(); // verifica ocorr�ncia de novas vari�veis
		
		return err;
	}
	
	/** M�todo para a regra de produ��o
	 * TIPO_VAR -> TIPO | vetor [ "n�mero" .. "n�mero" ] de TIPO  */
	private String tipo_var(){
		String err ="";
		int inicio;
		int fim;
		int tipoBasico;
		
		tipoBasico = tipo();// verifica se o tipo da vari�vel � um dos tipos b�sicos
		if( tipoBasico > 1 && tipoBasico < 7 ){// gambi
			StringTokenizer aux = new StringTokenizer(codigoAux);
			while ( aux.hasMoreTokens()){
				String rep = aux.nextToken(",");
				if( tabelaHash.containsKey(rep) )
					return "Variável \"" + rep + "\" declarada mais de uma vez.";
				tabelaHash.put( rep , new VariavelDeclarada( rep,"BASICO", tipoBasico ));
			}
			return err;
		}
		
		if( tk.getClas() == 18 ){// consome o terminal "vetor"			
			tk = tk.getNext();
			err = "";
		}	
		else
			return err = erro("Esperando o tipo da variável.");
		
		if( tk.getClas() == 27 ) // consome o terminal "["
			tk = tk.getNext();
		else
			return err = erro("Esperando \"[\".");
		
		if( tk.getClas() == 21 ){// verifica se o terminal � um n�mero
			inicio = Integer.parseInt(tk.getRepr());
			tk = tk.getNext();
		}
		else
			return err = erro("Argumento \"" + tk.getRepr() + "\" inválido.");
		
		if( tk.getClas() == 54 )// consome o terminal ".."
			tk = tk.getNext();
		else
			return err = erro("Esperando \"..\"");
		
		if( tk.getClas() == 21 ){// verifica se o terminal � um n�mero
			fim = Integer.parseInt(tk.getRepr()); 			
			tk = tk.getNext();
		}
		else
			return err = erro("Argumento \"" + tk.getRepr() + "\" inválido.");
		
		if ( inicio >= fim )
			return err = erro("Limite inferior não pode ser maior ou \n\t" +
								"igual que Limite superior.");
		
			if( tk.getClas() == 28 ){// verifica se o terminal � um "]"
			tk = tk.getNext();
		}
		else
			return err = erro("Esperando \"..\".");
		
		if( tk.getClas() == 19 )// verifica se o terminal � um "de"
			tk = tk.getNext();
		else
			return err = erro("Esperando termo \"de\".");
		
		tipoBasico = tipo();
		if( tipoBasico < 2 || tipoBasico > 6 )
			return err = erro("Esperando o tipo do vetor.");
		
		StringTokenizer aux = new StringTokenizer(codigoAux);
		while ( aux.hasMoreTokens()){
			String rep = aux.nextToken(",");
			if( tabelaHash.containsKey(rep) )
				return "Variável \"" + rep + "\" declarada mais de uma vez.";
			codigoGerado += rep + "[" + String.valueOf( fim - inicio + 1 ) + "]";
			if( aux.hasMoreTokens())
				codigoGerado += ",";
			tabelaHash.put( rep , new VariavelDeclarada( rep,"VETOR", inicio, fim, tipoBasico ) );
		}
		codigoAux = "";
		return err;
	}
	
	/** M�todo para a regra de produ��o
	 * TIPO ->  inteiro | real | logico | cadeia | caractere   */
	private int tipo( ){
		int tip = tk.getClas();
		//verifica��o dos tipo de variavel declarada e convers�o para codigo c++
		if( tip > 1 && tip < 7 ){
			switch( tip ){
				case 2:{
					codigoGerado += "int ";
					break;
				}
				case 3:{
					codigoGerado += "float ";
					break;
				}
				case 4:{
					codigoGerado += "bool ";
					break;
				}				
				case 5:{
					codigoGerado += "string ";
					break;
				}				
				case 6:{
					codigoGerado += "char ";
					break;
				}				
			}			
			tk = tk.getNext();
			return tip;	
		}
		
		return 546;
	}
	
	/** M�todo para a regra de prod��o
	 * LIST_CMD -> CMD ; LIST_CMD | e        */
	private String list_cmd(){
		String err = "";
		
		if (tk.getNext() == null )
			return err = erro("Estrutura \"variaveis (...) inicio (...) fim.\" incorreta.");
		
		//  				fim					 ate					]]
		if ( tk.getClas() == 8 || tk.getClas() == 17 || tk.getClas() == 24 )
			return err;
		
		err = cmd();   // analisa o primeiro comando da lista ou sub-lista de comandos/
		if( err != "" )
			return err;
		
		if( tk.getClas() == 51 && tk.getNext() != null ){ // verifica se h� um terminal ";" separando os comandos
			tk = tk.getNext();
			codigoGerado += ";";
		}
		else{
			if(tk.getNext() == null )
				return err = erro("Estrutura \"variaveis (...) inicio (...) fim.\" incorreta.");
			return err = erro("Esperando \";\""); 
		}
		
		err = list_cmd();  // chamada recursiva para analisar v�rios comandos
		
		return err;
	}
	
	/** M�todo para a regra de prod��o
	 * CMD -> ler LISTA2 | escrever LISTA3 | enquanto EXPR faca CMD | repita LIST_CMD ate EXPR
	 * 		  | se EXPR entao CMD CMD2 | [[ LIST_CMD ]] | "identificador" ID2 <- EXP */
	private String cmd(){
		String err = "";
		
		if( tk.getClas() == 9 ){ // verifica a ocorr�ncia do comando "ler"
			tk = tk.getNext();			
			codigoGerado += "\n";
			ident ++;
			identa();
			codigoGerado += "cin >> ";
			err = lista2();// verifica vari�veis a serem lidas ( id ou id[indice]  )
			ident --;
			
			return err;
		}
		
		if( tk.getClas() == 10 ){ // verifica a ocorr�ncia do comando "escrever"
			tk = tk.getNext();
			codigoGerado += "\n";
			ident ++;
			identa();
			codigoGerado += "cout << ";
			err = lista3();// verifica vari�veis a serem escritas ( id ou id[indice] ou 'literal' )
			ident --;
			
			return err;
			
		}
//**********************
		if( tk.getClas() == 91 ){ // verifica a ocorr�ncia do comando "pause"
			tk = tk.getNext();
			codigoGerado += "\n";
			ident ++;
			identa();
			codigoGerado += "getch()";
			ident --;
			
			return err;
			
		}
		if( tk.getClas() == 92 ){ // verifica a ocorr�ncia do comando "novalinha"
			tk = tk.getNext();
			codigoGerado += "\n";
			ident ++;
			identa();
			codigoGerado += "cout << endl";
			ident -= 1;
			
			return err;
			
		}
/*		if( tk.getClas() == 93 ){ // verifica a ocorr�ncia do comando "raiz"
			tk = tk.getNext();
			codigoGerado += "sqrt(";
			err = id();
			if ( err != "" )
				return err;
			codigoGerado += ")";
			return err;
		}
 */	
//***********************
		if( tk.getClas() == 14 ){ // verifica a ocorr�ncia do comando "enquanto"
			codigoGerado += "\n";
			ident += 1;
			identa();
			codigoGerado += "while (";
			tk = tk.getNext();
			err = expr();// verifica a express�o
			if( err != "" )
				return err;
			
			if ( tk.getClas() == 15 ){// verifica a ocorr�ncia do terminal "faca"
				codigoGerado += ")";
				tk = tk.getNext();
				
				err = cmd();
				if ( err != "" )
					return err;
			}
			else
				return err = erro("Esperando termo \"faca\".");
			
			ident--;
			
			return err;
		}
		
		if( tk.getClas() == 16 ){ // verifica a ocorr�ncia do comando "repita"
			codigoGerado += "\n";
			ident ++;
			identa();
			codigoGerado += "do {";
			tk = tk.getNext();
			
			err = list_cmd();// verifica os comandos do comando "repita"
			if( err != "" )
				return err;
			
			if ( tk.getClas() == 17 ){// verifica a ocorr�ncia do terminal "ate"
				codigoGerado += "\n";
				identa();
				codigoGerado += "}";
				codigoGerado += "while ( !(";
				tk = tk.getNext();
				
				err = expr();
				codigoGerado += ") )";
				if ( err != "" )
					return err;
			}
			else
				err = erro("Esperando termo\"ate\".");
			
			ident --;
			
			return err;
		}
		
		if( tk.getClas() == 11){ // verifica a ocorr�ncia do comando "se"
			tk = tk.getNext();
			codigoGerado += "\n"; 
			ident++;
			identa();
			codigoGerado += "if (";
			
			err = expr();
			if (err != "")
				return err;
			
			if ( tk.getClas() == 12 ){ // verifica a ocorr�ncia do terminal "entao"
				tk = tk.getNext();
				codigoGerado += ")";// tirei o {
			}
                        else
				return err = erro("Esperando termo \"entao\".");
			
			err = cmd();
			
			if( err != "")
				return err;
			
			err = cmd2(); // verifica a ocor�ncia do "senao"
			ident--;
			return err;
		}		
		
		if( tk.getClas() == 23){ // verifica a ocorr�ncia do comando "[["
			tk = tk.getNext();
			codigoGerado += "\n";
			identa();
			codigoGerado += "{";
			err = list_cmd();
			if (err != "")
				return err;
			
			if ( tk.getClas() == 24 ){ // verifica a ocorr�ncia do terminal "]]"
				tk = tk.getNext();
				codigoGerado += "\n";
				identa();
				codigoGerado += "}";
			}
			else
				err = erro("Esperando \"]]\"");

			return err;
		}
		
		if( tk.getClas() == 20 ){ // verifica a ocorr�ncia do "identificador "
			if(!(tabelaHash.containsKey(tk.getRepr()))){
				return err = erro("Variável \"" + tk.getRepr() + "\" não declarada.");
			}
			variavel = (VariavelDeclarada) tabelaHash.get(tk.getRepr());
			codigoGerado += "\n";
			ident ++;
			identa();
			codigoGerado += tk.getRepr();
			tk = tk.getNext();			
			
			err = id2();
			if( err != "" )
				return err;
			
			if ( tk.getClas() == 22 ){ // VERIFICA A OCORRENCIA DE "<-"
				tk = tk.getNext();
				codigoGerado += " = ";
			}
			else
				return err = erro("Esperando \"<-\".");
			
			err = exp();
			ident--;
		}
		
		//					fim					 ate					]]
		if ( tk.getClas() != 8 && tk.getClas() != 17 && tk.getClas() != 24 && tk.getClas() != 51)
			return err = erro("\"" + tk.getRepr() + "\" não representa comando válido.");
		
		return err;
	}
	/** M�todo para a regra de produ��o
	 * CMD2 -> senao CMD | e   */
	private String cmd2(){
		String err = "";
		
		if ( tk.getClas() == 13){//senao
			codigoGerado += ";";
			identa();
			codigoGerado += "\n";
			identa();
			codigoGerado += "else";
			tk = tk.getNext();
			
			err = cmd();
			if ( err != "")
				return err;
		}
		
		
		return err; 
	}
	
	/** M�todo para a regra de produ��o
	 * LISTA2 -> "identificador" LIST_VAR2   */
	private String lista2(){
		String err ="";
		
		if( tk.getClas() == 20 ){// verifica se a variavel � representada por um identificador
			if(!(tabelaHash.containsKey(tk.getRepr()))){
				return err = erro("Variável \"" + tk.getRepr() + "\" não declarada.");
			}
			variavel = (VariavelDeclarada) tabelaHash.get(tk.getRepr());
			codigoGerado += tk.getRepr();
			tk = tk.getNext();			
		}
		else
			return err = erro("Identificador \"" + tk.getRepr() + "\" inválido.");
		
		err = list_var2();// verifica se h� mais de uma vari�vel na declara��o
		
		return err;
	}
	
	/** M�todo para a regra de produ��o
	 * LIST_VAR2 -> e | , LISTA2 | [ INDICE ] LIST_VAR2             */
	private String list_var2(){
		String err = "";
		if( tk.getClas() == 52 ){//virgula (',') 
			tk = tk.getNext();
			codigoGerado += ";\n";
			identa();
			codigoGerado += "cin >> ";
			
			err = lista2();
			if( err != "" )
				return err;
		}
		
		if( tk.getClas() == 27 ){ // consome o terminal "["
			codigoGerado += "[";
			tk = tk.getNext();
			
			err = indice();
			if( err != "" )
				return err;
			
			if( tk.getClas() == 28 ){ // consome o terminal "]"
				codigoGerado += "]";
				tk = tk.getNext();
			}
			else
				return err = erro("Esperando \"]\"");
			
			err = list_var2();
			if( err != "" )
				return err;
		}
		else
			if( variavel.getTipo() == "VETOR" )
				return erro("Esperando \"[\"." );
		
		return err; // transi��o vazia
	}
	
	/** M�todo para a regra de produ��o
	 * LISTA3 -> "identificador" LIST_VAR3 | 'literal' LIST_VAR3  */
	private String lista3(){
		String err ="";
		
		if( tk.getClas() == 20 ){// verifica se a variavel � representada por um identificador
			if(!(tabelaHash.containsKey(tk.getRepr()))){
				return err = erro("Variável \"" + tk.getRepr() + "\" não declarada.");
			}
			variavel = (VariavelDeclarada) tabelaHash.get(tk.getRepr());
			codigoGerado += tk.getRepr();
			tk = tk.getNext();
		}
		else
			if ( tk.getClas() == 29 ){ // verifica se o terminal � um 'literal'
				String aux = tk.getRepr();
				aux = aux.replaceAll("\"", "\\\\\"");
				aux = aux.replace('\'', '\"' );
				codigoGerado += aux;
				tk = tk.getNext();
			}
			else
				return err = erro("Esperando Variável ou Literal de Cadeia");
		
		err = list_var3();// verifica se h� mais de uma vari�vel para serem escritas
		
		return err;
	}
	
	/** M�todo para a regra de produ��o
	 * LIST_VAR3 -> e | , LISTA3 | [ INDICE ] LIST_VAR3             */
	private String list_var3(){
		String err = "";
		
		if( tk.getClas() == 52 ){ // virgula (',')
			codigoGerado += ";\n";
			identa();
			codigoGerado += "cout << ";
			tk = tk.getNext();
			
			err = lista3();
			if( err != "" )
				return err;
		}
		
		if( tk.getClas() == 27 ){ // consome o terminal "["
			codigoGerado += "[";
			tk = tk.getNext();
			
			err = indice();
			if( err != "" )
				return err;
			
			if( tk.getClas() == 28 ){ // consome o terminal "]"
				codigoGerado += "]";
				tk = tk.getNext();
			}
			else
				return err = erro("Esperando \"]\"");
			
			err = list_var3();
			if( err != "" )
				return err;
		}
		else
			if( variavel.getTipo() == "VETOR" )
				return erro("Elemento de vetor \"" + variavel.getRepr() + " \" não indexado." );
		
		return err; // transi��o vazia
	}
	
	/** M�todo para a regra de produ��o
	 * INDICE -> "numero" | "identificador"  ID2        */
	private String indice(){
		String err ="";
		
		if ( tk.getClas() == 21 ){//numero
			int num = Integer.parseInt(tk.getRepr());
			if( num < variavel.getIndexBegin() || num > variavel.getIndexEnd() )
				return err = erro( "Índice de vetor inválido." );
			codigoGerado += String.valueOf(num - variavel.getIndexBegin() );
			tk = tk.getNext();
			variavel = new VariavelDeclarada();
			return err;
		}
		
		if( tk.getClas() == 20 ){//identificador
			if(!(tabelaHash.containsKey(tk.getRepr())))
				return err = erro("Variável \"" + tk.getRepr() + "\" não declarada.");
			
			VariavelDeclarada varAux = (VariavelDeclarada)tabelaHash.get(tk.getRepr());
			
			if ( varAux.getTipo() == "BASICO" && varAux.getTipoBasico() == 2 )
				codigoGerado += tk.getRepr() + "-" + String.valueOf(variavel.getIndexBegin());
			else
				codigoGerado += tk.getRepr();
			tk = tk.getNext();
			variavel = varAux;
			err = id2();
		}
		
		return err;
	}
	
	/** M�todo para a regra de produ��o
	 * EXP ->  verdadeiro | falso | "literal" | TERMO EXP2 | raiz ID  */
	private String exp(){
		String err = "";
		
		if( tk.getClas() == 41 || tk.getClas() == 42 ){// "verdadeiro" e "falso"
			switch (tk.getClas()){
				case 41:{
					codigoGerado += "true";
					break;
				}
				case 42:{
					codigoGerado += "false";
					break;
				}				
			}
			tk = tk.getNext();
			return err;
		}
		
		if ( tk.getClas() == 29 ){ // "literal"
			String gambiarra = tk.getRepr();
			gambiarra = gambiarra.replaceAll("'","\"");
			codigoGerado += gambiarra;			
			tk = tk.getNext();
			return err;
		}
		
		err = termo();
		if ( err != "" )
			return err;
		
		err = exp2();
		
		return err;
	}
	
	/** M�todo para a regra de produ��o
	 * TERMO ->  FATOR TERMO2         */
	private String termo(){
		String err = "";
		
		err = fator();
		if ( err != "" )
			return err;
		
		err = termo2();
		if ( err != "" )
			return err;
		
		return err;
	}
	
	/** M�todo para a regra de produ��o
	 * TERMO2 ->  OP_MULT TERMO2 | e        */
	private String termo2(){
		String err = "";
		
		if ( op_mult() ){
			
			err = fator();
			if( err != "" )
				return err;
			
			err = termo2();
			if ( err != "" )
				return err;
			
		}
		
		return err;
	}
	
	/** M�todo para a regra de produ��o
	 * FATOR ->  ( EXP )| ID            */
	private String fator(){
		String err = "";
		
		if ( tk.getClas() == 25 ){ // "("
			codigoGerado += "(";
			tk = tk.getNext();
			
			err = exp();
			if( err != "")
				return err;
			
			if( tk.getClas() == 26 ){// ")"
				codigoGerado += ")";
				tk = tk.getNext();
			}
			else
				err = erro("Esperando \")\"");
			
			return err;
		}
		
		err = id();
		
		return err;
	}
	
	/** M�todo para a regra de produ��o
	 * ID ->  "numero" | "identificador" ID2   */
	private String id(){
		String err = "";
		
		if ( tk.getClas() == 21 || tk.getClas() == 30 ){
			codigoGerado += tk.getRepr();
			tk = tk.getNext();
			return err;
		}
		
		if ( tk.getClas() == 20 ){
			if(!(tabelaHash.containsKey(tk.getRepr()))){
				return err = erro("Variável \"" + tk.getRepr() + "\" não declarada.");
			}
			codigoGerado += tk.getRepr();
			variavel = (VariavelDeclarada) tabelaHash.get(tk.getRepr());
			tk = tk.getNext();
			
			err = id2();
		}
		else
			err = erro("Identificador \"" + tk.getRepr() + "\" inválido.");
		
		return err;
	}
	
	/** M�todo para a regra de produ��o
	 * ID2 ->  e | [ INDICE ]           */
	private String id2(){
		String err = "";
		
		if ( tk.getClas() == 27 ){
			codigoGerado += "[";
			tk = tk.getNext();
			
			err = indice();
			if( err != "")
				return err;
			
			if( tk.getClas() == 28 ){
				codigoGerado += "]";
				tk = tk.getNext();
			}
			else
				err = erro("Esperando \"]\"");
		}
		else
			if( variavel.getTipo() == "VETOR" )
				return erro("Elemento de vetor \"" + variavel.getRepr() + 
							" \" não indexado." );
		
		return err;
	}	
	
	/** M�todo para a regra de produ��o
	 * EXP2 ->  OP_ADITIVO TERMO EXP2 | e   */
	private String exp2(){
		String err = "";
		
		if( op_aditivo() ){
			
			err = termo();
			if( err != "")
				return err;
			
			err = exp2();
		}		
		
		return err;
	}
	
	
	/** M�todo para a regra de produ��o
	 * OP_ADITIVO -> + | -  */
	private boolean op_aditivo(){
		
		if ( tk.getClas() == 61 || tk.getClas() == 62 ){
			codigoGerado += tk.getRepr();
			tk = tk.getNext();
			return true;
		}
		
		return false;
	}
	
	/** M�todo para a regra de produ��o
	 * OP_MULT -> * | / | % | #           */
	private boolean op_mult(){
		if ( tk.getClas() > 62 && tk.getClas() < 67 ){
			if(tk.getRepr().equals("#")){				
				codigoGerado += "/";
				tk = tk.getNext();				
			}
			else{
				codigoGerado += tk.getRepr();
				tk = tk.getNext();
			}					
		return true;
		}
		return false;
	}
	/** M�todo para a regra de produ��o
	 * EXPR ->  EXP OP_RELACIONAL EXP EXPR2  */
	private String expr(){
		String err = "";
		
		if ( tk.getClas() == 43){ //nao
			codigoGerado += "!";
			tk = tk.getNext();
		}
		
		err = exp();
		if( err != "" )
			return err;
		
		err = op_relacional();
		if( err != "" )
			return err;
		
		err = exp();
		if( err != "" )
			return err;
		
		err = expr2();
		
		return err;
	}
	
	/** M�todo para a regra de produ��o
	 * EXPR2 ->  OP_LOGICO EXP*/
	private String expr2(){
		String err = "";
		
		if ( op_logico() )
			err = expr();
		
		return err;
	}	
	
	/** M�todo para a regra de produ��o
	 * OP_LOGICO -> e | ou  */
	private boolean op_logico(){
		
		if( tk.getClas() == 44 || tk.getClas() == 45 ){
			switch(tk.getClas()){
			case 44:{
				codigoGerado += " && ";
				break;
			}
			case 45:{
				codigoGerado += " || ";
				break;
			}				
			}
			tk = tk.getNext();
			return true;
		}
		
		return false;
	}
	
	/** M�todo para a regra de produ��o
	 * OP_RELACIONAL -> < | > | <= | >= | <> */
	private String op_relacional(){
		String err = "";
		
		if( tk.getClas() > 30 && tk.getClas() < 37 ){
			switch(tk.getClas()){
				case 31:{
					codigoGerado += " == ";
					break;
				}
				case 32:{
					codigoGerado += " != ";
					break;
				}			
				case 33:{
					codigoGerado += " < ";
					break;
				}				
				case 34:{
					codigoGerado += " > ";
					break;
				}				
				case 35:{
					codigoGerado += " <= ";
					break;
				}				
				case 36:{
					codigoGerado += " >= ";
					break;
				}				
			}
			tk = tk.getNext();
		}
		else
			err = erro("\"" + tk.getRepr() + "\" n�o � operador relacional v�lido.");
		
		return err;
	}
}