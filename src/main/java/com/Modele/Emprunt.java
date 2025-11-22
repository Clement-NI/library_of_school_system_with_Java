package com.Modele;

import java.util.Date;

public class Emprunt {
    private String ID_Emprunt;
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

    public String getID_Emprunt() {
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

    public void modifyStatusAdherent(){
        if(this.DateRetourPrevue.before(this.DateRetourReelle)){
            this.adherent.setStatut_Adherent(true);

        }
    }

    @Override
    public String toString() {
        return "Adherent : " + this.adherent + "\nDocument : " + this.Document.getNom() + "\nDate d'emprunt: " + this.DateEmprunt
                + "\nDate d'retourPrevue: " + this.DateRetourPrevue + "\nDate d'retourReelle: " + this.DateRetourReelle;
    }
}
