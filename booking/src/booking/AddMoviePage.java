package booking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddMoviePage extends JFrame {
    private JTextField titleField;
    private JTextField releaseDateField;

    public AddMoviePage() {
        setTitle("Add Movie");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245)); // Light grey background

        // Title Banner
        JLabel titleLabel = new JLabel("Add Movie", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Modern font
        titleLabel.setForeground(new Color(70, 130, 180)); // Steel blue color
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0)); // Padding
        add(titleLabel, BorderLayout.NORTH);

        // Add Movie Panel
        JPanel addMoviePanel = new JPanel(new GridBagLayout());
        addMoviePanel.setBackground(new Color(255, 255, 255)); // White background
        addMoviePanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Padding
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titleLabelField = new JLabel("Title:");
        titleLabelField.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Bold font
        addMoviePanel.add(titleLabelField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        titleField = new JTextField(15);
        titleField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Modern font
        titleField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), // Light grey border
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding inside the field
        ));
        addMoviePanel.add(titleField, gbc);

        // Release Date
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel releaseDateLabel = new JLabel("Release Date:");
        releaseDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Bold font
        addMoviePanel.add(releaseDateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        releaseDateField = new JTextField(15);
        releaseDateField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Modern font
        releaseDateField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), // Light grey border
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding inside the field
        ));
        addMoviePanel.add(releaseDateField, gbc);

        // Add Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton addButton = new JButton("Add Movie");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Modern font
        addButton.setBackground(new Color(70, 130, 180)); // Steel blue color
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false); // Remove focus border
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String releaseDate = releaseDateField.getText();
                if (title.isEmpty() || releaseDate.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields!");
                    return;
                }
                Movie.addMovie(-1, title, releaseDate); // Add movie with a dummy ID
                JOptionPane.showMessageDialog(null, "Movie added successfully!");
                dispose(); // Close the add movie page
            }
        });
        addMoviePanel.add(addButton, gbc);

        // Add the add movie panel to the frame
        add(addMoviePanel, BorderLayout.CENTER);

        // Center the window on the screen
        setLocationRelativeTo(null);
        setVisible(true);
    }
}