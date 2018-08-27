package com.selenium.mothercare.testcases.SuiteCheckout_Desktop;

import java.awt.Container;
import java.util.Hashtable;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
public class TC_REG_MUK_148 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_148(Hashtable<String, String> data) {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_148");
		System.out.println(" Starting test - tc_REG_MUK_148");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}

		// adding product to basket
		addToBasket(data.get("Product"));

		// clicking the basket button
		click("shoppingCart_Link_xpath");

		// clicking secure checkout button
		click("checkout_Btn_xpath");
		wait(2);
		scrollDown(0, 350);
		driver.findElement(By.id("dwfrm_singleshipping_email")).sendKeys("test0904@test.com");
		// clicking checkout as guest button
		click("checkoutAsGuest_btn_xpath");
        wait(5);

		// clicking Click & Collect delivery option
		click("clickandcollect_radioBtn_xpath");

		scrollDown(0, 450);

		// selecting store
		type("clickAndCollect_postCode_input_xpath", Constants.POSTAL_CODE);
		click("clickAndCollect_find_btn_xpath");
		wait(4);
		click("selectStore_btn_xpath");

		scrollDown(0, 450);

		// enter personal information
		selectValueFromDropDown("selectTitle_dropDown_xpath", Constants.TITLE);
		type("bfpoFirstName_input_xpath", Constants.FIRST_NAME);
		type("bfpoLastName_input_xpath", Constants.LAST_NAME);
		//type("bfpoEmail_input_xpath", Constants.EMAIL);
		type("bfpoPhone_input_xpath", Constants.PHONE_NUMBER);

		// clicking proceed to payment button
		click("proceedToPayment_Btn_xpath");

		wait(2);

		// pay with credit card
		click("creditcardPay_Btn_xpath");

		scrollDown(0, 450);

		selectValueFromDropDown("billing_SelectTitle_dropDown_xpath",
				Constants.TITLE);
		type("billing_FirstName_DeliveryCnC_input_xpath", Constants.FIRST_NAME);
		type("billing_LastName_DeliveryCnC_input_xpath", Constants.LAST_NAME);
		clear("billingAdrEmail_input_xpath");
		type("billingAdrEmail_input_xpath", Constants.BILLING_EMAIL);
		type("billing_Phoneno_DeliveryCnC_input_xpath", Constants.PHONE_NUMBER);

		scrollDown(0, 450);
		type("billingAdrHouseNum_input_xpath", Constants.BILLING_HOUSE_NUM);
		type("adrSearchPostalCode_input_xpath", Constants.POSTAL_CODE);
		click("findAdr_Btn_xpath");
		wait(2);
		click("selectNewBillingAdr_dropDown_xpath");
		pressDown("selectNewBillingAdr_dropDown_xpath");
		pressEnter("selectNewBillingAdr_dropDown_xpath");
		scrollToBottomofAPage();
		wait(5);
		click("submitBill_Btn_xpath");

		wait(2);
		driver.switchTo().frame("paymentIframe");

		clear("creditCard_nameoncard_input_xpath");
		type("creditCard_nameoncard_input_xpath", Constants.FIRST_NAME);
		clear("creditCard_cardnumber_input_xpath");
		type("creditCard_cardnumber_input_xpath", Constants.CARD_NO_VISA);
		selectValueFromDropDown("creditCard_expdate_dropDown_xpath", "02");
		selectValueFromDropDown("creditCard_expyear_dropDown_xpath", "2020");
		clear("creditCard_cvv_input_xpath");
		type("creditCard_cvv_input_xpath", Constants.CARD_CVV);
		click("creditCard_savecard_checkBox_xpath");
		click("creditCard_placeorder_btn_xpath");

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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_148");
		System.out.println(" Ending test - tc_REG_MUK_148");
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
