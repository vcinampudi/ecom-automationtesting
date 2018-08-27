package com.selenium.mothercare.testcases.SuiteDeliveryOption_Desktop;

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
 2. Search furniture product
 3. Verify delivery option on PDP
 */
public class TC_REG_MUK_073 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_073(Hashtable<String, String> data) {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_073");
		System.out.println(" Starting test - tc_REG_MUK_073");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}
		
		//searching furniture product
		type("searchProduct_input_xpath", data.get("FurnitureProduct"));
		pressEnter("searchProduct_input_xpath");
		
		if(getElementsSize("acceptCookies_btn_xpath")>0)
		{
			click("acceptCookies_btn_xpath");
		}
		
		scrollDown(0, 450); 
		
		//verifying availability of furniture delivery option
		
		if(getElementsSize("furnitureDeliveryOption_text_xpath")>0)
		{
			WebElement furnitureDeliveryOption = getElement("furnitureDeliveryOption_text_xpath");
			String furnitureDeliveryText = furnitureDeliveryOption.getAttribute("class");
			
			System.out.println("Furniture Delivery text: " + furnitureDeliveryText);
			
			if (furnitureDeliveryText.contains("two_man"))
			{
				System.out.println("Two man delivery option is available for the product and successfully displayed on the PDP");
				reportPass("Two man delivery option is available for the product and successfully displayed on the PDP");
			}
			else
			{
				System.out.println("Two man delivery option is not available for the product");
				reportFailure("Two man delivery option is not available for the product");
			}
		}
		else
		{
			reportFailure("Furniture delivery option is not available for the product.");
		}
		
	}

	@BeforeTest
	public void checkTestRunnable() {
		xls = new Xls_Reader(Constants.SUITE_DELIVERYOPTION_XLS_PATH);
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_073");
		System.out.println(" Ending test - tc_REG_MUK_073");

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
