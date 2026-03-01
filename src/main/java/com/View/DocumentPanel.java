package com.View;

import com.Controller.BibliotequeManager;
import com.Modele.Document;
import com.Modele.Livre;
import com.Modele.Magazine;
import com.Modele.Periodite;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class DocumentPanel extends JPanel {
    private BibliotequeManager manager;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> typeFilterCombo;

    public DocumentPanel(BibliotequeManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Recherche et filtres"));

        panel.add(new JLabel("Rechercher:"));
        searchField = new JTextField(20);
        panel.add(searchField);

        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> rechercherDocument());
        panel.add(searchButton);

        panel.add(new JLabel("Type:"));
        typeFilterCombo = new JComboBox<>(new String[]{"Tous", "Livre", "Magazine"});
        typeFilterCombo.addActionListener(e -> rechercherDocument());
        panel.add(typeFilterCombo);

        JButton showAllButton = new JButton("Afficher tous");
        showAllButton.addActionListener(e -> {
            searchField.setText("");
            typeFilterCombo.setSelectedIndex(0);
            refreshTable();
        });
        panel.add(showAllButton);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        tableModel = new DefaultTableModel(
            new String[]{"ID", "Type", "Nom", "Description", "Details"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    afficherDetails();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(e -> ajouterDocument());
        panel.add(addButton);

        JButton editButton = new JButton("Modifier");
        editButton.addActionListener(e -> modifierDocument());
        panel.add(editButton);

        JButton detailsButton = new JButton("Voir details");
        detailsButton.addActionListener(e -> afficherDetails());
        panel.add(detailsButton);

        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(e -> supprimerDocument());
        panel.add(deleteButton);

        JButton refreshButton = new JButton("Rafraichir");
        refreshButton.addActionListener(e -> refreshTable());
        panel.add(refreshButton);

        JButton statsButton = new JButton("Statistiques");
        statsButton.addActionListener(e -> afficherStats());
        panel.add(statsButton);

        return panel;
    }

    private void ajouterDocument() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            "Ajouter un document", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Nouveau Document");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Type de document:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Livre", "Magazine"});
        formPanel.add(typeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1;
        JTextField nomField = new JTextField(20);
        formPanel.add(nomField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        JTextArea descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        formPanel.add(descScroll, gbc);

        JPanel dynamicPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(dynamicPanel, gbc);

        JPanel livrePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcLivre = new GridBagConstraints();
        gbcLivre.fill = GridBagConstraints.HORIZONTAL;
        gbcLivre.insets = new Insets(5, 5, 5, 5);

        gbcLivre.gridx = 0; gbcLivre.gridy = 0;
        livrePanel.add(new JLabel("ISBN:"), gbcLivre);
        gbcLivre.gridx = 1;
        JTextField isbnField = new JTextField(20);
        livrePanel.add(isbnField, gbcLivre);

        gbcLivre.gridx = 0; gbcLivre.gridy = 1;
        livrePanel.add(new JLabel("Nombre de pages:"), gbcLivre);
        gbcLivre.gridx = 1;
        JSpinner nbPagesSpinner = new JSpinner(new SpinnerNumberModel(100, 1, 10000, 1));
        livrePanel.add(nbPagesSpinner, gbcLivre);

        JPanel magazinePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcMag = new GridBagConstraints();
        gbcMag.fill = GridBagConstraints.HORIZONTAL;
        gbcMag.insets = new Insets(5, 5, 5, 5);

        gbcMag.gridx = 0; gbcMag.gridy = 0;
        magazinePanel.add(new JLabel("Numero:"), gbcMag);
        gbcMag.gridx = 1;
        JSpinner numeroSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
        magazinePanel.add(numeroSpinner, gbcMag);

        gbcMag.gridx = 0; gbcMag.gridy = 1;
        magazinePanel.add(new JLabel("Periodicite:"), gbcMag);
        gbcMag.gridx = 1;
        JComboBox<String> perioditeCombo = new JComboBox<>(new String[]{"SEMAINE", "HEBDOMATAIRE", "MENSUEL", "ANNUEL"});
        magazinePanel.add(perioditeCombo, gbcMag);

        CardLayout cardLayout = new CardLayout();
        dynamicPanel.setLayout(cardLayout);
        dynamicPanel.add(livrePanel, "Livre");
        dynamicPanel.add(magazinePanel, "Magazine");

        typeCombo.addActionListener(e -> {
            String type = (String) typeCombo.getSelectedItem();
            cardLayout.show(dynamicPanel, type);
        });

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton saveButton = new JButton("Enregistrer");
        saveButton.addActionListener(e -> {
            String nom = nomField.getText().trim();
            String description = descriptionArea.getText().trim();
            String type = (String) typeCombo.getSelectedItem();

            if (nom.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Le nom est obligatoire",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                if (type.equals("Livre")) {
                    String isbn = isbnField.getText().trim();
                    int nbPages = (Integer) nbPagesSpinner.getValue();

                    if (isbn.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog,
                            "L ISBN est obligatoire",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    manager.ajouterDocument(nom, description, isbn, nbPages,"livre");
                } else {
                    int numero = (Integer) numeroSpinner.getValue();
                    String periodite = (String) perioditeCombo.getSelectedItem();

                    manager.ajouterDocument(nom, description, numero, periodite,"magazine");
                }

                refreshTable();
                dialog.dispose();
                JOptionPane.showMessageDialog(this,
                    "Document ajoute avec succes",
                    "Succes", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erreur lors de l ajout: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void modifierDocument() {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                    "Veuillez selectionner un document a modifier",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String type = (String) tableModel.getValueAt(selectedRow, 1);
            String nomActuel = (String) tableModel.getValueAt(selectedRow, 2);
            String descriptionActuelle = (String) tableModel.getValueAt(selectedRow, 3);

            Document doc = manager.recherchertouslesDocumentsParId(id);
            if (doc == null) {
                JOptionPane.showMessageDialog(this,
                    "Document introuvable",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Modifier le document", true);
            dialog.setLayout(new BorderLayout(10, 10));
            dialog.setSize(450, 350);
            dialog.setLocationRelativeTo(this);

            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            JLabel titleLabel = new JLabel("Modifier: " + nomActuel);
            titleLabel.setFont(new Font("Dialog", Font.BOLD, 16));
            mainPanel.add(titleLabel, BorderLayout.NORTH);

            JPanel formPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(new JLabel("Type:"), gbc);
            gbc.gridx = 1;
            JLabel typeLabel = new JLabel(type);
            formPanel.add(typeLabel, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            formPanel.add(new JLabel("Nom:"), gbc);
            gbc.gridx = 1;
            JTextField nomField = new JTextField(nomActuel, 20);
            formPanel.add(nomField, gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            formPanel.add(new JLabel("Description:"), gbc);
            gbc.gridx = 1;
            JTextArea descriptionArea = new JTextArea(descriptionActuelle, 3, 20);
            descriptionArea.setLineWrap(true);
            descriptionArea.setWrapStyleWord(true);
            JScrollPane descScroll = new JScrollPane(descriptionArea);
            formPanel.add(descScroll, gbc);

            JPanel specificPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbcSpec = new GridBagConstraints();
            gbcSpec.fill = GridBagConstraints.HORIZONTAL;
            gbcSpec.insets = new Insets(5, 5, 5, 5);

            JTextField isbnField = null;
            JSpinner nbPagesSpinner = null;
            JSpinner numeroSpinner = null;
            JComboBox<String> perioditeCombo = null;

            if (doc.getType_de_document().equals("livre")) {
                Livre livre = (Livre) doc;

                gbcSpec.gridx = 0; gbcSpec.gridy = 0;
                specificPanel.add(new JLabel("ISBN:"), gbcSpec);
                gbcSpec.gridx = 1;
                isbnField = new JTextField(livre.getISBN(), 20);
                specificPanel.add(isbnField, gbcSpec);

                gbcSpec.gridx = 0; gbcSpec.gridy = 1;
                specificPanel.add(new JLabel("Nombre de pages:"), gbcSpec);
                gbcSpec.gridx = 1;

                int nbPages = livre.getNbpages();
                if (nbPages < 1 || nbPages > 10000) {
                    nbPages = 100;
                }

                try {
                    nbPagesSpinner = new JSpinner(new SpinnerNumberModel(nbPages, 1, 10000, 1));
                    specificPanel.add(nbPagesSpinner, gbcSpec);
                } catch (IllegalArgumentException ex) {
                    nbPagesSpinner = new JSpinner(new SpinnerNumberModel(100, 1, 10000, 1));
                    specificPanel.add(nbPagesSpinner, gbcSpec);
                }

            } else{
                Magazine magazine = (Magazine) doc;

                gbcSpec.gridx = 0; gbcSpec.gridy = 0;
                specificPanel.add(new JLabel("Numero:"), gbcSpec);
                gbcSpec.gridx = 1;

                int numero = magazine.getNumero();
                if (numero < 1 || numero > 9999) {
                    numero = 1;
                }

                try {
                    numeroSpinner = new JSpinner(new SpinnerNumberModel(numero, 1, 9999, 1));
                    specificPanel.add(numeroSpinner, gbcSpec);
                } catch (IllegalArgumentException ex) {
                    numeroSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
                    specificPanel.add(numeroSpinner, gbcSpec);
                }

                gbcSpec.gridx = 0; gbcSpec.gridy = 1;
                specificPanel.add(new JLabel("Periodicite:"), gbcSpec);
                gbcSpec.gridx = 1;
                perioditeCombo = new JComboBox<>(new String[]{"SEMAINE", "HEBDOMATAIRE", "MENSUEL", "ANNUEL"});

                Periodite perioditeActuelle = magazine.getPeriodite();
                if (perioditeActuelle != null) {
                    String perioditeStr = convertirEnumVersString(perioditeActuelle);
                    perioditeCombo.setSelectedItem(perioditeStr);
                }
                specificPanel.add(perioditeCombo, gbcSpec);
            }

            gbc.gridx = 0; gbc.gridy = 3;
            gbc.gridwidth = 2;
            formPanel.add(specificPanel, gbc);

            mainPanel.add(formPanel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

            JTextField finalIsbnField = isbnField;
            JSpinner finalNbPagesSpinner = nbPagesSpinner;
            JSpinner finalNumeroSpinner = numeroSpinner;
            JComboBox<String> finalPerioditeCombo = perioditeCombo;

            JButton saveButton = new JButton("Enregistrer");
            saveButton.addActionListener(e -> {
                String nom = nomField.getText().trim();
                String description = descriptionArea.getText().trim();

                if (nom.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                        "Le nom est obligatoire",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    if (doc instanceof Livre) {
                        String isbn = finalIsbnField.getText().trim();
                        int nbPages = (Integer) finalNbPagesSpinner.getValue();

                        if (isbn.isEmpty()) {
                            JOptionPane.showMessageDialog(dialog,
                                "L ISBN est obligatoire",
                                "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        manager.modifierLivre(new Livre(id, nom, description, isbn, nbPages));
                    } else if (doc instanceof Magazine) {
                        int numero = (Integer) finalNumeroSpinner.getValue();
                        String perioditeStr = (String) finalPerioditeCombo.getSelectedItem();

                        Periodite periodite = Periodite.valueOf(perioditeStr);

                        manager.modifierMagazine(new Magazine(id, nom, description, numero, periodite));
                    }

                    refreshTable();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this,
                        "Document modifie avec succes",
                        "Succes", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Erreur lors de la modification: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });

            JButton cancelButton = new JButton("Annuler");
            cancelButton.addActionListener(e -> dialog.dispose());

            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            dialog.add(mainPanel);
            dialog.setVisible(true);
}

private String convertirEnumVersString(Periodite periodite) {
    if (periodite == null) {
        return "Mensuel";
    }
    switch (periodite) {
        case HEBDOMATAIRE:
            return "Hebdomadaire";
        case MENSUEL:
            return "Mensuel";
        case SEMAINE:
            return "Semaine";
        case ANNUEL:
            return "Annuel";
        default:
            return "Mensuel";
    }
}


    private void rechercherDocument() {
        String keyword = searchField.getText().trim();
        String type_constraint = typeFilterCombo.getSelectedItem().toString();

        if (keyword.isEmpty()) {
            filtrerParType();
            return;
        }

        ArrayList<Document> documents;

        if (type_constraint.equals("Tous")) {
            documents = manager.recherchertouslesDocumentsParNom(keyword);
        } else if (type_constraint.equals("Livre")) {
            documents = (ArrayList<Document>)(ArrayList<?>) manager.rechercherLivreParNom(keyword);
        } else {
            documents = (ArrayList<Document>)(ArrayList<?>) manager.rechercherMagazineParNom(keyword);
        }

        afficherTableau(documents);

        if (documents.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Aucun document trouve pour: " + keyword,
                "Resultat", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void filtrerParType() {
        String type = (String) typeFilterCombo.getSelectedItem();
        ArrayList<Document> documents = new ArrayList<>();

        switch (type) {
            case "Tous":
                documents.addAll(manager.obtenirtouslesDocuments());
                break;
            case "Livre":
                documents.addAll(manager.obtenirTousLesLivres());
                break;
            case "Magazine":
                documents.addAll(manager.obtenirTousLesMagazines());
                break;
        }

        afficherTableau(documents);
    }

    private void supprimerDocument() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Veuillez selectionner un document",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String type = (String) tableModel.getValueAt(selectedRow, 1);
        String nom = (String) tableModel.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Etes vous sur de vouloir supprimer ce " + type.toLowerCase() + "\n" +
            "Nom: " + nom,
            "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            manager.supprimerDocument(id);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Document supprime avec succes",
                "Succes", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void afficherDetails() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez selectionner un document",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String type = (String) tableModel.getValueAt(selectedRow, 1);
        String nom = (String) tableModel.getValueAt(selectedRow, 2);
        String description = (String) tableModel.getValueAt(selectedRow, 3);
        String details = (String) tableModel.getValueAt(selectedRow, 4);

        String message = "Details du document\n\n" +
                        "ID: " + id + "\n" +
                        "Type: " + type + "\n" +
                        "Nom: " + nom + "\n" +
                        "Description: " + (description.isEmpty() ? "N/A" : description) + "\n" +
                        details;

        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 250));

        JOptionPane.showMessageDialog(this, scrollPane,
            "Details du document", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshTable() {
        ArrayList<Document> documents = new ArrayList<>();
        documents.addAll(manager.obtenirtouslesDocuments());
        afficherTableau(documents);
    }

    private void afficherTableau(ArrayList<Document> documents) {
        tableModel.setRowCount(0);

        for (Document doc : documents) {
            String type;
            String details;

            if (doc instanceof Livre) {
                Livre livre = (Livre) doc;
                type = "Livre";
                details = "ISBN: " + livre.getISBN() + ", Pages: " + livre.getNbpages();
            } else if (doc instanceof Magazine) {
                Magazine magazine = (Magazine) doc;
                type = "Magazine";
                details = "Numero " + magazine.getNumero() + ", " + magazine.getPeriodite();
            } else {
                type = doc.getType_de_document();
                details = "N/A";
            }

            tableModel.addRow(new Object[]{
                doc.getId(),
                type,
                doc.getNom(),
                doc.getDescription(),
                details
            });
        }
    }

    private void afficherStats() {
        int totalLivres = manager.compterLivres();
        int totalMagazines = manager.compterMagazines();
        int total = totalLivres + totalMagazines;

        String message = "Statistiques des documents\n\n" +
                        "Total documents: " + total + "\n" +
                        "Livres: " + totalLivres + "\n" +
                        "Magazines: " + totalMagazines + "\n\n" +
                        "Pourcentage de livres: " +
                        (total > 0 ? String.format("%.1f", (totalLivres * 100.0 / total)) + "%" : "0%") + "\n" +
                        "Pourcentage de magazines: " +
                        (total > 0 ? String.format("%.1f", (totalMagazines * 100.0 / total)) + "%" : "0%");

        JOptionPane.showMessageDialog(this, message,
            "Statistiques", JOptionPane.INFORMATION_MESSAGE);
    }
}