package Views;

import Controllers.AllPartiesByIdController;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class AllPartiesByIdView extends JFrame {
    private JTable partiesTable;
    private JLabel playerNameLabel = new JLabel();
    private JLabel titleLabel = new JLabel();
    private Object[][] tableauParties;

    public AllPartiesByIdView() {}

    public void initializeView() {
        tableauParties = AllPartiesByIdController.recupererPartiesJoueur();
        setTitle("Toutes les parties");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        titleLabel.setText("Parties de " + playerNameLabel.getText());
        playerNameLabel.setText("Nom du Joueur : " + playerNameLabel.getText());

        afficherPartiesJoueur(tableauParties);
    }

    public void afficherPartiesJoueur(Object[][] tableauParties) {
        String[] columnNames = {"ID Partie", "État", "Coups Max", "Action"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Seule la colonne des boutons est éditable pour activer le clic
            }
        };

        for (Object[] partie : tableauParties) {
            int idPartie = (int) partie[1];
            int etat = (int) partie[4];
            int nbcoupsPartie = (int) partie[2];
            String etatPartie = obtenirEtatPartie(etat);

            tableModel.addRow(new Object[]{idPartie, etatPartie, nbcoupsPartie, "Voir la partie"});
        }

        partiesTable = new JTable(tableModel);
        partiesTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        partiesTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox())); // Utiliser un JCheckBox pour activer le bouton sans édition

        // Centrer les cellules de données
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < partiesTable.getColumnCount() - 1; i++) { // Éviter la dernière colonne (boutons)
            partiesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(partiesTable);
        add(scrollPane);
    }

    private String obtenirEtatPartie(int etat) {
        switch (etat) {
            case 0: return "En attente";
            case 1: return "Gagné";
            case 2: return "Perdu";
            default: return "Inconnu";
        }
    }

    // Classe pour afficher le bouton sans permettre l'édition de son texte
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setText("Voir la partie"); // Texte fixe pour le bouton
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Classe pour gérer l'action du bouton sans permettre de modifier le texte
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean clicked;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Voir la partie");
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped(); // Arrêter l'édition pour que le bouton réagisse
                    AllPartiesByIdController.afficherPartie((int) tableauParties[currentRow][1]); // ID de la partie
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row; // Récupérer l'index de la ligne actuelle pour l'action
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            clicked = false;
            return "Voir la partie";
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
}
