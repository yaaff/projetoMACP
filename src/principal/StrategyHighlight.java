package principal;

import javax.swing.JTextPane;

/** Interface do Strategy para fazer Highlight */
public interface StrategyHighlight {
	/** metodo da interface que fara o highlight do codigo-fonte */	
	public void fazerHighlight(JTextPane aux);	
}
