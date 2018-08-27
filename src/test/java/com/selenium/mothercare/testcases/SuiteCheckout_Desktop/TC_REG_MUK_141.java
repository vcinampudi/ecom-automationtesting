package com.selenium.mothercare.testcases.SuiteCheckout_Desktop;

import java.io.IOException;
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
Delivery Option=Click and Collect
Payment type=Credit Card
 */
public class TC_REG_MUK_141 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_141(Hashtable<String, String> data)
			throws IOException {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_141");
		System.out.println(" Starting test - tc_REG_MUK_141");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}

		// Adding Product to basket
		addToBasket(data.get("StandardProduct"));
		// clicking the basket button
		click("shoppingCart_Link_xpath");
		// Clicking on checkout button
		click("checkout_Btn_xpath");
		wait(4);
		scrollDown(0, 350);
		driver.findElement(By.id("dwfrm_singleshipping_email")).sendKeys("test0904@test.com");
		//scrollToBottomofAPage();
		// Selecting guest checkout option
		click("guestCheckout_btn_xpath");
		wait(3);
		// Selecting Click and Collect delivery
		click("clickandcollect_radioBtn_xpath");
		wait(3);
		// Searching for store
		type("searchStore_Input_xpath", Constants.POSTAL_CODE);
		pressEnter("searchStore_Input_xpath");
		wait(7);
		// Selecting Mothercare watford store
		click("mothercare_Guest_User_Watford_Btn_xpath");
		// Filling up the contact details
		wait(2);
		clear("firstName_DeliveryCnC_input_xpath");
		clear("lastName_DeliveryCnC_input_xpath");
		//clear("emailId_DeliveryCnC_input_xpath");
		clear("deliveryDetails_contactNum_Input_xpath");
		selectValueFromDropDown("selectTitle_dropDown_xpath", "Mr");
		type("firstName_DeliveryCnC_input_xpath", Constants.FIRST_NAME);
		type("lastName_DeliveryCnC_input_xpath", Constants.LAST_NAME);
		//type("emailId_DeliveryCnC_input_xpath", Constants.EMAIL_ID);
		type("deliveryDetails_contactNum_Input_xpath", Constants.CONTACT_NUM);
		scrollToBottomofAPage();
		// Clicking on Proceed to Payment button
		click("proceedToPayment_Btn_xpath");
		// Calling function for paying with Credit Card
		wait(4);
		click("creditcardPay_Btn_xpath");
		wait(2);
		selectValueFromDropDown("billing_SelectTitle_dropDown_xpath", "Mr");
		type("billing_FirstName_DeliveryCnC_input_xpath", Constants.FIRST_NAME);
		type("billing_LastName_DeliveryCnC_input_xpath", Constants.LAST_NAME);
		// type("billingAdrEmail_input_xpath", Constants.BILLING_EMAIL);
		type("billing_Phoneno_DeliveryCnC_input_xpath", Constants.CONTACT_NUM);
		type("billingAdrHouseNum_input_xpath", Constants.BILLING_HOUSE_NUM);
		type("adrSearchPostalCode_input_xpath", Constants.POSTAL_CODE);
		scrollToBottomofAPage();
		// Finding Address
		click("findAdr_Btn_xpath");
		wait(2);
		// Select address from dropdown
		click("selectNewBillingAdr_dropDown_xpath");
		pressDown("selectNewBillingAdr_dropDown_xpath");
		pressEnter("selectNewBillingAdr_dropDown_xpath");
		scrollToBottomofAPage();
		wait(6);
		// Clicking on Bill to this address button
		click("submitBill_Btn_xpath");
		payWithNewCreditCard();

		// validating order page
		System.out.println("Order success message "
				+ getText("orderSuccessMessage_text_xpath"));
		if (getText("orderSuccessMessage_text_xpath").equals(
				Constants.SUCCESS_ORDER_MESSAGE)) {

			// Writing Order Number into an Excel Sheet
			scrollDown(0, 300);
			wait(4);
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_141");
		System.out.println(" Ending test - tc_REG_MUK_141");

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
