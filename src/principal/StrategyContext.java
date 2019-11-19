package principal;

import javax.swing.JTextPane;
/** Context do padrao Strategy para Fazer Highlight */
public class StrategyContext {
	
	private StrategyHighlight highlight = null;
	/** Context do padrao Strategy para Fazer Highlight */
	public StrategyContext(){}
	/** seta a estrategia para o highlight*/
	public void setHighlighter(StrategyHighlight paramHighlight){
		this.highlight = paramHighlight;
	}
	/** retorna qual estrategia foi selecionada*/
	public StrategyHighlight getHighlighter(){
		return highlight;
	}
	/** metodo que realiza o highlight do codigo da areaDetexto setada*/
	public void FazerHighlight(JTextPane areaDeTexto){
		highlight.fazerHighlight(areaDeTexto);
	}
}
