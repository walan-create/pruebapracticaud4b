package org.iesvdm.sudoku;

public class Sudoku {

    private int gridSize = 9;
    private int numClues = 63;
    private int[][] board;

    public int getNumClues() {
        return numClues;
    }

    public void setNumClues(int numClues) {
        this.numClues = numClues;
    }
    public int[][] getBoard() {
        return board;
    }
    public void setBoard(int[][] board) {
        this.board = board;
    }
    public int getGridSize() {
        return gridSize;
    }
    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    void fillBoardRandomly() {
        board = new int[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                board[i][j] = (int)(Math.random()*(gridSize+1));
            }
        }
    }

    void fillBoardBasedInCluesRandomly(){

        board = new int[gridSize][gridSize];
        int cont = 0;
        int i = 0;
        int j = 0;
        while (cont < numClues) {
            i = (int) (Math.random() * gridSize);
            j = (int) (Math.random() * gridSize);
            if (board[i][j] == 0) {
                board[i][j] = 1 + (int)(Math.random() * (gridSize));
                cont++;
            }
        }

    }

    void fillBoardBasedInCluesRandomlySolvable() {
        Sudoku sudoku = null;
        do {
                fillBoardBasedInCluesRandomly();
                sudoku = new Sudoku();
                sudoku.gridSize=gridSize;
                sudoku.copyBoard(board);
        }while(sudoku.solveBoard());
    }

    void fillBoardSolvable() {
        Sudoku sudokuAux = new Sudoku();
        sudokuAux.setGridSize(gridSize);
        do {
            sudokuAux.fillBoardRandomly();
            copyBoard(sudokuAux.board);
        } while(!sudokuAux.solveBoard());
    }

    void fillBoardUnsolvable() {
        Sudoku sudokuAux = new Sudoku();
        sudokuAux.setGridSize(gridSize);
        do {
            sudokuAux.fillBoardRandomly();
            copyBoard(sudokuAux.board);
        } while(sudokuAux.solveBoard());
    }

    void copyBoard(int[][] boardSrc) {
        this.board = new int[gridSize][gridSize];
        for (int i = 0; i < boardSrc.length; i++) {
            System.arraycopy(boardSrc[i], 0, this.board[i], 0, gridSize);
        }
    }

    void putNumberInBoard(int number, int row, int column) {
        this.board[row][column] = number;
    }

    void printBoard() {
        for (int row = 0; row < gridSize; row++) {
            if (row % 3 == 0 && row != 0) {
                System.out.println("-----------");
            }
            for (int column = 0; column < gridSize; column++) {
                if (column % 3 == 0 && column != 0) {
                    System.out.print("|");
                }
                System.out.print(board[row][column]);
            }
            System.out.println();
        }
    }


    boolean isNumberInRow(int number, int row) {
        for (int i = 0; i < gridSize; i++) {
            if (board[row][i] == number) {
                return true;
            }
        }
        return false;
    }

    boolean isNumberInColumn(int number, int column) {
        for (int i = 0; i < gridSize; i++) {
            if (board[i][column] == number) {
                return true;
            }
        }
        return false;
    }

    boolean isNumberInBox(int number, int row, int column) {
        int localBoxRow = row - row % 3;
        int localBoxColumn = column - column % 3;

        for (int i = localBoxRow; i < localBoxRow + 3; i++) {
            for (int j = localBoxColumn; j < localBoxColumn + 3; j++) {
                if (board[i][j] == number) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean isValidPlacement(int number, int row, int column) {
        return !isNumberInRow(number, row) &&
                !isNumberInColumn(number, column) &&
                !isNumberInBox(number, row, column);
    }

    boolean solveBoard() {
        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {
                if (board[row][column] == 0) {
                    for (int numberToTry = 1; numberToTry <= gridSize; numberToTry++) {
                        if (isValidPlacement( numberToTry, row, column)) {
                            board[row][column] = numberToTry;

                            if (solveBoard()) {
                                return true;
                            }
                            else {
                                board[row][column] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

}

