package org.example.realestate;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RealEstateCrawlerService {

    private WebDriver driver;

    public RealEstateCrawlerService() {
        WebDriverManager.chromedriver().setup();
        this.driver = new ChromeDriver();
    }

    public void crawlData(String url) {
        try {
            driver.get(url);

            List<WebElement> propertyElements = driver.findElements(By.className("property-item"));

            for (WebElement element : propertyElements) {
                String title = element.findElement(By.className("property-title")).getText();
                String price = element.findElement(By.className("property-price")).getText();
                String location = element.findElement(By.className("property-location")).getText();

                System.out.println("Title: " + title);
                System.out.println("Price: " + price);
                System.out.println("Location: " + location);
                System.out.println("-----------------------------");
            }
        } finally {
            driver.quit();
        }
    }
}