import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        JLabel titleLabel = new JLabel("Configuration", JLabel.CENTER);
        titleLabel.setFont(new Font("Sans-Serif", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Field Width
        JLabel fieldWidthLabel = new JLabel("Field Width (No of cells):");
        JSlider fieldWidthSlider = new JSlider(5, 15, 10);
        fieldWidthSlider.setMajorTickSpacing(1);
        fieldWidthSlider.setPaintTicks(true);
        fieldWidthSlider.setPaintLabels(true);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(fieldWidthLabel, gbc);
        gbc.gridx = 1;
        add(fieldWidthSlider, gbc);

        // Field Height
        JLabel fieldHeightLabel = new JLabel("Field Height (No of cells):");
        JSlider fieldHeightSlider = new JSlider(15, 30, 20);
        fieldHeightSlider.setMajorTickSpacing(1);
        fieldHeightSlider.setPaintTicks(true);
        fieldHeightSlider.setPaintLabels(true);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(fieldHeightLabel, gbc);
        gbc.gridx = 1;
        add(fieldHeightSlider, gbc);

        // Game Level
        JLabel gameLevelLabel = new JLabel("Game Level:");
        JSlider gameLevelSlider = new JSlider(1, 10, 1);
        gameLevelSlider.setMajorTickSpacing(1);
        gameLevelSlider.setPaintTicks(true);
        gameLevelSlider.setPaintLabels(true);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(gameLevelLabel, gbc);
        gbc.gridx = 1;
        add(gameLevelSlider, gbc);

        // Music On/Off
        JCheckBox musicCheckbox = new JCheckBox("Music (On/Off):", true);
        JLabel musicLabel = new JLabel("On");
        musicCheckbox.addActionListener(e -> musicLabel.setText(musicCheckbox.isSelected() ? "On" : "Off"));
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(musicCheckbox, gbc);
        gbc.gridx = 1;
        add(musicLabel, gbc);

        // Sound Effect On/Off
        JCheckBox soundEffectCheckbox = new JCheckBox("Sound Effect (On/Off):", true);
        JLabel soundEffectLabel = new JLabel("On");
        soundEffectCheckbox.addActionListener(e -> soundEffectLabel.setText(soundEffectCheckbox.isSelected() ? "On" : "Off"));
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(soundEffectCheckbox, gbc);
        gbc.gridx = 1;
        add(soundEffectLabel, gbc);

        // AI Play On/Off
        JCheckBox aiPlayCheckbox = new JCheckBox("AI Play (On/Off):");
        JLabel aiPlayLabel = new JLabel("Off");
        aiPlayCheckbox.addActionListener(e -> aiPlayLabel.setText(aiPlayCheckbox.isSelected() ? "On" : "Off"));
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(aiPlayCheckbox, gbc);
        gbc.gridx = 1;
        add(aiPlayLabel, gbc);

        // Extend Mode On/Off
        JCheckBox extendModeCheckbox = new JCheckBox("Extend Mode (On/Off):");
        JLabel extendModeLabel = new JLabel("Off");
        extendModeCheckbox.addActionListener(e -> extendModeLabel.setText(extendModeCheckbox.isSelected() ? "On" : "Off"));
        gbc.gridx = 0;
        gbc.gridy = 7;
        add(extendModeCheckbox, gbc);
        gbc.gridx = 1;
        add(extendModeLabel, gbc);

        // Back Button
        JButton backButton = new JButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        add(backButton, gbc);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(mainMenu);
                frame.revalidate();
            }
        });

        JLabel authorLabel = new JLabel("Author: Your Name", JLabel.CENTER);
        authorLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 12));
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        add(authorLabel, gbc);
    }
}
