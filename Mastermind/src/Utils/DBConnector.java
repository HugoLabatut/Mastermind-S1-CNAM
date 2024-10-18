package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    public static Connection connectToDB() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/mastermind", "root", "");
    }

    public static void main(String[] args) {
        try (Connection con = connectToDB()) {
            System.out.println("Connexion à la DB réussie");
        } catch (SQLException e) {
            System.out.println("Connexion à la DB échouée : " + e.getMessage());
        }
    }
}