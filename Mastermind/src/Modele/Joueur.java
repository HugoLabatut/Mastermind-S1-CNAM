package Modele;

public class Joueur implements JoueurInterface {
    private int id;
    private String nom;

    public Joueur(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public int getId() {
        return this.id;
    }

    public String getNom() {
        return this.nom;
    }

    public String toString() {
        return "ID: " + this.id + ", Nom: " + this.nom;
    }

    public String GetJoueur() {
        return "";
    }
}