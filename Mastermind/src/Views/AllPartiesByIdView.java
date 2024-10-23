package Views;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class AllPartiesByIdView extends JFrame {
    private JTextArea resultArea;
    private JLabel playerNameLabel;
    private JLabel titleLabel;

    public AllPartiesByIdView() {
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        titleLabel = new JLabel("Parties de ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        playerNameLabel = new JLabel("Nom du Joueur : ", SwingConstants.CENTER);
        playerNameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        add(playerNameLabel, BorderLayout.SOUTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Méthode pour afficher les parties
    public void afficherPartiesJoueur(String playerName, ArrayList<HashMap<String, Object>> parties) {
        resultArea.setText(""); // Effacer le texte précédent

        // Mettre à jour le titre
        titleLabel.setText("Parties de " + playerName);
        playerNameLabel.setText("Nom du Joueur : " + playerName);

        if (parties == null || parties.isEmpty()) {
            resultArea.append("Aucune partie trouvée pour ce joueur.\n");
        } else {
            for (HashMap<String, Object> partie : parties) {
                // Affichage de l'état de la partie en fonction de la valeur de etat_partie
                String etatPartie;
                int etat = (int) partie.get("etat_partie"); // Cast à int pour éviter les erreurs de comparaison
                switch (etat) {
                    case 0:
                        etatPartie = "En attente";
                        break;
                    case 1:
                        etatPartie = "Gagné";
                        break;
                    case 2:
                        etatPartie = "Perdu";
                        break;
                    default:
                        etatPartie = "Inconnu";
                }

                resultArea.append("État Partie: " + etatPartie + "\n");
                resultArea.append("Nombre de Coups max: " + partie.get("nbcoups_partie") + "\n");
                resultArea.append("-----------------------------\n");
            }
        }
    }
}
