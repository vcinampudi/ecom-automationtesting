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
 2. Search and add furniture product to basket
 3. Continue to checkout till delivery option page
 4. Verify furniture delivery option availability
 */
public class TC_REG_MUK_110 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_110(Hashtable<String, String> data) {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_110");
		System.out.println(" Starting test - tc_REG_MUK_110");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}
		
		doLogin(data.get("Username"), data.get("Password"));
		
		//adding product to basket
		addToBasket(data.get("Product"));
		
		//adding furniture product to the basket
		addToBasket(data.get("FurnitureProduct"));
		
		// clicking the basket button
		click("shoppingCart_Link_xpath");
				
		//clicking secure checkout button
		click ("checkout_Btn_xpath");
		
		wait(2);
		
		scrollDown(0, 500);
		
		//selecting UK delivery option
		click("deliveryToUK_radioBtn_xpath");
		
		//selecting address
		if(getElementsSize("selectExistingAdr_btn_xpath")>0)
		{
			click("selectExistingAdr_btn_xpath");
			
			//checking if the furniture delivery method is shown
			
			if(getElementsSize("furnitureDelivery_radioBtn_xpath")>0)
			{
				//check if the estimated delivery date information is displayed
				if(getElementsSize("furnitureDelivery_information_xpath")>0)
				{
					WebElement furnitureDeliveryInfo = getElement("furnitureDelivery_information_xpath");
					wait(3);
					String furnitureDeliveryEstimationText = furnitureDeliveryInfo.getText();
					System.out.println("Furniture delivery estimated dates:" + furnitureDeliveryEstimationText);
					if (furnitureDeliveryEstimationText.contains(Constants.FURNITURE_DELIVERY_ESTIMATED_DELIVERY_DAYS))
					{
						System.out.println("Furniture delivery estimated days displayed correctly");
						reportPass("Furniture delivery estimated days displayed correctly");
					}
					else
					{
						System.out.println("Furniture delivery estimated days text is not displayed");
						reportFailure("Furniture delivery estimated days text is not displayed");
					}
				}
				else
				{
					System.out.println("Estimated delivery date and furniture delivery information is not displayed.");
					reportFailure("Estimated delivery date and furniture delivery information is not displayed.");
				}
			}
			else
			{
				System.out.println("Furniture delivery option is not displayed");
				reportFailure("Furniture delivery option is not displayed");
			}
		}
		else
		{
			scrollToElement("addNewAdr_Btn_xpath");
			
			click("addNewAdr_Btn_xpath");
			
			scrollDown(0, 550);
			
			WebElement phoneNumberField = getElement("bfpoPhone_input_xpath");
			String phoneNumberValue = phoneNumberField.getText();
			if(phoneNumberValue.isEmpty())
			type("bfpoPhone_input_xpath", Constants.PHONE_NUMBER);

			// entering postcode
			type("addressSearch_postCode_xpath", Constants.POSTAL_CODE);

			// clicking find button
			click("findAdr_Btn_xpath");

			wait(2);

			// selecting value from dropdown
			selectValueFromDropDown(
					"selectNewBillingAdr_DeliveryUK_dropDown_xpath",
					Constants.NEW_BILLING_ADR);

			scrollDown(0, 900);

			// clicking deliver to this address button
			click("deliverToThisAdr_Btn_xpath");
			
			wait(2);
			
			//checking if the furniture delivery method is shown
			
			if(getElementsSize("furnitureDelivery_radioBtn_xpath")>0)
			{
				//check if the estimated delivery date information is displayed
				if(getElementsSize("furnitureDelivery_information_xpath")>0)
				{
					WebElement furnitureDeliveryInfo = getElement("furnitureDelivery_information_xpath");
					wait(3);
					String furnitureDeliveryEstimationText = furnitureDeliveryInfo.getText();
					System.out.println("Furniture delivery estimated dates:" + furnitureDeliveryEstimationText);
					if (furnitureDeliveryEstimationText.contains(Constants.FURNITURE_DELIVERY_ESTIMATED_DELIVERY_DAYS))
					{
						System.out.println("Furniture delivery estimated days displayed correctly");
						reportPass("Furniture delivery estimated days displayed correctly");
					}
					else
					{
						System.out.println("Furniture delivery estimated days text is not displayed");
						reportFailure("Furniture delivery estimated days text is not displayed");
					}
				}
				else
				{
					System.out.println("Estimated delivery date and furniture delivery information is not displayed.");
					reportFailure("Estimated delivery date and furniture delivery information is not displayed.");
				}
			}
			else
			{
				System.out.println("Furniture delivery option is not displayed");
				reportFailure("Furniture delivery option is not displayed");
			}
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_110");
		System.out.println(" Ending test - tc_REG_MUK_110");

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
