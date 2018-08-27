package com.selenium.mothercare.testcases.SuiteCheckout_Desktop;

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
/*Scenario:
User type=Registered
Delivery Address=New
Delivery Option=International
Delivery Method=Channel Island
Payment type=Gift Card +   Paypal
*/
public class TC_REG_MUK_138 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_145(Hashtable<String, String> data) {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_138");
		System.out.println(" Starting test - tc_REG_MUK_138");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}
		// Logging in

		doLogin(data.get("Username"), data.get("Password"));
		// Adding Product to basket
		addToBasket(data.get("Product"));

		// clicking the basket button
		click("shoppingCart_Link_xpath");
		// Clicking on checkout button
		click("checkout_Btn_xpath");
		wait(2);
		scrollDown(0, 400);
		wait(2);
		// clicking international delivery option
		click("internationalDelivery_radioBtn_xpath");
		// Scrolling To Add New Address Button
		scrollToElement("addNewAdr_Btn_xpath");

		// clicking add new address
		click("addNewAdr_Btn_xpath");

		scrollDown(0, 800);
		
		//Entering Phone number
		type("bfpoPhone_input_xpath", Constants.PHONE_NUMBER);

		// Entering Address Line 1
		type("addLine1_input_xpath", Constants.ADD_LINE_1);

		// Entering Town
		type("town_input_xpath", Constants.TOWN);

		// Selecting Country
		selectValueFromDropDown("selectCountry_dropDown_xpath",
				Constants.COUNTRY);

		// Entering Internationl Postal Code
		type("intPostcode_input_xpath", Constants.INT_POSTCODE);

		// clicking deliver to this address button
		click("deliverToThisAdr_Btn_xpath");

		// selecting Channel Island option
		click("channelIsland_radioBtn_xpath");

		scrollDown(0, 400);
		click("proceedToPayment_Btn_xpath");

		scrollDown(0, 400);
		// pay using gift card and Paypal
		click("redeemGiftCardLink_xpath");
		type("giftCardNumber_input_xpath", data.get("GiftCard"));
		click("redeem_btn_xpath");
		wait(5);

		int redemptionSuccessTextSize = getElementsSize("redemptionSuccessful_text_xpath");

		if (redemptionSuccessTextSize > 0) {

			if (getElement("remianingBalance_text_xpath").getText().equals("£0.00")) {
				reportFailure("Please use Gift card with less value");
			} else {

				click("partialPayment_Gift_Paypal_button_xpath");
				wait(3);
				click("proceedToPayPal_btn_xpath");
				wait(3);

				// Calling Paywith Paypal Local function. This function is not
				// written in TestBase.
				partPaywithPaypal(data.get("PayPalUsername"), data.get("PayPalPassword"));

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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_138");
		System.out.println(" Ending test - tc_REG_MUK_138");

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

	public void partPaywithPaypal(String email, String password) {

		wait(6);
		System.out.println(driver.getWindowHandles().size());
		List<WebElement> frames = driver.findElements(By.tagName("iframe"));
		System.out.println("Num of frames: " + frames.size());

		for (int i = 0; i < frames.size(); i++) {
			System.out.println("frame id" + i + " "
					+ frames.get(i).getAttribute("id"));
			driver.switchTo().frame(
					driver.findElements(By.tagName("iframe")).get(i));
			if (getElementsSize("payPal_AdtnlScr_email_input_xpath") > 0) {
				clear("payPal_AdtnlScr_email_input_xpath");
				type("payPal_AdtnlScr_email_input_xpath", email);
				type("payPal_AdtnlScr_password_input_xpath", password);
				click("payPal_payment_btn_xpath");
				driver.switchTo().defaultContent();
				while ((getElementsSize("payPal_processing_spinner_xpath") > 0)) {
					wait(18);
				}
				click("payPal_adtnlScr_login_btn_xpath");

				break;
			}

		}
	}
}
