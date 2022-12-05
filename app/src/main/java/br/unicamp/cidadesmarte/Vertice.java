package br.unicamp.cidadesmarte;

/*
    Danyelle Nogueira França 21232
    Julia Flausino da Silva  21241
*/

public class Vertice
{
    private boolean foiVisitado, estaAtivo;
    private String  rotulo; // identificador

    public Vertice (String rotulo)
    {
        this.rotulo = rotulo;
        this.foiVisitado = false;
        this.estaAtivo = true;
    }

    public boolean isFoiVisitado() {
        return foiVisitado;
    }

    public void setFoiVisitado(boolean foiVisitado) {
        this.foiVisitado = foiVisitado;
    }

    public boolean isEstaAtivo() {
        return estaAtivo;
    }

    public void setEstaAtivo(boolean estaAtivo) {
        this.estaAtivo = estaAtivo;
    }

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

}
