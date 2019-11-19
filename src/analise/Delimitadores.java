package analise;


/* Trabalho do Curso de Ciência da Comutação
 * Disciplina: Compiladores
 * Prof�.: Carlos Salles
 * Alunos: Daniel Lima Gomes Junior - CP03122-53
 *         Leandro Sousa Marques - CP03105-52
 */
/** Verifica os tipos de delimitadores */
public class Delimitadores {

    final String delimitadores = new String(" \r\t\n;:,.+-*/%#[]()=<>");

    /** Verifica os tipos de delimitadores */
    public Delimitadores() {
    }

    /** Verifica se o caractere � um delimitador de tokens */
    public boolean isDelimitador(char c) {
        int i = delimitadores.indexOf(c);

        if (i != -1) {
            return true;
        } else {
            return false;
        }
    }

    /** Verifica se o caractere � um delimitador de tokens classicos */
    public boolean isDelimitadorClassico(char c) {
        int i = delimitadores.lastIndexOf(c, 3);

        if (i != -1) {
            return true;
        } else {
            return false;
        }
    }

    /** Verifica se o caracter � um delimitador e token ao mesmo tempo */
    public boolean isDelimitadorToken(char c) {
        int i = delimitadores.indexOf(c, 4);

        if (i != -1) {
            return true;
        } else {
            return false;
        }
    }
    /** Variavel do tipo StartLex */
    private StartLexica lex;

    /** Retorna a variavel Lex do tipo StartLex */
    public StartLexica getStartLex() {
        return lex;
    }

    /** Seta a variavel Lex do tipo StartLex */
    public void setStartLex(StartLexica lex) {
        this.lex = lex;
    }
}
