
package cs342project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ModificationOnCars extends JFrame {
    private JButton DeletB, BackB, AddCarB;
    private JTextField TypeT, ModelT, CompanyT, ColorT, PriceT;
    private JComboBox<String> year;
    private JTable carTable;
    private DefaultTableModel carTableModel; // Model for JTable
    private JLabel AddCarLabel, DeleteCarLabel;
    private String[] Years = {"Year", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025"};

    public ModificationOnCars() {
        setTitle("Modification");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TypeT = new JTextField();
        ModelT = new JTextField();
        CompanyT = new JTextField();
        ColorT = new JTextField();
        PriceT = new JTextField();
        year = new JComboBox<>(Years);

        Font textFieldFont = new Font("Serif", Font.PLAIN, 16);
        JTextField[] textFields = {TypeT, ModelT, CompanyT, ColorT, PriceT};
        for (JTextField textField : textFields) {
            textField.setFont(textFieldFont);
            textField.setPreferredSize(new Dimension(250, 25));
        }

        DeletB = new JButton("Delete Car");
        BackB = new JButton("Back");
        AddCarB = new JButton("Add Car");

        JButton[] buttons = {DeletB, BackB, AddCarB};
        Font buttonFont = new Font("Serif", Font.BOLD, 14);
        Color buttonColor = new Color(100, 149, 200);
        Color buttonTextColor = Color.WHITE;
        for (JButton button : buttons) {
            button.setFont(buttonFont);
            button.setPreferredSize(new Dimension(120, 35));
            button.setBackground(buttonColor);
            button.setForeground(buttonTextColor);
        }
        
        AddCarLabel = new JLabel("Add Car", SwingConstants.CENTER);
        AddCarLabel.setFont(new Font("Serif", Font.BOLD, 24));
        AddCarLabel.setForeground(Color.BLACK);

        DeleteCarLabel = new JLabel("Delete Car", SwingConstants.CENTER);
        DeleteCarLabel.setFont(new Font("Serif", Font.BOLD, 24));
        DeleteCarLabel.setForeground(Color.BLACK);

        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(AddCarLabel, BorderLayout.NORTH);

        JPanel deleteCarPanel = new JPanel(new BorderLayout());

        JPanel deleteHeaderPanel = new JPanel(new BorderLayout());

        JPanel deleteCarInputs = new JPanel(new GridLayout(2, 3, 15, 15));

        JLabel[] deleteLabels = {
            new JLabel("Type:"), new JLabel("Model:"), new JLabel("Year:"),
            new JLabel("Company:"), new JLabel("Color:"), new JLabel("Daily Rent Price:")
        };

        for (JLabel label : deleteLabels) {
            label.setFont(new Font("Serif", Font.BOLD, 16));
        }

        deleteCarInputs.add(deleteLabels[0]);
        deleteCarInputs.add(TypeT);
        deleteCarInputs.add(deleteLabels[1]);
        deleteCarInputs.add(ModelT);
        deleteCarInputs.add(deleteLabels[2]);
        deleteCarInputs.add(year);
        deleteCarInputs.add(deleteLabels[3]);
        deleteCarInputs.add(CompanyT);
        deleteCarInputs.add(deleteLabels[4]);
        deleteCarInputs.add(ColorT);
        deleteCarInputs.add(deleteLabels[5]);
        deleteCarInputs.add(PriceT);

        deleteHeaderPanel.add(deleteCarInputs, BorderLayout.CENTER);

        deleteHeaderPanel.add(DeleteCarLabel, BorderLayout.SOUTH);

        deleteCarPanel.add(deleteHeaderPanel, BorderLayout.NORTH);

        // إعداد الجدول
        String[] columnNames = {"Make", "Model", "Year", "Type", "Color", "Price/Day"};
        carTableModel = new DefaultTableModel(columnNames, 0); // جدول بأعمدة فقط
        carTable = new JTable(carTableModel);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane availableCarsScroll = new JScrollPane(carTable);
        availableCarsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        availableCarsScroll.setPreferredSize(new Dimension(400, 400));

        JPanel scrollPanel = new JPanel(new BorderLayout());
        scrollPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        scrollPanel.add(availableCarsScroll, BorderLayout.CENTER);

        deleteCarPanel.add(scrollPanel, BorderLayout.CENTER);
        //
        JPanel deleteButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        deleteButtonPanel.add(AddCarB);
        deleteButtonPanel.add(DeletB);
        deleteButtonPanel.add(BackB);

        deleteCarPanel.add(deleteButtonPanel, BorderLayout.SOUTH);

        mainPanel.add(deleteCarPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
       
       // bottomPanel.add(BackB);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);

        // Load cars from the database
        loadCarsFromDatabase();

        setVisible(true);

        AddCarB.addActionListener(new Add());
        DeletB.addActionListener(new Delete());
        BackB.addActionListener(new Back());
            }

    // Load cars from the database into the JTable
    private void loadCarsFromDatabase() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://cars.c300y0suq7by.eu-north-1.rds.amazonaws.com:3306/CarRental", "admin", "laM#003#");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Vehicles")) {

            // Clear the table model
            carTableModel.setRowCount(0);

            // Populate the table with data from the database
            while (rs.next()) {
                carTableModel.addRow(new Object[]{
                        rs.getString("Make"),
                        rs.getString("Model"),
                        rs.getString("Year"),
                        rs.getString("Type"),
                        rs.getString("Color"),
                        rs.getDouble("PricePerDay")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading cars: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Custom Exception for validation errors
    public class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }

    public class Add implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                // التحقق من صحة الإدخالات
                if (CompanyT.getText().isEmpty() || ModelT.getText().isEmpty() || TypeT.getText().isEmpty() || 
                    ColorT.getText().isEmpty() || PriceT.getText().isEmpty() || year.getSelectedIndex() == 0) {
                    throw new ValidationException("All fields must be filled in.");
                }

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://cars.c300y0suq7by.eu-north-1.rds.amazonaws.com:3306/CarRental", "admin", "laM#003#");
                     PreparedStatement stmt = conn.prepareStatement("INSERT INTO Vehicles (Make, Model, Year, Type, Color, PricePerDay) VALUES (?, ?, ?, ?, ?, ?)");) {

                    stmt.setString(1, CompanyT.getText());
                    stmt.setString(2, ModelT.getText());
                    stmt.setString(3, year.getSelectedItem().toString());
                    stmt.setString(4, TypeT.getText());
                    stmt.setString(5, ColorT.getText());
                    stmt.setDouble(6, Double.parseDouble(PriceT.getText()));

                    int rowsInserted = stmt.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(null, "Car added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadCarsFromDatabase();
                    }
                } catch (SQLException ex) {
                    throw new ValidationException("Database error: " + ex.getMessage());
                } catch (NumberFormatException ex) {
                    throw new ValidationException("Invalid price. Please enter a valid number.");
                }
            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public class Delete implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int selectedRow = carTable.getSelectedRow();
                if (selectedRow == -1) {
                    throw new ValidationException("Please select a car to delete.");
                }

                String make = carTableModel.getValueAt(selectedRow, 0).toString();
                String model = carTableModel.getValueAt(selectedRow, 1).toString();
                String year = carTableModel.getValueAt(selectedRow, 2).toString();

                int confirmation = JOptionPane.showConfirmDialog(null, 
                    "Are you sure you want to delete the selected car?", 
                    "Confirm Deletion", 
                    JOptionPane.YES_NO_OPTION);

                if (confirmation == JOptionPane.YES_OPTION) {
                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://cars.c300y0suq7by.eu-north-1.rds.amazonaws.com:3306/CarRental", "admin", "laM#003#");
                         PreparedStatement stmt = conn.prepareStatement("DELETE FROM Vehicles WHERE Make = ? AND Model = ? AND Year = ?");) {

                        stmt.setString(1, make);
                        stmt.setString(2, model);
                        stmt.setString(3, year);

                        int rowsDeleted = stmt.executeUpdate();
                        if (rowsDeleted > 0) {
                            JOptionPane.showMessageDialog(null, "Car deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            loadCarsFromDatabase();
                        }
                    } catch (SQLException ex) {
                        throw new ValidationException("Error deleting car: " + ex.getMessage());
                    }
                }
            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

   

    public class Back implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            AdminWindow1 B = new AdminWindow1();
            B.setVisible(true);
            dispose();
        }
    }

    public static void main(String[] args) {
        new ModificationOnCars();
    }
}
