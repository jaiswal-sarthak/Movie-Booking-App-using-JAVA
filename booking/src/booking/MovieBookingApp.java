package booking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class MovieBookingApp {
    public static void main(String[] args) {
        // Fetch movies from API and store in database
        List<MovieItem> movies = Movie.getUpcomingMoviesFromAPI();
        for (MovieItem movie : movies) {
            Movie.addMovie(movie.getId(), movie.getTitle(), movie.getReleaseDate());
        }

        // Open the login page
        new LoginPage();
    }
}

// Login Page
class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        setTitle("Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245)); // Light grey background

        // Title Banner
        JLabel titleLabel = new JLabel("Movie Ticket Booking System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Modern font
        titleLabel.setForeground(new Color(70, 130, 180)); // Steel blue color
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0)); // Padding
        add(titleLabel, BorderLayout.NORTH);

        // Login Panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(255, 255, 255)); // White background
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Padding
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Bold font
        loginPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Modern font
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), // Light grey border
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding inside the field
        ));
        loginPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Bold font
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Modern font
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), // Light grey border
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding inside the field
        ));
        loginPanel.add(passwordField, gbc);

        // Login Button with Icon (Resized)
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        ImageIcon loginIcon = new ImageIcon("src/icons/login_icon.png");
        Image loginImage = loginIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); // Resize icon
        JButton loginButton = new JButton("Login", new ImageIcon(loginImage)); // Add resized icon
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Modern font
        loginButton.setBackground(new Color(70, 130, 180)); // Steel blue color
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false); // Remove focus border
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (User.login(username, password)) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                    dispose(); // Close the login page

                    if (username.equals(User.getAdminUsername())) { // Use the getter method
                        new AdminPanel(); // Open admin panel
                    } else {
                        new MovieBookingPage(username); // Open the booking page
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Credentials!");
                }
            }
        });
        loginPanel.add(loginButton, gbc);

        // Signup Button with Icon (Resized)
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        ImageIcon signupIcon = new ImageIcon("src/icons/signup_icon.png");
        Image signupImage = signupIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); // Resize icon
        JButton signupButton = new JButton("Signup", new ImageIcon(signupImage)); // Add resized icon
        signupButton.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Modern font
        signupButton.setBackground(new Color(70, 130, 180)); // Steel blue color
        signupButton.setForeground(Color.WHITE);
        signupButton.setFocusPainted(false); // Remove focus border
        signupButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignupPage(); // Open the signup page
            }
        });
        loginPanel.add(signupButton, gbc);

        // Add the login panel to the frame
        add(loginPanel, BorderLayout.CENTER);

        // Center the window on the screen
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

// Signup Page
class SignupPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public SignupPage() {
        setTitle("Signup");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245)); // Light grey background

        // Title Banner
        JLabel titleLabel = new JLabel("Signup", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Modern font
        titleLabel.setForeground(new Color(70, 130, 180)); // Steel blue color
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0)); // Padding
        add(titleLabel, BorderLayout.NORTH);

        // Signup Panel
        JPanel signupPanel = new JPanel(new GridBagLayout());
        signupPanel.setBackground(new Color(255, 255, 255)); // White background
        signupPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Padding
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Bold font
        signupPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Modern font
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), // Light grey border
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding inside the field
        ));
        signupPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Bold font
        signupPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Modern font
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), // Light grey border
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding inside the field
        ));
        signupPanel.add(passwordField, gbc);

        // Signup Button with Icon (Resized)
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        ImageIcon signupIcon = new ImageIcon("src/icons/signup_icon.png");
        Image signupImage = signupIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); // Resize icon
        JButton signupButton = new JButton("Signup", new ImageIcon(signupImage)); // Add resized icon
        signupButton.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Modern font
        signupButton.setBackground(new Color(70, 130, 180)); // Steel blue color
        signupButton.setForeground(Color.WHITE);
        signupButton.setFocusPainted(false); // Remove focus border
        signupButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (User.register(username, password)) {
                    JOptionPane.showMessageDialog(null, "Signup Successful! Please login.");
                    dispose(); // Close the signup page
                } else {
                    JOptionPane.showMessageDialog(null, "Signup Failed! Username may already exist.");
                }
            }
        });
        signupPanel.add(signupButton, gbc);

        // Add the signup panel to the frame
        add(signupPanel, BorderLayout.CENTER);

        // Center the window on the screen
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

// Movie Booking Page
class MovieBookingPage extends JFrame {
    private JComboBox<MovieItem> movieComboBox;
    private JPanel seatPanel;
    private JButton[][] seatButtons;
    private JButton bookButton;
    private JButton logoutButton;
    private JButton profileButton;
    private JComboBox<String> dateComboBox;
    private JComboBox<String> timeComboBox;
    private String username;

    public MovieBookingPage(String username) {
        this.username = username;
        setTitle("Movie Booking");
        setSize(900, 700); // Increased window size for better spacing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245)); // Light grey background

        // Title Banner
        JLabel titleLabel = new JLabel("Book Your Movie Tickets", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32)); // Modern font
        titleLabel.setForeground(new Color(70, 130, 180)); // Steel blue color
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0)); // Padding
        add(titleLabel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(255, 255, 255)); // White background
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Padding
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        // Movie Selection Panel
        JPanel moviePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        moviePanel.setBackground(new Color(255, 255, 255)); // White background
        JLabel movieLabel = new JLabel("Select Movie:");
        movieLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Bold font
        moviePanel.add(movieLabel);
        movieComboBox = new JComboBox<>();
        movieComboBox.setRenderer(new MovieItemRenderer());
        loadMovies(); // Load movies
        movieComboBox.setPreferredSize(new Dimension(300, 30)); // Set combo box size
        moviePanel.add(movieComboBox);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(moviePanel, gbc);

        // Date Selection Panel
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        datePanel.setBackground(new Color(255, 255, 255)); // White background
        JLabel dateLabel = new JLabel("Select Date:");
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Bold font
        datePanel.add(dateLabel);
        dateComboBox = new JComboBox<>();
        loadDates(); // Load available dates
        dateComboBox.setPreferredSize(new Dimension(150, 30)); // Set combo box size
        datePanel.add(dateComboBox);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(datePanel, gbc);

        // Time Selection Panel
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timePanel.setBackground(new Color(255, 255, 255)); // White background
        JLabel timeLabel = new JLabel("Select Time:");
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Bold font
        timePanel.add(timeLabel);
        timeComboBox = new JComboBox<>();
        loadTimes(); // Load available times
        timeComboBox.setPreferredSize(new Dimension(150, 30)); // Set combo box size
        timePanel.add(timeComboBox);

        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(timePanel, gbc);

        // Seat Selection Panel
        seatPanel = new JPanel(new GridLayout(5, 5, 10, 10));
        seatPanel.setBackground(new Color(255, 255, 255)); // White background
        seatPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(seatPanel, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(245, 245, 245)); // Light grey background

        // Book Button
        bookButton = new JButton("Book Tickets");
        bookButton.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Modern font
        bookButton.setBackground(new Color(70, 130, 180)); // Steel blue color
        bookButton.setForeground(Color.WHITE);
        bookButton.setFocusPainted(false); // Remove focus border
        bookButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        bookButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookSelectedSeats();
            }
        });
        buttonPanel.add(bookButton);

        // Profile Button
        profileButton = new JButton("Profile");
        profileButton.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Modern font
        profileButton.setBackground(new Color(70, 130, 180)); // Steel blue color
        profileButton.setForeground(Color.WHITE);
        profileButton.setFocusPainted(false); // Remove focus border
        profileButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        profileButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserProfilePage(username); // Open user profile page
            }
        });
        buttonPanel.add(profileButton);

        // Logout Button
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Modern font
        logoutButton.setBackground(new Color(70, 130, 180)); // Steel blue color
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false); // Remove focus border
        logoutButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the booking page
                new LoginPage(); // Open the login page
            }
        });
        buttonPanel.add(logoutButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        mainPanel.add(buttonPanel, gbc);

        // Add the main panel to the frame
        add(mainPanel, BorderLayout.CENTER);

        // Add ActionListener to movieComboBox
        movieComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSeats(); // Load seats when a movie is selected
            }
        });

        // Add ActionListener to dateComboBox
        dateComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTimes(); // Load times when a date is selected
            }
        });

        setVisible(true);
    }

    private void loadMovies() {
        String query = "SELECT id, title FROM movies";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                movieComboBox.addItem(new MovieItem(id, title));
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadDates() {
        // Add dates for the next 7 days
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 7; i++) {
            Date date = new Date(System.currentTimeMillis() + (i * 24 * 60 * 60 * 1000));
            dateComboBox.addItem(sdf.format(date));
        }
    }

    private void loadTimes() {
        // Add timing slots for the selected date
        timeComboBox.removeAllItems();
        timeComboBox.addItem("10:00 AM");
        timeComboBox.addItem("02:00 PM");
        timeComboBox.addItem("06:00 PM");
        timeComboBox.addItem("10:00 PM");
    }

    private void loadSeats() {
        seatPanel.removeAll(); // Clear previous seats
        seatButtons = new JButton[5][5]; // 5x5 seat grid

        MovieItem selectedMovie = (MovieItem) movieComboBox.getSelectedItem();
        String selectedDate = (String) dateComboBox.getSelectedItem();
        String selectedTime = (String) timeComboBox.getSelectedItem();

        // Convert time from "HH:mm a" to "HH:mm:ss"
        SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedTime;
        try {
            Date time = inputFormat.parse(selectedTime);
            formattedTime = outputFormat.format(time);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Invalid time format!");
            return;
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                String seatNumber = "Seat " + (i + 1) + "-" + (j + 1);
                seatButtons[i][j] = new JButton(seatNumber);
                seatButtons[i][j].setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Modern font

                // Check if the seat is already booked for the selected time
                if (!Movie.isSeatAvailable(selectedMovie.getId(), selectedDate, formattedTime, seatNumber)) {
                    seatButtons[i][j].setBackground(Color.GRAY); // Mark as occupied
                    seatButtons[i][j].setEnabled(false); // Disable the button
                } else {
                    seatButtons[i][j].setBackground(Color.GREEN); // Mark as available
                    seatButtons[i][j].setEnabled(true); // Enable the button
                }

                seatButtons[i][j].setForeground(Color.BLACK);
                seatButtons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton clickedSeat = (JButton) e.getSource();
                        if (clickedSeat.getBackground() == Color.GREEN) {
                            clickedSeat.setBackground(Color.RED); // Mark as selected
                        } else {
                            clickedSeat.setBackground(Color.GREEN); // Mark as available
                        }
                    }
                });
                seatPanel.add(seatButtons[i][j]);
            }
        }

        seatPanel.revalidate();
        seatPanel.repaint();
    }

    private void bookSelectedSeats() {
        List<String> selectedSeats = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (seatButtons[i][j].getBackground() == Color.RED) {
                    selectedSeats.add("Seat " + (i + 1) + "-" + (j + 1));
                }
            }
        }

        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No seats selected!");
            return;
        }

        MovieItem selectedMovie = (MovieItem) movieComboBox.getSelectedItem();
        String selectedDate = (String) dateComboBox.getSelectedItem();
        String selectedTime = (String) timeComboBox.getSelectedItem();

        // Convert time from "HH:mm a" to "HH:mm:ss"
        SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedTime;
        try {
            Date time = inputFormat.parse(selectedTime);
            formattedTime = outputFormat.format(time);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Invalid time format!");
            return;
        }

        // Simulate user ID (replace with actual user ID from login)
        int userId = User.getUserId(username);

        // Calculate total price
        int pricePerSeat = 100;
        int totalPrice = selectedSeats.size() * pricePerSeat;

        // Simulate payment gateway integration
        boolean paymentSuccess = simulatePayment(totalPrice);
        if (!paymentSuccess) {
            JOptionPane.showMessageDialog(null, "Payment Failed! Please try again.");
            return;
        }

        // Save the booking with all seats as a single string
        int bookingId = Movie.bookTicket(userId, selectedMovie.getId(), selectedDate, formattedTime, selectedSeats, totalPrice);

        if (bookingId != -1) {
            StringBuilder message = new StringBuilder("Booking Confirmation:\n");
            message.append("Movie: ").append(selectedMovie.getTitle()).append("\n");
            message.append("Date: ").append(selectedDate).append("\n");
            message.append("Time: ").append(selectedTime).append("\n");
            message.append("Seats: ").append(String.join(", ", selectedSeats)).append("\n");
            message.append("Total Price: ₹").append(totalPrice).append("\n");
            message.append("Booking Successful!");

            JOptionPane.showMessageDialog(null, message.toString());

            // Generate QR code for the booking
            String qrCodeText = "Booking ID: " + bookingId + "\n" +
                    "Movie: " + selectedMovie.getTitle() + "\n" +
                    "Date: " + selectedDate + "\n" +
                    "Time: " + selectedTime + "\n" +
                    "Seats: " + String.join(", ", selectedSeats) + "\n" +
                    "Total Price: ₹" + totalPrice;
            String qrCodePath = "booking_qr_" + bookingId + ".png"; // Unique filename for each booking
            generateQRCode(qrCodeText, qrCodePath);

            // Show QR code in a new window
            new QRCodeDisplay(qrCodePath);

            dispose(); // Close the booking page
            new ConfirmationPage(selectedMovie.getTitle(), selectedSeats, selectedDate, selectedTime, totalPrice); // Open the confirmation page
        } else {
            JOptionPane.showMessageDialog(null, "Booking Failed! Please try again.");
        }
    }
    private boolean simulatePayment(int totalPrice) {
        // Simulate payment gateway integration
        int option = JOptionPane.showConfirmDialog(null, "Proceed to pay ₹" + totalPrice + "?", "Payment", JOptionPane.YES_NO_OPTION);
        return option == JOptionPane.YES_OPTION;
    }

    private String generateQRCode(String text, String filePath) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);

            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            return filePath;
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to generate QR code!");
            return null;
        }
    }

    
}

// User Profile Page
class UserProfilePage extends JFrame {
    public UserProfilePage(String username) {
        setTitle("User Profile");
        setSize(800, 600); // Larger window for better spacing
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245)); // Light grey background

        // Title Banner
        JLabel titleLabel = new JLabel("Booking History", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32)); // Modern font
        titleLabel.setForeground(new Color(70, 130, 180)); // Steel blue color
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0)); // More padding
        add(titleLabel, BorderLayout.NORTH);

        // Booking History Panel
        JPanel historyPanel = new JPanel();
        historyPanel.setBackground(new Color(255, 255, 255)); // White background
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS)); // Vertical layout
        historyPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Padding

        // Fetch booking history for the user
        int userId = User.getUserId(username);
        String query = "SELECT * FROM bookings WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int bookingId = rs.getInt("id");
                String movie = Movie.getMovieTitle(rs.getInt("movie_id"));
                String date = rs.getString("date");
                String time = rs.getString("time");
                String seats = rs.getString("seats");
                int totalPrice = rs.getInt("total_price");

                // Create a panel for each booking
                JPanel bookingPanel = new JPanel(new BorderLayout());
                bookingPanel.setBackground(new Color(255, 255, 255)); // White background
                bookingPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Padding

                // Booking details
                JLabel bookingLabel = new JLabel(
                        "<html>Booking ID: " + bookingId + "<br>Movie: " + movie + "<br>Date: " + date +
                                "<br>Time: " + time + "<br>Seats: " + seats + "<br>Total Price: ₹" + totalPrice + "</html>"
                );
                bookingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Modern font
                bookingPanel.add(bookingLabel, BorderLayout.CENTER);

                // Load QR code for the booking
                String qrCodePath = "booking_qr_" + bookingId + ".png";
                ImageIcon qrCodeIcon = new ImageIcon(qrCodePath);
                JLabel qrCodeLabel = new JLabel(qrCodeIcon);
                bookingPanel.add(qrCodeLabel, BorderLayout.EAST);

                // Add the booking panel to the history panel
                historyPanel.add(bookingPanel);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }

        // Wrap the history panel in a scroll pane
        JScrollPane scrollPane = new JScrollPane(historyPanel);
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
                dispose(); // Close the profile page
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

// Admin Panel

// Add Movie Page


// View Bookings Page


// MovieItem Class
class MovieItem {
    private int id;
    private String title;
    private String releaseDate;

    public MovieItem(int id, String title, String releaseDate) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
    }

    public MovieItem(int id, String title) {
        this.id = id;
        this.title = title;
        this.releaseDate = ""; // Set releaseDate to an empty string
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString() {
        return title;
    }
}

// MovieItemRenderer Class
class MovieItemRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof MovieItem) {
            MovieItem movie = (MovieItem) value;
            setText(movie.getTitle());
        }
        return this;
    }
}

// Confirmation Page
class ConfirmationPage extends JFrame {
    public ConfirmationPage(String movie, List<String> seats, String date, String time, int totalPrice) {
        setTitle("Booking Confirmation");
        setSize(600, 500); // Slightly larger window for better spacing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245)); // Light grey background

        // Title Banner
        JLabel titleLabel = new JLabel("Booking Confirmation", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32)); // Modern font
        titleLabel.setForeground(new Color(70, 130, 180)); // Steel blue color
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0)); // More padding
        add(titleLabel, BorderLayout.NORTH);

        // Confirmation Details Panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setBackground(new Color(255, 255, 255)); // White background
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS)); // Vertical layout
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Padding

        // Add details with icons and better styling
        addDetail(detailsPanel, "Movie: " + movie, new Color(50, 150, 250), "src/icons/movie_icon.png");
        addDetail(detailsPanel, "Date: " + date, new Color(50, 150, 250), "src/icons/calendar_icon.png");
        addDetail(detailsPanel, "Time: " + time, new Color(50, 150, 250), "src/icons/clock_icon.png");
        addDetail(detailsPanel, "Seats: " + String.join(", ", seats), new Color(50, 150, 250), "src/icons/seat_icon.png");
        addDetail(detailsPanel, "Total Price: ₹" + totalPrice, new Color(34, 139, 34), "src/icons/price_icon.png");

        // Add details panel to the center
        add(detailsPanel, BorderLayout.CENTER);

        // Back to Login Button
        JButton backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Modern font
        backButton.setBackground(new Color(70, 130, 180)); // Steel blue color
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false); // Remove focus border
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the confirmation page
                new LoginPage(); // Open the login page
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

    // Helper method to add details with icons and styling
    private void addDetail(JPanel panel, String text, Color color, String iconPath) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18)); // Modern font
        label.setForeground(color);
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Padding

        // Add icon (if iconPath is provided)
        if (iconPath != null && !iconPath.isEmpty()) {
            ImageIcon icon = new ImageIcon(iconPath);
            Image scaledIcon = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaledIcon));
        }

        panel.add(label);
    }
}

// Movie Class
class Movie {
    public static List<MovieItem> getUpcomingMoviesFromAPI() {
        List<MovieItem> movies = new ArrayList<>();
        String apiKey = "jfvsdfgf13rfbwcgyqq63432gffg81t676"; // Your TMDb API key
        String apiUrl = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + apiKey;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray results = jsonResponse.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject movie = results.getJSONObject(i);
                int id = movie.getInt("id");
                String title = movie.getString("title");
                String releaseDate = movie.optString("release_date", ""); // Use optString to handle missing release_date
                if (!releaseDate.isEmpty()) {
                    movies.add(new MovieItem(id, title, releaseDate));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return movies;
    }

    public static void addMovie(int id, String title, String releaseDate) {
        String query = "INSERT INTO movies (id, title, release_date) VALUES (?, ?, ?) " +
                      "ON DUPLICATE KEY UPDATE title = VALUES(title), release_date = VALUES(release_date)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.setString(2, title);
            stmt.setString(3, releaseDate);
            stmt.executeUpdate();
            System.out.println("Movie added/updated: " + title);
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static int bookTicket(int userId, int movieId, String date, String time, List<String> seats, int totalPrice) {
        String query = "INSERT INTO bookings (user_id, movie_id, date, time, seats, total_price) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, movieId);
            stmt.setString(3, date);
            stmt.setString(4, time);
            stmt.setString(5, String.join(", ", seats)); // Store all seats as a single string
            stmt.setInt(6, totalPrice);
            stmt.executeUpdate();

            // Retrieve the generated booking ID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return the booking ID
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        return -1; // Return -1 if the booking fails
    }

    public static boolean isSeatAvailable(int movieId, String date, String time, String seatNumber) {
        String query = "SELECT * FROM bookings WHERE movie_id = ? AND date = ? AND time = ? AND seats LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, movieId);
            stmt.setString(2, date);
            stmt.setString(3, time); // Filter by the selected time
            stmt.setString(4, "%" + seatNumber + "%"); // Check if the seat is part of the seats string
            ResultSet rs = stmt.executeQuery();
            return !rs.next(); // Seat is available if no matching record is found
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static String getMovieTitle(int movieId) {
        String query = "SELECT title FROM movies WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("title");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        return "Unknown Movie";
    }
}

// User Class
class User {
    // Admin credentials
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    // Public getter for admin username
    public static String getAdminUsername() {
        return ADMIN_USERNAME;
    }

    // Single login method to handle both admin and regular users
    public static boolean login(String username, String password) {
        // Check if the user is the admin
        if (username.equals(ADMIN_USERNAME)) {
            return password.equals(ADMIN_PASSWORD); // Admin login
        }

        // Regular user login
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Return true if the user exists
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Register a new user
    public static boolean register(String username, String password) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get user ID by username
    public static int getUserId(String username) {
        String query = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
}


// DatabaseConnection Class
class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/movie_booking";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}