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
 * TrackingPage
 *
 * Locates objects in first tracking section Est. Date of Delivery:date, month,
 * panel-title(status)
 *
 * Locates objects in second tracking section Status: status, date of last
 * action, last action description
 *
 * Initializes logger
 *
 * Methods to retrieve data from the objects where data is converted (trimmed,
 * split, converted to boolean) as needed for comparison in TrackingPageTest
 *
 * @author Yakovenko, Galina
 *
 */
public class TrackingPage {

	/**
	 * Takes the full string for the delivery status text, trims it, and returns
	 * a boolean: true if status = "delivered"
	 *
	 * Called by methods that check status where expected text for delivered is
	 * "delivered"
	 *
	 * @param statusFullText
	 *            String status pulled from page
	 * @return boolean: true if delivered
	 */
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

	@FindBy(xpath = "//div[@class='edd-date']")
	private WebElement eddDate;

	@FindBy(xpath = "//div[@class='title panel-title']")
	private WebElement eddDeliveryStatus;

	@FindBy(xpath = "//div[@class='edd-month']")
	private WebElement eddMonth;

	@FindBy(xpath = "//div[@class='description-container']/span[@class='description']")
	private WebElement statusActionDescription;

	@FindBy(xpath = "//div[@class='tracking-status-container']/h2")
	private WebElement statusHeading;

	@FindBy(xpath = "//div[@class='timestamp']")
	private WebElement statusTimestamp;

	/**
	 * Constructs TrackingPage instance. Uses BasicPage constructor
	 *
	 * Initializes Logger
	 *
	 * @param driver
	 */
	public TrackingPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.logger = Logger.getLogger(TrackingPage.class);
	}

	/**
	 * Gets the full string for the delivery status text, trims it, calls
	 * seeIfEqualsDelivered() and returns a boolean: true if status =
	 * "delivered"
	 *
	 * @return boolean: true if delivered
	 */
	public boolean checkActivityDesc() {
		String statusFullText = this.statusHeading.getText();
		return seeIfEqualsDelivered(statusFullText);
	}

	/**
	 * Gets the full string for the delivery status text, trims it, calls
	 * seeIfEqualsDelivered() and returns a boolean: true if status =
	 * "delivered"
	 *
	 * @return boolean: true if delivered
	 */
	public boolean checkActivityStatus() {
		String statusFullText = this.statusHeading.getText();
		return seeIfEqualsDelivered(statusFullText);
	}

	/**
	 * Gets the full string for the delivery status text, trims it, and returns
	 * a boolean: true if status = "delivery date"
	 *
	 * @return boolean: true if delivered (if text is "delivery date")
	 */
	public boolean checkEddStatus() {
		String statusFullText = this.eddDeliveryStatus.getText();
		String status = statusFullText.trim();
		if (status.equalsIgnoreCase("delivery date")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets the full string for the delivery date month, trims it, and returns
	 * the first three letters
	 *
	 * @return String first three letters of delivery date month
	 */
	public String retrieveAbbEddMo() {
		String monthFullText = this.eddMonth.getText();
		String month = monthFullText.trim();
		String monthAbb = month.substring(0, 3);
		return monthAbb;
	}

	/**
	 * Gets the full string for the last activity date, trims it, splits it and
	 * returns String[] with 2 elements: the first three letters of month and
	 * the date
	 *
	 * @return String[] element 1: first three letter of month, element 2: date
	 *         -- for last activity date
	 */
	public String[] retrieveActivityDate() {
		String dateFullText = this.statusTimestamp.getText();
		String date = dateFullText.trim();
		String[] dateInPieces = date.split(" ");
		String[] dateSubPieces = dateInPieces[1].split("\n");
		dateInPieces[1] = dateSubPieces[0];
		return dateInPieces;
	}

	/**
	 * Gets the full string for the delivery date date, trims it, and returns as
	 * string
	 *
	 * @return String delivery date date
	 */
	public String retrieveEddDate() {
		String dateFullText = this.eddDate.getText();
		String date = dateFullText.trim();
		return date;
	}

}
