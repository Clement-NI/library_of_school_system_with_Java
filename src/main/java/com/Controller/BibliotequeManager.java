package com.Controller;

import com.Modele.Adherent;
import com.Modele.Document;
import com.Modele.Livre;
import com.Modele.Magazine;
import com.Modele.Emprunt;
import com.Modele.DAO.AdherentDAO;
import com.Modele.DAO.DocumentDAO;
import com.Modele.DAO.EmpruntDAO;
import com.Modele.DAO.DatabaseConnection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Gestionnaire de la bibliothèque
 * Coordonne les opérations entre les DAOs et la logique métier
 */
public class BibliotequeManager {
    private AdherentDAO adherentDAO;
    private DocumentDAO documentDAO;
    private EmpruntDAO empruntDAO;

    /**
     * Constructeur initialisant les DAOs et la base de données
     */
    public BibliotequeManager() {
        // Initialiser la base de données
        DatabaseConnection.initializeDatabase();

        // Initialiser les DAOs
        this.adherentDAO = new AdherentDAO();
        this.documentDAO = new DocumentDAO();
        this.empruntDAO = new EmpruntDAO();
    }

    // ========== ADHERENT ==========


    public void inscriptionAdherent(String nom, String prenom) {
        try {
            Adherent adherent = new Adherent(nom, prenom);
            adherentDAO.ajouter(adherent);
            System.out.println("✓ Nouvel adhérent ajouté: " + adherent.toString());
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de l'ajout d'adhérent: " + e.getMessage());
        }
    }


    public Adherent rechercherAdherent(int id) {
        try {
            return adherentDAO.rechercherParId(id);
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche: " + e.getMessage());
            return null;
        }
    }


    public ArrayList<Adherent> rechercherAdherentParNom(String nom) {
        try {
            return adherentDAO.rechercherParNom(nom);
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche: " + e.getMessage());
            return new ArrayList<>();
        }
    }


    public ArrayList<Adherent> obtenirTousLesAdherents() {
        try {
            return adherentDAO.obtenirTous();
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors du chargement: " + e.getMessage());
            return new ArrayList<>();
        }
    }


    public void modifierAdherent(int id, String nom, String prenom) {
        try {
            Adherent adherent = new Adherent(id, nom, prenom);
            adherentDAO.modifier(adherent);
            System.out.println("✓ Adhérent modifié: " + adherent.toString());
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la modification: " + e.getMessage());
        }
    }


    public void supprimerAdherent(int id) {
        try {
            adherentDAO.supprimer(id);
            System.out.println("✓ Adhérent supprimé");
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la suppression: " + e.getMessage());
        }
    }


    public void afficherTousLesAdherents() {
        ArrayList<Adherent> adherents = obtenirTousLesAdherents();
        if (adherents.isEmpty()) {
            System.out.println("Aucun adhérent trouvé");
        } else {
            System.out.println("\n=== Liste des adhérents ===");
            for (Adherent a : adherents) {
                System.out.println(a.toString());
            }
        }
    }



    // ========== LIVRE ==========
    public void ajouterLivre(String nom, String description, String isbn, int nbpages) {
        try {
            Livre livre = new Livre(nom, description, isbn, nbpages);
            documentDAO.ajouterDocument(livre);
            System.out.println("Livre ajouté: " + livre.toString());
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    public Livre rechercherLivre(int id) {
        try {
            return documentDAO.rechercherLivreParId(id);
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche: " + e.getMessage());
            return null;
        }
    }


    public ArrayList<Livre> rechercherLivreParNom(String nom) {
        try {
            return documentDAO.rechercherLivreParNom(nom);
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche: " + e.getMessage());
            return new ArrayList<>();
        }
    }


    public ArrayList<Livre> obtenirTousLesLivres() {
        try {
            return documentDAO.obtenirTousLesLivres();
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors du chargement: " + e.getMessage());
            return new ArrayList<>();
        }
    }


    public void modifierLivre(Livre livre) {
        try {
            documentDAO.modifierLivre(livre);
            System.out.println("✓ Livre modifié: " + livre.toString());
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la modification: " + e.getMessage());
        }
    }


    public void supprimerLivre(int id) {
        try {
            documentDAO.supprimerLivre(id);
            System.out.println("✓ Livre supprimé");
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la suppression: " + e.getMessage());
        }
    }


    public void afficherTousLesLivres() {
        ArrayList<Livre> livres = obtenirTousLesLivres();
        if (livres.isEmpty()) {
            System.out.println("Aucun livre trouvé");
        } else {
            System.out.println("\n=== Liste des livres ===");
            for (Livre l : livres) {
                System.out.println(l.toString());
            }
        }
    }

    /**
     * Compter le nombre total de livres
     * @return nombre de livres
     */
    public int compterLivres() {
        try {
            return documentDAO.compterLivres();
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors du comptage: " + e.getMessage());
            return 0;
        }
    }

    // ========== MAGAZINE ==========

    /**
     * Ajouter un magazine
     * @param nom nom du magazine
     * @param description description du magazine
     * @param numero numéro du magazine
     * @param periodite périodicité
     */
    public void ajouterMagazine(String nom, String description, int numero, String periodite) {
        try {
            Magazine magazine = new Magazine(nom, description, numero,
                com.Modele.Periodite.valueOf(periodite));
            documentDAO.ajouterDocument(magazine);
            System.out.println("✓ Magazine ajouté: " + magazine.toString());
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de l'ajout: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("✗ Périodicité invalide: " + e.getMessage());
        }
    }

    /**
     * Rechercher un magazine par ID
     * @param id ID du magazine
     * @return objet Magazine ou null
     */
    public Magazine rechercherMagazine(int id) {
        try {
            return documentDAO.rechercherMagazineParId(id);
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche: " + e.getMessage());
            return null;
        }
    }

    /**
     * Rechercher des magazines par nom
     * @param nom nom à rechercher
     * @return ArrayList des magazines correspondants
     */
    public ArrayList<Magazine> rechercherMagazineParNom(String nom) {
        try {
            return documentDAO.rechercherMagazineParNom(nom);
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la recherche: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Obtenir tous les magazines
     * @return ArrayList de tous les magazines
     */
    public ArrayList<Magazine> obtenirTousLesMagazines() {
        try {
            return documentDAO.obtenirTousLesMagazines();
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors du chargement: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Modifier un magazine
     * @param magazine magazine à modifier
     */
    public void modifierMagazine(Magazine magazine) {
        try {
            documentDAO.modifierMagazine(magazine);
            System.out.println("✓ Magazine modifié: " + magazine.toString());
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la modification: " + e.getMessage());
        }
    }

    /**
     * Supprimer un magazine
     * @param id ID du magazine
     */
    public void supprimerMagazine(int id) {
        try {
            documentDAO.supprimerMagazine(id);
            System.out.println("✓ Magazine supprimé");
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la suppression: " + e.getMessage());
        }
    }

    /**
     * Afficher tous les magazines
     */
    public void afficherTousLesMagazines() {
        ArrayList<Magazine> magazines = obtenirTousLesMagazines();
        if (magazines.isEmpty()) {
            System.out.println("Aucun magazine trouvé");
        } else {
            System.out.println("\n=== Liste des magazines ===");
            for (Magazine m : magazines) {
                System.out.println(m.toString());
            }
        }
    }

    /**
     * Compter le nombre total de magazines
     * @return nombre de magazines
     */
    public int compterMagazines() {
        try {
            return documentDAO.compterMagazines();
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors du comptage: " + e.getMessage());
            return 0;
        }
    }

    // ========== EMPRUNT ==========
    public Emprunt creerEmprunt(int adherentId, int documentId, Date dateRetourPrevue) {
        try {
            Adherent adherent = adherentDAO.rechercherParId(adherentId);
            Document document = null;

            // Chercher d'abord dans les livres
            Livre livre = documentDAO.rechercherLivreParId(documentId);
            if (livre != null) {
                document = livre;
            } else {
                // Sinon chercher dans les magazines
                Magazine magazine = documentDAO.rechercherMagazineParId(documentId);
                if (magazine != null) {
                    document = magazine;
                }
            }

            if (adherent != null && document != null) {
                Emprunt emprunt = new Emprunt(document, adherent, new Date(), dateRetourPrevue);
                empruntDAO.ajouter(emprunt);
                System.out.println("Emprunt créé: " + emprunt.toString());
                return emprunt;
            } else {
                System.err.println("Adhérent ou document non trouvé");
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création d'emprunt: " + e.getMessage());
            return null;
        }
    }

    public void enregistrerRetour(int empruntId) {
        try {
            empruntDAO.enregistrerRetour(empruntId);
            System.out.println("Retour enregistré");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement du retour: " + e.getMessage());
        }
    }


    public ArrayList<Emprunt> obtenirTousLesEmprunts() {
        try {
            return empruntDAO.obtenirTous();
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public ArrayList<Emprunt> obtenirEmpruntsAdherent(int adherentId) {
        try {
            return empruntDAO.obtenirEmpruntsParAdherent(adherentId);
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public ArrayList<Emprunt> obtenirEmpruntsNonRetournes() {
        try {
            return empruntDAO.obtenirEmpruntsNonRetournes();
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors du chargement: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public ArrayList<Emprunt> obtenirEmpruntsEnRetard() {
        try {
            ArrayList<Emprunt> emprunts = empruntDAO.obtenirEmpruntsEnRetard();
            for(Emprunt emprunt : emprunts){
                emprunt.modifyStatusAdherent();
                adherentDAO.modifier(emprunt.getAdherent());
            }
            return emprunts;
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors du chargement: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public ArrayList<Emprunt> obtenirEmpruntsEnRetardAdherent(int adherentId) {
        try {
            return empruntDAO.obtenirEmpruntsEnRetardParAdherent(adherentId);
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors du chargement: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void afficherTousLesEmprunts() {
        ArrayList<Emprunt> emprunts = obtenirTousLesEmprunts();
        if (emprunts.isEmpty()) {
            System.out.println("Aucun emprunt trouvé");
        } else {
            System.out.println("\n=== Liste des emprunts ===");
            for (Emprunt e : emprunts) {
                System.out.println(e.toString());
            }
        }
    }


    public int compterEmprunts() {
        try {
            return empruntDAO.compterEmprunts();
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors du comptage: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Compter le nombre d'emprunts non retournés
     * @return nombre d'emprunts non retournés
     */
    public int compterEmpruntsNonRetournes() {
        try {
            return empruntDAO.compterEmpruntsNonRetournes();
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors du comptage: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Compter le nombre d'emprunts en retard
     * @return nombre d'emprunts en retard
     */
    public int compterEmpruntsEnRetard() {
        try {
            return empruntDAO.compterEmpruntsEnRetard();
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage: " + e.getMessage());
            return 0;
        }
    }

    public void supprimerEmprunt(int empruntId) {
        try {
            empruntDAO.supprimer(empruntId);
        }catch (SQLException e){
            System.err.println("Erreur lors du supprimer de emprunt: " + e.getMessage());
        }
    }

    public void modifierEmprunt(int empruntId, String dateRetourPrevue) {
    try {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateRetourPrevueEnDate = dateFormat.parse(dateRetourPrevue);


        Date aujourdhui = new Date();
        if (dateRetourPrevueEnDate.after(aujourdhui)) {
            empruntDAO.mettreAJourDateRetourPrevue(empruntId, dateRetourPrevueEnDate);
            System.out.println("Date de retour prévue modifiée avec succès");
        } else {
            System.err.println("Erreur: La date de retour ne peut pas être dans le passé");
        }

    } catch (ParseException e) {
        System.err.println("Erreur de format de date: " + e.getMessage());
    } catch (SQLException e) {
        System.err.println("Erreur lors de la modification de l'emprunt: " + e.getMessage());
    }
}
}