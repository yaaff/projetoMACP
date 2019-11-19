/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import static analise.Compilacao.refInterface;
import analise.FrameLexica;
import analise.FrameSintatica;
import analise.StartLexica;
import analise.Token;
import geracao.SemanticaCpp;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;


public class Cmd extends JFrame
{
        private Token token;
        FrameLexica frameLexica;
        private String cppRefecence;
        
	static JTextArea texto;
        
	public Cmd()
	{
                super("C:\\Windows\\system32\\cmd.exe");
                if (UI.areaDeTexto.getText().length() == 0) 
                {
                    JOptionPane.showMessageDialog(refInterface,
                        "A area de desenvolvimento esta vazia!",
                        MACP.version, JOptionPane.WARNING_MESSAGE);
                } 
                
                else 
                {
                    if (facadeCompilar()) 
                    {
                        if (UI.pasta == null) 
                        {
                            try 
                            {
                                    refInterface.Salvar();//salva o .txt
                            } 
                            
                            catch (IOException e) 
                            {
                                e.printStackTrace();
                            }
                        }
                
                        if (UI.pasta != null) 
                        {
                            // compilar cpp e executar exe;
                            //String cppCode = geraCompiladoCpp();
                            File cppOut = new File(System.getProperty("user.dir"), "macp-translated-file.cpp");
                            try 
                            {                        
                                if (!cppOut.exists()) 
                                {
                                    cppOut.createNewFile();
                                }
                                
                                FileOutputStream fileOutputStream = new FileOutputStream(cppOut);
                                DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
                                //dataOutputStream.writeBytes(cppCode);
                                dataOutputStream.flush();
                                dataOutputStream.close();
                                
                                File fileConf = new File(System.getProperty("user.dir"), "conf.ini");                                                
                                FileReader fileReader = new FileReader(fileConf);
                                BufferedReader bufferedReader = new BufferedReader(fileReader);
                                String compPath = bufferedReader.readLine();
                                String includesPath = bufferedReader.readLine();
                                bufferedReader.close();
                                
                                if (compPath == null || compPath.isEmpty()) 
                                {
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
                                try 
                                {
                			if(UI.radioWindows.isSelected()) // se chegou ate aqui, então o algoritmo não possui erros lexicos nem erros sintaticos
                                        { 
                                            ImageIcon img = new ImageIcon("cmd.jpg"); // icone do cmd
                                            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // essa linha de codigo fechava a IDE junto com o compilador.
                                            setIconImage(img.getImage()); // carregando icone do cmd
                                            setSize(680,340);
		
                                            texto = new JTextArea(100,100);
                                            //texto.setText("C:\\User\\Lucas>"); // seta o texto inicial do jtextarea
                                            //texto.append(""); // adiciona texto no jtextarea
                                            texto.setBackground(Color.black);
                                            texto.setForeground(Color.white);
                                            texto.setFont(new Font("Lucida Console", Font.PLAIN, 12)); 
                                            add(texto);
                    
                                            //texto.addActionListener (this);
                    
                                            JScrollPane scrollPane = new JScrollPane(texto);
                                            scrollPane.setPreferredSize(new Dimension(450, 110));
                                            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                                            texto.setLineWrap(true); // quando chega no canto direito, quebra linha
                                            texto.setWrapStyleWord(false); // true = pula linha sem cortar a palavra || false = pula linha cortando a palavra
                                            add(scrollPane, BorderLayout.CENTER);
                                            setLocation(100, 100); // posião incial da tela
                                            setVisible(true); //FIMTELA ------------------------------------------------------------------------------------------------
                                            
                                            String text = UI.areaDeTexto.getText();
                                            Interpretacao interpretacao = new Interpretacao(text, this); // CHAMANDO O INTERPRETADOR
                                            int SAIR = interpretacao.SAIR; // pega o sair antes de terminar a classe interpretação, sair sempre é 0
                                            if(SAIR != 0) setVisible(false);
                                            //System.out.println("RETORNO: " + interpretacao.retorno());
                                            //System.out.println("SAIR: " + SAIR);
                                                    
                                            // ESSA LINHA DE CODIGO ABRE A SAIDA PADRAO DO C! Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL "+path+"MACP.bat");//executa o .bat      
                                        }
                                            
                			else
                                        {
                                                String unixPath = path+"MACP.bat";
                            			Runtime.getRuntime().exec("xterm -e chmod 777 "+unixPath);//seta permissao para executar o .bat
                            			Runtime.getRuntime().exec("xterm -hold -e "+unixPath);//executa o .bat para Linux
                                            //System.out.println("xterm -e chmod 777 "+unixPath+"\n"+ "xterm -hold -e ."+unixPath);
                                	}
                                }
                            
                                catch (IOException e) 
                                {
                                    JOptionPane.showMessageDialog(refInterface,
						"Ocorreu um erro ao gerar o executavel.\n" +
						"Verifique se o sistema esta configurado corretamente!\n" +
						"Foi salvo um arquivo .cpp na mesma pasta do seu codigo-fonte para \n" +
						"geracao do executavel",
						MACP.version,JOptionPane.ERROR_MESSAGE);
                                }
                            } 
                            
                            catch (Exception e) 
                            {
                                JOptionPane.showMessageDialog(
                                    null,
                                    "Ocorreu um erro ao tentar executar.\n"
                                    + "Talvez voce precise configurar o compilador no conf.ini");
                                e.printStackTrace();
                            }
                        }
                    }
                }
	}

        public boolean facadeCompilar() 
        {
            try 
            {
                StartLexica.erros = 0;
                StartLexica startLex = new StartLexica(UI.areaDeTexto.getText());
                String resultado = startLex.getResultado();
                token = startLex.getTk();
                SemanticaCpp gerador = new SemanticaCpp(token);
                
                String stSintatica = gerador.getErroSintatico();
                cppRefecence = gerador.codigoGerado;

                if ((StartLexica.erros == 0)) 
                {
                    if (stSintatica != "") 
                    {
                        FrameSintatica.getInstance(refInterface, "               Contem erros sintaticos                 ", stSintatica);
                    } else 
                    {
                    return true;
                    }
                } 
                
                else if (StartLexica.erros != 0) 
                {
                    String txtResultado;
                    if (StartLexica.erros == 1) 
                    {
                        txtResultado = "            Contem 1 erro lexico              ";
                } 
                    else 
                    {
                        txtResultado = "                Contem " + String.valueOf(StartLexica.erros) + " erros lexicos                ";
                    }
                        FrameLexica.getInstance(refInterface, txtResultado, resultado);
                }
            } 
            
            catch (NullPointerException nulo) 
            {
                JOptionPane.showMessageDialog(refInterface,
                    "Ocorreu um erro inesperado!\n"
                    + "Funcao: facadeCompila()\nDescricao do erro:"
                    + nulo.getStackTrace(),
                    "ATENCAO!", JOptionPane.ERROR_MESSAGE);
            }
        return false;

        }
        
        public void SalvarBat(String path, String comp, String in, String out, String includes) throws IOException 
        {                
           
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
