package Views;

import Controllers.JeuController;
import Enums.Couleur;

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
    private JButton[][] boardButtons; // Grille du plateau
    private JLabel[][] resultLabels; // Labels pour les résultats [x, y, z]
    private Color[] currentCombination; // Tableau pour la combinaison en cours
    private Color[] correctCombination; // Tableau pour la combinaison correcte
    private int combinationLength;  // Longueur de la combinaison (modifiable)
    private int currentRow; // Commence en bas, va vers le haut
    private Color[] availableColors;
    private JTextArea logArea;  // Bloc de texte en bas à gauche
    private JeuController controller;

    public void MastermindView(int combinationLength, int nbTries, JeuController controller) {
        this.
        this.combinationLength = combinationLength;
        this.currentRow = nbTries;
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
            colorButton.setOpaque(true);
            colorButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            colorButton.addActionListener(new ColorButtonListener());
            colorSelectionPanel.add(colorButton);
        }

        // Plateau de jeu (13 lignes et (combinationLength + 1) colonnes pour les résultats)
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(13, combinationLength + 1));  // +1 pour afficher [x, y, z] à droite

        boardButtons = new JButton[13][combinationLength];
        resultLabels = new JLabel[13][1];  // Une seule colonne pour afficher les résultats

        for (int row = 0; row < 13; row++) {
            for (int col = 0; col < combinationLength; col++) {
                boardButtons[row][col] = new JButton();
                boardButtons[row][col].setPreferredSize(new Dimension(40, 40));
                boardButtons[row][col].setEnabled(false);  // Désactiver par défaut
                boardButtons[row][col].setBackground(Color.WHITE);  // Couleur par défaut pour indiquer une case vide
                final int currentCol = col;  // Variable pour utilisation dans les listeners
                boardButtons[row][col].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Permettre d'annuler une couleur si la case n'est pas blanche
                        if (!boardButtons[currentRow][currentCol].getBackground().equals(Color.WHITE)) {
                            boardButtons[currentRow][currentCol].setBackground(Color.WHITE);  // Réinitialiser la cellule
                            currentCombination[currentCol] = null;  // Retirer la couleur à cet indice
                            updateConfirmButtonState();  // Mettre à jour l'état du bouton "Confirmer"
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

        // Activer uniquement la ligne courante (currentRow)
        for (int col = 0; col < combinationLength; col++) {
            boardButtons[currentRow][col].setEnabled(true);  // Activer les boutons de la ligne courante
        }

        // Panneau de contrôle avec un bouton "Confirmer" et un label d'information
        controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());

        confirmButton = new JButton("Confirmer le coup");
        confirmButton.setEnabled(false);  // Désactivé tant que la combinaison n'est pas complète
        confirmButton.addActionListener(new ConfirmButtonListener());

        infoLabel = new JLabel("Choisissez une combinaison.");
        controlPanel.add(infoLabel, BorderLayout.NORTH);
        controlPanel.add(confirmButton, BorderLayout.SOUTH);

        // Bloc de texte en bas à gauche
        logArea = new JTextArea(5, 20);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(scrollPane, BorderLayout.WEST);  // Ajouter la zone de texte à gauche
        bottomPanel.add(colorSelectionPanel, BorderLayout.EAST);  // Couleurs en bas à droite

        // Ajouter les panels au frame principal
        add(bottomPanel, BorderLayout.SOUTH);
        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.NORTH);

        currentCombination = new Color[combinationLength];  // Initialiser avec la longueur de la combinaison

        setVisible(true);
    }

    // Listener pour sélectionner une couleur
    private class ColorButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton selectedButton = (JButton) e.getSource();
            Color selectedColor = selectedButton.getBackground();

            // Trouver la première case vide dans la ligne courante
            for (int col = 0; col < combinationLength; col++) {
                if (currentCombination[col] == null) {
                    boardButtons[currentRow][col].setBackground(selectedColor);
                    currentCombination[col] = selectedColor;  // Ajouter la couleur à la combinaison
                    break;
                }
            }

            updateConfirmButtonState();
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

    // Listener pour confirmer la combinaison
    private class ConfirmButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Envoyer la combinaison au contrôleur et recevoir les résultats
            int[] result = sendCombinationToController(currentCombination);  // [x, y, z]

            // Mettre à jour le label des résultats
            resultLabels[currentRow][0].setText("[" + result[0] + ", " + result[1] + ", " + result[2] + "]");

            // Logique pour confirmer le coup
            infoLabel.setText("Combinaison confirmée !");
            confirmButton.setEnabled(false);

            // Désactiver les boutons de la ligne précédente
            for (int col = 0; col < combinationLength; col++) {
                boardButtons[currentRow][col].setEnabled(false);  // Désactiver la ligne confirmée
            }

            // Déplacer à la ligne suivante si le coup n'est pas final
            if (currentRow > 0 && result[0] != 1) {  // Si le jeu n'est pas gagné
                currentRow--;
                currentCombination = new Color[combinationLength];  // Réinitialiser la combinaison pour la nouvelle ligne

                // Activer les boutons de la nouvelle ligne
                for (int col = 0; col < combinationLength; col++) {
                    boardButtons[currentRow][col].setEnabled(true);  // Activer la nouvelle ligne
                }

                infoLabel.setText("Nouvelle ligne, choisissez une nouvelle combinaison.");
            } else {
                infoLabel.setText("Partie terminée !");
                if (result[0] == 1) {
                    logArea.append("Vous avez gagné !\n");
                } else {
                    logArea.append("La partie est terminée. Essayez encore !\n");
                }
            }
        }
    }

    // Simule l'envoi de la combinaison au contrôleur
    private int[] sendCombinationToController(Color[] combination) {
        // Simuler la comparaison de la combinaison
        // Le contrôleur doit retourner un array [x, y, z]
        // x = 1 si la partie
        return new int[]{0, 2, 1}; // Résultat fictif pour l'exemple
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JeuView::new);
    }
}
