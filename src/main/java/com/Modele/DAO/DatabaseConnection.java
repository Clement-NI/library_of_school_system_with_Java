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
                "  statutPenalite FLOAT DEFAULT 0" +
                ")"
            );
            System.out.println(" Table 'adherent' créée");

            // Creer la table des documents
            conn.createStatement().execute(
              "CREATE TABLE IF NOT EXISTS document (\n" +
                      "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                      "    nom TEXT NOT NULL,\n" +
                      "    description TEXT,\n" +
                      "    type_de_document TEXT NOT NULL\n" +
                      ")"
            );
            System.out.println(" Table 'document' créée");

            // Créer la table des livres
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS Livres (" +
                "  id INTEGER PRIMARY KEY," +
                "  nom TEXT NOT NULL," +
                "  description TEXT," +
                "  ISBN TEXT NOT NULL," +
                        "nombre_de_pages INTEGER NOT NULL," +
                        "FOREIGN KEY(id) REFERENCES document(id) ON DELETE CASCADE" +
                ")"
            );
            System.out.println(" Table 'livres' créée");


            // Créer la table des magazines
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS Magazine (" +
                "  id INTEGER PRIMARY KEY," +
                        "  nom TEXT NOT NULL," +
                "  description TEXT," +
                "  numero INTEGER," +
                "  periodite TEXT," +
                        "FOREIGN KEY(id) REFERENCES document(id) ON DELETE CASCADE" +
                ")"
            );
            System.out.println("Table 'magazine' créée");

            // Créer la table des enregistrements d'emprunt
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS Emprunt (" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  document_id INTEGER NOT NULL," +
                 " type_de_document TEXT," +
                "  adherent_id INTEGER NOT NULL," +
                "  dateEmprunt DATE NOT NULL," +
                "  dateRetourPrevue DATE NOT NULL," +
                "  dateRetourReelle DATE," +
                "  FOREIGN KEY(adherent_id) REFERENCES Adherent(id) ON DELETE CASCADE," +
                        "  FOREIGN KEY(document_id) REFERENCES document(id) ON DELETE CASCADE" +
                ")"
            );
            System.out.println(" Table 'emprunt' créée");


            conn.createStatement().execute(
                    "CREATE VIEW IF NOT EXISTS document_view AS " +
                    "SELECT " +
                    "    d.id, " +
                    "    d.nom, " +
                    "    d.description, " +
                    "    d.type_de_document, " +
                    "    l.ISBN, " +
                    "    l.nombre_de_pages, " +
                    "    m.numero, " +
                    "    m.periodite " +
                    "FROM document d " +
                    "LEFT JOIN livres l ON d.id = l.id " +
                    "LEFT JOIN magazine m ON d.id = m.id"
            );
            System.out.println("Vue 'document_view' créée avec succès");

            conn.createStatement().execute(
                "CREATE VIEW IF NOT EXISTS adherent_view AS " +
                "SELECT " +
                "    A.id, " +
                "    A.nom, " +
                "    A.prenom, " +
                "    ROUND(" +
                "        COALESCE(" +
                "            0.5 * SUM(" +
                "                julianday('now') - " +
                "                julianday(datetime(E.dateRetourPrevue / 1000, 'unixepoch'))" +
                "            ), " +
                "            0" +
                "        ), " +
                "        2" +
                "    ) AS penalite " +
                "FROM Adherent A " +
                "LEFT JOIN Emprunt E ON A.id = E.adherent_id " +
                "    AND datetime(E.dateRetourPrevue / 1000, 'unixepoch') < datetime('now') " +
                "    AND E.dateRetourReelle IS NULL " +
                "GROUP BY A.id, A.nom, A.prenom"
            );
        System.out.println("Vue 'adherent_view' créée avec succès");


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