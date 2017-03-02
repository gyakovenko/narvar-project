/**
 *   File Name: trackingPage.java<br>
 *
 *   Yakovenko, Galina<br>
 *   Created: Mar 2, 2017
 *
 */

package narvar;

import org.apache.log4j.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

import com.sqa.gy.helpers.*;

/**
 * trackingPage Locates objects in first tracking section Est. Date of Delivery:
 * date, month, panel-title(status) Locates objects in second tracking section
 * Actions: status, date of last action, last action description Initializes
 * logger
 * 
 * @author Yakovenko, Galina
 *
 */
public class trackingPage {

	public Logger logger;

	private WebDriver driver;

	@FindBy(xpath = "//div[@class='edd-date']/text()")
	private WebElement eddDate;

	@FindBy(xpath = "//div[@class='title panel-title']/text()")
	private WebElement eddDeliveryStatus;

	@FindBy(xpath = "//div[@class='edd-month']/text()")
	private WebElement eddMonth;

	@FindBy(xpath = "//div[@class='description-container’]/span[@class='description’]/text()")
	private WebElement statusActionDescription;

	@FindBy(xpath = "//div[@class=‘tracking-status-container’]/h2/text()")
	private WebElement statusHeading;

	@FindBy(xpath = "//div[@class='timestamp’]/text()")
	private WebElement statusTimestamp;

	/**
	 * Constructs trackingPage instance. Uses BasicPage constructor
	 *
	 * @param driver
	 */
	public trackingPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.logger = Logger.getLogger(BasicPage.class);
	}

	public Logger getLogger() {
		return this.logger;
	}

	private void setLogger(Logger logger) {
		this.logger = logger;
	}

}
