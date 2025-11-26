package com.Modele;

public enum Periodite {
    SEMAINE("semaine"),
    HEBDOMATAIRE("hebdomataire"),
    MENSUEL("mensuel"),
    ANNUEL("annuel");

    private String libelle;

    Periodite(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}