package principal;

/**  
 * Universidade Federal do Maranhao, 2006
 * Centro de Ciencia e Tecnologia
 * Desenvolvedores: Daniel Lima Gomes Junior - CP03122-53
 *      		Leandro Sousa Marques - CP03105-52
 *				Ulysses Santos Sousa - CP04133-60
 */
import java.awt.BorderLayout;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.text.DefaultEditorKit;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import analise.Compilacao;
import analise.FrameGeracao;
import principal.confini.EditarConfINI;

/**
 * IDE grafica desenvolvida para facilitar o desenvolvimento de programas em portugol *
 */
public class UI extends JFrame implements KeyListener {

    /** Setando o tamanho do TabSize do JtextPane */
    public void setTabs(JTextPane textPane, int charactersPerTab) {
        FontMetrics fm = textPane.getFontMetrics(textPane.getFont());
        int charWidth = fm.charWidth('w');
        int tabWidth = charWidth * charactersPerTab;

        TabStop[] tabs = new TabStop[10];

        for (int j = 0; j < tabs.length; j++) {
            int tab = j + 1;
            tabs[j] = new TabStop(tab * tabWidth);
        }

        TabSet tabSet = new TabSet(tabs);
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setTabSet(attributes, tabSet);
        int length = textPane.getDocument().getLength();
        textPane.getStyledDocument().setParagraphAttributes(0, length, attributes, false);
    }
//	--------------------------------------------------------------------	

    /** Cria e formata a area de edicao de texto */
    public void AreaTexto(String Texto) {
        UI.this.setJMenuBar(UI.this.barra);
        UI.this.add(UI.this.barraBotoes, BorderLayout.NORTH);

        layout = new GridBagLayout();
        container = new Container();
        container.setLayout(layout);
        containerTexto = new Container();
        containerTexto.setLayout(layout);
        constraints = new GridBagConstraints();

        // configura as restricoes do Layout para area de texto
        areaDeTexto = new JTextPane();
        AttributeSet normal = BLACK;
        areaDeTexto.setCharacterAttributes(normal, false);
        areaDeTexto.setText(Texto);

        areaDeTexto.addKeyListener(this);

        // criacao do JTextArea da Numeracao //
        //--begin--
        strNumber = " ";
        numberLine = 0;
        numberLine = getLineCount();
        for (int i = 1; i <= numberLine; i++) {
            strNumber += String.valueOf(i) + " \n ";
        }
        //--end--

//		areaNumeroLinhas = new JTextArea( strNumber, 5, String.valueOf( numberLine ).length() );
        areaNumeroLinhas = new JTextPane();
        areaNumeroLinhas.setText(strNumber);

        areaDeTexto.setSelectionColor(Color.decode("#10377C"));
        areaDeTexto.setSelectedTextColor(Color.white);
        areaNumeroLinhas.setBackground(Color.decode("#CCCCCC"));
        areaNumeroLinhas.setForeground(Color.decode("#666666"));
        areaNumeroLinhas.setFocusable(false);

        Font f = new Font("Courier New", Font.ITALIC, 14);
        areaNumeroLinhas.setFont(f);

        constraints.gridx = 1; // A coluna onde a Area de texto sera colocada na grade
        constraints.gridy = 0; // A linha onde a Area de texto sera colocada na grade

        constraints.gridwidth = 1; // o numero de colunas q a Area ocupa na grade
        constraints.gridheight = 1; // o numero de linhas q a Area ocupa na grade

        //  faz area de texto ocupa toda a area do componente
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;

        layout.setConstraints(areaDeTexto, constraints);
        containerTexto.add(areaDeTexto);

        areaNumeroLinhas.setEditable(false);

        // configura o layout area de numero de linhas do texto
        constraints.gridx = 0; // A colulna onde a Area de numeros sera colocada na grade
        constraints.gridy = 0; // A linha onde a Area de numeros sera colocada na grade

        constraints.gridwidth = 1; // o numero de colunas q a Area de numeros ocupa na grade
        constraints.gridheight = 1; // o numero de linhas q a Area de numeros ocupa na grade

        // faz area de numeros aumentar apenas na vertical
        constraints.weightx = 0;
        constraints.weighty = 1;

        layout.setConstraints(areaNumeroLinhas, constraints);
        containerTexto.add(areaNumeroLinhas);
        constraints.weightx = 1;
        layout.setConstraints(containerTexto, constraints);

        // cria barras de rolagem para a area de texto.
        scrollPane = new JScrollPane(containerTexto, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        layout.setConstraints(scrollPane, constraints);
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.setAutoscrolls(false);
        container.add(scrollPane);
        setTabs(areaDeTexto, 2);

        StyledDocument styledDoc = areaDeTexto.getStyledDocument();
        if (styledDoc instanceof AbstractDocument) {
            doc = (AbstractDocument) styledDoc;
        }

        doc.addUndoableEditListener(new MyUndoableEditListener());

        UI.this.getContentPane().add(container, BorderLayout.CENTER);

        compilacao = new Compilacao();
        Compilacao.refInterface = UI.this;

    }

    private class EditConfINIActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            EditarConfINI editConf = new EditarConfINI(UI.this, true);
            editConf.setLocationRelativeTo(UI.this);
            editConf.show();
        }
    }

//	--------------------------------------------------------------------
    /** Retorna o numero total de linhas do TextPane */
    public static int getLineCount() {
        int numeroLinhas = 0;
        for (int i = 0; i < areaDeTexto.getText().length(); i++) {
            if (areaDeTexto.getText().charAt(i) == '\n') {
                numeroLinhas++;
            }
        }
        return numeroLinhas + 1;
    }
//	--------------------------------------------------------------------

    protected class MyUndoableEditListener implements UndoableEditListener {

        public void undoableEditHappened(UndoableEditEvent e) {
            //Remember the edit and update the menus.
            if (e instanceof UndoableEditEvent) {
                undo.addEdit(e.getEdit());
                undoAction.updateUndoState();
                redoAction.updateRedoState();
            }
        }
    }

//	--------------------------------------------------------------------
    public class UndoAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public UndoAction() {
            super("Desfazer                     Ctrl-Z");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undo.undo();
            } catch (CannotUndoException ex) {
            }
            updateUndoState();
            redoAction.updateRedoState();
        }

        public void updateUndoState() {
            if (undo.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, "Desfazer                     Ctrl-Z");
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Desfazer                     Ctrl-Z");
            }
        }
    }
//	--------------------------------------------------------------------

    public class RedoAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public RedoAction() {
            super("Refazer                       Ctrl-Y");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undo.redo();
            } catch (CannotRedoException ex) {
            }
            updateRedoState();
            undoAction.updateUndoState();
        }

        public void updateRedoState() {
            if (undo.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, "Refazer                       Ctrl-Y");
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Refazer                       Ctrl-Y");
            }
        }
    }
//	--------------------------------------------------------------------

    /** Keylistener - Tratamento das Acoes do teclado */
    public void keyTyped(KeyEvent e) {
        if (!(btn_salvar.isEnabled())) {
            btn_salvar.setEnabled(true);
            salvarThis.setEnabled(true);
        }
    }

    /** Keylistener - Tratamento das Acoes do teclado */
    public void keyPressed(KeyEvent e) {
        Display(e);
    }

    /** Keylistener - Tratamento das Acoes do teclado */
    public void keyReleased(KeyEvent e) {
//		int id = e.getID();		
        int keyCode = e.getKeyCode();

        if (keyCode == 17) {
            CTRLPressionado = false;
        }
        if (keyCode == 16) {
            SHIFTPressionado = false;
        }
    }

    /** Keylistener - Tratamento das Acoes do teclado */
    public void actionPerformed(ActionEvent e) {
    }
//	--------------------------------------------------------------------            

    /** Tratamento dos eventos especificos do teclado */
    protected void Display(KeyEvent e) {
        String Transfer = areaDeTexto.getText();
        int id = e.getID();
        int keyCode = e.getKeyCode();

        if (id != KeyEvent.KEY_RELEASED) {
            /** imprime o codigo do caractere que foi digitado*/
            if (keyCode == 10) {//Tratamento do ENTER
                int caret = areaDeTexto.getCaretPosition();
                Enter();
                if ((areaDeTexto.getCaretPosition() < areaDeTexto.getText().length() - 1) && (areaDeTexto.getCaretPosition() > 1)) {
                    AttributeSet normal = BLACK;
                    areaDeTexto.setCharacterAttributes(normal, false);
                    areaDeTexto.setText(Transfer);
                    areaDeTexto.setCaretPosition(caret);
                    if (btn_high.isSelected()) {
                        context.setHighlighter(new StrategyConcretePortugol());
                        context.FazerHighlight(areaDeTexto);
                    }
                }
                areaDeTexto.setCaretPosition(caret);
                CTRLPressionado = false;
                SHIFTPressionado = false;
            }
            /*
            else if (keyCode == 117) {//Tratamento do F6	como atalho para gerar codigo c++
                compilacao.geraCpp();
            } 
             */
             else if (keyCode == 120) {//Tratamento do F9	como atalho para compilar...
                new Cmd();
            } 
            /*
            else if (keyCode == 8) {//Tratamento do BACKSPACE
                limpaSelecao();
                BackSpace();
                CTRLPressionado = false;
                SHIFTPressionado = false;

                // chamando funcao que efetua highlight
                int caret = areaDeTexto.getCaretPosition();
                if ((areaDeTexto.getCaretPosition() < areaDeTexto.getText().length() - 1) && (areaDeTexto.getCaretPosition() > 1)) {
                    AttributeSet normal = BLACK;
                    areaDeTexto.setCharacterAttributes(normal, false);
                    areaDeTexto.setText(Transfer);
                    areaDeTexto.setCaretPosition(caret);
                    if (btn_high.isSelected()) {
                        context.setHighlighter(new StrategyConcretePortugol());
                        context.FazerHighlight(areaDeTexto);
                    }
                }
                areaDeTexto.setCaretPosition(caret);
            }             
            else if (keyCode == 127) {//Tratamento do DELETE
                
                limpaSelecao();
                Delete();
                CTRLPressionado = false;
                SHIFTPressionado = false;

                // chamando funcao que efetua highlight/
                int caret = areaDeTexto.getCaretPosition();
                if ((areaDeTexto.getCaretPosition() < areaDeTexto.getText().length() - 1) && (areaDeTexto.getCaretPosition() > 1)) {
                    AttributeSet normal = BLACK;
                    areaDeTexto.setCharacterAttributes(normal, false);
                    areaDeTexto.setText(Transfer);
                    areaDeTexto.setCaretPosition(caret);
                    if (btn_high.isSelected()) {
                        context.setHighlighter(new StrategyConcretePortugol());
                        context.FazerHighlight(areaDeTexto);
                    }
                }
                areaDeTexto.setCaretPosition(caret);
            }
             */             
             else if ((keyCode == 65) || (keyCode == 79)) {//Tratamento do ABRIR
                if (CTRLPressionado == true) {
                    AtalhoAbrir();
                }
                CTRLPressionado = false;
                SHIFTPressionado = false;
            } else if (keyCode == 78) {//Tratamento do NOVO
                if (CTRLPressionado == true) {
                    AtalhoNovo();
                }
                CTRLPressionado = false;
                SHIFTPressionado = false;
            } else if (keyCode == 84) {//Tratamento do Selecionar Tudo
                if (CTRLPressionado == true) {
                    areaDeTexto.selectAll();
                }
                CTRLPressionado = false;
                SHIFTPressionado = false;
            } else if (keyCode == 83) {//Tratamento do SALVAR
                if ((CTRLPressionado == true) && (SHIFTPressionado == true)) {
                    try {
                        Salvar();
                    } catch (IOException e1) {
                    }
                    CTRLPressionado = false;
                    SHIFTPressionado = false;
                } else if ((CTRLPressionado == true) && (SHIFTPressionado == false)) {//Tratamento do SALVAR COMO
                    AtalhoSalvarThis();
                    CTRLPressionado = false;
                    SHIFTPressionado = false;
                }
            } else if (keyCode == 32) {//Tratamento do ESPACO (tratar o highlight)
                limpaSelecao();
                int caret = areaDeTexto.getCaretPosition();
                if ((areaDeTexto.getCaretPosition() < areaDeTexto.getText().length() - 1) && (areaDeTexto.getCaretPosition() > 1)) {
                    AttributeSet normal = BLACK;
                    areaDeTexto.setCharacterAttributes(normal, false);
                    areaDeTexto.setText(Transfer);
                    areaDeTexto.setCaretPosition(caret);
                    if (btn_high.isSelected()) {
                        context.setHighlighter(new StrategyConcretePortugol());
                        context.FazerHighlight(areaDeTexto);
                    }
                }
                areaDeTexto.setCaretPosition(caret);
            } else if (keyCode == 114) {//Tratamento do F3 como atalho para Ir para Linha
                Util.gotoLine(areaDeTexto);
                CTRLPressionado = false;
                SHIFTPressionado = false;
            } else if (keyCode == 90) {//Tratamento do CTRL - Z
                if (CTRLPressionado == true) {
                    try {
                        undo.undo();
                    } catch (CannotUndoException ex) {
                    }
                }
                redoAction.updateRedoState();
            } else if (keyCode == 89) {//Tratamento do CTRL - Y
                if (CTRLPressionado == true) {
                    try {
                        undo.redo();
                    } catch (CannotRedoException ex) {
                    }
                    undoAction.updateUndoState();
                }
            } else if (keyCode == 70) {//Tratamento do CTRL - F
                if (CTRLPressionado == true) {
                    Util.busca(areaDeTexto);
                    CTRLPressionado = false;
                    SHIFTPressionado = false;
                }
            } else if (keyCode == 17) {//Tratamento do CTRL
                CTRLPressionado = true;
            } else if (keyCode == 16) {//Tratamento do SHIFT
                SHIFTPressionado = true;
            } else if (keyCode == 91
                    || keyCode == 93
                    || keyCode == 126
                    || keyCode == 59) {//chama highlight

                /** chamando funcao que efetua highlight*/
                int caret = areaDeTexto.getCaretPosition();
                if ((areaDeTexto.getCaretPosition() < areaDeTexto.getText().length() - 1) && (areaDeTexto.getCaretPosition() > 1)) {
                    AttributeSet normal = BLACK;
                    areaDeTexto.setCharacterAttributes(normal, false);
                    areaDeTexto.setText(Transfer);
                    areaDeTexto.setCaretPosition(caret);
                    if (btn_high.isSelected()) {
                        context.setHighlighter(new StrategyConcretePortugol());
                        context.FazerHighlight(areaDeTexto);
                    }
                }
                areaDeTexto.setCaretPosition(caret);

            }
        }
    }

    /** verifica se tem texto que vai ser deletado*/
    private void limpaSelecao() {
        try {
            if (areaDeTexto.getSelectedText() != "") {
                areaDeTexto.replaceSelection("");
            }
        } catch (Exception e) {
        }
    }
//	--------------------------------------------------------------------	

    /** Tratamento do ENTER */
    private void Enter() {
        // Listener da tecla enter (numeracao)
        if (btn_num.isSelected()) {
            strNumber = " ";
            numberLine = 0;
            numberLine = getLineCount() + 1;
            for (int i = 1; i <= numberLine; i++) {
                strNumber += String.valueOf(i) + " \n ";
            }
            areaNumeroLinhas.setText(strNumber);
        } else {
            areaNumeroLinhas.setText("");
        }
    }
//	--------------------------------------------------------------------

    /** Tratamento do BACKSPACE */
    private void BackSpace() {
        // Listener da tecla backspace (numeracao)
        if (btn_num.isSelected()) {
            strNumber = " 1 \n ";
            numberLine = 0;
            numberLine = getLineCount();
            for (int i = 2; i <= numberLine; i++) {
                strNumber += String.valueOf(i) + " \n ";
            }
            areaNumeroLinhas.setText(strNumber);
        } else {
            areaNumeroLinhas.setText("");
        }
    }

//	--------------------------------------------------------------------        
    /** Tratamento do DELETE */
    public void Delete() {
        // Listener da tecla delete (numeracao)
        if (btn_num.isSelected()) {
            strNumber = " ";
            numberLine = 0;
            numberLine = getLineCount() + 1;
            for (int i = 1; i <= numberLine - 1; i++) {
                strNumber += String.valueOf(i) + " \n ";
            }
            areaNumeroLinhas.setText(strNumber);
        } else {
            areaNumeroLinhas.setText("");
        }
    }
//	--------------------------------------------------------------------

    /** Numeracao das Linhas editadas */
    public void NumeracaoLinha() {
        // processa a numeracao das linhas de texto
        if (btn_num.isSelected()) {
            strNumber = " ";
            numberLine = 0;
            numberLine = getLineCount();

            for (int i = 1; i <= numberLine; i++) {
                strNumber += String.valueOf(i) + " \n ";
            }

            areaNumeroLinhas.setText(strNumber);
        } else {
            areaNumeroLinhas.setText("");
        }
    }
//	--------------------------------------------------------------------

    /** Filtro para abrir arquivos .txt */
    class Filtro extends javax.swing.filechooser.FileFilter {

        public boolean accept(File file) {
            String filename = file.getName();
            return filename.endsWith(".txt");
        }

        public String getDescription() {
            return "*.txt";
        }
    }

//	--------------------------------------------------------------------
    /** Metodo para abrir arquivos e armazenar em buffer para o tratamento */
    public void Carrega() throws IOException {
        //escolha do arquivo txt
        JFileChooser abrirArquivo = new JFileChooser("C:\\UFMA-CP\\fontes");
        abrirArquivo.addChoosableFileFilter(new Filtro());
        abrirArquivo.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int escolha;
        escolha = abrirArquivo.showOpenDialog(this);
        File arquivo;
        if (escolha == JFileChooser.CANCEL_OPTION) {
            TokenTudo = areaDeTexto.getText();
            return;
        }
        arquivo = abrirArquivo.getSelectedFile();
        //	----------- bufferizacao --------------
        if (!arquivo.isFile()) {
            JOptionPane.showMessageDialog(this,
                    "O arquivo especificado nao pode ser aberto!",
                    MACP.version, JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            if (arquivo.canRead()) {
                arquivoAux = arquivo;

                caminho = arquivo.getAbsolutePath(); //pegando caminho do arquivo
                pasta = arquivo.getCanonicalPath(); //pegando caminho do arquivo
                pasta = pasta.replace(".txt", ".exe");
                BufferedReader entrada = new BufferedReader(new FileReader(arquivo));
                String conteudo = "";
                String tokens = "";

                while ((conteudo = entrada.readLine()) != null) {
                    tokens = tokens.concat(conteudo);
                    tokens = tokens.concat("\n");
                }
                TokenTudo = tokens;
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao Abrir Arquivo",
                        MACP.version, JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//	--------------------------------------------------------------------

    /** Metodo para a formatacao correta do texto a ser salvo  */
    public void FormatacaoTexto() {
        texto_areaDeTexto = areaDeTexto.getText();
        if (!(texto_areaDeTexto.contains("\r\n"))) {
            texto_areaDeTexto = texto_areaDeTexto.replaceAll("\n", "\r\n");
        }
    }

//	--------------------------------------------------------------------
    /** Metodo para alterar o arquivo con.ini */
    public void alterarConfig() {
        //ABRINDO O CONF.INI------------------------------------------
        BufferedReader entrada = null;
        String conteudo = "";
        String configAtual = "";
        int flagArquivo = 0;
        //conf.ini padrao
        final String command;

        if (radioWindows.isSelected()) {
            command = "cd\\UFMA-CP\\gcc\\bin \r\n"
                    + "g++ -I../include -I../include/c++ -I../include/c++/mingw32 -I../include/c++/backward -L../lib ";
        } else {
            command = "g++ ";
        }

        config = "O arquivo conf.ini nao pode ser carregado!\n"
                + "Altere estas linhas e clique em salvar para criar\n"
                + "um novo arquivo!";
        try {
            entrada = new BufferedReader(new FileReader("./conf.ini"));
            flagArquivo = 1;
        } catch (FileNotFoundException e1) {
            flagArquivo = 0;

            /*
            JOptionPane.showMessageDialog(UI.this,
                    "O arquivo conf.ini foi criado!\n"
                    + "Tente editar novamente!\n",
                    MACP.version, JOptionPane.INFORMATION_MESSAGE);
             */

            //criacao do arquivo conf.ini-----------------------------------
            FileOutputStream out = null;
            try {
                out = new FileOutputStream("./conf.ini");
            } catch (FileNotFoundException e) {
            }
            PrintStream p = new PrintStream(out);
            try {
                p.write(command.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            p.close();

        }
        if (flagArquivo == 1) {
            try {
                while ((conteudo = entrada.readLine()) != null) {
                    configAtual = configAtual.concat(conteudo);
                    configAtual = configAtual.concat("\r\n");
                }
            } catch (IOException e1) {
            }

            config = configAtual;
            //TRATAMENTO DAS ALTERACOES DE CONF.INI-----------------------
            frameConfig = new JFrame("Configuracao de Compilacao");
            Container Lexcontainer = new Container();
            frameConfig.setIconImage((new ImageIcon(getClass().getResource(icon))).getImage());
            final JTextArea resultado = new JTextArea(12, 30);
            final JLabel label = new JLabel("");
            JButton op1, op2;
            //	FlowLayout layoutResult = new FlowLayout();
            JScrollPane LexscrollPane = new JScrollPane();
            Lexcontainer = frameConfig.getContentPane();
            Lexcontainer.setLayout(new FlowLayout());
            resultado.setTabSize(2);

            label.setForeground(Color.red);
            resultado.setForeground(Color.black);
            resultado.setText(config);

            resultado.setFont(Font.decode("Verdana"));
            resultado.setSelectionColor(Color.decode("#10377C"));
            resultado.setSelectedTextColor(Color.WHITE);

            op1 = new JButton("Salvar alteracoes");
            op2 = new JButton("Carregar padrao");
            Lexcontainer.add(resultado);
            LexscrollPane = new JScrollPane(resultado, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            op1.addActionListener(
                    new ActionListener() {

                        public void actionPerformed(ActionEvent actionEvent) {
                            //SALVANDO O CONF.INI-----------------------------------------
                            String texto = resultado.getText();
                            if (!(texto.contains("\r\n"))) {
                                texto = texto.replaceAll("\n", "\r\n");
                            }
                            resultado.setText(texto);

                            FileOutputStream out = null;
                            try {
                                out = new FileOutputStream("./conf.ini");
                            } catch (FileNotFoundException e) {
                                JOptionPane.showMessageDialog(UI.this,
                                        "O arquivo nao foi encontrado!\n"
                                        + "Se o erro persistir instale o CP novamente,\n"
                                        + "pois o arquivo conf.ini pode estar corrompido",
                                        MACP.version, JOptionPane.ERROR_MESSAGE);
                            }
                            PrintStream p = new PrintStream(out);
                            try {
                                p.write(resultado.getText().getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            p.close();
                            label.setText("As configuracoes de compilacao foram atualizadas!");

                        }
                    });
            op2.addActionListener(
                    new ActionListener() {

                        public void actionPerformed(ActionEvent actionEvent) {
                            texto = command;
                            resultado.setText(texto);
                        }
                    });

            Lexcontainer.add(op1);
            Lexcontainer.add(op2);
            frameConfig.setSize(350, 300);
            Lexcontainer.setVisible(true);
            Lexcontainer.add(LexscrollPane);
            Lexcontainer.add(label);
            resultado.setEditable(true);
            resultado.setFocusable(true);
            frameConfig.setResizable(false);
            frameConfig.setLocationRelativeTo(this);
            frameConfig.setVisible(true);
        }
    }

//	--------------------------------------------------------------------
    /** Metodo para salvar arquivos */
    public void Salvar() throws IOException {
        FormatacaoTexto();
        JFileChooser abrirArquivo = new JFileChooser("C:\\MACP\\fontes");
        abrirArquivo.addChoosableFileFilter(new Filtro());
        int escolha = abrirArquivo.showSaveDialog(this);
        File arquivo;

        if (escolha == JFileChooser.CANCEL_OPTION) {
            return;
        } else if (escolha == JFileChooser.APPROVE_OPTION) {
            arquivo = abrirArquivo.getSelectedFile();
            if (arquivo.exists()) {
                int response = JOptionPane.showConfirmDialog(UI.this,
                        "Esta pasta ja contem um arquivo chamado " + arquivo.getName()
                        + "\nDeseja substituir o arquivo existente? ", "Confirmar substituicao de arquivo",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.CANCEL_OPTION) {
                    return;
                } else if (response == JOptionPane.OK_OPTION) {
                    arquivoAux = arquivo;
                    caminho = arquivo.getAbsolutePath(); //pegando caminho do arquivo
                    pasta = arquivo.getCanonicalPath();
                    pasta = pasta.replace(".txt", ".exe");
                    if (!arquivo.getName().endsWith(".txt")) {
                        FileOutputStream out = new FileOutputStream(arquivo + ".txt");
                        PrintStream p = new PrintStream(out);
                        p.write(texto_areaDeTexto.getBytes());
                        p.close();
                    } else if (arquivo.getName().endsWith(".txt")) {
                        FileOutputStream out = new FileOutputStream(arquivo);
                        PrintStream p = new PrintStream(out);
                        p.write(texto_areaDeTexto.getBytes());
                        p.close();
                    }
                }
            } else {
                arquivoAux = arquivo;
                caminho = arquivo.getAbsolutePath(); //pegando caminho do arquivo
                pasta = arquivo.getCanonicalPath();
                pasta = pasta.replace(".txt", ".exe");
                if (!arquivo.getName().endsWith(".txt")) {
                    FileOutputStream out = new FileOutputStream(arquivo + ".txt");
                    PrintStream p = new PrintStream(out);
                    p.write(texto_areaDeTexto.getBytes());
                    p.close();
                } else if (arquivo.getName().endsWith(".txt")) {
                    FileOutputStream out = new FileOutputStream(arquivo);
                    PrintStream p = new PrintStream(out);
                    p.write(texto_areaDeTexto.getBytes());
                    p.close();
                }
            }
        }
    }

//	--------------------------------------------------------------------
    /** Metodo para salvar o arquivo ja aberto */
    public void AtalhoSalvarThis() {
        if (caminho == null) {
            try {
                Salvar();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            FormatacaoTexto();

            if (!arquivoAux.getName().endsWith(".txt")) {
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(arquivoAux + ".txt");
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(UI.this,
                            "O arquivo nao foi encontrado!",
                            MACP.version, JOptionPane.ERROR_MESSAGE);
                }
                PrintStream p = new PrintStream(out);
                try {
                    p.write(texto_areaDeTexto.getBytes());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                p.close();
            } else if (arquivoAux.getName().endsWith(".txt")) {
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(arquivoAux);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                PrintStream p = new PrintStream(out);
                try {
                    p.write(texto_areaDeTexto.getBytes());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                p.close();
            }
        }
    }
//	--------------------------------------------------------------------

    public void AtalhoAbrir() {
        try {
            areaNumeroLinhas.setEditable(true);
            Carrega();
            tokens = TokenTudo;

            if (!(areaDeTexto.getText() == "")) {
                areaDeTexto.setText(tokens);
                //FormatacaoTextoCarrega();
                NumeracaoLinha();
                context.setHighlighter(new StrategyConcretePortugol());
                context.FazerHighlight(areaDeTexto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//	--------------------------------------------------------------------
    public void AtalhoNovo() {
        if (!(areaDeTexto.getText().equals("variaveis\n\ninicio\n\nfim."))) {
            int response = JOptionPane.showConfirmDialog(UI.this,
                    "A area de desenvolvimento nao esta vazia!"
                    + "\nDeseja descartar seu desenvolvimento? ", "Atencao",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (response == JOptionPane.NO_OPTION) {
                return;
            } else if (response == JOptionPane.YES_OPTION) {
                btn_salvar.setEnabled(false);
                salvarThis.setEnabled(false);

                caminho = null; //zerando pois nao ha mais nenhum arquivo em buffer
                pasta = null;

                tokens = "";
                areaDeTexto.setText("variaveis\n\ninicio\n\nfim.");
                NumeracaoLinha();
                areaNumeroLinhas.setText(strNumber);
            }
            if (btn_high.isSelected()) {
                context.setHighlighter(new StrategyConcretePortugol());
                context.FazerHighlight(areaDeTexto);
            }
        } else {
            return;
        }
    }

    /********************************************************************************/
    /** Classe para o tratamento das acoes dos itens de menu */
    private class ItemMenuHandler implements ActionListener {

        // trata os eventos com o metodo actionPerformad
        public void actionPerformed(ActionEvent event) {
            // usuario escolhe um dos itens de menus
//			----------------------------------------------
            if (event.getSource() == novo) {
                AtalhoNovo();
                int PosAux = areaDeTexto.getCaretPosition();
                if (btn_high.isSelected()) {
                    context.setHighlighter(new StrategyConcretePortugol());
                    context.FazerHighlight(areaDeTexto);
                }
                areaDeTexto.setCaretPosition(PosAux);
            } //			----------------------------------------------
            else if (event.getSource() == abrir) {
                AtalhoAbrir();
            } //			----------------------------------------------
            else if (event.getSource() == salvarThis) {
                AtalhoSalvarThis();
            } //			----------------------------------------------
            else if (event.getSource() == salvar) {
                try {
                    Salvar();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } //			----------------------------------------------
            else if (event.getSource() == sair) {
                System.exit(0);
            } //			----------------------------------------------
            else if (event.getSource() == lexica) {
                if (areaDeTexto.getText().length() == 0) {
                    JOptionPane.showMessageDialog(UI.this,
                            "A area de desenvolvimento esta vazia!",
                            MACP.version, JOptionPane.WARNING_MESSAGE);
                } else {
                    compilacao.analiseLexica();
                }
            } //			----------------------------------------------
            else if (event.getSource() == sintatica) {

                if (areaDeTexto.getText().length() == 0) {
                    JOptionPane.showMessageDialog(UI.this,
                            "A area de desenvolvimento esta vazia!",
                            MACP.version, JOptionPane.WARNING_MESSAGE);
                } else {
                    compilacao.analiseSintatica();
                }
            } //			----------------------------------------------
            else if (event.getSource() == geracaojava) {
                compilacao.geraJava();
            }
            //			----------------------------------------------
            else if (event.getSource() == geracaocpp) {
                compilacao.geraCpp();
            } //			----------------------------------------------
            else if (event.getSource() == geracaoc) {
                compilacao.geraC();

            } //			----------------------------------------------
            else if (event.getSource() == geracaopascal) {
                compilacao.geraPascal();

            } //			----------------------------------------------
            else if (event.getSource() == compilar) {
                //compilacao.RodarAutomatico();
                new Cmd();
            }//fim else maior
            //			----------------------------------------------
            else if (event.getSource() == wwwCsalles) {
                String url = "http://www.deinf.ufma.br/~csalles";
                try {
                    if (radioWindows.isSelected()) {
                        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                    } else {
                        Runtime.getRuntime().exec("firefox " + url);
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(UI.this,
                            "Para entrar em contato com o professor Carlos Salles\n"
                            + "acesse: www.deinf.ufma.br/~csalles"
                            + "ou envie email para: csalles@deinf.ufma.br\n",
                            MACP.version, JOptionPane.INFORMATION_MESSAGE);
                }
            } //			----------------------------------------------
            else if (event.getSource() == wwwLuciano) {
                String url = "http://www.deinf.ufma.br/~lrc";
                try {
                    if (radioWindows.isSelected()) {
                        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                    } else {
                        Runtime.getRuntime().exec("firefox " + url);
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(UI.this,
                            "Para entrar em contato com o professor Luciano\n"
                            + "acesse: www.deinf.ufma.br/~lrc"
                            + "ou envie email para: lrc@deinf.ufma.br\n",
                            MACP.version, JOptionPane.INFORMATION_MESSAGE);
                }
            } //			----------------------------------------------
            else if (event.getSource() == wwwVidal) {
                String url = "http://www.deinf.ufma.br/~vidal";
                try {
                    if (radioWindows.isSelected()) {
                        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                    } else {
                        Runtime.getRuntime().exec("firefox " + url);
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(UI.this,
                            "Para entrar em contato com o professor Vidal\n"
                            + "acesse: www.deinf.ufma.br/~vidal"
                            + "ou envie email para: vidal@deinf.ufma.br\n",
                            MACP.version, JOptionPane.INFORMATION_MESSAGE);
                }
            } //			----------------------------------------------
            else if (event.getSource() == download) {
                String url = "http://portugol.sourceforge.net";
                try {
                    if (radioWindows.isSelected()) {
                        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                    } else {
                        Runtime.getRuntime().exec("firefox " + url);
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(UI.this,
                            "Para encontrar maiores informacoes sobre o programa, basta acessar:\n"
                            + "acesse: portugol.sourceforge.net",
                            MACP.version, JOptionPane.ERROR_MESSAGE);
                }
            }
            else if (event.getSource() == manual) {

                    JOptionPane.showMessageDialog(UI.this,
                            "Em construção",
                            MACP.version, JOptionPane.INFORMATION_MESSAGE);

            }//			----------------------------------------------
            else if (event.getSource() == sobre) {
                try {
                    new MostraImagem(0);
                } catch (InterruptedException e) {
                    JOptionPane.showMessageDialog(UI.this,
                            "Ocorreu um erro! Thread InterruptedException",
                            MACP.version, JOptionPane.ERROR_MESSAGE);
                }
            } //			----------------------------------------------
            else if (event.getSource() == selecionarTudo) {
                areaDeTexto.selectAll();
            } //			----------------------------------------------
            else if (event.getSource() == highlight) {
                if (btn_high.isSelected()) {
                    btn_high.setSelected(false);
                    highlight.setText("Fazer highlight");

                    StyleContext sc = StyleContext.getDefaultStyleContext();
                    AttributeSet normal = sc.addAttribute(SimpleAttributeSet.EMPTY,
                            StyleConstants.Foreground, Color.BLACK);

                    int PosAux = areaDeTexto.getCaretPosition();
                    String Transfer = areaDeTexto.getText();
                    areaDeTexto.setCharacterAttributes(normal, false);
                    areaDeTexto.setText(Transfer);
                    areaDeTexto.setCaretPosition(PosAux);
                } else {
                    btn_high.setSelected(true);
                    highlight.setText("Retirar highlight");
                    context.setHighlighter(new StrategyConcretePortugol());
                    context.FazerHighlight(areaDeTexto);
                }
            } //			----------------------------------------------
            else if (event.getSource() == procurar) {
                Util.busca(areaDeTexto);
            } //			----------------------------------------------
            else if (event.getSource() == irParaLinha) {
                Util.gotoLine(areaDeTexto);
            } //			----------------------------------------------
            else if (event.getSource() == copiar) {
            } //			----------------------------------------------
            else if (event.getSource() == colar) {
            } //			----------------------------------------------
            else if (event.getSource() == recortar) {
            } //			----------------------------------------------
            else if (event.getSource() == conf) {
                if ((frameConfig == null) || (!frameConfig.isVisible())) {
                    alterarConfig();
                }
                if (frameConfig.isShowing()) {
                    frameConfig.requestFocus();
                }
            } //			----------------------------------------------
            else if (event.getSource() == conf2) {
                JOptionPane.showMessageDialog(UI.this,
                        "O arquivo conf.ini foi criado para auxiliar o processo de compilacao.\n"
                        + "Este arquivo contem o caminho do compilador gcc para a geracao do executavel.\n\n"                        
                        + "1. EM AMBIENTE WINDOWS: \n"
                        + "Usando o MinGW, o arquivo vai com estas linhas de codigo: \n"
                        + "\"cd\\MACP\\gcc\\bin\\\" (Pasta /bin dentro do MinGW)\n"
                        + "\"g++ -I../include -I../include/c++ -I../include/c++/mingw32 \n"
                        + "-I../include/c++/backward -L../lib\" (comando de compilacao no Windows)\n\n"
                        + "2. EM AMBIENTE LINUX:\n"
                        + "\"g++\" (chama o compilador Gcc que deve estar devidamente instalado...)\n"
                        + "OBS.: No caso do Linux devera ser instalado o \"x-term\" para execucao\n"
                        + " automatica dos programas gerados", MACP.version, JOptionPane.INFORMATION_MESSAGE);
            } //			----------------------------------------------
            else if (event.getSource() == numera) {
                if (!(btn_num.isSelected())) {
                    btn_num.setSelected(true);
                    NumeracaoLinha();
                    numera.setText("Retirar numeracao");
                } else {
                    btn_num.setSelected(false);
                    areaNumeroLinhas.setText("");
                    numera.setText("Inserir numeracao");
                }
            }
        }
    }

    /********************************************************************************/
//	--------------------------------------------------------------------
    /** Interface do programa*/
    public UI() {
        // texto da Barra de Titulo
        super(MACP.version);
        // altera o icone da barra de titulos
        setIconImage((new ImageIcon(getClass().getResource(icon))).getImage());

        /**INTERFACE------------------------------------------------*/
        // cria barra de menus
        barra = new JMenuBar();
        // configura o menu 'Arquivo'
        arquivo = new JMenu("Arquivo");
        arquivo.setMnemonic('A');
        // configura o menu 'Ferramentas'
        tools = new JMenu("Editar");
        tools.setMnemonic('E');
        // configura o menu 'Executar'
        executar = new JMenu("Executar");
        executar.setMnemonic('x');
        // configura o menu 'Opcoes'
        opcoes = new JMenu("Opcoes");
        opcoes.setMnemonic('O');
        // configura o menu 'Ajuda'
        ajuda = new JMenu("Ajuda");
        ajuda.setMnemonic('j');


        // adiciona o menu Arquivo na barra de menus
        barra.add(arquivo);
        // Adiciona o menu 'Editar'
        barra.add(tools);
        // Adiciona o menu 'Executar' a barra de menus
        barra.add(executar);
        // Adiciona o menu 'opcoes' a barra de menus
        barra.add(opcoes);
        // Adiciona o menu 'compilar' a barra de menus
        barra.add(ajuda);


        /**BARRA DE BOTOES*/
//		 configura o botao NOVO
        final Icon ico_novo = new ImageIcon(getClass().getResource("img/novo.png"));
        final Icon ico_novo_on = new ImageIcon(getClass().getResource("img/novo_on.png"));
        JButton btn_novo = new JButton();
        btn_novo.setBorder(null);
        btn_novo.setPressedIcon(ico_novo);
        btn_novo.setRolloverIcon(ico_novo_on);
        btn_novo.setBackground(Color.decode("#EBE9ED"));
        btn_novo.setIcon(ico_novo);
//		 configura o botao ABRIR
        final Icon ico_abrir = new ImageIcon(getClass().getResource("img/abrir.png"));
        final Icon ico_abrir_on = new ImageIcon(getClass().getResource("img/abrir_on.png"));
        JButton btn_abrir = new JButton();
        btn_abrir.setBorder(null);
        btn_abrir.setPressedIcon(ico_abrir);
        btn_abrir.setRolloverIcon(ico_abrir_on);
        btn_abrir.setBackground(Color.decode("#EBE9ED"));
        btn_abrir.setIcon(ico_abrir);
//		 configura o botao SALVAR
        final Icon ico_salvar = new ImageIcon(getClass().getResource("img/salvar.png"));
        final Icon ico_salvar_on = new ImageIcon(getClass().getResource("img/salvar_on.png"));
        ico_salvar_off = new ImageIcon(getClass().getResource("img/salvar_off.png"));
        btn_salvar = new JButton();
        btn_salvar.setBorder(null);
        btn_salvar.setDisabledIcon(ico_salvar_off);
        btn_salvar.setEnabled(false);
        btn_salvar.setPressedIcon(ico_salvar);
        btn_salvar.setRolloverIcon(ico_salvar_on);
        btn_salvar.setBackground(Color.decode("#EBE9ED"));
        btn_salvar.setIcon(ico_salvar);
//		 configura o botao HIGHLIGHT
        final Icon ico_high = new ImageIcon(getClass().getResource("img/high.png"));
        final Icon ico_high_on = new ImageIcon(getClass().getResource("img/high_on.png"));
        btn_high = new JButton();
        btn_high.setBorder(null);
        btn_high.setPressedIcon(ico_high);
        btn_high.setRolloverIcon(ico_high_on);
        btn_high.setBackground(Color.decode("#EBE9ED"));
        btn_high.setIcon(ico_high);
//		 configura o botao NUM
        final Icon ico_num = new ImageIcon(getClass().getResource("img/num.png"));
        final Icon ico_num_on = new ImageIcon(getClass().getResource("img/num_on.png"));
        btn_num = new JButton();
        btn_num.setBorder(null);
        btn_num.setRolloverIcon(ico_num_on);
        btn_num.setBackground(Color.decode("#EBE9ED"));
        btn_num.setIcon(ico_num);
//		 configura o botao compilar
        final Icon ico_run = new ImageIcon(getClass().getResource("img/run.png"));
        final Icon ico_run_on = new ImageIcon(getClass().getResource("img/run_on.png"));
        JButton btn_run = new JButton();
        btn_run.setBorder(null);
        btn_run.setPressedIcon(ico_run);
        btn_run.setRolloverIcon(ico_run_on);
        btn_run.setBackground(Color.decode("#EBE9ED"));
        btn_run.setIcon(ico_run);
//		 configura o botao gerac
        final Icon ico_gerac = new ImageIcon(getClass().getResource("img/c.png"));
        final Icon ico_gerac_on = new ImageIcon(getClass().getResource("img/c_on.png"));
        JButton btn_gerac = new JButton();
        btn_gerac.setBorder(null);
        btn_gerac.setPressedIcon(ico_gerac);
        btn_gerac.setRolloverIcon(ico_gerac_on);
        btn_gerac.setBackground(Color.decode("#EBE9ED"));
        btn_gerac.setIcon(ico_gerac);
//		 configura o botao geracpp
        final Icon ico_geracpp = new ImageIcon(getClass().getResource("img/cpp.png"));
        final Icon ico_geracpp_on = new ImageIcon(getClass().getResource("img/cpp_on.png"));
        JButton btn_geracpp = new JButton();
        btn_geracpp.setBorder(null);
        btn_geracpp.setPressedIcon(ico_geracpp);
        btn_geracpp.setRolloverIcon(ico_geracpp_on);
        btn_geracpp.setBackground(Color.decode("#EBE9ED"));
        btn_geracpp.setIcon(ico_geracpp);

//		 configura o botao gerapascal
        final Icon ico_gerapascal = new ImageIcon(getClass().getResource("img/pascal.png"));
        final Icon ico_gerapascal_on = new ImageIcon(getClass().getResource("img/pascal_on.png"));
        JButton btn_gerapascal = new JButton();
        btn_gerapascal.setBorder(null);
        btn_gerapascal.setPressedIcon(ico_gerapascal);
        btn_gerapascal.setRolloverIcon(ico_gerapascal_on);
        btn_gerapascal.setBackground(Color.decode("#EBE9ED"));
        btn_gerapascal.setIcon(ico_gerapascal);

//		 configura o botao gerajava
        final Icon ico_gerajava = new ImageIcon(getClass().getResource("img/java.png"));
        final Icon ico_gerajava_on = new ImageIcon(getClass().getResource("img/java_on.png"));
        JButton btn_gerajava = new JButton();
        btn_gerajava.setBorder(null);
        btn_gerajava.setPressedIcon(ico_gerajava);
        btn_gerajava.setRolloverIcon(ico_gerajava_on);
        btn_gerajava.setBackground(Color.decode("#EBE9ED"));
        btn_gerajava.setIcon(ico_gerajava);

        /**---------------- Configuracao da JToolBar --------------*/
        barraBotoes = new JToolBar();

//		 Adiciona o botao novo
        barraBotoes.add(btn_novo);
        btn_novo.setToolTipText("Novo  CTRL-N");
        btn_novo.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent actionEvent) {
                        AtalhoNovo();
                    }
                });
//		 Adiciona o botao abrir
        barraBotoes.add(btn_abrir);
        btn_abrir.setToolTipText("Abrir  CTRL-A");
        btn_abrir.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent actionEvent) {
                        AtalhoAbrir();
                    }
                });
//		 Adiciona o botao salvar
        barraBotoes.add(btn_salvar);
        btn_salvar.setToolTipText("Salvar  CTRL-S");
        btn_salvar.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent actionEvent) {
                        AtalhoSalvarThis();
                        btn_salvar.setEnabled(false);
                    }
                });
        //barraBotoes.addSeparator();
//		 Adiciona o botao highlight
        barraBotoes.add(btn_high);
        btn_high.setToolTipText("Highlight");
        btn_high.setSelectedIcon(ico_high_on);
        btn_high.setSelected(true);
        btn_high.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent actionEvent) {
                        if (btn_high.isSelected()) {
                            btn_high.setSelected(false);
                            highlight.setText("Fazer highlight");

                            StyleContext sc = StyleContext.getDefaultStyleContext();
                            AttributeSet normal = sc.addAttribute(SimpleAttributeSet.EMPTY,
                                    StyleConstants.Foreground, Color.BLACK);

                            int PosAux = areaDeTexto.getCaretPosition();
                            String Transfer = areaDeTexto.getText();
                            areaDeTexto.setCharacterAttributes(normal, false);
                            areaDeTexto.setText(Transfer);
                            areaDeTexto.setCaretPosition(PosAux);
                        } else {
                            btn_high.setSelected(true);
                            highlight.setText("Retirar highlight");
                            if (btn_high.isSelected()) {
                                context.setHighlighter(new StrategyConcretePortugol());
                                context.FazerHighlight(areaDeTexto);
                            }
                        }
                    }
                });
//		 Adiciona o botao numeracao
        barraBotoes.add(btn_num);
        btn_num.setToolTipText("Numeracao");
        btn_num.setSelectedIcon(ico_num_on);
        btn_num.setSelected(true);
        btn_num.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent actionEvent) {
                        if (!(btn_num.isSelected())) {
                            btn_num.setSelected(true);
                            NumeracaoLinha();
                            numera.setText("Retirar numeracao");
                        } else {
                            btn_num.setSelected(false);
                            areaNumeroLinhas.setText("");
                            numera.setText("Inserir numeracao");
                        }
                    }
                });

        barraBotoes.addSeparator();
//		 Adiciona o botao gerarC
        barraBotoes.add(btn_gerac);
        btn_gerac.setToolTipText("Gerar C");
        btn_gerac.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        compilacao.geraC();
                    }
                });

        barraBotoes.addSeparator();
//		 Adiciona o botao gerarC++
        barraBotoes.add(btn_geracpp);
        btn_geracpp.setToolTipText("Gerar C++");
        btn_geracpp.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent actionEvent) {
                        compilacao.geraCpp();
                    }
                });

        barraBotoes.addSeparator();
//		 Adiciona o botao gerarPascal
        barraBotoes.add(btn_gerapascal);
        btn_gerapascal.setToolTipText("Gerar Pascal");
        btn_gerapascal.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        compilacao.geraPascal();
                    }
                });

        barraBotoes.addSeparator();
//		 Adiciona o botao gerarJava
        barraBotoes.add(btn_gerajava);
        btn_gerajava.setToolTipText("Gerar Java");
        btn_gerajava.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        compilacao.geraJava();
                    }
                });

        barraBotoes.addSeparator();
//		 Adiciona o botao compilar
        barraBotoes.add(btn_run);
        btn_run.setToolTipText("Compilar e Executar [ F9 ]");
        btn_run.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent actionEvent) {
                        //compilacao.RodarAutomatico();
                        new Cmd();
                    }
                });

        // Adiciona o checkbox highlight ao menu
        barra.add(Box.createHorizontalGlue());
        JLabel title = new JLabel("Plataforma:");
        radioWindows = new JRadioButton("Windows");
        radioWindows.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent actionEvent) {
                        radioWindows.setSelected(true);
                        radioLinux.setSelected(false);
                    }
                });
        radioLinux = new JRadioButton("Linux");
        radioLinux.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent actionEvent) {
                        radioWindows.setSelected(false);
                        radioLinux.setSelected(true);
                    }
                });

        barra.add(title);
        barra.add(radioWindows);
        barra.add(radioLinux);

        //Verifica se o S.O. é Windows ou Linux
        checaSistema();

        /** Acoes do menu  */
        // configura o item de nemu 'novo'
        novo = new JMenuItem("Novo...                Ctrl-N");
        novo.setMnemonic('N');
        // configura o item de menu 'Abrir'
        abrir = new JMenuItem("Abrir...                Ctrl-A");
        abrir.setMnemonic('b');
        // configura o item de menu 'Salvar'
        salvarThis = new JMenuItem("Salvar                  Ctrl-S");
        salvarThis.setMnemonic('S');
        //salvarThis.setEnabled(false);
        // configura o item de menu 'Salvar como'
        salvar = new JMenuItem("Salvar como...    Ctrl-Shift-S");
        salvar.setMnemonic('c');
        // configura o item de menu 'Sair'
        sair = new JMenuItem("Sair                      Alt+F4");
        sair.setMnemonic('r');

        // configura o item de menu 'Analise Lexica'
        lexica = new JMenuItem("Analise Lexica...");
        // configura o item de menu 'Analise Sintatica'
        sintatica = new JMenuItem("Analise Sintatica...");
        // configura o item de menu 'Geracao de Codigo Pascal'
        geracaopascal = new JMenuItem("Geracao de Codigo Pascal...");
        // configura o item de menu 'Geracao de Codigo C'
        geracaoc = new JMenuItem("Geracao de Codigo C...");
        // configura o item de menu 'Geracao de Codigo C++'
        geracaocpp = new JMenuItem("Geracao de Codigo C++...");        
        // configura o item de menu 'Geracao de Codigo Java'
        geracaojava = new JMenuItem("Geracao de Codigo Java... ");       
        // configura o item de menu 'Compilar'
        compilar = new JMenuItem("Compilar...                           F9");
        compilar.setMnemonic('C');

        // configura o item procurar
        procurar = new JMenuItem("Procurar...                  Ctrl-F");
        procurar.setMnemonic('P');

        //inicializa a areaDeTexto
        AreaTexto("variaveis\n\ninicio\n\nfim.");
        //seta as opcoes do DefaultKit
        Action actions[] = areaDeTexto.getActions();

        Action cutAction = FrameGeracao.findAction(actions, DefaultEditorKit.cutAction);
        Action copyAction = FrameGeracao.findAction(actions, DefaultEditorKit.copyAction);
        Action pasteAction = FrameGeracao.findAction(actions, DefaultEditorKit.pasteAction);
        // configura o item copiar
        copiar = new JMenuItem(copyAction);
        copiar.setText("Copiar                         Ctrl-C");
        copiar.setMnemonic('C');
        // configura o item colar
        colar = new JMenuItem(pasteAction);
        colar.setText("Colar                           Ctrl-V");
        colar.setMnemonic('o');
        // configura o item recortar
        recortar = new JMenuItem(cutAction);
        recortar.setText("Recortar                     Ctrl-X");
        recortar.setMnemonic('t');


        // configura o item desfazer
        desfazer = new JMenuItem("Desfazer");
        desfazer.setMnemonic('D');
        // configura o item refazer
        refazer = new JMenuItem("Refazer");
        refazer.setMnemonic('R');
        // configura o item selecionar tudo
        selecionarTudo = new JMenuItem("Selecionar Tudo          Ctrl-T");
        selecionarTudo.setMnemonic('S');
        // configura o item selecionar tudo
        irParaLinha = new JMenuItem("Ir para Linha...            F3");
        irParaLinha.setMnemonic('I');
        // configura o item editar conf.ini
        conf = new JMenuItem("Editar conf.ini...");
        conf.setMnemonic('E');
        // configura o item sobre conf.ini
        conf2 = new JMenuItem("O que o o conf.ini?");
        conf2.setMnemonic('O');
        // configura o item criar conf.ini
        numera = new JMenuItem("Retirar numeracao");
        numera.setMnemonic('C');
        // Configura o checkbox highlight do menu
        highlight = new JMenuItem("Retirar highlight");
        highlight.setMnemonic('h');


        // configura o item de menu do 'JRE'
        download = new JMenuItem("portugol.sourceforge.net");
        download.setMnemonic('p');
        // configura o item de menu 'Manual'
        manual = new JMenuItem("Manual");
        manual.setMnemonic('M');
        // configura o item de menu 'Sobre'
        sobre = new JMenuItem("Sobre o Compilador Portugol");
        sobre.setMnemonic('S');

        // configura o item de menu 'Documentacao'
        wwwCsalles = new JMenuItem("~csalles");
        wwwCsalles.setMnemonic('c');
        // configura o item de menu 'Download do Dev-Cpp'
        wwwLuciano = new JMenuItem("~luciano");
        wwwLuciano.setMnemonic('l');
        // configura o item de menu 'Vidal'
        wwwVidal = new JMenuItem("~vidal");
        wwwVidal.setMnemonic('v');

        // adiciona 'Novo' ao menu 'Arquivo'
        arquivo.add(novo);
        // adiciona 'Abrir' ao menu 'Arquivo'
        arquivo.add(abrir);
        // adiciona 'Salvar' ao menu 'Arquivo'
        arquivo.add(salvarThis);
        salvarThis.setEnabled(false);
        // adiciona 'Salvar' ao menu 'Arquivo'
        arquivo.add(salvar);
        arquivo.addSeparator();
        // adiciona 'Sair' ao menu 'Arquivo'
        arquivo.add(sair);

        // adiciona 'lexica' ao menu 'Executar'
        executar.add(lexica);
        // adiciona 'Sintatica' ao menu 'Executar'
        executar.add(sintatica);
        executar.addSeparator();
        // adiciona 'Gera Pascal' ao menu 'Executar'
        executar.add(geracaopascal);
        // adiciona 'Gera C' ao menu 'Executar'
        executar.add(geracaoc);
        // adiciona 'Gera C++' ao menu 'Executar'
        executar.add(geracaocpp);
        // adiciona 'Gera Java' ao menu 'Executar'
        executar.add(geracaojava);
        executar.addSeparator();
        executar.addSeparator();
        // adiciona 'Compilando' ao menu 'Executar'
        executar.add(compilar);

        // adiciona 'Copiar' ao menu 'Editar'
        tools.add(copiar);
        // adiciona 'Colar' ao menu 'Editar'
        tools.add(colar);
        // adiciona 'Recortar' ao menu 'Editar'
        tools.add(recortar);
        tools.addSeparator();
        // adiciona 'Desfazer' ao menu 'Editar'
        undoAction = new UndoAction();
        tools.add(undoAction);
        // adiciona 'Refazer' ao menu 'Editar'
        redoAction = new RedoAction();
        tools.add(redoAction);
        // adiciona 'Selecionar Tudo' ao menu 'Editar'
        tools.add(selecionarTudo);
        //tools.addSeparator();

        // adiciona 'Ir para a linha' ao menu 'Editar'
        tools.addSeparator();
        tools.add(irParaLinha);
        // adiciona 'Procurar' ao menu 'Editar'
        tools.add(procurar);

        // adiciona 'Editar conf.ini' ao menu 'Editar'
        opcoes.add(conf);
        // adiciona 'Sobre conf.ini' ao menu 'Editar'
        opcoes.add(conf2);
        //adiciona o highlight
        opcoes.addSeparator();
        opcoes.add(highlight);
        // adiciona 'Numeracao' ao menu 'Editar'
        opcoes.add(numera);

        // adiciona 'Download do JRE' ao menu 'Ajuda'
        ajuda.add(download);
        // adiciona '~csalles' ao menu 'Ajuda'
        //ajuda.add(wwwCsalles);
        // adiciona '~Luciano' ao menu 'Ajuda'
        //ajuda.add(wwwLuciano);
        // adiciona '~Vidal' ao menu 'Ajuda'
        //ajuda.add(wwwVidal);
        ajuda.addSeparator();
        // adiciona 'Manual' ao menu 'Ajuda'
        ajuda.add(manual);
        // adiciona 'Sobre' ao menu 'Ajuda'
        ajuda.add(sobre);

        // registra tratadores de eventos
        ItemMenuHandler handler = new ItemMenuHandler();

        novo.addActionListener(handler);
        abrir.addActionListener(handler);
        salvarThis.addActionListener(handler);
        salvar.addActionListener(handler);
        sair.addActionListener(handler);

        lexica.addActionListener(handler);
        sintatica.addActionListener(handler);
        geracaopascal.addActionListener(handler);
        geracaoc.addActionListener(handler);
        geracaocpp.addActionListener(handler);
        geracaojava.addActionListener(handler);
        compilar.addActionListener(handler);
        procurar.addActionListener(handler);
        selecionarTudo.addActionListener(handler);
        irParaLinha.addActionListener(handler);
        copiar.addActionListener(handler);
        colar.addActionListener(handler);
        recortar.addActionListener(handler);
        conf.addActionListener(new EditConfINIActionListener());
        conf2.addActionListener(handler);
        numera.addActionListener(handler);


        wwwVidal.addActionListener(handler);
        wwwCsalles.addActionListener(handler);
        wwwLuciano.addActionListener(handler);
        download.addActionListener(handler);
        manual.addActionListener(handler);
        sobre.addActionListener(handler);

        highlight.addActionListener(handler);

        //funcoes dos botoes
        btn_novo.addActionListener(handler);
        btn_abrir.addActionListener(handler);
        btn_salvar.addActionListener(handler);
        btn_high.addActionListener(handler);
        btn_geracpp.addActionListener(handler);
        btn_gerac.addActionListener(handler);
        btn_gerajava.addActionListener(handler);
        btn_gerapascal.addActionListener(handler);
        btn_run.addActionListener(handler);

        /**end INTERFACE------------------------------------------------*/
        //setando o tamanho padrao da janela (Total - 30% relativo a barra de iniciar do Windows)
        Dimension tamanhoTela = kitTela.getScreenSize();
        double alturaTela = tamanhoTela.getHeight();
        double larguraTela = tamanhoTela.getWidth();
        UI.this.setLocation((int) alturaTela / 4, (int) larguraTela / 4);
        UI.this.setSize(600, 400);
        UI.this.setExtendedState(MAXIMIZED_BOTH);
        UI.this.setVisible(true);
        areaDeTexto.requestFocus();
        areaDeTexto.setCaretPosition(10);
        if (btn_high.isSelected()) {
            context.setHighlighter(new StrategyConcretePortugol());
            context.FazerHighlight(areaDeTexto);
        }
    }

    /** verifica sistema operacional do usuario **/
    private void checaSistema() {
        File verifica = new File("C:\\WINDOWS");
        if (verifica.canRead()) {
            radioWindows.setSelected(true);
            radioLinux.setSelected(false);
        } else {
            radioWindows.setSelected(false);
            radioLinux.setSelected(true);
        }
    }
    //	--------------------------------------------------------------------
    /** Definicao das variaveis */
    /** Icone principal do programa*/
    public static String icon = "img/icon.png";
    /** informacoes de compilacao*/
    public static String config = "";
    private static final long serialVersionUID = 1L;
    //private  int linh = 2 ;
    private String texto_areaDeTexto = "";
    /** guarda as acoes realizadas para executar o UNDO/REDO*/
    public HashMap actions;
    public JTextPane painel;
    /** String para tratamento de TokenTudo */
    public static String tokens = "";
    private Toolkit kitTela = Toolkit.getDefaultToolkit();
    private boolean CTRLPressionado = false, SHIFTPressionado = false;
    /** String que armazena em buffer o codigo-fonte */
    public static String TokenTudo = "";
    /** seta que o S.O. usado ? windows*/
    public static JRadioButton radioWindows;
    /** seta que o S.O. usado ? linux*/
    public static JRadioButton radioLinux;
    /** seta que o S.O. usado ? string auxiliar para modificacao das configuracoes de compilacao*/
    public static String texto = "";//conf.ini
    /** informacoes sobre o arquivo destino (.exe)*/
    public static String exe = "";
    private int numberLine = 0;
    /** area de edicao de texto */
    public static JTextPane areaDeTexto;
    /** area para numeracao das linhas de texto  */
    public static JTextPane areaNumeroLinhas;
    /** area para mostrar variaveis no processo de debug  */
    private JScrollPane scrollPane;
    JFrame frame, frameConfig;
    JFrame frameDebug = new JFrame();
    private String strNumber = "";
    /** guarda a path do executavel gerado*/
    public static String caminho = null;
    /** guarda a pasta onde o arquivo cpp sera gerado*/
    public static String pasta = null;
    /** guarda informacoes da localizacao do arquivo*/
    public static File arquivoAux;
    private Container containerTexto;
    private static Compilacao compilacao;
    // janela do Aplicativo e seu tipo de layout
    private Container container;
    private GridBagLayout layout;
    private GridBagConstraints constraints;
    // barra de menu
    private JMenuBar barra;
    private JToolBar barraBotoes;
    private JMenu arquivo, executar, tools, opcoes, ajuda;
    private JMenuItem novo, abrir, salvar, salvarThis, sair, geracaocpp, geracaoc, geracaopascal, geracaojava, sobre, manual, wwwVidal, lexica;
    private JMenuItem sintatica, wwwLuciano, procurar, compilar, wwwCsalles, download, desfazer, refazer, numera;
    private JMenuItem selecionarTudo, irParaLinha, conf, conf2, colar, copiar, recortar, highlight;
    final JButton btn_high, btn_num, btn_salvar;
    final Icon ico_salvar_off;
    /** Styles para inserir highlight na edicao de texto*/
    public AbstractDocument doc;
    /** Actions mapeada para a acao de desfazer*/
    public UndoAction undoAction;
    /** Actions mapeada para a acao de refazer*/
    public RedoAction redoAction;
    public UndoManager undo = new UndoManager();
    private StrategyContext context = new StrategyContext();
    static SimpleAttributeSet BLACK = new SimpleAttributeSet();

    static {
        StyleConstants.setForeground(BLACK, Color.black);
        StyleConstants.setBold(BLACK, false);
        StyleConstants.setFontFamily(BLACK, "Courier New");
        StyleConstants.setFontSize(BLACK, 14);
    }
}
