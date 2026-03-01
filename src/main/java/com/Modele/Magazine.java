package com.Modele;



public class Magazine extends Document{

    private int numero;
    private Periodite periodite;

    public Magazine(int id,String nom,String description, int numero, Periodite periodite) {
        super(id,nom, description,"magazine");
        this.numero = numero;
        this.periodite = periodite;
    }

    public Magazine(String nom, String description, int numero, Periodite periodite) {
        super(nom, description,"magazine");
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
