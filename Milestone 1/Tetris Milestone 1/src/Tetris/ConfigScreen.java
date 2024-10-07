package Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConfigScreen extends JPanel {
    private JFrame frame;
    private MainMenu mainMenu;

    public ConfigScreen(JFrame frame, MainMenu mainMenu) {
        this.frame = frame;
        this.mainMenu = mainMenu;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER; // Center-align

        // Title label for the configuration screen
        JLabel titleLabel = new JLabel("Configuration", JLabel.CENTER);
        titleLabel.setFont(new Font("Sans-Serif", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Field Width Slider Configuration
        JLabel fieldWidthLabel = new JLabel("Field Width (No of cells):");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(fieldWidthLabel, gbc);

        JSlider fieldWidthSlider = new JSlider(5, 15, GameConfig.BOARD_WIDTH);
        fieldWidthSlider.setMajorTickSpacing(1);
        fieldWidthSlider.setPaintTicks(true);
        fieldWidthSlider.setPaintLabels(true);
        gbc.gridy = 2;
        add(fieldWidthSlider, gbc);

        // Field Height Slider Configuration
        JLabel fieldHeightLabel = new JLabel("Field Height (No of cells):");
        gbc.gridy = 3;
        add(fieldHeightLabel, gbc);

        JSlider fieldHeightSlider = new JSlider(15, 30, GameConfig.BOARD_HEIGHT);
        fieldHeightSlider.setMajorTickSpacing(5);
        fieldHeightSlider.setMinorTickSpacing(1);
        fieldHeightSlider.setPaintTicks(true);
        fieldHeightSlider.setPaintLabels(true);
        gbc.gridy = 4;
        add(fieldHeightSlider, gbc);

        // Game Level Slider Configuration
        JLabel gameLevelLabel = new JLabel("Game Level:");
        gbc.gridy = 5;
        add(gameLevelLabel, gbc);

        JSlider gameLevelSlider = new JSlider(1, 10, GameConfig.INITIAL_LEVEL);
        gameLevelSlider.setMajorTickSpacing(1);
        gameLevelSlider.setPaintTicks(true);
        gameLevelSlider.setPaintLabels(true);
        gbc.gridy = 6; // Move to the next row
        add(gameLevelSlider, gbc);

        // Extend Mode On/Off Checkbox and Label
        JCheckBox extendModeCheckbox = new JCheckBox("Extend Mode (On/Off):", GameConfig.EXTEND_MODE);
        JLabel extendModeLabel = new JLabel(GameConfig.EXTEND_MODE ? "On" : "Off");
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        add(extendModeCheckbox, gbc);
        gbc.gridx = 1;
        add(extendModeLabel, gbc);

        // Player Type Dropdown
        JLabel playerTypeLabel = new JLabel("Player Type:");
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        add(playerTypeLabel, gbc);

        String[] playerTypes = {"Human", "AI", "External"};
        JComboBox<String> playerTypeDropdown = new JComboBox<>(playerTypes);
        playerTypeDropdown.setSelectedItem(GameConfig.PLAYER_TYPE);
        gbc.gridx = 1;
        add(playerTypeDropdown, gbc);

        // Player 1 Type Dropdown
        JLabel player1TypeLabel = new JLabel("Player 1 Type:");
        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        add(player1TypeLabel, gbc);

        JComboBox<String> player1TypeDropdown = new JComboBox<>(playerTypes);
        player1TypeDropdown.setSelectedItem(GameConfig.PLAYER1_TYPE);
        gbc.gridx = 1;
        add(player1TypeDropdown, gbc);

        // Player 2 Type Dropdown
        JLabel player2TypeLabel = new JLabel("Player 2 Type:");
        gbc.gridy = 10;
        gbc.gridx = 0;
        add(player2TypeLabel, gbc);

        JComboBox<String> player2TypeDropdown = new JComboBox<>(playerTypes);
        player2TypeDropdown.setSelectedItem(GameConfig.PLAYER2_TYPE);
        gbc.gridx = 1;
        add(player2TypeDropdown, gbc);

        // Disable Player 1 and Player 2 Type Dropdowns if Extended Mode is off
        player1TypeDropdown.setEnabled(GameConfig.EXTEND_MODE);
        player2TypeDropdown.setEnabled(GameConfig.EXTEND_MODE);

        // Listener for Extend Mode Checkbox
        extendModeCheckbox.addActionListener(e -> {
            if (extendModeCheckbox.isSelected()) {
                extendModeLabel.setText("On");
                GameConfig.EXTEND_MODE = true;
                player1TypeDropdown.setEnabled(true);
                player2TypeDropdown.setEnabled(true);
                playerTypeDropdown.setEnabled(false);
            } else {
                extendModeLabel.setText("Off");
                GameConfig.EXTEND_MODE = false;
                player1TypeDropdown.setEnabled(false);
                player2TypeDropdown.setEnabled(false);
                playerTypeDropdown.setEnabled(true);
            }
        });

        // Music On/Off Checkbox and Label
        JCheckBox musicCheckbox = new JCheckBox("Music (On/Off):", GameConfig.MUSIC_ON);
        JLabel musicLabel = new JLabel(GameConfig.MUSIC_ON ? "On" : "Off");
        musicCheckbox.addActionListener(e -> {
            if (musicCheckbox.isSelected()) {
                musicLabel.setText("On");
                AudioManager.playBackgroundMusic(); // Play background music using AudioManager
                GameConfig.MUSIC_ON = true;
            } else {
                musicLabel.setText("Off");
                AudioManager.stopBackgroundMusic(); // Stop background music using AudioManager
                GameConfig.MUSIC_ON = false;
            }
        });
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        add(musicCheckbox, gbc);
        gbc.gridx = 1;
        add(musicLabel, gbc);

        // Sound Effect On/Off Checkbox and Label
        JCheckBox soundEffectCheckbox = new JCheckBox("Sound Effect (On/Off):", GameConfig.SOUND_EFFECTS_ON);
        JLabel soundEffectLabel = new JLabel(GameConfig.SOUND_EFFECTS_ON ? "On" : "Off");
        soundEffectCheckbox.addActionListener(e -> {
            if (soundEffectCheckbox.isSelected()) {
                soundEffectLabel.setText("On");
                AudioManager.enableSoundEffects(); // Enable sound effects using AudioManager
                GameConfig.SOUND_EFFECTS_ON = true;
            } else {
                soundEffectLabel.setText("Off");
                AudioManager.disableSoundEffects(); // Disable sound effects using AudioManager
                GameConfig.SOUND_EFFECTS_ON = false;
            }
        });
        gbc.gridy = 12;
        gbc.gridx = 0;
        add(soundEffectCheckbox, gbc);
        gbc.gridx = 1;
        add(soundEffectLabel, gbc);

        // Back Button to return to Main Menu
        JButton backButton = new JButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        add(backButton, gbc);

        backButton.addActionListener(e -> {
            // Save configuration settings before going back to the main menu
            GameConfig.BOARD_WIDTH = fieldWidthSlider.getValue();
            GameConfig.BOARD_HEIGHT = fieldHeightSlider.getValue();
            GameConfig.INITIAL_LEVEL = gameLevelSlider.getValue();
            GameConfig.MUSIC_ON = musicCheckbox.isSelected();
            GameConfig.SOUND_EFFECTS_ON = soundEffectCheckbox.isSelected();
            GameConfig.EXTEND_MODE = extendModeCheckbox.isSelected();

            if (GameConfig.EXTEND_MODE) {
                GameConfig.PLAYER1_TYPE = (String) player1TypeDropdown.getSelectedItem();
                GameConfig.PLAYER2_TYPE = (String) player2TypeDropdown.getSelectedItem();
            } else {
                GameConfig.PLAYER_TYPE = (String) playerTypeDropdown.getSelectedItem();
            }

            frame.setContentPane(mainMenu);
            frame.revalidate();
            frame.pack();
        });

        // Author Label
        JLabel authorLabel = new JLabel(GameConfig.AUTHOR, JLabel.CENTER);
        authorLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 12));
        gbc.gridy = 14;
        gbc.gridwidth = 2;
        add(authorLabel, gbc);
    }
}
