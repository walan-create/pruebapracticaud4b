package org.iesvdm.sudoku;

//import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SudokuTest {

    private Sudoku sudoku=new Sudoku();

    @Test
    public void numeroClues() {
        assertEquals(63, sudoku.getNumClues());
    }
    @Test
    public void copyBoard() {
        Sudoku sudoku1 = new Sudoku();
        sudoku1.fillBoardRandomly();
        int[][] board = sudoku1.getBoard();
        sudoku.copyBoard(board);
        int[][] copiedBoard = sudoku.getBoard();
        for (int i = 0; i < board.length; i++) {
            assertArrayEquals(board[i], copiedBoard[i]);
        }
    }
    @Test
    public void fillBoardRandomly() {
        sudoku.fillBoardRandomly();
        int[][] board = sudoku.getBoard();
        assertEquals(9, board.length);
        for (int i = 0; i < board.length; i++) {
            assertEquals(9, board[i].length);
        }
    }
    @Test
    public void fillBoardBasedInCluesRandomly() {
        sudoku.setNumClues(10);
        sudoku.fillBoardBasedInCluesRandomly();
        int[][] board = sudoku.getBoard();
        int countClues = 0;
        for (int[] row : board) {
            for (int cell : row) {
                if (cell != 0) {
                    countClues++;
                }
            }
        }
        assertEquals(10, countClues);
    }
    @Test
    public void fillBoardSolvable() {
        sudoku.fillBoardSolvable();
        Assertions.assertTrue(sudoku.solveBoard());
    }
    @Test
    public void fillBoardUnsolvable() {
        sudoku.fillBoardUnsolvable();
        Assertions.assertFalse(sudoku.solveBoard());
    }
    @Test
    public void putNumberInBoard() {
        sudoku.setBoard(new int[9][9]);
        sudoku.putNumberInBoard(5, 0, 0);
        assertEquals(5, sudoku.getBoard()[0][0]);
    }
    @Test
    public void isNumberInRow() {
        sudoku.setBoard(new int[9][9]);
        sudoku.putNumberInBoard(5, 0, 0);
        Assertions.assertTrue(sudoku.isNumberInRow(5, 0));
        Assertions.assertFalse(sudoku.isNumberInRow(5, 1));
    }
    @Test
    public void isNumberInColumn() {
        sudoku.setBoard(new int[9][9]);
        sudoku.putNumberInBoard(5, 0, 0);
        Assertions.assertTrue(sudoku.isNumberInColumn(5, 0));
        Assertions.assertFalse(sudoku.isNumberInColumn(5, 1));
    }
    @Test
    public void isNumberInBox() {
        sudoku.setBoard(new int[9][9]);
        sudoku.putNumberInBoard(5, 1, 1);
        Assertions.assertTrue(sudoku.isNumberInBox(5, 0, 0));
        Assertions.assertFalse(sudoku.isNumberInBox(5, 4, 4));
    }
    @Test
    public void isValidPlacement() {
        sudoku.setBoard(new int[9][9]);
        sudoku.putNumberInBoard(5, 0, 0);
        Assertions.assertFalse(sudoku.isValidPlacement(5, 0, 1));
        Assertions.assertTrue(sudoku.isValidPlacement(6, 0, 1));
    }
    @Test
    public void testUnsolveBoard() {
        sudoku.fillBoardSolvable();
        assertTrue(sudoku.solveBoard());
    }
    @Test
    public void testSolveBoard() {
        sudoku.fillBoardUnsolvable();
        assertFalse(sudoku.solveBoard());
    }


}
