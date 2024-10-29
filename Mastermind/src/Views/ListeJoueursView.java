package Views;

import Controllers.ListeJoueursController;
import Modele.Joueur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ListeJoueursView extends JFrame {
    private JPanel listeJoueursPanel;
    private JLabel listeTitre;
    private JTable listeTable;
    private JScrollPane listeScroll;
    private JPanel boutonsPanel;
    private JButton boutonRetourBtn;
    private JButton boutonSelectBtn;
    private JButton boutonNouvJoueurBtn;
    private String[] colonnesNoms;
    private Object[][] donneesTableau;
    private Joueur lesJoueurs;

    public ListeJoueursView() {
        setTitle("Mastermind - Liste des joueurs");
        setSize(530, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        lesJoueurs = new Joueur();
        ArrayList<HashMap<String, Object>> listeDesJoueursDB = lesJoueurs.getAllJoueurInDb();

        colonnesNoms = new String[]{"ID", "Nom"};
        donneesTableau = convertData(listeDesJoueursDB);

        listeJoueursPanel = new JPanel();
        listeJoueursPanel.setLayout(new BorderLayout());
        add(listeJoueursPanel);

        listeTitre = new JLabel("Liste des joueurs");
        listeTitre.setHorizontalAlignment(JLabel.CENTER);
        listeTitre.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        listeTitre.setFont(new Font("Arial", Font.PLAIN, 20));
        listeJoueursPanel.add(listeTitre, BorderLayout.NORTH);

        listeTable = new JTable(donneesTableau, colonnesNoms);
        listeTable.setDefaultEditor(Object.class, null);
        listeScroll = new JScrollPane(listeTable);
        listeJoueursPanel.add(listeScroll, BorderLayout.CENTER);

        // Panel pour les boutons
        JPanel boutonsPanel = new JPanel();
        boutonsPanel.setLayout(new BorderLayout());

        // Créer un sous-panel pour le centre
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        boutonNouvJoueurBtn = new JButton("Nouveau joueur");
        centerPanel.add(boutonNouvJoueurBtn);
        boutonsPanel.add(centerPanel, BorderLayout.CENTER);

        // Créer un sous-panel pour la gauche
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        boutonSelectBtn = new JButton("Sélectionner le joueur");
        leftPanel.add(boutonSelectBtn);
        boutonsPanel.add(leftPanel, BorderLayout.WEST);

        // Créer un sous-panel pour la droite
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        boutonRetourBtn = new JButton("Retour au menu");
        rightPanel.add(boutonRetourBtn);
        boutonsPanel.add(rightPanel, BorderLayout.EAST);

        listeJoueursPanel.add(boutonsPanel, BorderLayout.SOUTH);

        setVisible(true);
    }


    public JTable getListeTable() {
        return listeTable;
    }

    public JButton getBoutonSelectBtn() {
        return boutonSelectBtn;
    }

    public JButton getBoutonNouvJoueurBtn() {
        return boutonNouvJoueurBtn;
    }

    public JButton getBoutonRetourBtn() {
        return boutonRetourBtn;
    }

    public void addJoueur() {
        JTextField nouvJoueurNom = new JTextField();

        Object[] messageFormulaire = {
                "Votre nom : ", nouvJoueurNom
        };

        int reponse = JOptionPane.showConfirmDialog(null, messageFormulaire, "Nouveau joueur", JOptionPane.OK_CANCEL_OPTION);

        if(reponse == JOptionPane.OK_OPTION) {
            String nomJoueur = nouvJoueurNom.getText();

            if (nomJoueur.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vous avez laissé un champ vide.", "Avertissement", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ListeJoueursController controller = new ListeJoueursController();
            controller.addJoueurController(nomJoueur);
            refreshTableData();
        }
    }

    private void refreshTableData() {
        ArrayList<HashMap<String, Object>> updatedListeDesJoueursDB = lesJoueurs.getAllJoueurInDb();
        donneesTableau = convertData(updatedListeDesJoueursDB);
        listeTable.setModel(new DefaultTableModel(donneesTableau, colonnesNoms));
    }

    private Object[][] convertData(ArrayList<HashMap<String, Object>> listeDesJoueurs) {
        int rowCount = listeDesJoueurs.size();
        int columnCount = 2;
        Object[][] data = new Object[rowCount][columnCount];

        for(int i = 0; i < rowCount; i++) {
            HashMap<String, Object> row = listeDesJoueurs.get(i);
            data[i][0] = row.get("id_joueur");
            data[i][1] = row.get("nom_joueur");
        }

        return data;
    }
}

