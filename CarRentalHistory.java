
package cs342project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CarRentalHistory extends JFrame {
    private JTable historyTable, statsTable, totalRevenueTable;
    private DefaultTableModel historyTableModel, statsTableModel, totalRevenueTableModel;
    private JButton back;

    public CarRentalHistory() {
        super("Car Rental History");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Rental History", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // History Table Setup
        String[] historyColumns = {
                "Rental ID", "Customer Name", "Car Model", "Rental Date", "Return Date", "Daily Price", "Total Price", "Booking Status"
        };
        historyTableModel = new DefaultTableModel(historyColumns, 0);
        historyTable = new JTable(historyTableModel);
        JScrollPane historyScrollPane = new JScrollPane(historyTable);

        // Stats Table Setup
        String[] statsColumns = {
                "Car ID", "Car Model", "Times Rented", "Monthly Revenue"
        };
        statsTableModel = new DefaultTableModel(statsColumns, 0);
        statsTable = new JTable(statsTableModel);
        JScrollPane statsScrollPane = new JScrollPane(statsTable);

        // Total Revenue Table Setup
        String[] totalRevenueColumns = {
                "Month", "Year", "Total Revenue"
        };
        totalRevenueTableModel = new DefaultTableModel(totalRevenueColumns, 0);
        totalRevenueTable = new JTable(totalRevenueTableModel);
        JScrollPane totalRevenueScrollPane = new JScrollPane(totalRevenueTable);

        // Panel to Hold Tables
        JPanel tablesPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        tablesPanel.add(historyScrollPane);
        tablesPanel.add(statsScrollPane);
        tablesPanel.add(totalRevenueScrollPane);
        add(tablesPanel, BorderLayout.CENTER);

        // Back Button Setup
        JPanel buttonPanel = new JPanel();
        back = new JButton("Back");
        back.setFont(new Font("Serif", Font.BOLD, 14));
        back.setBackground(new Color(100, 149, 200));
        back.setForeground(Color.WHITE);
        back.setPreferredSize(new Dimension(120, 35));
        buttonPanel.add(back);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        back.addActionListener(new BackActionListener());

        // Load Data
        loadRentalHistoryFromDatabase();
        loadStatisticsFromDatabase();

        setVisible(true);
    }

    private class BackActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new AdminWindow1().setVisible(true);
            dispose();
        }
    }

    private void loadRentalHistoryFromDatabase() {
        String dbUrl = "jdbc:mysql://cars.c300y0suq7by.eu-north-1.rds.amazonaws.com:3306/CarRental";
        String dbUser = "admin";
        String dbPassword = "laM#003#";

        String query = " SELECT  b.BookingID AS RentalID, u.FullName AS CustomerName, "
                + "CONCAT(v.Make, ' ', v.Model) AS CarModel, b.StartDate AS RentalDate,"
                + " b.EndDate AS ReturnDate, v.PricePerDay AS DailyPrice,(GREATEST(DATEDIFF(b.EndDate, b.StartDate),"
                + " 1) * v.PricePerDay) AS TotalPrice, b.Status AS BookingStatus FROM Bookings b JOIN Users u"
                + " ON b.UserID = u.UserID JOIN  Vehicles v ON b.VehicleID = v.VehicleID;";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            historyTableModel.setRowCount(0); // Clear existing data

            while (rs.next()) {
                Object[] row = {
                        rs.getString("RentalID"),
                        rs.getString("CustomerName"),
                        rs.getString("CarModel"),
                        rs.getString("RentalDate"),
                        rs.getString("ReturnDate"),
                        rs.getDouble("DailyPrice"),
                        rs.getDouble("TotalPrice"),
                        rs.getString("BookingStatus")
                };
                historyTableModel.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading rental history: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadStatisticsFromDatabase() {
        String dbUrl = "jdbc:mysql://cars.c300y0suq7by.eu-north-1.rds.amazonaws.com:3306/CarRental";
        String dbUser = "admin";
        String dbPassword = "laM#003#";

        String query = 
      "  SELECT "
           +" v.VehicleID AS CarID,"
           +" CONCAT(v.Make, ' ', v.Model) AS CarModel,"
           +" COUNT(b.BookingID) AS TimesRented,"
           +" SUM(GREATEST(DATEDIFF(b.EndDate, b.StartDate), 1) * v.PricePerDay) AS MonthlyRevenue"
      +"  FROM "
          +"  Bookings b"
       +" JOIN "
           +" Vehicles v ON b.VehicleID = v.VehicleID"
      +"  WHERE "
          +"  MONTH(b.StartDate) = MONTH(CURRENT_DATE())"
           +" AND YEAR(b.StartDate) = YEAR(CURRENT_DATE())"
      +"  GROUP BY "
          +"  v.VehicleID, v.Make, v.Model;"
    ;

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            statsTableModel.setRowCount(0); // Clear existing data

            double totalMonthlyRevenue = 0;
            String currentMonth = java.time.LocalDate.now().getMonth().toString();
            int currentYear = java.time.LocalDate.now().getYear();

            while (rs.next()) {
                Object[] row = {
                        rs.getString("CarID"),
                        rs.getString("CarModel"),
                        rs.getInt("TimesRented"),
                        rs.getDouble("MonthlyRevenue")
                };
                totalMonthlyRevenue += rs.getDouble("MonthlyRevenue");
                statsTableModel.addRow(row);
            }

            // Add total revenue to its table
            totalRevenueTableModel.setRowCount(0); // Clear previous data
            totalRevenueTableModel.addRow(new Object[]{currentMonth, currentYear, totalMonthlyRevenue});

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading statistics: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CarRentalHistory::new);
    }
}
