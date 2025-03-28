package booking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPanel extends JFrame {
    public AdminPanel() {
        setTitle("Admin Panel");
        setSize(800, 600); // Larger window for admin panel
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245)); // Light grey background

        // Title Banner
        JLabel titleLabel = new JLabel("Admin Panel", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32)); // Modern font
        titleLabel.setForeground(new Color(70, 130, 180)); // Steel blue color
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0)); // More padding
        add(titleLabel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(255, 255, 255)); // White background
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Padding
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        // Add Movie Button
        JButton addMovieButton = new JButton("Add Movie");
        addMovieButton.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Modern font
        addMovieButton.setBackground(new Color(70, 130, 180)); // Steel blue color
        addMovieButton.setForeground(Color.WHITE);
        addMovieButton.setFocusPainted(false); // Remove focus border
        addMovieButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        addMovieButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        addMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddMoviePage(); // Open add movie page
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(addMovieButton, gbc);

        // View Bookings Button
        JButton viewBookingsButton = new JButton("View Bookings");
        viewBookingsButton.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Modern font
        viewBookingsButton.setBackground(new Color(70, 130, 180)); // Steel blue color
        viewBookingsButton.setForeground(Color.WHITE);
        viewBookingsButton.setFocusPainted(false); // Remove focus border
        viewBookingsButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        viewBookingsButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        viewBookingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewBookingsPage(); // Open view bookings page
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(viewBookingsButton, gbc);

        // Add the main panel to the frame
        add(mainPanel, BorderLayout.CENTER);

        // Center the window on the screen
        setLocationRelativeTo(null);
        setVisible(true);
    }
}