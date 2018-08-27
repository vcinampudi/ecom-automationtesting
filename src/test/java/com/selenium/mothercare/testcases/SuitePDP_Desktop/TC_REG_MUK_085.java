package com.selenium.mothercare.testcases.SuitePDP_Desktop;

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

/* Steps:
1. Load MUK site
2. Search personalized product and add it to basket
3. Navigate to basket and click on edit
4. Update name and DOB date
*/
public class TC_REG_MUK_085 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;
	String oldPrice = null, selectedColor = null;
	Boolean Flag = false;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_085(Hashtable<String, String> data)
			throws IOException {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_085");
		System.out.println(" Starting test - tc_REG_MUK_085");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}

		// Searching the Product
		type("searchProduct_input_xpath", data.get("Product"));
		pressEnter("searchProduct_input_xpath");

		// Accepting the cookie policies
		//click("acceptCookies_btn_xpath");
		wait(1);

		scrollDown(0, 500);
		wait(2);

		if (getElementsSize("productNotFound_text_xpath") > 0) {
			reportFailure("Product not found, please check and input valid product Id");
		} else {
			if (getElementsSize("prodctHolderDOB_day_xpath") > 0) {
				// Capturing the Personalized atrributes for the product
				type("productHolderName_input_xpath", "Sunay");
				selectValueFromDropDown("prodctHolderDOB_day_xpath", "10");
				wait(1);
				selectValueFromDropDown("prodctHolderDOB_month_xpath",
						"September");
				wait(1);
				selectValueFromDropDown("prodctHolderDOB_year_xpath", "2017");
				wait(2);
				// Clicking on Add Product to basket button
				click("addToBasket_btn_xpath");
				wait(2);
				// clicking the basket button
				click("shoppingCart_Link_xpath");
				wait(2);
				scrollDown(0, 300);
				wait(2);
				if (getElementsSize("basket_personalisedInfo_text_xpath") > 0) {
					System.out
							.println(getText("basket_personalisedInfo_text_xpath"));

					// clicking edit basket link
					click("edit_Cart_Link_xpath");
					wait(2);
					// Capturing the Personalized attributes for the product
					type("productHolderName_input_xpath", "Sunay New");
					selectValueFromDropDown("prodctHolderDOB_day_xpath", "12");
					wait(1);
					selectValueFromDropDown("prodctHolderDOB_month_xpath",
							"March");
					wait(1);
					selectValueFromDropDown("prodctHolderDOB_year_xpath",
							"2018");
					wait(2);
					click("editbasket_PopUp_UpdateBasket_button_xpath");
					wait(5);
					if (getElementsSize("basket_personalisedInfo_text_xpath") > 0) {
						System.out.println(getText("basket_personalisedInfo_text_xpath"));
						reportPass("Personalised Details has been updated successfully");
					} else {
						reportFailure("Failed to Update the Personalised Details");
					}
				} else {
					reportFailure("Personalised product details are not getting added into the basket");
				}

			} else {
				reportFailure("Kindly select valid personalised product");
			}
		}
	}

	@BeforeTest
	public void checkTestRunnable() throws IOException {
		xls = new Xls_Reader(Constants.SUITE_PDP_XLS_PATH);
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_085");
		System.out.println(" Ending test - tc_REG_MUK_085");

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
