package Modele;

public interface JoueurInterface {
    public int getId();

    public String getNom();

    public String toString();

    public String getOneJoueurInDb(int id);

    public String getAllJoueurInDb();

    public void createJoueurInDB();

}
