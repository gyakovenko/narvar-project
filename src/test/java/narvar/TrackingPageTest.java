/**
 *   File Name: TrackingPageTest.java<br>
 *
 *   Yakovenko, Galina<br>
 *   Created: Mar 2, 2017
 *
 */

package narvar;

import static org.testng.Assert.*;

import org.testng.annotations.*;

/**
 * TrackingPageTest Tests page to verify that the Est. Delivery Date section and
 * the Actions and Status section display the same delivery status and delivery
 * date
 *
 * @author Yakovenko, Galina
 */
public class TrackingPageTest extends BasicTest {

	private static String baseURL = "http://ship.sephora.com/tracking/sephora/ontrac?dzip=94002&country=US&lang=EN&tracking_numbers=C11504830885106";

	public TrackingPageTest() {
		super(baseURL);
	}

	@Test
	public void testDeliveryStatusAndDatesMatch() {
		TrackingPage trackingPage = new TrackingPage(getDriver());
		// find values for all elements to be compared
		getLogger().info("GETTING VALUES FROM PAGE: ");
		boolean eddStatusDelivered = trackingPage.checkEddStatus();
		getLogger().info("eddStatusDelivered = " + eddStatusDelivered);

		String eddMonthAbb = trackingPage.retrieveAbbEddMo();
		getLogger().info("eddMonthAbb = " + eddMonthAbb);

		String eddDate = trackingPage.retrieveEddDate();
		getLogger().info("eddDate = " + eddDate);

		boolean activityStatusDelivered = trackingPage.checkActivityStatus();
		getLogger().info("activityStatusDelivered = " + activityStatusDelivered);

		String[] activityDate = trackingPage.retrieveActivityDate();
		getLogger().info("activityDate = " + activityDate[0] + " " + activityDate[1]);

		boolean activityDescIsDelivered = trackingPage.checkActivityDesc();
		getLogger().info("activityDescIsDelivered = " + activityDescIsDelivered);

		// compare the values
		getLogger().info("COMPARING THE VALUES: ");
		boolean[] matches = compareStatusAndDates(eddStatusDelivered, activityStatusDelivered, activityDescIsDelivered,
				eddMonthAbb, activityDate, eddDate);

		// Assert: if not yet delivered, just status. If delivered then also
		// date
		if (eddStatusDelivered != true) {
			assertEquals(matches[0], true);
		} else {
			boolean[] expected = { true, true };
			assertEquals(matches, expected);
		}
	}

	private boolean[] compareStatusAndDates(boolean eddStatusDelivered, boolean activityStatusDelivered,
			boolean activityDescIsDelivered, String eddMonthAbb, String[] activityDate, String eddDate) {
		boolean[] matches = { false, false };
		if (eddStatusDelivered == activityStatusDelivered && activityStatusDelivered == activityDescIsDelivered) {
			matches[0] = true;
			getLogger().info("Delivery status matches for all three fields in 2 sections");
			if (eddStatusDelivered == true) {
				if (eddMonthAbb.equalsIgnoreCase(activityDate[0]) && eddDate.equalsIgnoreCase(activityDate[1])) {
					matches[1] = true;
					getLogger().info("Delivery date matches for 2 sections");
				} else {
					getLogger().info("Delivery date does not match for 2 sections");
				}
			}
		} else {
			getLogger().info("Delivery status does not match for all three fields");
		}
		return matches;
	}

}
