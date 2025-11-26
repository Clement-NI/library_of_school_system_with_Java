package com.Modele;

import java.util.Date;

import static java.lang.Math.max;

public class Emprunt {
    private int ID_Emprunt;
    private Document Document;
    private Adherent adherent;
    private Date DateEmprunt;
    private Date DateRetourPrevue;
    private Date DateRetourReelle;

    public Emprunt(Document Document, Adherent adherent, Date Emprunt, Date RetourPrevue) {
        this.Document = Document;
        this.adherent = adherent;
        this.DateEmprunt = Emprunt;
        this.DateRetourPrevue = RetourPrevue;
    }

    public int getID_Emprunt() {
        return ID_Emprunt;
    }

    public Document getDocument() {
        return Document;
    }

    public Adherent getAdherent() {
        return adherent;
    }

    public Date getDateEmprunt() {
        return DateEmprunt;
    }

    public Date getDateRetourPrevue() {
        return DateRetourPrevue;
    }

    public Date getDateRetourReelle() {
        return DateRetourReelle;
    }

    public void setDateRetourReelle(Date dateRetourReelle) {
        DateRetourReelle = dateRetourReelle;
    }

    public void setID(int ID_Emprunt) {
        this.ID_Emprunt = ID_Emprunt;
    }

    public void modifyStatusAdherent(int jour_de_retard){
            this.adherent.setStatut_Adherent(max(0.5 * jour_de_retard,adherent.getStatutPenalite()));
    }

    @Override
    public String toString() {
        return "Adherent : " + this.adherent + "\nDocument : " + this.Document.getNom() + "\nDate d'emprunt: " + this.DateEmprunt
                + "\nDate d'retourPrevue: " + this.DateRetourPrevue + "\nDate d'retourReelle: " + this.DateRetourReelle;
    }
}
