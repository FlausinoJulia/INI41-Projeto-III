package br.unicamp.cidadesmarte;

import androidx.annotation.NonNull;

public class Cidade implements Comparable<Cidade>
{
    private String nome;
    private double x, y;

    public Cidade (String nome, double x, double y)
    {
        this.nome = nome;
        this.x = x;
        this.y = y;
    }

    public String getNome()
    {
        return this.nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public double getX()
    {
        return this.x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return this.y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    @Override
    public int compareTo(Cidade o) {
        return nome.compareTo(o.getNome());
    }

    @NonNull
    @Override
    public String toString()
    {
        return this.nome;
    }
}
