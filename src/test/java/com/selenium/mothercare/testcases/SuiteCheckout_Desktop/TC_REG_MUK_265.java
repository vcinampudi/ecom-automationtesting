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

public class TC_REG_MUK_265 extends TestBase {
	// Test Update 3
	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;
	String window1;
	String window2;

	@Test(dataProvider = "getData")
	public void tC_REG_MUK_265(Hashtable<String, String> data) {

		test.log(LogStatus.INFO, " Starting test - tC_REG_MUK_265");
		System.out.println(" Starting test - tC_REG_MUK_265");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}

		doLogin(data.get("Username"), data.get("Password"));
		addToBasket(data.get("FurnitureProduct"));
		click("shoppingCart_Link_xpath");
		wait(2);
		click("checkoutWithPayPal_btn_xpath");
		wait(3);

		if (getElement("payPal_popUp_Img_xpath").isDisplayed()) {
			click("payPal_ChannelIslands_Del_btn_xpath");
			click("continueToPayPal_ChannelIsland_btn_xpath");
			wait(6);
			if (getElementsSize("savedInternationalAdr_text_xpath") > 0) {
				int numOfInternationalAdr = getElementsSize("international_addresses_list_xpath");
				System.out.println("numOfInternationalAdr: "
						+ numOfInternationalAdr);
				if (numOfInternationalAdr > 0) {
					int numOfJersyInternationalAdr = getElementsSize("jersey_internationalAdr_text_xpath");
					int numOfGurenseyInternationalAdr = getElementsSize("guernsey_internationalAdr_text_xpath");
					System.out.println("numOfJersyInternationalAdr: "
							+ numOfJersyInternationalAdr);
					System.out.println("numOfGurenseyInternationalAdr: "
							+ numOfGurenseyInternationalAdr);

					int noOtherChannelCountry = 0;
					for (int i = 1; i <= numOfInternationalAdr - 1; i++) {
						String jerseySavedAdrXpath = prop
								.getProperty("international_addresses_list_xpath")
								+ "["
								+ i
								+ "]"
								+ prop.getProperty("jersey_xpath");
						String guernseySavedAdrXpath = prop
								.getProperty("international_addresses_list_xpath")
								+ "["
								+ i
								+ "]"
								+ prop.getProperty("guernsey_xpath");

						System.out.println("jerseySavedAdrXpath: "
								+ jerseySavedAdrXpath);
						System.out.println("guernseySavedAdrXpath: "
								+ guernseySavedAdrXpath);

						int jerseySavedAdrXpathSize = driver.findElements(
								By.xpath(jerseySavedAdrXpath)).size();
						int guernseySavedAdrXpathSize = driver.findElements(
								By.xpath(guernseySavedAdrXpath)).size();
						System.out.println("jerseySavedAdrXpathSize: "
								+ jerseySavedAdrXpathSize);
						System.out.println("guernseySavedAdrXpathSize: "
								+ guernseySavedAdrXpathSize);
						String selectAddressButton = prop
								.getProperty("international_addresses_list_xpath")
								+ "[" + i + "]" + "/div/div[2]/button";

						if (!(jerseySavedAdrXpathSize > 0 || guernseySavedAdrXpathSize > 0)) {
							System.out.println("selectAddressButton: "
									+ selectAddressButton);
							driver.findElement(By.xpath(selectAddressButton))
									.click();

							// VALIDATION
							if (getElementsSize("cannotDeliveredToSelCountryPopUp_btn_xpath") > 0)
								reportPass("The following products cannot be delivered to your selected country.");
							else
								reportFailure("User is able to deliver the product o other than channel islands country");
							break;
						} else {
							noOtherChannelCountry++;
						}

						if (noOtherChannelCountry == numOfInternationalAdr - 1) {
							reportFailure("No other international Address than Jersey and Guernsey is saved");
						}

					}

					// }
				}

			}

		}

		else {
			reportFailure("PayPal Pop up is not displaying");
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

		test.log(LogStatus.INFO, " Ending test - tC_REG_MUK_265");
		System.out.println(" Ending test - tC_REG_MUK_265");

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
