package Tetris;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class HighScoreScreen extends JPanel {
    private JFrame frame;
    private MainMenu mainMenu;
    private HighScoreManager highScoreManager;

    public HighScoreScreen(JFrame frame, MainMenu mainMenu) {
        this.frame = frame;
        this.mainMenu = mainMenu;
        this.highScoreManager = new HighScoreManager();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("High Score", JLabel.CENTER);
        titleLabel.setFont(new Font("Sans-Serif", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        String[] columnNames = {"Name", "Score"};
        List<HighScoreManager.HighScore> highScores = highScoreManager.getHighScores();
        Object[][] data = new Object[highScores.size()][2];
        for (int i = 0; i < highScores.size(); i++) {
            data[i][0] = highScores.get(i).getPlayerName();
            data[i][1] = highScores.get(i).getScore();
        }

        JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 400)); // Increase the size as needed
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH; // Allow the table to expand
        add(scrollPane, gbc);

        // Back Button
        JButton backButton = new JButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        add(backButton, gbc);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(mainMenu);
                frame.revalidate();
                frame.pack();
            }
        });

        JLabel authorLabel = new JLabel(GameConfig.AUTHOR, JLabel.CENTER);
        authorLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 12));
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(authorLabel, gbc);

        setPreferredSize(new Dimension(500, 600)); // Increase the overall panel size

        frame.setSize(600, 700); // Set the frame size to ensure everything fits
        frame.setLocationRelativeTo(null);
    }
}
