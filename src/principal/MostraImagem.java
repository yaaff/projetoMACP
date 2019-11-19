package principal;

/* Trabalho do Curso de Ci�ncia da Computa��o
 * Disciplina: Compiladores
 * Prof�.: Carlos Salles
 * Alunos: Daniel Lima Gomes Junior - CP03122-53
 * 		   Leandro Sousa Marques - CP03105-52 
 */

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


/** Diagrama de Classe do Compilador */
public class MostraImagem extends JFrame{	
	private static final long serialVersionUID = 1L;
	private JLabel Figura;
        public static JLabel whois1, whois2;
	private Icon creditos = new ImageIcon(getClass().getResource( "img/creditos.png" ) );	
	Container container = getContentPane();    	
	  
		
	/** Diagrama de Classe do Compilador */
	public MostraImagem(int choice) throws InterruptedException {		
		setTitle(MACP.version + " - Diagrama de Classe");
		setIconImage( ( new ImageIcon( getClass().getResource( UI.icon ) ) ).getImage() );
		
		if(choice != 1)
		{
			container.setLayout( new FlowLayout());
			Creditos();
                        //new Movimentar(); //animação dos nomes - TODO
		}
	}	
	
	/** Creditos do programa */	
	public void Creditos(){		
		setTitle(MACP.version + " - Créditos");
		Figura = new JLabel(creditos);		
		container.add(Figura);
                Vector ColaboradoresArr = new Vector<String>();
                         ColaboradoresArr.add("COORDENAÇÃO:");
                         ColaboradoresArr.add("Prof. Dr. Carlos de Salles Soares Neto (UFMA)");
                         ColaboradoresArr.add("Prof. M.Sc. Daniel Lima Gomes Jr (IFMA)");
                         ColaboradoresArr.add("COLABORAÇÃO:");
                         ColaboradoresArr.add("Prof. M.Sc. Ulysses Santos Sousa (IFMA, 2008-Atual)");
                         ColaboradoresArr.add("Prof. M.Sc. Gilvan Vilarim (IFRJ, 2011-Atual)");
                         ColaboradoresArr.add("Leandro Sousa Marques (UFMA, 2006-Atual)");
                         ColaboradoresArr.add("Prof. Dr. Luciano Reis Coutinho (UFMA, 2008)");
                         ColaboradoresArr.add("Prof. Dr. Alexandre César Tavares Vidal (UFMA, 2008)");
                         ColaboradoresArr.add("ALUNOS:");
                         ColaboradoresArr.add("Hedvan Fernandes Pinto (UFMA, 2012)");
                         ColaboradoresArr.add("Welyab da Silva Paula (UFMA, 2011)");
                         ColaboradoresArr.add("Rafaela Magalhães Neri(IFMA, 2011)");

		whois1 = new JLabel("");
                String colab = "<html><center>";
                              for(int i=0; i<ColaboradoresArr.size(); i++)
                                colab += ColaboradoresArr.get(i) + "<br/>";
                              colab += "</center></html>";
                whois1.setText(colab);
		
		whois2 = new JLabel("<html><center><br/>&copy 2007-2012 MACP Compilador Portugol.</center></html>");
		container.add(whois1);                		                
		container.add(whois2);
		setSize(320,490);
		setResizable(false);
		container.setBackground(Color.decode("#AAAAAA"));
		//whois1.setFont(Font.decode("Arial"));
                whois1.setFont(new Font("Dialog",10,10));
		whois1.setFont(whois1.getFont().deriveFont(Font.BOLD));
		whois2.setFont(whois1.getFont().deriveFont(Font.BOLD));

		setLocationRelativeTo(this);		
		setVisible(true);
	}
	
}


class Movimentar extends Thread{    
    public void run(){
        JLabel titles = MostraImagem.whois1;
        int refTitles=titles.getX();
        
        int soma=0;
        while(true)
        {

            titles.setBounds(titles.getX()+5, titles.getY(), titles.getWidth(), titles.getHeight());
            soma+=5;
            if(soma==1500)
            {//o problema ta aqui.
                titles.setBounds(refTitles, titles.getY(), titles.getWidth(), titles.getHeight());
                soma=0;
            }
            try {
                this.sleep(40);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
  }
}
