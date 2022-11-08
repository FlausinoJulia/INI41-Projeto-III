package br.unicamp.cidadesmarte;

import android.content.Context;
import android.widget.Toast;

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

    public void novoVertice(String rotulo)
    {
        //  adicionamos o novo vertice no vetor de vertices
        vertices[numVerts] = new Vertice(rotulo);
        numVerts++;
    }

    // criar uma aresta indicando apenas que há ligação entre um vértice e outro
    public void novaAresta(int origem, int destino)
    {
        matriz[origem][destino] = 1; // indicamos que tem “ligacao” entre o vertice inicio e o vertice fim
    }

    // criar uma aresta indicando qual o peso de ir da origem até o destino
    public void NovaAresta(int origem, int destino, int peso)
    {
        matriz[origem][destino] = peso;
    }

    public void ExibirVertice(int v, Context context)
    {
        String texto = vertices[v].getRotulo();
        Toast.makeText(context, texto, Toast.LENGTH_SHORT).show();
    }
}
