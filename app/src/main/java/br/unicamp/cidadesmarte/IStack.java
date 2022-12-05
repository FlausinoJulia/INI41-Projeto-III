package br.unicamp.cidadesmarte;

/*
    Danyelle Nogueira França 21232
    Julia Flausino da Silva  21241
*/

public interface IStack<Dado> {
    void empilhar(Dado dado) throws Exception;   // empilha o objeto "dado"
    Dado desempilhar() throws Exception;         // remove e retorna o objeto do topo
    Dado oTopo() throws Exception;               // retorna o objeto do topo sem removê-lo
    public int getTamanho();                     // informa a quantidade de itens empilhados
    boolean getEstaVazia();                      // informa se a pilha está ou não vazia
}
