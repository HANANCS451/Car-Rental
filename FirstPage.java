 package cs342project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FirstPage extends JFrame {
    private JLabel l1;
    private JButton b1, b2, b3;

    public FirstPage() {
        setTitle("Hello Our Dear Customers!!");
        setSize(800, 600); // تعديل حجم النافذة
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // MAIN LAYOUT
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20)); // تباعد أكبر بين المناطق

        // Title
        l1 = new JLabel("WELCOME TO DRIVE NOW!", SwingConstants.CENTER);
        l1.setFont(new Font("Serif", Font.BOLD, 24)); // تكبير النص ليكون متناسقًا مع الحجم
        l1.setBorder(BorderFactory.createEmptyBorder(100, 0, 10, 0)); // زيادة المسافة العلوية
        mainPanel.add(l1, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // ترتيب عمودي للأزرار
        b1 = new JButton("Log In");
        b2 = new JButton("Sign Up");
        b3 = new JButton("Get to know our app");

        // Colors and Size of Buttons
        b1.setBackground(new Color(100, 149, 200));
        b1.setForeground(Color.WHITE);
        b1.setFont(new Font("Arial", Font.BOLD, 18)); // Same size as Sign Up
        b2.setBackground(new Color(100, 149, 200));
        b2.setForeground(Color.WHITE);
        b2.setFont(new Font("Arial", Font.BOLD, 18)); // Same size as Sign Up
        b3.setBackground(new Color(100, 149, 200));
        b3.setForeground(Color.WHITE);
        b3.setFont(new Font("Arial", Font.BOLD, 15)); // Same size as Sign Up

        Dimension buttonSize = new Dimension(200, 40); // تكبير الأزرار قليلاً
        b1.setMaximumSize(buttonSize);
        b2.setMaximumSize(buttonSize);
        b3.setMaximumSize(buttonSize);

        // Adding Buttons with Alignment and Spacing
        b1.setAlignmentX(Component.CENTER_ALIGNMENT);
        b2.setAlignmentX(Component.CENTER_ALIGNMENT);
        b3.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(b1);
        buttonPanel.add(Box.createVerticalStrut(20)); // تباعد أكبر بين الأزرار
        buttonPanel.add(b2);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(b3);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Image Panel
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Align content to the left
        ImageIcon phot = new ImageIcon("C:\\Users\\L4312\\OneDrive\\الصور\\photo_2024-12-11_19-54-09-removebg-preview.png");
        JLabel photoLabel;
        Image img = phot.getImage();
        Image resizedImage = img.getScaledInstance(300, 150, Image.SCALE_SMOOTH); // ضبط الحجم ليكون 250x100 (مثل صفحة Sign Up)
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        photoLabel = new JLabel(resizedIcon);
        imagePanel.add(photoLabel);
        imagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding around the image
        mainPanel.add(imagePanel, BorderLayout.SOUTH);

        this.add(mainPanel);

        // Action Listener for Log In Button
        b1.addActionListener(new LogIn());
        b2.addActionListener(new Signup());
        b3.addActionListener(new GetToKnowUs1());

    }

    public class LogIn implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Login log = new Login();
            log.setVisible(true);
            dispose();
        }
    }

    public class Signup implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            SignUp sign = new SignUp();
            sign.setVisible(true);
            dispose();
        }
    }
    public class GetToKnowUs1 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            GetToKnowUs sign = new GetToKnowUs();
            sign.setVisible(true);
            dispose();
        }
    }

    public static void main(String[] args) {
        updateExpiredBookingsAndCars();
        FirstPage ob = new FirstPage();
        ob.setVisible(true); // Show the window
    }
    
private static void updateExpiredBookingsAndCars() {
    try {
        Connection connection = DatabaseConnection.connect();

        String updateQuery = 
        "UPDATE Bookings b " +
        "JOIN Vehicles v ON b.VehicleID = v.VehicleID " +
        "SET b.Status = 'completed', v.Status = 'available' " +
        "WHERE b.EndDate < CURRENT_DATE AND b.Status = 'active' AND v.Status = 'rented'";
    
     PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
    int rowsUpdated = updateStmt.executeUpdate();

        updateStmt.close();
      
        connection.close();

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(
            null,
            "Database error: " + ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}

}
