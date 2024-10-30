package Utils;

import Enums.Couleur;
import java.awt.Color;
import java.util.Arrays;

public class UtilsColor {

    public static int[] convertirCouleursEnIndices(Color[] couleurs) {
        int[] indices = new int[couleurs.length];

        for (int i = 0; i < couleurs.length; i++) {
            Color color = couleurs[i];
            indices[i] = getIndiceCouleur(color);
        }
        return indices;
    }

    private static int getIndiceCouleur(Color color) {
        Couleur[] valeursCouleur = Couleur.values();
        for (int i = 0; i < valeursCouleur.length; i++) {
            if (valeursCouleur[i].getSwingColor().equals(color)) {
                return i; // Retourne l'indice correspondant dans l'enum
            }
        }
        throw new IllegalArgumentException("Couleur non trouvée dans l'énumération : " + color);
    }

    public static Color[] convertirIndicesEnCouleurs(int[] indices) {
        Color[] couleurs = new Color[indices.length];
        System.out.print(Arrays.toString(indices));

        for (int i = 0; i < indices.length; i++) {
            int indice = indices[i];
            couleurs[i] = getCouleurFromIndice(indice); // Utilise la méthode getCouleurFromIndice
        }

        return couleurs;
    }

    private static Color getCouleurFromIndice(int indice) {
        Couleur[] valeursCouleur = Couleur.values();

        if (indice < 0 || indice > valeursCouleur.length) {
            throw new IllegalArgumentException("Indice en dehors des limites de l'énumération : " + indice);
        }

        return valeursCouleur[indice].getSwingColor(); // Retourne la couleur associée à l'indice
    }

    // Transforme un array en int des chiffres qui le composent (opposé d'intToArray)
    // ex. entrée [1,2,3,4], sortie 1234
    public static int arrayToInt(int[] tableau) {
        int nombre = 0;
        for (int j : tableau) {
            nombre = nombre * 10 + j; // Décale les chiffres déjà présents à gauche et ajoute le nouveau chiffre
        }
        return nombre;
    }

    // Transforme un int en array des chiffres qui le composent (opposé d'arrayToInt)
    // ex. entrée 1234, sortie [1,2,3,4]
    public static int[] intToArray(int nombre) {
        String nombreString = String.valueOf(nombre);
        int[] chiffres = new int[nombreString.length()];

        for (int i = 0; i < nombreString.length(); i++) {
            chiffres[i] = Character.getNumericValue(nombreString.charAt(i));
        }
        return chiffres;
    }
}