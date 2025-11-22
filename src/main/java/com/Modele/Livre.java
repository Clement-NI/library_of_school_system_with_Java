package com.Modele;

public class Livre extends Document{
    private String ISBN;
    private int nbpages;

    public Livre(String nom, String description, String ISBN, int nbpages) {
        super(nom, description);
        this.ISBN = ISBN;
        this.nbpages = nbpages;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getNbpages() {
        return nbpages;
    }

    public void setNbpages(int nbpages) {
        this.nbpages = nbpages;
    }
}
