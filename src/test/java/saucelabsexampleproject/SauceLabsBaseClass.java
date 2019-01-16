package saucelabsexampleproject;

import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.SauceOnDemandTestWatcher;
import com.saucelabs.junit.ConcurrentParameterized;

/**
 * The Example Sauce Labs Base Class
 * 
 * Extend this class and implement test methods to define tests. You shouldn't need to 
 * amend this class ordinarily
 * @author ric
 *
 */
@Ignore
@RunWith(ConcurrentParameterized.class)
public class SauceLabsBaseClass implements SauceOnDemandSessionIdProvider {
	
	/**
	 * The project name gets appended to the test name for easy viewing in SauceLabs
	 */
	public static String projectName = "Sauce Labs Example Project";
	
	/**
	 * username,password and the base url of the application should NOT be in the source code
	 * these should be passed into the process
	 * 
	 * the build tag will be set by Jenkins automatically, and can be passed in manually
	 * when running locally
	 */
	private static final String username = System.getProperty("SAUCE_USERNAME");
	private static final String accesskey = System.getProperty("SAUCE_ACCESS_KEY");
	private static final String appBaseURL = System.getProperty("APP_BASE_URL");
	private static final String buildTag = System.getProperty("BUILD_TAG");
    
	private static final String seleniumURI = "@ondemand.saucelabs.com:443";
    
    private String browser;
    private String os;
    private String version;
    private String deviceName;
    private String deviceOrientation;
    private WebDriver driver;
    private String sessionId;
    
    /**
     * Constructs a new instance of the test.  The constructor requires three string parameters, which represent the operating
     * system, version and browser to be used when launching a Sauce VM.  The order of the parameters should be the same
     * as that of the elements within the {@link #browsersStrings()} method.
     * @param os
     * @param version
     * @param browser
     * @param deviceName
     * @param deviceOrientation
     */
    public SauceLabsBaseClass(String os, String version, String browser, String deviceName, String deviceOrientation) {
        super();
        this.os = os;
        this.version = version;
        this.browser = browser;
        this.deviceName = deviceName;
        this.deviceOrientation = deviceOrientation;
    }
    
    /**
     * Constructs a {@link SauceOnDemandAuthentication} instance using the supplied user name/access key.  To use the authentication
     * supplied by environment variables or from an external file, use the no-arg {@link SauceOnDemandAuthentication} constructor.
     */
    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(username, accesskey);

    /**
     * JUnit Rule which will mark the Sauce Job as passed/failed when the test succeeds or fails.
     */
    @Rule
    public SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher(this, authentication);

    
    @Rule
    public TestName name = new TestName() {
        public String getMethodName() {
            return String.format("%s", projectName + " - " + buildTag + " - " + os + " - " + browser + " - v" + version + " - " +super.getMethodName());
        }
    };

    /**
     * @return a LinkedList containing String arrays representing the browser combinations the test should be run against. The values
     * in the String array are used as part of the invocation of the test constructor
     */
    @ConcurrentParameterized.Parameters
    public static LinkedList<String[]> browsersStrings() {
        LinkedList<String[]> browsers = new LinkedList<String[]>();
        
        // order of the string array matches the constructor - ie :
        // browsers.add(new String[]{"OS", "Browser Version", "Browser", "DeviceName", "Device Orientation"});
        // you can find the correct strings for the OS/Browser combinations at the following page :
        // https://wiki.saucelabs.com/display/DOCS/Platform+Configurator#/
        
        browsers.add(new String[]{"Windows 10", "14.14393", "MicrosoftEdge", null, null});
        browsers.add(new String[]{"Windows 10", "49.0", "firefox", null, null});
        browsers.add(new String[]{"Windows 7", "11.0", "internet explorer", null, null});
        browsers.add(new String[]{"OS X 10.11", "10.0", "safari", null, null});
        browsers.add(new String[]{"OS X 10.10", "54.0", "chrome", null, null});
        
        return browsers;
    }

    /**
     * Constructs a new {@link RemoteWebDriver} instance which is configured to use the capabilities defined by the {@link #browser},
     * {@link #version} and {@link #os} instance variables, and which is configured to run against ondemand.saucelabs.com, using
     * the username and access key populated by the {@link #authentication} instance.
     *
     * @throws Exception if an error occurs during the creation of the {@link RemoteWebDriver} instance.
     */
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
        capabilities.setCapability(CapabilityType.VERSION, version);
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("device-orientation", deviceOrientation);
        capabilities.setCapability(CapabilityType.PLATFORM, os);
        capabilities.setCapability("name", name.getMethodName());

        // Append the build tag as long as it has been set either by Jenkins or manually
        if (buildTag != null) {
            capabilities.setCapability("build", buildTag);
        }
        
        this.driver = new RemoteWebDriver(
                new URL("https://" + username+ ":" + accesskey + seleniumURI +"/wd/hub"),
                capabilities);

        this.sessionId = (((RemoteWebDriver) driver).getSessionId()).toString();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    /**
     * @return the value of the Sauce Job id.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @return the Selenium WebDriver to execute tests
     */
	protected WebDriver getDriver() {
		return driver;
	}

	/**
	 * @return the application Base URL to execute tests
	 */
	protected static String getAppbaseurl() {
		return appBaseURL;
	}

}
