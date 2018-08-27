package com.selenium.mothercare.testcases.SuiteCheckout_Desktop;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

public class TC_REG_MUK_243 extends TestBase {
	// Test Update 3
	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;
	String window1;
	String window2;

	@Test(dataProvider = "getData")
	public void tC_REG_MUK_243(Hashtable<String, String> data) {

		test.log(LogStatus.INFO, " Starting test - tC_REG_MUK_243");
		System.out.println(" Starting test - tC_REG_MUK_243");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}
		driver.get("https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&useraction=commit&"
				+ "token=EC-6T231254JP0059505#/checkout/login");
		wait(3);
		paymentWithPayPalExpress(data.get("EmailAdd"),
				data.get("PayPalPassword"));
		wait(15);
		navigate("appUrl");

		wait(10);

		// doLogin(data.get("Username"), data.get("Password"));
		addToBasket(data.get("StandardProduct"));
		click("shoppingCart_Link_xpath");
		click("checkoutWithPayPal_btn_xpath");
		wait(3);
		// waitUntilElementPresent("payPal_popUp_Img_xpath");

		if (getElement("payPal_popUp_Img_xpath").isDisplayed()) {
			click("payPal_standardDelivery_btn_xpath");
			click("continueToPayPal_btn_xpath");
			// switchToPayPalWindow();
			while(getElementsSize("payPal_preLoader_spinner_xpath")>0){
				wait(5);
			}
			handelingFrame();
			//System.out.println("win"+ driver.getWindowHandles().size());
			//driver.switchTo().defaultContent();
		
				
		
		
			
			
			//handelingFrame();

			/*
			 * while(getElementsSize("payPal_adtnlScr_login_btn_xpath")==0){
			 * wait(10); }
			 * if(getElementsSize("payPal_adtnlScr_login_btn_xpath")>0){
			 * click("payPal_adtnlScr_login_btn_xpath"); //
			 * driver.switchTo().window(window1); }
			 */
			// wait(20);
			/*
			 * while (!(getElementsSize("payPal_AdtnlScr_email_input_xpath") >
			 * 0) || (getElementsSize("PayPal0nThirdPage_link_xpath") > 0)) {
			 * wait(5); } paymentWithPayPalExpress(data.get("EmailAdd"),
			 * data.get("PayPalPassword")); wait(5);
			 * 
			 * // Validation if
			 * ((getElementsSize("orderConfirmation_text_xpath")) > 0) {
			 * reportPass("Order placed successfully"); } else {
			 * reportFailure("Order not placed"); }
			 */

		}

		else {
			reportFailure("PayPal Pop up is not displaying");
		}

	}

	private void handelingFrame() {

		try {
			List<WebElement> frames = driver.findElements(By.tagName("iframe"));
			System.out.println("Num of frames: " + frames.size());
			for (int i = 0; i < frames.size(); i++) {
				System.out.println("frame id" + i + " "
						+ frames.get(i).getAttribute("id"));
				driver.switchTo().frame(frames.get(i));
				wait(2);
				if (getElementsSize("payPal_adtnlScr_login_btn_xpath") > 0) {
					click("payPal_adtnlScr_login_btn_xpath");
					break;

				}
				//driver.switchTo().defaultContent();
				//wait(2);
			}
		} catch (Exception e) {

			reportFailure(e.getMessage());
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

		test.log(LogStatus.INFO, " Ending test - tC_REG_MUK_243");
		System.out.println(" Ending test - tC_REG_MUK_243");

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
			// navigate("appUrl");

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

	private void paymentWithPayPalExpress(String email, String pwd) {

		// If PayPal Latest page is displaying
		try {
			if (getElementsSize("payPal_AdtnlScr_email_input_xpath") > 0) {
				List<WebElement> frames = driver.findElements(By
						.tagName("iframe"));
				System.out.println("Num of frames: " + frames.size());
				for (int i = 0; i < frames.size(); i++) {
					System.out.println("frame id" + i + " "
							+ frames.get(i).getAttribute("id"));
					driver.switchTo().frame(
							driver.findElements(By.tagName("iframe")).get(i));
					wait(2);
					if (getElementsSize("payPal_AdtnlScr_email_input_xpath") > 0) {
						clear("payPal_AdtnlScr_email_input_xpath");
						type("payPal_AdtnlScr_email_input_xpath", email);
						type("payPal_AdtnlScr_password_input_xpath", pwd);
						click("payPal_payment_btn_xpath");
						driver.switchTo().defaultContent();
						/*
						 * while
						 * ((getElementsSize("payPal_processing_spinner_xpath")
						 * > 0)) { wait(18); }
						 * click("payPal_adtnlScr_login_btn_xpath");
						 * driver.switchTo().window(window1);
						 */
						break;
					}

				}
			}

			// If PayPal old page is displaying
			else {
				click("PayPal0nThirdPage_link_xpath");
				// wait(10);
				// input[@value='Pay with my PayPal account']
				// input[@id='loadLogin']

				while (!(getElementsSize("payPal_email_input_xpath") > 0)) {
					wait(5);
				}
				clear("payPal_email_input_xpath");
				type("payPal_email_input_xpath", email);
				type("payPal_password_input_xpath", pwd);
				click("payPal_login_btn_xpath");
				// waitUntilElementPresent("payPal_payment_btn_xpath");
				click("payPal_payment_btn_xpath");

			}
		} catch (Exception e) {

			reportFailure(e.getMessage());
		}

	}

	private void switchToPayPalWindow() {
		try {
			wait(5);
			Set<String> wins = driver.getWindowHandles();
			Iterator itr = wins.iterator();
			System.out.println("Win " + wins.size());
			window1 = (String) itr.next();
			window2 = (String) itr.next();
			System.out.println("Window 1: " + window1 + "Window 2: " + window2);
			driver.switchTo().window(window2);
			driver.manage().window().maximize();
		} catch (Exception e) {

			reportFailure(e.getMessage());
		}

	}

}
