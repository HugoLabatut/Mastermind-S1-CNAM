package Controllers;
import Modele.Partie;

import java.awt.*;

public class JeuController {

    private static JeuController instance;
    private Partie partieEnCours;

    private JeuController() {
    }

    public static synchronized JeuController getInstance() {
        if (instance == null) {
            instance = new JeuController();
        }
        return instance;
    }

    public void setPartieEnCours(Partie partieEnCours) {
        this.partieEnCours = partieEnCours;
    }

    // Méthode pour récupérer le retour du contrôleur après un coup
    public int[] getRetourCoup(Color[] combinaisonJoueur) {
        int[] combinaisonInt = Utils.UtilsColor.convertirCouleursEnIndices(combinaisonJoueur);
        return partieEnCours.checkCoup(combinaisonInt);
    }

    // Autres méthodes pour gérer l'état du jeu si nécessaire
}
