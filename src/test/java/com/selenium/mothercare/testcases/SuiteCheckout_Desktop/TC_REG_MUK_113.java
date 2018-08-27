package com.selenium.mothercare.testcases.SuiteCheckout_Desktop;

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
 3. Continue to checkout till delivery page
 4. Select UK delivery option and manually add UK delivery address  
 */
public class TC_REG_MUK_113 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_113(Hashtable<String, String> data) {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_113");
		System.out.println(" Starting test - tc_REG_MUK_113");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}

		// User Login
		doLogin(data.get("Username"), data.get("Password"));
		
		//adding product to basket
		addToBasket(data.get("Product"));
		
		// clicking the basket button
		click("shoppingCart_Link_xpath");
		
		//clicking secure checkout button
		click ("checkout_Btn_xpath");
		
		//clicking UK Standard radio button
		click ("deliveryToUK_radioBtn_xpath");
		
		//scrolling down
		scrollDown(0, 1000);
		
		wait(2);
		
		//clicking add new address
		click ("addNewAdr_Btn_xpath");
		
		//scrolling down 
		scrollDown(0, 500);
		
		//clicking enter address manually link
		click("enterAddressManuallyLink_xpath");
		
		//filling UK address manually and navigate to payment page
		enterAddressManually(data.get("AddressNickname"), data.get("AddressLine1"), data.get("AddressLine2"), data.get("Town"), data.get("Postcode"));
		
		wait(2);
		//validating shipping page
		System.out.println("Text: " + getText("availableDeliveryOptions_text_xpath"));
		if (getText("availableDeliveryOptions_text_xpath").equals(Constants.AVAILABLE_DELIVERY_OPTION))
		{
			
			reportPass("Successfully navigated to delivery option page!");
		}
		else
		{
			reportFailure("Failed to navigate on delivery option page!");
		}
		
		
		
		
	}

	@BeforeTest
	public void checkTestRunnable() {
		xls = new Xls_Reader(Constants.SUITE_CHECKOUT_XLS_PATH);
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_113");
		System.out.println(" Ending test - tc_REG_MUK_113");

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
