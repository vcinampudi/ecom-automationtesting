package com.selenium.mothercare.testcases.SuiteCheckout_Desktop;

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

public class TC_UserRegistration extends TestBase {
	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;

	@Test(dataProvider = "getData")
	public void tc_UserRegistration(Hashtable<String, String> data) {

		test.log(LogStatus.INFO, " Starting test - tc_UserRegistration");
		System.out.println(" Starting test - tc_UserRegistration");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}

		// clicking on sign-in Register link
		wait(4);
		click("signInLink_xpath");
		wait(2);
		// Accepting the cookies policies
		//click("acceptCookies_btn_xpath");
		wait(1);
		scrollDown(0, 400);
		wait(2);
		// Selecting customer title
		selectValueFromDropDown("userRegistration_selectTitle_dropDown_xpath",
				data.get("Title"));
		// Filling up the customer details
		type("userRegistration_fname_input_xpath", Constants.FIRST_NAME);
		type("userRegistration_lname_input_xpath", Constants.LAST_NAME);
		type("userRegistration_email_input_xpath", data.get("Username"));
		type("userRegistration_confirmemail_input_xpath", data.get("Username"));
		type("userRegistration_password_input_xpath", data.get("Password"));
		type("userRegistration_confirmpassword_input_xpath",
				data.get("Password"));
		click("userRegistration_email_signup_checkBox_xpath");
		// submitting the form values
		click("userRegistration_createaccount_button_xpath");
		wait(2);

		// validating registered customer

		if (getText("userRegistration_confirmation_text_xpath").equals(
				Constants.SUCCESS_USER_REG_MESSAGE)) {

			reportPass("User registration is successfull!");
		} else {
			reportFailure("Failed to register new user, please verify the customer details");
		}

	}

	@BeforeTest
	public void checkTestRunnable() {
		xls = new Xls_Reader(Constants.SUITE_CHECKOUT_XLS_PATH);
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

		test.log(LogStatus.INFO, " Ending test - tc_UserRegistration");
		System.out.println(" Ending test - tc_UserRegistration");

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
