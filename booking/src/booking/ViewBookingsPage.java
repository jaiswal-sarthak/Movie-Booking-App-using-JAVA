package booking;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewBookingsPage extends JFrame {
    public ViewBookingsPage() {
        setTitle("View Bookings");
        setSize(800, 600); // Larger window for view bookings
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245)); // Light grey background

        // Title Banner
        JLabel titleLabel = new JLabel("View Bookings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32)); // Modern font
        titleLabel.setForeground(new Color(70, 130, 180)); // Steel blue color
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0)); // More padding
        add(titleLabel, BorderLayout.NORTH);

        // Bookings Panel
        JPanel bookingsPanel = new JPanel();
        bookingsPanel.setBackground(new Color(255, 255, 255)); // White background
        bookingsPanel.setLayout(new BoxLayout(bookingsPanel, BoxLayout.Y_AXIS)); // Vertical layout
        bookingsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Padding

        // Fetch all bookings
        String query = "SELECT * FROM bookings";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String movie = Movie.getMovieTitle(rs.getInt("movie_id"));
                String date = rs.getString("date");
                String time = rs.getString("time");
                String seats = rs.getString("seats");
                int totalPrice = rs.getInt("total_price");

                JLabel bookingLabel = new JLabel(
                        "<html>Movie: " + movie + "<br>Date: " + date + "<br>Time: " + time +
                                "<br>Seats: " + seats + "<br>Total Price: ₹" + totalPrice + "</html>"
                );
                bookingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Modern font
                bookingLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Padding
                bookingsPanel.add(bookingLabel);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }

        // Wrap the bookings panel in a scroll pane
        JScrollPane scrollPane = new JScrollPane(bookingsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Add the scroll pane to the center
        add(scrollPane, BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Modern font
        backButton.setBackground(new Color(70, 130, 180)); // Steel blue color
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false); // Remove focus border
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the view bookings page
            }
        });

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(245, 245, 245)); // Light grey background
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0)); // Padding
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Center the window on the screen
        setLocationRelativeTo(null);
        setVisible(true);
    }
}