package cs342project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame {
    private JLabel l1, l2, l3, l4;
    private JTextField t1;
    private JPasswordField txtPassword;
    private JComboBox<String> c1;
    private JButton b1, b2;

    public Login() {
        setTitle("WELCOME TO DRIVE NOW");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Top Panel (Form Area)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 15, 10);

        // Title
        l1 = new JLabel("LOGIN TO YOUR ACCOUNT");
        l1.setFont(new Font("Serif", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(l1, gbc);

        // Username
        l2 = new JLabel("Username:");
        l2.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(l2, gbc);

        t1 = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(t1, gbc);

        // Password
        l3 = new JLabel("Password:");
        l3.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(l3, gbc);

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(t1.getPreferredSize());
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(txtPassword, gbc);

        // Role
        l4 = new JLabel("Role:");
        l4.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(l4, gbc);

        c1 = new JComboBox<>(new String[]{"Customer", "Admin"});
        int comboWidth = t1.getPreferredSize().width / 2;
        c1.setPreferredSize(new Dimension(comboWidth, t1.getPreferredSize().height));
        c1.setBackground(new Color(100, 149, 200));
        c1.setForeground(Color.WHITE);
        c1.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(c1, gbc);

        // Login and Cancel Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        b1 = new JButton("Login");
        b1.setBackground(new Color(100, 149, 200));
        b1.setForeground(Color.WHITE);
        b1.setFont(new Font("Arial", Font.BOLD, 18));

        b2 = new JButton("Cancel");
        b2.setBackground(new Color(100, 149, 200));
        b2.setForeground(Color.WHITE);
        b2.setFont(new Font("Arial", Font.BOLD, 18));

        buttonPanel.add(b1);
        buttonPanel.add(b2);

        // Add buttons panel to formPanel
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 10, 10, 10);
        formPanel.add(buttonPanel, gbc);

        // Bottom Panel (For Image)
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\USER 1\\Downloads\\Screenshot_2024-11-21_155601-removebg-preview.png");
        Image scaledImage = imageIcon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imagePanel.add(imageLabel);

        // Add Panels to Frame
        add(formPanel, BorderLayout.CENTER);
        add(imagePanel, BorderLayout.SOUTH);

        // Add action listeners
        b1.addActionListener(new LoginAction());
        b2.addActionListener(new maincss());
        setVisible(true);
    }
    public class EmptyException extends Exception {
    public EmptyException(String message) {
        super(message); }}

public class IncorrectLoginException extends Exception {
    public IncorrectLoginException(String message) {
        super(message); }}

    public class maincss implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            FirstPage m = new FirstPage();
            m.setVisible(true);
            dispose();
        }
    }

    class LoginAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        String username = t1.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String role = (String) c1.getSelectedItem();

       
        try {
             if (username.isEmpty() || password.isEmpty()) {
    throw new EmptyException("Username and password cannot be empty.");
}

           Connection connection = DatabaseConnection.connect();
            // Query
            String query = "SELECT * FROM Users WHERE Username = ? AND Password = ? AND Role = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userID = rs.getInt("UserID"); // لميس : علشان اقدر اربط المستخدم بالطلب
                JOptionPane.showMessageDialog(
                        null,
                        "Welcome, " + username + "!",
                        "Login Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // Navigate to the appropriate window based on role
                if (role.equals("Admin")) {
                    AdminWindow1 adminWindow = new AdminWindow1(); // نافذة الأدمن
                    adminWindow.setVisible(true);
                } else if (role.equals("Customer")) {
                    Users userWindow = new Users(userID); // نافذة المستخدم   
                    userWindow.setVisible(true);
                }
                dispose(); // Close login window
            } else {
                    throw new IncorrectLoginException("Invalid login credentials.");
                }


            rs.close();
            stmt.close();
            connection.close();
              } catch (EmptyException | IncorrectLoginException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
            } 
            catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Database error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

    public static void main(String[] args) {
        new Login();
    }
}
