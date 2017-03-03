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
 * TrackingPageTest tests shipping tracking page to verify that the Est.
 * Delivery Date section and the Status section display the same delivery status
 * and, if delivered, delivery date
 *
 * @author Yakovenko, Galina
 */
public class TrackingPageTest extends BasicTest {

	private static String baseURL = "http://ship.sephora.com/tracking/sephora/ontrac?dzip=94002&country=US&lang=EN&tracking_numbers=C11504830885106";

	public TrackingPageTest() {
		super(baseURL);
	}

	/**
	 * Tests shipping tracking page to verify that the Est. Delivery Date
	 * section and the Status section display the same delivery status and, if
	 * delivered, delivery date Gets values from the page, then compares and
	 * asserts if values match.
	 */
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

	/**
	 * Takes the param values from site, compares delivery status If delivery
	 * status match as Delievered, also compares delivery dates
	 * 
	 * @param eddStatusDelivered
	 *            boolean delivered status = true/false from the Delivery Date
	 *            section
	 * @param activityStatusDelivered
	 *            boolean delivered status = true/false from the title of Status
	 *            section
	 * @param activityDescIsDelivered
	 *            boolean delivered status = true/false from the most recent
	 *            activity description in Status section
	 * @param eddMonthAbb
	 *            String for delivery date month (first 3 letters) from Delivery
	 *            Date section
	 * @param activityDate
	 *            String[] containing delivery date month (first 3 letters) and
	 *            delivery date date from the most recent activity description
	 *            in Status section
	 * @param eddDate
	 *            String for delivery date date from Delivery Date section
	 * @return boolean[] length 2: (do the delivery statuses match in all three
	 *         places?), (do the delivery dates match?)
	 */
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
