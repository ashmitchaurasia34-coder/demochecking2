import java.sql.*;
import java.util.Scanner;

public class InventoryManagement {

    public static void main(String[] args) {

        try {

            // DATABASE CONNECTION

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/retail_db",
                    "root",
                    "MYSQL@1234"
            );

            System.out.println("Database Connected Successfully!");

            Scanner sc = new Scanner(System.in);

            int choice;

            do {

                System.out.println("\n===== INVENTORY MANAGEMENT SYSTEM =====");

                System.out.println("1. Insert Products");
                System.out.println("2. Retrieve Products");
                System.out.println("3. Update Product Stock");
                System.out.println("4. Search and Modify Product Price");
                System.out.println("5. Calculate Inventory Value and Delete");
                System.out.println("6. Exit");

                System.out.print("Enter your choice: ");

                choice = sc.nextInt();

                switch (choice) {

                    // PART A: INSERTION

                    case 1: {

                        String sql = "INSERT INTO Product VALUES (?, ?, ?, ?, ?)";

                        PreparedStatement ps = con.prepareStatement(sql);

                        for (int i = 1; i <= 3; i++) {

                            System.out.println("Enter Product ID:");
                            int id = sc.nextInt();
                            sc.nextLine();

                            System.out.println("Enter Product Name:");
                            String name = sc.nextLine();

                            System.out.println("Enter Category:");
                            String category = sc.nextLine();

                            System.out.println("Enter Quantity:");
                            int quantity = sc.nextInt();

                            System.out.println("Enter Price:");
                            double price = sc.nextDouble();

                            ps.setInt(1, id);
                            ps.setString(2, name);
                            ps.setString(3, category);
                            ps.setInt(4, quantity);
                            ps.setDouble(5, price);

                            int rows = ps.executeUpdate();

                            System.out.println(rows + " record inserted!");

                        }

                        ps.close();
                        break;
                    }


                    // PART B: RETRIEVAL

                    case 2: {

                        System.out.println("Enter Category:");
                        sc.nextLine();
                        String category = sc.nextLine();

                        String sql = "SELECT * FROM Product WHERE category = ? AND quantity < 20 ORDER BY price DESC";

                        PreparedStatement ps = con.prepareStatement(sql);

                        ps.setString(1, category);

                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {

                            System.out.println("Product ID: " + rs.getInt("product_id"));
                            System.out.println("Product Name: " + rs.getString("product_name"));
                            System.out.println("Category: " + rs.getString("category"));
                            System.out.println("Quantity: " + rs.getInt("quantity"));
                            System.out.println("Price: " + rs.getDouble("price"));

                            System.out.println("----------------------");

                        }

                        rs.close();
                        ps.close();
                        break;
                    }


                    // PART C: UPDATE STOCK

                    case 3: {

                        System.out.print("Enter Product ID:");
                        int id = sc.nextInt();

                        System.out.print("Enter Quantity to Add:");
                        int qty = sc.nextInt();

                        String sql = "UPDATE Product SET quantity = quantity + ? WHERE product_id = ?";

                        PreparedStatement ps = con.prepareStatement(sql);

                        ps.setInt(1, qty);
                        ps.setInt(2, id);

                        int rows = ps.executeUpdate();

                        if (rows > 0) {
                            System.out.println("Stock Updated Successfully!");
                        } else {
                            System.out.println("Product Not Found!");
                        }

                        ps.close();
                        break;
                    }


                    // PART D: SEARCH AND MODIFY PRICE

                    case 4: {

                        System.out.print("Enter Product ID:");
                        int id = sc.nextInt();

                        System.out.print("Enter New Price:");
                        double newPrice = sc.nextDouble();

                        String sql = "SELECT * FROM Product WHERE product_id = ?";

                        PreparedStatement ps = con.prepareStatement(sql);

                        ps.setInt(1, id);

                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {

                            double oldPrice = rs.getDouble("price");

                            System.out.println("Old Price: " + oldPrice);

                            if (newPrice > oldPrice) {

                                String update = "UPDATE Product SET price = ? WHERE product_id = ?";

                                PreparedStatement ps2 = con.prepareStatement(update);

                                ps2.setDouble(1, newPrice);
                                ps2.setInt(2, id);

                                ps2.executeUpdate();

                                System.out.println("Price Updated Successfully!");

                                ps2.close();

                            } else {

                                System.out.println("New Price Must Be Greater Than Old Price!");

                            }

                        } else {

                            System.out.println("Product Not Found!");

                        }

                        rs.close();
                        ps.close();
                        break;
                    }


                    // PART E: AGGREGATE AND DELETE

                    case 5: {

                        String sql = "SELECT SUM(quantity * price) AS total FROM Product";

                        PreparedStatement ps = con.prepareStatement(sql);

                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {

                            double total = rs.getDouble("total");

                            System.out.println("Total Inventory Value: " + total);

                        }

                        rs.close();
                        ps.close();


                        System.out.print("Enter Product ID to delete:");
                        int id = sc.nextInt();

                        String delete = "DELETE FROM Product WHERE product_id = ? AND quantity = 0";

                        PreparedStatement ps2 = con.prepareStatement(delete);

                        ps2.setInt(1, id);

                        int rows = ps2.executeUpdate();

                        if (rows > 0) {
                            System.out.println("Product Deleted Successfully!");
                        } else {
                            System.out.println("Product Cannot Be Deleted!");
                        }

                        ps2.close();
                        break;
                    }


                    case 6:

                        System.out.println("Exiting Inventory Management System...");
                        break;


                    default:

                        System.out.println("Invalid Choice!");

                }

            } while (choice != 6);

            con.close();
            sc.close();

        } catch (SQLException e) {

            System.out.println("Database Error!");
            System.out.println(e.getMessage());

        }

    }
}