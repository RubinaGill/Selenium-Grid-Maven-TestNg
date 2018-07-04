package Tests;

import SeleniumSupport.Driver;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;

/**
 * This class is used for setup and teardown phases
 */
public class AbstractTest {

    WebDriver driver;

    @BeforeMethod
    public void setup( ITestContext context) {
        driver = new Driver().getDriver(context.getCurrentXmlTest().getParameter("Browser"));
    }


    @Test
    public void test1() {
        System.out.println("inside driver................../n/n/n/n/n/n");
        driver.get("http://www.google.com");
    }

    @Test
    public void test2() {
        System.out.println("inside driver 2................../n/n/n/n/n/n");
        driver.get("http://www.google.com");
    }

    @AfterMethod
    public void close() {
        this.driver.close();
    }
}