package testNGListeners;

import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to generate xml file for testng
 */
public class SuiteCreator implements IAlterSuiteListener {
    @Override
    public void alter(List<XmlSuite> suites) {

        String browserName;

        //parameters to pass in <test> tags
        HashMap<String, String> parametersOfTest1 = new HashMap<>();
        parametersOfTest1.put("browser", "chrome");

        Map<String, String> parametersOfTest2 = new HashMap<>();
        parametersOfTest2.put("browser", "firefox");

        //getting top level suite
        XmlSuite suite = suites.get(0);
        suite.setName("Dynamic suite");
        suite.setParallel(XmlSuite.ParallelMode.TESTS);

        //package that contains tests
        XmlPackage xmlPackage = new XmlPackage("Tests");

        // get Browser property value from maven commandline otherwise from testng-all.xml
        browserName = System.getProperty("Browser");
        if (browserName == null) {
            browserName = suite.getParameter("Browser");
        }

        browserName = browserName.toLowerCase();

        //creating <test> tag part of xml file
        try {
            switch (browserName) {
                case "all":
                    XmlTest xmlTest = getXmlTest(suite, "chrome", parametersOfTest1);
                    xmlTest.setXmlPackages(Collections.singletonList(xmlPackage));

                    XmlTest xmlTest2 = getXmlTest(suite, "firefox", parametersOfTest2);
                    xmlTest2.setXmlPackages(Collections.singletonList(xmlPackage));
                    break;
                case "chrome":
                    XmlTest chromeXmlTest = getXmlTest(suite, "chrome", parametersOfTest1);
                    chromeXmlTest.setXmlPackages(Collections.singletonList(xmlPackage));
                    break;
                case "firefox":
                    XmlTest firefoxXmlTest = getXmlTest(suite, "firefox", parametersOfTest2);
                    firefoxXmlTest.setXmlPackages(Collections.singletonList(xmlPackage));
                    break;
                default:
                    throw new RuntimeException(browserName+" browser is not yet supported!");
            }
        } catch (NullPointerException e) {
            throw new RuntimeException("browser is not passed as a parameter!");
        }
    }

    /**
     * @param suite       object of master suite
     * @param browserName browser name to be passed as parameter value
     * @param parameters  Map object for all parameters
     * @return <test> tag object
     */
    private XmlTest getXmlTest(XmlSuite suite, String browserName, Map<String, String> parameters) {
        XmlTest xmlTest = new XmlTest(suite);
        xmlTest.setName(browserName + " Tests");
        xmlTest.setParameters(parameters);
        return xmlTest;
    }

}
