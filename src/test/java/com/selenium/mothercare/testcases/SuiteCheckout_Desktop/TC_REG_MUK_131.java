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
3. Continue to checkout using standard UK delivery till payment page
4. Select credit card payment mode and verify billing address radio button 
*/
public class TC_REG_MUK_131 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_131(Hashtable<String, String> data) {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_131");
		System.out.println(" Starting test - tc_REG_MUK_131");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}

		// User Login
		doLogin(data.get("Username"), data.get("Password"));
		// Adding Product to basket
		addToBasket(data.get("StandardProduct"));
		// clicking the basket button
		click("shoppingCart_Link_xpath");
		//Clicking on checkout button
		click("checkout_Btn_xpath");
		//Selecting UK delivery
		click("deliveryToUK_radioBtn_xpath");
		wait(4);
		scrollDown(0,600);
		//Clicking on Adding New Address
		click("addNewAdr_Btn_xpath");
		wait(4);
		//Adding Post code
		type("addressSearch_postCode_xpath", Constants.POSTAL_CODE);
		pressEnter("addressSearch_postCode_xpath"); 
		wait(2);
		scrollDown(0, 1600);
		//Seelcting the delivery address from dropdown
		click("selectNewBillingAdr_DeliveryUK_dropDown_xpath");
		pressDown("selectNewBillingAdr_DeliveryUK_dropDown_xpath");
		pressEnter("selectNewBillingAdr_DeliveryUK_dropDown_xpath");
		scrollToBottomofAPage();
		//Clicking on delivery to address button
		click("deliverToThisAdr_Btn_xpath");
		//Selecting standard delivery shipping method
		wait(4);
		click("standardDelivery_radioBtn_xpath");
		scrollToBottomofAPage();
		click("proceedToPayment_Btn_xpath");
		wait(4);
		click("creditcardPay_Btn_xpath");
		if(getElement("use_SameAddress_As_Delivery_radioBtn_xpath").isEnabled()){
			reportPass("User is able to select same billing adddress as delivery");
		}
		else{
			reportFailure("User is not able to select same billing adddress as delivery");
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_131");
		System.out.println(" Ending test - tc_REG_MUK_131");

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
