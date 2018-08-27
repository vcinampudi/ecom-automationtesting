package com.selenium.mothercare.testcases.SuiteCartPage_Desktop;

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
2. Search and add the product to basket
3. Navigate to basket page
4. Click on continue to checkout button
5. Continue with guest user
6. Check total price on order summary section
7. Select any delivery method, add address details and check updated total price in order summary section
*/
public class TC_REG_MUK_112 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;
	String oldTotal=null;
	
	@Test(dataProvider = "getData")
	public void tc_REG_MUK_112(Hashtable<String, String> data)
			throws IOException {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_112");
		System.out.println(" Starting test - tc_REG_MUK_112");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}

		// adding product to basket
		addToBasket(data.get("Product"));

		// clicking the basket button
		click("shoppingCart_Link_xpath");

		// clicking secure checkout button
		click("checkout_Btn_xpath");

		// Selecting guest checkout option
		wait(2);
		scrollDown(0, 350);
		driver.findElement(By.id("dwfrm_singleshipping_email")).sendKeys("test0904@test.com");
		// clicking checkout as guest button
		click("checkoutAsGuest_btn_xpath");
        wait(5);
		
		//Fetching the current charges
		oldTotal=getText("deliveryDetails_Ordersummary_Total_text_xpath").substring(1);
		
		// Selecting Click and Collect delivery Method
		click("clickandcollect_radioBtn_xpath");
		
		// Validating the charges against old charges and new updated charges
				if (!getText("deliveryDetails_Ordersummary_Total_text_xpath").substring(1)
						.equals(oldTotal)) {
					reportPass("Delivery Total has been Updated correctly based on new delivery method "
							+ getText("deliveryDetails_Ordersummary_Total_text_xpath")
									.substring(1)+" Old Total "+oldTotal);
				} else
					reportFailure("Unable to Update the Order Total");

			}


	@BeforeTest
	public void checkTestRunnable() throws IOException {
		xls = new Xls_Reader(Constants.SUITE_CARTPAGE_XLS_PATH);
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_112");
		System.out.println(" Ending test - tc_REG_MUK_112");

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
