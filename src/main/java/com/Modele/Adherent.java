package com.Modele;

public class Adherent {
    private int ID_Adherent;
    private String Nom;
    private String Prenom;
    private boolean Statut_Penalite;

    public Adherent(int ID, String Nom, String Prenom) {
        this.ID_Adherent = ID;
        this.Nom = Nom;
        this.Prenom = Prenom;
        Statut_Penalite = false;
    }

    public Adherent(String Nom, String Prenom) {
        this.Nom = Nom;
        this.Prenom = Prenom;
        Statut_Penalite = false;
    }

    public int getID() {
        return ID_Adherent;
    }

    public String getNom() {
        return Nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public boolean getStatutPenalite() {
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

    public void setID(int ID_Adherent) {
        this.ID_Adherent = ID_Adherent;
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
