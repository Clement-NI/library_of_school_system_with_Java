package com.Controller;

import com.Modele.Adherent;
import com.Modele.Document;
import com.Modele.Emprunt;

import java.util.ArrayList;
import java.util.Date;

public class BibliotequeManager {
    public ArrayList<Adherent> listeAdherent;
    public ArrayList<Emprunt> listeEmprunt;
    public BibliotequeManager() {
        listeAdherent = new ArrayList<>();
        listeEmprunt = new ArrayList<>();
    }

    public void ajouterAdherent(Adherent adherent) {
        listeAdherent.add(adherent);
    }

    public void ajouterEmprunt(Emprunt emprunt) {
        listeEmprunt.add(emprunt);
    }

    public ArrayList<Adherent> getListeAdherent() {
        return listeAdherent;
    }

    public ArrayList<Emprunt> getListeEmprunt() {
        return listeEmprunt;
    }

    public void afficherListeAdherent() {
        for (Adherent adherent : listeAdherent) {
            System.out.println(adherent.toString());
        }
    }

    public void afficherListeEmprunt() {
        for (Emprunt emprunt : listeEmprunt) {
            System.out.println(emprunt.toString());
        }
    }

    public void Inscription_adrent(String nom, String prenom){
        Adherent adherent = new Adherent(nom, prenom);
        this.ajouterAdherent(adherent);
        System.out.println("Nouvel adherent ajoute: " + adherent.toString());
    }

    public Emprunt emprunt(Adherent adherent, Document document, Date date_de_retour_prevu){
        Emprunt emprunt_creer = new Emprunt(document,adherent,new Date(),date_de_retour_prevu);
        this.listeEmprunt.add(emprunt_creer);
        System.out.println(emprunt_creer.toString());
        return emprunt_creer;
    }

    public void Retour(Emprunt emprunt){
        emprunt.setDateRetourReelle(new Date());
        emprunt.modifyStatusAdherent();
        System.out.println(emprunt.toString());
    }



}
