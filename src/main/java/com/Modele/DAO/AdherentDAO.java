package com.Modele.DAO;

import com.Modele.*;

import java.sql.*;
import java.util.ArrayList;

/**
 * Objet d'accès aux données Adherent (DAO)
 * Responsable de toutes les interactions avec la table des membres dans la base de données
 */
public class AdherentDAO {

    /**
     * Ajouter un nouveau membre à la base de données
     *
     * @param adherent objet Adherent à ajouter
     * @return
     * @throws SQLException exception de la base de données
     */
    public Adherent ajouter(Adherent adherent) throws SQLException {
        String sql = "INSERT INTO adherent (nom, prenom, statutPenalite) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, adherent.getNom());
            stmt.setString(2, adherent.getPrenom());
            stmt.setBoolean(3, adherent.getStatutPenalite());
            stmt.executeUpdate();

            // Obtenir l'ID généré automatiquement et le définir dans l'objet
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                adherent.setID(generatedKeys.getInt(1));
            }
        }
        return adherent;
    }

    public Adherent rechercherParId(int id) throws SQLException {
        String sql = "SELECT * FROM adherent WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return creerAdherent(rs);
            }
        }
        return null;
    }

    public ArrayList<Adherent> obtenirTous() throws SQLException {
        ArrayList<Adherent> adherents = new ArrayList<>();
        String sql = "SELECT * FROM adherent";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                adherents.add(creerAdherent(rs));
            }
        }
        return adherents;
    }

    public ArrayList<Adherent> rechercherParNom(String str) throws SQLException {
        ArrayList<Adherent> adherents = new ArrayList<>();
        String sql = "SELECT * FROM adherent WHERE nom LIKE ? OR prenom LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + str + "%");
            stmt.setString(2, "%" + str + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                adherents.add(creerAdherent(rs));
            }
        }
        return adherents;
    }

    public void modifier(Adherent adherent) throws SQLException {
        String sql = "UPDATE adherent SET nom = ?, prenom = ?, statutPenalite = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, adherent.getNom());
            stmt.setString(2, adherent.getPrenom());
            stmt.setBoolean(3, adherent.getStatutPenalite());
            stmt.setInt(4, adherent.getID());
            stmt.executeUpdate();
        }
    }


    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM adherent WHERE id = ? AND statutPenalite = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Adherent creerAdherent(ResultSet rs) throws SQLException {
        Adherent adherent = new Adherent(-1,rs.getString("nom"), rs.getString("prenom"));
        adherent.setID(rs.getInt("id"));
        adherent.setStatut_Adherent(rs.getBoolean("statutPenalite"));
        return adherent;
    }

}