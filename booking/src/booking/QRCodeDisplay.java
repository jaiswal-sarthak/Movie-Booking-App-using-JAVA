package booking;

import javax.swing.*;
import java.awt.*;

public class QRCodeDisplay extends JFrame {
    public QRCodeDisplay(String qrCodePath) {
        setTitle("QR Code");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245)); // Light grey background

        // Load QR code image
        ImageIcon qrCodeIcon = new ImageIcon(qrCodePath);
        JLabel qrCodeLabel = new JLabel(qrCodeIcon);
        qrCodeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(qrCodeLabel, BorderLayout.CENTER);

        // Center the window on the screen
        setLocationRelativeTo(null);
        setVisible(true);
    }
}