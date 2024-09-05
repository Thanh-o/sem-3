import com.github.javafaker.Faker;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MongoDataGenerator {
    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("sales_db");
        MongoCollection<Document> saleCollection = database.getCollection("sales");

        Faker faker = new Faker();
        Random random = new Random();
        //san pham mau
        List<Document> productList = List.of(
                new Document("product_id", "PROD001").append("price", 100),
                new Document("product_id", "PROD002").append("price", 150),
                new Document("product_id", "PROD003").append("price", 200),
                new Document("product_id", "PROD004").append("price", 300),
                new Document("product_id", "PROD005").append("price", 350),
                new Document("product_id", "PROD006").append("price", 400),
                new Document("product_id", "PROD007").append("price", 450),
                new Document("product_id", "PROD008").append("price", 500),
                new Document("product_id", "PROD009").append("price", 550),
                new Document("product_id", "PROD0010").append("price", 600)
        );
        //Tao 10.000 ban ghi(ban hang)
        List<Document> saleRecords = new ArrayList<>();
        for (int i = 0; i<10000; i++){
            String customerId = UUID.randomUUID().toString();
            //tao thong tin mua sp
            List<Document> items = new ArrayList<>();
            int numItems = random.nextInt(5) + 1;//bao nhieu sp trong 1 giao dich
            for (int j = 0; j < numItems; j++){
                Document product = productList.get(random.nextInt(productList.size()));
                int quantity = random.nextInt(10) + 1;
                items.add(new Document("product_id",
                        product.getString("product_id"))
                        .append("quantity", quantity)
                        .append("price", product.getInteger("price")));
            }
            //Tin tong tien va giao dich
            int totalAmount = items.stream()
                    .mapToInt(item -> item.getInteger("quantity") * item.getInteger("price"))
                    .sum();
            //Ngay thuc hien giao dich
            String saleDate = faker.date().past(365, TimeUnit.DAYS).toString();
            //Tao ban ghi mau
            Document saleRecord = new Document("date", saleDate)
                    .append("customer_id", customerId)
                    .append("items", items)
                    .append("totalAmount", totalAmount);
            saleRecords.add(saleRecord);
        }
        //chen du lieu vao MongoDb
        saleCollection.insertMany(saleRecords);
        System.out.println("ok");

        mongoClient.close();
    }


}
