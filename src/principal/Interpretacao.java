package principal;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.KeyStroke;
import com.towel.math.Expression; // calcula expressões matematicas
import groovy.lang.GroovyShell;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import java.util.List;

/**
 *
 * @author Lucas
 */
public class Interpretacao{
    
    Cmd p;
    String str;
    StringBuffer tempdigitado = new StringBuffer(100);
    int key, indexvar, ERROS=0, SAIR=0;
    ArrayList<String> algtexto = new ArrayList<String>(); // array que guarda cada linha de codigo separadas
    ArrayList<String> vars = new ArrayList<String>(); // array que guarda as variaveis declaradas (ARMAZENA O NOME)
    ArrayList<String> varscorresp = new ArrayList<String>(); // array que se relaciona à outra array para receber as variaveis (ARMAZENA OS VALROES)
    
    public Object retorno(){   
        System.out.println("retornando");
        return SAIR;  
    } 
    
    public Interpretacao(String texto, Cmd c) throws IOException{
        p = c;
        String variaveis = null;
        String inicio = null;
        String tipo = null; 
        String reais, inteiros, caracteres, vetores;
        int linhas = 0, i = 0, virgulas = 0;
        
        int len = texto.length();  
        System.out.println("tamanho do texto: " + len);
        
        System.out.println("indice de variaveis: " + texto.indexOf("variaveis"));
        System.out.println("indice do inicio: " + texto.indexOf("inicio"));
        
        // VARIAVEIS
        variaveis = texto.substring(texto.indexOf("variaveis")+10, texto.indexOf("inicio"));
        //Contando quantas linhas tem entre variaveis e inicio (quantos tipos foram declarados)
        for (i = 0; i < variaveis.length(); i++) { // for que conta quantas linhas tem na declaração de variaveis
            if (variaveis.charAt(i) == ';') {
                    linhas++;
            }
        }
        System.out.println("numero de linhas de variaveis: " + linhas);
        
        for (i = 0; i < variaveis.length(); i++) { // for que pega quantas virgulas tem na declaração de variaveis
            if (variaveis.charAt(i) == ',') {
                    virgulas++;
            }
        }
        System.out.println("numero de virgulas de variaveis: " + (virgulas));
        
        System.out.println("numero de variaveis declaradas: " + (virgulas + linhas - 1)); // formula para encontrar quantas variaveis foram declaradas
        
        String temp = null;
        StringBuffer teste = new StringBuffer(100);
        System.out.println("QUANTIDADE DE CHAR EM VARIAVEIS: " + variaveis.length());
        for (i = 0; i < (variaveis.length()-4); i++) {
            while(variaveis.charAt(i) != ',')
            {
                if(variaveis.charAt(i) == ';') break;
                if(variaveis.charAt(i) == ':')
                {
                    while(variaveis.charAt(i+1) != ';'){i++;} // dentro desse while é possivel pegar o tipo das variaveis declaradas
                }
                //if (Character.isAlphabetic(variaveis.charAt(i))==true && variaveis.charAt(i+1) != ';') // não pegava os underlines e etc
                if(variaveis.charAt(i+1) != ';') teste.append(variaveis.charAt(i));
                i++;
            }
            if(i == variaveis.length()) break;
            temp = teste.toString(); // temp armazena a variavel delcarada
            temp = temp.replaceAll(" ", "");
            temp = temp.replaceAll("\n", "");
            temp = temp.replaceAll("\t", "");
            teste.delete(0, teste.length()); // apaga o que tinha no buffer para pegar outra variavel
            vars.add(temp); // adicionando o nome da variável à array
        }
        
        ArrayList<String> ordenacao = new ArrayList<String>(vars.size());
        //List ordenacao = new ArrayList(vars.size());
        for(int counter = 0; counter < vars.size(); counter++) 
        {
            ordenacao.add(counter, "0"); // "inicializando" as variaveis da lista ordenacao
        } 
        
        int inf; // variável que recebe o valor de contendencia de cada variavel
        for(int counter = 0; counter < vars.size(); counter++) // laço que pega a "contendencia" de cada variavel
        {  
            for(int aux = counter+1; aux < vars.size(); aux++)
            {
                if(vars.get(counter).contains(vars.get(aux)))
                {
                    inf = new Integer(ordenacao.get(counter));
                    inf++;
                    ordenacao.set(counter, String.valueOf(inf));
                }
                if(vars.get(aux).contains(vars.get(counter)))
                {
                    inf = new Integer(ordenacao.get(aux));
                    inf++;
                    ordenacao.set(aux, String.valueOf(inf));
                }
            }
        }
        
        int menor = 100, maior = 0;
        for(int counter = 0; counter < vars.size(); counter++) // laço que ordena as 2 array, nome e contidencia (SORT ORDEM CRESCENTE)
        {
            for (i=0; i < vars.size()-1; i++) 
            {
                System.out.println("comparando: " + ordenacao.get(i) + " com " + ordenacao.get(i+1) + ":" + ordenacao.get(i).compareTo(ordenacao.get(i+1)));
                if (ordenacao.get(i).compareTo(ordenacao.get(i+1)) > 0)
                {
                    temp = vars.get(i);
                    vars.set(i,vars.get(i+1) );
                    vars.set(i+1, temp);
                    temp = ordenacao.get(i);
                    ordenacao.set(i,ordenacao.get(i+1) );
                    ordenacao.set(i+1, temp);
                }
            }
        }
        
        ArrayList<String> inverte = new ArrayList<String>(vars.size());
        ArrayList<String> invertevar = new ArrayList<String>(vars.size());
        i = ordenacao.size() - 1;
        while ( i >= 0) // laço para ordenar as 2 array do maior para o menor (INVERTE A ARRAY)  
        {  
            inverte.add(ordenacao.get(i));
            invertevar.add(vars.get(i));
            i--;  
        }  
        vars = invertevar;
        ordenacao = inverte;  
        
        System.out.println("apresentando por ordem de contendencia(maior para o menor): ");
        for(int counter = 0; counter < vars.size(); counter++) System.out.println(vars.get(counter) + " " + ordenacao.get(counter));
        System.out.println("apresentando a arraylist vars: ");
        for(int counter = 0; counter < vars.size(); counter++) 
        {
            System.out.println(counter + ":" + vars.get(counter)); // mostra todos os elementos da arraylist
            varscorresp.add(counter, null); // "inicializando" as variaveis em uma arraylist
        } 
             
        tipo = variaveis.substring(variaveis.indexOf(":")+1, variaveis.indexOf(";"));
        System.out.println("tipo da variavel delcarada: " + tipo);
                
        // INICIO
        inicio = texto.substring(texto.indexOf("inicio")+6, texto.indexOf("fim."));
        linhas = 0;
        int cont = 0;
        System.out.println(teste);
        for (i = 0; i < inicio.length(); i++) 
        {
            if (Character.isWhitespace(inicio.charAt(i))==true && cont == 0); // if para não adicionar os espaços antes da linha de codigo
            else if (inicio.charAt(i) == '\n') { // aqui pode ser implementado a interpretação de cada linha do algoritmo
                temp = teste.toString();
                algtexto.add(temp);
                teste.delete(0, teste.length());
                linhas++;
                cont = 0;
            }
            else {
                    teste.append(inicio.charAt(i));
                    cont++;
            }
        }
        System.out.println("numero de linhas no inicio: " + (linhas-1));
        for(int counter = 0; counter < algtexto.size(); counter++) System.out.println(counter + ":" + algtexto.get(counter));
        
        p.texto.addKeyListener(new KeyListener() {
            StringBuffer temp = new StringBuffer(100);
            String str = null;
            @Override
            public void keyTyped(KeyEvent ke) {

            }

            @Override
            public void keyPressed(KeyEvent ke) {
                key = ke.getKeyCode();
                if(SAIR==0)
                {
                    if (key == KeyEvent.VK_ENTER)
                    {
                        System.out.println("o que foi digitado no cmd: " + temp);
                        str = temp.toString();
                        // adicionar temp à variavel
                        //varscorresp.add(indexvar, str);
                        varscorresp.set(indexvar, str);
                        temp.delete(0, temp.length());
                        tempdigitado.delete(0, tempdigitado.length());
                    }
                    else { // pega os caracteres apertados na jtextarea e adiciona a temp
                        if(key != KeyEvent.VK_BACK_SPACE) {
                            p.texto.setEditable(true);
                            temp.append(ke.getKeyChar());
                            tempdigitado.append(ke.getKeyChar());
                        }
                        else if(key == KeyEvent.VK_BACK_SPACE && tempdigitado.length() > 0) {
                            p.texto.setEditable(true);
                            //temp.append(ke.getKeyChar());
                            //System.out.println("templength: "+tempdigitado.length());
                            temp.deleteCharAt(temp.length()-1);
                            tempdigitado.deleteCharAt(tempdigitado.length()-1);
                        }
                        else if(key == KeyEvent.VK_BACK_SPACE && tempdigitado.length() == 0) {
                            p.texto.setEditable(false);
                        }
                        System.out.println("append: "+temp);
                    }
                } else { // else que fecha a janela apos ser pressionado alguma tecla depois do fim da execução do programa
                    p.dispose();
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                
            }
        });
        
        p.texto.getCaret().setVisible(true);  
        p.texto.setCaretColor(Color.white);
        new NewThread();
    }
    
    class NewThread implements Runnable {
        String temp;
        Thread t;
        
        NewThread() {
            String temp = null;
            t = new Thread(this);
            t.start();
        }
        
        public void run() {
            try {
                String variavel = null, texto, resultado = null, temporaria = null, de = null, ate = null, passo = null;
                Boolean resultse = false, resultenquanto = false, bloco = false, repita = false;
                int index = 0, indexresult, colchetes = 0, repeticoespara = 0, repeticoescpx = 0, indexrepeticoescpx = 0, indexpara = 0, incremento = 0, repeticoesenquanto = 0, counterenquanto = 0, counterrepita = 0, fimenquanto = 0, index_var_para = 0, ate_para = 0; // indexresult é a variavel que receberá um resultado
                ArrayList<Integer> index_colchete = new ArrayList<Integer>();
                index_colchete.add(0);
                index_colchete.add(0);
                index_colchete.add(0);
                index_colchete.add(0);
                for(int counter = 0; counter < algtexto.size(); counter++) // INTERPRETAÇÂO !!!
                {
                    System.out.println("linha da vez: "+algtexto.get(counter)+" de numero: "+counter);
                    if(algtexto.get(counter).contains("novalinha"))
                    {
                        p.texto.append("\n");
                    }
                    else if(algtexto.get(counter).contains("pausa")) 
                    {
                        while(key==0) {
                                Thread.sleep(500);
                        }
                        if(key!=KeyEvent.VK_ENTER) p.texto.setText("");
                        else; // quando o ENTER é pressionado no comando pausa, o programa buga.
                        key=0;
                        p.texto.setEditable(false);
                    }
                    else if(algtexto.get(counter).contains("[[")) 
                    {
                        if(algtexto.get(counter-1).contains("enquanto"))
                        {
                            System.out.println("ENQUANTO na linha anterior");
                            index_colchete.set(colchetes, counter);
                            System.out.println("colchete ENQUANTO: "+colchetes);
                            System.out.println("index colchete ENQUANTO: "+index_colchete.get(colchetes));
                        }
                        if(!algtexto.get(counter-1).contains("se")) 
                        {
                            System.out.println("nao tem SE na linha anterior");
                            colchetes++;
                        }
                    }
                    else if(algtexto.get(counter).contains("]];") && repeticoescpx > 1) // tratamento do incremento do PARA com bloco de execução 
                    {
                        incremento = new Integer(de) + new Integer(passo);
                        de = String.valueOf(incremento);
                        varscorresp.set(indexpara, de);
                        //repeticoescpx--;
                        repeticoescpx = ate_para /*- new Integer(de)*/;
                        if(new Integer(de) <= ate_para) counter = indexrepeticoescpx-2;
                    }
                    else if(algtexto.get(counter).contains("escrever")) 
                    {
                        if(algtexto.get(counter).contains("'")) // apresentar texto
                        { 
                            temp = algtexto.get(counter).toString();
                            temp = temp.substring(temp.indexOf("escrever '")+10, temp.indexOf("';"));
                            System.out.println("tempantes: "+temp);
                            if(temp.contains("\\n")) {
                                temp = temp.replace("\\n", "\n");
                            }                            
                            System.out.println("tempdepois: "+temp);                            
                            p.texto.append(temp);
                        }
                        else // apresentar valor de uma variavel
                        { 
                            texto = algtexto.get(counter);
                            variavel = texto.substring(texto.indexOf("escrever ")+9, texto.indexOf(";"));
                            index = vars.indexOf(variavel);
                            p.texto.append(varscorresp.get(index));
                        }
                        p.texto.setCaretPosition(p.texto.getText().length());
                    }
                    else if(algtexto.get(counter).contains("senao"))
                    {
                        if(resultse == true && !algtexto.get(counter+1).contains("[[")) counter++;
                        else if(resultse == true && algtexto.get(counter+1).contains("[["))
                        {
                            while(true)
                            {
                                if(algtexto.get(counter).contains("]]")) break;
                                counter++;
                            }
                        }
                    }
                    else if(algtexto.get(counter).contains("fimse")) System.out.println("ACHEI UM FIMSE");
                    else if(algtexto.get(counter).contains("para")) 
                    {
                        texto = algtexto.get(counter);
                        System.out.println("texto antes: "+texto);
                        texto = texto.replaceAll("para", "");
                        
                        temporaria = texto.substring(texto.indexOf("de")+2, texto.indexOf("ate"));
                        temporaria = temporaria.replaceAll(" ", "");
                        de = temporaria;
                        texto = texto.replaceAll("de", "");
                        
                        temporaria = texto.substring(texto.indexOf("ate")+3, texto.indexOf("passo"));
                        temporaria = temporaria.replaceAll(" ", "");
                        ate = temporaria;
                        texto = texto.replaceAll("ate", "");
                        
                        temporaria = texto.substring(texto.indexOf("passo")+5, texto.indexOf("faca"));
                        temporaria = temporaria.replaceAll(" ", "");
                        passo = temporaria;
                        texto = texto.replaceAll("passo", "");
                        texto = texto.replaceAll("faca", "");
                        System.out.println("texto depois: "+texto);
                        
                        for (String var: vars) // laço que substitui a variavel pelo seu respectivo valor
                        {
                                if(texto.contains(var)){ // verifica se a variavel está contida na arraylist
                                    index = vars.indexOf(var); // se sim, pega o index dessa variavel
                                }
                        }
                        System.out.println("de: "+de+" ate: "+ate+" passo: "+passo);
                        index_var_para = index;
                        ate_para = new Integer(ate);
                        varscorresp.set(index, de);
                        indexpara = index;
                        
                        if(algtexto.get(counter+1).contains("[[")) // se é um bloco de codigo
                        {
                            counter++;
                            repeticoescpx = new Integer(ate) - new Integer(de);
                            if(repeticoescpx % new Integer(passo) == 0) repeticoescpx = (repeticoescpx/new Integer(passo))+1;
                            else repeticoescpx = (repeticoescpx/new Integer(passo))+1;
                            System.out.println("repeticoescpx: "+repeticoescpx);
                            indexrepeticoescpx = counter+2;
                        }
                        else // ou apenas uma linha de codigo
                        {
                            repeticoespara = new Integer(ate) - new Integer(de);
                            if(repeticoespara % new Integer(passo) == 0) repeticoespara = (repeticoespara/new Integer(passo))+1;
                            else repeticoespara = (repeticoespara/new Integer(passo))+1;
                            System.out.println("repeticoes: "+repeticoespara);
                        }
                    }
                    else if(algtexto.get(counter).contains("enquanto")) 
                    {
                        counterenquanto = counter;
                        texto = algtexto.get(counter);
                        System.out.println("texto antes: "+texto);
                        texto = texto.replaceAll("enquanto", "");
                        texto = texto.replaceAll("faca", "");
                        texto = texto.replaceAll(" ", "");
                        for (String var: vars) // laço que substitui a variavel pelo seu respectivo valor
                        {
                            if(texto.contains(var)) // verifica se a variavel está contida na arraylist
                            {
                                index = vars.indexOf(var); // se sim, pega o index dessa variavel
                            }
                            else
                            {
                                System.out.println("var nao contem");
                                System.out.println(texto);
                                System.out.println(var);
                            }
                            if(varscorresp.get(index) == null) texto = texto.replaceAll(var, "0"); // e substitui pelo respectivo valor
                            else texto = texto.replaceAll(var, varscorresp.get(index)); // e substitui pelo respectivo valor
                        }
                        System.out.println("texto depois: "+texto);
                        resultenquanto = (Boolean) new GroovyShell().evaluate(texto); // retorna true ou false em relação à expressão booleana
                        System.out.println("apresentando a expressao dentro do enquanto: " + texto + " :" + resultenquanto);
                        
                        if(algtexto.get(counter+1).contains("[[")) // se é um bloco de codigo
                        {
                            System.out.println("enquanto é um bloco");
                            bloco = true;
                        }
                        else if(!algtexto.get(counter+1).contains("[[")) // ou apenas uma linha de codigo
                        {
                            System.out.println("enquanto não é um bloco");
                            bloco = false;
                            if(resultenquanto == false) counter++;
                        }
                        
                        if(resultenquanto == true && bloco == false) repeticoesenquanto = 2;
                        if(resultenquanto == false && bloco == true) // sair do bloco
                        {
                            int colch;
                            colch = colchetes;
                            while(true) // laço para pular todo o bloco do WHILE
                            {
                                if(algtexto.get(counter).contains("[[")) colchetes++;
                                else if(algtexto.get(counter).contains("]];"))
                                {
                                    colchetes--;
                                    resultenquanto = true;
                                    if(colchetes==colch) break;
                                }
                                counter++;
                            }
                        }
                    }
                    else if(algtexto.get(counter).contains("repita"))
                    {
                        counterrepita = counter++;
                        repita = true;
                    }
                    else if(algtexto.get(counter).contains("ler")) 
                    {
                        texto = algtexto.get(counter);
                        variavel = texto.substring(texto.indexOf("ler ")+4, texto.indexOf(";"));
                        if(vars.indexOf(variavel)!= -1) // pegando o index da variavel na array de declaração de variaveis
                        {
                            indexvar = vars.indexOf(variavel);
                        }
                        else ERROS++;
                        while(key!=KeyEvent.VK_ENTER) {
                            //if(key==KeyEvent.VK_BACK_SPACE && tempdigitado.length()==0) {
                                //p.texto.setEditable(false);
                            //} // false quando o texto digitado for igual a zero
                            //else{
                                //p.texto.setEditable(true);
                                //System.out.println("aqui");
                                //p.texto.setCaretPosition(p.texto.getText().length()); // setando o curso no final
                                Thread.sleep(500); //}
                        }
                        key=0;
                        p.texto.setEditable(false);
                    }
                    else if(algtexto.get(counter).contains("se") && algtexto.get(counter).contains("entao"))
                    {
                        texto = algtexto.get(counter);
                        texto = texto.substring(texto.indexOf("se")+2, texto.indexOf("entao"));
                        // Organizando o "se"
                        if(texto.contains("<=")) texto = texto.replaceAll("<=", "!@@");
                        else if(texto.contains(">=")) texto = texto.replaceAll(">=", "!@#");
                        else if(texto.contains("<>")) texto = texto.replaceAll("<>", "!##");
                        else if(texto.contains("=")) texto = texto.replaceAll("=", "==");
                        if(texto.contains("verdadeiro")) texto = texto.replaceAll("verdadeiro", "1");
                        if(texto.contains("falso")) texto = texto.replaceAll("falso", "0");
                        if(texto.contains(" e ")) texto = texto.replaceAll("e", "&");
                        if(texto.contains(" ou ")) texto = texto.replaceAll("ou", "|");
                        if(texto.contains(" nao ")) texto = texto.replaceAll("nao", "!");
                        texto = texto.replaceAll("!@@", "<=");
                        texto = texto.replaceAll("!@#", ">=");
                        texto = texto.replaceAll("!##", "!=");
                        texto = texto.replaceAll(" ", "");
                        for (String var: vars) // laço que substitui a variavel pelo seu respectivo valor
                        {
                            if(texto.contains(var)){ // verifica se a variavel está contida na arraylist
                                index = vars.indexOf(var); // se sim, pega o index dessa variavel
                                
                                //System.out.println("var int: " + varscorresp.get(index).);
                                if(texto.contains("!") && !texto.contains("!="))
                                {
                                    texto = texto.replaceAll("!", "");
                                    if(varscorresp.get(index).matches("0.0")) texto = texto.replaceAll(var, "1");
                                    else texto = texto.replaceAll(var, "0");
                                }
                                else texto = texto.replaceAll(var, varscorresp.get(index)); // e substitui pelo respectivo valor
                            }
                            else
                            {
                                System.out.println("var nao contem");
                                System.out.println(texto);
                                System.out.println(var);
                            }
                        }
                        
                        resultse = (Boolean) new GroovyShell().evaluate(texto); // retorna true ou false em relação à expressão booleana
                        System.out.println("apresentando a expressao dentro do se: " + texto + " :" + resultse);
                        if(resultse == false && !algtexto.get(counter+1).contains("[[")) counter++; // se a condição for falsa e for um SE simples, pula a linha de execução
                        else if(resultse == false && algtexto.get(counter+1).contains("[["))
                        {
                            while(true) // laço para pular todo o bloco do SE
                            {
                                if(algtexto.get(counter).contains("]]")) break;
                                counter++;
                            }
                        }
                    }
                    else if(algtexto.get(counter).contains("<-")) // atribuição
                    {
                        if(algtexto.get(counter).contains("'")) // receber caractere ou cadeia de caracteres
                        {
                            texto = algtexto.get(counter);
                            variavel = texto.substring(0, texto.indexOf("<-"));
                            variavel = variavel.replaceAll(" ", "");
                            indexresult = vars.indexOf(variavel);
                            variavel = texto.substring(texto.indexOf("'")+1, texto.lastIndexOf("'"));
                            varscorresp.set(indexresult, variavel);
                        }
                        else if(algtexto.get(counter).contains("verdadeiro")) // recebendo um valor booleano verdadeiro
                        {
                            texto = algtexto.get(counter).replaceAll("verdadeiro", "'1'");
                            variavel = texto.substring(0, texto.indexOf("<-"));
                            variavel = variavel.replaceAll(" ", "");
                            indexresult = vars.indexOf(variavel);
                            variavel = texto.substring(texto.indexOf("'")+1, texto.lastIndexOf("'"));
                            varscorresp.set(indexresult, variavel);
                        }
                        else if(algtexto.get(counter).contains("falso")) // recebendo um valor booleano falso
                        {
                            texto = algtexto.get(counter).replaceAll("falso", "'0'");
                            variavel = texto.substring(0, texto.indexOf("<-"));
                            variavel = variavel.replaceAll(" ", "");
                            indexresult = vars.indexOf(variavel);
                            variavel = texto.substring(texto.indexOf("'")+1, texto.lastIndexOf("'"));
                            varscorresp.set(indexresult, variavel);
                        }
                        else if(algtexto.get(counter).contains("raiz")) // calcula a raiz de um número
                        {
                            texto = algtexto.get(counter).replaceAll("raiz ", "");
                            variavel = texto.substring(0, texto.indexOf("<-"));
                            variavel = variavel.replaceAll(" ", "");
                            indexresult = vars.indexOf(variavel);
                            variavel = texto.substring(texto.indexOf("-")+1, texto.lastIndexOf(";"));
                            for (String var: vars) // laço que substitui a variavel pelo seu respectivo valor
                            {
                                if(variavel.contains(var)) // verifica se a variavel está contida na arraylist
                                    index = vars.indexOf(var); // se sim, pega o index dessa variavel
                                    variavel = variavel.replaceAll(var, varscorresp.get(index)); // e substitui pelo respectivo valor
                            }
                            // quando sai do laço, a variavel "variavel" possui a string expressão matematica que deverá ser calculada!
                            Expression exp = new Expression(variavel); // utilizando projeto jar da Towel, recebe uma string que contem uma expressao
                            double result = exp.resolve(); // metodo que calcula a expressao
                            double d = pow(result, 1.0 / 2.0);
                            variavel = String.valueOf(d);
                            varscorresp.set(indexresult, variavel);
                        }
                        else if(algtexto.get(counter).contains("quad")) // calculando o quadrado do número
                        {
                            texto = algtexto.get(counter).replaceAll("quad ", "");
                            variavel = texto.substring(0, texto.indexOf("<-"));
                            variavel = variavel.replaceAll(" ", "");
                            indexresult = vars.indexOf(variavel);
                            variavel = texto.substring(texto.indexOf("-")+1, texto.lastIndexOf(";"));
                            for (String var: vars) // laço que substitui a variavel pelo seu respectivo valor
                            {
                                if(variavel.contains(var)) // verifica se a variavel está contida na arraylist
                                    index = vars.indexOf(var); // se sim, pega o index dessa variavel
                                    variavel = variavel.replaceAll(var, varscorresp.get(index)); // e substitui pelo respectivo valor
                            }
                            // quando sai do laço, a variavel "variavel" possui a string expressão matematica que deverá ser calculada!
                            Expression exp = new Expression(variavel); // utilizando projeto jar da Towel, recebe uma string que contem uma expressao
                            double result = exp.resolve(); // metodo que calcula a expressao
                            double d = pow(result, 2.0);
                            variavel = String.valueOf(d);
                            varscorresp.set(indexresult, variavel);
                        }
                        else if(algtexto.get(counter).contains("trunc")) // pegando apenas a parte inteira de um numero ou expressão
                        {
                            texto = algtexto.get(counter).replaceAll("trunc ", "");
                            variavel = texto.substring(0, texto.indexOf("<-"));
                            variavel = variavel.replaceAll(" ", "");
                            indexresult = vars.indexOf(variavel);
                            variavel = texto.substring(texto.indexOf("-")+1, texto.lastIndexOf(";"));
                            for (String var: vars) // laço que substitui a variavel pelo seu respectivo valor
                            {
                                if(variavel.contains(var)) // verifica se a variavel está contida na arraylist
                                    index = vars.indexOf(var); // se sim, pega o index dessa variavel
                                    variavel = variavel.replaceAll(var, varscorresp.get(index)); // e substitui pelo respectivo valor
                            }
                            // quando sai do laço, a variavel "variavel" possui a string expressão matematica que deverá ser calculada!
                            Expression exp = new Expression(variavel); // utilizando projeto jar da Towel, recebe uma string que contem uma expressao
                            double result = exp.resolve(); // metodo que calcula a expressao
                            variavel = String.valueOf((int)result);
                            varscorresp.set(indexresult, variavel);
                        }
                        else if(algtexto.get(counter).contains("abs")) // pegando apenas a parte absoluta de um numero ou expressão
                        {
                            texto = algtexto.get(counter).replaceAll("abs ", "");
                            variavel = texto.substring(0, texto.indexOf("<-"));
                            variavel = variavel.replaceAll(" ", "");
                            indexresult = vars.indexOf(variavel);
                            variavel = texto.substring(texto.indexOf("-")+1, texto.lastIndexOf(";"));
                            for (String var: vars) // laço que substitui a variavel pelo seu respectivo valor
                            {
                                if(variavel.contains(var)) // verifica se a variavel está contida na arraylist
                                    index = vars.indexOf(var); // se sim, pega o index dessa variavel
                                    variavel = variavel.replaceAll(var, varscorresp.get(index)); // e substitui pelo respectivo valor
                            }
                            // quando sai do laço, a variavel "variavel" possui a string expressão matematica que deverá ser calculada!
                            Expression exp = new Expression(variavel); // utilizando projeto jar da Towel, recebe uma string que contem uma expressao
                            double result = exp.resolve(); // metodo que calcula a expressao
                            if(result<0) variavel = String.valueOf(result*(-1));
                            else variavel = String.valueOf(result);
                            varscorresp.set(indexresult, variavel);
                        }
                        else if(algtexto.get(counter).contains("arred")) // arredondando um numero ou expressão
                        {
                            texto = algtexto.get(counter).replaceAll("arred ", "");
                            variavel = texto.substring(0, texto.indexOf("<-"));
                            variavel = variavel.replaceAll(" ", "");
                            indexresult = vars.indexOf(variavel);
                            variavel = texto.substring(texto.indexOf("-")+1, texto.lastIndexOf(";"));
                            for (String var: vars) // laço que substitui a variavel pelo seu respectivo valor
                            {
                                if(variavel.contains(var)) // verifica se a variavel está contida na arraylist
                                    index = vars.indexOf(var); // se sim, pega o index dessa variavel
                                    variavel = variavel.replaceAll(var, varscorresp.get(index)); // e substitui pelo respectivo valor
                            }
                            // quando sai do laço, a variavel "variavel" possui a string expressão matematica que deverá ser calculada!
                            Expression exp = new Expression(variavel); // utilizando projeto jar da Towel, recebe uma string que contem uma expressao
                            double result = exp.resolve(); // metodo que calcula a expressao
                            variavel = String.valueOf(round(result));
                            varscorresp.set(indexresult, variavel);
                        }
                        else // em ultimo caso, é apenas uma expressão aritmetica
                        {
                            texto = algtexto.get(counter).replaceAll(" ", ""); // retirando os espaços em branco
                            texto = texto.replaceAll(",", ".");
                            texto = texto.replaceAll("#", "/");
                            variavel = texto.substring(0, texto.indexOf("<-"));
                            indexresult = vars.indexOf(variavel);
                            // PEGAR O RESULTADO DA OPERACAO ARITMETICA
                            variavel = texto.substring(texto.indexOf("<-")+2, texto.indexOf(";"));
                            for (String var: vars) // laço que substitui a variavel pelo seu respectivo valor
                            {
                                if(variavel.contains(var)) // verifica se a variavel está contida na arraylist
                                    index = vars.indexOf(var); // se sim, pega o index dessa variavel
                                    variavel = variavel.replaceAll(var, varscorresp.get(index)); // e substitui pelo respectivo valor
                            }
                            // quando sai do laço, a variavel "variavel" possui a string expressão matematica que deverá ser calculada!
                            System.out.println("var "+variavel);
                            Expression exp = new Expression(variavel); // utilizando projeto jar da Towel, recebe uma string que contem uma expressao
                            double result = exp.resolve(); // metodo que calcula a expressao
                            resultado = String.valueOf(result);
                            varscorresp.set(indexresult, resultado); // substitui o valor calculado na variavel correspondente ao index
                            if(repeticoescpx > 1 && indexresult == index_var_para) de = String.valueOf(Math.round(result)); // se a atribuição está dentro de um laço para
                        }
                    }
                    else if(algtexto.get(counter).contains("]]; ate"))
                    {
                        texto = algtexto.get(counter).replaceAll("]]; ate ", "");
                        texto = texto.replaceAll(" ", "");
                        texto = texto.replaceAll(";", "");
                        texto = texto.replaceAll("=", "==");
                        for (String var: vars) // laço que substitui a variavel pelo seu respectivo valor
                        {
                            if(texto.contains(var)) // verifica se a variavel está contida na arraylist
                            {
                                index = vars.indexOf(var); // se sim, pega o index dessa variavel
                            }
                            else
                            {
                                System.out.println("var nao contem");
                                System.out.println(texto);
                                System.out.println(var);
                            }
                            if(varscorresp.get(index) == null) texto = texto.replaceAll(var, "0"); // e substitui pelo respectivo valor
                            else texto = texto.replaceAll(var, varscorresp.get(index)); // e substitui pelo respectivo valor
                        }
                        System.out.println("texto depois: "+texto);
                        repita = (Boolean) new GroovyShell().evaluate(texto); // retorna true ou false em relação à expressão booleana
                        System.out.println("apresentando a expressao dentro do enquanto: " + texto + " :" + repita); // se repita for false, então o laço deve ser repetido até que seja true.
                        if(repita==false) counter = counterrepita;
                        
                    }
                    else if(algtexto.get(counter).contains("]];"))
                    {
                        if(resultenquanto == true)
                        {
                            if(resultse == false)
                            {
                                colchetes--;
                                System.out.println("colchete: "+colchetes);
                                System.out.println("index colchete: "+index_colchete.get(colchetes));
                                System.out.println("resultenquanto: "+resultenquanto);
                                System.out.println("bloco: "+bloco);
                                if(repeticoesenquanto > 1 && resultenquanto == true)
                                { 
                                    fimenquanto = counter;
                                    System.out.println("enquanto true, linha da vez nao eh enquanto, bloco true");
                                        //counter = counterenquanto-1;
                                    counter = index_colchete.get(colchetes)-2;
                                }
                                else if(resultenquanto == true && bloco == true)
                                {
                                    System.out.println("enquanto true, linha da vez nao eh enquanto, bloco true (elseif)");
                                    counter = index_colchete.get(colchetes)-2;
                                }
                            }
                            else resultse = false;
                        }
                    }
                    
                    if(repeticoespara > 1 && !algtexto.get(counter).contains("para"))
                    { 
                        incremento = new Integer(de) + new Integer(passo);
                        de = String.valueOf(incremento);
                        varscorresp.set(indexpara, de);
                        counter--;
                        repeticoespara--;
                    }
                    
                    if(repeticoesenquanto > 1 && !algtexto.get(counter).contains("enquanto") && resultenquanto == true && bloco == false) // loop para um enquanto sem bloco de execução (so funciona para uma linha de execução, pois ele executa a proxima linha do enquanto e retorna para o enquanto. ARRUMAR)
                    { 
                        counter-=2;
                    }
                }
                System.out.println("apresentando o valor armazenado nas variaveis: ");
                for(int counter = 0; counter < varscorresp.size(); counter++) System.out.println(counter + ":" + varscorresp.get(counter));
                if(ERROS == 0) p.texto.append("\n\n PROGRAMA EXECUTADO COM SUCESSO!");
                else p.texto.append("\n\n PROGRAMA EXECUTADO COM ERROS: " + ERROS);
                SAIR++; // ja pode fechar a frame cmd
            } catch (InterruptedException e) {}
        }
    } 
}
