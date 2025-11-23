package com.View;


import com.Controller.BibliotequeManager;
import com.Modele.Emprunt;
import com.Modele.Adherent;
import com.Modele.Document;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Panneau de gestion des emprunts
 */
public class EmpruntPanel extends JPanel {
    private BibliotequeManager manager;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField adherentIdField;
    private JTextField documentIdField;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public EmpruntPanel(BibliotequeManager manager) {
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
        JPanel panel = new JPanel(new GridLayout(2, 3, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Créer un emprunt"));
        panel.setPreferredSize(new Dimension(0, 100));

        panel.add(new JLabel("ID Adhérent:"));
        adherentIdField = new JTextField();
        panel.add(adherentIdField);

        panel.add(new JLabel("ID Document:"));
        documentIdField = new JTextField();
        panel.add(documentIdField);

        JButton createButton = new JButton("Créer emprunt");
        createButton.addActionListener(e -> creerEmprunt());
        panel.add(createButton);

        JButton clearButton = new JButton("Effacer");
        clearButton.addActionListener(e -> {
            adherentIdField.setText("");
            documentIdField.setText("");
        });
        panel.add(clearButton);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panneau de filtres
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton allButton = new JButton("Tous les emprunts");
        allButton.addActionListener(e -> afficherTousEmprunts());
        filterPanel.add(allButton);

        JButton nonRetournesButton = new JButton("Non retournés");
        nonRetournesButton.addActionListener(e -> afficherEmpruntsNonRetournes());
        filterPanel.add(nonRetournesButton);

        JButton enRetardButton = new JButton("En retard");
        enRetardButton.addActionListener(e -> afficherEmpruntsEnRetard());
        filterPanel.add(enRetardButton);

        panel.add(filterPanel, BorderLayout.NORTH);

        // Tableau
        tableModel = new DefaultTableModel(
            new String[]{"ID", "ID Adhérent", "Nom Adhérent", "Document", "Date Emprunt", "Retour Prévu", "Retour Réel"}, 0
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

        JButton retourButton = new JButton("Enregistrer retour");
        retourButton.addActionListener(e -> enregistrerRetour());
        panel.add(retourButton);

        JButton refreshButton = new JButton("Rafraîchir");
        refreshButton.addActionListener(e -> refreshTable());
        panel.add(refreshButton);

        JButton statsButton = new JButton("Statistiques");
        statsButton.addActionListener(e -> afficherStats());
        panel.add(statsButton);

        return panel;
    }

    private void creerEmprunt() {
        String adherentIdStr = adherentIdField.getText().trim();
        String documentIdStr = documentIdField.getText().trim();

        if (adherentIdStr.isEmpty() || documentIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int adherentId = Integer.parseInt(adherentIdStr);
            int documentId = Integer.parseInt(documentIdStr);

            // Créer une date de retour prévue (14 jours à partir d'aujourd'hui)
            Date dateRetourPrevue = new Date(System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000);

            Emprunt emprunt = manager.creerEmprunt(adherentId, documentId, dateRetourPrevue);

            if (emprunt != null) {
                adherentIdField.setText("");
                documentIdField.setText("");
                refreshTable();
                JOptionPane.showMessageDialog(this, "Emprunt créé avec succès",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la création de l'emprunt",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Les IDs doivent être des nombres",
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enregistrerRetour() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un emprunt",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int empruntId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir enregistrer le retour?",
            "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            manager.enregistrerRetour(empruntId);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Retour enregistré avec succès",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void afficherTousEmprunts() {
        ArrayList<Emprunt> emprunts = manager.obtenirTousLesEmprunts();
        afficherTableau(emprunts);
    }

    private void afficherEmpruntsNonRetournes() {
        ArrayList<Emprunt> emprunts = manager.obtenirEmpruntsNonRetournes();
        afficherTableau(emprunts);
    }

    private void afficherEmpruntsEnRetard() {
        ArrayList<Emprunt> emprunts = manager.obtenirEmpruntsEnRetard();
        afficherTableau(emprunts);
    }

    private void refreshTable() {
        afficherTousEmprunts();
    }

    private void afficherTableau(ArrayList<Emprunt> emprunts) {
        tableModel.setRowCount(0);
        for (Emprunt e : emprunts) {
            String dateRetourReelle = e.getDateRetourReelle() != null ?
                dateFormat.format(e.getDateRetourReelle()) : "N/A";

            tableModel.addRow(new Object[]{
                e.getID_Emprunt(),
                e.getAdherent().getID(),
                e.getAdherent().getNom() + " " + e.getAdherent().getPrenom(),
                e.getDocument().getNom(),
                dateFormat.format(e.getDateEmprunt()),
                dateFormat.format(e.getDateRetourPrevue()),
                dateRetourReelle
            });
        }
    }

    private void afficherStats() {
        int total = manager.compterEmprunts();
        int nonRetournes = manager.compterEmpruntsNonRetournes();
        int enRetard = manager.compterEmpruntsEnRetard();

        String message = "Total emprunts: " + total + "\n" +
                        "Non retournés: " + nonRetournes + "\n" +
                        "En retard: " + enRetard;

        JOptionPane.showMessageDialog(this, message,
            "Statistiques", JOptionPane.INFORMATION_MESSAGE);
    }
}