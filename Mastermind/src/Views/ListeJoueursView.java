package Views;

import Modele.Joueur;

import javax.swing.*;
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
    private JButton boutonNouvPartieBtn;
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
        listeTitre.setHorizontalAlignment(JLabel.LEFT);
        listeTitre.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        listeJoueursPanel.add(listeTitre, BorderLayout.NORTH);

        listeTable = new JTable(donneesTableau, colonnesNoms);
        listeScroll = new JScrollPane(listeTable);
        listeJoueursPanel.add(listeScroll, BorderLayout.CENTER);

        boutonsPanel = new JPanel();
        boutonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        boutonRetourBtn = new JButton("Retour au menu");
        boutonNouvPartieBtn = new JButton("Nouvelle partie");
        boutonSelectBtn = new JButton("Sélectionner le joueur");
        boutonsPanel.add(boutonRetourBtn);
        boutonsPanel.add(boutonNouvPartieBtn);
        boutonsPanel.add(boutonSelectBtn);
        listeJoueursPanel.add(boutonsPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public JTable getListeTable() {
        return listeTable;
    }

    public JButton getBoutonSelectBtn() {
        return boutonSelectBtn;
    }

    public JButton getBoutonNouvPartieBtn() {
        return boutonNouvPartieBtn;
    }

    public JButton getBoutonRetourBtn() {
        return boutonRetourBtn;
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

