package Controllers;

import Modele.Joueur;
import Modele.Partie;
import Views.JeuView;
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
                String nomJoueur = view.getInputJoueur();
                nouvJoueur.createJoueurInDB(nomJoueur);
                Partie nouvPartie = new Partie();
                JeuView vueJeu = new JeuView();
                JeuController jeu = new JeuController(nouvJoueur, nouvPartie, vueJeu);
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