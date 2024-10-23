package Views;

import Controllers.JeuController;
import Modele.Joueur;
import Modele.Partie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuView extends JFrame {
    private JButton nouvPartieBtn;
    private JButton listePartieBtn;
    private JButton quitterAppBtn;

    public MenuView() {
        setTitle("Mastermind - Menu principal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Titre
        JLabel menuTitle = new JLabel("Mastermind", SwingConstants.CENTER);
        menuTitle.setFont(new Font("Arial", Font.BOLD, 24));
        add(menuTitle, BorderLayout.NORTH);

        // Panneau principal des boutons
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(3, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Boutons
        nouvPartieBtn = new JButton("Nouvelle partie en tant qu'invité");
        listePartieBtn = new JButton("Liste des parties");
        quitterAppBtn = new JButton("Quitter");

        menuPanel.add(nouvPartieBtn);
        menuPanel.add(listePartieBtn);
        menuPanel.add(quitterAppBtn);

        add(menuPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public JButton getQuitterAppBtn() {
        return quitterAppBtn;
    }

    public JButton getNouvPartieBtn() {
        return nouvPartieBtn;
    }

    public JButton getListePartieBtn() {
        return listePartieBtn;
    }
}
