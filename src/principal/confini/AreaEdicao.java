package principal.confini;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JPanel;
import javax.swing.JTextPane;

/**
 *
 * @author Welyab da Silva Paula
 */
public class AreaEdicao extends JPanel implements ConteudoSalvavel {

    private File file;
    private JTextPane textPane;

    public AreaEdicao() {
        setLayout(new BorderLayout());
        file = null;
        textPane = new JTextPane();
    }

    public String getTexto() {
        return textPane.getText();
    }

    public File getLocalDestino() {
        return file;
    }
}
