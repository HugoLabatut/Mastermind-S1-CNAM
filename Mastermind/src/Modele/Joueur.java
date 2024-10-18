package Modele;

import Utils.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Joueur implements JoueurInterface {
    private int idJoueur;
    private String nomJoueur;

    public Joueur(int idJoueur, String nomJoueur) {
        this.idJoueur = idJoueur;
        this.nomJoueur = nomJoueur;
    }

    public int getId() {
        return this.idJoueur;
    }

    public String getNom() {
        return this.nomJoueur;
    }

    public String toString() {
        return ("Identifiant " + this.idJoueur + "\n" +
                "Nom " + this.nomJoueur + "\n");
    }

    public void createJoueurInDB() {
        String requete = "INSERT INTO joueur (nom_joueur) " +
                "VALUES (?)";
        try (
                Connection con = DBConnector.connectToDB();
                PreparedStatement stmt = con.prepareStatement(requete, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, nomJoueur);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.idJoueur = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur DB lors de la création : " + e.getMessage() + "\n");
            throw new RuntimeException(e);
        }
    }

    public String getOneJoueurInDb(int id) {
        String requete = "SELECT * FROM joueur WHERE id_joueur = ?";
        try (
                Connection con = DBConnector.connectToDB();
                PreparedStatement stmt = con.prepareStatement(requete)
        ) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                this.idJoueur = rs.getInt("id_joueur");
                this.nomJoueur = rs.getString("nom_joueur");
                return this.toString();
            } else {
                return "Joueur non trouvé.";
            }
        } catch (SQLException e) {
            System.out.println("Erreur DB lors de la récupération du joueur : " + e.getMessage() + "\n");
            throw new RuntimeException(e);
        }
    }

    public ArrayList<HashMap<String, Object>> getAllJoueurInDb() {
        StringBuilder result = new StringBuilder();
        String requete = "SELECT * FROM joueur";

        try (
                Connection con = DBConnector.connectToDB();
                PreparedStatement stmt = con.prepareStatement(requete)
        ) {
            ResultSet rs = stmt.executeQuery();
            return resultSetToList(rs);
        } catch (SQLException e) {
            System.out.println("Erreur DB lors de la récupération des joueurs : " + e.getMessage() + "\n");
            throw new RuntimeException(e);
        }
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
}
