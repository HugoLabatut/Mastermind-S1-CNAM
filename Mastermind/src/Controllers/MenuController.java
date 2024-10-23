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
                Partie nouvPartie = new Partie();
                nouvPartie.initiateNouvellePartieInvite(4,12,8);
                JeuView vueJeu = new JeuView(nouvPartie, nouvJoueur);
                JeuController jeu = new JeuController(vueJeu);
            }
        });

        this.view.getListePartieBtn().addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               // ListePartieView listepartie = new ListePartieView();
               // new ListePartieController(listepartie);
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
