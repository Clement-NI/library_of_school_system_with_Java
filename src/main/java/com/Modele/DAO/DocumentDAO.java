package com.Modele.DAO;

import com.Modele.Document;
import com.Modele.Livre;
import com.Modele.Magazine;
import com.Modele.Periodite;
import java.sql.*;
import java.util.ArrayList;


public class DocumentDAO {

    // ========= DOCUMENT =========
    public void ajouterDocument(Document document) throws SQLException{
        String sql_document = "INSERT INTO document(nom,description,type_de_document) VALUES(?,?,?)";

       try(Connection conn = DatabaseConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql_document)) {
           ps.setString(1, document.getNom());
           ps.setString(2, document.getDescription());
           System.out.println("Ici" + document.getType_de_document());
           ps.setString(3,document.getType_de_document());
           ps.executeUpdate();
             // Obtenir l'ID généré automatiquement
            try (Statement stmt2 = conn.createStatement();
             ResultSet rs = stmt2.executeQuery("SELECT last_insert_rowid() as id")) {
            if (rs.next()) {
                document.setId(rs.getInt("id"));
            }

            if(document.getType_de_document().equals("magazine")){
                this.ajouterMagazine((Magazine) document);
            }else if(document.getType_de_document().equals("livre")){
                this.ajouterLivre((Livre) document);
            }else{
                System.out.println("Erreur du type de document");
            }

       }
      }catch (SQLException e) {
           e.printStackTrace();
       }
    }

    public ArrayList<Document> obtenirTousLesDocument() throws SQLException {
        ArrayList<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM document_view";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                documents.add(crierDocument(rs));
            }
        }
        return documents;
    }


    public Document rechercherDocumentsParId(int id) throws SQLException {
        String sql = "SELECT * FROM document_view WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return crierDocument(rs);
            }
        }
        return null;
    }

    public Document modifiyDocument(Document document) throws SQLException{
        return null;
    }

    public ArrayList<Document> rechercherTousLesDocumentsParNom(String nom) throws SQLException {
        ArrayList<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM document_view WHERE nom LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nom + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                documents.add(crierDocument(rs));
            }
        }

        return documents;
    }

     private Document crierDocument(ResultSet rs) throws SQLException {
        if(rs.getString("type_de_document").equals("magazine")){
             Document document = new Magazine(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("description"),
                rs.getInt("numero"),
                Periodite.valueOf(rs.getString("periodite").toUpperCase())
          );
             return document;

        }else if(rs.getString("type_de_document").equals("livre")) {
            Document document = new Livre(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("description"),
                rs.getString("ISBN"),
                rs.getInt("nombre_de_pages")
          );
            return document;


        }else{
           return null;
        }

    }

    public void supprimerDocument(int documentId) throws SQLException{
        String sql1 = "DELETE FROM document WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql1)) {
            stmt.setInt(1,documentId);
            stmt.executeUpdate();

        }

        String sql2 = "DELETE FROM livres WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql2)) {
            stmt.setInt(1,documentId);
            stmt.executeUpdate();

        }

        String sql3 = "DELETE FROM magazine WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql3)) {
            stmt.setInt(1,documentId);
            stmt.executeUpdate();

        }




    }


    // ========== LIVRE ==========
    public void ajouterLivre(Livre livre) throws SQLException {

        String sql = "INSERT INTO Livres (nom, description, ISBN, nombre_de_pages,id) VALUES (?, ?, ?, ?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, livre.getNom());
            stmt.setString(2, livre.getDescription());
            stmt.setString(3, livre.getISBN());
            stmt.setInt(4, livre.getNbpages());
            stmt.setInt(5, livre.getId());
            stmt.executeUpdate();


        }


    }


    public Livre rechercherLivreParId(int id) throws SQLException {
        String sql = "SELECT * FROM Livres WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return crierLivre(rs);
            }
        }
        return null;
    }


    public ArrayList<Livre> obtenirTousLesLivres() throws SQLException {
        ArrayList<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM Livres";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                livres.add(crierLivre(rs));
            }
        }
        return livres;
    }

    /**
     * Rechercher des livres par nom
     * @param nom nom du livre
     * @return liste des livres correspondants
     * @throws SQLException exception de la base de données
     */
    public ArrayList<Livre> rechercherLivreParNom(String nom) throws SQLException {
        ArrayList<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM Livres WHERE nom LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nom + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                livres.add(crierLivre(rs));
            }
        }
        return livres;
    }

    /**
     * Mettre à jour un livre
     * @param livre objet Livre à mettre à jour
     * @throws SQLException exception de la base de données
     */
    public void modifierLivre(Livre livre) throws SQLException {
        String sql = "UPDATE Livres SET nom = ?, description = ?, ISBN = ?, nombre_de_pages = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, livre.getNom());
            stmt.setString(2, livre.getDescription());
            stmt.setString(3, livre.getISBN());
            stmt.setInt(4, livre.getNbpages());
            stmt.setInt(5, livre.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Supprimer un livre
     * @param id ID du livre
     * @throws SQLException exception de la base de données
     */
    public void supprimerLivre(int id) throws SQLException {
        String sql = "DELETE FROM Livres WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Compter le nombre total de livres
     * @return nombre total de livres
     * @throws SQLException exception de la base de données
     */
    public int compterLivres() throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM Livres";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        }
        return 0;
    }


    private Livre crierLivre(ResultSet rs) throws SQLException {
        Livre livre = new Livre(
            rs.getInt("id"),
            rs.getString("nom"),
            rs.getString("description"),
            rs.getString("ISBN"),
            rs.getInt("nombre_de_pages")
        );
        return livre;
    }

    // ========== MAGAZINE ==========


    public void ajouterMagazine(Magazine magazine) throws SQLException {
        String sql = "INSERT INTO Magazine (nom, description, numero, periodite,id) VALUES (?, ?, ?, ?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, magazine.getNom());
            stmt.setString(2, magazine.getDescription());
            stmt.setInt(3, magazine.getNumero());
            stmt.setString(4, magazine.getPeriodite().toString());
            stmt.setInt(5, magazine.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Rechercher un magazine par ID
     * @param id ID du magazine
     * @return objet Magazine, retourne null s'il n'existe pas
     * @throws SQLException exception de la base de données
     */
    public Magazine rechercherMagazineParId(int id) throws SQLException {
        String sql = "SELECT * FROM Magazine WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return crierMagazine(rs);
            }
        }
        return null;
    }

    /**
     * Obtenir tous les magazines
     * @return ArrayList de tous les magazines
     * @throws SQLException exception de la base de données
     */
    public ArrayList<Magazine> obtenirTousLesMagazines() throws SQLException {
        ArrayList<Magazine> magazines = new ArrayList<>();
        String sql = "SELECT * FROM Magazine";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                magazines.add(crierMagazine(rs));
            }
        }
        return magazines;
    }

    /**
     * Rechercher des magazines par nom
     * @param nom nom du magazine
     * @return liste des magazines correspondants
     * @throws SQLException exception de la base de données
     */
    public ArrayList<Magazine> rechercherMagazineParNom(String nom) throws SQLException {
        ArrayList<Magazine> magazines = new ArrayList<>();
        String sql = "SELECT * FROM Magazine WHERE nom LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nom + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                magazines.add(crierMagazine(rs));
            }
        }
        return magazines;
    }

    /**
     * Mettre à jour un magazine
     * @param magazine objet Magazine à mettre à jour
     * @throws SQLException exception de la base de données
     */
    public void modifierMagazine(Magazine magazine) throws SQLException {
        String sql = "UPDATE Magazine SET nom = ?, description = ?, numero = ?, periodite = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, magazine.getNom());
            stmt.setString(2, magazine.getDescription());
            stmt.setInt(3, magazine.getNumero());
            stmt.setString(4, magazine.getPeriodite().toString());
            stmt.setInt(5, magazine.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Supprimer un magazine
     * @param id ID du magazine
     * @throws SQLException exception de la base de données
     */
    public void supprimerMagazine(int id) throws SQLException {
        String sql = "DELETE FROM Magazine WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Compter le nombre total de magazines
     * @return nombre total de magazines
     * @throws SQLException exception de la base de données
     */
    public int compterMagazines() throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM Magazine";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        }
        return 0;
    }

    /**
     * Créer un objet Magazine à partir de ResultSet
     * @param rs objet ResultSet
     * @return objet Magazine
     * @throws SQLException exception de la base de données
     */
    private Magazine crierMagazine(ResultSet rs) throws SQLException {
        Magazine magazine = new Magazine(
            rs.getInt("id"),
            rs.getString("nom"),
            rs.getString("description"),
            rs.getInt("numero"),
            Periodite.valueOf(rs.getString("periodite").toUpperCase())
        );
        return magazine;
    }




    public int compterDocuments() throws SQLException {
        return compterLivres() + compterMagazines();
    }

    public boolean EstDisponible(int documentId) throws SQLException {
    // Vérifier si le document est actuellement emprunté (pas encore retourné)
    String sql = "SELECT * FROM Emprunt WHERE document_id = ? AND dateRetourReelle IS NULL";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, documentId);

        try (ResultSet rs = stmt.executeQuery()) {
            // Si un résultat existe = document emprunté = NON disponible
            if (rs.next()) {
                return false; // Document emprunté, pas disponible
            }
        }

        return true; // Aucun emprunt actif = disponible

    } catch (SQLException e) {
        e.printStackTrace();
        throw e; // Relancer l'exception au lieu de retourner true
    }
}




}