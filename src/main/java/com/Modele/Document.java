package com.Modele;

public abstract class Document {
    protected String nom;
    protected String description;

    public Document(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }


}
