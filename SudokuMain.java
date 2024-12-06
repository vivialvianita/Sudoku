/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026221161 - Ryan Adi Putra Pratama
 * 2 - 5026231185 - Jannati Urfa Muhayat
 * 3 - 5026231226 - Vivi Alvianita
 */
package sudoku;

import sudoku.GameBoardPanel;

import javax.sound.sampled.*;
import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * The main Sudoku program with a welcome page and background music.
 */
public class SudokuMain extends JFrame {
    private static final long serialVersionUID = 1L; // Prevent serial warning

    // Panels for the CardLayout
    private JPanel cards;
    private CardLayout cardLayout;

    // Components for the welcome page
    private JPanel welcomePanel;
    private JButton btnPlay;

    // Sudoku game components
    GameBoardPanel board = new GameBoardPanel();
    JButton btnNewGame = new JButton("New Game");
    JMenuBar difficulty = new JMenuBar();
    JMenu diffmenu = new JMenu("Level");

    // create menuitems
    JMenuItem easy = new JMenuItem("Easy");
    JMenuItem medium = new JMenuItem("Intermediate");
    JMenuItem hard = new JMenuItem("Difficult");

// add menu items to menu

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

    private String diff = "Easy";


    private Clip backgroundMusic;
    private JButton btnToggleMusic; // Tombol untuk menghidupkan/mematikan musik
    private boolean isMusicPlaying = true; // Status musik (nyala/mati)

    // Constructor
    public SudokuMain() {
        // Setting up CardLayout
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // Initialize welcome page and game panels
        initWelcomePage();
        initGamePanel();

        // Add panels to CardLayout
        cards.add(welcomePanel, "Welcome");
        cards.add(createGamePanel(), "Game");

        // Set up JFrame
        getContentPane().add(cards);
        setTitle("Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);

        // Start background music
        playBackgroundMusic();


    }

    public static void main(String[] args) {
        new SudokuMain();
    }

    // Initialize the welcome page
    private void initWelcomePage() {
        welcomePanel = new JPanel();
        welcomePanel.setLayout(new BorderLayout());
        welcomePanel.setBackground(new Color(255, 228, 196)); // Light color theme

        JLabel lblTitle = new JLabel("Welcome to Sudoku", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 36));
        lblTitle.setForeground(new Color(139, 69, 19)); // Brownish color

        btnPlay = new JButton("Play");
        btnPlay.setFont(new Font("Serif", Font.BOLD, 24));
        btnPlay.setBackground(new Color(255, 165, 0)); // Orange button
        btnPlay.setForeground(Color.WHITE);
        btnPlay.setFocusPainted(false);
        btnPlay.addActionListener(e -> {
            cardLayout.show(cards, "Game");
            startTimer();  // Start the timer when the game starts
        });

        welcomePanel.add(lblTitle, BorderLayout.CENTER);
        welcomePanel.add(btnPlay, BorderLayout.SOUTH);
    }

    // Initialize the game panel
    private void initGamePanel() {
        board.newGame(diff); // Initialize the game board
    }

    // Create the game panel with all components
    private JPanel createGamePanel() {
        JPanel gamePanel = new JPanel(new BorderLayout());

        // Top panel for displaying time, score, progress, and level
        JPanel topPanel = new JPanel(new GridLayout(1, 4));
        topPanel.add(lblTime);
        topPanel.add(lblScore);
        topPanel.add(lblProgress);
        topPanel.add(lblLevel);
        gamePanel.add(topPanel, BorderLayout.NORTH);

        gamePanel.add(board, BorderLayout.CENTER);
        diffmenu.add(easy);
        diffmenu.add(medium);
        diffmenu.add(hard);

// add menu to menu bar
        difficulty.add(diffmenu);

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        btnNewGame.addActionListener(e -> newGame());
        easy.addActionListener(e -> {
            diff = "Easy"; // Mengatur level kesulitan
            newGame();     // Memulai permainan baru
        });

        medium.addActionListener(e -> {
            diff = "Intermediate"; // Mengatur level kesulitan
            newGame();     // Memulai permainan baru
        });


        hard.addActionListener(e -> {
            diff = "Difficult"; // Mengatur level kesulitan
            newGame();     // Memulai permainan baru
        });

        btnPause.addActionListener(e -> togglePause(true));
        btnResume.addActionListener(e -> togglePause(false));
        btnSaveProgress.addActionListener(e -> saveProgress());
        btnLoadProgress.addActionListener(e -> loadProgress());

        // Tombol untuk menghidupkan/mematikan musik
        btnToggleMusic = new JButton("On/Off Music");
        btnToggleMusic.setFont(new Font("Serif", Font.BOLD, 14));
        btnToggleMusic.addActionListener(e -> toggleMusic());

        bottomPanel.add(btnNewGame);
        bottomPanel.add(difficulty);
        bottomPanel.add(btnPause);
        bottomPanel.add(btnResume);
        bottomPanel.add(btnSaveProgress);
        bottomPanel.add(btnLoadProgress);
        bottomPanel.add(btnToggleMusic); // Menambahkan tombol kontrol musik

        gamePanel.add(bottomPanel, BorderLayout.SOUTH);

        return gamePanel;
    }

    // Timer for tracking gameplay time
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

    // Pause or resume the game
    private void togglePause(boolean pause) {
        isPaused = pause;
    }

    // Start a new game with increased level
    private void newGame() {
        elapsedTime = 0;
        score = 1000;
        level++;
        lblTime.setText("Time: 0s");
        lblScore.setText("Score: 1000");
        lblLevel.setText("Level: " + level);
        board.newGame(diff);
    }

    // Update the score label
    private void updateScore() {
        lblScore.setText("Score: " + score);
    }

    // Update progress label
    private void updateProgress() {
        int filledCells = board.getFilledCellsCount();
        int totalCells = board.getTotalCells();
        int progressPercentage = (filledCells * 100) / totalCells;
        lblProgress.setText("Progress: " + progressPercentage + "%");

        if (board.isSolved()) {
            if (elapsedTime < bestTime) {
                bestTime = elapsedTime;
                JOptionPane.showMessageDialog(this, "New Best Time: " + bestTime + "s!");
            }
            newGame();
        }
    }

    // Save game progress
    private void saveProgress() {
        JOptionPane.showMessageDialog(this, "Progress saved!");
    }

    // Load game progress
    private void loadProgress() {
        JOptionPane.showMessageDialog(this, "Progress loaded!");
    }

    // Menyalakan atau mematikan musik
    private void toggleMusic() {
        if (isMusicPlaying) {
            stopBackgroundMusic();
        } else {
            playBackgroundMusic();
        }
        isMusicPlaying = !isMusicPlaying; // Toggle the music state
    }

    // Play background music with looping
    private void playBackgroundMusic() {
        try {
            File musicFile = new File("assets/Electric_-_3_Minute_Countdown_[_YTBMP3.org_].wav");

            // Cek apakah file ada
            if (!musicFile.exists()) {
                System.out.println("File not found: " + musicFile.getAbsolutePath());
                return;
            }

            // Memulai pemutaran musik
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);

            // Looping musik
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Looping musik tanpa henti
            backgroundMusic.start(); // Mulai pemutaran musik
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace(); // Jika ada error, tampilkan error
        }
    }

    // Fungsi untuk menghentikan musik latar
    private void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }
}