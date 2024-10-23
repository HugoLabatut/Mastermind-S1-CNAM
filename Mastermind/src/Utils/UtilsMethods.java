package Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
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
}
