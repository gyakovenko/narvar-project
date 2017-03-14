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
 * split, converted to boolean) as needed for comparison in
 * SephoraProdTrackingPageTest
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

	@FindBy(xpath = "//div[contains(@class, 'tracking-status-container')]/h2")
	private WebElement eddDeliveryStatus;

	@FindBy(xpath = "//div[@class='title panel-title']")
	private WebElement eddDeliveryStatusSectionTitle;

	@FindBy(xpath = "//div[@class='edd-month']")
	private WebElement eddMonth;

	@FindBy(xpath = "//a[(@class='sms-redirect-link') and (text()=' Unsubscribe ')]")
	private WebElement linkToUnsubscribe;

	@FindBy(xpath = "//div[@class='description-container']/span[@class='description']")
	private WebElement statusActionDescription;

	@FindBy(xpath = "//div[@class='tracking-status-container']/h2")
	private WebElement statusHeading;

	@FindBy(xpath = "//div[@class='timestamp']")
	private WebElement statusTimestamp;

	@FindBy(xpath = "//a[@class='tracking-number']")
	private WebElement trackingNumberAfterJustShipped;

	@FindBy(xpath = "//div[@class='tracking-number']")
	private WebElement trackingNumberWhenJustShipped;

	@FindBy(xpath = "//button[contains(@class,'sms-widget-btn')]")
	WebElement buttonToSubmitMobileNumber;

	@FindBy(xpath = "//div[@id='feedback-comment-header']/span")
	WebElement feedbackAdjective;

	@FindBy(id = "feedback-comment-input")
	WebElement feedbackCommentField;

	@FindBy(xpath = "//feedback-panel-complete[contains(@class,'animate-switch')]")
	WebElement feedbackSubmittedMessage;

	@FindBy(xpath = "//ul[@id='feedback-survey-stars']/li[5]/i")
	WebElement fiveStarFeedback;

	@FindBy(xpath = "//ul[@id='feedback-survey-stars']/li[4]/i")
	WebElement fourStarFeedback;

	@FindBy(xpath = "//input[contains(@class, 'mobileNumber')]")
	WebElement inputFieldForMobileNotifSignUp;

	@FindBy(xpath = "//a[@class='sms-signup-link']")
	WebElement linkForMobileNotifSignUp;

	@FindBy(xpath = "//a[(@class='sms-redirect-link') and (text()=' Update Your Number ')]")
	WebElement linkToUpdateMobileNumber;

	@FindBy(xpath = "//div[@class='t2-tooltip error-tooltip']/div[contains(.,' Please enter a valid phone number. ')]")
	WebElement mobileNumberError;

	@FindBy(xpath = "//h4[@class='sms-header new-sms-header']")
	WebElement newSMSMessage;

	@FindBy(xpath = "//ul[@id='feedback-survey-stars']/li/i")
	WebElement oneStarFeedback;

	@FindBy(id = "send-feedback-btn")
	WebElement sendFeedbackButton;

	@FindBy(xpath = "//div[@class='sms-update-links unsubscribe']/a[@class ='sms-redirect-link']")
	WebElement signUpAgainLink;

	@FindBy(xpath = "//ul[@id='feedback-survey-stars']/li[3]/i")
	WebElement threeStarFeedback;

	@FindBy(xpath = "//ul[@id='feedback-survey-stars']/li[2]/i")
	WebElement twoStarFeedback;

	@FindBy(xpath = "//a[(@class='sms-redirect-link') and (text()=' Unsubscribe ')]")
	WebElement unsubscribeLink;

	@FindBy(xpath = "//h4[contains (.,'You are now signed up for text messages about your delivery.')]")
	WebElement updateSMSMessage;

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
		String statusFullText = this.eddDeliveryStatusSectionTitle.getText();
		String status = statusFullText.trim();
		if (status.equalsIgnoreCase("delivery date")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets the full string for the delivery status text, trims it and returns
	 * it
	 *
	 * @return String text of Delivery Status
	 */
	public String findEddStatus() {
		String statusFullText = this.eddDeliveryStatus.getText();
		return statusFullText.trim();
	}

	/**
	 *
	 * @return
	 */
	public boolean[] isTrackingNumberClickable() {
		boolean clickableNumberPresent;
		try {
			clickableNumberPresent = this.trackingNumberAfterJustShipped.isDisplayed();
		} catch (Exception NoSuchElementException) {
			clickableNumberPresent = false;
		}
		boolean notClickableNumberPresent;
		try {
			notClickableNumberPresent = this.trackingNumberWhenJustShipped.isDisplayed();
		} catch (Exception NoSuchElementException) {
			notClickableNumberPresent = false;
		}
		boolean[] trackingNumberBooleans = { clickableNumberPresent, notClickableNumberPresent };
		return trackingNumberBooleans;
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

	/**
	 * @return
	 */
	public String retrieveSMSNotificationConfirmationMessage() {
		String message = this.newSMSMessage.getText().trim();
		return message;
	}

	/**
	 * @return
	 */
	public String retrieveSMSNotificationConfirmationMessageAfterUpdate() {
		String message = this.updateSMSMessage.getText().trim();
		return message;
	}

}
