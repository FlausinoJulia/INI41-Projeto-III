package br.unicamp.cidadesmarte;

import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class Grafo
{
    private final int NUM_VERTICES = 100;
    private Vertice[] vertices;
    private int[][] matriz;
    int numVerts;

    // DIJKSTRA
    DistOriginal[] percurso; // vetor que guarda os caminhos que vão sendo encontrados
    int infinito = Integer.MAX_VALUE;
    int verticeAtual;     // global que indica o vértice atualmente sendo visitado
    long doInicioAteAtual; // global usada para ajustar menor caminho com Djikstra
    int nTree;

    public Grafo()
    {
        vertices = new Vertice[NUM_VERTICES];
        matriz = new int[NUM_VERTICES][NUM_VERTICES];
        numVerts = 0;
        nTree = 0;
        for (int i = 0; i < NUM_VERTICES; i++)
            for (int j = 0; j < NUM_VERTICES; j++)
                matriz[i][j] = infinito;

        percurso = new DistOriginal[NUM_VERTICES];
    }

    // adiciona um novo vértice, com o rótulo passado por parametro, no vetor de vertices
    public void novoVertice(String rotulo)
    {
        vertices[numVerts] = new Vertice(rotulo);
        numVerts++;
    }

    // criar uma aresta indicando apenas que há ligação entre um vértice e outro
    public void novaAresta(int origem, int destino)
    {
        matriz[origem][destino] = 1; // indicamos que tem “ligacao” entre origem e destino
    }

    // criar uma aresta indicando qual o peso de ir da origem até o destino
    public void novaAresta(int origem, int destino, int peso)
    {
        matriz[origem][destino] = peso;
    }

    public void exibirVertice(int v, TextView tv)
    {
        String texto = vertices[v].getRotulo();
        tv.setText(texto);
    }

    // retorna o primeiro vértice sem sucessores da matriz
    public int semSucessores()
    {
        boolean temAresta;

        // para cada linha da matriz
        for (int linha = 0; linha < numVerts; linha++)
        {
            temAresta = false;

            // percorremos cada coluna dessa linha
            for (int coluna = 0; coluna < numVerts; coluna++)
                // vemos se tem uma ligação entre o vértice da linha e o vértice da coluna
                if (matriz[linha][coluna] != infinito)
                {
                    temAresta = true; // se tem, indicamos que essa linha possui sucesores
                    break;            // paramos de percorrer a linha, porque achamos um sucessor nesse vértice,
                                      // então não temos mais que verificar o resto das colunas
                }

            // se a linha que acabamos de percorrer não tem arestas
            if (!temAresta)
                return linha; // retornamos o indice da linha desse vértice (que não possui sucessores)
        }

        // não achamos nenhum vértice sem sucessor, retornamos -1
        return -1;
    }

    // remove um vértice do grafo
    public void removerVertice(int v)
    {
        if (v != numVerts - 1)
        {
            // movendo todos os vertices à direita do vertice a ser removido para a esquerda
            for (int j = v; j < numVerts - 1; j++)
                vertices[j] = vertices[j+1];

            // removendo vértice da matriz
            for (int linha = v; v < numVerts; linha++)
                moverLinhas(linha, numVerts - 1);
            for (int coluna = v; v < numVerts; coluna++)
                moverColunas(coluna, numVerts - 1);
        }

        numVerts--; // removemos um vértice
    }

    // metodo auxiliar para remover vertice
    private void moverLinhas(int linha, int tamanho)
    {
        if (linha != numVerts - 1)
            for (int coluna = 0; coluna < tamanho; coluna++)
                matriz[linha][coluna] = matriz[linha+1][coluna];
    }

    // metodo auxiliar para remover vertice
    private void moverColunas(int coluna, int tamanho)
    {
        if (coluna != numVerts - 1)
            for (int linha = 0; linha < tamanho; linha++)
                matriz[linha][coluna] = matriz[linha][coluna++];
    }

    public String ordenacaoTopologica()
    {
        PilhaVetor<String> gPilha = new PilhaVetor<String>();
        int origVerts = numVerts;
        while (numVerts > 0)
        {
            int currVertex = semSucessores();
            if (currVertex == -1)
                return "Erro: grafo possui ciclos";
            try
            {
                gPilha.empilhar(vertices[currVertex].getRotulo());
            }
            catch (Exception overflowPilha)
            {
                Log.wtf("ErroClasseGrafo", "Overflow da pilha na ordenação topológica");
            }


            removerVertice(currVertex);
        }

        String resultado = "Sequencia da Ordenacao Topologica: ";

        while(gPilha.getTamanho() > 0)
            try {
                resultado += gPilha.desempilhar() + " ";
            }
            catch (Exception underflowPilha)
            {
                Log.wtf("ErroClasseGrafo", "Underflow da pilha na ordenação topológica");
            }

        return resultado;
    }

    // PERCURSO EM PROFUNDIDADE //
    // retorna a posicao do primeiro vértice adjacente não visitado do vértice v
    private int obterVerticeAdjacenteNaoVisitado(int v)
    {
        // percorremos a linha do vértice v
        for (int j = 0; j < numVerts; j++)
            if ((matriz[v][j] != infinito) && !vertices[j].isFoiVisitado())
                return j;

        return -1;
    }

    // percorre todo o grafo, partindo do primeiro vertice
    public void percursoEmProfundidade(TextView tv)
    {
        tv.setText("");
        Stack<Integer> gPilha = new Stack<Integer>();

        // limpando o "foi visitado" de todos os vertices
        limparFoiVisitado();

        vertices[0].setFoiVisitado(true);
        exibirVertice(0, tv);
        gPilha.push(0);

        int v;
        while (gPilha.size() > 0)
        {
            v = obterVerticeAdjacenteNaoVisitado(gPilha.peek());
            if (v == -1)
                gPilha.pop();
            else
            {
                vertices[v].setFoiVisitado(true);
                exibirVertice(v, tv);
                gPilha.push(v);
            }
        }

        // limpando o "foi visitado" de todos os vertices
        for (int i = 0; i <= numVerts - 1; i++)
            vertices[i].setFoiVisitado(false);
    }

    public void processarNo(int i)
    {
        String rotulo = vertices[i].getRotulo();
        Log.i("rotulo", rotulo);
    }

    public void percursoEmProfundidadeRec(int part)
    {
        int i;
        processarNo(part);
        vertices[part].setFoiVisitado(true);
        for (i = 0; i < numVerts; ++i)
            if (matriz[part][i] == 1 && !vertices[i].isFoiVisitado())
                percursoEmProfundidadeRec(i);
    }

    // PERCURSO EM LARGURA //
    public void percursoPorLargura(TextView tv)
    {
        tv.setText("");
        Queue<Integer> gQueue = null;
        vertices[0].setFoiVisitado(true);
        exibirVertice(0, tv);
        gQueue.add(0); // vai adicionar no null?
        int vert1, vert2;
        while (gQueue.size() > 0 )
        {
            vert1 = gQueue.remove();
            vert2 = obterVerticeAdjacenteNaoVisitado(vert1);
            while (vert2 != -1)
            {
                vertices[vert2].setFoiVisitado(true);
                exibirVertice(vert2, tv);
                gQueue.add(vert2);
                vert2 = obterVerticeAdjacenteNaoVisitado(vert1);
            }
        }
        for (int i = 0; i < numVerts; i++)
            vertices[i].setFoiVisitado(false);
    }

    // ARVORE GERADORA MINIMA //
    public void arvoreGeradoraMinima(int primeiro, TextView tv)
    {
        tv.setText("");
        Stack<Integer> gPilha = new Stack<Integer>();
        vertices[primeiro].setFoiVisitado(true);
        gPilha.push(primeiro);
        int currVertex, ver;
        while (gPilha.size() > 0)
        {
            currVertex = gPilha.peek();
            ver = obterVerticeAdjacenteNaoVisitado(currVertex);
            if (ver == -1)
                gPilha.pop();
            else
            {
                vertices[ver].setFoiVisitado(true);
                gPilha.push(ver);
                exibirVertice(currVertex, tv);
                String texto = tv.getText().toString() + "--> ";
                tv.setText(texto);
                exibirVertice(ver, tv);
                String texto2 = tv.getText().toString() + "  ";
                tv.setText(texto2);
            }
        }

        limparFoiVisitado();
    }

    // DIJKSTRA //
    public String menorCaminho (int inicioDoPercurso, int finalDoPercurso)
    {
        // se saimos de um vértice para ir para ele mesmo, não há caminho
        if (inicioDoPercurso == finalDoPercurso)
            return "Não há caminho.";
        else
        {
            // deixamos todos os vértices como não visitados
            limparFoiVisitado();

            // visitamos o vértice de origem do percurso
            vertices[inicioDoPercurso].setFoiVisitado(true);
            // percorremos cada coluna na linha do vértice de origem do percurso
            for (int j = 0; j < numVerts; j++)
            {
                // guardamos o peso do movimento da origem para cada vertice do grafo no vetor percurso
                int tempDist = matriz[inicioDoPercurso][j];
                percurso[j] = new DistOriginal(inicioDoPercurso, tempDist);
            }

            // percorre cada vértice
            for (int nTree = 0; nTree < numVerts; nTree++)
            {
                int indiceDoMenor = obterMenor();
                long distanciaMinima = percurso[indiceDoMenor].getDistancia();

                verticeAtual = indiceDoMenor;
                doInicioAteAtual = percurso[indiceDoMenor].getDistancia();

                vertices[verticeAtual].setFoiVisitado(true);
                ajustarMenorCaminho();
            }

            return exibirPercursos(inicioDoPercurso, finalDoPercurso);
        }
    }

    public int obterMenor()
    {
        long distanciaMinima = infinito;
        int indiceDaMinima = 0;
        for (int j = 0; j < numVerts; j++)
        {
            if (!(vertices[j].isFoiVisitado()) && (percurso[j].getDistancia() < distanciaMinima))
            {
                distanciaMinima = percurso[j].getDistancia();
                indiceDaMinima = j;
            }
        }

        return indiceDaMinima;
    }

    public void ajustarMenorCaminho()
    {
        for (int coluna = 0; coluna < numVerts; coluna++)
        {
            if (!vertices[coluna].isFoiVisitado())
            {
                int atualAteMargem = matriz[verticeAtual][coluna];
                long doInicioAteMargem = doInicioAteAtual + atualAteMargem;
                long distanciaDoCaminho = percurso[coluna].getDistancia();

                if (doInicioAteMargem < distanciaDoCaminho)
                {
                    percurso[coluna].setVerticePai(verticeAtual);
                    percurso[coluna].setDistancia(doInicioAteMargem);
                }
            }
        }
    }


    public String exibirPercursos(int inicioDoPercurso, int finalDoPercurso)
    {
        String resultado = "";
        for (int j = 0; j < numVerts; j++)
        {
            resultado += vertices[j].getRotulo() + "=";
            if (percurso[j].getDistancia() == infinito)
                resultado += "inf";
            else
                resultado += percurso[j].getDistancia()+" ";
            String pai = vertices[percurso[j].getVerticePai()].getRotulo();
            resultado += "(" + pai + ") ";
        }

        int onde = finalDoPercurso;
        Stack<String> pilha = new Stack<String>();
        int cont = 0;
        while (onde != inicioDoPercurso)
        {
            onde = percurso[onde].getVerticePai();
            pilha.push(vertices[onde].getRotulo());
            cont++;
        }
        resultado = "";
        while (pilha.size() != 0)
        {
            resultado += pilha.pop();
            if (pilha.size() != 0)
                resultado += " --> ";
        }
        if ((cont == 1) && (percurso[finalDoPercurso].getDistancia() == infinito))
            resultado = "Não há caminho.";
        else
            resultado += " --> " + vertices[finalDoPercurso].getRotulo();

        return resultado;
    }

    public boolean verificouTodosOsVertices()
    {
        for (Vertice vertice : vertices) {
            if (!vertice.isFoiVisitado())
                return false;
        }

        return true;
    }


    public List<String> acharTodosOsCaminhosRec (int origem, int destino) throws Exception
    {
        // verificamos se os indices de origem e de destino são válidos
        if (destino >= numVerts || destino < 0)
            throw new Exception("O vértice de destino não existe no grafo");

        if (origem >= numVerts || origem < 0)
            throw new Exception("O vértice de origem não existe no grafo");

        // criamos uma lista de caminhos e uma string caminho
        List<String> caminhos = new ArrayList<String>();
        String caminho = "";

        // se o vértice de origem é o mesmo de destino, não temos caminhos
        if (origem == destino)
        {
            caminho = "Vértice de origem é igual ao vértice de destino.";
            caminhos.add(caminho);
        }
        else
        {
            // setamos todos os vértices como não visitados
            limparFoiVisitado();

            // chamada do método recursivo para achar todos os caminhos
            acharTodosOsCaminhosRec(origem, destino, caminhos, caminho);
        }

        // retornamos a lista de caminhos
        return caminhos;
    }

    // BACKTRACKING //
    private void acharTodosOsCaminhosRec(int atual, int destino, List<String> caminhos, String caminho)
    {
        // setamos o vertice atual como visitado e adicionamos ele no caminho
        vertices[atual].setFoiVisitado(true);
        if (caminho.equals(""))
        {
            caminho += vertices[atual].getRotulo();
        }
        else
            caminho += " --> " + vertices[atual].getRotulo();

        // se chegamos ao destino
        if (atual == destino)
        {
            caminhos.add(caminho); // adicionamos o caminho encontrado na lista de caminhos
            caminho =  "";         // limpamos a string caminho para formar um novo caminho
        }
        else
        {
            // percorre cada vértice partindo do vértice atual
            for (int i = 0; i < numVerts; i++)
            {
                // se encontro um vértice que ainda não foi visitado e que tem caminho
                if (!vertices[i].isFoiVisitado() && matriz[atual][i] != infinito)
                {
                    // vou para o vértice ainda não visitado
                    acharTodosOsCaminhosRec(i, destino, caminhos, caminho);
                    // depois que já vi todos os caminhos possíveis nessa saída, marco ela como não visitada
                    vertices[i].setFoiVisitado(false);
                }
            }
        }
    }

    // método que seta todos os vértices como não visitados
    private void limparFoiVisitado()
    {
        for (int j = 0; j < numVerts; j++)
            vertices[j].setFoiVisitado(false);
    }
}
