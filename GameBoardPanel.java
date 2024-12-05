package sudoku;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // Define named constants for UI sizes
    public static final int CELL_SIZE = 60;   // Cell width/height in pixels
    public static final int BOARD_WIDTH  = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;

    // Board width/height in pixels
    private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    private Puzzle puzzle = new Puzzle();

    private int filledCellsCount = 0;  // Jumlah sel yang diisi dengan benar
    private int totalCells = SudokuConstants.GRID_SIZE * SudokuConstants.GRID_SIZE;

    /** Constructor */
    public GameBoardPanel() {
        super.setLayout(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));

        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col);
                super.add(cells[row][col]);
            }
        }

        CellInputListener listener = new CellInputListener();
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].isEditable()) {
                    cells[row][col].addActionListener(listener);
                }
            }
        }

        super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    }

    /**
     * Generate a new puzzle; and reset the game board of cells based on the puzzle.
     * You can call this method to start a new game.
     */
    public void newGame() {
        puzzle.newPuzzle(2);
        filledCellsCount = 0;  // Reset jumlah sel yang terisi
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
            }
        }
    }

    /**
     * Return true if the puzzle is solved
     * i.e., none of the cell have status of TO_GUESS or WRONG_GUESS
     */
    public boolean isSolved() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].status == CellStatus.TO_GUESS || cells[row][col].status == CellStatus.WRONG_GUESS) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Mengembalikan jumlah sel yang sudah terisi
     */
    public int getFilledCellsCount() {
        return filledCellsCount;
    }

    /**
     * Mengembalikan total jumlah sel
     */
    public int getTotalCells() {
        return totalCells;
    }

    // Listener untuk input di sel
    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Cell sourceCell = (Cell) e.getSource();
            try {
                int numberIn = Integer.parseInt(sourceCell.getText());
                if (numberIn == sourceCell.number) {
                    if (sourceCell.status != CellStatus.CORRECT_GUESS) {
                        filledCellsCount++;  // Tambah jumlah sel yang terisi dengan benar
                    }
                    sourceCell.status = CellStatus.CORRECT_GUESS;
                } else {
                    if (sourceCell.status == CellStatus.CORRECT_GUESS) {
                        filledCellsCount--;  // Kurangi jumlah sel yang terisi jika sebelumnya benar
                    }
                    sourceCell.status = CellStatus.WRONG_GUESS;
                }
                sourceCell.paint();  // Repaint cell
            } catch (NumberFormatException ex) {
                sourceCell.setText("");  // Reset jika input tidak valid
            }

            if (isSolved()) {
                JOptionPane.showMessageDialog(null, "Congratulations! You solved the puzzle!");
                newGame();
            }
        }
    }
}
