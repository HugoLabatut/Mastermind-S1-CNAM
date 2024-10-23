import Controllers.MenuController;
import Views.JeuView;
import Views.MenuView;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Exécution de l'interface graphique du jeu Mastermind
        // SwingUtilities.invokeLater(JeuView::new);
        MenuView menuwdw = new MenuView();
        new MenuController(menuwdw);
    }
}