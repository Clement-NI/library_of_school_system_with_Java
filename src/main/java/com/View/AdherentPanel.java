package com.View;

import com.Controller.BibliotequeManager;
import com.Modele.Adherent;
import com.Modele.Emprunt;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
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
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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

        JButton modifyButton = new JButton("Modifier");
        modifyButton.addActionListener(e-> modifyAdherent());
        panel.add(modifyButton);

        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(e -> supprimerAdherent());
        panel.add(deleteButton);

        JButton histoButton = new JButton("Historique d'emprunt");
        histoButton.addActionListener(e->showHistorique());
        panel.add(histoButton);


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

    private void modifyAdherent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un adhérent",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String nomActuel = (String) tableModel.getValueAt(selectedRow, 1);
        String prenomActuel = (String) tableModel.getValueAt(selectedRow, 2);

        JTextField nomField = new JTextField(nomActuel);
        JTextField prenomField = new JTextField(prenomActuel);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Nom:"));
        panel.add(nomField);
        panel.add(new JLabel("Prénom:"));
        panel.add(prenomField);



        int result = JOptionPane.showConfirmDialog(this, panel,
            "Modifier l'adhérent", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String nouveauNom = nomField.getText().trim();
            String nouveauPrenom = prenomField.getText().trim();


            if (nouveauNom.isEmpty() || nouveauPrenom.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le nom et le prénom ne peuvent pas être vides",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            manager.modifierAdherent(id, nouveauNom, nouveauPrenom);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Adhérent modifié avec succès",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

        }
    }

    private void showHistorique(){
            // Vérifier qu'un adhérent est sélectionné
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un adhérent",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Récupérer l'ID de l'adhérent sélectionné
            int id = (int) tableModel.getValueAt(selectedRow, 0);

            // Obtenir les emprunts de l'adhérent
            ArrayList<Emprunt> emprunts = manager.obtenirEmpruntsAdherent(id);

            // Créer un nouveau modèle de table pour l'historique
            DefaultTableModel historiqueTableModel = new DefaultTableModel(
                new String[]{"ID Emprunt", "Document", "Date Emprunt", "Date Retour Prevue", "Date Retour Réelle"}, 0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            // Remplir le modèle avec les données des emprunts
            for (Emprunt emprunt : emprunts) {
                if(emprunt.getDocument() == null){
                    continue;
                }

                String dateRetourReelle = emprunt.getDateRetourReelle() != null ?
                        dateFormat.format(emprunt.getDateRetourReelle()) : "N/A";
                historiqueTableModel.addRow(new Object[]{
                    emprunt.getID_Emprunt(),
                    emprunt.getDocument().getNom(),
                    dateFormat.format(emprunt.getDateEmprunt()),
                    dateFormat.format(emprunt.getDateRetourPrevue()),
                    dateRetourReelle
                });
            }

            // Créer une nouvelle table pour l'historique
            JTable historiqueTable = new JTable(historiqueTableModel);
            historiqueTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(historiqueTable);

            // Créer un panneau pour l'historique
            JPanel historiquePanel = new JPanel(new BorderLayout());
            historiquePanel.add(new JLabel("Historique d'emprunt de l'adhérent ID: " + id),
                                BorderLayout.NORTH);
            historiquePanel.add(scrollPane, BorderLayout.CENTER);

            // Créer un panneau pour le bouton (AVANT de créer le dialog)
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton retourButton = new JButton("Enregistrer retour");

            // Action du bouton avec accès à la table d'historique
            retourButton.addActionListener(e -> {
                int selectedHistoRow = historiqueTable.getSelectedRow();
                if (selectedHistoRow == -1) {
                    JOptionPane.showMessageDialog(historiquePanel,
                        "Veuillez sélectionner un emprunt dans l'historique",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Récupérer l'ID de l'emprunt sélectionné
                int idEmprunt = (int) historiqueTableModel.getValueAt(selectedHistoRow, 0);

                // Vérifier si le retour n'a pas déjà été enregistré
                String dateRetourReelle = (String) historiqueTableModel.getValueAt(selectedHistoRow, 4);
                if (!dateRetourReelle.equals("N/A")) {
                    JOptionPane.showMessageDialog(historiquePanel,
                        "Le retour de cet emprunt a déjà été enregistré le " + dateRetourReelle,
                        "Information", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                double penalite = manager.getPenaliteParEmprunt(idEmprunt);

                // Confirmer l'action
                int confirm = JOptionPane.showConfirmDialog(historiquePanel,
                    "L'adherent doit payer une penalite de " + penalite +" euros.\nVoulez-vous enregistrer le retour de cet emprunt?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Appeler la méthode du manager pour enregistrer le retour
                    manager.enregistrerRetour(idEmprunt);

                    JOptionPane.showMessageDialog(historiquePanel,
                        "Retour enregistré avec succès",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);

                    // Rafraîchir l'historique
                    historiqueTableModel.setValueAt(dateFormat.format(new java.util.Date()),
                                                   selectedHistoRow, 4);

                    // Rafraîchir le tableau principal des adhérents
                    refreshTable();
                }
            });

            buttonPanel.add(retourButton);
            historiquePanel.add(buttonPanel, BorderLayout.SOUTH);

            // Afficher dans une nouvelle fenêtre ou un dialogue
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                                          "Historique des emprunts", true);
            dialog.setContentPane(historiquePanel);
            dialog.setSize(700, 450);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
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
                a.getStatutPenalite()
            });
        }
    }

    private void afficherStats() {
        int total = manager.afficherLeNombredAdherents();
        String message = "Nombre total d'adhérents: " + total;
        JOptionPane.showMessageDialog(this, message,
            "Statistiques", JOptionPane.INFORMATION_MESSAGE);
    }


}




