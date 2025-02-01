package cs342project;

import javax.swing.*;
import java.awt.*;

public class GetToKnowUs extends JFrame {
    public GetToKnowUs() {
        setTitle("About Us");
        setSize(800, 600); // Window size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Main layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Vertical layout
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Adjust padding (top, left, bottom, right)

        // Add image at the top
        ImageIcon icon = new ImageIcon("C:\\Users\\L4312\\OneDrive\\الصور\\photo_2024-12-11_19-54-09-removebg-preview.png"); // Make sure the image is in the project directory
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(300, 150, Image.SCALE_SMOOTH); // Resize the image
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        JLabel imageLabel = new JLabel(resizedIcon, SwingConstants.CENTER);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the image

        // Add information text
        JLabel aboutLabel = new JLabel("<html><div style='text-align: center;'>"
                + "<h2>Welcome to the DRIVE NOW Car Rental System!</h2>"
                + "<p>We provide reliable and affordable vehicles for your travel needs.</p>"
                + "<p>Enjoy a seamless experience with us.</p>"
                + "<p>Your journey starts here!</p>"
                + "</div></html>", SwingConstants.CENTER);
        aboutLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        aboutLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the text

        // Add button
        JButton mainMenuButton = new JButton("Main Screen");
        mainMenuButton.setBackground(new Color(100, 149, 200)); // Cornflower blue
        mainMenuButton.setForeground(Color.WHITE);
        mainMenuButton.setFont(new Font("Arial", Font.BOLD, 16));
        mainMenuButton.setPreferredSize(new Dimension(200, 50));
        mainMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button

        // Add components to the main panel
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer (small space at the top)
        mainPanel.add(imageLabel); // Add image
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between image and text
        mainPanel.add(aboutLabel); // Add text
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Larger space between text and button
        mainPanel.add(mainMenuButton); // Add button

        // Add main panel to frame
        this.add(mainPanel);

        // Action listener for the "Main Menu" button
        mainMenuButton.addActionListener(e -> {
            FirstPage firstPage = new FirstPage();
            firstPage.setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GetToKnowUs frame = new GetToKnowUs();
            frame.setVisible(true);
        });
    }
}
