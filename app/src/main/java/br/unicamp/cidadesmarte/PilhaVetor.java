package br.unicamp.cidadesmarte;

import java.util.ArrayList;

public class PilhaVetor<Dado> implements IStack<Dado>
{
    private ArrayList<Dado> p;
    int topo;

    public PilhaVetor(int maximo){
        p = new ArrayList<Dado>(maximo);
        topo = -1;
    }

    public PilhaVetor() {
        p = new ArrayList<Dado>(500);
        topo = -1;
    }

    public void empilhar(Dado dado) throws Exception {
        if (getTamanho() == p.size())
            throw new Exception("Pilha cheia (Stack Overflow)!");

        topo = topo + 1;    // ou apenas
        p.set(topo, dado);     // p[++topo] = dado;
    }

    public Dado desempilhar() throws Exception {
        if (getEstaVazia())
            throw new Exception("Pilha vazia (Stack Underflow)!");
        Dado dadoEmpilhado = p.get(topo); // ou
        topo = topo - 1;              // Dado dadoEmpilhado = p[topo--];
        return dadoEmpilhado;
    }

    @Override
    public Dado oTopo() {
        return p.get(topo);
    }

    @Override
    public int getTamanho() {
        return topo + 1;
    }

    @Override
    public boolean getEstaVazia() {
        return topo < 0;
    }
}
