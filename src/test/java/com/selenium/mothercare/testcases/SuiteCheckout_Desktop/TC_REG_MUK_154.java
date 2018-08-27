package com.selenium.mothercare.testcases.SuiteCheckout_Desktop;

import java.util.Hashtable;

import org.openqa.selenium.By;
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
/*
User type=Guest
Card Type=Any
Delivery Address=Any
Delivery Option=UK
Delivery Method=Standard
Payment type=Credit Card
 */
public class TC_REG_MUK_154 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_154(Hashtable<String, String> data) {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_154");
		System.out.println(" Starting test - tc_REG_MUK_154");

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

		// Clicking on checkout button
		click("checkout_Btn_xpath");
		wait(2);
		scrollDown(0, 350);
		driver.findElement(By.id("dwfrm_singleshipping_email")).sendKeys("test0904@test.com");
		// clicking checkout as guest button
		click("checkoutAsGuest_btn_xpath");
        wait(5);
		// Accepting the cookies policy
		//click("acceptCookies_btn_xpath");
		//wait(2);
		// Selecting UK delivery
		click("deliveryToUK_radioBtn_xpath");
		wait(3);
		selectValueFromDropDown("selectTitle_dropDown_xpath", "Mr");
		clear("firstName_DeliveryCnC_input_xpath");
		clear("lastName_DeliveryCnC_input_xpath");
		//clear("emailId_DeliveryCnC_input_xpath");
		clear("deliveryDetails_contactNum_Input_xpath");
		selectValueFromDropDown("selectTitle_dropDown_xpath", "Mr");
		type("firstName_DeliveryCnC_input_xpath", Constants.FIRST_NAME);
		type("lastName_DeliveryCnC_input_xpath", Constants.LAST_NAME);
		//type("emailId_DeliveryCnC_input_xpath", Constants.EMAIL_ID);
		type("deliveryDetails_contactNum_Input_xpath", Constants.CONTACT_NUM);
		type("addressSearch_postCode_xpath", Constants.POSTAL_CODE);
		click("findAdr_Btn_xpath");
		wait(2);
		// selecting value from dropdown
		selectValueFromDropDown(
				"selectNewBillingAdr_DeliveryUK_dropDown_xpath",
				Constants.NEW_BILLING_ADR);
		wait(2);
		scrollDown(0, 900);
		// clicking deliver to this address button
		click("deliverToThisAdr_Btn_xpath");

		wait(2);
		// Selecting Standard delivery option
		click("standardDeliveryOption_radioBtn_xpath");
		wait(2);

		// Clicking payment button
		scrollToElement("proceedToPayment_Btn_xpath");
		wait(2);
		click("proceedToPayment_Btn_xpath");
		wait(2);
		// Making payment with credit card
		payWithNewCreditCard();

		// validating order page
		System.out.println("Order success message "
				+ getText("orderSuccessMessage_text_xpath"));
		if (getText("orderSuccessMessage_text_xpath").equals(
				Constants.SUCCESS_ORDER_MESSAGE)) {

			// Writing Order Number into an Excel Sheet
			scrollDown(0, 350);
			wait(3);
			xlsWriter.saveDataToXls(testCaseName,
					getElement("orderNumber_text_xpath").getText());
			reportPass("Order placed successfully!");

		} else {
			reportFailure("Failed to place order!");
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_154");
		System.out.println(" Ending test - tc_REG_MUK_154");

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
