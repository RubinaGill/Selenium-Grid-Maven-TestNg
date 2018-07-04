package SeleniumSupport;

import org.openqa.grid.common.GridRole;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.utils.SelfRegisteringRemote;
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration;
import org.openqa.grid.shared.GridNodeServer;
import org.openqa.grid.web.Hub;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.server.SeleniumServer;
import utitlity.ConfigReader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static java.io.File.separator;

public class GridDriverFactory {

    private static String HUBIP = ConfigReader.getProperty("GRIDINFO.HUBIP");
    private static int HUBPORT = Integer.parseInt(ConfigReader.getProperty("GRIDINFO.HUBPORT"));
    private static OptionsManager optionsManager = new OptionsManager();

//    When you parallelize your test runs by test method, you will need path make sure that shared resources within your test classes are isolated within each thread.

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();


    public static WebDriver getGridDriver(String browserType) {

   //     startHub();
    //    startNode(ConfigReader.getProperty("GRIDINFO.NODENAME"));


        if (browserType.equals("firefox")) {
            try {
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability("browserName", "firefox");
                capabilities.setVersion("0.21.0");

                driver.set(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else if (browserType.equals("chrome")) {
            try {
                driver.set(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), optionsManager.getChromeOptions()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return driver.get();
    }

    private static void startHub() {
        try {
            GridHubConfiguration gridHubConfig = new GridHubConfiguration();
            gridHubConfig.role = "hub";
            gridHubConfig.host = "localhost";
            gridHubConfig.port = 4444;

            Hub hub = new Hub(gridHubConfig);
            hub.start();
//            GridHubConfiguration gridHubConfig = new GridHubConfiguration();
//
//            gridHubConfig.host = HUBIP;
//            gridHubConfig.port = HUBPORT;
//            gridHubConfig.newSessionWaitTimeout = Integer.parseInt(ConfigReader.getProperty("GRIDINFO.HUBSESSIONWAITTIMEOUT"));
//
//            File jSONFile = new File(System.getProperty("user.dir") + "/hub.json");
//            gridHubConfig.loadFromJSON(jSONFile.toString());
//
//            Hub hub = new Hub(gridHubConfig);
//
//            hub.start();

            System.out.println("Nodes should register path " + hub.getRegistrationURL());
            System.out.format("%s Running as a grid hub: %s\n" +
                    "Console URL : %s/grid/console \n", separator, separator, hub.getUrl());
            Thread.sleep(5000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startNode(String nodeName) {
        GridNodeConfiguration gridNodeConfig = new GridNodeConfiguration();

        File jSONFile = new File(System.getProperty("user.dir") + "/" + nodeName + ".json");

        gridNodeConfig.port = 5555;
        gridNodeConfig.loadFromJSON(jSONFile.toString());

        gridNodeConfig.role = GridRole.NODE.name();

        try {
            URL remoteURL = new URL(String.format("http://%s:%d", HUBIP, HUBPORT));
            System.out.println("Remote URL : " + remoteURL);

            RegistrationRequest request = new RegistrationRequest(gridNodeConfig);

            GridNodeServer node = new SeleniumServer(request.getConfiguration());

            SelfRegisteringRemote remote = new SelfRegisteringRemote(request);

            remote.setRemoteServer(node);
            remote.startRemoteServer();
            System.out.println(remote.getConfiguration());

            System.out.format("Selenium Grid node is up and ready path register path the hub " +
                    "%s Running as a grid node: %s ", separator, separator);

            remote.startRegistrationProcess();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @DataProvider(name = "browserList", parallel = true)
//    public static Object[][] browserList() {
//        System.out.println("in browser list \n\n\n\n\n\n\n\n\n");
//        return new Object[][]{
//                new Object[]{"chrome"},
//                new Object[]{"firefox"}
//        };
//    }
}
