package Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuView extends JFrame {
    public MenuView() {
        setTitle("Mastermind - Menu principal");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel menuPanel = new JPanel(new BorderLayout(2,2));
        JLabel menuTitle = new JLabel("Mastermind");
        menuTitle.setFont(new Font("Arial", Font.PLAIN, 20));
        menuTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JButton nouvpartieBtn = new JButton("Nouvelle partie");

        JButton quitterappBtn = new JButton("Quitter le logiciel");

        add(menuTitle, BorderLayout.NORTH);
        add(nouvpartieBtn, BorderLayout.CENTER);
        add(quitterappBtn, BorderLayout.SOUTH);

        quitterappBtn.addActionListener(e -> fermerApp());

        setVisible(true);
    }

    private void fermerApp() {
        int reponse = JOptionPane.showConfirmDialog(
                null,
                "Voulez-vous quitter l'application ?",
                "Quitter l'application",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if(reponse == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
