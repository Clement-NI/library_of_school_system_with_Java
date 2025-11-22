package com.Modele;



public class Magazine extends Document{
    private int numero;
    private Periodite periodite;

    public Magazine(String nom,String description, int numero, Periodite periodite) {
        super(nom, description);
        this.numero = numero;
        this.periodite = periodite;
    }


    public Periodite getPeriodite() {
        return periodite;
    }

    public int getNumero() {
        return numero;
    }

    public void setPeriodite(Periodite periodite) {
        this.periodite = periodite;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}
