/**
 *   File Name: QATrackingPageTest.java<br>
 *
 *   Yakovenko, Galina<br>
 *   Created: Mar 10, 2017
 *
 */

package narvar;

import static org.testng.Assert.*;

import java.util.concurrent.*;

import org.apache.commons.lang3.text.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import helpers.*;

/*-
 * QATrackingPageTest
 * Tests narvar tracking page for following displayed data and functionality:
 * Verify shipping status
 * Verify page title
 * Verify delivery events displayed - not yet implemented
 * Verify tracking number is/isn't clickable - unable to find clickable element for L&G
 * Verify user is/isnt able to submit mobile phone number string for notifications
 * Verify user is/isnt able to update mobile phone number to string
 * Verify user is able to unsubscribe from notifications
 * Verify user is able to submit a feedback rating and verify correct adjective is displayed
 * Verify user is able to update feedback rating on comments view and verify correct adjective is displayed
 * Verify user is able to submit a feedback rating with comments and verify correct adjective is displayed
 *
 * Extends BasicTest
 *
 * @author Yakovenko, Galina
 */

public class QATrackingPageTest extends BasicTest {

	DataType[] emptyDataType = new DataType[0];

	public QATrackingPageTest() {
		super();
	}

	/**
	 * Main data plus mobile notifications and mobile numbers variation
	 *
	 * @return String coName, String baseUrl, String shippingStatus, String
	 *         feedbackSubmitConfirmMessage, Object[] feedbackAdjectives, String
	 *         commentToEnter
	 */
	@DataProvider
	public Object[][] feedbackCommentsData() {
		Object[][] mainData = DataHelper.getTextCSVFileData("QADataForTest/", "MainData.csv", this.emptyDataType);
		Object[][] ratingAdjectives = DataHelper.getTextCSVFileData("QADataForTest/", "RatingAdjectives.csv",
				this.emptyDataType);
		Object[][] feedbackComments = DataHelper.getTextCSVFileData("QADataForTest/", "FeedbackComments.csv",
				this.emptyDataType);
		Object[][] mainAdjectivesFeedbackData = joinDataMainPlusArray(mainData, ratingAdjectives);
		Object[][] mainAdjectivesCommentsData = joinMainArraywithVariations(mainAdjectivesFeedbackData,
				feedbackComments);
		return mainAdjectivesCommentsData;
	}

	/**
	 * Main data plus mobile notifications and mobile numbers variation
	 *
	 * @return String coName, String baseUrl, String shippingStatus, String
	 *         feedbackSubmitConfirmMessage, Object[] feedbackAdjectives, int
	 *         numberOfStars
	 */
	@DataProvider
	public Object[][] feedbackData() {
		Object[][] mainData = DataHelper.getTextCSVFileData("QADataForTest/", "MainData.csv", this.emptyDataType);
		Object[][] ratingAdjectives = DataHelper.getTextCSVFileData("QADataForTest/", "RatingAdjectives.csv",
				this.emptyDataType);
		Object[][] numbersOfStars = DataHelper.getTextCSVFileData("QADataForTest/", "NumberOfStars.csv",
				new DataType[] { DataType.INT, DataType.INT, DataType.INT, DataType.INT, DataType.INT });
		Object[][] mainAdjectivesFeedbackData = joinDataMainPlusArray(mainData, ratingAdjectives);
		Object[][] mainAdjectivesNumbersData = joinMainArraywithVariations(mainAdjectivesFeedbackData, numbersOfStars);
		return mainAdjectivesNumbersData;
	}

	/**
	 * Main data
	 *
	 * @return String coName, String baseUrl, String shippingStatus, String
	 *         feedbackSubmitConfirmMessage
	 */
	@DataProvider
	public Object[][] mainData() {
		Object[][] mainData = DataHelper.getTextCSVFileData("QADataForTest/", "MainData.csv", this.emptyDataType);
		return mainData;
	}

	/**
	 * Main data plus mobile notifications and mobile numbers variation
	 *
	 * @return String coName, String baseUrl, String shippingStatus, String
	 *         feedbackSubmitConfirmMessage, boolean abletoSignUpFromBaseUrl,
	 *         String defaultNombileNumber, String expSubmitMobileNoMessage,
	 *         String expUpdateMobileNoMessage, String
	 *         expUnsubscribeMobileNoMessage, String mobileNumberVariation
	 */
	@DataProvider
	public Object[][] mobileData() {
		Object[][] mainData = DataHelper.getTextCSVFileData("QADataForTest/", "MainData.csv", this.emptyDataType);
		Object[][] mobileNotifMessagesEtc = DataHelper.getTextCSVFileData("QADataForTest/",
				"MobileNotifMessagesEtc.csv", new DataType[] { DataType.BOOLEAN, DataType.STRING, DataType.STRING,
						DataType.STRING, DataType.STRING });
		Object[][] mobileNumbers = DataHelper.getTextCSVFileData("QADataForTest/", "MobileNumbersForNotifications.csv",
				this.emptyDataType);
		Object[][] mainAndMobileNotifMessages = joinDataForMobileMessageOnly(mainData, mobileNotifMessagesEtc);
		Object[][] mainMessagesNumbersMobile = joinMainArraywithVariations(mainAndMobileNotifMessages, mobileNumbers);
		return mainMessagesNumbersMobile;
	}

	/**
	 * Main data plus mobile notifications and mobile numbers variation
	 *
	 * @return String coName, String baseUrl, String shippingStatus, String
	 *         feedbackSubmitConfirmMessage, boolean abletoSignUpFromBaseUrl,
	 *         String defaultNombileNumber, String expSubmitMobileNoMessage,
	 *         String expUpdateMobileNoMessage, String
	 *         expUnsubscribeMobileNoMessage
	 */
	@DataProvider
	public Object[][] mobileDataNoActualNumbers() {
		Object[][] mainData = DataHelper.getTextCSVFileData("QADataForTest/", "MainData.csv", this.emptyDataType);
		Object[][] mobileNotifMessagesEtc = DataHelper.getTextCSVFileData("QADataForTest/",
				"MobileNotifMessagesEtc.csv", new DataType[] { DataType.BOOLEAN, DataType.STRING, DataType.STRING,
						DataType.STRING, DataType.STRING });
		Object[][] mainAndMobileNotifMessages = joinDataForMobileMessageOnly(mainData, mobileNotifMessagesEtc);
		return mainAndMobileNotifMessages;
	}

	/**
	 * Main data plus shipping events as an array
	 *
	 * @return
	 */
	@DataProvider
	public Object[][] shippingData() {
		Object[][] mainData = DataHelper.getTextCSVFileData("QADataForTest/", "MainData.csv", this.emptyDataType);
		Object[][] shippingEvents = DataHelper.getTextCSVFileData("QADataForTest/", "ShippingEvents.csv",
				this.emptyDataType);
		Object[][] shippingData = joinDataMainPlusArray(mainData, shippingEvents);
		return shippingData;
	}

	/**
	 * Click on x number of stars, click on y number of stars on comments page,
	 * submit. Verify adjective matches number of stars on comments page after
	 * update. Verify confirmation message matches expected.
	 *
	 * @param coName
	 *            - DP data not used in test
	 * @param baseUrl
	 *            - passed in through DP
	 * @param shippingStatus
	 *            - DP data not used in test
	 * @param feedbackSubmitConfirmMessage
	 *            - passed in through DP
	 * @param feedbackAdjectives
	 *            - array of adjectives to match number of stars
	 * @param numberOfStars
	 *            - number of stars from 1-5 to select
	 */
	@Test(dataProvider = "feedbackData", groups = "feedback")
	public void testCanSubmitFeedbackChangeRating(String coName, String baseUrl, String shippingStatus,
			String feedbackSubmitConfirmMessage, Object[] feedbackAdjectives, int numberOfStars) {
		int defaultNumberofStars = assignDefaultNumberOfStars(numberOfStars);
		String[] actualAndExpectedStrings = submitFeedbackWithOrWithoutChangeComments(true, defaultNumberofStars,
				feedbackAdjectives, numberOfStars, baseUrl, "");
		assertEquals(actualAndExpectedStrings[0], actualAndExpectedStrings[1],
				"Adjective for feedback does not match expected");
		assertEquals(actualAndExpectedStrings[2], feedbackSubmitConfirmMessage,
				"Confirmation message for feedback submitted does not match expected");
	}

	/**
	 * Click on x number of stars, verify adjective matches number of stars on
	 * comments page after update, click submit, verify confirmation message
	 * matches expected.
	 *
	 * @param coName
	 *            - DP data not used in test
	 * @param baseUrl
	 *            - passed in through DP
	 * @param shippingStatus
	 *            - DP data not used in test
	 * @param feedbackSubmitConfirmMessage
	 *            - passed in through DP
	 * @param feedbackAdjectives
	 *            - array of adjectives to match number of stars
	 * @param numberOfStars
	 *            - number of stars from 1-5 to select
	 */
	@Test(dataProvider = "feedbackData", groups = "feedback")
	public void testCanSubmitFeedbackNoComment(String coName, String baseUrl, String shippingStatus,
			String feedbackSubmitConfirmMessage, Object[] feedbackAdjectives, int numberOfStars) {
		String[] actualAndExpectedStrings = submitFeedbackWithOrWithoutChangeComments(false, 0, feedbackAdjectives,
				numberOfStars, baseUrl, "");
		assertEquals(actualAndExpectedStrings[0], actualAndExpectedStrings[1],
				"Adjective for feedback does not match expected");
		assertEquals(actualAndExpectedStrings[2], feedbackSubmitConfirmMessage,
				"Confirmation message for feedback submitted does not match expected");
	}

	/**
	 * Click on default number of stars, Verify adjective matches number of
	 * stars on comments page after update, Enter comment, submit. Verify
	 * confirmation message matches expected.
	 *
	 * @param coName
	 *            - DP data not used in test
	 * @param baseUrl
	 *            - passed in through DP
	 * @param shippingStatus
	 *            - DP data not used in test
	 * @param feedbackSubmitConfirmMessage
	 *            - passed in through DP
	 * @param feedbackAdjectives
	 *            - array of adjectives to match number of stars
	 * @param commentToEnter
	 *            - String to enter as comment
	 */
	@Test(dataProvider = "feedbackCommentsData", groups = "feedback")
	public void testCanSubmitFeedbackWithComment(String coName, String baseUrl, String shippingStatus,
			String feedbackSubmitConfirmMessage, Object[] feedbackAdjectives, String commentToEnter) {
		int numberOfStars = 5;
		String[] actualAndExpectedStrings = submitFeedbackWithOrWithoutChangeComments(false, 0, feedbackAdjectives,
				numberOfStars, baseUrl, commentToEnter);
		assertEquals(actualAndExpectedStrings[0], actualAndExpectedStrings[1],
				"Adjective for feedback does not match expected");
		assertEquals(actualAndExpectedStrings[2], feedbackSubmitConfirmMessage,
				"Confirmation message for feedback submitted does not match expected");
	}

	/**
	 * Compares displayed delivery status to expected
	 *
	 * @param coName
	 *            - DP data not used in test
	 * @param baseUrl
	 *            - passed in through DP
	 * @param expectedShippingStatus
	 *            - passed in through DP
	 * @param feedbackSubmitConfirmMessage
	 *            - DP data not used in test
	 */
	@Test(dataProvider = "mainData", groups = "basic")
	public void testDeliveryStatus(String coName, String baseUrl, String expectedShippingStatus,
			String feedbackSubmitConfirmMessage) {
		TrackingPage trackingPage = new TrackingPage(getDriver());
		getDriver().get(baseUrl);
		String eddDeliveryStatus = trackingPage.findEddStatus();
		assertEquals(eddDeliveryStatus, expectedShippingStatus, "Delivery status does not match expected");
	}

	/**
	 * Enter mobile number for notifications, click submit, verify message
	 * displayed
	 *
	 * To Implement: vary the expected result and assert based on the mobile
	 * number string (valid or not) but code at beginning of the string.
	 * Currently variations with not enough numerals are failing and too many
	 * numerals are passing without checking what was actually in the field at
	 * submission
	 *
	 * @param coName
	 *            - DP data not used in test
	 * @param baseUrl
	 *            - passed in through DP
	 * @param shippingStatus
	 *            - DP data not used in test
	 * @param feedbackSubmitConfirmMessage
	 *            - DP data not used in test
	 * @param abletoSignUpFromBaseUrl
	 *            - boolean if false, must click on a link to enter mobile
	 *            number
	 * @param defaultNombileNumber
	 *            - String default mobile number string valid input
	 * @param expSubmitMobileNoMessage
	 *            - String expected message after number is submitted
	 * @param expUpdateMobileNoMessage
	 *            - String expected message after number is updated
	 * @param expUnsubscribeMobileNoMessage
	 *            - String expected message after unsubscribe
	 * @param mobileNumberVariation
	 *            - String to input as mobile number some valid, some not
	 */
	@Test(dataProvider = "mobileData", groups = { "mobileNotif", "notFullyWorking", "submit" })
	public void testMobileNotifSubmit(String coName, String baseUrl, String shippingStatus,
			String feedbackSubmitConfirmMessage, boolean abletoSignUpFromBaseUrl, String defaultNombileNumber,
			String expSubmitMobileNoMessage, String expUpdateMobileNoMessage, String expUnsubscribeMobileNoMessage,
			String mobileNumberVariation) {
		String actualMessageAfterSignUp = "";
		String expectedResultCode = mobileNumberVariation.substring(0, 1);
		String numberToPass = mobileNumberVariation.substring(1);
		System.out.println(numberToPass);
		TrackingPage trackingPage = new TrackingPage(getDriver());
		getDriver().get(baseUrl);
		String originalMessageText = signUpForMobileNotif(trackingPage, abletoSignUpFromBaseUrl, numberToPass);
		switch (expectedResultCode) {
		case "e":
			String errorText = trackingPage.mobileNumberError.getText().trim();
			assertEquals(errorText, "Please enter a valid phone number.", "Mobile number error message does not match");
			break;
		default:
			WebDriverWait wait = new WebDriverWait(getDriver(), 15);
			wait.until(ExpectedConditions.invisibilityOfElementWithText(
					(By.xpath("//h4[@class='sms-header new-sms-header']")), originalMessageText));
			actualMessageAfterSignUp = trackingPage.retrieveSMSNotificationConfirmationMessage();
			assertEquals(actualMessageAfterSignUp, WordUtils.capitalize(expSubmitMobileNoMessage),
					"Confirmation message for new mobile number for SMS notifications does not match expected");
			break;
		}
		trackingPage.unsubscribeLink.click();
	}

	/**
	 * Submit mobile number for notifications, click unsubscribe, verify message
	 * displayed
	 *
	 * @param coName
	 *            - DP data not used in test
	 * @param baseUrl
	 *            - passed in through DP
	 * @param shippingStatus
	 *            - DP data not used in test
	 * @param feedbackSubmitConfirmMessage
	 *            - DP data not used in test
	 * @param abletoSignUpFromBaseUrl
	 *            - boolean if false, must click on a link to enter mobile
	 *            number
	 * @param defaultNombileNumber
	 *            - String default mobile number string valid input
	 * @param expSubmitMobileNoMessage
	 *            - String expected message after number is submitted
	 * @param expUpdateMobileNoMessage
	 *            - String expected message after number is updated
	 * @param expUnsubscribeMobileNoMessage
	 *            - String expected message after unsubscribe
	 */
	@Test(dataProvider = "mobileDataNoActualNumbers", groups = { "mobileNotif", "notFullyWorking" })
	public void testMobileNotifUnsubscribe(String coName, String baseUrl, String shippingStatus,
			String feedbackSubmitConfirmMessage, boolean abletoSignUpFromBaseUrl, String defaultNombileNumber,
			String expSubmitMobileNoMessage, String expUpdateMobileNoMessage, String expUnsubscribeMobileNoMessage) {
		String actualMessageAfterUnsubscribe = "";
		String xpathWithExpectedMessage = "//h4[contains(., '" + expUnsubscribeMobileNoMessage + "')]";
		TrackingPage trackingPage = new TrackingPage(getDriver());
		getDriver().get(baseUrl);
		signUpForMobileNotif(trackingPage, abletoSignUpFromBaseUrl, defaultNombileNumber);
		unsubscribeMobileNotif(trackingPage);
		WebDriverWait wait = new WebDriverWait(getDriver(), 15);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathWithExpectedMessage)));
		actualMessageAfterUnsubscribe = getDriver().findElement(By.xpath(xpathWithExpectedMessage)).getText().trim();
		assertEquals(actualMessageAfterUnsubscribe, WordUtils.capitalize(expUnsubscribeMobileNoMessage),
				"Confirmation message for updated mobile number for SMS notifications does not match expected");
	}

	/**
	 * Enters default number for mobile notifications, updates to a mobile
	 * number string, verifies confirmation message for success.
	 *
	 * Currently: unable to locate any text within confirmation message
	 * elements. First message fades out and is replaced by another. Unable to
	 * get text from either.
	 *
	 * To Implement: vary the expected result and assert based on the mobile
	 * number string (valid or not) but code at beginning of the string.
	 *
	 * @param coName
	 *            - DP data not used in test
	 * @param baseUrl
	 *            - passed in through DP
	 * @param shippingStatus
	 *            - DP data not used in test
	 * @param feedbackSubmitConfirmMessage
	 *            - DP data not used in test
	 * @param abletoSignUpFromBaseUrl
	 *            - boolean if false, must click on a link to enter mobile
	 *            number
	 * @param defaultNombileNumber
	 *            - String default mobile number string valid input
	 * @param expSubmitMobileNoMessage
	 *            - String expected message after number is submitted
	 * @param expUpdateMobileNoMessage
	 *            - String expected message after number is updated
	 * @param expUnsubscribeMobileNoMessage
	 *            - String expected message after unsubscribe
	 * @param mobileNumberVariation
	 *            - String to input as mobile number some valid, some not
	 */
	@Test(dataProvider = "mobileData", groups = { "mobileNotif", "notFullyWorking" })
	public void testMobileNotifUpdate(String coName, String baseUrl, String shippingStatus,
			String feedbackSubmitConfirmMessage, boolean abletoSignUpFromBaseUrl, String defaultNombileNumber,
			String expSubmitMobileNoMessage, String expUpdateMobileNoMessage, String expUnsubscribeMobileNoMessage,
			String mobileNumberVariation) {
		String numberToPass = mobileNumberVariation.substring(1);
		String actualMessageAfterUpdate = "";
		String xpathWithExpectedMessage = "//h4[contains(., '" + expUpdateMobileNoMessage + "')]";
		TrackingPage trackingPage = new TrackingPage(getDriver());
		getDriver().get(baseUrl);
		signUpForMobileNotif(trackingPage, abletoSignUpFromBaseUrl, defaultNombileNumber);
		updateMobileNumber(trackingPage, numberToPass);
		WebDriverWait wait = new WebDriverWait(getDriver(), 15);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathWithExpectedMessage)));
		actualMessageAfterUpdate = trackingPage.retrieveSMSNotificationConfirmationMessageAfterUpdate();
		assertEquals(actualMessageAfterUpdate, WordUtils.capitalize(expUpdateMobileNoMessage),
				"Confirmation message for updated mobile number for SMS notifications does not match expected");
		trackingPage.unsubscribeLink.click();
	}

	/**
	 * Uses company name to create expected page title and compares to actual
	 * title
	 *
	 * @param coName
	 *            - used to create expected page title
	 * @param baseUrl
	 *            - passed in through DP
	 * @param shippingStatus
	 *            - DP data not used in test
	 * @param feedbackSubmitConfirmMessage
	 *            - DP data not used in test
	 */
	@Test(dataProvider = "mainData", groups = "basic")

	public void testPageTitle(String coName, String baseUrl, String shippingStatus,
			String feedbackSubmitConfirmMessage) {
		String expectedPageTitle = (coName + ".narvar.com");
		getDriver().get(baseUrl);
		String actualTitle = getDriver().getTitle();
		System.out.println(actualTitle);
		assertEquals(actualTitle, expectedPageTitle, "Title does not match expected");
	}

	@Test(dataProvider = "shippingData", groups = { "notImplemented", "notFullyWorking" })
	public void testShipmentEvents(String coName, String baseUrl, String shippingStatus,
			boolean abletoSignUpFromBaseUrl, String expSubmitMobileNoMessage, String expUpdateMobileNoMessage,
			String expUnsubscribeMobileNoMessage, Object[] shippingEventsData, Object[] mobileNumbers,
			Object[] feedbackAdjectives) {
	}

	/**
	 * Depending on the shipping status, the shipping tracking number should or
	 * should not be clickable. Element will be clickable is the clickable is
	 * visible, non-clickable is not visible or vice versa. Asserts visibility
	 * of these elements.
	 *
	 * for L&G, the clickable element is not found on the page - not sure why
	 *
	 * @param coName
	 *            - DP data not used in test
	 * @param baseUrl
	 *            - baseURL passed in through DP
	 * @param shippingStatus
	 *            - passed in through DP
	 * @param feedbackSubmitConfirmMessage
	 *            - DP data not used in test
	 *
	 */
	@Test(dataProvider = "mainData", groups = { "basic", "notFullyWorking" })
	public void testTrackingNumberClickable(String coName, String baseUrl, String shippingStatus,
			String feedbackSubmitConfirmMessage) {
		boolean[] expectedClickable = findExpectedClickableValues(shippingStatus);
		TrackingPage trackingPage = new TrackingPage(getDriver());
		getDriver().get(baseUrl);
		boolean[] actuallyClickable = trackingPage.isTrackingNumberClickable();
		assertEquals(actuallyClickable, expectedClickable, "Clickability of tracking number does not match expected");
	}

	private int assignDefaultNumberOfStars(int numberOfStars) {
		int defaultNumberOfStars;
		if (numberOfStars == 5) {
			defaultNumberOfStars = 2;
		} else {
			defaultNumberOfStars = 5;
		}
		return defaultNumberOfStars;
	}

	private void clickHoverOnStars(TrackingPage trackingPage, int numOfStars, boolean click) {
		WebElement starToSelect;
		switch (numOfStars) {
		case 1:
			starToSelect = trackingPage.oneStarFeedback;
			break;
		case 2:
			starToSelect = trackingPage.twoStarFeedback;
			break;
		case 3:
			starToSelect = trackingPage.threeStarFeedback;
			break;
		case 4:
			starToSelect = trackingPage.fourStarFeedback;
			break;
		case 5:
			starToSelect = trackingPage.fiveStarFeedback;
			break;
		default:
			starToSelect = trackingPage.oneStarFeedback;
			break;
		}
		if (click) {
			starToSelect.click();
		} else {
			Actions action = new Actions(getDriver());
			action.moveToElement(starToSelect).build().perform();
		}
	}

	private boolean[] findExpectedClickableValues(String shippingStatus) {
		boolean[] expectedClickable = { true, false };
		if (shippingStatus.equalsIgnoreCase("Just Shipped")) {
			expectedClickable[0] = false;
			expectedClickable[1] = true;
		}
		return expectedClickable;
	}

	private Object[][] joinDataForMobileMessageOnly(Object[][] mainData, Object[][] mobileNotifMessagesEtc) {
		Object[][] mainAndMobileNotifMessages = new Object[mainData.length][];
		for (int i = 0; i < mainData.length; i++) {
			mainAndMobileNotifMessages[i] = new Object[mainData[i].length + mobileNotifMessagesEtc[i].length];
			for (int j = 0; j < mainData[i].length; j++) {
				mainAndMobileNotifMessages[i][j] = mainData[i][j];
			}
			for (int k = mainData[i].length; k < mainAndMobileNotifMessages[i].length; k++) {
				for (int j = 0; j < mainData[i].length; j++) {
					mainAndMobileNotifMessages[i][k] = mobileNotifMessagesEtc[i][k - mainData[i].length];
				}
			}
		}
		return mainAndMobileNotifMessages;
	}

	private Object[][] joinDataMainPlusArray(Object[][] mainData, Object[][] secondDataWillBeOneArray) {
		Object[][] joinedData = new Object[mainData.length][];
		for (int i = 0; i < mainData.length; i++) {
			joinedData[i] = new Object[mainData[i].length + 1];
			for (int j = 0; j < mainData[i].length; j++) {
				joinedData[i][j] = mainData[i][j];
			}
			joinedData[i][joinedData[i].length - 1] = secondDataWillBeOneArray[0];
		}
		return joinedData;
	}

	private Object[][] joinMainArraywithVariations(Object[][] baseArray, Object[][] variationsArray) {
		Object[][] combinedArray = new Object[(baseArray.length) * (variationsArray[0].length)][];
		for (int i = 0; i < combinedArray.length; i++) {
			combinedArray[i] = new Object[baseArray[0].length + 1];
		}
		for (int i = 0; i < combinedArray[0].length - 1; i++) {
			for (int j = 0; j < combinedArray.length; j++) {
				for (int k = 0; k < variationsArray[0].length; k++) {
					if (5 * j + k < combinedArray.length) {
						combinedArray[5 * j + k][i] = baseArray[j][i];
						combinedArray[5 * j + k][combinedArray[0].length - 1] = variationsArray[0][k];
					}
				}
			}
		}
		return combinedArray;
	}

	private String retrieveAdjectiveFromCommentsPage(TrackingPage trackingPage) {
		String actualAdjectiveCommentsPage = trackingPage.feedbackAdjective.getText().trim();
		actualAdjectiveCommentsPage = actualAdjectiveCommentsPage.substring(0,
				(actualAdjectiveCommentsPage.length() - 1));
		return actualAdjectiveCommentsPage;
	}

	private String signUpForMobileNotif(TrackingPage trackingPage, boolean abletoSignUpFromBaseUrl,
			String numberToEnter) {
		String originalMessageText;
		if (!abletoSignUpFromBaseUrl) {
			trackingPage.linkForMobileNotifSignUp.click();
		}
		try {
			if (getDriver().findElement(By.xpath("//h4[@class='sms-header new-sms-header']")).isDisplayed()) {
				trackingPage.unsubscribeLink.click();
				trackingPage.signUpAgainLink.click();
			}
		} catch (Exception e) {
		}
		originalMessageText = trackingPage.newSMSMessage.getText();
		trackingPage.inputFieldForMobileNotifSignUp.clear();
		for (int i = 0; i < numberToEnter.length(); i++) {
			trackingPage.inputFieldForMobileNotifSignUp.sendKeys(numberToEnter.substring(i, i + 1));
		}
		trackingPage.buttonToSubmitMobileNumber.click();
		return originalMessageText;

	}

	private String[] submitFeedbackWithOrWithoutChangeComments(boolean changeRating, int defaultNumberofStars,
			Object[] feedbackAdjectives, int numberOfStars, String baseUrl, String commentToEnter) {
		String actualAdjectiveCommentsPage = "";
		String expectedAdjectiveCommentsPage = ((String) feedbackAdjectives[numberOfStars - 1]).toLowerCase();
		String actualMessageAfterFeedback = "";
		TrackingPage trackingPage = new TrackingPage(getDriver());
		getDriver().get(baseUrl);
		if (changeRating) {
			clickHoverOnStars(trackingPage, defaultNumberofStars, true);
			clickHoverOnStars(trackingPage, numberOfStars, false);
		} else {
			clickHoverOnStars(trackingPage, numberOfStars, true);
		}
		actualAdjectiveCommentsPage = retrieveAdjectiveFromCommentsPage(trackingPage);
		if (!commentToEnter.equalsIgnoreCase("")) {
			trackingPage.feedbackCommentField.sendKeys(commentToEnter);
		}
		trackingPage.sendFeedbackButton.click();
		waitForMessageToDisplay();
		actualMessageAfterFeedback = trackingPage.feedbackSubmittedMessage.getAttribute("caption");
		String[] actualAndExpectedStrings = { actualAdjectiveCommentsPage, expectedAdjectiveCommentsPage,
				actualMessageAfterFeedback };
		return actualAndExpectedStrings;
	}

	private void unsubscribeMobileNotif(TrackingPage trackingPage) {
		trackingPage.unsubscribeLink.click();
	}

	private void updateMobileNumber(TrackingPage trackingPage, String numberToEnter) {
		trackingPage.linkToUpdateMobileNumber.click();
		signUpForMobileNotif(trackingPage, true, numberToEnter);
	}

	private void waitForMessageToDisplay() {
		getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(getDriver(), 15);
		wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//feedback-panel-complete[contains(@class,'animate-switch')]")));
	}

}
