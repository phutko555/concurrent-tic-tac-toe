package com.epam.rd.autocode.concurrenttictactoe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.epam.rd.autocode.concurrenttictactoe.ThrowingConsumer.silentConsumer;
import static com.epam.rd.autocode.concurrenttictactoe.Utils.tableString;
import static org.junit.jupiter.api.Assertions.*;

class TicTacToeTest {

        @Test
        public void testEmptyTable() {
            TicTacToe ticTacToe = TicTacToe.buildGame();
            char[][] expected = new char[][]{
                    {'\0', '\0', '\0'},
                    {'\0', '\0', '\0'},
                    {'\0', '\0', '\0'}
            };
            assertArrayEquals(expected, ticTacToe.table(), "The board should be empty initially.");
        }



    @Test
    public void testSemiFilledTable() {
        TicTacToe ticTacToe = TicTacToe.buildGame();
        ticTacToe.setMark(0, 0, 'X');
        ticTacToe.setMark(0, 1, 'O');
        ticTacToe.setMark(0, 2, 'X');

        char[][] expected = {
                {'X', 'O', 'X'},
                {'\0', '\0', '\0'},
                {'\0', '\0', '\0'}
        };

        assertArrayEquals(expected, ticTacToe.table(), "Board should match expected after moves.");
    }

    @Test
    void testFilledTable() {
        final TicTacToe ticTacToe = TicTacToe.buildGame();

        ticTacToe.setMark(0,0, 'X');
        ticTacToe.setMark(0,1, 'O');
        ticTacToe.setMark(0,2, 'X');

        ticTacToe.setMark(1,0, 'O');
        ticTacToe.setMark(1,1, 'X');
        ticTacToe.setMark(1,2, 'O');

        ticTacToe.setMark(2,1, 'X');
        ticTacToe.setMark(2,0, 'O');
        ticTacToe.setMark(2,2, 'X');

        assertEquals("" +
                "XOX\n" +
                "OXO\n" +
                "OXX", tableString(ticTacToe.table()));
    }

    @Test
    public void testNoMoveAfterWin() {
        TicTacToe ticTacToe = TicTacToe.buildGame();
        ticTacToe.setMark(0, 0, 'X');
        ticTacToe.setMark(0, 1, 'X');
        ticTacToe.setMark(0, 2, 'X');  // This should win the game

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            ticTacToe.setMark(1, 1, 'O');  // Attempting to play after win
        });

        assertEquals("Cannot place mark after the game is won", exception.getMessage());
    }

    @Test
    void testTableMaybeFilledInAnyOrder() {
        final TicTacToe ticTacToe = TicTacToe.buildGame();

        ticTacToe.setMark(0,0, 'X');
        ticTacToe.setMark(1,0, 'X');
        ticTacToe.setMark(0,1, 'X');
        ticTacToe.setMark(1,1, 'X');
        ticTacToe.setMark(0,2, 'X');
        ticTacToe.setMark(1,2, 'X');
        ticTacToe.setMark(2,0, 'X');
        ticTacToe.setMark(2,1, 'O');
        ticTacToe.setMark(2,2, 'O');

        assertEquals("" +
                "XXX\n" +
                "XXX\n" +
                "XOO", tableString(ticTacToe.table()));
    }

    @Test
    void testLastMark() {
        final TicTacToe ticTacToe = TicTacToe.buildGame();

        ticTacToe.setMark(0,0, 'X');
        assertEquals('X', ticTacToe.lastMark());
        ticTacToe.setMark(1,0, 'X');
        assertEquals('X', ticTacToe.lastMark());
        ticTacToe.setMark(0,1, 'O');
        assertEquals('O', ticTacToe.lastMark());
        ticTacToe.setMark(1,1, 'O');
        assertEquals('O', ticTacToe.lastMark());
        ticTacToe.setMark(0,2, 'X');
        assertEquals('X', ticTacToe.lastMark());
        ticTacToe.setMark(1,2, 'X');
        assertEquals('X', ticTacToe.lastMark());
        ticTacToe.setMark(2,0, 'X');
        assertEquals('X', ticTacToe.lastMark());
        ticTacToe.setMark(2,1, 'O');
        assertEquals('O', ticTacToe.lastMark());
        ticTacToe.setMark(2,2, 'O');
        assertEquals('O', ticTacToe.lastMark());

        assertEquals("" +
                "XOX\n" +
                "XOX\n" +
                "XOO", tableString(ticTacToe.table()));
    }

    @Test
    void testCannotSetMarkTwice() {
        final TicTacToe ticTacToe = TicTacToe.buildGame();

        ticTacToe.setMark(0,0, 'X');
        assertThrows(IllegalArgumentException.class, () -> ticTacToe.setMark(0,0, 'X'));
        assertThrows(IllegalArgumentException.class, () -> ticTacToe.setMark(0,0, 'O'));

        ticTacToe.setMark(1,0, 'O');
        ticTacToe.setMark(2,0, 'X');
        assertThrows(IllegalArgumentException.class, () -> ticTacToe.setMark(1,0, 'X'));
        assertThrows(IllegalArgumentException.class, () -> ticTacToe.setMark(2,0, 'O'));

        assertEquals("" +
                "X  \n" +
                "O  \n" +
                "X  ", tableString(ticTacToe.table()));

    }

    @Test
    void testTableIsCopy(){
        final TicTacToe ticTacToe = TicTacToe.buildGame();

        ticTacToe.setMark(0,0, 'X');
        ticTacToe.setMark(1,0, 'O');
        ticTacToe.setMark(2,0, 'X');

        assertEquals("" +
                "X  \n" +
                "O  \n" +
                "X  ", tableString(ticTacToe.table()));

        ticTacToe.table()[1][1] = 'X';
        assertEquals("" +
                "X  \n" +
                "O  \n" +
                "X  ", tableString(ticTacToe.table()));

    }


}