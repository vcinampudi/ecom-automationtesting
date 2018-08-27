package com.selenium.mothercare.testcases.SuiteEditBasket_Desktop;

import java.util.Hashtable;
import java.util.List;

import org.openqa.selenium.By;
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
4. Select different color variant and click on update basket button
5. Validate variant on basket page
*/
public class TC_REG_MUK_088 extends TestBase {

	String testCaseName = this.getClass().getSimpleName();
	SoftAssert softAssert;
	Xls_Reader xls;
	String selectedColor = null;

	@Test(dataProvider = "getData")
	public void tc_REG_MUK_088(Hashtable<String, String> data) {

		test.log(LogStatus.INFO, " Starting test - tc_REG_MUK_088");
		System.out.println(" Starting test - tc_REG_MUK_088");

		if (data.get(Constants.COL_RUNMODENAME).equals(Constants.RUNMODE_NO)) {
			test.log(LogStatus.SKIP,
					"Skipping the DataSet as Runmode is set to NO");
			throw new SkipException(
					"Skipping the DataSet as Runmode is set to NO");
		}

		
		// Adding Product to basket
		addToBasket(data.get("ColorVariantProduct"));

		// clicking the basket button
		click("shoppingCart_Link_xpath");
		
		wait(2);
		
		scrollDown(0, 400);
		
		//clicking edit basket link
		click("editBasketLink_xpath");
		
		wait(2);
		
		//changing the color variant of the product
		
		List<WebElement> colorVariants = driver.findElements(By.xpath(prop.getProperty("colorSwatch_xpath")));
		
		System.out.println("No. of in stock color variant products: " + colorVariants.size());
		
		if((colorVariants.size()) > 0)
		{
			for(int i = 1; i < colorVariants.size(); i++ )
			{
				selectedColor = driver.findElement(By.xpath("//li[@class='b-swatches_item']/*[1]")).getAttribute("title");
				System.out.println("Color: " + selectedColor);
				colorVariants.get(i).click();
				wait(4);
				break;
			}
		}
		else
		{
			System.out.println("No other color variant is available to select!");
			reportFailure("No other color variant is available to select!");
		}
		
		//getting product name
		String productNamePDP = getElement("productName_PDP_text_xpath").getText();
		System.out.println("Product name on PDP: " + productNamePDP);
		
		//clicking update basket button
		click("updateBasket_btn_xpath");
		
		//validating whether the basket is updated correctly with selected color
		//String basketProductColor = getElement("basketColor_xpath").getText();
		//System.out.println("basket Product Color: " + basketProductColor);
		
		String productNameBasket = getElement("productName_basket_text_xpath").getText();
		System.out.println("Product name on basket: " + productNameBasket);
		if(productNamePDP.equals(productNameBasket))
		{
			System.out.println("Basket updated correctly with selected color!");
			reportPass("Basket updated correctly with selected color!");
		}
		else
		{
			System.out.println("Basket is failed to update correctly with selected color!");
			reportFailure("Basket is failed to update correctly with selected color!");
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

		test.log(LogStatus.INFO, " Ending test - tc_REG_MUK_088");
		System.out.println(" Ending test - tc_REG_MUK_088");

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
