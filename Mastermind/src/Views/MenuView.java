package Views;

import Controllers.JeuController;
import Modele.Joueur;
import Modele.Partie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuView extends JFrame {
    private String nomJoueur;
    private JTextField inputJoueur;
    private JButton nouvPartieBtn;
    private JScrollPane listeJoueurs;
    private JButton quitterAppBtn;

    public MenuView() {
        setTitle("Mastermind - Menu principal");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel menuPanel = new JPanel(new BorderLayout(10,10));
        JLabel menuTitle = new JLabel("Mastermind");
        menuTitle.setFont(new Font("Arial", Font.PLAIN, 20));
        menuTitle.setHorizontalAlignment(SwingConstants.CENTER);

        nouvPartieBtn = new JButton("Nouvelle partie");

        inputJoueur = new JTextField("Votre nom : ", 20);
        inputJoueur.setHorizontalAlignment(JTextField.CENTER);

        quitterAppBtn = new JButton("Quitter le logiciel");

        menuPanel.add(menuTitle, BorderLayout.NORTH);
        menuPanel.add(nouvPartieBtn, BorderLayout.CENTER);
        menuPanel.add(inputJoueur, BorderLayout.SOUTH);

        add(menuPanel, BorderLayout.CENTER);
        add(quitterAppBtn, BorderLayout.SOUTH);

        nouvPartieBtn.addActionListener(e -> lancerNouvPartie(nomJoueur));

        setVisible(true);
    }

    public JButton getQuitterAppBtn() {
        return quitterAppBtn;
    }

    public JButton getNouvPartieBtn() {
        return nouvPartieBtn;
    }

    public String getInputJoueur() {
        return inputJoueur.getText();
    }

    private void lancerNouvPartie(String nomJoueur) {
        Joueur nouvJoueur = new Joueur();
        nomJoueur = inputJoueur.getText();
        nouvJoueur.createJoueurInDB(nomJoueur);
        Partie nouvPartie = new Partie();
        JeuView vueJeu = new JeuView();
        JeuController jeu = new JeuController(nouvJoueur, nouvPartie, vueJeu);
    }
}
