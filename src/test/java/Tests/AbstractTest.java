package Tests;

import SeleniumSupport.Driver;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AbstractTest {
    WebDriver driver;

    @BeforeMethod
    public void setup() {
        String browserName = getParameter("browser");
        driver = new Driver().getDriver(browserName);
    }

    private String getParameter(String name) {
        String value = System.getProperty(name);
        System.out.println("the value is " + value + " this");

        if (value == null)
            throw new RuntimeException(name + " is not passed as a parameter!");

        if (value.isEmpty())
            throw new RuntimeException(name + " value is empty!");
        return value;
    }

    @Test
    public void test1() {
        System.out.println("inside driver................../n/n/n/n/n/n");
        driver.get("http://www.google.com");
    }

}