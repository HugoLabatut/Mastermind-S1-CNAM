package Controllers;

import Views.ListeJoueursView;
import Views.MenuView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListeJoueursController {
    private ListeJoueursView view;

    public ListeJoueursController(ListeJoueursView view) {
        this.view = view;

        this.view.getBoutonRetourBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
                MenuView menuView = new MenuView();
                new MenuController(menuView);
            }
        });

        this.view.getBoutonNouvPartieBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Coder fonction Nouvelle partie.");
            }
        });

        this.view.getBoutonSelectBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = view.getListeTable().getSelectedRow();
                if(selectedRow != -1) {
                    Object id = view.getListeTable().getValueAt(selectedRow, 0);
                    Object nom = view.getListeTable().getValueAt(selectedRow, 1);

                    JOptionPane.showMessageDialog(
                            null,
                            "ID : " + id + "\nNom : " + nom,
                            "DEBUG joueur sélection.",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Veuillez sélectionner un joueur.",
                            "Avertissement",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });
    }
}
