package Modele;

import java.util.ArrayList;
import java.util.HashMap;

public interface JoueurInterface {
    public int getId();

    public String getNom();

    public String toString();

    public String getOneJoueurInDb(int id);

    public ArrayList<HashMap<String, Object>> getAllJoueurInDb();

    public void createJoueurInDB();

}
