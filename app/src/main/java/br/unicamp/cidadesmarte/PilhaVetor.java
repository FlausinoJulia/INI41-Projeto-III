package br.unicamp.cidadesmarte;

import java.util.ArrayList;
import java.util.List;

/*
    Danyelle Nogueira França 21232
    Julia Flausino da Silva  21241
*/

public class PilhaVetor<Dado> implements IStack<Dado>
{
    private Dado[] p;
    int topo;

    public PilhaVetor(int maximo){
        this.p = (Dado[]) new Object[maximo];
        topo = -1;
    }

    public PilhaVetor() {
        p = (Dado[]) new Object[500];
        topo = -1;
    }

    @Override
    public Dado oTopo() throws Exception {
        if (getEstaVazia())
            throw new Exception("Underflow - pilha vazia!");

        Dado dadoEmpilhado = p[topo];

        return dadoEmpilhado;
    }

    @Override
    public int getTamanho() {
        return topo + 1;
    }

    @Override
    public boolean getEstaVazia() {
        return topo < 0;
    }

    public void empilhar(Dado dado) throws Exception {
        if (getTamanho() == p.length)
            throw new Exception("Pilha cheia (Stack Overflow)!");

        topo = topo + 1;
        p[topo] = dado;
    }

    public Dado desempilhar() throws Exception {
        if (getEstaVazia())
            throw new Exception("Underflo - pilha vazia!");

        Dado dadoEmpilhado = p[topo];
        topo = topo - 1;
        return dadoEmpilhado;
    }

    public List<Dado> listarDadosDaPilha()
    {
        List<Dado> lista = new ArrayList<>();

        for (int i = 0; i <= topo; i++)
            lista.add(p[i]);

        return lista;
    }
}
