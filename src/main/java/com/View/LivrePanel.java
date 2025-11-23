package com.View;

import com.Controller.BibliotequeManager;
import com.Modele.Livre;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panneau de gestion des livres
 */
public class LivrePanel extends JPanel {
    private BibliotequeManager manager;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nomField;
    private JTextField descriptionField;
    private JTextField isbnField;
    private JTextField nbPagesField;
    private JTextField searchField;

    public LivrePanel(BibliotequeManager manager) {
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
        JPanel panel = new JPanel(new GridLayout(3, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Ajouter un livre"));
        panel.setPreferredSize(new Dimension(0, 120));

        panel.add(new JLabel("Nom:"));
        nomField = new JTextField();
        panel.add(nomField);

        panel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        panel.add(descriptionField);

        panel.add(new JLabel("ISBN:"));
        isbnField = new JTextField();
        panel.add(isbnField);

        panel.add(new JLabel("Nombre de pages:"));
        nbPagesField = new JTextField();
        panel.add(nbPagesField);

        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(e -> ajouterLivre());
        panel.add(addButton);

        JButton clearButton = new JButton("Effacer");
        clearButton.addActionListener(e -> {
            nomField.setText("");
            descriptionField.setText("");
            isbnField.setText("");
            nbPagesField.setText("");
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
        searchButton.addActionListener(e -> rechercherLivre());
        searchPanel.add(searchButton);
        JButton showAllButton = new JButton("Afficher tous");
        showAllButton.addActionListener(e -> refreshTable());
        searchPanel.add(showAllButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        // Tableau
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Nom", "Description", "ISBN", "Pages"}, 0
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
        deleteButton.addActionListener(e -> supprimerLivre());
        panel.add(deleteButton);

        JButton refreshButton = new JButton("Rafraîchir");
        refreshButton.addActionListener(e -> refreshTable());
        panel.add(refreshButton);

        JButton statsButton = new JButton("Statistiques");
        statsButton.addActionListener(e -> afficherStats());
        panel.add(statsButton);

        return panel;
    }

    private void ajouterLivre() {
        String nom = nomField.getText().trim();
        String description = descriptionField.getText().trim();
        String isbn = isbnField.getText().trim();
        String nbPagesStr = nbPagesField.getText().trim();

        if (nom.isEmpty() || isbn.isEmpty() || nbPagesStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int nbPages = Integer.parseInt(nbPagesStr);
            manager.ajouterLivre(nom, description, isbn, nbPages);
            nomField.setText("");
            descriptionField.setText("");
            isbnField.setText("");
            nbPagesField.setText("");
            refreshTable();
            JOptionPane.showMessageDialog(this, "Livre ajouté avec succès",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Le nombre de pages doit être un nombre",
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerLivre() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un livre",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer ce livre?",
            "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            manager.supprimerLivre(id);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Livre supprimé avec succès",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void rechercherLivre() {
        String nom = searchField.getText().trim();
        if (nom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un nom",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<Livre> livres = manager.rechercherLivreParNom(nom);
        afficherTableau(livres);
    }

    private void refreshTable() {
        ArrayList<Livre> livres = manager.obtenirTousLesLivres();
        afficherTableau(livres);
    }

    private void afficherTableau(ArrayList<Livre> livres) {
        tableModel.setRowCount(0);
        for (Livre l : livres) {
            tableModel.addRow(new Object[]{
                l.getId(),
                l.getNom(),
                l.getDescription(),
                l.getISBN(),
                l.getNbpages()
            });
        }
    }

    private void afficherStats() {
        int total = manager.compterLivres();
        String message = "Nombre total de livres: " + total;
        JOptionPane.showMessageDialog(this, message,
            "Statistiques", JOptionPane.INFORMATION_MESSAGE);
    }
}
