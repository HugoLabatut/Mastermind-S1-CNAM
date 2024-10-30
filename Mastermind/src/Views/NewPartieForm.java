package Views;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class NewPartieForm {

    private final HashMap<String, Object> formInput;
    private HashMap<String, Object> formResult;

    public NewPartieForm(HashMap<String, Object> formInput) {
        this.formInput = formInput;
        this.formResult = showFormulaire();
    }

    public HashMap<String, Object> getFormResult() {
        return formResult;
    }

    public HashMap<String, Object> showFormulaire() {
        JTextField lengthCoupField = new JTextField((String) formInput.getOrDefault("Longueur de la combinaison", ""),
                10);
        JTextField maxColorsField = new JTextField((String) formInput.getOrDefault("Nombres de couleurs (max 9)", ""), 10);
        JTextField maxCoupsField = new JTextField((String) formInput.getOrDefault("Nombre de coups max)", ""),
                10);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.add(new JLabel("Longueur de la combinaison :")); panel.add(lengthCoupField);
        panel.add(new JLabel("Nombres de couleurs (max 9) :")); panel.add(maxColorsField);
        panel.add(new JLabel("Nombre de coups max :")); panel.add(maxCoupsField);

        int result;
        do {
            result = JOptionPane.showConfirmDialog(null, panel,"Formulaire de nouvelle partie",
                    JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                if (lengthCoupField.getText().trim().isEmpty() || maxColorsField.getText().trim().isEmpty() || maxCoupsField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Tous les champs doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                } else if (Integer.parseInt(maxColorsField.getText().trim()) > 9 || Integer.parseInt(maxColorsField.getText().trim()) < 0) {
                    JOptionPane.showMessageDialog(null, "Merci de saisir un nombre de couleur max entre 1 et 9 inclus" +
                                    ".", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
                else {
                    formResult = new HashMap<>();
                    formResult.put("lengthCoup", Integer.parseInt(lengthCoupField.getText().trim()));
                    formResult.put("maxColors", Integer.parseInt(maxColorsField.getText().trim()));
                    formResult.put("maxCoup", Integer.parseInt(maxCoupsField.getText().trim()));
                    return formResult;
                }
            } else {
                break;
            }
        } while (true);
        return formResult != null ? formResult : new HashMap<>();
    }

    public static void main(String[] args) {
        HashMap<String, Object> previousFormInput = new HashMap<>();

        NewPartieForm exempleFormulaire = new NewPartieForm(previousFormInput);
        HashMap<String, Object> formResult = exempleFormulaire.getFormResult();

        if (!formResult.isEmpty()) {
            System.out.println("Longueur de la combinaison : " + formResult.get("Longueur de la combinaison"));
            System.out.println("Nombres de couleurs (max 9) : " + formResult.get("Nombres de couleurs"));
            System.out.println("Nombre de coups max : " + formResult.get("Nombre de coups max"));
        } else {
            System.out.println("Formulaire annulé ou incomplet.");
        }
    }
}
