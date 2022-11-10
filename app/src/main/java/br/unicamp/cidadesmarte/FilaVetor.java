package br.unicamp.cidadesmarte;

import androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback;

import java.util.ArrayList;
import java.util.List;

public class FilaVetor<Tipo> implements IQueue<Tipo> {
    public static int MAXIMO = 500;   // tamanho default do vetor F
    private int posicoes;     // tamanho dado pela aplicação
    private ArrayList<Tipo> F; // vetor de objetos genéricos, com tamanho genérico,
    // usado como área de armazenamento
    private int inicio = 0, // índice do início da fila
            fim = 0;    // índice do fim da fila

    public FilaVetor()// construtor que utiliza o default MAXIMO
    {
        posicoes = 500;         // armazena o tamanho físico do vetor
        F = new ArrayList<Tipo>(posicoes);
    }

    public FilaVetor(int posic) // construtor polimórfico
    {
        posicoes = posic;         // armazena o tamanho físico do vetor
        F = new ArrayList<Tipo>(posicoes);   // F é um vetor de Tipo; cria um
    }

    @Override
    public void enfileirar(Tipo elemento) throws Exception {
        if (getTamanho() == posicoes - 1)
            throw new FilaCheiaException("Fila cheia (overflow)");
        F.set(fim, elemento);                 // inclui elemento na primeira posição livre
        fim = (fim + 1) % posicoes; // calcula próxima posição livre
    }

    @Override
    public Tipo retirar() throws Exception {
        Tipo o;
        if (getEstaVazia())
            throw new FilaVaziaException("Underflow da fila");
        o = F.get(inicio);
        F.set(inicio, null);      // libera memória
        inicio = (inicio + 1) % posicoes;   // calcula novo inicio da fila
        return o;                        	// devolve elemento inicial
    }

    @Override
    public Tipo inicio() throws Exception{
        if (getEstaVazia())
            throw new FilaVaziaException("Esvaziamento (underflow) da fila");
        Tipo o = F.get(inicio);     // devolve o objeto do início da fila
        return o; 				// sem retirá-lo da fila
    }

    @Override
    public Tipo fim() throws Exception{
        Tipo o;
        if (getEstaVazia())
            throw new FilaVaziaException("Underflow da fila");
        if (fim == 0)
            o = F.get(posicoes - 1);    // devolve o objeto do final da fila
        else          // sem retirá-lo da fila
            o = F.get(fim - 1);
        return o;
    }

    @Override
    public boolean getEstaVazia() {
        return (inicio == fim);
    }

    @Override
    public int getTamanho() {
        return (posicoes - inicio + fim) % posicoes;
    }

    @Override
    public List<Tipo> Lista() {
        return null;
    }
}
