/**
 *   File Name: BasicTest.java<br>
 *
 *   Yakovenko, Galina<br>
 *   Created: Mar 2, 2017
 *
 */

package narvar;

import java.util.concurrent.*;

import org.apache.log4j.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.*;
import org.testng.annotations.*;

/**
 * BasicTest to be extended by narvar TrackingPageTest Sets up driver with
 * baseURL before every test Tears down after every test
 *
 * @author Yakovenko, Galina
 */
public class BasicTest {

	private String baseUrl;
	private WebDriver driver;
	private Logger logger;

	/**
	 * Constructs instance of BasicTest and initiates logger
	 *
	 * @param baseUrl
	 *            passed when creating instance for child class
	 */
	public BasicTest(String baseUrl) {
		this.baseUrl = baseUrl;
		this.logger = Logger.getLogger(BasicTest.class);
	}

	public WebDriver getDriver() {
		return this.driver;
	}

	public Logger getLogger() {
		return this.logger;
	}

	/**
	 * sets up Chrome Driver before each test - not currently enabled
	 */
	@BeforeMethod(enabled = false)
	public void setUpChrome() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.get(getBaseUrl());
	}

	/**
	 * sets up Firefox Driver before each test
	 */
	@BeforeMethod // (enabled = false)
	public void setUpFirefox() {
		this.driver = new FirefoxDriver();
		this.driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		this.driver.get(getBaseUrl());
	}

	/**
	 * tears down and closes driver after each test
	 */
	@AfterMethod
	public void tearDown() {
		getDriver().close();
	}

	private String getBaseUrl() {
		return this.baseUrl;
	}

}
