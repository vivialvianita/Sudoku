package Sudoku;

import sudoku.GameBoardPanel;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The main Sudoku program with modified features for user experience.
 */
public class SudokuMain extends JFrame {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // private variables
    GameBoardPanel board = new GameBoardPanel();
    JButton btnNewGame = new JButton("New Game");
    JButton btnPause = new JButton("Pause");
    JButton btnResume = new JButton("Resume");
    JButton btnSaveProgress = new JButton("Save Progress");
    JButton btnLoadProgress = new JButton("Load Progress");

    JLabel lblTime = new JLabel("Time: 0s", SwingConstants.CENTER);
    JLabel lblScore = new JLabel("Score: 1000", SwingConstants.CENTER);
    JLabel lblProgress = new JLabel("Progress: 0%", SwingConstants.CENTER);
    JLabel lblLevel = new JLabel("Level: 1", SwingConstants.CENTER);

    private Timer timer;
    private int elapsedTime = 0;
    private int score = 1000;
    private boolean isPaused = false;
    private int level = 1;
    private int bestTime = Integer.MAX_VALUE;

    // Constructor
    public SudokuMain() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        // Panel atas untuk menampilkan waktu, skor, progress, dan level
        JPanel topPanel = new JPanel(new GridLayout(1, 4));
        topPanel.add(lblTime);
        topPanel.add(lblScore);
        topPanel.add(lblProgress);
        topPanel.add(lblLevel);
        cp.add(topPanel, BorderLayout.NORTH);

        cp.add(board, BorderLayout.CENTER);

        // Menambahkan tombol New Game, Pause, Resume, Save, dan Load di bagian bawah
        JPanel bottomPanel = new JPanel(new FlowLayout());
        btnNewGame.addActionListener(e -> newGame());
        btnPause.addActionListener(e -> togglePause(true));
        btnResume.addActionListener(e -> togglePause(false));
        btnSaveProgress.addActionListener(e -> saveProgress());
        btnLoadProgress.addActionListener(e -> loadProgress());

        bottomPanel.add(btnNewGame);
        bottomPanel.add(btnPause);
        bottomPanel.add(btnResume);
        bottomPanel.add(btnSaveProgress);
        bottomPanel.add(btnLoadProgress);
        cp.add(bottomPanel, BorderLayout.SOUTH);

        // Initialize the game board to start the game
        board.newGame();

        pack();  // Pack the UI components
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Handle window-closing
        setTitle("Sudoku");
        setVisible(true);

        startTimer();  // Start the timer
    }

    public static void main(String[] args) {
        new SudokuMain();
    }

    // Timer untuk menghitung waktu permainan
    private void startTimer() {
        timer = new Timer(1000, e -> {
            if (!isPaused) {
                elapsedTime++;
                lblTime.setText("Time: " + elapsedTime + "s");

                if (elapsedTime % 5 == 0) {
                    score -= 5;
                    updateScore();
                }
                updateProgress();
            }
        });
        timer.start();
    }

    // Mengatur jeda atau lanjutkan permainan
    private void togglePause(boolean pause) {
        isPaused = pause;
    }

    // Memulai permainan baru dengan level yang lebih tinggi
    private void newGame() {
        elapsedTime = 0;
        score = 1000;
        level++;
        lblTime.setText("Time: 0s");
        lblScore.setText("Score: 1000");
        lblLevel.setText("Level: " + level);
        board.newGame();
    }

    // Menyimpan skor
    private void updateScore() {
        lblScore.setText("Score: " + score);
    }

    // Menyimpan progress berdasarkan jumlah sel yang terisi
    private void updateProgress() {
        int filledCells = board.getFilledCellsCount();
        int totalCells = board.getTotalCells();
        int progressPercentage = (filledCells * 100) / totalCells;
        lblProgress.setText("Progress: " + progressPercentage + "%");

        if (board.isSolved()) {
            if (elapsedTime < bestTime) {
                bestTime = elapsedTime;
                JOptionPane.showMessageDialog(null, "New Best Time: " + bestTime + "s!");
            }
            newGame();
        }
    }

    // Menyimpan progress permainan
    private void saveProgress() {
        JOptionPane.showMessageDialog(this, "Progress saved!");
        // Logic untuk menyimpan game state di sini
    }

    // Memuat progress permainan
    private void loadProgress() {
        JOptionPane.showMessageDialog(this, "Progress loaded!");
        // Logic untuk memuat game state di sini
    }
}
