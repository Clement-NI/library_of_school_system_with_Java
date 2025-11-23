package com.View;



import com.Controller.BibliotequeManager;
import com.Modele.Magazine;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panneau de gestion des magazines
 */
public class MagazinePanel extends JPanel {
    private BibliotequeManager manager;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nomField;
    private JTextField descriptionField;
    private JTextField numeroField;
    private JComboBox<String> perioditeCombo;
    private JTextField searchField;

    public MagazinePanel(BibliotequeManager manager) {
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
        panel.setBorder(BorderFactory.createTitledBorder("Ajouter un magazine"));
        panel.setPreferredSize(new Dimension(0, 120));

        panel.add(new JLabel("Nom:"));
        nomField = new JTextField();
        panel.add(nomField);

        panel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        panel.add(descriptionField);

        panel.add(new JLabel("Numéro:"));
        numeroField = new JTextField();
        panel.add(numeroField);

        panel.add(new JLabel("Périodicité:"));
        perioditeCombo = new JComboBox<>(new String[]{"SEMAINE", "HEBDOMATAIRE", "MENSUEL", "ANNUEL"});
        panel.add(perioditeCombo);

        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(e -> ajouterMagazine());
        panel.add(addButton);

        JButton clearButton = new JButton("Effacer");
        clearButton.addActionListener(e -> {
            nomField.setText("");
            descriptionField.setText("");
            numeroField.setText("");
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
        searchButton.addActionListener(e -> rechercherMagazine());
        searchPanel.add(searchButton);
        JButton showAllButton = new JButton("Afficher tous");
        showAllButton.addActionListener(e -> refreshTable());
        searchPanel.add(showAllButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        // Tableau
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Nom", "Description", "Numéro", "Périodicité"}, 0
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
        deleteButton.addActionListener(e -> supprimerMagazine());
        panel.add(deleteButton);

        JButton refreshButton = new JButton("Rafraîchir");
        refreshButton.addActionListener(e -> refreshTable());
        panel.add(refreshButton);

        JButton statsButton = new JButton("Statistiques");
        statsButton.addActionListener(e -> afficherStats());
        panel.add(statsButton);

        return panel;
    }

    private void ajouterMagazine() {
        String nom = nomField.getText().trim();
        String description = descriptionField.getText().trim();
        String numeroStr = numeroField.getText().trim();
        String periodite = (String) perioditeCombo.getSelectedItem();

        if (nom.isEmpty() || numeroStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int numero = Integer.parseInt(numeroStr);
            manager.ajouterMagazine(nom, description, numero, periodite);
            nomField.setText("");
            descriptionField.setText("");
            numeroField.setText("");
            refreshTable();
            JOptionPane.showMessageDialog(this, "Magazine ajouté avec succès",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Le numéro doit être un nombre",
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerMagazine() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un magazine",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer ce magazine?",
            "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            manager.supprimerMagazine(id);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Magazine supprimé avec succès",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void rechercherMagazine() {
        String nom = searchField.getText().trim();
        if (nom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un nom",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<Magazine> magazines = manager.rechercherMagazineParNom(nom);
        afficherTableau(magazines);
    }

    private void refreshTable() {
        ArrayList<Magazine> magazines = manager.obtenirTousLesMagazines();
        afficherTableau(magazines);
    }

    private void afficherTableau(ArrayList<Magazine> magazines) {
        tableModel.setRowCount(0);
        for (Magazine m : magazines) {
            tableModel.addRow(new Object[]{
                m.getId(),
                m.getNom(),
                m.getDescription(),
                m.getNumero(),
                m.getPeriodite()
            });
        }
    }

    private void afficherStats() {
        int total = manager.compterMagazines();
        String message = "Nombre total de magazines: " + total;
        JOptionPane.showMessageDialog(this, message,
            "Statistiques", JOptionPane.INFORMATION_MESSAGE);
    }
}




