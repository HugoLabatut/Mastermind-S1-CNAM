package Modele;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import Utils.DBConnector;
import Utils.UtilsColor;
import Utils.UtilsMethods;

public class Partie implements PartieInterface{

    private int idPartie = 0;
    private int[] coupGagnant;
    private int[][] coups;
    private int nbCoupsInDb = 0;
    private int maxCoups;
    private int maxColors;
    private int lengthCoup;
    UtilsColor UtilsColor;
    private int etatPartie = 0;

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
        this.maxColors = nbColor;
        this.coupGagnant = generateCoupGagnant(lengthCoup, nbColor);
        this.idPartie = addPartieToDb(idJoueur, this.coupGagnant);
        return this;
    }

    public Partie initiateNouvellePartieInvite(int lengthCoup, int maxCoups, int nbColor) {
        this.lengthCoup = lengthCoup;
        this.maxCoups = maxCoups;
        this.maxColors = nbColor;
        this.coupGagnant = generateCoupGagnant(lengthCoup, nbColor);
        return this;
    }

    public int getEtatPartie() {
        return this.etatPartie;
    }

    public int getMaxColors() { return this.maxColors; }

    public Color[] getCoupGagnantAsColors() {
        return Utils.UtilsColor.convertirIndicesEnCouleurs(coupGagnant);
    }

    public Color[][] getCoupsAsColors() {
        if(this.idPartie != 0) {
            readCoupsFromDb(this.idPartie);
        }
        if (coups == null) {
            return null;
        }
        Color[][] coupsColors = new Color[this.coups.length][];
        for (int i = 0; i < this.coups.length; i++) {
            coupsColors[i] = Utils.UtilsColor.convertirIndicesEnCouleurs(this.coups[i]);
        }
        return coupsColors;
    }

    private int[] generateCoupGagnant(int length, int nbColor) {
        int[] coupGagnant = new int[length];
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            int n = r.nextInt(nbColor);
            coupGagnant[i] = n;
        }
        return coupGagnant;
    }

    public int[] checkCoup(int[] coupPropose) {
        System.out.print(Arrays.toString(coupGagnant));
        System.out.print(Arrays.toString(coupPropose));
        int communs = compterElementsCommuns(coupGagnant, coupPropose);
        int bonnePlace = compterElementsMemePlace(coupGagnant, coupPropose);
        int mauvaisePlace = communs - bonnePlace;
        int gagnant = 0;
        if (bonnePlace == coupGagnant.length) {
            gagnant = 1;
        }
        return new int[] { bonnePlace, mauvaisePlace, gagnant };
    }


    public ArrayList<HashMap<String, Object>> readAllPartiesOfPlayerFromDb(int idJoueur) {
        String requete = "SELECT * FROM partie WHERE id_joueur = ?";
        try (
                Connection con = DBConnector.connectToDB();
                PreparedStatement stmt = con.prepareStatement(requete)
        ) {
            stmt.setInt(1, idJoueur);
            ResultSet rs = stmt.executeQuery();
            return UtilsMethods.resultSetToList(rs);
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
                this.coupGagnant = UtilsColor.intToArray(number);
                this.maxCoups = rs.getInt("nbcoups_partie");
                this.lengthCoup = String.valueOf(rs.getInt("suite_partie")).length();
                this.etatPartie = rs.getInt("etat_partie");
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
                coupsList.add(UtilsColor.intToArray(coup));
            }
            coups = coupsList.toArray(new int[coupsList.size()][]); // Convertir en tableau 2D
            nbCoupsInDb = coups.length;
        } catch (SQLException e) {
            System.out.println("Erreur DB lors de la lecture : " + e.getMessage() + "\n");
            throw new RuntimeException(e);
        }
    }

    public int addPartieToDb(int idJoueur, int[] suitePartie) {
        String requete = "INSERT INTO partie (etat_partie, suite_partie, nbcoups_partie, id_joueur, nbcouleur_partie)" +
                " VALUES (0, ?, ?, ?, ?)";
        try (
                Connection con = DBConnector.connectToDB();
                PreparedStatement stmt = con.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS) // Indiquer que tu veux les clés générées
        ) {
            stmt.setInt(1, UtilsColor.arrayToInt(suitePartie));
            stmt.setInt(2, maxCoups);
            stmt.setInt(3, idJoueur);
            stmt.setInt(4, maxColors);
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
        String requete = "UPDATE partie SET etat_partie = ? WHERE id_partie = ?";
        try (
                Connection con = DBConnector.connectToDB();
                PreparedStatement stmt = con.prepareStatement(requete)
        ) {
            stmt.setInt(1, etatPartie);
            stmt.setInt(2, idPartie);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur DB lors de la modification : " + e.getMessage() + "\n");
            throw new RuntimeException(e);
        }

        for (int i = nbCoupsInDb; i < coups.length; i++) {
            String requeteCoup = "INSERT IGNORE INTO coups (contenu_coup, rang_coup, id_partie) VALUES (?, ?, ?)";
            try (
                    Connection con = DBConnector.connectToDB();
                    PreparedStatement stmt = con.prepareStatement(requeteCoup)
            ) {
                stmt.setInt(1, UtilsColor.arrayToInt(coups[i]));
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

}
