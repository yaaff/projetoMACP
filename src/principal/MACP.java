package principal;

/* Trabalho do Curso de Ci�ncia da Computa��o
 * Disciplina: Compiladores
 * Prof�.: Carlos Salles
 * Alunos: Daniel Lima Gomes Junior - CP03122-53
 * 		   Leandro Sousa Marques - CP03105-52 
 */
import java.io.IOException;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.UIManager;

/** Classe Principal do Compilador de Portugu�s Estruturado */
public class MACP extends JFrame {

    private static final long serialVersionUID = 1L;
    /** Vers�o de Desenvolvimento */
    public static String version = "MACP v.5.1";

    /** Classe Principal do Compilador de Portugu�s Estruturado */
    public MACP() {
    }

    /** M�todo Main (Principal) */
    public static void main(String args[]) throws IOException {
//        for(Entry e : System.getenv().entrySet()){
//            System.out.printf("%25s : %-1s\n", e.getKey(), e.getValue());
//        }
//
//        if(true) return ;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // não faz nada
        }

        SplashScreen splash = new SplashScreen(3000);//3 segs
        splash.showSplashAndExit();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                UI aplicativo = new UI();
                aplicativo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
}
