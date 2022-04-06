package accessibility;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import com.deque.html.axecore.selenium.AxeBuilderOptions;
import com.deque.html.axecore.selenium.AxeReporter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.List;

public class AccessibilityTester {
    WebDriver driver;

    private static final URL scriptURL = AccessibilityTester.class.getResource("/axe.min.js");
    @BeforeMethod
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("http://www.amazon.com");
    }
    @Test
    public void testMyHost() throws FileNotFoundException {
        AxeBuilder axeBuilder = new AxeBuilder();
        Results results = axeBuilder.analyze(driver);
        List<Rule> violations = results.getViolations();
        if (violations.size() == 0)
        {
            Assert.assertTrue(true, "No violations found");
        }
        else
        {
            JsonParser jsonParser = new JsonParser();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String reportFile = "";
            AxeReporter.writeResultsToJsonFile(reportFile, results);
            JsonElement jsonElement = jsonParser.parse(new FileReader(reportFile + ".json"));
            String prettyJson = gson.toJson(jsonElement);
            AxeReporter.writeResultsToTextFile(reportFile, prettyJson);
            Assert.assertEquals(violations.size(), 0, violations.size() + " violations found");
        }
    }
    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
