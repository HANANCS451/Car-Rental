package cs342project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SignUp extends JFrame {
    private JLabel l1, L2, L3, L4, L5, L6;
    private JTextField T1, T2, T3, T4;
    private JPasswordField txtPassword;
    private JButton b1, b2;

    public SignUp() {
        setTitle("WELCOME TO DRIVE NOW");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Top Panel (Form Area)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        l1 = new JLabel("CREATE YOUR ACCOUNT");
        l1.setFont(new Font("Serif", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(l1, gbc);

        // Full Name
        L2 = new JLabel("Full Name:");
        L2.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(L2, gbc);

        T1 = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(T1, gbc);

        // Username
        L3 = new JLabel("Username:");
        L3.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(L3, gbc);

        T2 = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(T2, gbc);

        // Password
        L4 = new JLabel("Password:");
        L4.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(L4, gbc);

        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(txtPassword, gbc);

        // Email
        L5 = new JLabel("Email:");
        L5.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(L5, gbc);

        T3 = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(T3, gbc);

        // Phone Number
        L6 = new JLabel("Phone Number:");
        L6.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(L6, gbc);

        T4 = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(T4, gbc);

        // Buttons Panel (Sign Up and Cancel)
        b1 = new JButton("Sign Up");
        b1.setBackground(new Color(100, 149, 200));
        b1.setForeground(Color.WHITE);
        b1.setFont(new Font("Arial", Font.BOLD, 18));

        b2 = new JButton("Cancel");
        b2.setBackground(new Color(100, 149, 200));
        b2.setForeground(Color.WHITE);
        b2.setFont(new Font("Arial", Font.BOLD, 18));

        // Panel to align buttons in the center
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.add(b1);
        buttonPanel.add(b2);

        // Add buttons panel to formPanel
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(50, 10, 10, 10);
        formPanel.add(buttonPanel, gbc);

        // Bottom Panel (For Image)
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\USER 1\\Downloads\\Screenshot_2024-11-21_155601-removebg-preview.png");
        Image img = imageIcon.getImage();
        Image resizedImage = img.getScaledInstance(300, 150, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        JLabel imageLabel = new JLabel(resizedIcon);
        imagePanel.add(imageLabel);
        // Add Panels to Frame
        add(formPanel, BorderLayout.CENTER);
        add(imagePanel, BorderLayout.SOUTH);
        setVisible(true);
        b1.addActionListener(new SignUpAction());
        b2.addActionListener(new maincs());
    }
class EmptyFieldException extends Exception {
    public EmptyFieldException(String message) {
        super(message);
    }
}
class InvalidEmailException extends Exception {
    public InvalidEmailException(String message) {
        super(message);
    }
}
class InvalidPhoneNumberException extends Exception {
    public InvalidPhoneNumberException(String message) {
        super(message);
    }
}
class InvalidFieldFormatException extends Exception {
    public InvalidFieldFormatException(String message) {
        super(message);
    }
}

class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
    public class SignUpAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        String fullName = T1.getText().trim();
        String username = T2.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String email = T3.getText().trim();
        String phone = T4.getText().trim();
        try {
            
            
            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                throw new EmptyFieldException("All fields are required."); }
            
            if (!fullName.matches("^[a-zA-Z\\s]+$")) {
                throw new InvalidFieldFormatException("Full name can only contain letters and spaces."); }
            
            if (!phone.matches("^5\\d{8}$")) {
                throw new InvalidPhoneNumberException("Invalid phone number. It must be 10 digits and start with 5."); }
          
            if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                throw new InvalidEmailException("Invalid email format."); }
            
            if (!password.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$")) {
                throw new InvalidPasswordException("Password must include at least one uppercase letter, one number, and one special character.");}

             if (password.toLowerCase().contains(username.toLowerCase()) || password.toLowerCase().contains(email.split("@")[0].toLowerCase())) {
                throw new InvalidPasswordException("Password should not be similar to username or email.");}
            // Connect to MySQL database
            Connection connection = DatabaseConnection.connect();
            // Check if email, phone, or password already exists
            String checkQuery = "SELECT * FROM Users WHERE Email = ? OR Phone = ? OR Password = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, email);
            checkStmt.setString(2, phone);
            checkStmt.setString(3, password);

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                // Determine which field is duplicated
                String existingField = "";
                if (rs.getString("Email").equals(email)) {
                    existingField = "Email";
                } else if (rs.getString("Phone").equals(phone)) {
                    existingField = "Phone number";
                } else if (rs.getString("Password").equals(password)) {
                    existingField = "Password";
                }
                JOptionPane.showMessageDialog(
                        null,
                        existingField + " is already registered. Please use a different one.",
                        "Sign Up Failed",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Insert new user
            String insertQuery = "INSERT INTO Users (FullName, Username, Password, Email, Phone, Role) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
            insertStmt.setString(1, fullName);
            insertStmt.setString(2, username);
            insertStmt.setString(3, password);
            insertStmt.setString(4, email);
            insertStmt.setString(5, phone);
            insertStmt.setString(6, "Customer"); // Default role is Customer
           
            int rows = insertStmt.executeUpdate();
            if (rows > 0) {
                // Retrieve the UserID using a SELECT query
                String getIdQuery = "SELECT UserID FROM Users WHERE Username = ?";
                PreparedStatement getIdStmt = connection.prepareStatement(getIdQuery);
                getIdStmt.setString(1, username);

                ResultSet idResult = getIdStmt.executeQuery();
                int userID = -1;
                if (idResult.next()) {
                    userID = idResult.getInt("UserID");
                }
                JOptionPane.showMessageDialog(
                        null,
                        "Sign Up Successful! Welcome, " + fullName + "!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                
                // Navigate to Users page
                Users userPage = new Users(userID);
                userPage.setVisible(true);
                dispose(); // Close the current SignUp window
            }

            // Close resources
            rs.close();
            checkStmt.close();
            insertStmt.close();
            connection.close();
            
        } catch (EmptyFieldException ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage(), "Input Error",JOptionPane.ERROR_MESSAGE);} 
        
        catch (InvalidFieldFormatException ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage(),"Input Error",JOptionPane.ERROR_MESSAGE);}
        
        catch (InvalidPhoneNumberException ex) {JOptionPane.showMessageDialog(null, ex.getMessage(),"Input Error", JOptionPane.ERROR_MESSAGE );} 
       
        catch (InvalidEmailException ex) {JOptionPane.showMessageDialog(null,ex.getMessage(), "Input Error",JOptionPane.ERROR_MESSAGE );} 
        
         catch (InvalidPasswordException ex) {JOptionPane.showMessageDialog(null,ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);} 
        
        catch (SQLException ex) {JOptionPane.showMessageDialog(null,"Database error: " + ex.getMessage(),"Error", JOptionPane.ERROR_MESSAGE );}
    }
}

    public class maincs implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            FirstPage s = new FirstPage();
            s.setVisible(true);
            dispose();
        }
    }

    public static void main(String[] args) {
        new SignUp();
    }
}
