# Car-Rental software
Overview
A Java Swing-based desktop application for managing car rentals. It allows users to browse cars, book rentals, and manage reservations, while administrators can oversee inventory, transactions, and user accounts.

Features
User Features
Sign Up & Login: Secure authentication for users.
Car Browsing: View available cars with details.
Car Booking: Rent cars with a specified duration.
Rental Management: View and cancel bookings.
Profile Management: Update personal details.
Admin Features
Car Inventory Management: Add, modify, or remove cars.
Rental History Tracking: Monitor rental transactions.
User Management: Manage user accounts and roles.
Reports & Analytics: Generate reports on bookings and revenue.
Technologies Used
Language: Java
GUI: Java Swing
Database: MySQL (Amazon RDS)
IDE: NetBeans
Database Structure
Users: Stores user credentials and roles.
Vehicles: Maintains car details and availability.
Bookings: Records rental transactions.
Reports: Logs revenue and booking data.
Installation & Setup
# Clone the repository
git clone https://github.com/YourUsername/Car_Rental_Software.git

# Navigate to the project directory
cd Car_Rental_Software
Configure Database Connection:
Ensure MySQL is installed and running.
Update database credentials in the configuration file.
Run the Application:
Open the project in NetBeans.
Compile and run the application.
Exception Handling
EmptyFieldException: Prevents blank required fields.
InvalidLoginException: Handles incorrect login attempts.
SQLException: Manages database connection errors.
InvalidFormatException: Ensures input validation.
Testing
The application was tested for:

Button functionalities
Login & sign-up interactions
Booking and cancellation workflows
Admin dashboard operations
