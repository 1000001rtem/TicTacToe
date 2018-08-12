package com.ereminartem;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class StartNewGameWindow extends JFrame {

    private final GameWindow gameWindow;

    private static final int WIN_HEIGHT = 230;
    private static final int WIN_WIDTH = 350;
    private static final int MIN_WIN_LEN = 3; // минимальная выйграшная длина
    private static final int MAX_WIN_LEN = 10; // максимальная выйграшная длина
    private static final int MIN_FIELD_SIZE = 3;
    private static final int MAX_FIELD_SIZE = 10;
    private static final String STR_WIN_LEN = "Winning Length: ";
    private static final String STR_FIELD_SIZE = "Field Size: ";

    private JRadioButton jrbHumVsAi = new JRadioButton("Human vs. AI", true);
    private JRadioButton jrbHumVsHum = new JRadioButton("Human vs. Human");
    private ButtonGroup gameMode = new ButtonGroup();

    private JSlider slFieldSize;
    private JSlider slWinLength;

    StartNewGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        setSize(WIN_WIDTH, WIN_HEIGHT);
        Rectangle gameWindowBounds = gameWindow.getBounds();
        int posX = (int) gameWindowBounds.getCenterX() - WIN_WIDTH / 2;
        int posY = (int) gameWindowBounds.getCenterY() - WIN_HEIGHT / 2;
        setLocation(posX, posY);
        setTitle("New game parameters");

        setLayout(new GridLayout(10, 1));

        addGameContolsMode();
        addGameControlsFieldWinLenght();

        JButton btnStartGame = new JButton("Start a game");
        btnStartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnStartGameClick();
            }
        });
        add(btnStartGame);
    }

    void btnStartGameClick() {
        int gameMode;
        if (jrbHumVsAi.isSelected())
            gameMode = Map.MODE_H_V_A;
        else if (jrbHumVsHum.isSelected())
            gameMode = Map.MODE_H_V_H;
        else
            throw new RuntimeException("No buttons selected");

        int fieldSize = slFieldSize.getValue();
        int winLength = slWinLength.getValue();
        gameWindow.startNewGame(gameMode, fieldSize, fieldSize, winLength);
        setVisible(false);
    }

    void addGameContolsMode() {
        add(new JLabel("Choose gaming mode:"));
        gameMode.add(jrbHumVsAi);
        gameMode.add(jrbHumVsHum);
        add(jrbHumVsHum);
        add(jrbHumVsAi);
    }

    void addGameControlsFieldWinLenght() {
        add(new JLabel("Choose field size:"));
        final JLabel lblFieldSize = new JLabel(STR_FIELD_SIZE + MIN_FIELD_SIZE);
        add(lblFieldSize);


        slFieldSize = new JSlider(MIN_FIELD_SIZE, MAX_FIELD_SIZE, MIN_FIELD_SIZE);
        slFieldSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int currentFieldSize = slFieldSize.getValue();
                lblFieldSize.setText(STR_FIELD_SIZE + currentFieldSize);
                slWinLength.setMaximum(currentFieldSize);
            }
        });
        add(slFieldSize);

        add(new JLabel("Choose winning length:"));
        final JLabel lblWinLen = new JLabel(STR_WIN_LEN + MIN_WIN_LEN);
        add(lblWinLen);

        slWinLength = new JSlider(MIN_WIN_LEN, MAX_WIN_LEN, MIN_WIN_LEN);
        slWinLength.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblWinLen.setText(STR_WIN_LEN + slWinLength.getValue());
            }
        });
        add(slWinLength);
    }
}
