package com.selenium.mothercare.testcases.SuiteMyAccount_Desktop;

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
 2. Customer login
 3. Navigate to My Account -> Order History
 4. Navigate to My Account -> Address Book -> Create New Address
 5. Navigate to My Account -> Payment Details and set preferred card
 */

public class TC_REG_MUK_177 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;
	String oldPrice = null, selectedColor = null;
	Boolean Flag = false;

	@Test(dataProvider = "getData",priority=1)
	public void tc_REG_MUK_177(Hashtable<String, String> data)
			throws IOException {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_177");
		System.out.println(" Starting test - tc_REG_MUK_177");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}

		// Login

		doLogin(data.get("UserName"), data.get("Password"));
		// Click on My Account
		//getElements("myAccount_link_xpath").get(0).click();
		wait(4);
		//scrollDown(0, 650);
		//wait(1);
		click("myAccount_orderHistory_link_xpath");
		

		// Verification
		if (getElementsSize("orderHistory_text_xpath") > 0) {
			reportPass("Order History details are displaying correctly");
		} else
			reportFailure("Order History details are not displaying correctly");

	}
	
	@Test(dataProvider = "getData",priority=2)
	public void tc_REG_MUK_178(Hashtable<String, String> data)
			throws IOException {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_178");
		System.out.println(" Starting test - tc_REG_MUK_178");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}

		// Click on My Account
		getElements("myAccount_link_xpath").get(0).click();
				
		wait(4);
		//scrollDown(0, 650);
		//wait(1);
		//Click on Address Book
		scrollToElement("myAccount_adrBook_link_xpath");
		click("myAccount_adrBook_link_xpath");
		scrollDown(0, 650);
		//Click on add New Adr
		click("adrBook_addNewAdr_link_xpath");
		//Click On UK Adr Radio Button
		click("adrBook_UKAdr_link_xpath");
		

		// Verification
		if (getElementsSize("adrBook_personalInfo_text_xpath") > 0) {
			reportPass("All options to add an address are displaying");
		} else
			reportFailure("options to add an address are not displaying");

	}

	@Test(dataProvider = "getData",priority=3)
	public void tc_REG_MUK_180(Hashtable<String, String> data)
			throws IOException {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_180");
		System.out.println(" Starting test - tc_REG_MUK_180");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}
	
		// Click on My Account
				getElements("myAccount_link_xpath").get(0).click();
				
				wait(4);
			//	scrollDown(0, 650);
			//	wait(1);

		//Click on Payment Book
		click("paymentDetails_link_xpath");
		
		wait(2);

		// Verification
		if (getElementsSize("noCard_text_xpath") > 0) {
			reportFailure("No cards are added please add atleast one card");
		}
		else{
			int numOfCards=getElementsSize("myAccount_paymentCard_list_xpath");
			for(int i=1;i<=numOfCards;i++){
				int counter=0;
				String expiryCardXpath="//ul[@class='b-payment_list']/li["+i+"]/div/div[2]";
				if(driver.findElement(By.xpath(expiryCardXpath)).getAttribute("Class").contains("hidden")){
					String preferredCard="//*[@id='primary']/div[1]/div[3]/div[2]/ul/li["+i+"]/div/div[1]";
					if(driver.findElement(By.xpath(preferredCard)).getAttribute("Class").equals("b-card_status m-preferred")){
						reportInfo("This card is preferred card already");
					}
					else{
						String myPreferredCardCheckBox="//*[@id='primary']/div[1]/div[3]/div[2]/ul/li["+i+"]/div/div[3]/label";
						driver.findElement(By.xpath(myPreferredCardCheckBox)).click();
						wait(2);
						preferredCard="//*[@id='primary']/div[1]/div[3]/div[2]/ul/li["+i+"]/div/div[1]";
						if(driver.findElement(By.xpath(preferredCard)).getAttribute("Class").equals("b-card_status m-preferred")){
							reportPass("Card is preferred now");
							break;
						}
						else
							reportFailure("Card is not changed to preferred");
						
					}
				}
				else{
					reportInfo("Card is expired");
					counter++;
					if(counter==numOfCards){
						reportFailure("All cards are expired please add new card");
					}
				}
				
				
				
			}
		}

	}
	
	
	@BeforeTest
	public void checkTestRunnable() throws IOException {
		xls = new Xls_Reader(Constants.SUITE_MYACCOUNT_XLS_PATH);
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_177,178and180");
		System.out.println(" Ending test - tc_REG_MUK_177,178and180");

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
