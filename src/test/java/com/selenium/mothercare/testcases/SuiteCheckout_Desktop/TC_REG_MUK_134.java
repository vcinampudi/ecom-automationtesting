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
/*Scenario:
User type=Registered
Card Type=Saved
Delivery Address=New
Delivery Option=Click and Collect
Payment type=Credit Card
 */
public class TC_REG_MUK_134 extends TestBase {
//Test Update 3
	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_134(Hashtable<String, String> data) {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_134");
		System.out.println(" Starting test - tc_REG_MUK_134");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}

		doLogin(data.get("Username"), data.get("Password"));
		type("searchProduct_input_xpath", data.get("Product"));
		pressEnter("searchProduct_input_xpath");
		scrollDown(0, 370);
		Character cartQuantityBefore = getText("cart_quantity_text_xpath")
				.charAt(0);
		System.out.println("Bef" + cartQuantityBefore);
		click("addToBasket_btn_xpath");
		wait(2);
		Character cartQuantityAfter = getText("cart_quantity_text_xpath")
				.charAt(0);
		System.out.println("After" + cartQuantityAfter);

		if (Character.getNumericValue(cartQuantityAfter) > Character
				.getNumericValue(cartQuantityBefore)) {
			test.log(LogStatus.INFO, "Product Added in cart");
			click("shoppingCart_Link_xpath");
			// click("shoppingCart_Link_xpath");
			click("checkout_Btn_xpath");

			if (data.get("UserType").equals("UnRegistered")) {
				doLoginUnRegUser(data.get("Username"), data.get("Password"));
			}

			// Delivery Option="Click and Collect" and Delivery Adr is "New"
			/*
			 * if (data.get("DeliveryOption").equals("CnC")&&
			 * data.get("DeliveryAdr").equals("New")) { System.out.println(
			 * "Delivery Option=Click and Collect and Delivery Adr is New");
			 */
			click("clickandcollect_radioBtn_xpath");
			wait(5);
			//scrollDown(0, 1450);
		//	wait(3);
			//click("searchStores_Btn_xpath");//New Delivery Adr
		//	wait(3);
			type("searchStore_Input_xpath", Constants.POSTAL_CODE);
			pressEnter("searchStore_Input_xpath");
			wait(3);
			click("mothercare_Watford_Btn_xpath");
			wait(3);
			clear("deliveryDetails_contactNum_Input_xpath");
			type("deliveryDetails_contactNum_Input_xpath",
					Constants.CONTACT_NUM);
			click("proceedToPayment_Btn_xpath");
			click("creditcardPay_Btn_xpath");
			scrollDown(0, 1900);
			wait(2);
			click("addNewAdr_Btn_xpath");//Add new billing adr
			type("billingAdrEmail_input_xpath", Constants.BILLING_EMAIL);
			type("billingAdrHouseNum_input_xpath", Constants.BILLING_HOUSE_NUM);
			type("adrSearchPostalCode_input_xpath", Constants.POSTAL_CODE);
			scrollToBottomofAPage();
			click("findAdr_Btn_xpath");
			wait(2);
			// pressTab("findAdr_Btn_xpath");
			click("selectNewBillingAdr_dropDown_xpath");
			pressDown("selectNewBillingAdr_dropDown_xpath");
			pressEnter("selectNewBillingAdr_dropDown_xpath");
			scrollToBottomofAPage();
			wait(5);
			click("submitBill_Btn_xpath");
			click("first_creditCard_xpath");
			type("creditCard_CVV_input_xpath", Constants.CARD_CVV);
			click("creditCard_placeOrder_Btn_xpath");
			
			// validating order page
			System.out.println("Order success message "
					+ getText("orderSuccessMessage_text_xpath"));
			if (getText("orderSuccessMessage_text_xpath").equals(
					Constants.SUCCESS_ORDER_MESSAGE)) {

				//Writing Order Number into an  Excel Sheet
				scrollDown(0, 350);
				wait(3);
				xlsWriter.saveDataToXls(testCaseName,getElement("orderNumber_text_xpath").getText());
				reportPass("Order placed successfully!");
				
				
			} else {
				reportFailure("Failed to place order!");
			}

			// }

			// Delivery Option="UK" and Delivery Adr is "New"
			/*
			 * if
			 * (data.get("DeliveryOption").equals(Constants.DELIVERY_OPTION_UK
			 * )&& data.get("DeliveryAdr").equals(Constants.DELIVERY_ADR_NEW)){
			 * System.out.println("Delivery Option=UK and Delivery Adr is New");
			 * click("deliveryToUK_radioBtn_xpath"); wait(2); scrollDown(0,
			 * 1470); click("addNewAdr_Btn_xpath");
			 * type("addressSearch_postCode_xpath", Constants.POSTAL_CODE);
			 * pressEnter("addressSearch_postCode_xpath"); scrollDown(0, 1600);
			 * click("selectNewBillingAdr_DeliveryUK_dropDown_xpath");
			 * pressDown("selectNewBillingAdr_DeliveryUK_dropDown_xpath");
			 * pressEnter("selectNewBillingAdr_DeliveryUK_dropDown_xpath");
			 * //type("addressNickName_DeliveryUK_input_xpath",
			 * Constants.NICKNAME); scrollToBottomofAPage();
			 * click("deliverToThisAdr_Btn_xpath");
			 * if(data.get("DeliveryMethod")
			 * .equals(Constants.DELIVERY_TYPE_STANDARD)){
			 * click("standardDelivery_radioBtn_xpath"); }
			 * click("proceedToPayment_Btn_xpath"); wait(5);
			 * if(data.get("PayType"
			 * ).equals(Constants.PAYMENT_MODE_CREDIT_CARD)&&
			 * data.get("CardType").equals(Constants.CARD_TYPE_SAVED)){
			 * click("creditcardPay_Btn_xpath");
			 * click("first_creditCard_xpath");
			 * type("creditCard_CVV_input_xpath", Constants.CARD_CVV);
			 * click("creditCard_placeOrder_Btn_xpath"); }
			 * 
			 * 
			 * 
			 * }
			 */

		} else {
			test.log(LogStatus.INFO, "Product Not Added in cart");
			reportFailure("Product not added");
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_134");
		System.out.println(" Ending test - tc_REG_MUK_134");

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
