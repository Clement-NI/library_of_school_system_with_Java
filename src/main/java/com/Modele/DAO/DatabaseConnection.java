package com.Modele.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitaire de connexion à la base de données
 * Responsable de la création de connexions SQLite et de l'initialisation des tables
 */
public class DatabaseConnection {
    // Chemin du fichier de base de données SQLite (créé dans le répertoire racine du projet)
    private static final String DATABASE_URL = "jdbc:sqlite:bibliotheque.db";

    static {
        try {
            // Charger le pilote JDBC SQLite
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Pilote JDBC SQLite non trouvé: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtenir une connexion à la base de données
     * @return Connection objet de connexion à la base de données
     * @throws SQLException exception de connexion à la base de données
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    /**
     * Initialiser les tables de la base de données
     * Créer tous les tableaux nécessaires: adherent, document, magazine, emprunt
     */
    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            // Créer la table des membres
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS Adherent (" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  nom TEXT NOT NULL," +
                "  prenom TEXT NOT NULL," +
                "  statutPenalite INTEGER DEFAULT 0" +
                ")"
            );
            System.out.println(" Table 'adherent' créée");

            // Creer la table des documents
            conn.createStatement().execute(
              "CREATE TABLE IF NOT EXISTS document (\n" +
                      "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                      "    nom TEXT NOT NULL,\n" +
                      "    description TEXT,\n" +
                      "    type TEXT NOT NULL\n" +
                      ")"
            );
            System.out.println(" Table 'document' créée");

            // Créer la table des livres
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS Livres (" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  nom TEXT NOT NULL," +
                "  description TEXT," +
                "  ISBN TEXT NOT NULL," +
                        "nombre_de_pages INTEGER NOT NULL" +
                ")"
            );
            System.out.println(" Table 'livres' créée");


            // Créer la table des magazines
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS Magazine (" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  document_id INTEGER NOT NULL," +
                "  numero INTEGER," +
                "  periodite TEXT" +
                ")"
            );
            System.out.println("Table 'magazine' créée");

            // Créer la table des enregistrements d'emprunt
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS Emprunt (" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  document TEXT NOT NULL," +
                 " type_de_document TEXT NOT NULL," +
                "  adherent_id INTEGER NOT NULL," +
                "  dateEmprunt DATE NOT NULL," +
                "  dateRetourPrevue DATE NOT NULL," +
                "  dateRetourReelle DATE," +
                        "CHECK type_de_document in ('magazine', 'livre')," +
                "  FOREIGN KEY(adherent_id) REFERENCES adherent(id)" +
                ")"
            );
            System.out.println(" Table 'emprunt' créée");

            System.out.println("\n Initialisation de la base de données réussie! Fichier: bibliotheque.db");

        } catch (SQLException e) {
            System.err.println(" Échec de l'initialisation de la base de données: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Tester la connexion à la base de données
     */
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("✓ Connexion à la base de données réussie!");
            }
        } catch (SQLException e) {
            System.err.println("✗ Échec de la connexion à la base de données: " + e.getMessage());
            e.printStackTrace();
        }
    }
}