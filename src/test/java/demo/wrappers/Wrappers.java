package demo.wrappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.checkerframework.checker.units.qual.A;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */
    WebDriver driver;

    public Wrappers(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateToXScraper() {
        driver.get("https://www.scrapethissite.com/pages/");
    }

    public void clickOn(WebElement element) {
        element.click();
    }

    public int collumnNumber(String text) {
        int num = 0;
        List<WebElement> headingList = driver.findElements(By.xpath("//tbody/tr/th"));
        for (WebElement element : headingList) {
            String text1 = element.getText();
            if (text1.contains(text)) {
                num++;
                return num;
            }
            num++;
        }

        return num;
    }

    public ArrayList<Integer> winPercent(double num) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        ArrayList<Integer> rowList = new ArrayList<>();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody/tr/td[6]")));
        List<WebElement> elements = driver.findElements(By.xpath("//tbody/tr/td[6]"));
        int ind = 2;
        for (WebElement element : elements) {
            String percentText = element.getText();
            String percentTrim = percentText.trim();
            double num1 = Double.parseDouble(percentTrim);
            if (num1 < num) {
                rowList.add(ind);

            }
            ind++;
        }
        return rowList;
    }

    public Map<String, Object> addData(int row) throws Exception {
        Map<String, Object> hm1Map = new HashMap<>();
        long epochTime = System.currentTimeMillis() / 1000L;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//tbody/tr/td")));
        WebElement titleElement = driver.findElement(By.xpath("//tbody/tr[" + row + "]/td[1]"));
        WebElement nominationElement = driver.findElement(By.xpath("//tbody/tr[" + row + "]/td[2]"));
        WebElement AwardElement = driver.findElement(By.xpath("//tbody/tr[" + row + "]/td[3]"));
        String titleString = titleElement.getText();
        String nominationString = nominationElement.getText();
        String AwardString = AwardElement.getText();
        hm1Map.put("epoch Time", epochTime);
        hm1Map.put("Title", titleString);
        hm1Map.put("Nominations", nominationString);
        hm1Map.put("Awards", AwardString);
        if (row == 1) {
            hm1Map.put("Best Picture", "True");
        } else {
            hm1Map.put("Best Picture", "False");
        }
        return hm1Map;
    }

}
