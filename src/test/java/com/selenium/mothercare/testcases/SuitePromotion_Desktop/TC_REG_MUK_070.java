package com.selenium.mothercare.testcases.SuitePromotion_Desktop;

import java.util.Hashtable;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.relevantcodes.extentreports.LogStatus;
import com.selenium.core.base.TestBase;
import com.selenium.core.util.Constants;
import com.selenium.core.util.DataUtil;
import com.selenium.core.util.Xls_Reader;
/* Steps:
1. Load MUK site
2. Search and add the product to basket
3. Navigate to basket page and click on add promo link
4. Enter promo code and apply
5. Remove promo code 
*/
public class TC_REG_MUK_070 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_70(Hashtable<String, String> data) {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_70");
		System.out.println(" Starting test - tc_REG_MUK_70");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}

		// Adding Product to basket
		addToBasket(data.get("Product"));

		// clicking the basket button
		click("shoppingCart_Link_xpath");
		scrollDown(0, 500);
		wait(2);
		if (getElementsSize("cart_ApplyPromo_Code_Link_xpath") > 0) {
			click("cart_ApplyPromo_Code_Link_xpath");
			wait(1);
			// Applying Promo code
			type("cart_PromoCode_input_xpath", data.get("PromoCode"));

			// Clicking on Apply button
			click("cart_ApplyCoupon_button_xpath");

			wait(1);
			if (getElementsSize("cart_AppliedPromoCode_text_xpath") > 0) {
				if (getElementsSize("cart_RemovePromo_Link_xpath") > 0) {
					click("cart_RemovePromo_Link_xpath");
					wait(1);
					if (getElementsSize("cart_ApplyPromo_Code_Link_xpath") > 0) {
						reportPass("Promotion has been successfully removed");
					} else {
						reportFailure("Unable to remove applied promotion");
					}

				} else {
					reportFailure("Remove button is not available");
				}
			} else {
				reportFailure("Unable to apply promotion code, please check the settings in BM");
			}

		} else {
			reportFailure("Promotion Apply Code link is not available");
		}
	}

	@BeforeTest
	public void checkTestRunnable() {
		xls = new Xls_Reader(Constants.SUITE_PROMOTION_XLS_PATH);
		test = rep.startTest(testCaseName);

		if (!DataUtil.isRunnable(testCaseName, xls)) {
			test.log(LogStatus.SKIP,
					"Skipping the test case as Runmode is set to NO");
			System.out
					.println("Skipping the test case as Runmode is set to NO");
			throw new SkipException(
					"Skipping the test case as Runmode is set to NO");
		}

	}

	@AfterTest
	public void tearDown() {

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_70");
		System.out.println(" Ending test - tc_REG_MUK_70");

		wait(3);
		if (driver != null)
			driver.quit();

	}

	@BeforeMethod
	public void init() {

		if (driver == null) {
			test.log(LogStatus.INFO, "Opening Browser");
			System.out.println("Opening Browser");
			openBrowser(prop.getProperty("browserType"));
			navigate("appUrl");

		}

		softAssert = new SoftAssert();

	}

	@AfterMethod
	public void quit() {
		try {
			softAssert.assertAll();
		} catch (Error e) {
			test.log(LogStatus.WARNING, e.getMessage());
		}

		if (rep != null) {
			rep.endTest(test);
			rep.flush();
		}

	}

	@DataProvider
	public Object[][] getData() {

		super.init();
		return DataUtil.getTestData(xls, testCaseName);

	}

}
