package com.selenium.mothercare.testcases.SuitePDP_Desktop;

import java.io.IOException;
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
 2. Search product
 3. Enter name, DOB and product to the basket 
 */
public class TC_REG_MUK_043 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;
	String oldPrice = null, selectedColor = null;
	Boolean Flag = false;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_043(Hashtable<String, String> data)
			throws IOException {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_043");
		System.out.println(" Starting test - tc_REG_MUK_043");

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

		scrollDown(0, 100);
		wait(2);

		if (getElementsSize("productNotFound_text_xpath") > 0) {
			reportFailure("Product not found, please check and input valid product Id");
		} else {
			if (getElementsSize("productHolderName_input_xpath") > 0
					|| getElementsSize("prodctHolderDOB_day_xpath") > 0) {

				// Capturing the Personalized atrributes for the product
				type("productHolderName_input_xpath", "Sunay");
				selectValueFromDropDown("prodctHolderDOB_day_xpath", "10");
				wait(1);
				selectValueFromDropDown("prodctHolderDOB_month_xpath",
						"September");
				wait(1);
				selectValueFromDropDown("prodctHolderDOB_year_xpath", "2017");
				wait(1);
				scrollDown(0, 400);
				wait(1);
				// Clicking on Add Product to basket button
				click("addToBasket_btn_xpath");
				wait(2);
				// clicking the basket button
				click("shoppingCart_Link_xpath");
				wait(2);
				scrollDown(0, 300);
				wait(1);
				if (getElementsSize("basket_personalisedInfo_text_xpath") > 0) {
					reportPass("Personalised product info has been added successfully");
				} else {
					reportFailure("Personalised product information is not getting reflected");
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_043");
		System.out.println(" Ending test - tc_REG_MUK_043");

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
