package com.selenium.mothercare.testcases.SuiteCheckout_Desktop;

import java.awt.Container;
import java.util.Hashtable;

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
/* Scenario:
 User type=Registered
Delivery Address=Existing
Delivery Option=International
Delivery Method=Rest of The world
Payment type=Gift Card + Gift Card
 */
public class TC_REG_MUK_139 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_139(Hashtable<String, String> data) {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_139");
		System.out.println(" Starting test - tc_REG_MUK_139");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}

		// User Login
		doLogin(data.get("Username"), data.get("Password"));

		// adding product to basket
		addToBasket(data.get("InternationalProduct"));

		// clicking the basket button
		click("shoppingCart_Link_xpath");

		// changing quantity of added product
		pressBackspace("basketQuantity_input_xpath");
		wait(3);
		type("basketQuantity_input_xpath", (data.get("Quantity")));
		wait(2);
		// clicking secure checkout button
		click("checkout_Btn_xpath");

		scrollDown(0, 400);

		// clicking international delivery option
		click("internationalDelivery_radioBtn_xpath");

		scrollToElement("addNewAdr_Btn_xpath");
		click("addNewAdr_Btn_xpath");

		// entering international delivery fields and proceed to payment
		scrollToElement("selectCountry_dropDown_xpath");
		type("bfpoPhone_input_xpath", Constants.PHONE_NUMBER);
		type("addLine1_input_xpath", Constants.ADD_LINE_1);
		type("addLine2_input_xpath", Constants.ADD_LINE_2);
		type("town_input_xpath", Constants.TOWN);
		selectValueFromDropDown("selectCountry_dropDown_xpath",
				Constants.COUNTRY_REST_OF_THE_WORLD);

		scrollDown(0, 500);
		type("intPostcode_input_xpath", Constants.REST_OF_THE_WORLD_POSTCODE);

		// clicking deliver to this address button
		click("deliverToThisAdr_Btn_xpath");

		wait(2);

		// selecting rest of the world delivery option
		click("restOfTheWorldDeliveryOption_radioBtn_xpath");
		scrollDown(0, 500);
		click("proceedToPayment_Btn_xpath");

		wait(2);
		scrollDown(0, 400);
		// pay using 2 gift cards
		click("redeemGiftCardLink_xpath");
		type("giftCardNumber_input_xpath", data.get("GiftCard1"));
		click("redeem_btn_xpath");
		wait(5);

		int redemptionSuccessTextSize = getElementsSize("redemptionSuccessful_text_xpath");

		if (redemptionSuccessTextSize > 0) {
			type("giftCardNumber_input_xpath", data.get("GiftCard2"));
			click("redeemAnotherGiftCard_btn_xpath");
			wait(6);
			WebElement placeOrderBtn = getElement("giftCardPlaceOrder_btn_xpath");
			String remainingToPayText = getElement("remainingToPay_text_xpath")
					.getText();
			System.out.println(remainingToPayText);
			Boolean placeOrderBtnIsDisplayedStatus = placeOrderBtn
					.isDisplayed();
			System.out.println(placeOrderBtnIsDisplayedStatus);
			if (placeOrderBtn.isDisplayed()
					&& remainingToPayText.contains("0.00")) {
				System.out
						.println("Place order button displayed! And Remaining to pay is 0.00");
				click("giftCardPlaceOrder_btn_xpath");

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

			} else {
				test.log(LogStatus.INFO,
						" Unable to place order using multiple gift cards only");
				System.out
						.println(" Unable to place order using multiple gift cards only");
				reportFailure("Failed to place order using multiple gift cards!");
			}

		} else {
			test.log(LogStatus.INFO, " Unable to redeem gift card");
			System.out.println(" Unable to redeem gift card");
			reportFailure("Unable to redeem gift card");
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_139");
		System.out.println(" Ending test - tc_REG_MUK_139");

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
