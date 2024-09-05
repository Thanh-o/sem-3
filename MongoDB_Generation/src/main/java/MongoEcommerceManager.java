import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MongoEcommerceManager {
    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("eShop");
        MongoCollection<Document> orderCollection = database.getCollection("OrderCollection");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Select an operation:");
            System.out.println("1. Insert multiple documents");
            System.out.println("2. Update delivery address");
            System.out.println("3. Delete order");
            System.out.println("4. Read all orders");
            System.out.println("5. Calculate total amount");
            System.out.println("6. Count total 'somi' products");
            System.out.println("7. Exit");
            System.out.print("Your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    insertMultipleOrders(orderCollection);
                    break;
                case 2:
                    updateDeliveryAddress(orderCollection, scanner);
                    break;
                case 3:
                    deleteOrder(orderCollection, scanner);
                    break;
                case 4:
                    readAllOrders(orderCollection);
                    break;
                case 5:
                    calculateTotalAmount(orderCollection);
                    break;
                case 6:
                    countSomiProducts(orderCollection);
                    break;
                case 7:
                    mongoClient.close();
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void insertMultipleOrders(MongoCollection<Document> orderCollection) {
        List<Document> orders = new ArrayList<>();

        Document order1 = new Document("orderid", 1)
                .append("products", List.of(
                        new Document("product_id", "quanau")
                                .append("product_name", "quan au")
                                .append("size", "XL")
                                .append("price", 10)
                                .append("quantity", 1),
                        new Document("product_id", "somi")
                                .append("product_name", "ao so mi")
                                .append("size", "XL")
                                .append("price", 11)
                                .append("quantity", 2)
                ))
                .append("total_amount", 32)
                .append("delivery_address", "Hanoi");

        orders.add(order1);


        orderCollection.insertMany(orders);
        System.out.println("Orders inserted successfully.");
    }

    private static void updateDeliveryAddress(MongoCollection<Document> orderCollection, Scanner scanner) {
        System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new delivery address: ");
        String newAddress = scanner.nextLine();
        orderCollection.updateOne(Filters.eq("orderid", orderId), Updates.set("delivery_address", newAddress));
        System.out.println("Delivery address has been updated.");
    }

    private static void deleteOrder(MongoCollection<Document> orderCollection, Scanner scanner) {
        System.out.print("Enter order ID to delete: ");
        int orderId = scanner.nextInt();
        orderCollection.deleteOne(Filters.eq("orderid", orderId));
        System.out.println("Order has been deleted.");
    }

    private static void readAllOrders(MongoCollection<Document> orderCollection) {
        List<Document> allOrders = orderCollection.find().into(new ArrayList<>());
        System.out.println("All orders:");
        for (Document order : allOrders) {
            List<Document> products = (List<Document>) order.get("products");
            for (Document product : products) {
                System.out.printf("Product Name: %s | Price: %d | Quantity: %d | Total: %d\n",
                        product.getString("product_name"), product.getInteger("price"),
                        product.getInteger("quantity"), product.getInteger("price") * product.getInteger("quantity"));
            }
            System.out.println("Delivery Address: " + order.getString("delivery_address"));
            System.out.println();
        }
    }

    private static void calculateTotalAmount(MongoCollection<Document> orderCollection) {
        int totalAmount = orderCollection.find().into(new ArrayList<>())
                .stream().mapToInt(order -> order.getInteger("total_amount")).sum();
        System.out.println("Total amount of all orders: " + totalAmount);
    }

    private static void countSomiProducts(MongoCollection<Document> orderCollection) {
        long totalSomi = orderCollection.find().into(new ArrayList<>())
                .stream().flatMap(order -> ((List<Document>) order.get("products")).stream())
                .filter(product -> "somi".equals(product.getString("product_id")))
                .mapToInt(product -> product.getInteger("quantity")).sum();
        System.out.println("Total 'somi' products sold: " + totalSomi);
    }
}