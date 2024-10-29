package Controllers;

import Modele.Joueur;
import Modele.Partie;
import Views.AllPartiesByIdView;
import Views.JeuView;
import Views.ListeJoueursView;
import Views.MenuView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuController {
    private MenuView view;

    public MenuController(MenuView view) {
        this.view = view;

        this.view.getNouvPartieBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Joueur nouvJoueur = new Joueur();
                Partie nouvPartie = new Partie();
                nouvPartie.initiateNouvellePartieInvite(4,12,9);
                view.setVisible(false);
                JeuView vueJeu = new JeuView(nouvPartie, nouvJoueur);
                JeuController jeu = JeuController.getInstance();
                jeu.setPartieEnCours(nouvPartie);
            }
        });

        this.view.getListePartieBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Initialiser la vue des parties
                AllPartiesByIdView allPartiesByIdView = new AllPartiesByIdView();

                // Initialiser le contrôleur des parties
                AllPartiesByIdController allPartiesByIdController = new AllPartiesByIdController(allPartiesByIdView);

                // Activer la vue
                allPartiesByIdView.initializeView();

                // Afficher la vue
                allPartiesByIdView.setVisible(true);

                // Masquer la vue du menu (optionnel selon votre logique)
                view.setVisible(false);
            }
        });

        this.view.getListeJoueursBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListeJoueursView vueListeJoueurs = new ListeJoueursView();
                new ListeJoueursController(vueListeJoueurs);
                view.dispose();
            }
        });

        this.view.getQuitterAppBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int reponse = JOptionPane.showConfirmDialog(
                        null,
                        "Voulez-vous quitter l'application ?",
                        "Quitter l'application",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if(reponse == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }
}
