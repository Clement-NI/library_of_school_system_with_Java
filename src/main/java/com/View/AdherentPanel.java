package com.View;

import com.Controller.BibliotequeManager;
import com.Modele.Adherent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panneau de gestion des adhérents
 */
public class AdherentPanel extends JPanel {
    private BibliotequeManager manager;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField searchField;

    public AdherentPanel(BibliotequeManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());

        // Panneau supérieur pour les entrées
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Panneau central pour le tableau
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // Panneau inférieur pour les boutons
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        // Charger les données
        refreshTable();
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Ajouter un adhérent"));
        panel.setPreferredSize(new Dimension(0, 120));

        panel.add(new JLabel("Nom:"));
        nomField = new JTextField();
        panel.add(nomField);

        panel.add(new JLabel("Prénom:"));
        prenomField = new JTextField();
        panel.add(prenomField);

        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(e -> ajouterAdherent());
        panel.add(addButton);

        JButton clearButton = new JButton("Effacer");
        clearButton.addActionListener(e -> {
            nomField.setText("");
            prenomField.setText("");
        });
        panel.add(clearButton);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panneau de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Rechercher:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> rechercherAdherent());
        searchPanel.add(searchButton);
        JButton showAllButton = new JButton("Afficher tous");
        showAllButton.addActionListener(e -> refreshTable());
        searchPanel.add(showAllButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        // Tableau
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Nom", "Prénom", "Pénalité"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(e -> supprimerAdherent());
        panel.add(deleteButton);

        JButton refreshButton = new JButton("Rafraîchir");
        refreshButton.addActionListener(e -> refreshTable());
        panel.add(refreshButton);

        JButton statsButton = new JButton("Statistiques");
        statsButton.addActionListener(e -> afficherStats());
        panel.add(statsButton);

        return panel;
    }

    private void ajouterAdherent() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        manager.inscriptionAdherent(nom, prenom);
        nomField.setText("");
        prenomField.setText("");
        refreshTable();
        JOptionPane.showMessageDialog(this, "Adhérent ajouté avec succès",
            "Succès", JOptionPane.INFORMATION_MESSAGE);
    }

    private void supprimerAdherent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un adhérent",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer cet adhérent?",
            "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            manager.supprimerAdherent(id);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Adhérent supprimé avec succès",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void rechercherAdherent() {
        String nom = searchField.getText().trim();
        if (nom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un nom",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<Adherent> adherents = manager.rechercherAdherentParNom(nom);
        afficherTableau(adherents);
    }

    private void refreshTable() {
        ArrayList<Adherent> adherents = manager.obtenirTousLesAdherents();
        afficherTableau(adherents);
    }

    private void afficherTableau(ArrayList<Adherent> adherents) {
        tableModel.setRowCount(0);
        for (Adherent a : adherents) {
            tableModel.addRow(new Object[]{
                a.getID(),
                a.getNom(),
                a.getPrenom(),
                a.getStatutPenalite() ? "Oui" : "Non"
            });
        }
    }

    private void afficherStats() {
        int total = 1;
        String message = "Nombre total d'adhérents: " + total;
        JOptionPane.showMessageDialog(this, message,
            "Statistiques", JOptionPane.INFORMATION_MESSAGE);
    }
}




