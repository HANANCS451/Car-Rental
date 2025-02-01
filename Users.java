package cs342project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.*;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;

public class Users extends JFrame {
    private JButton b1, b2, b3, b4, b5, b6, b7, b8;
    private JTextField t1;
    private JTable availableCarsTable;
    private JTable myRentalsTable; 
    private DefaultTableModel myRentalsModel; 
    private DefaultTableModel tableModel; 
    private JLabel AvailableCarsLabel, MyRentalsLabel;
     private int userID;

    public Users(int userID) {
          this.userID = userID;
        setTitle("WELCOME TO DRIVE NOW");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize components
        t1 = new JTextField(20);
        b1 = new JButton("Search Car");
        b2 = new JButton("Book Car");
        b3 = new JButton("Load Available Cars");
        b4 = new JButton("Load My Rentals");
        b5 = new JButton("Cancel Rental");
        b6 = new JButton("Mark as Read");
        b7 = new JButton("Delete");
        b8 = new JButton("Logout");

        // Set button colors
        JButton[] buttons = {b1, b2, b3, b4, b5, b6, b7, b8};
        for (JButton button : buttons) {
            button.setBackground(new Color(100, 149, 200));
            button.setForeground(Color.WHITE);
        }

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID", "Make", "Model", "Type", "Color", "Price Per Day"});
        availableCarsTable = new JTable(tableModel);

        myRentalsModel = new DefaultTableModel();
        myRentalsModel = new DefaultTableModel(new Object[]{"Booking ID", "Car", "Start Date", "End Date", "Total Price", "Status"}, 0);
        myRentalsTable = new JTable(myRentalsModel);

        AvailableCarsLabel = new JLabel("Available Cars", SwingConstants.CENTER);
        AvailableCarsLabel.setFont(new Font("Serif", Font.BOLD, 16));
        MyRentalsLabel = new JLabel("My Rentals", SwingConstants.CENTER);
        MyRentalsLabel.setFont(new Font("Serif", Font.BOLD, 16));

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        JPanel availableCarsPanel = new JPanel(new BorderLayout());
        JPanel availableCarsTopPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel availableCarsButtonsPanel = new JPanel();

        searchPanel.add(t1);
        searchPanel.add(b1);

        availableCarsTopPanel.add(searchPanel, BorderLayout.NORTH);
        availableCarsTopPanel.add(AvailableCarsLabel, BorderLayout.SOUTH);

        availableCarsButtonsPanel.add(b2);
        availableCarsButtonsPanel.add(b3);

        JScrollPane tableScrollPane = new JScrollPane(availableCarsTable); 
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        availableCarsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        availableCarsPanel.add(availableCarsTopPanel, BorderLayout.NORTH);
        availableCarsPanel.add(tableScrollPane, BorderLayout.CENTER); 
        availableCarsPanel.add(availableCarsButtonsPanel, BorderLayout.SOUTH);

        JPanel myRentalsPanel = new JPanel(new BorderLayout());
        JPanel myRentalsButtonsPanel = new JPanel();

        myRentalsButtonsPanel.add(b4);
        myRentalsButtonsPanel.add(b5);
        myRentalsButtonsPanel.add(b8);

        JScrollPane rentalsScrollPane = new JScrollPane(myRentalsTable);
        rentalsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        myRentalsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        myRentalsPanel.add(MyRentalsLabel, BorderLayout.NORTH);
        myRentalsPanel.add(rentalsScrollPane, BorderLayout.CENTER);
        myRentalsPanel.add(myRentalsButtonsPanel, BorderLayout.SOUTH);

        leftPanel.add(availableCarsPanel);
        leftPanel.add(myRentalsPanel);
/*
        JPanel notificationsPanel = new JPanel(new BorderLayout());
        JPanel notificationsButtonsPanel = new JPanel();

        notificationsButtonsPanel.add(b6);
        notificationsButtonsPanel.add(b7);

        JScrollPane notificationsScrollPane = new JScrollPane(new JList<>());
        notificationsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        notificationsScrollPane.setPreferredSize(new Dimension(200, 0));

        notificationsPanel.setBorder(BorderFactory.createTitledBorder("Notifications"));
        notificationsPanel.add(notificationsScrollPane, BorderLayout.CENTER);
        notificationsPanel.add(notificationsButtonsPanel, BorderLayout.SOUTH);
*/
        mainPanel.add(leftPanel, BorderLayout.CENTER);
       // mainPanel.add(notificationsPanel, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);

       
        b1.addActionListener(new SearchCar());
        b2.addActionListener(new BookCar());
        b3.addActionListener(new LoadAvailableCars());
        b4.addActionListener(new LoadMyRental());
        b5.addActionListener(new CancelRental());

        b8.addActionListener(new maincss());

        setVisible(true);
    }
    
    class EmptySearchQueryException extends Exception {
    public EmptySearchQueryException(String message) {
        super(message);
    }
}
    class CarNotFoundException extends Exception {
    public CarNotFoundException(String message) {
        super(message);
    }
}
        // Inner Class for "Search Car" Button
    private class SearchCar implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        try {
            String searchQuery = t1.getText().trim().toLowerCase();
            if (searchQuery.isEmpty()) {
                throw new EmptySearchQueryException("Please enter a car name to search.");  }
            // Clear the table model before executing the search
            tableModel.setRowCount(0);
            // Connect to the database
            try (Connection connection = DatabaseConnection.connect()) {
                String query = "SELECT VehicleID, Make, Model, Type, Color, PricePerDay FROM Vehicles WHERE Status = 'available' AND LOWER(Make) = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, searchQuery);
                ResultSet rs = stmt.executeQuery();
                boolean found = false;
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getInt("VehicleID"),
                            rs.getString("Make"),
                            rs.getString("Model"),
                            rs.getString("Type"),
                            rs.getString("Color"),
                            rs.getDouble("PricePerDay")
                    });
                    found = true;
                }

                if (!found) {
                    throw new CarNotFoundException("Car not available.");
                }
            }
        } catch (EmptySearchQueryException ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage(),"Input Error",JOptionPane.ERROR_MESSAGE ); } 
        
        catch (CarNotFoundException ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage(),"Search Result",JOptionPane.INFORMATION_MESSAGE); } 
        
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(),"Database Error",   JOptionPane.ERROR_MESSAGE);}
    }
}

    
    // Inner Class for "Load Available Cars" Button
    private class LoadAvailableCars implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            tableModel.setRowCount(0);
            try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT VehicleID, Make, Model, Type, Color, PricePerDay FROM Vehicles WHERE Status = 'available'";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("VehicleID"),
                        rs.getString("Make"),
                        rs.getString("Model"),
                        rs.getString("Type"),
                        rs.getString("Color"),
                        rs.getDouble("PricePerDay")
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Users.this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        }
    }
   

    
/*
Lamis:
This code handles car rental operations:
1. BookCar: Allows users to select a car, enter rental dates, and confirm booking. 
   The booking is saved in the database, and the car's status is updated to "rented".
2. LoadMyRental: Loads and displays the user's rentals from the database.
3. CancelRental: Allows users to cancel a rental, and updates the booking 
   and vehicle status in the database.
It also includes date validation and user prompts for confirmation during booking and cancellation.
*/

    //Class for handling the "Book Car" button functionality.
     //Class for handling the "Book Car" button functionality.
    private class BookCar implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            int selectedRow = availableCarsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                    Users.this,
                    "No car selected. Please select a car first.",
                    "Selection Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
            }

            // Get selected car details
            int vehicleId = (int) tableModel.getValueAt(selectedRow, 0); 
            String carMake = (String) tableModel.getValueAt(selectedRow, 1); 
            String carModel = (String) tableModel.getValueAt(selectedRow, 2); 
            double pricePerDay = (double) tableModel.getValueAt(selectedRow, 5); 

            
            LocalDate startDate = null, endDate = null;
            String startDateStr , endDateStr;
            while (true) {
            try {
                // Take start date
                startDateStr = JOptionPane.showInputDialog(
                    Users.this,
                    "Enter start date (YYYY-MM-DD):",
                    "Start Date",
                    JOptionPane.QUESTION_MESSAGE
                );
                  // Check if user cancels
                 if (startDateStr == null) {
                    JOptionPane.showMessageDialog(
                    Users.this,
                    "Booking cancelled.",
                    "Cancelled",
                    JOptionPane.INFORMATION_MESSAGE
                    );
                    return; // Exit the method if user cancels
                 }
        
                if (startDateStr.isEmpty()) {
                    throw new IllegalArgumentException("Start date cannot be empty.");
                }

                // Validate format of start date
                startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                // Check if the start date is in the past
                if (startDate.isBefore(LocalDate.now())) {
                    throw new IllegalArgumentException("Start date cannot be in the past.");
                }

                // Take end date
                endDateStr = JOptionPane.showInputDialog(
                    Users.this,
                    "Enter end date (YYYY-MM-DD):",
                    "End Date",
                    JOptionPane.QUESTION_MESSAGE
                );

                if (endDateStr == null) {
                    JOptionPane.showMessageDialog(
                     Users.this,
                     "Booking cancelled.",
                    "Cancelled",
                    JOptionPane.INFORMATION_MESSAGE
                    );
                    return; // Exit the method if user cancels
                }

                if (endDateStr.isEmpty()) {
                    throw new IllegalArgumentException("End date cannot be empty.");
                }

                endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ISO_LOCAL_DATE);

                // Check if end date is before start date
                if (endDate.isBefore(startDate)) {
                    throw new IllegalArgumentException("End date cannot be before start date.");
                }

                // If no exception, break out of the loop (dates are valid)
                break;
                
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(
                    Users.this,
                    "Invalid date format. Please enter a valid date in the format YYYY-MM-DD.",
                    "Date Format Error",
                    JOptionPane.ERROR_MESSAGE
                );
                
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                    Users.this,
                    "Error: " + ex.getMessage(),
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }

            // Calculate the total cost of the booking based on the number of days and daily price
            long daysBooked = 1 +ChronoUnit.DAYS.between(startDate, endDate);
            double totalPrice = daysBooked * pricePerDay;

            // Confirm booking
            int confirm = JOptionPane.showConfirmDialog(
                Users.this,
                "Car: " + carMake + " " + carModel + "\n" +
                "Start Date: " + startDate + "\n" +
                "End Date: " + endDate + "\n" +
                "Days Booked: " + daysBooked + "\n" +
                "Total Price: $" + totalPrice + "\n\n" +
                "Do you want to confirm this booking?",
                "Confirm Booking",
                JOptionPane.YES_NO_OPTION
            );

            // If the user confirms the booking, proceed to book the car
            if (confirm == JOptionPane.YES_OPTION) {
                Connection connection = null;
                PreparedStatement stmt = null;
                PreparedStatement updateStmt = null;
                try {
                    // Open the connection
                    connection = DatabaseConnection.connect();

                    // insert
                    String insertQuery = "INSERT INTO Bookings (VehicleID, UserID, StartDate, EndDate, TotalPrice, Status) VALUES (?, ?, ?, ?, ?, ?)";
                    stmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                    stmt.setInt(1, vehicleId);
                    stmt.setInt(2, userID);
                    stmt.setDate(3, java.sql.Date.valueOf(startDate));
                    stmt.setDate(4, java.sql.Date.valueOf(endDate));
                    stmt.setDouble(5, totalPrice);
                    stmt.setString(6, "active");

                    int rowsInserted = stmt.executeUpdate();

                    if (rowsInserted > 0) {
                         ResultSet generatedKeys = stmt.getGeneratedKeys();
                        int bookingID = -1;
                        if (generatedKeys.next()) {
                        bookingID = generatedKeys.getInt(1); // Get the generated key
            }
                        // Prepare the update statement for vehicle status
                        String updateVehicleStatusQuery = "UPDATE Vehicles SET Status = 'rented' WHERE VehicleID = ?";
                        updateStmt = connection.prepareStatement(updateVehicleStatusQuery);
                        updateStmt.setInt(1, vehicleId);
                        int rowsUpdated = updateStmt.executeUpdate();

                        // Confirm to the user that the booking is successful 
                        if (rowsUpdated > 0) {
                            JOptionPane.showMessageDialog(
                                Users.this,
                                "Booking confirmed.",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                             String carName = carMake + " " + carModel;

                // Open the Bill window and pass the relevant details
                Bill bill = new Bill(bookingID, carName, startDate.toString(), endDate.toString(), totalPrice);
                bill.setVisible(true); 
                        } else {
                            throw new SQLException("Failed to update vehicle status.");
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                        Users.this, 
                        "Database error: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                } finally {
                // Close resources in finally block to ensure they are closed even if an error occurs
                    try {
                        if (stmt != null) stmt.close();
                        if (updateStmt != null)updateStmt.close();
                        if (connection != null)connection.close();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(
                            Users.this, 
                            "Error closing resources: " + ex.getMessage(), 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }   
            }
                  
                      else {
                // If the user does not confirm the booking, show a cancellation message
                JOptionPane.showMessageDialog(
                    Users.this,
                    "Booking request was cancelled. No changes were made.",
                    "Booking Cancelled",
                    JOptionPane.INFORMATION_MESSAGE
            );}
        }    
    }

    //Class for handling the "Load My Rentals" button functionality.
    // Class for handling the "Load My Rentals" button functionality.
    private class LoadMyRental implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            
        myRentalsModel.setRowCount(0); // Clear the current table data
        Connection connection = null;
        PreparedStatement stmt = null;
        int activeRentalsCount = 0; // Counter for active rentals
        StringBuilder message = new StringBuilder(); // For dialog message

        try {
            // Open the connection
            connection = DatabaseConnection.connect();

            // Query to fetch bookings for the user
            String query = "SELECT b.BookingID, CONCAT(v.Make, ' ', v.Model) AS Car, b.StartDate, b.EndDate, b.TotalPrice, b.Status " +
                           "FROM Bookings b " +
                           "JOIN Vehicles v ON b.VehicleID = v.VehicleID " +
                           "WHERE b.UserID = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();

            // Process each record
            while (rs.next()) {
                myRentalsModel.addRow(new Object[] {
                        rs.getInt("BookingID"),
                        rs.getString("Car"),
                        rs.getDate("StartDate"),
                        rs.getDate("EndDate"),
                        rs.getDouble("TotalPrice"),
                        rs.getString("Status")
                });
                
                // Increment active rentals count if status is "active"
                if ("active".equalsIgnoreCase(rs.getString("Status"))) {
                    activeRentalsCount++;

                    // Calculate remaining days if the rental is active
                    long remainingDays = 0; // Default value
                    if (rs.getDate("EndDate") != null) {
                        LocalDate today = LocalDate.now(); // Get today's date
                        LocalDate endDate = rs.getDate("EndDate").toLocalDate(); // Convert SQL Date to LocalDate
                        remainingDays = ChronoUnit.DAYS.between(today, endDate);
                    }

                    // Append booking details and remaining days to the message
                    message.append("Booking ID: ").append(rs.getInt("BookingID"))
                           .append(", Car: ").append(rs.getString("Car"))
                           .append(", Remaining Days: ").append(remainingDays).append("\n");
                }
            }

            // Prepare final message
            if (activeRentalsCount > 0) {
                message.insert(0, "You have " + activeRentalsCount + " active rental(s):\n\n");
            } else {
                message.append("You have no active rentals.");
            }

            // Show dialog with the details
            JOptionPane.showMessageDialog(
                Users.this,
                message.toString(),
                
                "Active Rentals",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException ex) {
            // Handle database connection or query execution errors
            JOptionPane.showMessageDialog(
                    Users.this,
                    "Database error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }catch (IllegalArgumentException ex) {
                // Handle any invalid argument errors
                JOptionPane.showMessageDialog(
                    Users.this,
                    "Invalid input: " + ex.getMessage(),
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            }  finally {
            // Close resources in finally block
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        Users.this,
                        "Error closing resources: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    }

    //Class for handling the "Cancel Rental" button functionality.
    private class CancelRental implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get the selected row from the "My Rentals" table
            int selectedRow = myRentalsTable.getSelectedRow(); 
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                    Users.this,
                    "No car selected. Please select a car first.",
                    "Selection Error",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Get selected rental details 
            int bookingId = (int) myRentalsModel.getValueAt(selectedRow, 0);
            String car = (String) myRentalsModel.getValueAt(selectedRow, 1);
            LocalDate endDate = ((java.sql.Date) myRentalsModel.getValueAt(selectedRow, 3)).toLocalDate();
            String bookingStatus = (String) myRentalsModel.getValueAt(selectedRow, 5);
            
            if (!"active".equalsIgnoreCase(bookingStatus)) {
                JOptionPane.showMessageDialog(
                    Users.this,
                    "Only active bookings can be cancelled.",
                    "Cancellation Not Allowed",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            // Calculate the remaining days
            long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), endDate);
           
        
            // Initialize database resources
            Connection connection = null;
            PreparedStatement getVehicleStmt = null;
            PreparedStatement cancelBookingStmt = null;
            PreparedStatement updateVehicleStmt = null;

            try {
                connection = DatabaseConnection.connect();

                // Retrieve VehicleID associated with the booking
                String getVehicleQuery = "SELECT VehicleID FROM Bookings WHERE BookingID = ?";
                getVehicleStmt = connection.prepareStatement(getVehicleQuery);
                getVehicleStmt.setInt(1, bookingId);
                ResultSet rs = getVehicleStmt.executeQuery();

                if (!rs.next()) {
                    throw new NoSuchElementException("No vehicle found for the selected booking.");
                }
                int vehicleId = rs.getInt("VehicleID");


                // Confirm cancellation with user
                int confirm = JOptionPane.showConfirmDialog(
                    Users.this,
                    "Car: " + car + "\n" +
                    "Days Remaining: " + daysRemaining + "\n\n" +
                    "Are you sure you want to cancel this booking?",
                    "Confirm Cancellation",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    // Update booking status to "cancelled"
                    String cancelBookingQuery = "UPDATE Bookings SET Status = 'canceled' WHERE BookingID = ?";
                    cancelBookingStmt = connection.prepareStatement(cancelBookingQuery);
                    cancelBookingStmt.setInt(1, bookingId);
                    int rowsUpdated = cancelBookingStmt.executeUpdate();

                    if (rowsUpdated == 0) {
                        throw new SQLException("Failed to update the booking status.");
                    }

                    // Update vehicle status to "available"
                    String updateVehicleQuery = "UPDATE Vehicles SET Status = 'available' WHERE VehicleID = ?";
                    updateVehicleStmt = connection.prepareStatement(updateVehicleQuery);
                    updateVehicleStmt.setInt(1, vehicleId);
                    int vehicleRowsUpdated = updateVehicleStmt.executeUpdate();

                    if (vehicleRowsUpdated == 0) {
                        throw new SQLException("Failed to update the booking status.");
                    }
                    //myRentalsModel.setValueAt("canceled", selectedRow, 5);
                    JOptionPane.showMessageDialog(
                        Users.this, 
                        "Rental canceled.", 
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        Users.this, 
                        "The booking was not canceled.", 
                        "Cancellation Aborted", 
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                    Users.this, 
                    "Database error: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (NoSuchElementException ex) {
                // Handle cases where no data is found for the vehicle or booking
                JOptionPane.showMessageDialog(
                    Users.this,
                    "Data error: " + ex.getMessage(),
                    "Data Not Found",
                    JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                // Handle any invalid argument errors
                JOptionPane.showMessageDialog(
                    Users.this,
                    "Invalid input: " + ex.getMessage(),
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            }  finally {
            // Ensure all database resources are properly closed
                try {
                    if (cancelBookingStmt != null) cancelBookingStmt.close();
                    if (getVehicleStmt != null) getVehicleStmt.close();
                    if (updateVehicleStmt != null) updateVehicleStmt.close();
                    if (connection != null) connection.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                    Users.this,
                    "Error closing resources: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}

 
    public class maincss implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            FirstPage f = new FirstPage();
            f.setVisible(true);
            dispose();
        }
    }
}
