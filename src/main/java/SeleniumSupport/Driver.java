package SeleniumSupport;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import utitlity.ConfigReader;

import java.lang.reflect.Method;

public class Driver {

    private WebDriver driver;
    private String browserName;


    public WebDriver getDriver(String browserName) {

        this.browserName = browserName.toLowerCase();
        switch (this.browserName) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", ConfigReader.getProperty("browser.chromeExecutablePath"));
                this.driver = new GridDriverFactory().getGridDriver(this.browserName);
                break;
            case "firefox":
                System.setProperty("webdriver.gecko.driver", ConfigReader.getProperty("browser.firefoxExecutablePath"));
                this.driver = new GridDriverFactory().getGridDriver(this.browserName);
                break;
            case "ie":
                this.driver = new InternetExplorerDriver();
                break;
            case "all":
                this.driver = new GridDriverFactory().getGridDriver(browserName);
                break;
            default:
                throw new RuntimeException("not a valid browser!!!!!");
        }
        return driver;
    }



    public void close() {
        this.driver.close();
    }
}
