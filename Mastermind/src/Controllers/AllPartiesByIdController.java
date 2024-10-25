package Controllers;

import Modele.Partie;
import Modele.Joueur;
import Utils.UtilsMethods;
import Views.AllPartiesByIdView;
import Views.JeuView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class AllPartiesByIdController {

    private static Partie partieModel;
    private static Joueur joueurModel;
    private static AllPartiesByIdView view;

    public AllPartiesByIdController(AllPartiesByIdView view) {
        this.partieModel = new Partie();
        this.joueurModel = new Joueur();
        this.view = view;
    }

    // Méthode pour afficher les parties d'un joueur
    public static Object[][] recupererPartiesJoueur() {
//        int idJoueur = joueurModel.getId();
        int idJoueur = 1;
        ArrayList<HashMap<String, Object>> parties = partieModel.readAllPartiesOfPlayerFromDb(idJoueur);
        // Mise à jour de la vue avec les données du joueur et des parties
        return UtilsMethods.convertirEnTableau(parties);
    }

    // Méthode pour afficher les détails de la partie spécifique
    public static void afficherPartie(int partieId) {
        // Code pour rediriger ou afficher les détails de la partie avec l'ID spécifique
        partieModel.readSinglePartieFromDb(partieId);
        JeuView jeuView = new JeuView(partieModel, joueurModel);
        view.dispose();
    }
}
