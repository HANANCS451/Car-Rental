package cs342project;
import java.sql.*;

public class DatabaseConnection {

    // database information 
    private static final String URL = "jdbc:mysql://cars.c300y0suq7by.eu-north-1.rds.amazonaws.com:3306/CarRental";
    private static final String USER = "admin";
    private static final String PASSWORD = "laM#003#";

    // Connection object
    private static Connection connection = null;
    
    // Establish a connection to the database
    public static Connection connect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions and print the error message
            System.out.println("error " + e.getMessage());
        }
        return connection;// Return the connection object
    }

}
