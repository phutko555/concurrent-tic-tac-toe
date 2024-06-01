package com.epam.rd.autocode.concurrenttictactoe;

public class PlayerImpl implements Player {
    private final TicTacToe ticTacToe;
    private final char mark;
    private final PlayerStrategy strategy;

    public PlayerImpl(TicTacToe ticTacToe, char mark, PlayerStrategy strategy) {
        this.ticTacToe = ticTacToe;
        this.mark = mark;
        this.strategy = strategy;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                synchronized (ticTacToe) {
                    if (ticTacToe.lastMark() != mark) {
                        Move move = strategy.computeMove(mark, ticTacToe);
                        if (move != null) {
                            ticTacToe.setMark(move.row, move.column, mark);
                            ticTacToe.notifyAll(); // Notify other player's thread
                        }
                    }
                }
                Thread.sleep(10); // Prevent tight loop, simulate thinking
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static Player createPlayer(TicTacToe ticTacToe, char mark, PlayerStrategy strategy) {
        return new PlayerImpl(ticTacToe, mark, strategy);
    }
}
