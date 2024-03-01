import java.sql.*;
import java.util.Scanner;
public class Main {
    public static void main(String[] args){
        Connection connection = null;
        Scanner scanner = new Scanner(System.in);

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "admin");

            System.out.println("Choose");
            System.out.println("1. Add order");
            System.out.println("2. Update order");
            System.out.println("3. Delete order");
            System.out.println("4. View all orders");
            System.out.println("5. Search orders");
            System.out.println("6. Order statistics");
            System.out.println("Your choice:");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addRecord(connection, scanner);
                    break;
                case 2:
                    updateRecord(connection, scanner);
                    break;
                case 3:
                    deleteRecord(connection, scanner);
                    break;
                case 4:
                    viewAllOrders(connection);
                    break;
                case 5:
                    searchOrders(connection, scanner);
                    break;
                case 6:
                    orderStatistics(connection);
                    break;
                default:
                    System.out.println("Incorrect choice");
            }

            connection.close();
            scanner.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addRecord(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter your name:");
        String name = scanner.nextLine();

        System.out.println("Enter your surname:");
        String surname = scanner.nextLine();

        System.out.println("Enter your email:");
        String email = scanner.nextLine();

        System.out.println("Enter flower name:");
        String flowerName = scanner.nextLine();

        System.out.println("Enter quantity:");
        int quantity = Integer.parseInt(scanner.nextLine());

        System.out.println("Choose arrangement (bouquet/box/pot):");
        String arrangement = scanner.nextLine().toLowerCase();


        if (!arrangement.equals("bouquet") && !arrangement.equals("box") && !arrangement.equals("pot")) {
            System.out.println("Incorrect arrangement. Please choose bouquet, box or pot.");
            return;
        }


        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO flower_shop (name, surname, email, flower_name, quantity, arrangement) VALUES (?, ?, ?, ?, ?, ?)");
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, surname);
        preparedStatement.setString(3, email);
        preparedStatement.setString(4, flowerName);
        preparedStatement.setInt(5, quantity);
        preparedStatement.setString(6, arrangement);

        preparedStatement.executeUpdate();

        System.out.println("Succesfully ordered!");

        preparedStatement.close();
    }

    private static void updateRecord(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter ID of the order to update:");
        int orderId = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter your name:");
        String name = scanner.nextLine();

        System.out.println("Enter your surname:");
        String surname = scanner.nextLine();

        System.out.println("Enter your email:");
        String email = scanner.nextLine();

        System.out.println("Enter flower name:");
        String flowerName = scanner.nextLine();

        System.out.println("Enter quantity:");
        int quantity = Integer.parseInt(scanner.nextLine());

        System.out.println("Choose arrangement (bouquet/box/pot):");
        String arrangement = scanner.nextLine().toLowerCase();


        if (!arrangement.equals("bouquet") && !arrangement.equals("box") && !arrangement.equals("pot")) {
            System.out.println("Incorrect arrangement. Please choose bouquet, box or pot.");
            return;
        }


        PreparedStatement updateStatement = connection.prepareStatement("UPDATE flower_shop SET name = ?, surname = ?, email = ?, flower_name = ?, quantity = ?, arrangement = ? WHERE id = ?");
        updateStatement.setString(1, name);
        updateStatement.setString(2, surname);
        updateStatement.setString(3, email);
        updateStatement.setString(4, flowerName);
        updateStatement.setInt(5, quantity);
        updateStatement.setString(6, arrangement);
        updateStatement.setInt(7, orderId);

        int rowsAffected = updateStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Order updated successfully!");
        } else {
            System.out.println("Order with ID " + orderId + " not found.");
        }

        updateStatement.close();
    }


    private static void deleteRecord(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter ID of the order to delete:");
        int orderId = Integer.parseInt(scanner.nextLine());


        PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM flower_shop WHERE id = ?");
        deleteStatement.setInt(1, orderId);
        int rowsAffected = deleteStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Order deleted successfully!");
        } else {
            System.out.println("Order with ID " + orderId + " not found.");
        }

        deleteStatement.close();
    }

    private static void viewAllOrders(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM flower_shop");

        while (resultSet.next()) {
            System.out.println("ID: " + resultSet.getInt("id"));
            System.out.println("Name: " + resultSet.getString("name"));
            System.out.println("Surname: " + resultSet.getString("surname"));
            System.out.println("Email: " + resultSet.getString("email"));
            System.out.println("Flower Name: " + resultSet.getString("flower_name"));
            System.out.println("Quantity: " + resultSet.getInt("quantity"));
            System.out.println("Arrangement: " + resultSet.getString("arrangement"));
            System.out.println("-----------------------------------");
        }

        resultSet.close();
        statement.close();
    }

    private static void searchOrders(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter customer name or flower name to search:");
        String searchQuery = scanner.nextLine();

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM flower_shop WHERE name LIKE ? OR flower_name LIKE ?");
        preparedStatement.setString(1, "%" + searchQuery + "%");
        preparedStatement.setString(2, "%" + searchQuery + "%");

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println("ID: " + resultSet.getInt("id"));
            System.out.println("Name: " + resultSet.getString("name"));
            System.out.println("Surname: " + resultSet.getString("surname"));
            System.out.println("Email: " + resultSet.getString("email"));
            System.out.println("Flower Name: " + resultSet.getString("flower_name"));
            System.out.println("Quantity: " + resultSet.getInt("quantity"));
            System.out.println("Arrangement: " + resultSet.getString("arrangement"));
            System.out.println("-----------------------------------");
        }

        resultSet.close();
        preparedStatement.close();
    }


    private static void orderStatistics(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT AVG(quantity) AS avg_quantity, MAX(quantity) AS max_quantity FROM flower_shop");

        if (resultSet.next()) {
            System.out.println("Average Quantity: " + resultSet.getDouble("avg_quantity"));
            System.out.println("Maximum Quantity: " + resultSet.getInt("max_quantity"));
        }

        resultSet.close();
        statement.close();
    }
}
