package Enums;

public enum Couleur {
    ROUGE(1),
    BLEU(2),
    VERT(3),
    JAUNE(4),
    NOIR(5),
    ORANGE(6),
    MARRON(7),
    FUCHSIA(8);

    private final int valCouleur;

    private Couleur(int coul) {
        this.valCouleur = coul;
    }

    public int getCouleur() {
        return this.valCouleur;
    }
}
