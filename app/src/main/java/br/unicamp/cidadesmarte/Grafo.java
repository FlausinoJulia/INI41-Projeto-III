package br.unicamp.cidadesmarte;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Queue;
import java.util.Stack;

public class Grafo
{
    private final int NUM_VERTICES = 20;
    private Vertice[] vertices;
    private int[][] matriz;
    int numVerts;
    // DataGridView dgv; // aqui seria algum componente da activity?

    public Grafo()
    {
        // this.dgv = dgv; // dgv seria passado como parametro

        vertices = new Vertice[NUM_VERTICES];
        matriz = new int[NUM_VERTICES][NUM_VERTICES];
        numVerts = 0;
        for (int i = 0; i < NUM_VERTICES; i++)
            for (int j = 0; j < NUM_VERTICES; j++)
                matriz[i][j] = 0;
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
                if (matriz[linha][coluna] > 0)
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
        Stack<String> gPilha = new Stack<String>();

        int origVerts = numVerts;
        while (numVerts > 0)
        {
            int currVertex = semSucessores();
            if (currVertex == -1)
                return "Erro: grafo possui ciclos";
            gPilha.push(vertices[currVertex].getRotulo());
            removerVertice(currVertex);
        }
        String resultado = "Sequencia da Ordenacao Topologica: ";
        while(gPilha.size() > 0)
            resultado += gPilha.pop() + " ";

        return resultado;
    }

    // PERCURSO EM PROFUNDIDADE //

    private int obterVerticeAdjacenteNaoVisitado(int v)
    {
        for (int j = 0; j < numVerts; j++)
            if ((matriz[v][j] != 0) && !vertices[j].isFoiVisitado())
                return j;

        return -1;
    }

    public void percursoEmProfundidade(TextView tv)
    {
        tv.setText("");
        Stack<Integer> gPilha = new Stack<Integer>();
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

        for (int j = 0; j <= numVerts - 1; j++)
            vertices[j].setFoiVisitado(false);
    }

    /*
    // percurso em profundidade

    private int ObterVerticeAdjacenteNaoVisitado(int v)
    {
      for (int j = 0; j < numVerts; j++)
        if ((adjMatrix[v, j] != 0) && (!vertices[j].FoiVisitado))
          return j;
      return -1;
    }

    public void PercursoEmProfundidade(TextBox txt)
    {
      txt.Clear();
      PilhaVetor<int> gPilha = new PilhaVetor<int>(); // para guardar a sequência de vértices
      vertices[0].FoiVisitado = true;
      gPilha.Empilhar(0);
      ExibirVertice(0, txt);
      int v;
      while (!gPilha.EstaVazia)
      {
        v = ObterVerticeAdjacenteNaoVisitado(gPilha.OTopo());
        if (v == -1)
          gPilha.Desempilhar();
        else
        {
          vertices[v].FoiVisitado = true;
          ExibirVertice(v, txt);
          gPilha.Empilhar(v);
        }
      }

      // limpa rastreio de visitas npara novos percursos não
      // ficarem poluídos com o percurso anterior
      for (int j = 0; j <= numVerts - 1; j++)
        vertices[j].FoiVisitado = false;
    }
    */

    // PERCURSO EM LARGURA //

    public void percursoPorLargura(TextView tv)
    {
        tv.setText("");
        Queue<Integer> gQueue = null;
        vertices[0].setFoiVisitado(true);
        exibirVertice(0, tv);
        gQueue.add(0);
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

}
