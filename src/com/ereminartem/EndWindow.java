package com.ereminartem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndWindow extends JFrame {
    private static final int WIN_HEIGHT = 230;
    private static final int WIN_WIDTH = 350;
    private final Map gameWindow;
    private String text;

    public EndWindow(Map gameWindow, String text) {
        this.text = text;
        this.gameWindow = gameWindow;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIN_WIDTH, WIN_HEIGHT);
        Rectangle gameWindowBounds = gameWindow.getBounds();
        int posX = (int) gameWindowBounds.getCenterX() - WIN_WIDTH / 2;
        int posY = (int) gameWindowBounds.getCenterY() - WIN_HEIGHT / 2;
        setLocation(posX, posY);
        setTitle("END GAME");
        setLayout(new BorderLayout());
        JLabel label = new JLabel(this.text);
        add(label, BorderLayout.NORTH);
        JPanel panel = new JPanel(new GridLayout(1, 2));
        JButton btnOk = new JButton("OK");
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new GameWindow();
                setVisible(false);
            }
        });
        JButton btnExit = new JButton("EXIT");
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        panel.add(btnOk);
        panel.add(btnExit);
        add(panel, BorderLayout.SOUTH);
        setVisible(true);
    }
}
