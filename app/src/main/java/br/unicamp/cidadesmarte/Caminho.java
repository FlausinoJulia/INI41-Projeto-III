package br.unicamp.cidadesmarte;

public class Caminho implements Comparable<Caminho>
{
    private String cidadeDeOrigem, cidadeDeDestino;
    private int distanciaCaminho, tempoCaminho, custoCaminho;

    public Caminho() // construtor default (sem parametros)
    {
        this.cidadeDeOrigem  = "";
        this.cidadeDeDestino = "";
        this.distanciaCaminho = 0;
        this.tempoCaminho = 0;
        this.custoCaminho = 0;
    }

    public Caminho(String origem, String destino, int dist, int temp, int custo) // construtor parametrizado
    {
        this.cidadeDeOrigem = origem;
        this.cidadeDeDestino = destino;
        this.distanciaCaminho = dist;
        this.tempoCaminho = temp;
        this.custoCaminho = custo;
    }

    public Caminho(String origem, String destino) // construtor que recebe só a chave (nome) como parâmetro
    {
        this.cidadeDeOrigem  = origem;
        this.cidadeDeDestino = destino;
    }

    public String getCidadeDeOrigem()
    {
        return this.cidadeDeOrigem;
    }

    public void setCidadeDeOrigem(String origem) {
        this.cidadeDeOrigem = origem;
    }

    public String getCidadeDeDestino()
    {
        return this.cidadeDeDestino;
    }

    public void setCidadeDeDestino(String destino)
    {
        this.cidadeDeDestino = destino;
    }

    public int getDistancia()
    {
        return this.distanciaCaminho;
    }

    public void setDistancia(int distancia)
    {
        this.distanciaCaminho = distancia;
    }

    public int getTempo()
    {
        return this.tempoCaminho;
    }

    public void setTempo(int tempo)
    {
        this.tempoCaminho = tempo;
    }

    public int getCusto() {
        return this.custoCaminho;
    }

    public void setCusto (int custo)
    {
        this.custoCaminho = custo;
    }

    @Override
    public int compareTo(Caminho outroCaminho)
    {
        return this.toString().compareTo(outroCaminho.toString());
    }

    @Override
    public String toString()
    {
        return cidadeDeOrigem + cidadeDeDestino + distanciaCaminho + tempoCaminho + custoCaminho;
    }
}
