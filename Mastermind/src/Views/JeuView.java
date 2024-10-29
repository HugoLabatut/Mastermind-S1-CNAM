package Views;

import Controllers.JeuController;
import Controllers.MenuController;
import Enums.Couleur;
import Modele.Joueur;
import Modele.Partie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class JeuView extends JFrame {
    private final JLabel infoLabel;
    private final JButton confirmButton;
    private final JButton[][] boardButtons; // Grille du plateau
    private final JLabel[][] resultLabels; // Labels pour les résultats [x, y, z]
    private Color[] currentCombination; // Tableau pour la combinaison en cours
    private int currentRow; // Commence en bas, va vers le haut
    Partie partie;
    Joueur joueur;
    JeuController controller;
    private int etatPartie;
    Color[] correctCombination;

    public JeuView(Partie partie, Joueur joueur) {

        // Récupération des infos de la partie
        correctCombination = partie.getCoupGagnantAsColors();
        this.partie = partie;
        this.joueur = joueur;
        this.currentRow = partie.getMaxCoups();
        this.etatPartie = partie.getEtatPartie();
        controller = JeuController.getInstance();

        Color[] availableColors = new Color[partie.getMaxColors()];
        for (int i = 0; i < partie.getMaxColors(); i++) {
            availableColors[i] = Couleur.values()[i].getSwingColor();
        }

        setTitle("Mastermind");
        setSize(900, 800); // Agrandir la fenêtre sur l'axe X
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closingWindowListener();
            }
        });
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel pour sélectionner les couleurs
        JPanel colorSelectionPanel = new JPanel();
        colorSelectionPanel.setLayout(new FlowLayout());

        // Ajout des couleurs disponibles sous forme de boutons ronds
        for (Color color : availableColors) {
            JButton colorButton = new JButton();
            colorButton.setPreferredSize(new Dimension(40, 40));
            colorButton.setBackground(color);
            colorButton.setOpaque(true);
            colorButton.addActionListener(new ColorButtonListener());
            colorSelectionPanel.add(colorButton);
        }

        // Plateau de jeu (13 lignes et (partie.getLengthCoup() + 1) colonnes pour les résultats)
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(partie.getMaxCoups() + 1, partie.getLengthCoup() + 1));  // +1 pour afficher [x, y, z] à droite

        boardButtons = new JButton[partie.getMaxCoups() + 1][partie.getLengthCoup()];
        resultLabels = new JLabel[partie.getMaxCoups() + 1][1];  // Une seule colonne pour afficher les résultats

        for (int row = 0; row < partie.getMaxCoups() + 1; row++) {
            for (int col = 0; col < partie.getLengthCoup(); col++) {
                boardButtons[row][col] = new JButton();
                boardButtons[row][col].setPreferredSize(new Dimension(40, 40));
                boardButtons[row][col].setEnabled(false);
                boardButtons[row][col].setBackground(Color.WHITE);
                boardButtons[row][col].setOpaque(true);
                final int currentCol = col;
                boardButtons[row][col].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Permettre d'annuler une couleur si la case n'est pas blanche
                        if (!boardButtons[currentRow][currentCol].getBackground().equals(Color.WHITE)) {
                            boardButtons[currentRow][currentCol].setBackground(Color.WHITE);
                            currentCombination[currentCol] = null;
                            updateConfirmButtonState();
                            infoLabel.setText("Vous avez annulé une couleur, continuez la saisie.");
                        }
                    }
                });
                boardPanel.add(boardButtons[row][col]);
            }

            // Ajouter un label pour afficher le résultat [x, y, z]
            resultLabels[row][0] = new JLabel(" ");
            boardPanel.add(resultLabels[row][0]);
        }

        // Settings de la ligne de la combinaison gagnante, gris si partie en cours, la réponse sinon
        for (int col = 0; col < partie.getLengthCoup(); col++) {
            if (etatPartie == 0) {
                boardButtons[0][col].setBackground(new Color(175, 175, 175));
            } else {
                boardButtons[0][col].setBackground(correctCombination[col]);
            }
        }
        resultLabels[0][0].setText("Combinaison correcte");

        // Panneau de contrôle avec un bouton "Retour à l'accueil" et un label d'information
        JButton accueilButton = new JButton("Retour à l'accueil");
        accueilButton.addActionListener(new AccueilButtonListener());

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        if (etatPartie == 0) {
            infoLabel = new JLabel("Choisissez une combinaison.");
        } else if (etatPartie == 1) {
            infoLabel = new JLabel("La partie est terminée, vous aviez gagné !");
        } else {
            infoLabel = new JLabel("La partie est terminée, vous aviez perdu !");
        }
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        controlPanel.add(infoLabel, BorderLayout.WEST);
        controlPanel.add(accueilButton, BorderLayout.EAST);
        
        // Bloc de texte en bas à gauche
        JScrollPane scrollPane = getRulesScrollPane();

        confirmButton = new JButton("Confirmer le coup");
        confirmButton.setEnabled(false);  // Désactivé tant que la combinaison n'est pas complète
        confirmButton.addActionListener(new ConfirmButtonListener());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(scrollPane);
        bottomPanel.add(colorSelectionPanel);
        bottomPanel.add(confirmButton);

        // Ajouter les panels au frame principal
        add(bottomPanel, BorderLayout.SOUTH);
        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.NORTH);

        currentCombination = new Color[partie.getLengthCoup()];  // Initialiser avec la longueur de la combinaison

        Color[][] previousCoups = partie.getCoupsAsColors();
        if(previousCoups != null) {displayPrecedentCoups(previousCoups);}
        setVisible(true);
    }

    private static JScrollPane getRulesScrollPane() {
        JTextArea rulesArea = new JTextArea(5, 20);
        rulesArea.setEditable(false);
        rulesArea.setLineWrap(true);
        rulesArea.setWrapStyleWord(true);

        rulesArea.append("Règles du jeu :\n");
        rulesArea.append("""
                Vous devez deviner une combinaison secrète de couleurs en proposant des combinaisons.
                Après chaque essai, vous aurez des indices : un pion noir signifie qu'une couleur est correcte et bien placée, et un pion blanc signifie qu'une couleur est correcte mais mal placée.
                Le but est de deviner la combinaison en un nombre limité d'essais.
                """);
        return new JScrollPane(rulesArea);
    }

    private void displayPrecedentCoups() {
        Color[][] previousCoups = partie.getCoupsAsColors(); // Récupère les coups précédents
        if (previousCoups != null) {
            for (int row = previousCoups.length - 1; row >= 0; row--) { // Inversion de l'ordre des lignes
                for (int col = 0; col < previousCoups[row].length; col++) {
                    if (previousCoups[row][col] != null) {
                        boardButtons[row][col].setBackground(previousCoups[row][col]);
                    }
                }
            }
            currentRow = previousCoups.length;
        }

        // Mettre à jour currentRow si nécessaire
        currentRow = previousCoups.length;
    }


    // Vérifie si la combinaison est complète pour activer ou désactiver le bouton "Confirmer"
    private void updateConfirmButtonState() {
        boolean isComplete = true;
        for (Color color : currentCombination) {
            if (color == null) {
                isComplete = false;
                break;
            }
        }
        confirmButton.setEnabled(isComplete);
        if (isComplete) {
            infoLabel.setText("Combinaison complète, veuillez confirmer.");
        } else {
            infoLabel.setText("Continuez à choisir des couleurs.");
        }
    }

    // Listener pour sélectionner une couleur
    private class ColorButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton selectedButton = (JButton) e.getSource();
            Color selectedColor = selectedButton.getBackground();
            // Trouver la première case vide dans la ligne courante
            for (int col = 0; col < partie.getLengthCoup(); col++) {
                if (currentCombination[col] == null) {
                    boardButtons[currentRow][col].setBackground(selectedColor);
                    currentCombination[col] = selectedColor;  // Ajouter la couleur à la combinaison
                    break;
                }
            }

            updateConfirmButtonState();
        }
    }

    // Listener pour confirmer la combinaison
    private class ConfirmButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Envoyer la combinaison au contrôleur et recevoir les résultats
            System.out.print(Arrays.toString(currentCombination));
            int[] result = controller.getRetourCoup(currentCombination); // À changer pour faire un appel à la vérification

            // Mettre à jour le label des résultats
            resultLabels[currentRow][0].setText("<html>Couleurs bien placées : " + result[0] + "<br/>Couleurs correctes mais mal placées : " + result[1] + "</html>");

            // Logique pour confirmer le coup
            infoLabel.setText("Combinaison confirmée !");
            confirmButton.setEnabled(false);

            // Désactiver les boutons de la ligne précédente
            for (int col = 0; col < partie.getLengthCoup(); col++) {
                boardButtons[currentRow][col].setEnabled(false);  // Désactiver la ligne confirmée
            }

            // Déplacer à la ligne suivante si le coup n'est pas final
            if (currentRow > 1 && result[2] != 1) {  // Si le jeu n'est pas gagné
                currentRow--;
                currentCombination = new Color[partie.getLengthCoup()];  // Réinitialiser la combinaison pour la nouvelle ligne

                // Activer les boutons de la nouvelle ligne
                for (int col = 0; col < partie.getLengthCoup(); col++) {
                    boardButtons[currentRow][col].setEnabled(true);  // Activer la nouvelle ligne
                }

                infoLabel.setText("Nouvelle ligne, choisissez une nouvelle combinaison.");
            } else {
                // Affichage de la combinaison correcte
                for (int col = 0; col < partie.getLengthCoup(); col++) {
                    boardButtons[0][col].setBackground(correctCombination[col]);
                }

                //Affichage du message de victoire et setting de l'état de la partie
                if (result[2] == 1) {
                    infoLabel.setText("Partie terminée ! Vous avez gagné !\n");
                    etatPartie = 1;
                } else {
                    infoLabel.setText("Partie terminée ! Vous avez perdu !\n");
                    etatPartie = 2;
                }
            }
        }
    }

    private class AccueilButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(joueur.getId() != 0) {
                partie.savePartieToDb(etatPartie);
            }
            JeuView.this.dispose();
            MenuView newMenu = new MenuView();
            new MenuController(newMenu);
        }
    }

    private void closingWindowListener() {
        int reponse = JOptionPane.showConfirmDialog(
                null,
                "Voulez-vous quitter l'application ?",
                "Quitter l'application",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (reponse == JOptionPane.YES_OPTION) {
            if (joueur.getId() != 0) {
                partie.savePartieToDb(etatPartie);
            }
            System.exit(0);
        }
    }
}
