package br.unicamp.cidadesmarte;

public class DistOriginal // distancia desde o original
{
    public long distancia;
    public int verticePai;

    public DistOriginal(int vp, int d)
    {
        distancia = d;
        verticePai = vp;
    }

    public long getDistancia()
    {
        return distancia;
    }

    public void setDistancia (long distancia)
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
