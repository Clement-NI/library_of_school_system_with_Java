package com.Modele;

public class Adherent {
    private static int nextId = 1;
    private int ID_Adherent;
    private String Nom;
    private String Prenom;
    private boolean Statut_Penalite;

    public Adherent(String Nom, String Prenom) {
        this.ID_Adherent = nextId++;
        this.Nom = Nom;
        this.Prenom = Prenom;
    }

    public int getID_Adherent() {
        return ID_Adherent;
    }

    public String getNom_Adherent() {
        return Nom;
    }

    public String getPrenom_Adherent() {
        return Prenom;
    }

    public boolean getStatut_Penalite() {
        return Statut_Penalite;
    }

    public void setStatut_Adherent(boolean Statut_Penalite) {
        this.Statut_Penalite = Statut_Penalite;
    }

    public void setNom_Adherent(String Nom) {
        this.Nom = Nom;
    }

    public void setPrenom_Adherent(String Prenom) {
        this.Prenom = Prenom;
    }

    public void setNomPrenom(String nom, String prenom) {
        setNom_Adherent(nom);
        setPrenom_Adherent(prenom);
    }

    @Override
    public String toString() {
        return "Id : " + ID_Adherent + "Nom : " + Nom + "Prenom : " + Prenom;
    }
}
