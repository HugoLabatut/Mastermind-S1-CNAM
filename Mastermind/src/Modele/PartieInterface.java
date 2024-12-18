package Modele;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public interface PartieInterface {

    int idPartie = 0;
    int[] coupGagnant = new int[0];
    int[][] coups = new int[0][];
    int nbCoupsInDb = 0;

    // Fonctions principales

    Partie initiateNouvellePartie(int lengthCoup, int length, int nbColor, int idJoueur);
    Partie initiateNouvellePartieInvite(int lengthCoup, int length, int nbColor);

    private int[] generateCoupGagnant(int length, int nbColor) {
        return null;
    }

    // Vérifie à la fois les correspondances et le cas de victoire
    // Retourne un array de 3 int [x, y, z] = [Nombre de bonnes positions, nombre de mauvaises positions, victoire (1
    // si oui, 0 si non)
    int[] checkCoup(int[] coupPropose);


    // Partie CRUD (ou CRU dans ce cas)

    ArrayList<HashMap<String, Object>> readAllPartiesOfPlayerFromDb(int idJoueur);

    // Appelle le readCoupsFromDb
    void readSinglePartieFromDb(int idPartie);

    private void readCoupsFromDb(int idPartie) { }

    int addPartieToDb(int idJoueur, int[] suitePartie);

    // int etatPartie : 0 si partie en cours, 1 si victoire, 2 si défaite
    void savePartieToDb(int etatPartie);


    // Fonctions utilitaires

    private static int compterElementsCommuns(int[] coupGagnant, int[] coupPropose) {
        return 0;
    }

    private static int compterElementsMemePlace(int[] coupGagnant, int[] coupPropose) {
        return 0;
    }

    // Transforme un array en int des chiffres qui le composent (opposé d'intToArray)
    // ex. entrée [1,2,3,4], sortie 1234
    private int arrayToInt(int[] tableau) {
        return 0;
    }

    // Transforme un int en array des chiffres qui le composent (opposé d'arrayToInt)
    // ex. entrée 1234, sortie [1,2,3,4]
    private int[] intToArray(int nombre){
        return new int[]{0};
    }

}
