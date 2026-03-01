package com.View;

import javax.swing.*;
import com.Controller.BibliotequeManager;

import java.awt.*;

public class Mainframe extends JFrame {
    private BibliotequeManager manager;
    private JTabbedPane tabbedPane;

    public Mainframe(BibliotequeManager manager) throws HeadlessException {
        this.manager = manager;
         // Configuration de la fenêtre
        setTitle("Gestion de Bibliothèque");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // Créer les onglets
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Adhérents", new AdherentPanel(manager));
        tabbedPane.addTab("Documents",new DocumentPanel(manager));
//        tabbedPane.addTab("Livres", new LivrePanel(manager));
//        tabbedPane.addTab("Magazines", new MagazinePanel(manager));
        tabbedPane.addTab("Emprunts", new EmpruntPanel(manager));

        add(tabbedPane);
    }
}
