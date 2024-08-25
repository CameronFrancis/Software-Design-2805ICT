import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HighScoreScreen extends JPanel {
    private JFrame frame;
    private MainMenu mainMenu;

    public HighScoreScreen(JFrame frame, MainMenu mainMenu) {
        this.frame = frame;
        this.mainMenu = mainMenu;
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
        Object[][] data = {
            {"Tom", 869813},
            {"Anna", 754609},
            {"Jerry", 642371},
            {"Mike", 548020},
            {"Tom", 537728},
            {"Tom", 432740},
            {"Larry", 306705},
            {"Alice", 326161},
            {"Anna", 301649},
            {"Tom", 260598},
            {"Bob", 158432},
            {"Cathy", 148376},
            {"David", 138905},
            {"Eve", 128654},
            {"Frank", 118432},
            {"Grace", 108329},
            {"Hank", 98327},
            {"Ivy", 88215},
            {"Jack", 77204},
            {"Kate", 66193}
        };

        JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(250, 300)); // Adjust the size as needed
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH; // Allow the table to expand
        add(scrollPane, gbc);

        // Back Button
        JButton backButton = new JButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 11;
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

        setPreferredSize(new Dimension(300, 450)); 

        frame.pack();
        frame.setLocationRelativeTo(null); 
    }
}