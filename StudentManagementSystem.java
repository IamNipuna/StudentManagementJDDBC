import java.sql.*;
import java.util.Scanner;

public class StudentManagementSystem {

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/university";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connected to the database successfully!");
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n--- Student Management System ---");
                System.out.println("1. Add Student");
                System.out.println("2. View All Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addStudent(connection, scanner);
                        break;
                    case 2:
                        viewAllStudents(connection);
                        break;
                    case 3:
                        updateStudent(connection, scanner);
                        break;
                    case 4:
                        deleteStudent(connection, scanner);
                        break;
                    case 5:
                        System.out.println("Exiting system. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    private static void addStudent(Connection connection, Scanner scanner) {
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Student ID: ");
        String studentID = scanner.nextLine();
        System.out.print("Enter Graduation Year: ");
        int graduationYear = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Degree Program: ");
        String degreeProgram = scanner.nextLine();

        String sql = "INSERT INTO students (firstName, lastName, age, studentID, graduationYear, degreeProgram) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, age);
            statement.setString(4, studentID);
            statement.setInt(5, graduationYear);
            statement.setString(6, degreeProgram);
            statement.executeUpdate();
            System.out.println("Student added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    private static void viewAllStudents(Connection connection) {
        String sql = "SELECT * FROM students";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            System.out.println("\n--- Student List ---");
            while (resultSet.next()) {
                System.out.printf("ID: %s | Name: %s %s | Age: %d | Graduation Year: %d | Degree Program: %s\n",
                        resultSet.getString("studentID"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getInt("age"),
                        resultSet.getInt("graduationYear"),
                        resultSet.getString("degreeProgram"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving students: " + e.getMessage());
        }
    }

    private static void updateStudent(Connection connection, Scanner scanner) {
        System.out.print("Enter Student ID to update: ");
        String studentID = scanner.nextLine();

        System.out.print("Enter new First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter new Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter new Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new Graduation Year: ");
        int graduationYear = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new Degree Program: ");
        String degreeProgram = scanner.nextLine();

        String sql = "UPDATE students SET firstName = ?, lastName = ?, age = ?, graduationYear = ?, degreeProgram = ? WHERE studentID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, age);
            statement.setInt(4, graduationYear);
            statement.setString(5, degreeProgram);
            statement.setString(6, studentID);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student with ID " + studentID + " not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }

    private static void deleteStudent(Connection connection, Scanner scanner) {
        System.out.print("Enter Student ID to delete: ");
        String studentID = scanner.nextLine();

        String sql = "DELETE FROM students WHERE studentID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, studentID);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student with ID " + studentID + " not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }
}
