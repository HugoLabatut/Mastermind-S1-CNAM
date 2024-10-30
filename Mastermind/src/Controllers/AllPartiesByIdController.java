package Controllers;

import Modele.Partie;
import Modele.Joueur;
import Utils.UtilsMethods;
import Views.AllPartiesByIdView;
import Views.JeuView;
import Views.ListeJoueursView;
import Views.MenuView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class AllPartiesByIdController {

    private Partie partieModel;
    private Joueur joueurModel;
    private AllPartiesByIdView view;

    public AllPartiesByIdController(Joueur joueur) {
        this.partieModel = new Partie();
        this.joueurModel = joueur;
        this.view = new AllPartiesByIdView(joueurModel, partieModel);
        view.setController(this);

        // Activer la vue
        view.initializeView();

        // Afficher la vue
        view.setVisible(true);

        this.view.getBoutonRetourBtn().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view.dispose();
                ListeJoueursView  listeJoueursView  = new ListeJoueursView();
                new ListeJoueursController(listeJoueursView);
                listeJoueursView.setVisible(true);

            }
        });
    }

    // Méthode pour afficher les parties d'un joueur
    public Object[][] recupererPartiesJoueur() {
//        int idJoueur = joueurModel.getId();
        int idJoueur = joueurModel.getId();
        ArrayList<HashMap<String, Object>> parties = partieModel.readAllPartiesOfPlayerFromDb(idJoueur);
        // Mise à jour de la vue avec les données du joueur et des parties
        return UtilsMethods.convertirEnTableau(parties);
    }

    // Méthode pour afficher les détails de la partie spécifique
    public void afficherPartie(int partieId) {
        // Code pour rediriger ou afficher les détails de la partie avec l'ID spécifique
        partieModel.readSinglePartieFromDb(partieId);
        JeuView jeuView = new JeuView(partieModel, joueurModel);
        view.dispose();
    }
}
