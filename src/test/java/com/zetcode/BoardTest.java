package com.zetcode;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import java.util.Arrays;

import static org.junit.Assert.*;

public class BoardTest {
    private Board board;
    private JLabel statusBar;

    @Before
    public void setUp() {
        statusBar = new JLabel();
        board = new Board(statusBar);
    }

    @Test
    public void testInitializeField() {
        board.newGame();
        int[] expectedField = new int[board.getField().length];
        Arrays.fill(expectedField, Board.COVER_FOR_CELL);
        assertNotNull(board.getField());

    }

    @Test
    public void testPlaceMines() {
        board.newGame();
        int mineCount = 0;

        for (int cell : board.getField()) {
            if (cell == Board.COVERED_MINE_CELL) {
                mineCount++;
            }
        }

        assertEquals(40, mineCount); // Check if 40 mines are placed
    }

    @Test
    public void testUpdateAdjacentCells() {
        // Manually place a mine at position 0
        board.setField(new int[board.getField().length]);
        board.getField()[0] = Board.COVERED_MINE_CELL;

        // Update adjacent cells for the mine at position 0
        board.updateAdjacentCells(0);

        // Check the surrounding cells
        assertNotEquals(Board.COVER_FOR_CELL, board.getField()[1]); // Check against a specific covered value


    }

    @Test
    public void testFindEmptyCells() {
        board.newGame();
        // Simulate an empty cell at position 5
        board.getField()[5] = Board.EMPTY_CELL;

        // Manually clear adjacent cells
        board.findEmptyCells(5);

        // Check if adjacent cells are cleared
        for (int i = 0; i < board.getField().length; i++) {
            if (i != 5 && board.getField()[i] > Board.MINE_CELL) {
                assertNotEquals(1, board.getField()[1]); // should be uncovered
            }
        }
    }


}
