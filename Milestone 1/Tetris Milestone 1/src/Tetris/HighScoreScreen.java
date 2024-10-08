package Tetris;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.text.SimpleDateFormat;

public class HighScoreScreen extends JPanel {
    private JFrame frame;
    private MainMenu mainMenu;
    private HighScoreManager highScoreManager;
    private JTable table;

    public HighScoreScreen(JFrame frame, MainMenu mainMenu) {
        this.frame = frame;
        this.mainMenu = mainMenu;
        this.highScoreManager = new HighScoreManager();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Title Label
        JLabel titleLabel = new JLabel("High Scores", JLabel.CENTER);
        titleLabel.setFont(new Font("Sans-Serif", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(titleLabel, gbc);

        // High Scores Table
        updateHighScoresTable();

        // Back Button
        JButton backButton = new JButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        add(backButton, gbc);
        backButton.addActionListener(e -> {
            frame.setContentPane(mainMenu);
            frame.revalidate();
            frame.pack();
        });

        // Clear High Scores Button
        JButton clearButton = new JButton("Clear High Scores");
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(clearButton, gbc);
        clearButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear all high scores?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                highScoreManager.clearHighScores();
                updateHighScoresTable();
            }
        });

        // Author Label
        JLabel authorLabel = new JLabel(GameConfig.AUTHOR, JLabel.CENTER);
        authorLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(authorLabel, gbc);

        setPreferredSize(new Dimension(500, 600));
        frame.setSize(600, 700);
        frame.setLocationRelativeTo(null);
    }

    private void updateHighScoresTable() {
        if (table != null) {
            remove(table.getParent());
        }

        String[] columnNames = {"Name", "Score", "Date"};
        List<HighScoreManager.HighScore> highScores = highScoreManager.getHighScores();
        Object[][] data = new Object[highScores.size()][3];
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < highScores.size(); i++) {
            data[i][0] = highScores.get(i).getPlayerName();
            data[i][1] = highScores.get(i).getScore();
            data[i][2] = dateFormat.format(highScores.get(i).getDate());
        }

        table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 400));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(scrollPane, gbc);

        revalidate();
        repaint();
    }
}
