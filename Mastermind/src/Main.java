import Controllers.MenuController;
import Views.JeuView;
import Views.MenuView;

import javax.swing.*;
import java.awt.*;

public class Main {
    // Compatibilité cross-platform des éléments Swing (entre Windows et macOS)

    private static void setLookAndFeel() {
        String osName = System.getProperty("os.name").toLowerCase();
        try {
            if(osName.contains("mac")) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } else {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Il n'est pas possible de définir le LookAndFeel souhaité.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Exécution de l'interface graphique du jeu Mastermind
        // SwingUtilities.invokeLater(JeuView::new);
        setLookAndFeel();
        MenuView menuwdw = new MenuView();
        new MenuController(menuwdw);
    }
}