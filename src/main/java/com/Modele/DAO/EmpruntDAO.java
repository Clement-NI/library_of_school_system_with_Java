package com.Modele.DAO;

import com.Modele.Emprunt;
import com.Modele.Adherent;
import com.Modele.Livre;
import com.Modele.Magazine;
import com.Modele.Document;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Objet d'accès aux données Emprunt (DAO)
 * Responsable de toutes les interactions avec la table des emprunts
 */
public class EmpruntDAO {

    /**
     * Ajouter un nouvel enregistrement d'emprunt à la base de données
     * @param emprunt objet Emprunt à ajouter
     * @throws SQLException exception de la base de données
     */
    public void ajouter(Emprunt emprunt) throws SQLException {
        String sql = "INSERT INTO emprunt (document_id, adherent_id, dateEmprunt, dateRetourPrevue) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, emprunt.getDocument().getId());
            stmt.setInt(2, emprunt.getAdherent().getID());
            stmt.setDate(3, new java.sql.Date(emprunt.getDateEmprunt().getTime()));
            stmt.setDate(4, new java.sql.Date(emprunt.getDateRetourPrevue().getTime()));
            stmt.executeUpdate();

             try (Statement stmt2 = conn.createStatement();
             ResultSet rs = stmt2.executeQuery("SELECT last_insert_rowid() as id")) {
            if (rs.next()) {
                emprunt.setID(rs.getInt("id"));
            }
        }
        }
    }

    public Emprunt rechercherParId(int id) throws SQLException {
        String sql = "SELECT * FROM emprunt WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return crierEmprunt(rs);
            }
        }
        return null;
    }


    public ArrayList<Emprunt> obtenirTous() throws SQLException {
        ArrayList<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunt ORDER BY dateEmprunt DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                emprunts.add(crierEmprunt(rs));
            }
        }
        return emprunts;
    }

    public ArrayList<Emprunt> obtenirEmpruntsParAdherent(int adherentId) throws SQLException {
        ArrayList<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunt WHERE adherent_id = ? ORDER BY dateEmprunt DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, adherentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                emprunts.add(crierEmprunt(rs));
            }
        }
        return emprunts;
    }


    public ArrayList<Emprunt> obtenirEmpruntsNonRetournes() throws SQLException {
        ArrayList<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunt WHERE dateRetourReelle IS NULL ORDER BY dateRetourPrevue";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                emprunts.add(crierEmprunt(rs));
            }
        }
        return emprunts;
    }


    public ArrayList<Emprunt> obtenirEmpruntsEnRetard() throws SQLException {
        ArrayList<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunt " +
                     "WHERE dateRetourPrevue > CURRENT_DATE " +
                     "ORDER BY dateRetourPrevue";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                emprunts.add(crierEmprunt(rs));
            }
        }
        return emprunts;
    }


    public ArrayList<Emprunt> obtenirEmpruntsEnRetardParAdherent(int adherentId) throws SQLException {
        ArrayList<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunt " +
                     "WHERE adherent_id = ? AND dateRetourPrevue > CURRENT_DATE " +
                     "ORDER BY dateRetourPrevue";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, adherentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                emprunts.add(crierEmprunt(rs));
            }
        }
        return emprunts;
    }

    /**
     * Obtenir les emprunts non retournés d'un adhérent
     * @param adherentId ID de l'adhérent
     * @return ArrayList des emprunts actuels de cet adhérent
     * @throws SQLException exception de la base de données
     */
    public ArrayList<Emprunt> obtenirEmpruntsNonRetournesParAdherent(int adherentId) throws SQLException {
        ArrayList<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunt " +
                     "WHERE adherent_id = ? AND dateRetourReelle IS NULL " +
                     "ORDER BY dateRetourPrevue";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, adherentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                emprunts.add(crierEmprunt(rs));
            }
        }
        return emprunts;
    }

     public void mettreAJourDateRetourPrevue(int empruntId, Date dateRetourPrevue) throws SQLException {
        String sql = "UPDATE emprunt SET dateRetourPrevue = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(dateRetourPrevue.getTime()));
            stmt.setInt(2, empruntId);
            stmt.executeUpdate();
        }
    }


    public void mettreAJourDateRetour(int empruntId, Date dateRetour) throws SQLException {
        String sql = "UPDATE emprunt SET dateRetourReelle = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(dateRetour.getTime()));
            stmt.setInt(2, empruntId);
            stmt.executeUpdate();
        }
    }

    /**
     * Enregistrer le retour d'un emprunt (avec mise à jour du statut de l'adhérent si en retard)
     * @param empruntId ID d'emprunt
     * @throws SQLException exception de la base de données
     */
    public void enregistrerRetour(int empruntId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Récupérer l'emprunt
                Emprunt emprunt = rechercherParId(empruntId);

                if (emprunt != null) {
                    Date dateActuelle = new Date();

                    // Mettre à jour la date de retour
                    try (PreparedStatement stmt = conn.prepareStatement(
                            "UPDATE emprunt SET dateRetourReelle = ? WHERE id = ?")) {
                        stmt.setDate(1, new java.sql.Date(dateActuelle.getTime()));
                        stmt.setInt(2, empruntId);
                        stmt.executeUpdate();
                    }

                    // Vérifier si en retard et mettre à jour le statut de l'adhérent
                    if (dateActuelle.after(emprunt.getDateRetourPrevue())) {
                        try (PreparedStatement stmt = conn.prepareStatement(
                                "UPDATE adherent SET statutPenalite = 1 WHERE id = ?")) {
                            stmt.setInt(1, emprunt.getAdherent().getID());
                            stmt.executeUpdate();
                        }
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    /**
     * Supprimer un enregistrement d'emprunt
     * @param id ID d'emprunt
     * @throws SQLException exception de la base de données
     */
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM emprunt WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Compter le nombre total d'emprunts
     * @return nombre total d'emprunts
     * @throws SQLException exception de la base de données
     */
    public int compterEmprunts() throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM emprunt";

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
     * Compter le nombre d'emprunts non retournés
     * @return nombre d'emprunts non retournés
     * @throws SQLException exception de la base de données
     */
    public int compterEmpruntsNonRetournes() throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM emprunt WHERE dateRetourReelle IS NULL";

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
     * Compter le nombre d'emprunts en retard
     * @return nombre d'emprunts en retard
     * @throws SQLException exception de la base de données
     */
    public int compterEmpruntsEnRetard() throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM emprunt " +
                     "WHERE dateRetourPrevue > CURRENT_DATE";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        }
        return 0;
    }

    public ArrayList<Document> obtenirDocumentsEmpruntes(int adherentId) throws SQLException {
        ArrayList<Document> documents = new ArrayList<>();
        DocumentDAO docDAO = new DocumentDAO();

        String sql = "SELECT DISTINCT e.document_id, d.type " +
                     "FROM emprunt e " +
                     "JOIN document d ON e.document_id = d.id " +
                     "WHERE e.adherent_id = ? AND e.dateRetourReelle IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, adherentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int docId = rs.getInt("document_id");
                String type = rs.getString("type");

                if ("LIVRE".equals(type)) {
                    Livre livre = docDAO.rechercherLivreParId(docId);
                    if (livre != null) {
                        documents.add(livre);
                    }
                } else if ("MAGAZINE".equals(type)) {
                    Magazine magazine = docDAO.rechercherMagazineParId(docId);
                    if (magazine != null) {
                        documents.add(magazine);
                    }
                }
            }
        }
        return documents;
    }

    /**
     * Créer un objet Emprunt à partir de ResultSet
     * @param rs objet ResultSet
     * @return objet Emprunt
     * @throws SQLException exception de la base de données
     */
    private Emprunt crierEmprunt(ResultSet rs) throws SQLException {
        AdherentDAO adherentDAO = new AdherentDAO();
        DocumentDAO documentDAO = new DocumentDAO();

        int adherentId = rs.getInt("adherent_id");
        int documentId = rs.getInt("document_id");

        // Récupérer l'adhérent
        Adherent adherent = adherentDAO.rechercherParId(adherentId);

        // Récupérer le document (livre ou magazine)
        Document document = null;
        String docType = ""; // On va déterminer le type en requêtant

        // Vérifier si c'est un livre ou un magazine
        Livre livre = documentDAO.rechercherLivreParId(documentId);
        if (livre != null) {
            document = livre;
            docType = "LIVRE";
        } else {
            Magazine magazine = documentDAO.rechercherMagazineParId(documentId);
            if (magazine != null) {
                document = magazine;
                docType = "MAGAZINE";
            }
        }

        // Récupérer les dates
        Date dateEmprunt = new Date(rs.getDate("dateEmprunt").getTime());
        Date dateRetourPrevue = new Date(rs.getDate("dateRetourPrevue").getTime());
        Date dateRetourReelle = rs.getDate("dateRetourReelle") != null ?
            new Date(rs.getDate("dateRetourReelle").getTime()) : null;

        // Créer l'objet Emprunt
        Emprunt emprunt = new Emprunt(document, adherent, dateEmprunt, dateRetourPrevue);
        emprunt.setID(rs.getInt("id"));

        if (dateRetourReelle != null) {
            emprunt.setDateRetourReelle(dateRetourReelle);
        }

        return emprunt;
    }
}