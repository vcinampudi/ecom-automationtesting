package com.selenium.mothercare.testcases.SuiteEditBasket_Desktop;

import java.util.Hashtable;

import org.openqa.selenium.WebElement;
import org.testng.Reporter;
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
3. Navigate to basket page and click on edit link
4. Modify quantity and click on update basket button
5. Validate quantity on basket page
*/

public class TC_REG_MUK_071 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_071(Hashtable<String, String> data) {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_071");
		System.out.println(" Starting test - tc_REG_MUK_071");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}

		
		// Adding Product to basket
		addToBasket(data.get("Product"));

		// clicking the basket button
		click("shoppingCart_Link_xpath");
		
		wait(2);
		
		scrollDown(0, 400);
		
		//clicking edit basket link
		click("editBasketLink_xpath");
		
		wait(2);
		
		//increase the quantity
		pressBackspace("quickViewQuantity_input_xpath");
		type("quickViewQuantity_input_xpath", data.get("Quantity"));
		
		//clicking update basket button
		click("updateBasket_btn_xpath");
		
		//validating whether the basket is updated correctly
		
		WebElement quantityTextfield = getElement("basketQuantity_input_xpath");
		System.out.println("Quantity: " + quantityTextfield.getAttribute("value"));
		String Quantity = quantityTextfield.getAttribute("value");
		if(Quantity.equals(data.get("Quantity")))
		{
			System.out.println("Basket updated successfully");
			reportPass("Basket updated successfully");
		}
		else
		{
		System.out.println("Basket failed to update");
		reportFailure("Basket failed to update");
		}
	
		
	}

	@BeforeTest
	public void checkTestRunnable() {
		xls = new Xls_Reader(Constants.SUITE_EDITBASKET_XLS_PATH);
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_071");
		System.out.println(" Ending test - tc_REG_MUK_071");

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
