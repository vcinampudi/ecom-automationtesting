package com.selenium.mothercare.testcases.SuiteHeaderFooter_Desktop;

import java.io.IOException;
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
/*Steps:
 1. Load MUK site
 2. Customer login
 2. Add product to basket
 3. Scroll down and check footer
 */
public class TC_REG_MUK_108 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;
	String oldPrice = null, selectedColor = null;
	Boolean Flag = false;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_108(Hashtable<String, String> data)
			throws IOException {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_108");
		System.out.println(" Starting test - tc_REG_MUK_108");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}
	
		// User Login
		doLogin(data.get("Username"), data.get("Password"));
		
		//adding product to basket
		addToBasket(data.get("Product"));
		scrollToBottomofAPage();
		if(getElementsSize("helpandfaq_xpath")>0){
			reportPass("Footers are present on checkout confirmation page");
		}
		else
			reportFailure("Footers are not present on checkout confirmation page");

	}

	@BeforeTest
	public void checkTestRunnable() throws IOException {
		xls = new Xls_Reader(Constants.SUITE_HEADERFOOTER_XLS_PATH);
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_108");
		System.out.println(" Ending test - tc_REG_MUK_108");

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
	
	public void switchToWindow(){
		
		Set <String>windows=driver.getWindowHandles();
		Iterator itr=windows.iterator();
		String win1=(String) itr.next();
		String win2=(String) itr.next();
		driver.switchTo().window(win2);
		wait(3);
	}

}
