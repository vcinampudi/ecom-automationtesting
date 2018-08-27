package com.selenium.mothercare.testcases.SuiteCartPage_Desktop;

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
/* Steps:
1. Load MUK site
2. Search and add the product to basket
3. Navigate to basket page
4. Validate Total price
*/
public class TC_REG_MUK_080 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_80(Hashtable<String, String> data) {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_80");
		System.out.println(" Starting test - tc_REG_MUK_80");

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
		scrollDown(0, 300);
		String totalPrice = getElement("cartPage_TotalAmount_xpath").getText();
		String deliveryCharge = getElement("cartPage_delChargeAmnt_xpath")
				.getText();
		String subTotal = getElement("cartPage_subTotalAmnt_xpath").getText();
		System.out.println("Sub Total:"
				+ subTotal.substring(1, subTotal.length()));
		System.out.println("Delivery Charge: "
				+ deliveryCharge.substring(1, deliveryCharge.length()));
		System.out.println("Total: "
				+ totalPrice.substring(1, totalPrice.length()));
		// Validation
/*
		System.out.println("Total Int:"
				+ Double.parseDouble(totalPrice.substring(1,
						totalPrice.length())));
		System.out.println("SubTotal Int:"
				+ Double.parseDouble(subTotal.substring(1, subTotal.length())));
		System.out.println("Del Int:"
				+ Double.parseDouble(deliveryCharge.substring(1,
						deliveryCharge.length())));
		System.out.println(Double.parseDouble(subTotal.substring(1, subTotal.length()))+Double.parseDouble(deliveryCharge.substring(1,
						deliveryCharge.length())));*/
		
		
		  if(Double.parseDouble(totalPrice.substring(1,
		  totalPrice.length()))==(Double.parseDouble(subTotal.substring(1, subTotal.length()))+Double.parseDouble(deliveryCharge.substring(1,
					deliveryCharge.length())))){
		  reportPass("Total Amount is displaying correctly"); } 
		  else
		  reportFailure("Total Amount is not displayong correctly");
		 

	}

	@BeforeTest
	public void checkTestRunnable() {
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_80");
		System.out.println(" Ending test - tc_REG_MUK_80");

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
