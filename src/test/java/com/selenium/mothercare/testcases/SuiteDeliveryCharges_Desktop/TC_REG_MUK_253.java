package com.selenium.mothercare.testcases.SuiteDeliveryCharges_Desktop;

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
2. Search and add product to basket
3. Navigate to basket page and click on express paypal checkout
4. Verify collect in store delivery charges
*/
public class TC_REG_MUK_253 extends TestBase {
	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_253(Hashtable<String, String> data) {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_253");
		System.out.println(" Starting test - tc_REG_MUK_253");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}

		// User Login
		//doLogin(data.get("Username"), data.get("Password"));

		// Adding product to basket
		addToBasket(data.get("Product"));

		// Navigating to shopping basket
		click("shoppingCart_Link_xpath");
		wait(5);
		// Going through Paypal Express checkout
		click("checkoutWithPayPal_btn_xpath");
		wait(2);
	
		// Validating the charges received against actual charges value from BM
		if (getText("paypalPopUp_CnCDelCharges_text_xpath")
				.equalsIgnoreCase(prop.getProperty("CnC"))) {
			reportPass("Click and Collect charges has been displayed correctly for Paypal Express as : "
					+ getText("paypalPopUp_CnCDelCharges_text_xpath")
							.substring(1));
		} else
			reportFailure("Invalid Charges are getting displayed on Paypal Express");

	}

	@BeforeTest
	public void checkTestRunnable() {
		xls = new Xls_Reader(Constants.SUITE_DELCHARGES_XLS_PATH);
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_253");
		System.out.println(" Ending test - tc_REG_MUK_253");

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
