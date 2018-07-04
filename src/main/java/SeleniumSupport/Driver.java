package SeleniumSupport;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import utitlity.ConfigReader;

public class Driver {

    private WebDriver driver;
    private String browserName;


    public WebDriver getDriver(String browserName) {

        this.browserName = browserName;
        switch (this.browserName) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", ConfigReader.getProperty("browser.chromeExecutablePath"));
                this.driver = new ChromeDriver();
                break;
            case "firefox":
                System.setProperty("webdriver.gecko.driver", ConfigReader.getProperty("browser.firefoxExecutablePath"));
                this.driver = new FirefoxDriver();
                break;
            case "ie":
                throw new RuntimeException("internet explorer is not supported yet!!!!!");
            default:
                throw new RuntimeException("not a valid browser!!!!!");
        }
        return driver;
    }
}