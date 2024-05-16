package org.iesvdm.sudoku;

//import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
public class SudokuTest {

    @Test
    void failTest() {
        Sudoku sudoku = new Sudoku();
        sudoku.fillBoardBasedInCluesRandomlySolvable();
        //sudoku.fillBoardBasedInCluesRandomly();
        sudoku.printBoard();
    }

}
