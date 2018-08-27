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
 2. Search Stokke product
 3. Click on add to the basket
 4. Validate video overlay
 5. Check product in the basket
 */
public class TC_REG_MUK_047 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;
	String oldPrice = null, selectedColor = null;
	Boolean Flag = false;
	int size = 0;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_047(Hashtable<String, String> data)
			throws IOException {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_047");
		System.out.println(" Starting test - tc_REG_MUK_047");

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

			// Clicking on Add Product to basket button
			click("addToBasket_btn_xpath");
			wait(2);

			// Validating the Video Popup
			if (getElementsSize("stokee_videoPopup_xpath") > 0) {
				// scrollDown(0, 200);
				// wait(2);
				size = driver.findElements(By.tagName("iframe")).size();
				for (int i = 0; i < size; i++) {
					driver.switchTo().frame(i);
					wait(1);
					
					if (getElementsSize("stokke_VideoConfirm_checkBox_xpath") > 0) {
						click("stokke_VideoConfirm_checkBox_xpath");
						wait(2);
						click("stokke_SubmitVideoConfirm_button_xpath");
						break;
					}
					else{
						driver.switchTo().defaultContent();
					}

				}

				// clicking the basket button
				click("shoppingCart_Link_xpath");
				wait(2);

				scrollDown(0, 300);
				wait(1);

				if (driver.findElements(
						By.xpath("//a[contains(@title,'Stokke')]")).size() > 0) {
					reportPass("Stokke Product has been successfully added after watching the video");
				} else {
					reportFailure("Unable to add the stokke product into the basket");
				}
			} else {
				reportFailure("Video Pop up is not getting displayed");
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_047");
		System.out.println(" Ending test - tc_REG_MUK_047");

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
