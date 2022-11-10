package br.unicamp.cidadesmarte;

public interface IStack<Dado> {
    void empilhar(Dado dado);   // empilha o objeto "dado"
    Dado desempilhar();         // remove e retorna o objeto do topo
    Dado oTopo();               // retorna o objeto do topo sem removê-lo
    public int getTamanho();    // informa a quantidade de itens empilhados
    boolean getEstaVazia();     // informa se a pilha está ou não vazia
}
