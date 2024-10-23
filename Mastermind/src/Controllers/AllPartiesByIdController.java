package Controllers;

import Modele.Partie;
import Modele.Joueur;
import Views.AllPartiesByIdView;
import java.util.ArrayList;
import java.util.HashMap;

public class AllPartiesByIdController {

    private Partie partieModel;
    private Joueur joueurModel;
    private AllPartiesByIdView view;

    public AllPartiesByIdController(AllPartiesByIdView view) {
        this.partieModel = new Partie();
        this.joueurModel = new Joueur();
        this.view = view;
    }

    // Méthode pour afficher les parties d'un joueur
    public void afficherPartiesJoueur(int idJoueur) {
        String playerName = joueurModel.getOneJoueurInDb(idJoueur);
        ArrayList<HashMap<String, Object>> parties = partieModel.readAllPartiesOfPlayerFromDb(idJoueur);

        // Mise à jour de la vue avec les données du joueur et des parties
        view.afficherPartiesJoueur(playerName, parties);
    }
}
