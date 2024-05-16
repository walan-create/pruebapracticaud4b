package org.iesvdm.sudoku;

public class Main {
    public static void main(String[] args) {

        Sudoku sudokuSolver = new Sudoku();

        //sudokuSolver.setBoard(board);
        sudokuSolver.setGridSize(9);
        sudokuSolver.fillBoardRandomly();

        sudokuSolver.printBoard();

        if (sudokuSolver.solveBoard()) {
            System.out.println("Solved successfully!");
        } else {
            System.out.println("Unsolvable board :(");
        }

        sudokuSolver.printBoard();

    }
}
