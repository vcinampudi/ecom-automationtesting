package com.selenium.mothercare.testcases.SuitePLP_Desktop;

import java.io.IOException;
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
/* Steps:
 1. Load MUK site
 2. Navigate to PLP
 3. Check view details button availability and click on it
 */
public class TC_REG_MUK_064 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;
	String oldPrice = null, selectedColor = null;
	Boolean Flag = false;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_064(Hashtable<String, String> data)
			throws IOException {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_064");
		System.out.println(" Starting test - tc_REG_MUK_064");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}

		// Accepting the cookie policies
		//click("acceptCookies_btn_xpath");

		// Clicking on Clothing category menu item
		click("MegaNav_Clothing_menuItem_xpath");

		scrollDown(0, 600);

		wait(2);
		// Clicking on baby category
		click("clp_Cothing_baby_text_xpath");
		wait(2);

		if (getElementsSize("plp_Category_Refinement_text_xpath") > 0) {

			// Clicking on Premature sub category from refinements
			click("baby_premature_Link_xpath");
			wait(2);
			if (getElementsSize("plp_premature_text_xpath") > 0) {
				if (getElementsSize("plp_ViewDetails_button_xpath") > 0) {
					List<WebElement> element = driver.findElements(By
							.xpath("//span[.='view details']"));
					element.get(1).click();
					wait(3);
					if (getElementsSize("plp_ViewDetailsPopup_xpath") > 0) {
						reportPass("View Details popup Displayed");
					} else {
						reportFailure("View details are not working");
					}

				} else {
					reportFailure("View details button is not available");
				}
			} else {
				reportFailure("Refinements are not working");
			}
		} else {
			reportFailure("Product Refinements are not available");
		}

	}

	@BeforeTest
	public void checkTestRunnable() throws IOException {
		xls = new Xls_Reader(Constants.SUITE_PLP_XLS_PATH);
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_064");
		System.out.println(" Ending test - tc_REG_MUK_064");

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
