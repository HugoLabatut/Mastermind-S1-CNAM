package Views;

import Controllers.JeuController;
import Enums.Couleur;
import Modele.Joueur;
import Modele.Partie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JeuView extends JFrame {
    private JPanel colorSelectionPanel;
    private JPanel boardPanel;
    private JPanel controlPanel;
    private JLabel infoLabel;
    private JButton confirmButton;
    private JButton accueilButton;
    private JButton[][] boardButtons; // Grille du plateau
    private JLabel[][] resultLabels; // Labels pour les résultats [x, y, z]
    private Color[] currentCombination; // Tableau pour la combinaison en cours
    private Color[] correctCombination; // Tableau pour la combinaison correcte
    private int currentRow; // Commence en bas, va vers le haut
    private Color[] availableColors;
    private JTextArea rulesArea;  // Bloc de texte en bas à gauche
    Partie partie;
    Joueur joueur;

    public JeuView(Partie partie, Joueur joueur) {

        this.partie = partie;
        this.joueur = joueur;
        this.correctCombination = partie.getCoupGagnantAsColors();
        this.currentRow = partie.getMaxCoups();

        availableColors = new Color[Couleur.values().length];
        for (int i = 0; i < Couleur.values().length; i++) {
            availableColors[i] = Couleur.values()[i].getSwingColor();
        }

        setTitle("Mastermind");
        setSize(800, 800); // Agrandir la fenêtre sur l'axe X
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel pour sélectionner les couleurs
        colorSelectionPanel = new JPanel();
        colorSelectionPanel.setLayout(new FlowLayout());

        // Ajout des couleurs disponibles sous forme de boutons ronds
        for (Color color : availableColors) {
            JButton colorButton = new JButton();
            colorButton.setPreferredSize(new Dimension(40, 40));
            colorButton.setBackground(color);
            colorButton.addActionListener(new ColorButtonListener());
            colorSelectionPanel.add(colorButton);
        }

        // Plateau de jeu (13 lignes et (partie.getLengthCoup() + 1) colonnes pour les résultats)
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(partie.getMaxCoups() + 1, partie.getLengthCoup() + 1));  // +1 pour afficher [x, y, z] à droite

        boardButtons = new JButton[partie.getMaxCoups() + 1][partie.getLengthCoup()];
        resultLabels = new JLabel[partie.getMaxCoups() + 1][1];  // Une seule colonne pour afficher les résultats

        for (int row = 0; row < partie.getMaxCoups() + 1; row++) {
            for (int col = 0; col < partie.getLengthCoup(); col++) {
                boardButtons[row][col] = new JButton();
                boardButtons[row][col].setPreferredSize(new Dimension(40, 40));
                boardButtons[row][col].setEnabled(false);
                boardButtons[row][col].setBackground(Color.WHITE);
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

        // Activer uniquement la ligne courante (currentRow) pour la suppression des colors
        for (int col = 0; col < partie.getLengthCoup(); col++) {
            boardButtons[currentRow][col].setEnabled(true);
        }

        // Panneau de contrôle avec un bouton "Retour à l'accueil" et un label d'information
        accueilButton = new JButton("Retour à l'accueil");
        accueilButton.addActionListener(new AccueilButtonListener());

        controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        infoLabel = new JLabel("Choisissez une combinaison.");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        controlPanel.add(infoLabel, BorderLayout.WEST);
        controlPanel.add(accueilButton, BorderLayout.EAST);

        // Bloc de texte en bas à gauche
        rulesArea = new JTextArea(5, 20);
        rulesArea.setEditable(false);
        rulesArea.setLineWrap(true);
        rulesArea.setWrapStyleWord(true);

        rulesArea.append("Règles du jeu :\n");
        rulesArea.append("""
                Vous devez deviner une combinaison secrète de couleurs en proposant des combinaisons.
                Après chaque essai, vous aurez des indices : un pion noir signifie qu'une couleur est correcte et bien placée, et un pion blanc signifie qu'une couleur est correcte mais mal placée.
                Le but est de deviner la combinaison en un nombre limité d'essais.
                """);

        JScrollPane scrollPane = new JScrollPane(rulesArea);

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

        displayPrecedentCoups();
        setVisible(true);
    }

    private void displayPrecedentCoups() {
        Color[][] previousCoups = partie.getCoupsAsColors(); // Récupère les coups précédents
        if (previousCoups != null) {
        for (int row = 0; row < previousCoups.length; row++) {
            for (int col = 0; col < previousCoups[row].length; col++) {
                if (previousCoups[row][col] != null) {
                    boardButtons[row][col].setBackground(previousCoups[row][col]);
                }
            }
        }
        currentRow = partie.getCoupsAsColors().length;
        }
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
            System.out.print(currentRow);
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
            int[] result = {0, 2, 1}; // À changer pour faire un appel à la verif

            // Mettre à jour le label des résultats
            resultLabels[currentRow][0].setText("[" + result[0] + ", " + result[1] + ", " + result[2] + "]");

            // Logique pour confirmer le coup
            infoLabel.setText("Combinaison confirmée !");
            confirmButton.setEnabled(false);

            // Désactiver les boutons de la ligne précédente
            for (int col = 0; col < partie.getLengthCoup(); col++) {
                boardButtons[currentRow][col].setEnabled(false);  // Désactiver la ligne confirmée
            }

            // Déplacer à la ligne suivante si le coup n'est pas final
            if (currentRow > 1 && result[0] != 1) {  // Si le jeu n'est pas gagné
                currentRow--;
                currentCombination = new Color[partie.getLengthCoup()];  // Réinitialiser la combinaison pour la nouvelle ligne

                // Activer les boutons de la nouvelle ligne
                for (int col = 0; col < partie.getLengthCoup(); col++) {
                    boardButtons[currentRow][col].setEnabled(true);  // Activer la nouvelle ligne
                }

                infoLabel.setText("Nouvelle ligne, choisissez une nouvelle combinaison.");
            } else {
                infoLabel.setText("Partie terminée !");
                if (result[0] == 1) {
                    rulesArea.append("Vous avez gagné !\n");
                } else {
                    rulesArea.append("La partie est terminée. Essayez encore !\n");
                }
            }
        }
    }

    private class AccueilButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(joueur.getId() == 0) {
                System.out.print("Retour menu invité");
            }
        }
    }
}
