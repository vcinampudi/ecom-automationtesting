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
3. Continue to checkout using collect in store delivery option
4. Add all required details and place an order using credit card payment mode
5. Check details on order confirmation page 
*/
public class TC_REG_MUK_133 extends TestBase {
	// Test Update 3
	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_133(Hashtable<String, String> data) {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_133");
		System.out.println(" Starting test - tc_REG_MUK_133");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}

		doLogin(data.get("Username"), data.get("Password"));
		addToBasket(data.get("StandardProduct"));
		click("shoppingCart_Link_xpath");
		click("checkout_Btn_xpath");
		click("clickandcollect_radioBtn_xpath");
		// scrollDown(0, 300);
		wait(4);
	/*	scrollToElement("selectSavedStore_btn_xpath");
		if (getElement("selectSavedStore_btn_xpath").isDisplayed()) {
			click("selectSavedStore_btn_xpath");
		}
		// scrollDown(0, 1900);
		scrollToElement("deliveryDetails_contactNum_Input_xpath");
		type("deliveryDetails_contactNum_Input_xpath", Constants.CONTACT_NUM); */
		
		type("searchStore_Input_xpath", Constants.POSTAL_CODE);
		pressEnter("searchStore_Input_xpath");
		wait(3);
		click("mothercare_Watford_Btn_xpath");
		wait(3);
		clear("deliveryDetails_contactNum_Input_xpath");
		type("deliveryDetails_contactNum_Input_xpath",
				Constants.CONTACT_NUM);
		
		click("proceedToPayment_Btn_xpath");
		wait(4);
		// Pay with credit card
		payWithCreditCard(Constants.DELIVERY_OPTION_CnC);
		if (getElement("clickAndCollect_orderSummary_collectFrom_text_xpath")
				.isDisplayed()) {
			scrollToElement("clickAndCollect_orderSummary_collectFrom_text_xpath");
			reportPass("Store Details, Lead Time and other details displaying correctly");
		} else {
			reportFailure("Details arw not displaying");
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_133");
		System.out.println(" Ending test - tc_REG_MUK_133");

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
