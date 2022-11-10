package br.unicamp.cidadesmarte;

import java.util.List;

public interface IQueue<Dado> {
    void enfileirar(Dado dado) throws Exception;
    Dado retirar() throws Exception;
    Dado inicio() throws Exception;
    Dado fim() throws Exception;
    boolean getEstaVazia();
    int getTamanho();
    List<Dado> Lista();
}
