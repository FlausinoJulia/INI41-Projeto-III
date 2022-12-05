package br.unicamp.cidadesmarte;

/*
    Danyelle Nogueira França 21232
    Julia Flausino da Silva  21241
*/

public class Cidade implements Comparable<Cidade>
{
    private String nomeCidade;
    private double coordenadaX, coordenadaY;


    public Cidade (String nome, double x, double y)
    {
        this.nomeCidade = nome;
        this.coordenadaX = x;
        this.coordenadaY = y;
    }

    public Cidade (String nome)
    {
        this.nomeCidade = nome;
        this.coordenadaX = 0;
        this.coordenadaY = 0;
    }

    public String getNome()
    {
        return this.nomeCidade;
    }

    public void setNome(String nome)
    {
        this.nomeCidade = nome;
    }

    public double getX()
    {
        return this.coordenadaX;
    }

    public void setX(double x)
    {
        this.coordenadaX = x;
    }

    public double getY()
    {
        return this.coordenadaY;
    }

    public void setY(double y)
    {
        this.coordenadaY = y;
    }

    @Override
    public int compareTo(Cidade o) {
        return nomeCidade.compareTo(o.getNome());
    }

    @Override
    public String toString()
    {
        return this.nomeCidade;
    }
}
