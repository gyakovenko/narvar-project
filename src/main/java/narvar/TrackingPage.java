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
	 * @param driver
	 */
	public TrackingPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.logger = Logger.getLogger(TrackingPage.class);
	}

	public boolean checkActivityDesc() {
		String statusFullText = this.statusHeading.getText();
		return seeIfEqualsDelivered(statusFullText);
	}

	public boolean checkActivityStatus() {
		String statusFullText = this.statusHeading.getText();
		return seeIfEqualsDelivered(statusFullText);
	}

	public boolean checkEddStatus() {
		String statusFullText = this.eddDeliveryStatus.getText();
		String status = statusFullText.trim();
		if (status.equalsIgnoreCase("delivery date")) {
			return true;
		} else {
			return false;
		}
	}

	public String retrieveAbbEddMo() {
		String monthFullText = this.eddMonth.getText();
		String month = monthFullText.trim();
		String monthAbb = month.substring(0, 3);
		return monthAbb;
	}

	public String[] retrieveActivityDate() {
		String dateFullText = this.statusTimestamp.getText();
		String date = dateFullText.trim();
		String[] dateInPieces = date.split(" ");
		String[] dateSubPieces = dateInPieces[1].split("\n");
		dateInPieces[1] = dateSubPieces[0];
		return dateInPieces;
	}

	public String retrieveEddDate() {
		String dateFullText = this.eddDate.getText();
		String date = dateFullText.trim();
		return date;
	}

}
