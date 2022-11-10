package br.unicamp.cidadesmarte;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

public class PilhaVetor<Dado> {
    private ArrayList<Dado> p;
    int topo;

    public PilhaVetor(int maximo){
        p = new ArrayList<Dado>(maximo);
        topo = -1;
    }

    private PilhaVetor() {
        p = new ArrayList<Dado>(500);
        topo = -1;
    }

    public int Tamanho = topo + 1;

    public boolean EstaVazia = topo < 0;

    public void Empilhar(Dado dado) throws Exception {
        if (Tamanho == p.size())
            throw new Exception("Pilha cheia (Stack Overflow)!");

        topo = topo + 1;    // ou apenas
        p.set(topo, dado);     // p[++topo] = dado;
    }

    public Dado Desempilhar() throws Exception {
        if (EstaVazia)
            throw new Exception("Pilha vazia (Stack Underflow)!");
        Dado dadoEmpilhado = p.get(topo); // ou
        topo = topo - 1;              // Dado dadoEmpilhado = p[topo--];
        return dadoEmpilhado;
    }
}
