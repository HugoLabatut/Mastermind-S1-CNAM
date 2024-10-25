package Modele;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import Utils.DBConnector;

public class Partie implements PartieInterface{

    private int idPartie = 0;
    private int[] coupGagnant;
    private int[][] coups;
    private int nbCoupsInDb = 0;
    private int maxCoups;
    private int lengthCoup;

    public Partie() {}

    public int getLengthCoup() {
        return lengthCoup;
    }

    public int getMaxCoups() {
        return maxCoups;
    }

    // Fonctions principales

    public Partie initiateNouvellePartie(int lengthCoup, int maxCoups, int nbColor, int idJoueur) {
        this.lengthCoup = lengthCoup;
        this.maxCoups = maxCoups;
        this.coupGagnant = generateCoupGagnant(lengthCoup, nbColor);
        this.idPartie = addPartieToDb(idJoueur, this.coupGagnant);
        return this;
    }

    public Partie initiateNouvellePartieInvite(int lengthCoup, int maxCoups, int nbColor) {
        this.lengthCoup = lengthCoup;
        this.maxCoups = maxCoups;
        this.coupGagnant = generateCoupGagnant(lengthCoup, nbColor);
        return this;
    }

    public Color[] getCoupGagnantAsColors() {
        Color[] coupGagnantColors = new Color[this.coupGagnant.length];
        for (int i = 0; i < this.coupGagnant.length; i++) {
            coupGagnantColors[i] = new Color(this.coupGagnant[i]);
        }
        return coupGagnantColors;
    }

    public Color[][] getCoupsAsColors() {
        if (coups == null) {
            return null;
        }
        Color[][] coupsColors = new Color[this.coups.length][];
        for (int i = 0; i < this.coups.length; i++) {
            coupsColors[i] = new Color[this.coups[i].length];
            for (int j = 0; j < this.coups[i].length; j++) {
                coupsColors[i][j] = new Color(this.coups[i][j]);
            }
        }
        return coupsColors;
    }

    private int[] generateCoupGagnant(int length, int nbColor) {
        int[] coupGagnant = new int[length];
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            int n = r.nextInt(nbColor-1) + 1;
            coupGagnant[i] = n;
        }
        return coupGagnant;
    }

    public int[] checkCoup(int[] coupPropose) {
        int communs = compterElementsCommuns(coupGagnant, coupPropose);
        int bonnePlace = compterElementsMemePlace(coupGagnant, coupPropose);
        int mauvaisePlace = communs - bonnePlace;
        int gagnant = 0;
        if (bonnePlace == coupGagnant.length) {
            gagnant = 1;
        }
        return new int[] { bonnePlace, mauvaisePlace, gagnant };
    }


    // Partie CRUD (ou CRU dans ce cas)

    public ArrayList<HashMap<String, Object>> readAllPartiesOfPlayerFromDb(int idJoueur) {
        String requete = "SELECT * FROM partie WHERE id_joueur = ?";
        try (
                Connection con = DBConnector.connectToDB();
                PreparedStatement stmt = con.prepareStatement(requete)
        ) {
            stmt.setInt(1, idJoueur);
            ResultSet rs = stmt.executeQuery();
            return resultSetToList(rs);
        } catch (SQLException e) {
            System.out.println("Erreur DB lors de la lecture : " + e.getMessage() + "\n");
            throw new RuntimeException(e);
        }
    }

    public void readSinglePartieFromDb(int idPartie) {
        String requete = "SELECT * FROM partie WHERE id_partie = ?";
        try (
                Connection con = DBConnector.connectToDB();
                PreparedStatement stmt = con.prepareStatement(requete)
        ) {
            stmt.setInt(1, idPartie);
            ResultSet rs = stmt.executeQuery();
            this.idPartie = idPartie;
            if (rs.next()) {
                this.idPartie = rs.getInt("id_partie");
                int number = rs.getInt(3); // Récupère la 3ème colonne comme entier
                this.coupGagnant = intToArray(number);
                readCoupsFromDb(idPartie);
            } else {
                throw new SQLException("Aucune partie trouvée avec l'ID donné.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur DB lors de la lecture : " + e.getMessage() + "\n");
            throw new RuntimeException(e);
        }
    }

    private void readCoupsFromDb(int idPartie) {
        String requete = "SELECT contenu_coup FROM coup WHERE id_partie = ? ORDER BY rang_coup ASC;";
        try (
                Connection con = DBConnector.connectToDB();
                PreparedStatement stmt = con.prepareStatement(requete)
        ) {
            stmt.setInt(1, idPartie);
            ResultSet rs = stmt.executeQuery();
            ArrayList<int[]> coupsList = new ArrayList<>(); // Utiliser une liste dynamique
            while (rs.next()) {
                int coup = rs.getInt(1);
                coupsList.add(intToArray(coup));
            }
            coups = coupsList.toArray(new int[coupsList.size()][]); // Convertir en tableau 2D
            nbCoupsInDb = coups.length;
        } catch (SQLException e) {
            System.out.println("Erreur DB lors de la lecture : " + e.getMessage() + "\n");
            throw new RuntimeException(e);
        }
    }

    public int addPartieToDb(int idJoueur, int[] suitePartie) {
        String requete = "INSERT INTO partie (etat_partie, suite_partie, nbcoups_partie, id_joueur) VALUES (0, ?, 0, " +
                "?)";
        try (
                Connection con = DBConnector.connectToDB();
                PreparedStatement stmt = con.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS) // Indiquer que tu veux les clés générées
        ) {
            stmt.setInt(1, arrayToInt(suitePartie));
            stmt.setInt(2, idJoueur);
            stmt.executeUpdate();

            // Obtenir les clés générées
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Échec de la récupération de l'ID généré.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur DB lors de la création : " + e.getMessage() + "\n");
            throw new RuntimeException(e);
        }
    }

    public void savePartieToDb(int etatPartie) {
        String requete = "UPDATE partie SET etat_partie = ?, nbcoups_partie = ? WHERE id_partie = ?";
        try (
                Connection con = DBConnector.connectToDB();
                PreparedStatement stmt = con.prepareStatement(requete)
        ) {
            stmt.setInt(1, etatPartie);
            stmt.setInt(2, coups.length);
            stmt.setInt(3, idPartie);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur DB lors de la modification : " + e.getMessage() + "\n");
            throw new RuntimeException(e);
        }

        for (int i = nbCoupsInDb; i < coups.length; i++) {
            String requeteCoup = "INSERT INTO coups (contenu_coup, rang_coup, id_partie) VALUES (?, ?, ?)";
            try (
                    Connection con = DBConnector.connectToDB();
                    PreparedStatement stmt = con.prepareStatement(requeteCoup)
            ) {
                stmt.setInt(1, arrayToInt(coups[i]));
                stmt.setInt(2, i);
                stmt.setInt(3, idPartie);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Erreur DB lors de la création : " + e.getMessage() + "\n");
                throw new RuntimeException(e);
            }
        }
    }


    // Fonctions utilitaires

    private static int compterElementsCommuns(int[] coupGagnant, int[] coupPropose) {
        HashMap<Integer, Integer> occurrences1 = new HashMap<>();
        HashMap<Integer, Integer> occurrences2 = new HashMap<>();

        for (int number : coupGagnant) {
            occurrences1.put(number, occurrences1.getOrDefault(number, 0) + 1);
        }

        for (int number : coupPropose) {
            occurrences2.put(number, occurrences2.getOrDefault(number, 0) + 1);
        }

        int count = 0;
        for (Integer key : occurrences1.keySet()) {
            if (occurrences2.containsKey(key)) {
                count += Math.min(occurrences1.get(key), occurrences2.get(key));
            }
        }
        return count;
    }

    private static int compterElementsMemePlace(int[] coupGagnant, int[] coupPropose) {
        int result = 0;
        for (int i = 0; i < coupGagnant.length; i++) {
            if (coupGagnant[i] == coupPropose[i]) {
                result ++;
            }
        }
        return result;
    }

    public ArrayList<HashMap<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        ArrayList<HashMap<String, Object>> resultList = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            HashMap<String, Object> rowMap = new HashMap<>();
            // Parcourir chaque colonne
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object columnValue = rs.getObject(i);
                rowMap.put(columnName, columnValue);
            }
            resultList.add(rowMap); // Ajouter la ligne à la liste
        }
        return resultList;
    }

    // Transforme un array en int des chiffres qui le composent (opposé d'intToArray)
    // ex. entrée [1,2,3,4], sortie 1234
    private int arrayToInt(int[] tableau) {
        int nombre = 0;
        for (int j : tableau) {
            nombre = nombre * 10 + j; // Décale les chiffres déjà présents à gauche et ajoute le nouveau chiffre
        }
        return nombre;
    }

    // Transforme un int en array des chiffres qui le composent (opposé d'arrayToInt)
    // ex. entrée 1234, sortie [1,2,3,4]
    private int[] intToArray(int nombre) {
        String nombreString = String.valueOf(nombre);
        int[] chiffres = new int[nombreString.length()];

        for (int i = 0; i < nombreString.length(); i++) {
            chiffres[i] = Character.getNumericValue(nombreString.charAt(i));
        }
        return chiffres;
    }

}
