package Utils;

import Enums.VuesTypes;
import Views.JeuView;

import javax.swing.*;
import java.util.HashMap;

public class FactoryVues {
    public JFrame createView(VuesTypes typeVue){
        return switch (typeVue) {
//            case JOUEURS -> new MenuView();
//            case PARTIES -> new Motorcycle();
            case JEU -> new JeuView();
            default ->  null;
        };
    }

}
