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
 2. Search product
 3. Validate personalized product
 4. Check delivery options available
 */
public class TC_REG_MUK_044 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;
	String oldPrice = null, selectedColor = null;
	Boolean Flag = false;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_044(Hashtable<String, String> data)
			throws IOException {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_044");
		System.out.println(" Starting test - tc_REG_MUK_044");

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
			if (getElementsSize("productHolderName_input_xpath") > 0
					|| getElementsSize("prodctHolderDOB_day_xpath") > 0) {
				System.out.println("Delivery Options Count "
						+ getElementsSize("pdp_DeliveryOptions_text_xpath"));
				if (getElementsSize("pdp_DeliveryOptions_text_xpath") > 1) {
					reportFailure("Multiple delivery options are getting displayed");
				} else {
					if (driver
							.findElement(
									By.xpath("//ul[@class='b-delivery js-delivery-list']/li"))
							.getAttribute("data-option").equals("standard")) {
						reportPass("Only one delivery option is getting displayed as Standard Delivery");
					} else {
						reportFailure("Invalid Delivery option is getting displayed");
					}
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_044");
		System.out.println(" Ending test - tc_REG_MUK_044");

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
