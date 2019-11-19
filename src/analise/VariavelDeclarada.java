/* Projeto MACP - Compilador Portugol / portugol.sourceforge.net
 * @authors: Daniel Lima Gomes Junior - CP03122-53
 *          Leandro Sousa Marques - CP03105-52 
 */
package analise;

/**
 * @authors Leandro e Daniel
 *
 */
/**
 * Classe que contem os tipos utilizados no codigo de portugues estruturado
 */
public class VariavelDeclarada {
	//private VariavelDeclarada next;
	private String repr;
	private String tipo;
	private int indexBegin;
	private int indexEnd;
	private int tipoBasico;
	
	/**
	 * Construtor da Classe VariavelDevlarada
	 */	
	public VariavelDeclarada() {
		repr = "";
		tipo = "";
		tipoBasico = 0;
		indexBegin = indexEnd = 0;
	}
	/**
	 * Construtor de VariaveisDeclaradas que deve ser usado quando
	 * a variavel declarada for de um tipo Basico.
	 * Recebe como argumento a representacao, um String que deve ser "BASICO"
	 * e um inteiro para qual dos tipos basicos
	 */
	public VariavelDeclarada(String rp, String tp, int tpB) {
		repr = rp;
		tipo = tp;
		tipoBasico = tpB;
		indexBegin = indexEnd = 0;
	}
	
	/**
	 * Construtor de VariaveisDeclaradas que deve ser usado quando
	 * a variavel declarada for de um tipo Vetor.
	 * Recebe como argumento a representa��o, um String que deve ser "VETOR"
	 * e um inteiro para o indice inicial, outro para o indice final
	 * e outro para indicar qual dos tipos b�sicos
	 */
	public VariavelDeclarada(String rp, String tp, int iB, int iE, int tpB) {
		repr = rp;
		tipo = tp;
		indexBegin = iB;
		indexEnd = iE;
		tipoBasico = tpB;
	}
	
	/** Retorna a representa��o da vari�ve declarada */
	public String getRepr(){
		return repr;
	}
	
	/** Retorna o tipo da vari�vel que foi declarada ( BASICO ou VETOR )*/
	public String getTipo(){
		return tipo;
	}
	
	/** Retorna o tipo da vari�vel ou do vetor que foi declarado(a) */
	public int getTipoBasico(){
		return tipoBasico;
	}
	
	/** Retorna o indexador inicial de vari�vel tipo vetor */
	public int getIndexBegin(){
		return indexBegin;
	}
	
	/** Retorna o indexidor final de variavel tipo vetor */
	public int getIndexEnd(){
		return indexEnd;
	}
	
	/** M�todo para auterar a representa��o da variavel na tabela*/
	public void setRepr( String rp ){
		repr = rp;
	}
	
	/** M�todo para auterar a identifica��o de tipo da vari�vel na
	 *  tabela ( BASICO ou VETOR)*/
	public void setTipo( String tp ){
		tipo = tp;
	}	
	
	/** M�todo para auterar a o tipo b�sico da vari�vel na tabela*/
	public void setTipoBasico( int tp ){
		tipoBasico = tp;
	}
	
	/** M�todo para auterar o �ndece inicial de vari�vel tipo vetor na tabela*/
	public void setIndexBegin( int idx ){
		indexBegin = idx;
	}
	
	/** M�todo para auterar o �ndece final de vari�vel tipo vetor na tabela*/
	public void setIndexEnd( int idx ){
		indexEnd = idx;
	}
	
	/** Verifica se o tipo da variavel � BASICO */
	public boolean isBasico(){
		if ( tipo == "BASICO" )
			return true;
		else
			return false;
	}
	
	/** Verifica se o tipo da variavel � VETOR */
	public boolean isVetor(){
		if ( tipo == "VETOR" )
			return true;
		else
			return false;
	}
	
	/** Verifica se o tipo da variavel � MODULO */
	public boolean isModulo(){
		if ( tipo == "MODULO" )
			return true;
		else
			return false;
	}
	
	/** Verifica se o referenciamento ao vetor foi feito corretamento */
	public int isIndiceValido(int indice){
		if ( indice >= indexBegin && indice <= indexEnd )
			return indice - indexBegin;
		else
			return -1;
	}

}
