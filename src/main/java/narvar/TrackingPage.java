/**
 *   File Name: TrackingPage.java<br>
 *
 *   Yakovenko, Galina<br>
 *   Created: Mar 2, 2017
 *
 */

package narvar;

import org.apache.log4j.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

/**
 * TrackingPage Locates objects in first tracking section Est. Date of Delivery:
 * date, month, panel-title(status) Locates objects in second tracking section
 * Actions: status, date of last action, last action description Initializes
 * logger
 *
 * @author Yakovenko, Galina
 *
 */
public class TrackingPage {
	
	@FindBy(xpath = "//div[@class='edd-date']")
	private static WebElement eddDate;

	@FindBy(xpath = "//div[@class='title panel-title']")
	private static WebElement eddDeliveryStatus;

	@FindBy(xpath = "//div[@class='edd-month']")
	private static WebElement eddMonth;

	@FindBy(xpath = "//div[@class='description-container’]/span[@class='description’]")
	private static WebElement statusActionDescription;

	@FindBy(xpath = "//div[@class=‘tracking-status-container’]/h2")
	private static WebElement statusHeading;

	@FindBy(xpath = "//div[@class='timestamp’]")
	private static WebElement statusTimestamp;

	public static boolean checkActivityDesc() {
		String statusFullText = statusHeading.getText();
		return seeIfEqualsDelivered(statusFullText);
	}

	public static boolean checkActivityStatus() {
		String statusFullText = statusHeading.getText();
		return seeIfEqualsDelivered(statusFullText);
	}

	public static boolean checkEddStatus() {
		String statusFullText = eddDeliveryStatus.getText();
		return seeIfEqualsDelivered(statusFullText);
	}

	public static String retrieveAbbEddMo() {
		String monthFullText = eddMonth.getText();
		String month = monthFullText.trim();
		String monthAbb = month.substring(0, 3);
		return monthAbb;
	}

	public static String[] retrieveActivityDate() {
		String dateFullText = statusTimestamp.getText();
		String date = dateFullText.trim();
		String[] dateInPieces = date.split(" ");
		return dateInPieces;
	}

	public static String retrieveEddDate() {
		String dateFullText = eddDate.getText();
		String date = dateFullText.trim();
		return date;
	}

	private static boolean seeIfEqualsDelivered(String statusFullText) {
		String status = statusFullText.trim();
		if (status.equalsIgnoreCase("delivered")) {
			return true;
		} else {
			return false;
		}
	}

	public Logger logger;

	private WebDriver driver;

	/**
	 * Constructs TrackingPage instance. Uses BasicPage constructor
	 *
	 * @param driver
	 */
	public TrackingPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.logger = Logger.getLogger(TrackingPage.class);
	}

}
