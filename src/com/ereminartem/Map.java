package com.ereminartem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Map extends JPanel {

    public static final int MODE_H_V_A = 0;
    public static final int MODE_H_V_H = 1;
    static final char player_DOT = 'X';
    static final char Ai_DOT = 'O';
    static final char EMPTY_DOT = '.';
    private boolean moveFlag = true;
    static Random rnd = new Random();
    int[][] field;
    int fieldSizeX;
    int fieldSizeY;
    int winLeght;
    int cellheight;
    int cellWidth;

    boolean isInitialized = false;


    Map() {
        setBackground(Color.orange);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                update(e);
            }
        });
    }

    void update(MouseEvent e) {
        int cellX = e.getX() / cellWidth;
        int cellY = e.getY() / cellheight;
        if (moveFlag && checkMove(cellX, cellY)) {
            playerMove(cellX, cellY, player_DOT);
            if (!checkEndGame(player_DOT)) {
                AiMove();
                checkEndGame(Ai_DOT);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    void startNewGame(int mode, int fieldSizeX, int fieldSizeY, int winLength) {
        System.out.println("mode = " + mode +
                " fsX = " + fieldSizeX +
                " fsy = " + fieldSizeY +
                " winlen = " + winLength);
        this.fieldSizeX = fieldSizeX;
        this.fieldSizeY = fieldSizeY;
        this.winLeght = winLength;
        field = new int[fieldSizeY][fieldSizeX];
        emtpyField();
        isInitialized = true;
        repaint();
    }

    void render(Graphics g) {
        if (!isInitialized) return;
        int panelWidth = getWidth();
        int panelHeigt = getHeight();
        cellheight = panelHeigt / fieldSizeY;
        cellWidth = panelWidth / fieldSizeX;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5.0f));
        for (int i = 0; i < fieldSizeY; i++) {
            int y = i * cellheight;
            g2.drawLine(0, y, panelWidth, y);
        }

        for (int i = 0; i < fieldSizeX; i++) {
            int x = i * cellWidth;
            g2.drawLine(x, 0, x, panelHeigt);
        }
    }

    public void drawLines(Graphics g, int x, int y) {
        if (!isInitialized) return;
        int panelWidth = getWidth();
        int panelHeigt = getHeight();
        cellheight = panelHeigt / fieldSizeY;
        cellWidth = panelWidth / fieldSizeX;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5.0f));
        g2.setColor(Color.blue);
        g2.drawLine(cellWidth * x, cellheight * y, cellWidth * (x + 1), cellheight * (y + 1));
        g2.drawLine(cellWidth * (x + 1), cellheight * y, cellWidth * x, cellheight * (y + 1));
    }

    public void drawCircle(Graphics g, int x, int y) {
        if (!isInitialized) return;
        int panelWidth = getWidth();
        int panelHeigt = getHeight();
        cellheight = panelHeigt / fieldSizeY;
        cellWidth = panelWidth / fieldSizeX;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5.0f));
        g2.setColor(Color.red);
        g2.drawOval(cellWidth * x, cellheight * y, cellWidth, cellheight);
    }

    private void emtpyField() {
        for (int i = 0; i < this.field.length; i++) {
            for (int j = 0; j < this.field[0].length; j++) {
                this.field[i][j] = EMPTY_DOT;
            }
        }
    }

    public boolean checkEndGame(char symb) {
        if (fullField()) {
            new EndWindow(this, "Friendship WIN!!!");
            return true;
        }
        if (checkWin(symb)) {
            switch (symb) {
                case player_DOT:
                    new EndWindow(this, "Player WIN!!!");
                    return true;
                case Ai_DOT:
                    new EndWindow(this, "AI WIN!!!!");
                    return true;
            }
        }
        return false;
    }

    public void AiMove() {
        int x, y;
        for (int v = 0; v < this.field.length; v++) {
            for (int h = 0; h < this.field[0].length; h++) {
                if (h + winLeght <= this.field[0].length) {                           //по горизонтале
                    if (checkLineHorisont(v, h, player_DOT) == winLeght - 1) {
                        if (MoveAiLineHorisont(v, h, Ai_DOT)) return;
                    }

                    if (v - winLeght > -2) {                            //вверх по диагонале
                        if (checkDiaUp(v, h, player_DOT) == winLeght - 1) {
                            if (MoveAiDiaUp(v, h, Ai_DOT)) return;
                        }
                    }
                    if (v + winLeght <= this.field.length) {                       //вниз по диагонале
                        if (checkDiaDown(v, h, player_DOT) == winLeght - 1) {
                            if (MoveAiDiaDown(v, h, Ai_DOT)) return;
                        }
                    }
                }
                if (v + winLeght <= this.field.length) {                       //по вертикале
                    if (checkLineVertical(v, h, player_DOT) == winLeght - 1) {
                        if (MoveAiLineVertical(v, h, Ai_DOT)) return;
                    }
                }
            }
        }
        //игра на победу
        for (int v = 0; v < this.field.length; v++) {
            for (int h = 0; h < this.field[0].length; h++) {
                if (h + winLeght <= this.field[0].length) {                           //по горизонтале
                    if (checkLineHorisont(v, h, Ai_DOT) == winLeght - 1) {
                        if (MoveAiLineHorisont(v, h, Ai_DOT)) return;
                    }
                    if (v - winLeght > -2) {                            //вверх по диагонале
                        if (checkDiaUp(v, h, Ai_DOT) == winLeght - 1) {
                            if (MoveAiDiaUp(v, h, Ai_DOT)) return;
                        }
                    }
                    if (v + winLeght <= this.field.length) {                       //вниз по диагонале
                        if (checkDiaDown(v, h, Ai_DOT) == winLeght - 1) {
                            if (MoveAiDiaDown(v, h, Ai_DOT)) return;
                        }
                    }
                }
                if (v + winLeght <= this.field.length) {                       //по вертикале
                    if (checkLineVertical(v, h, Ai_DOT) == winLeght - 1) {
                        if (MoveAiLineVertical(v, h, Ai_DOT)) return;
                    }
                }
            }
        }

        //случайный ход
        do {
            y = rnd.nextInt(this.field.length);
            x = rnd.nextInt(this.field[0].length);
        } while (!checkMove(y, x));
        dotField(y, x, Ai_DOT);
        drawCircle(getGraphics(), y, x);
        moveFlag = true;
    }

    private boolean MoveAiLineVertical(int v, int h, char dot) {
        for (int i = v; i < winLeght; i++) {
            if ((field[i][h] == EMPTY_DOT)) {
                field[i][h] = dot;
                drawCircle(getGraphics(), i, h);
                moveFlag = true;
                return true;
            }
        }
        return false;
    }

    private boolean MoveAiDiaDown(int v, int h, char dot) {
        for (int i = 0; i < winLeght; i++) {
            if ((field[i + v][i + h] == EMPTY_DOT)) {
                field[i + v][i + h] = dot;
                drawCircle(getGraphics(), i + v, i + h);
                moveFlag = true;
                return true;
            }
        }
        return false;
    }

    private boolean MoveAiDiaUp(int v, int h, char dot) {
        for (int i = 0, j = 0; j < winLeght; i--, j++) {
            if ((field[v + i][h + j] == EMPTY_DOT)) {
                field[v + i][h + j] = dot;
                drawCircle(getGraphics(), i + v, j + h);
                moveFlag = true;
                return true;
            }
        }
        return false;
    }

    private boolean MoveAiLineHorisont(int v, int h, char dot) {
        for (int j = h; j < winLeght; j++) {
            if ((field[v][j] == EMPTY_DOT)) {
                field[v][j] = dot;
                drawCircle(getGraphics(), v, j);
                moveFlag = true;
                return true;
            }
        }
        return false;
    }

    private boolean fullField() {
        for (int i = 0; i < this.field.length; i++) {
            for (int j = 0; j < this.field[0].length; j++) {
                if (field[i][j] == EMPTY_DOT) return false;
            }
        }
        return true;
    }

    private boolean checkWin(char dot) {
        for (int v = 0; v < this.field.length; v++) {
            for (int h = 0; h < this.field[0].length; h++) {
                if (h + winLeght <= this.field[0].length) {//по горизонтале
                    if (checkLineHorisont(v, h, dot) >= winLeght) return true;

                    if (v - winLeght > -2) {                            //вверх по диагонале
                        if (checkDiaUp(v, h, dot) >= winLeght) return true;
                    }
                    if (v + winLeght <= this.field.length) {                       //вниз по диагонале
                        if (checkDiaDown(v, h, dot) >= winLeght) return true;
                    }
                }
                if (v + winLeght <= this.field.length) {                       //по вертикале
                    if (checkLineVertical(v, h, dot) >= winLeght) return true;
                }
            }
        }
        return false;
    }

    private int checkLineVertical(int v, int h, char dot) {
        int count = 0;
        for (int i = v; i < winLeght; i++) {
            if ((field[i][h] == dot)) count++;
        }
        return count;
    }

    private int checkDiaDown(int v, int h, char dot) {
        int count = 0;
        for (int i = 0; i < winLeght; i++) {
            if ((field[i + v][i + h] == dot)) count++;
        }
        return count;
    }

    private int checkDiaUp(int v, int h, char dot) {
        int count = 0;
        for (int i = 0, j = 0; j < winLeght; i--, j++) {
            if ((field[v + i][h + j] == dot)) count++;
        }
        return count;
    }

    private int checkLineHorisont(int v, int h, char dot) {
        int count = 0;
        for (int j = h; j < winLeght; j++) {
            if ((field[v][j] == dot)) count++;
        }
        return count;
    }


    private void playerMove(int row, int column, char dot) {
        if (checkMove(row, column)) ;
        {
            dotField(row, column, dot);
            drawLines(getGraphics(), row, column);
            moveFlag = false;
        }
    }

    public boolean checkMove(int row, int column) {
        if (!(field[row][column] == EMPTY_DOT)) return false;
        return true;
    }

    private void printFieldLine() {
        for (int i = 0; i < this.field.length * 2 + 1; i++) {
            System.out.print("-");
        }
        System.out.println("");
    }

    private void printField() {
        printFieldLine();
        for (int i = 0; i < this.field.length; i++) {
            System.out.print("|");
            for (int j = 0; j < this.field[0].length; j++) {
                System.out.print(field[i][j] + "|");
            }
            System.out.println("");
        }
        printFieldLine();
    }

    private void dotField(int y, int x, char dot) {
        field[y][x] = dot;
    }
}
