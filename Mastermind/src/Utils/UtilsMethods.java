package Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class UtilsMethods {
    private static UtilsMethods instance;

    public UtilsMethods() {
    }

    public static UtilsMethods getInstance() {
        if (instance == null) {
            instance = new UtilsMethods();
        }
        return instance;
    }

    public HashMap<String, Object> resultsToHashMap(ResultSet rs) {
        HashMap<String, Object> resultMap = new HashMap<>();
        try {
            // Vérifier si le ResultSet n'est pas vide
            if (rs.next()) { // Aller à la première ligne
                int columnCount = rs.getMetaData().getColumnCount(); // Nombre de colonnes
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rs.getMetaData().getColumnName(i); // Nom de la colonne
                    Object value = rs.getObject(i); // Valeur de la colonne
                    resultMap.put(columnName, value); // Ajouter à la HashMap
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public Object[][] convertirEnTableau(ArrayList<HashMap<String, Object>> listeParties) {
        if (listeParties == null || listeParties.isEmpty()) {
            return new Object[0][0]; // Retourner un tableau vide si la liste est vide
        }

        // Taille des colonnes basée sur le nombre de clés dans le premier HashMap
        int numColonnes = listeParties.get(0).size();
        int numLignes = listeParties.size();

        // Créer un tableau 2D avec le nombre de lignes et de colonnes approprié
        Object[][] tableau = new Object[numLignes][numColonnes];

        // Itérer sur chaque élément de la liste
        for (int i = 0; i < numLignes; i++) {
            HashMap<String, Object> map = listeParties.get(i);
            int j = 0;
            // Remplir les colonnes avec les valeurs des HashMap
            for (Object value : map.values()) {
                tableau[i][j] = value;
                j++;
            }
        }

        return tableau;
    }

}
