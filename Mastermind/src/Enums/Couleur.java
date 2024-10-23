package Enums;

import java.awt.*;

public enum Couleur {
    ROUGE(Color.RED),
    BLEU(Color.BLUE),
    VERT(Color.GREEN),
    JAUNE(Color.YELLOW),
    NOIR(Color.BLACK),
    ORANGE(Color.ORANGE),
    MARRON(new Color(165, 42, 42)), // Couleur marron personnalisée
    FUCHSIA(new Color(255, 0, 255)); // Couleur fuchsia personnalisée

    private final Color swingColor;

    Couleur(Color swingColor) {
        this.swingColor = swingColor;
    }

    public Color getSwingColor() {
        return swingColor;
    }
}
