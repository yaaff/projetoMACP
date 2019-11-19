package analise;

import geracao.SemanticaPascal;
import geracao.SemanticaJava;
import geracao.SemanticaCpp;
import com.sun.jna.Library;
import com.sun.jna.Native;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import principal.UI;
import principal.StrategyConcreteCpp;
import principal.StrategyContext;
import principal.MACP;
import principal.StrategyConcreteC;
import principal.StrategyConcreteJava;
import principal.StrategyConcretePascal;

/**Classe Compilacao utilizando o Padrao de Projeto Facade*/
public class Compilacao {

    private Token token;
    private String cppRefecence;
    FrameGeracao frameGeracao;
    FrameLexica frameLexica;
    FrameSintatica frameSintatica;
    /**interface que referencia a Interface Principal para a classe fachada Compilacao*/
    public static UI refInterface;
    private StrategyContext context = new StrategyContext();

    /**Construtor da classe Compilacao utilizando o Padrao de Projeto Facade*/
    public Compilacao() {
    }

    /**metodo que chama a analise lexica*/
    public void analiseLexica() {


        try {
            StartLexica.erros = 0;
            StartLexica startLex = new StartLexica(UI.areaDeTexto.getText());
            String resultado = startLex.getResultado();

            token = startLex.getTk();
            String txtResultado;
            if (StartLexica.erros == 0) {
                txtResultado = "                Nao contem erros lexicos                ";
            } else if (StartLexica.erros == 1) {
                txtResultado = "                Contem 1 erro lexico                ";
            } else {
                txtResultado = "                Contem " + String.valueOf(StartLexica.erros) + " erros lexicos                ";
            }

            if (resultado == "") {
                resultado = "                Nao contem erros lexicos                ";
            }

            frameLexica = FrameLexica.getInstance(refInterface, txtResultado, resultado);
        } catch (NullPointerException nulo) {
            JOptionPane.showMessageDialog(refInterface,
                    "Ocorreu um erro inesperado!\n"
                    + "Metodo: Compilacao.analiseLexica\n Descricao do erro:\n"
                    + nulo.getStackTrace(),
                    "ATENCAO!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Metodo para chamar a analise sintatica */
    public void analiseSintatica() {
        try {
            StartLexica startLex = new StartLexica(UI.areaDeTexto.getText());
            token = startLex.getTk();
            new StartGeraCode(token);

            String st = StartGeraCode.erroSintatico;
            String strSintatica = "";
            if (StartGeraCode.erroSintatico == "") {
                strSintatica = "                Nao contem erro sintatico                ";
            } else if (StartGeraCode.erroSintatico != "") {
                strSintatica = "                Contem erro sintatico                ";
            }
            frameSintatica = FrameSintatica.getInstance(refInterface, strSintatica, st);
        } catch (NullPointerException nulo) {
            JOptionPane.showMessageDialog(refInterface,
                    "Ocorreu um erro inesperado!\n"
                    + "Funcao: Compilacao.analiseSintatica()\nDescricao do erro:\n"
                    + nulo.getStackTrace(),
                    "ATENCAO!", JOptionPane.ERROR_MESSAGE);
        }
    }


    /** Metodo verificação lexica, sintatica, semantica sem exibir janela de codigo c++ em compilação*/
    public String geraCompiladoCpp() {
        String stCodigo = null;
        if (UI.areaDeTexto.getText().length() == 0) {
            JOptionPane.showMessageDialog(refInterface,
                    "A area de desenvolvimento esta vazia!",
                    MACP.version, JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                StartLexica.erros = 0;
                StartLexica startLex = new StartLexica(UI.areaDeTexto.getText());
                String resultado = startLex.getResultado();

                token = startLex.getTk();
                SemanticaCpp geracao = new SemanticaCpp(token);
                stCodigo = geracao.codigoGerado;

                while (stCodigo.contains(";\n;")) {
                    stCodigo = stCodigo.replaceAll(";\n;", ";\n");
                }
                while (stCodigo.contains(" ; ")) {
                    stCodigo = stCodigo.replaceAll(" ; ", ";");
                }
                while (stCodigo.contains("\n;")) {
                    stCodigo = stCodigo.replaceAll("\n;", ";\n");
                }
                while (stCodigo.contains(";;")) {
                    stCodigo = stCodigo.replaceAll(";;", ";");
                }
                while (stCodigo.contains("\n\n")) {
                    stCodigo = stCodigo.replaceAll("\n\n", "\n");
                }
                while (stCodigo.contains("};")) {
                    stCodigo = stCodigo.replaceAll("};", "}");
                }
                token = startLex.getTk();
                String stSintatica = geracao.erroSintatico;

                if ((StartLexica.erros == 0)) {
                    String strGeracao = "";
                    String strErros = "";
                    if (geracao.erroSintatico != "") {
                        strGeracao = "            Contem erros sintaticos              ";
                        strErros = stSintatica;
                        //  frameSintatica = FrameSintatica.getInstance(refInterface, strGeracao, strErros);
                    } else {
                        /*
                        strGeracao = "            Geracao de codigo realizada com sucesso!            ";
                        strErros = stCodigo;
                        frameGeracao = FrameGeracao.getInstance(refInterface, strGeracao, strErros);

                        //seta o ConcreteStrategy para C++ e habilita a area de texto para modificacoes do highlight
                        frameGeracao.txtAreaResultadoErros.setEditable(true);
                        context.setHighlighter(new StrategyConcreteCpp());
                        context.FazerHighlight(frameGeracao.txtAreaResultadoErros);
                        frameGeracao.txtAreaResultadoErros.setEditable(false);
                         * 
                         */
                    }

                } else if (StartLexica.erros != 0) {
                    String txtResultado;
                    if (StartLexica.erros == 1) {
                        txtResultado = "                Contem 1 erro lexico                ";
                    } else {
                        txtResultado = "                Contem " + String.valueOf(StartLexica.erros) + " erros lexicos                ";
                    }

                    frameLexica = FrameLexica.getInstance(refInterface, txtResultado, resultado);
                    return stCodigo;
                }
            } catch (NullPointerException nulo) {
                JOptionPane.showMessageDialog(refInterface,
                        "Ocorreu um erro inesperado!\n"
                        + "Metodo: Compilacao.geraCompiladoCpp\n Descricao do erro:\n"
                        + nulo.getStackTrace(),
                        "ATENCAO!", JOptionPane.ERROR_MESSAGE);
            }


        }
        return stCodigo;
    }


    /** Metodo para geracao do codigo c++ */
    public String geraCpp() {
        String stCodigo = null;
        if (UI.areaDeTexto.getText().length() == 0) {
            JOptionPane.showMessageDialog(refInterface,
                    "A area de desenvolvimento esta vazia!",
                    MACP.version, JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                StartLexica.erros = 0;
                StartLexica startLex = new StartLexica(UI.areaDeTexto.getText());
                String resultado = startLex.getResultado();

                token = startLex.getTk();
                SemanticaCpp geracao = new SemanticaCpp(token);
                stCodigo = geracao.codigoGerado;

                while (stCodigo.contains(";\n;")) {
                    stCodigo = stCodigo.replaceAll(";\n;", ";\n");
                }
                while (stCodigo.contains(" ; ")) {
                    stCodigo = stCodigo.replaceAll(" ; ", ";");
                }
                while (stCodigo.contains("\n;")) {
                    stCodigo = stCodigo.replaceAll("\n;", ";\n");
                }
                while (stCodigo.contains(";;")) {
                    stCodigo = stCodigo.replaceAll(";;", ";");
                }
                while (stCodigo.contains("\n\n")) {
                    stCodigo = stCodigo.replaceAll("\n\n", "\n");
                }
                while (stCodigo.contains("};")) {
                    stCodigo = stCodigo.replaceAll("};", "}");
                }
                token = startLex.getTk();
                String stSintatica = geracao.erroSintatico;

                if ((StartLexica.erros == 0)) {
                    String strGeracao = "";
                    String strErros = "";
                    if (geracao.erroSintatico != "") {
                        strGeracao = "            Contem erros sintaticos              ";
                        strErros = stSintatica;
                        frameSintatica = FrameSintatica.getInstance(refInterface, strGeracao, strErros);
                    } else {
                        strGeracao = "            Geracao de codigo realizada com sucesso!            ";
                        strErros = stCodigo;
                        frameGeracao = FrameGeracao.getInstance(refInterface, strGeracao, strErros);

                        //seta o ConcreteStrategy para C++ e habilita a area de texto para modificacoes do highlight
                        frameGeracao.txtAreaResultadoErros.setEditable(true);
                        context.setHighlighter(new StrategyConcreteCpp());
                        context.FazerHighlight(frameGeracao.txtAreaResultadoErros);
                        frameGeracao.txtAreaResultadoErros.setEditable(false);
                    }

                } else if (StartLexica.erros != 0) {
                    String txtResultado;
                    if (StartLexica.erros == 1) {
                        txtResultado = "                Contem 1 erro lexico                ";
                    } else {
                        txtResultado = "                Contem " + String.valueOf(StartLexica.erros) + " erros lexicos                ";
                    }

                    frameLexica = FrameLexica.getInstance(refInterface, txtResultado, resultado);
                    return stCodigo;
                }
            } catch (NullPointerException nulo) {
                JOptionPane.showMessageDialog(refInterface,
                        "Ocorreu um erro inesperado!\n"
                        + "Metodo: Compilacao.Geracpp\n Descricao do erro:\n"
                        + nulo.getStackTrace(),
                        "ATENCAO!", JOptionPane.ERROR_MESSAGE);
            }
            

        }
        return stCodigo;
    }

    /** Metodo para geracao do codigo c */
    public String geraC() {
        String stCodigo = null;
        if (UI.areaDeTexto.getText().length() == 0) {
            JOptionPane.showMessageDialog(refInterface,
                    "A area de desenvolvimento esta vazia!",
                    MACP.version, JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                StartLexica.erros = 0;
                StartLexica startLex = new StartLexica(UI.areaDeTexto.getText());
                String resultado = startLex.getResultado();

                token = startLex.getTk();
                SemanticaCpp geracao = new SemanticaCpp(token);
                stCodigo = geracao.codigoGerado;

                while (stCodigo.contains(";\n;")) {
                    stCodigo = stCodigo.replaceAll(";\n;", ";\n");
                }
                while (stCodigo.contains(" ; ")) {
                    stCodigo = stCodigo.replaceAll(" ; ", ";");
                }
                while (stCodigo.contains("\n;")) {
                    stCodigo = stCodigo.replaceAll("\n;", ";\n");
                }
                while (stCodigo.contains(";;")) {
                    stCodigo = stCodigo.replaceAll(";;", ";");
                }
                while (stCodigo.contains("\n\n")) {
                    stCodigo = stCodigo.replaceAll("\n\n", "\n");
                }
                while (stCodigo.contains("};")) {
                    stCodigo = stCodigo.replaceAll("};", "}");
                }
                token = startLex.getTk();
                String stSintatica = geracao.erroSintatico;

                if ((StartLexica.erros == 0)) {
                    String strGeracao = "";
                    String strErros = "";
                    if (geracao.erroSintatico != "") {
                        strGeracao = "            Contem erros sintaticos              ";
                        strErros = stSintatica;
                        frameSintatica = FrameSintatica.getInstance(refInterface, strGeracao, strErros);
                    } else {
                        strGeracao = "            Geracao de codigo realizada com sucesso!            ";
                        strErros = stCodigo;
                        frameGeracao = FrameGeracao.getInstance(refInterface, strGeracao, strErros);

                        //seta o ConcreteStrategy para C++ e habilita a area de texto para modificacoes do highlight
                        frameGeracao.txtAreaResultadoErros.setEditable(true);
                        context.setHighlighter(new StrategyConcreteC());
                        context.FazerHighlight(frameGeracao.txtAreaResultadoErros);
                        frameGeracao.txtAreaResultadoErros.setEditable(false);
                    }

                } else if (StartLexica.erros != 0) {
                    String txtResultado;
                    if (StartLexica.erros == 1) {
                        txtResultado = "                Contem 1 erro lexico                ";
                    } else {
                        txtResultado = "                Contem " + String.valueOf(StartLexica.erros) + " erros lexicos                ";
                    }

                    frameLexica = FrameLexica.getInstance(refInterface, txtResultado, resultado);
                    return stCodigo;
                }
            } catch (NullPointerException nulo) {
                JOptionPane.showMessageDialog(refInterface,
                        "Ocorreu um erro inesperado!\n"
                        + "Metodo: Compilacao.GeraC\n Descricao do erro:\n"
                        + nulo.getStackTrace(),
                        "ATENCAO!", JOptionPane.ERROR_MESSAGE);
            }
        }
        return stCodigo;
    }


    /** Metodo para geracao do codigo Pascal */
    public String geraPascal() {
        String stCodigo = null;
        if (UI.areaDeTexto.getText().length() == 0) {
            JOptionPane.showMessageDialog(refInterface,
                    "A area de desenvolvimento esta vazia!",
                    MACP.version, JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                StartLexica.erros = 0;
                StartLexica startLex = new StartLexica(UI.areaDeTexto.getText());
                String resultado = startLex.getResultado();

                token = startLex.getTk();
                SemanticaPascal geracao = new SemanticaPascal(token);
                stCodigo = geracao.codigoGerado;

                while (stCodigo.contains(";\n;")) {
                    stCodigo = stCodigo.replaceAll(";\n;", ";\n");
                }
                while (stCodigo.contains(" ; ")) {
                    stCodigo = stCodigo.replaceAll(" ; ", ";");
                }
                while (stCodigo.contains("\n;")) {
                    stCodigo = stCodigo.replaceAll("\n;", ";\n");
                }
                while (stCodigo.contains(";;")) {
                    stCodigo = stCodigo.replaceAll(";;", ";");
                }
                while (stCodigo.contains("\n\n")) {
                    stCodigo = stCodigo.replaceAll("\n\n", "\n");
                }
                while (stCodigo.contains("};")) {
                    stCodigo = stCodigo.replaceAll("};", "}");
                }
                token = startLex.getTk();
                String stSintatica = geracao.erroSintatico;

                if ((StartLexica.erros == 0)) {
                    String strGeracao = "";
                    String strErros = "";
                    if (geracao.erroSintatico != "") {
                        strGeracao = "            Contem erros sintaticos              ";
                        strErros = stSintatica;
                        frameSintatica = FrameSintatica.getInstance(refInterface, strGeracao, strErros);
                    } else {
                        strGeracao = "            Geracao de codigo realizada com sucesso!            ";
                        strErros = stCodigo;
                        frameGeracao = FrameGeracao.getInstance(refInterface, strGeracao, strErros);

                        //seta o ConcreteStrategy para C++ e habilita a area de texto para modificacoes do highlight
                        frameGeracao.txtAreaResultadoErros.setEditable(true);
                        context.setHighlighter(new StrategyConcretePascal());
                        context.FazerHighlight(frameGeracao.txtAreaResultadoErros);
                        frameGeracao.txtAreaResultadoErros.setEditable(false);
                    }

                } else if (StartLexica.erros != 0) {
                    String txtResultado;
                    if (StartLexica.erros == 1) {
                        txtResultado = "                Contem 1 erro lexico                ";
                    } else {
                        txtResultado = "                Contem " + String.valueOf(StartLexica.erros) + " erros lexicos                ";
                    }

                    frameLexica = FrameLexica.getInstance(refInterface, txtResultado, resultado);
                    return stCodigo;
                }
            } catch (NullPointerException nulo) {
                JOptionPane.showMessageDialog(refInterface,
                        "Ocorreu um erro inesperado!\n"
                        + "Metodo: Compilacao.GeraPascal\n Descricao do erro:\n"
                        + nulo.getStackTrace(),
                        "ATENCAO!", JOptionPane.ERROR_MESSAGE);
            }
        }
        return stCodigo;
    }

    /** Metodo para geracao do codigo Java */
    public String geraJava() {
        String stCodigo = null;
        if (UI.areaDeTexto.getText().length() == 0) {
            JOptionPane.showMessageDialog(refInterface,
                    "A area de desenvolvimento esta vazia!",
                    MACP.version, JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                StartLexica.erros = 0;
                StartLexica startLex = new StartLexica(UI.areaDeTexto.getText());
                String resultado = startLex.getResultado();

                token = startLex.getTk();
                SemanticaJava geracao = new SemanticaJava(token);
                stCodigo = geracao.codigoGerado;

                while (stCodigo.contains(";\n;")) {
                    stCodigo = stCodigo.replaceAll(";\n;", ";\n");
                }
                while (stCodigo.contains(" ; ")) {
                    stCodigo = stCodigo.replaceAll(" ; ", ";");
                }
                while (stCodigo.contains("\n;")) {
                    stCodigo = stCodigo.replaceAll("\n;", ";\n");
                }
                while (stCodigo.contains(";;")) {
                    stCodigo = stCodigo.replaceAll(";;", ";");
                }
                while (stCodigo.contains("\n\n")) {
                    stCodigo = stCodigo.replaceAll("\n\n", "\n");
                }
                while (stCodigo.contains("};")) {
                    stCodigo = stCodigo.replaceAll("};", "}");
                }
                token = startLex.getTk();
                String stSintatica = geracao.erroSintatico;

                if ((StartLexica.erros == 0)) {
                    String strGeracao = "";
                    String strErros = "";
                    if (geracao.erroSintatico != "") {
                        strGeracao = "            Contem erros sintaticos              ";
                        strErros = stSintatica;
                        frameSintatica = FrameSintatica.getInstance(refInterface, strGeracao, strErros);
                    } else {
                        strGeracao = "            Geracao de codigo realizada com sucesso!            ";
                        strErros = stCodigo;
                        frameGeracao = FrameGeracao.getInstance(refInterface, strGeracao, strErros);

                        //seta o ConcreteStrategy para C++ e habilita a area de texto para modificacoes do highlight
                        frameGeracao.txtAreaResultadoErros.setEditable(true);
                        context.setHighlighter(new StrategyConcreteJava());
                        context.FazerHighlight(frameGeracao.txtAreaResultadoErros);
                        frameGeracao.txtAreaResultadoErros.setEditable(false);
                    }

                } else if (StartLexica.erros != 0) {
                    String txtResultado;
                    if (StartLexica.erros == 1) {
                        txtResultado = "                Contem 1 erro lexico                ";
                    } else {
                        txtResultado = "                Contem " + String.valueOf(StartLexica.erros) + " erros lexicos                ";
                    }

                    frameLexica = FrameLexica.getInstance(refInterface, txtResultado, resultado);
                    return stCodigo;
                }
            } catch (NullPointerException nulo) {
                JOptionPane.showMessageDialog(refInterface,
                        "Ocorreu um erro inesperado!\n"
                        + "Metodo: Compilacao.GeraJava\n Descricao do erro:\n"
                        + nulo.getStackTrace(),
                        "ATENCAO!", JOptionPane.ERROR_MESSAGE);
            }
        }
        return stCodigo;
    }

    private class TTT extends JFrame {

        public TTT() {
            setSize(200, 200);
            setLayout(new BorderLayout());
            JScrollPane scrollPane = new JScrollPane();
            getContentPane().add(scrollPane);
            JTextArea area = new JTextArea();
            scrollPane.setViewportView(area);
            show();
        }
    }

    /** Metodo que realiza a compilacao do codigo e gera o objeto */
    public boolean facadeCompilar() {
        try {
            StartLexica.erros = 0;
            StartLexica startLex = new StartLexica(UI.areaDeTexto.getText());
            String resultado = startLex.getResultado();
            token = startLex.getTk();
            SemanticaCpp gerador = new SemanticaCpp(token);
            

            String stSintatica = gerador.getErroSintatico();
            cppRefecence = gerador.codigoGerado;

            if ((StartLexica.erros == 0)) {
                if (stSintatica != "") {
                    FrameSintatica.getInstance(refInterface, "               Contem erros sintaticos                 ", stSintatica);
                } else {
                    return true;
                }
            } else if (StartLexica.erros != 0) {
                String txtResultado;
                if (StartLexica.erros == 1) {
                    txtResultado = "            Contem 1 erro lexico              ";
                } else {
                    txtResultado = "                Contem " + String.valueOf(StartLexica.erros) + " erros lexicos                ";
                }
                FrameLexica.getInstance(refInterface, txtResultado, resultado);
            }
        } catch (NullPointerException nulo) {
            JOptionPane.showMessageDialog(refInterface,
                    "Ocorreu um erro inesperado!\n"
                    + "Funcao: facadeCompila()\nDescricao do erro:"
                    + nulo.getStackTrace(),
                    "ATENCAO!", JOptionPane.ERROR_MESSAGE);
        }
        return false;

    }

    public void RodarAutomatico2() {
        if (UI.areaDeTexto.getText().length() == 0) {
            JOptionPane.showMessageDialog(refInterface,
                    "A area de desenvolvimento esta vazia!",
                    MACP.version, JOptionPane.WARNING_MESSAGE);
        }
    }

    /** Chama o compilador automaticamente para rodar mais facil o codigo fonte */
    public void RodarAutomatico() {
        if (UI.areaDeTexto.getText().length() == 0) {
            JOptionPane.showMessageDialog(refInterface,
                    "A area de desenvolvimento esta vazia!",
                    MACP.version, JOptionPane.WARNING_MESSAGE);
        } else {
            if (facadeCompilar()) {
                if (UI.pasta == null) {
                    try {
                        refInterface.Salvar();//salva o .txt
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                
                if (UI.pasta != null) {
                    // compilar cpp e executar exe;
                    String cppCode = geraCompiladoCpp();
                    File cppOut = new File(System.getProperty("user.dir"), "macp-translated-file.cpp");
                    try {                        
                        if (!cppOut.exists()) {
                            cppOut.createNewFile();
                        }
                        FileOutputStream fileOutputStream = new FileOutputStream(cppOut);
                        DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
                        dataOutputStream.writeBytes(cppCode);
                        dataOutputStream.flush();
                        dataOutputStream.close();

                        File fileConf = new File(System.getProperty("user.dir"), "conf.ini");                                                
                        FileReader fileReader = new FileReader(fileConf);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        String compPath = bufferedReader.readLine();
                        String includesPath = bufferedReader.readLine();
                        bufferedReader.close();
                        if (compPath == null || compPath.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "O conf.ini está vazio!");
                            return;
                        }

                        String path = cppOut.getAbsolutePath().replace("macp-translated-file.cpp", "");
                        String comp = "\"" + compPath + "\"";
                        String in = "\"" + cppOut.getAbsolutePath() + "\"";
                        String out = "\"" + cppOut.getAbsolutePath().replace(".cpp", ".exe") + "\"";
                        String includes = includesPath;

                        //Salva o arquivo de script .bat para compilar e executar
                        SalvarBat(cppOut.getAbsolutePath(), comp, in, out, includes);

                        //Executa o Runtime
                        try {
				if(UI.radioWindows.isSelected())
					Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL "+path+"MACP.bat");//executa o .bat
				else{
                                        String unixPath = path+"MACP.bat";
					Runtime.getRuntime().exec("xterm -e chmod 777 "+unixPath);//seta permissao para executar o .bat
					Runtime.getRuntime().exec("xterm -hold -e "+unixPath);//executa o .bat para Linux
                                        //System.out.println("xterm -e chmod 777 "+unixPath+"\n"+ "xterm -hold -e ."+unixPath);
				}

			}
			catch (IOException e) {
				JOptionPane.showMessageDialog(refInterface,
						"Ocorreu um erro ao gerar o executavel.\n" +
						"Verifique se o sistema esta configurado corretamente!\n" +
						"Foi salvo um arquivo .cpp na mesma pasta do seu codigo-fonte para \n" +
						"geracao do executavel",
						MACP.version,JOptionPane.ERROR_MESSAGE);
			}
                        
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Ocorreu um erro ao tentar executar.\n"
                                + "Talvez voce precise configurar o compilador no conf.ini");
                        e.printStackTrace();
                    }

                }
            }
        }//fim do else
    }

    public static interface NativeAdapter extends Library {

        void executar(byte[] programa, int size);

        void compilar(
                byte[] compilador, int size1,
                byte[] arquivo_entrada, int size2,
                byte[] arquivo_saida, int size3);
    }

    /** Metodo para salvar script de compilacao */
    public void SalvarBat(String path, String comp, String in, String out, String includes) throws IOException {                
           
            // salva o bat na path atual
            String linhasComando="";
            if (UI.radioWindows.isSelected()) {
                linhasComando = "title "+MACP.version+" [ compilando e executando ]\r\n"
                        + "cd"+File.separator+" \r\n"
                        + "c:"+File.separator+" \r\n"                        
                        + comp + " " + in + " -o " + out + " " + includes +"\r\n"
                        + "cls \r\n"
                        + out + "\r\n"
                        + "pause";
            } else {
                if(includes==null)
                    includes = "";
                linhasComando = comp + " "+ in + " -o " + out + " " + includes +"\n"
                        + "cd \"" + path.replace("macp-translated-file.cpp", "") + "\"\n"
                        + "chmod 777 macp-translated-file.exe\n"
                        //+ "clear \r\n"
                        + "./macp-translated-file.exe\n";
                        //+ "."+out.replace("\"", "") + "\r\n";
            }
            //System.out.println(linhasComando);
            //System.out.println(path.replace("macp-translated-file.cpp", "MACP.bat"));
            
            String bat = path.replace("macp-translated-file.cpp", "MACP.bat");
            FileOutputStream outbat = new FileOutputStream(bat);
            PrintStream p = new PrintStream(outbat);
            p.write(linhasComando.getBytes());
            p.close();
            File Temp = new File(bat);
            Temp.deleteOnExit();
    }
}
