package com.selenium.mothercare.testcases.SuiteStoreFinder_Desktop;

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
/*
 Steps:
 1. Click on store finder link
 2. Search store
 3. Apply facility filter and search
 4. Clear filter
 5. View store details
 */
public class TC_REG_MUK_198 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;
	String oldPrice = null, selectedColor = null;
	Boolean Flag = false;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_198(Hashtable<String, String> data)
			throws IOException {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_198");
		System.out.println(" Starting test - tc_REG_MUK_198");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}

		// Accepting the cookie policies
		//click("acceptCookies_btn_xpath");
		 if(getElementsSize("storeFInder_link_xpath")>0){
			 //Looking for Store Finder Link
			 reportPass("StoreFinder Link is displaying");
			 click("storeFInder_link_xpath");
			 wait(3);
			 type("storeFInder_search_input_xpath", data.get("Store"));
			 pressEnter("storeFInder_search_input_xpath");
			 if(getElementsSize("storeNearBy_text_xpath")>0){
				 //Looking for Store search text
				 reportPass("Number of stores text is displaying");
				 click("storeFacility_btn_xpath");
				 scrollDown(0, 200);
				 click("accessiblebabyChanging_text_xpath");
				 scrollDown(0, 500);
				 click("storeFinder_facilities_applyBtn_xpath");
				 if(getElementsSize("accessiblebabyChanging_facility_xpath")>0){
					 reportPass("Facility Applied");
					 click("storeFacility_btn_xpath");
					 click("storeFinder_facilities_clearAllBtn_xpath");
					 click("storeFinder_facilities_applyBtn_xpath");
					 if(getElementsSize("accessiblebabyChanging_facility_xpath")==0){
						 reportPass("Facilities removed successfully");
						 click("firstStoreFinder_btn_xpath");
						 if(getElementsSize("storeFInder_breadcrumbs_xpath")>0){
							 reportPass("All details are displaying fine after selecting Store");
						 }
						 else
							 reportFailure("Details are not displaying fine after selecting Store");
					 }
					 else
						 reportFailure("Facility not removed");
				 }
				 else
					 reportFailure("Facility Not Applied");
				 
			 }
			 else
				 reportFailure("Number of stores text is not displaying");
		 }
		
		

	}

	@BeforeTest
	public void checkTestRunnable() throws IOException {
		xls = new Xls_Reader(Constants.SUITE_STOREFINDER_XLS_PATH);
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_198");
		System.out.println(" Ending test - tc_REG_MUK_198");

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
