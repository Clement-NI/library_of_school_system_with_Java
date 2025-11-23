package com.app;

import com.Controller.BibliotequeManager;
import com.View.*;
import javax.swing.*;

/**
 * Classe principale de l'application
 * Lance l'interface graphique Swing
 */
public class Main {
    public static void main(String[] args) {
        // Lancer l'interface sur le thread de dispatch Swing
        SwingUtilities.invokeLater(() -> {
            // Créer le gestionnaire
            BibliotequeManager manager = new BibliotequeManager();

            // Créer et afficher la fenêtre principale
            Mainframe frame = new Mainframe(manager);
            frame.setVisible(true);
        });
    }
}