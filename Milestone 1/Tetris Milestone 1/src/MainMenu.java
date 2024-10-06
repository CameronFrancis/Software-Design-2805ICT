import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
    private JFrame frame;

    public MainMenu(JFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title label for the main menu
        JLabel titleLabel = new JLabel("Main Menu", JLabel.CENTER);
        titleLabel.setFont(new Font("Sans-Serif", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Play Button
        JButton playButton = new JButton("Play");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(playButton, gbc);

        // Configuration Button
        JButton configButton = new JButton("Configuration");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(configButton, gbc);

        // High Scores Button
        JButton highScoresButton = new JButton("High Scores");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(highScoresButton, gbc);

        // Exit Button
        JButton exitButton = new JButton("Exit");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(exitButton, gbc);

        // Author Label
        JLabel authorLabel = new JLabel(GameConfig.AUTHOR, JLabel.CENTER);
        authorLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(authorLabel, gbc);

        // Add action listeners to buttons
        playButton.addActionListener(e -> {
            AudioManager.playBackgroundMusic(); // Play background music when game starts
            AudioManager.enableSoundEffects(); // Enable sound effects when game starts
            GameScreen gameScreen = new GameScreen(frame, MainMenu.this);
            frame.setContentPane(gameScreen);
            frame.pack();
            frame.revalidate();
            gameScreen.requestFocusInWindow();
        });

        configButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new ConfigScreen(frame, MainMenu.this));
                frame.revalidate();
                frame.pack();
            }
        });

        highScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new HighScoreScreen(frame, MainMenu.this));
                frame.revalidate();
                frame.pack();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}