package br.unicamp.cidadesmarte;

public class DistOriginal // distancia desde o original
{
    private int distancia;
    private int verticePai;

    public DistOriginal(int vp, int d)
    {
        distancia = d;
        verticePai = vp;
    }

    public int getDistancia()
    {
        return distancia;
    }

    public void setDistancia (int distancia)
    {
        this.distancia = distancia;
    }

    public int getVerticePai()
    {
        return verticePai;
    }

    public void setVerticePai(int verticePai)
    {
        this.verticePai = verticePai;
    }
}
