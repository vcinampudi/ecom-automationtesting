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
 2. customer login
 3. Search product, add to basket and continue to checkout
 4. Verify all delivery method availability 
 */
public class TC_REG_MUK_109 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_109(Hashtable<String, String> data) {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_109");
		System.out.println(" Starting test - tc_REG_MUK_109");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}
		
		doLogin(data.get("Username"), data.get("Password"));
		
		//adding product to basket
		addToBasket(data.get("Product"));
		
		// clicking the basket button
		click("shoppingCart_Link_xpath");
		wait(5);	
		//clicking secure checkout button
		click ("checkout_Btn_xpath");
		
		wait(5);
		
		scrollDown(0, 500);
		
		// getting click and collect radio button
		WebElement clickAndCollectRadioBtn = getElement("clickandcollect_radioBtn_xpath");
		System.out.println("Check if click and collect radio button is selected:" + clickAndCollectRadioBtn.isSelected());
		
		//getting UK Standard radio button
		WebElement uKStandardRadioBtn = getElement("deliveryToUK_radioBtn_xpath");
		System.out.println("Check if UK Standard radio button is selected:" + uKStandardRadioBtn.isSelected());
		
		//getting International radio button
		WebElement internationalRadioBtn = getElement("internationalDelivery_radioBtn_xpath");
		System.out.println("Check if international radio button is selected:" + internationalRadioBtn.isSelected());
		
		//getting Bfpo radio button
		WebElement bfpoRadioBtn = getElement("bfpo_radioBtn_xpath");
		System.out.println("Check if bfpo radio button is selected:" + bfpoRadioBtn.isSelected());
				
		//Check if delivery options are displayed and non selected
		if(clickAndCollectRadioBtn.isSelected()!= true && uKStandardRadioBtn.isSelected()!= true && internationalRadioBtn.isSelected()!= true && bfpoRadioBtn.isSelected()!= true)
		{
			System.out.println("All delivery options are displayed and are not selected by default.");
			reportPass("All delivery options are displayed and are not selected by default.");
		}
		else
		{
			System.out.println("All delivery options are not displayed as unselected");
			reportFailure("All delivery options are not displayed as unselected");
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_109");
		System.out.println(" Ending test - tc_REG_MUK_109");

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
