package demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases {
    ChromeDriver driver;

    /*
     * TODO: Write your tests here with testng @Test annotation.
     * Follow `testCase01` `testCase02`... format or what is provided in
     * instructions
     */

    /*
     * Do not change the provided methods unless necessary, they will help in
     * automation and assessment
     */
    @BeforeTest
    public void startBrowser() {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
    }

    @Test
    public void testCase01() throws JsonProcessingException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        List<Map<String, Object>> teamDataList = new ArrayList<>();
        Wrappers homePage = new Wrappers(driver);
        homePage.navigateToXScraper();
        WebElement HockeyPageLink = driver.findElement(By.xpath("//a[contains(text(),'Hockey Teams')]"));
        homePage.clickOn(HockeyPageLink);
        int col = homePage.collumnNumber("Team Name");
        int col1 = homePage.collumnNumber("Year");
        int col2 = homePage.collumnNumber("Win %");
        for (int i = 1; i <= 4; i++) {
            long epochTime = System.currentTimeMillis() / 1000L;

            ArrayList<Integer> numbList = homePage.winPercent(0.40);
            for (int nums : numbList) {
                String teamName = driver.findElement(By.xpath("//tbody/tr[" + nums + "]/td["
                        + col + "]")).getText();
                String year = driver.findElement(By.xpath("//tbody/tr[" + nums + "]/td[" +
                        col1 + "]")).getText();
                String winPercentageStr = driver.findElement(By.xpath("//tbody/tr[" + nums +
                        "]/td[" + col2 + "]"))
                        .getText();
                String st = winPercentageStr.trim();
                double winPercentage = Double.parseDouble(st);
                Map<String, Object> teamData = new HashMap<>();
                teamData.put("Epoch Time", epochTime);
                teamData.put("Team Name", teamName);
                teamData.put("Year", year);
                teamData.put("Win %", winPercentageStr);
                teamDataList.add(teamData);
            }
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@class='pagination']/li/a")));
            WebElement nextPage = driver.findElement(By.xpath("//ul[@class='pagination']/li/a"));
            homePage.clickOn(nextPage);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("teamDataList", teamDataList);
        try {
            String employeePrettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(inputMap);
            System.out.println(employeePrettyJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String userDir = System.getProperty("user.dir");

        // Writing JSON on a file
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(userDir + "\\src\\test\\resources\\JSONFromMap.json"),
                            inputMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCase02() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        ArrayList<Map<String, Object>> OscarList = new ArrayList<>();
        Wrappers homePage = new Wrappers(driver);
        homePage.navigateToXScraper();
        wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(),'Oscar Winning Films')]")));
        WebElement OscarLink = driver.findElement(By.xpath("//a[contains(text(),'Oscar Winning Films')]"));
        System.out.println("Oscar Winning Films");
        homePage.clickOn(OscarLink);
        List<WebElement> yearList = driver.findElements(By.xpath("//div[@class='col-md-12 text-center']/a"));
        for (WebElement years : yearList) {
            homePage.clickOn(years);
            Thread.sleep(10000);
            for (int i = 1; i <= 5; i++) {
                Map<String, Object> hm = homePage.addData(i);
                OscarList.add(hm);
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("OscarList", OscarList);
        String employeePrettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(inputMap);
        System.out.println(employeePrettyJson);
        String userDir = System.getProperty("user.dir");
        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(userDir + "\\src\\test\\resources\\oscar-winner-data.json"),
                        inputMap);

    }

    @AfterTest
    public void endTest() {
        driver.close();
        driver.quit();

    }
}