package com.epam.rd.autocode.concurrenttictactoe;

import java.util.Arrays;

public class TicTacToeImpl implements TicTacToe {
    private final char[][] board = new char[3][3];
    private boolean gameWon = false;  // This flag will indicate if the game has been won

    public TicTacToeImpl() {
        for (int i = 0; i < 3; i++) {
            Arrays.fill(board[i], '\0');  // Initialize all cells to be empty.
        }
    }


    private char lastMark = '\0';
    private boolean checkWin(char mark, int x, int y) {
        // Check horizontal, vertical, and diagonal conditions
        // Horizontal check
        if (board[x][0] == mark && board[x][1] == mark && board[x][2] == mark) {
            return true;
        }
        // Vertical check
        if (board[0][y] == mark && board[1][y] == mark && board[2][y] == mark) {
            return true;
        }
        // Diagonal check
        if (x == y && board[0][0] == mark && board[1][1] == mark && board[2][2] == mark) {
            return true;
        }
        if (x + y == 2 && board[0][2] == mark && board[1][1] == mark && board[2][0] == mark) {
            return true;
        }
        return false;
    }

    @Override
    public synchronized void setMark(int x, int y, char mark) {
        if (gameWon) {
            throw new IllegalStateException("Cannot place mark after the game is won");
        }
        if (board[x][y] != '\0') {
            throw new IllegalArgumentException("Cell is already occupied");
        }
        board[x][y] = mark;
        // After setting the mark, check if it results in a win
        if (checkWin(mark, x, y)) {
            gameWon = true;
        }
    }



    @Override
    public synchronized char[][] table() {
        return Arrays.stream(board).map(char[]::clone).toArray(char[][]::new);
    }

    @Override
    public char lastMark() {
        return lastMark;
    }

    public static TicTacToe buildGame() {
        return new TicTacToeImpl();
    }
}
