package com.selenium.core.base;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
//import java.io.File;
import java.io.*;
//import java.io.FileOutputStream;
//import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.management.DescriptorKey;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.*;
/*import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;*/
import org.eclipse.jetty.util.HttpCookieStore.Empty;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.*;
/*import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;*/
import org.testng.Assert;
import org.testng.SkipException;

import com.beust.jcommander.Parameter;
//import com.gargoylesoftware.htmlunit.WebClient;
import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.selenium.core.util.Constants;
import com.selenium.core.util.ExtentManager;
import com.selenium.core.util.Xls_Writer;

public class TestBase {

	public WebDriver driver;
	// public EventFiringWebDriver eventDriver;
	// public EventHandler handler;
	public Properties prop;
	public Alert alert;
	public ExtentReports rep = ExtentManager.getInstance();
	public ExtentTest test;
	private static ExtentReports extent;
	public String downloadPath;
	public Xls_Writer xlsWriter = null;

	public void init() {

		// Initialize the project properties file
		if (prop == null) {
			prop = new Properties();
			try {
				// rep.addSystemInfo("Browser",
				// prop.getProperty("browserType"));
				FileInputStream fs = new FileInputStream(
						System.getProperty("user.dir")+ "//src//test//resources//project.properties");
				prop.load(fs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// initialize xlsWriter object
		if (xlsWriter == null) {
			try {
				xlsWriter = new Xls_Writer(Constants.TEST_ORDERS_XLS);
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	public void openBrowser(String BrowserType) {
		// NOTE: Set downloaded directory to reports folder to allow TeamCity to
		// download files
		downloadPath = System.getProperty("user.dir") + "\\reports\\downloadedFiles";

		String logFilePath = System.getProperty("user.dir") + "\\logs";
		// System.out.println("File download path is set to: " + downloadPath);

		// File file = new File(downloadPath);
		// int numOfFIles = file.listFiles().length;
		// System.out.println(file.listFiles().length);

		// System.out.println(prop.get("appUrl"));
		String browser = BrowserType.toLowerCase().trim();

		if (browser.equals("MOZILLA".toLowerCase().trim())) {
			FirefoxProfile firefoxProfile = new FirefoxProfile();
			firefoxProfile.setPreference("browser.download.folderList", 2);
			firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);
			firefoxProfile.setPreference("browser.download.dir", downloadPath);
			firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk",
					"text/csv,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/x-excel, application/x-msexcel, application/excel, application/vnd.ms-excel");

			driver = new FirefoxDriver(firefoxProfile);
			
		} else if (browser.equals("CHROME".toLowerCase().trim())) {
			System.setProperty("webdriver.chrome.driver", Constants.CHROMEDRIVER_LOCATION);

			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("download.default_directory", downloadPath);
			chromePrefs.put("profile.default_content_setting_values.notifications", 2);
			chromePrefs.put("credentials_enable_service", false);
			chromePrefs.put("profile.password_manager_enabled", false);

			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs);
			options.addArguments("test-type");
			options.addArguments("disable-popup-blocking");
			options.addArguments("no-sandbox");

			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			/*
			 * //ChromeOptions options = new ChromeOptions();
			 * //options.addArguments("test-type");
			 * 
			 * DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			 * //capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			 */
			driver = new ChromeDriver(capabilities);

			// driver = new ChromeDriver();
		} else if (browser.equals("IE".toLowerCase().trim())) {
			System.setProperty("webdriver.ie.driver", Constants.IEDRIVER_LOCATION);
			System.setProperty("webdriver.ie.driver.loglevel", "INFO");
			System.setProperty("webdriver.ie.driver.logfile", logFilePath + "\\log" + new Date() + ".log");

			DesiredCapabilities capabilities = DesiredCapabilities.edge();
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

			driver = new InternetExplorerDriver(capabilities);

		} else if (browser.equals("MOZILLAGECKO".toLowerCase().trim())) {
			System.setProperty("webdriver.gecko.driver", Constants.GECKODRIVER_LOCATION);

			FirefoxProfile firefoxProfile = new FirefoxProfile();
			firefoxProfile.setPreference("browser.download.folderList", 2);
			firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);
			firefoxProfile.setPreference("browser.download.dir", downloadPath);
			firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk",
					"text/csv,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/x-excel, application/x-msexcel, application/excel, application/vnd.ms-excel");

			driver = new FirefoxDriver(firefoxProfile);
		}

		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		/*
		 * eventDriver=new EventFiringWebDriver(driver); handler=new EventHandler();
		 * eventDriver.register(handler);
		 */
	}

	public void navigate(String urlKey) {
		test.log(LogStatus.INFO, "Navigating to - " + prop.getProperty("appUrl"));
		System.out.println("Navigating to - " + prop.getProperty("appUrl"));
		driver.get(prop.getProperty("appUrl"));
		wait(1);
		// Inputting Authentication credentials
		Robot handle;
		try {
			handle = new Robot();

			StringSelection user = new StringSelection(prop.getProperty("authUsrName"));
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(user, null);
			handle.keyPress(KeyEvent.VK_CONTROL);
			handle.keyPress(KeyEvent.VK_V);
			handle.keyRelease(KeyEvent.VK_V);
			handle.keyRelease(KeyEvent.VK_CONTROL);
			handle.keyPress(KeyEvent.VK_TAB);
			handle.keyRelease(KeyEvent.VK_TAB);
			wait(2);
			StringSelection pwd = new StringSelection(prop.getProperty("authPasswrd"));
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(pwd, null);
			handle.keyPress(KeyEvent.VK_CONTROL);
			handle.keyPress(KeyEvent.VK_V);
			handle.keyRelease(KeyEvent.VK_V);
			handle.keyRelease(KeyEvent.VK_CONTROL);
			handle.keyPress(KeyEvent.VK_ENTER);
			handle.keyRelease(KeyEvent.VK_ENTER);
			// Closing Home PAge Banner
			wait(30);
			System.out.println("Waiting to display Home Page Banner");
			if (getElementsSize("areYou_banner_xpath") > 0) {
				click("areYou_bannerCloseBtn_xpath");

			}
			if (getElementsSize("acceptCookies_btn_xpath") > 0) {
				click("acceptCookies_btn_xpath");
			}

		} catch (AWTException e) {

			e.printStackTrace();
		}
	}

	public void click(String locatorKey) {

		test.log(LogStatus.INFO, "Clicking: " + locatorKey);
		System.out.println("Clicking: " + locatorKey);
		// driver.findElement(By.xpath(prop.getProperty(xpathKey))).click();
		try {
			getElement(locatorKey).click();
			wait(2);
		} catch (Exception e) {
			System.out.println("Not able to click on element - " + locatorKey + " OR element not visible currently. - "
					+ e.getMessage());
			reportFailure("Not able to click on element - " + locatorKey + " OR element not visible currently. - "
					+ e.getMessage());
		}
	}

	public void submit(String locatorKey) {

		test.log(LogStatus.INFO, "Clicking: " + locatorKey);
		System.out.println("Clicking: " + locatorKey);
		// driver.findElement(By.xpath(prop.getProperty(xpathKey))).click();
		try {
			getElement(locatorKey).submit();
			wait(2);
		} catch (Exception e) {
			System.out.println("Not able to click on element - " + locatorKey + " OR element not visible currently. - "
					+ e.getMessage());
			reportFailure("Not able to click on element - " + locatorKey + " OR element not visible currently. - "
					+ e.getMessage());
		}
	}

	public void type(String locatorKey, String data) {
		test.log(LogStatus.INFO, "Sending [" + data + "] data from Keyboard to [" + locatorKey + "]");

		System.out.println("Sending [" + data + "] data from Keyboard to [" + locatorKey + "]");
		// driver.findElement(By.xpath(prop.getProperty(xpathKey))).sendKeys(data);
		try {
			getElement(locatorKey).sendKeys(data);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
		}
		wait(2);
	}

	// THIS FUNCTION SEND KEYS USING JAVASCRIPT EXECUTOR
	public void typeUsingJavaScript(String inputValue, String locatorKey) {

		try {
			JavascriptExecutor myExecutor = ((JavascriptExecutor) driver);
			myExecutor.executeScript(
					"document.getElementById('" + prop.getProperty(locatorKey) + "').value='+" + inputValue + "';");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// TO CLEAR THE INPUT FIELDS
	public void clear(String locatorKey) {
		test.log(LogStatus.INFO, "Clearing the following field - " + locatorKey);
		System.out.println("Clearing the following field - " + locatorKey);

		try {
			getElement(locatorKey).clear();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
		}
	}

	public void pressEnter(String locatorKey) {
		test.log(LogStatus.INFO, "Sending Enter key to [" + locatorKey + "] from Keyboard");
		System.out.println("Sending Enter key to [" + locatorKey + "] from Keyboard");
		// driver.findElement(By.xpath(prop.getProperty(xpathKey))).sendKeys(data);
		try {
			getElement(locatorKey).sendKeys(Keys.ENTER);
			wait(1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
		}
	}

	// PRESS DOWN ARROW KEY
	public void pressDown(String locatorKey) {
		test.log(LogStatus.INFO, "Sending Enter key to [" + locatorKey + "] from Keyboard");
		System.out.println("Sending Enter key to [" + locatorKey + "] from Keyboard");
		// driver.findElement(By.xpath(prop.getProperty(xpathKey))).sendKeys(data);
		try {
			getElement(locatorKey).sendKeys(Keys.ARROW_DOWN);
			wait(2);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
		}
	}

	public void pressEsc(String locatorKey) {
		test.log(LogStatus.INFO, "Sending Escape key to [" + locatorKey + "] from Keyboard");
		System.out.println("Sending Escape key to [" + locatorKey + "] from Keyboard");
		// driver.findElement(By.xpath(prop.getProperty(xpathKey))).sendKeys(data);
		try {
			getElement(locatorKey).sendKeys(Keys.ESCAPE);
			wait(1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
		}
	}

	public void pressBackspace(String locatorKey) {
		test.log(LogStatus.INFO, "Sending Backspace key to [" + locatorKey + "] from Keyboard");
		System.out.println("Sending Backspace key to [" + locatorKey + "] from Keyboard");
		try {
			getElement(locatorKey).sendKeys(Keys.BACK_SPACE);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
		}
	}

	public void pressTab(String locatorKey) {
		test.log(LogStatus.INFO, "Sending Tab key-data to [" + locatorKey + "] from Keyboard");
		System.out.println("Sending Tab key-data to [" + locatorKey + "] from Keyboard");
		// driver.findElement(By.xpath(prop.getProperty(xpathKey))).sendKeys(data);
		try {
			getElement(locatorKey).sendKeys(Keys.TAB);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
		}

	}

	public void pressEnterAndWait(String locator_clicked, String locator_presence) {
		test.log(LogStatus.INFO, "Clicking and waiting on - " + locator_clicked);
		System.out.println("Clicking and waiting on - " + locator_clicked);
		int count = 5;
		try {
			for (int i = 0; i < count; i++) {
				getElement(locator_clicked).sendKeys(Keys.ENTER);
				wait(2);
				if (isElementPresent(locator_presence))
					break;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
		}
	}

	public WebElement getElement(String locatorKey) {

		test.log(LogStatus.INFO, "Finding element - " + locatorKey);
		System.out.println("Finding element - " + locatorKey);

		WebElement element = null;
		try {
			if (locatorKey.endsWith("_xpath")) {
				element = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
			} else if (locatorKey.endsWith("_id")) {
				element = driver.findElement(By.id(prop.getProperty(locatorKey)));
			} else if (locatorKey.endsWith("_linkText")) {
				element = driver.findElement(By.linkText(prop.getProperty(locatorKey)));
			} else if (locatorKey.endsWith("_name")) {
				element = driver.findElement(By.name(prop.getProperty(locatorKey)));
			} else if (locatorKey.endsWith("_partialLinkText")) {
				element = driver.findElement(By.partialLinkText(locatorKey));
			} else {
				element = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
			}

		} catch (Exception e) {
			// FAIL THE TEST AND REPORT THE ERROR
			reportFailure(e.getMessage());
			System.out.println(e.getMessage());
			// Assert.fail("Failed the Test " + e.getMessage());
		}

		return element;

	}

	// RETURN LIST OF ELEMENTS
	public List<WebElement> getElements(String locatorKey) {

		test.log(LogStatus.INFO, "Finding element - " + locatorKey);
		System.out.println("Finding element - " + locatorKey);

		List<WebElement> elements = null;
		try {
			if (locatorKey.endsWith("_xpath")) {
				elements = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
			} else if (locatorKey.endsWith("_id")) {
				elements = driver.findElements(By.id(prop.getProperty(locatorKey)));
			} else if (locatorKey.endsWith("_linkText")) {
				elements = driver.findElements(By.linkText(prop.getProperty(locatorKey)));
			} else if (locatorKey.endsWith("_name")) {
				elements = driver.findElements(By.name(prop.getProperty(locatorKey)));
			} else if (locatorKey.endsWith("_partialLinkText")) {
				elements = driver.findElements(By.partialLinkText(locatorKey));
			} else {
				elements = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
			}

		} catch (Exception e) {
			// FAIL THE TEST AND REPORT THE ERROR
			reportFailure(e.getMessage());
			System.out.println(e.getMessage());
			// Assert.fail("Failed the Test " + e.getMessage());
		}

		return elements;

	}

	// THIS FUNCTION WILL GET THE VALUE OF THE HTML ATTRIBUTES
	public String getAttributeValue(String locatorKey, String attributeName) {

		test.log(LogStatus.INFO,
				"Getting attribute value for locator - " + locatorKey + ", and Attribute Name - " + attributeName);
		System.out.println(
				"Getting attribute value for locator - " + locatorKey + ", and Attribute Name - " + attributeName);

		try {
			String attributeValue;
			attributeValue = getElement(locatorKey).getAttribute(attributeName);
			return attributeValue;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
			return null;
		}

	}

	public void wait(int timeToWaitInSec) {

		// test.log(LogStatus.INFO, "Waiting for " + timeToWaitInSec +
		// " seconds");
		// System.out.println("Waiting for " + timeToWaitInSec + " seconds");

		try {
			Thread.sleep(timeToWaitInSec * 1000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	public void waitForPageToLoad() {

		test.log(LogStatus.INFO, "Waiting for page to Load");
		System.out.println("Waiting for page to Load");
		wait(1);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String state = (String) js.executeScript("return document.readyState");

		while (!state.equals("complete")) {
			wait(2);
			state = (String) js.executeScript("return document.readyState");
		}
	}

	public void clickAndWait(String locator_clicked, String locator_presence) {
		test.log(LogStatus.INFO, "Clicking and waiting on - " + locator_clicked);
		System.out.println("Clicking and waiting on - " + locator_clicked);
		int count = 8;
		try {
			for (int i = 0; i < count; i++) {
				getElement(locator_clicked).click();
				wait(2);
				if (isElementPresent(locator_presence))
					break;
			}
		} catch (Exception e) {
			System.out.println("Not able to click on element - " + locator_clicked
					+ " OR element not visible currently. - " + e.getMessage());
			reportFailure("Not able to click on element - " + locator_clicked + " OR element not visible currently. - "
					+ e.getMessage());
		}
	}

	// Get the text of the attribute
	public String getText(String locatorKey) {
		test.log(LogStatus.INFO, "Getting text from [" + locatorKey + "]");
		System.out.println("Getting text from [" + locatorKey + "]");
		return getElement(locatorKey).getText();

	}

	// GET THE TITLE OF THE PAGE
	public String getWindowTitle() {
		test.log(LogStatus.INFO, "Getting window title");
		System.out.println("Getting window title");
		return driver.getTitle();
	}

	// THIS FUNCTION WILL REMOVE THE FIRST N NUMBER OF CHARACTERS FROM STRING
	public String removeFirstChars(String string, int numOfChars) {
		try {
			return string.trim().substring(numOfChars);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
			return null;
		}
	}

	// THIS FUNCTION GIVES NUMBER OF FILES IN A FOLDER
	public int listNumOfFiles(String folderPath) {

		test.log(LogStatus.INFO, "Inside listNumOfFiles function. Fetching number of files in - " + folderPath);
		System.out.println("Inside listNumOfFiles function. Fetching number of files in - " + folderPath);

		try {
			File file = new File(folderPath);
			int numOfFiles = file.listFiles().length;
			return numOfFiles;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
			return 0;
		}

	}

	// ******************** VALIDATION FUNCTIONS *********************** //
	public boolean checkStatusCorrect(String expectedStatus) {
		test.log(LogStatus.INFO, "Verifying asset status is expected: " + expectedStatus);
		System.out.println("Verifying asset status is expected: " + expectedStatus);

		String currentStatus = getElement("current_workflow_status").getText();
		test.log(LogStatus.INFO, "Current status: " + currentStatus);
		System.out.println("Current status: " + currentStatus);
		if (currentStatus.equals(expectedStatus)) {
			test.log(LogStatus.INFO, "Assets statuses match: " + expectedStatus + ", " + currentStatus);
			System.out.println("Assets statuses match: " + expectedStatus + ", " + currentStatus);
			return true;
		} else {
			test.log(LogStatus.INFO, "Status mismatch - expected status: '" + expectedStatus
					+ "' while current status: '" + currentStatus + "'");
			System.out.println("Status mismatch - expected status: '" + expectedStatus + "' while current status: '"
					+ currentStatus + "'");

			return false;
		}
	}

	// VERIFIES THAT CORRECT OPTION IS AVAILABLE IN ACTION BUTTON
	// get rid of this terrible method, why did i even write it
	public boolean verifyOptionAvailable(String expectedOption) {

		// only max three options available per status
		String[] buttonOption_xpaths = { ".//*[@id='actionBtn0']", ".//*[@id='actionBtn1']",
				".//*[@id='actionViewButton0']", ".//*[@id='actionViewButton1']" };

		for (String s : buttonOption_xpaths) {
			try {
				String text = driver.findElement(By.xpath(s)).getText();
				if (text.equals(expectedOption)) {
					test.log(LogStatus.INFO, "Expected option " + expectedOption + "is available.");
					System.out.print("Expected option '" + expectedOption + "' isn't available.");
					wait(1);
					return true;
				}
			} catch (NoSuchElementException e) {
				System.out.print("Unable to locate " + s);
			}

		}
		test.log(LogStatus.INFO, "Expected option " + expectedOption + "isn't available.");
		System.out.print("Expected option " + expectedOption + "isn't available.");
		wait(1);
		return false;
	}

	// TO VERIFY IF ELEMENT IS PRESENT ON PAGE OR NOT
	public boolean isElementPresent(String locatorKey) {

		test.log(LogStatus.INFO, "Verifying if element is present - " + locatorKey);
		System.out.println("Verifying if element is present - " + locatorKey);

		List<WebElement> elementList = null;
		try {
			if (locatorKey.endsWith("_id"))
				elementList = driver.findElements(By.id(prop.getProperty(locatorKey)));
			else if (locatorKey.endsWith("_name"))
				elementList = driver.findElements(By.name(prop.getProperty(locatorKey)));
			else if (locatorKey.endsWith("_xpath"))
				elementList = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
			else {
				elementList = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
				// System.out.println("Locator not correct - " + locatorKey);
				// reportFailure("Locator not correct - " + locatorKey);
				// Assert.fail("Locator not correct - " + locatorKey);
			}

			if (elementList.size() == 0) {
				test.log(LogStatus.INFO, "Element is not present - " + locatorKey);
				System.out.println("Element is not present - " + locatorKey);
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
			return false;
		}
	}

	// TO COMPARE TEXT AND VERIFY
	public boolean verifyText(String locatorKey, String expectedTextKey) {

		String actualText = getElement(locatorKey).getText().trim();
		String expectedText = prop.getProperty(expectedTextKey).trim();

		test.log(LogStatus.INFO,
				"Verifying texts equality - Actual Text : " + actualText + " . Expected Text : " + expectedText);
		System.out.println(
				"Verifying texts equality - Actual Text : " + actualText + " . Expected Text : " + expectedText);

		try {
			if (actualText.equals(expectedText))
				return true;
			else
				return false;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
			return false;
		}
	}

	// TO VERIFY TEXT PRESENT ON PAGE OR NOT
	public boolean verifyTextPresent(String textToVerify) {
		test.log(LogStatus.INFO, "Verifying text present on page - " + textToVerify);
		System.out.println("Verifying text present on page - " + textToVerify);

		try {
			if (driver.getPageSource().contains(textToVerify)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("Text not present on page - " + textToVerify + "--" + e.getMessage());
			reportFailure("Text not present on page - " + textToVerify + "--" + e.getMessage());
			return false;
		}
	}

	public void expandTreeElement(String elementToExpand) {
		test.log(LogStatus.INFO, "Expanding element " + elementToExpand);
		System.out.println("Expanding element " + elementToExpand);
		String assetTypeExpandBtnXpath = "//span[.='" + elementToExpand + "']/../preceding-sibling::span";
		driver.findElement(By.xpath(assetTypeExpandBtnXpath)).click();
		wait(1);
	}

	// TO CHECK IF SEARCHED TEXT IS PRESENT IN TREE
	public boolean isValuePresentInTree(String valueToSearch) {
		try {
			String search_xpath = "//span[contains(text(),'" + valueToSearch + "')]";
			test.log(LogStatus.INFO, "Checking existence of [" + valueToSearch + "] in [" + search_xpath + "]");
			System.out.println("Checking existence of [" + valueToSearch + "] in [" + search_xpath + "]");
			int size = driver.findElements(By.xpath(search_xpath)).size();

			if (size != 0) {
				System.out.println(valueToSearch + "Found in" + search_xpath);
				test.log(LogStatus.INFO, valueToSearch + "Found in" + search_xpath);
				return true;
			} else {
				System.out.println(valueToSearch + "Not Found in" + search_xpath);
				test.log(LogStatus.INFO, valueToSearch + "Not Found in" + search_xpath);
				return false;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
			test.log(LogStatus.INFO, "Exiting isValurPressentInTree function");
			return false;
		}

	}

	// TO GET THE URL OF THE PAGE
	public String getUrl() {

		try {
			String url = driver.getCurrentUrl();
			test.log(LogStatus.INFO, "Current url is - " + url);
			System.out.println("Current url is - " + url);
			return url;
		} catch (Exception e) {
			System.out.println("Unable to get the URL - " + e.getMessage());
			reportFailure("Unable to get the URL - " + e.getMessage());
			return null;
		}
	}

	// TO GET THE URL OF THE PAGE
	public void navigateToUrl(String url) {

		test.log(LogStatus.INFO, "Navigating to url - " + url);
		System.out.println("Navigating to url - " + url);

		try {
			driver.navigate().to(url);
		} catch (Exception e) {
			System.out.println("Unable to navigate to the URL - " + e.getMessage());
			reportFailure("Unable to navigate to the URL - " + e.getMessage());
		}
	}

	// REFRESH THE PAGE
	public void refresh() {
		System.out.println("Refreshing the page");
		driver.navigate().refresh();
	}

	// THIS FUNCTION WILL HOVER THE MOUSE ON SPECIFIC WEBELEMENT AND CLICK ON
	// THE VALUE INSIDE IT
	public void mouseHoverAndClick(String locatorKey, String attributeToSelect) {
		test.log(LogStatus.INFO, "Hovering mouse on element: " + locatorKey + " and selecting " + attributeToSelect);
		System.out.println("Hovering mouse on element: " + locatorKey + " and selecting " + attributeToSelect);

		try {
			String xpathToUse = "//span[.='" + attributeToSelect + "']";
			System.out.println(xpathToUse);
			wait(1);
			Actions act = new Actions(driver);
			act.moveToElement(getElement(locatorKey)).build().perform();
			act.moveToElement(getElement(locatorKey)).click().build().perform();
			wait(1);
			act.moveToElement(getElement(locatorKey)).click().build().perform();
			wait(1);
			WebElement el1 = driver.findElement(By.xpath(xpathToUse));
			act.moveToElement(el1).click().build().perform();
			wait(1);
		} catch (Exception e) {
			System.out.println(Constants.FIND_ELEMENT_ERROR + "Value to Hover: " + prop.getProperty("locatorKey")
					+ e.getMessage());
			reportFailure(Constants.FIND_ELEMENT_ERROR + "Value to Hover: " + prop.getProperty("locatorKey")
					+ e.getMessage());
		}
	}

	// THIS FUNCTION WILL HOVER THE MOUSE ON SPECIFIC WEBELEMENT.
	public void mouseHoverAndClickwithLocatorKey(String locatorKey, String itemLocatorKey) {
		test.log(LogStatus.INFO, "Hovering mouse on element: " + locatorKey + " and selecting " + itemLocatorKey);
		System.out.println("Hovering mouse on element: " + locatorKey + " and selecting " + itemLocatorKey);

		try {
			System.out.println(prop.getProperty(itemLocatorKey));
			wait(1);
			Actions act = new Actions(driver);
			act.moveToElement(getElement(locatorKey)).build().perform();
			act.moveToElement(getElement(locatorKey)).click().build().perform();
			wait(1);
			act.moveToElement(getElement(locatorKey)).click().build().perform();
			wait(1);

			act.moveToElement(getElement(itemLocatorKey)).click().build().perform();
			wait(1);
		} catch (Exception e) {
			System.out.println(Constants.FIND_ELEMENT_ERROR + "Value to Hover: " + prop.getProperty("locatorKey")
					+ e.getMessage());
			reportFailure(Constants.FIND_ELEMENT_ERROR + "Value to Hover: " + prop.getProperty("locatorKey")
					+ e.getMessage());
		}
	}

	// THIS FUNCTION WILL MAKE THE PAGE TO SCROLL DOWN TO THE BOTTOM
	public void scrollToBottomofAPage() { /*
											 * driver.navigate().to(URL+ "directory/companies?trk=hb_ft_companies_dir"
											 * ); WebElement element = driver.findElement
											 * (By.linkText("Import/Export"));
											 */

		test.log(LogStatus.INFO, "Scrolling to the bottom of the page");
		System.out.println("Scrolling to the bottom of the page");
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	// THIS FUNCTION WILL MAKE THE PAGE TO SCROLL UP
	public void scrollUp() { /*
								 * driver.navigate().to(URL+ "directory/companies?trk=hb_ft_companies_dir" );
								 * WebElement element = driver.findElement (By.linkText("Import/Export"));
								 */

		test.log(LogStatus.INFO, "Scrolling to the top of the page");
		System.out.println("Scrolling to the top of the page");
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(0,-250)", "");
	}

	// THIS FUNCTION WILL SCROLL THE PAGE AS PER USERS INPUT OF PIXELS
	public void scrollDown(int position1, int position2) {
		test.log(LogStatus.INFO, "Scrolling to the bottom of the page");
		System.out.println("Scrolling to the bottom of the page");
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(" + position1 + "," + position2 + ")", "");

	}

	/*
	 * // GET RESPONSE CODE FROM THE PAGE public int getStatusCode(String pageUrl)
	 * throws IOException { WebClient webClient = new WebClient(); int code =
	 * webClient.getPage(pageUrl).getWebResponse().getStatusCode();
	 * webClient.closeAllWindows(); return code; }
	 */

	// THIS FUNCTION WLL GET THE SIZE OF ELEMENTS
	public int getElementsSize(String locatorKey) {
		int size = 0;
		try {
			if (locatorKey.endsWith("_xpath")) {
				size = driver.findElements(By.xpath(prop.getProperty(locatorKey))).size();
			} else if (locatorKey.endsWith("_id")) {
				size = driver.findElements(By.id(prop.getProperty(locatorKey))).size();
			} else if (locatorKey.endsWith("_linkText")) {
				size = driver.findElements(By.linkText(prop.getProperty(locatorKey))).size();
			} else if (locatorKey.endsWith("_name")) {
				size = driver.findElements(By.name(prop.getProperty(locatorKey))).size();
			} else if (locatorKey.endsWith("_partialLinkText")) {
				size = driver.findElements(By.partialLinkText(locatorKey)).size();
			} else {
				System.out.println("Locator not correct - " + locatorKey);
				reportFailure("Locator not correct - " + locatorKey);
				Assert.fail("Locator not correct - " + locatorKey);
			}
		} catch (Exception e) {
			// FAIL THE TEST AND REPORT THE ERROR
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
			// e.printStackTrace();
			// Assert.fail("Failed the Test " + e.getMessage());
		}
		return size;
	}

	// THIS FUNCTION WILL ACCEPT THE ALERTS
	public void acceptAlert() {

		test.log(LogStatus.INFO, "Accepting alert");
		System.out.println("Accepting alert");

		try {
			alert = driver.switchTo().alert();
			alert.accept();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
		}
	}

	// THIS FUNCTION WILL DISMISS THE ALERTS
	public void dismissAlert() {

		test.log(LogStatus.INFO, "Dismissing the alert");
		System.out.println("Dismissing the alert");

		try {
			alert = driver.switchTo().alert();
			alert.dismiss();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
		}
	}

	// GET CURRENT DATE IN DD/MM/YYYY FORMAT
	public String getCurrentDate() {

		test.log(LogStatus.INFO, "Getting Current Date");
		System.out.println("Getting Current Date");

		try {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");

			/*
			 * String strDate = sdf.format(date);
			 * System.out.println("formatted date in mm/dd/yy : " + strDate);
			 */

			sdf = new SimpleDateFormat("dd/MM/yyyy");
			String strDate = sdf.format(date);
			return strDate;
			// System.out.println("formatted date in dd/MM/yyyy : " + strDate);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
			return null;
		}

	}

	// THIS FUNCTION RETURNS TOMORROWS DATE
	public String getTomorrowsDate() {
		try {
			Calendar calObject = Calendar.getInstance();
			calObject.add(Calendar.DAY_OF_YEAR, 1);
			String tomorrowDate = new SimpleDateFormat("dd/MM/yyyy").format(calObject.getTime());
			System.out.println("Tomorrow's Date :" + tomorrowDate);
			return tomorrowDate;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
			return null;
		}
	}

	// ***************** REPORTING FUNCTIONS ***********************//

	// REPORT PASS
	public void reportPass(String message) {
		System.out.println("Pass : " + message);
		test.log(LogStatus.PASS, message);
		takeScreenshot();
	}

	// REPORT FAIL
	public void reportFailure(String message) {
		System.out.println("Fail : " + message);
		test.log(LogStatus.FAIL, message);
		takeScreenshot();
		Assert.fail(message);
	}

	// REPORT SKIP
	public void reportSkip(String message) {
		System.out.println("Skip : " + message);
		test.log(LogStatus.SKIP, message);
		takeScreenshot();
		throw new SkipException(message);
	}

	// REPORT INFO

	public void reportInfo(String message) {
		System.out.println("Info : " + message);
		test.log(LogStatus.INFO, message);
		takeScreenshot();

	}

	public void takeScreenshot() {
		// fileName of the screenshot
		Date d = new Date();
		String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
		// store screenshot in that file
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile,
					new File(System.getProperty("user.dir") + "//reports//screenshots//" + screenshotFile));
			// FileUtils.copyFile(scrFile, new
			// File(System.getProperty("user.dir")+ "//screenshots//" +
			// screenshotFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// put screenshot file in reports
		// test.log(LogStatus.INFO, "Screenshot-> " +
		// test.addScreenCapture(System.getProperty("user.dir") +
		// "//reports//screenshots//" + screenshotFile));
		test.log(LogStatus.INFO, "Screenshot-> " + test.addScreenCapture("./screenshots/" + screenshotFile));
	}

	// ********************** APP FUNCTIONS ********************************* //

	public boolean doLogin(String username, String password) {
		test.log(LogStatus.INFO, " Trying to login with username - " + username + " & password - " + password);
		System.out.println(" Trying to login with username - " + username + " & password - " + password);

		try {

			click("signInLink_xpath");
			// click("acceptCookies_btn_xpath");
			// scrollDown(0, 470);
			scrollToElement("login_submit_btn_xpath");
			type("login_username_input_xpath", username);
			type("login_password_input_xpath", password);
			click("login_submit_btn_xpath");
			wait(3);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
			// test.log(LogStatus.FAIL, e.getMessage());

		}

		try {
			if (isElementPresent("logoutLink_xpath")) {
				test.log(LogStatus.INFO, " Login is successful");
				return true;
			} else {
				test.log(LogStatus.INFO, " Login Failed");
				return false;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
			// test.log(LogStatus.FAIL, e.getMessage());
			return false;
		}
	}

	public boolean doLoginUnRegUser(String username, String password) {
		test.log(LogStatus.INFO, " Trying to login with username - " + username + " & password - " + password);
		System.out.println(" Trying to login with username - " + username + " & password - " + password);

		try {

			scrollDown(0, 470);
			type("login_username_input_xpath", username);
			type("login_password_input_xpath", password);
			click("login_submit_btn_xpath");
			wait(3);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
			// test.log(LogStatus.FAIL, e.getMessage());

		}

		try {
			if (isElementPresent("deliveryDetails_Text_xpath")) {
				test.log(LogStatus.INFO, " Login is successful");
				return true;
			} else {
				test.log(LogStatus.INFO, " Login Failed");
				return false;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
			// test.log(LogStatus.FAIL, e.getMessage());
			return false;
		}
	}

	public void doLogout() {
		test.log(LogStatus.INFO, "Logging out");
		System.out.println("Logging out");

		try {
			click("logout_image_xpath");
			wait(2);
			click("logout_image_logout_xpath");
			wait(5);
		} catch (Exception e) {
			test.log(LogStatus.INFO, e.getMessage());
			System.out.println(e.getMessage());
		}
	}

	// THIS FUNCTION WILL UPLOAD THE FILE AT GIVEN LOCATION USER HAS TO PROVIDE
	// LOCATOR AND FILENAME WITH EXTENSION

	public void uploadFile(String locaterKey, String fileName) {
		test.log(LogStatus.INFO, "Starting File: " + fileName + " Uploading");
		wait(2);

		try {
			String fileToUploadPath = System.getProperty("user.dir") + "\\executioninfo\\testFilesToUpload\\"
					+ fileName;
			File f = new File(fileToUploadPath);
			if (f.exists() && !f.isDirectory()) {
				System.out.println(System.getProperty("user.dir") + "\\executioninfo\\testFilesToUpload\\" + fileName);
				getElement(locaterKey)
						.sendKeys(System.getProperty("user.dir") + "\\executioninfo\\testFilesToUpload\\" + fileName);
				wait(5);
				test.log(LogStatus.INFO, "File Uploading Completed");
			} else {
				reportFailure("File to upload: [" + fileToUploadPath + "] does not exists");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(fileName + " File is not getting uploaded: " + e.getMessage());
			reportFailure(fileName + " File is not getting uploaded: " + e.getMessage());
		}
	}

	// THIS FUNCTION WILL CLICK ON SEARCHED ENTITY
	public void clickOnSearchedEntity(String valueToSelect) {
		test.log(LogStatus.INFO, "Executing clickOnSearchedEntity: " + valueToSelect + " function");
		System.out.println("Executing clickOnSearchedEntity: " + valueToSelect + " function");

		try {
			String xpathToUse = "//div[contains(text(),'" + valueToSelect + "')]";
			driver.findElement(By.xpath(xpathToUse)).click();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
		}
		wait(4);
	}

	// SELECT MODULE FROM HOME PAGE
	public void selectValueFromList(String valueToSelect) {
		test.log(LogStatus.INFO, "Selecting: " + valueToSelect);
		System.out.println("Selecting: " + valueToSelect);
		wait(2);
		try {
			String valueToSelect_xpath = "//li[.='" + valueToSelect + "']";
			System.out.println("valueToSelect_xpath " + valueToSelect_xpath);
			driver.findElement(By.xpath(valueToSelect_xpath)).click();
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure(Constants.FIND_ELEMENT_ERROR + "Value to Select - " + valueToSelect);
		}
		wait(3);
		test.log(LogStatus.INFO, "Exiting selectValue function");
		System.out.println("Exiting selectValue function");
	}

	// SELECT FL TYPE FROM HOME PAGE
	public void selectFLTypeFromList(String valueToSelect) {
		test.log(LogStatus.INFO, "Selecting: " + valueToSelect);
		System.out.println("Selecting: " + valueToSelect);
		wait(2);
		try {
			String valueToSelect_xpath = "//ul[@id='GroupAssetType_listbox']/li[.='" + valueToSelect + "']";
			System.out.println("valueToSelect_xpath " + valueToSelect_xpath);
			driver.findElement(By.xpath(valueToSelect_xpath)).click();
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure(Constants.FIND_ELEMENT_ERROR + "FL Type to Select - " + valueToSelect);
		}
		wait(3);
		test.log(LogStatus.INFO, "Exiting selectFLTypeFromList function");
		System.out.println("Exiting selectFLTypeFromList function");
	}

	// SELECT ASSET FROM LIST
	public void selectAsset(String assetName) {
		test.log(LogStatus.INFO, "Executing selectAsset: " + assetName);
		System.out.println("Executing selectAsset: " + assetName);
		wait(2);
		try {
			String valueToSelect_xpath = "//a[.='" + assetName + "']";
			driver.findElement(By.xpath(valueToSelect_xpath)).click();
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure(Constants.FIND_ELEMENT_ERROR + " valueToSelect_xpath");
		}
		wait(3);
		test.log(LogStatus.INFO, "Exiting selectAsset function");
		System.out.println("Exiting selectAsset function");
	}

	// SELECT VALUES FROM TREE
	public void selectValueFromTree(String valueToSelect) {
		test.log(LogStatus.INFO, "Executing selectValueFromTree: " + valueToSelect);
		System.out.println("Executing selectValueFromTree: " + valueToSelect);
		wait(2);

		if (valueToSelect.contains("Created")) {

			try {
				String valueToSelect_xpath = "//span[.='" + valueToSelect + " (Created)']";
				driver.findElement(By.xpath(valueToSelect_xpath)).click();
			} catch (Exception e) {
				e.printStackTrace();
				reportFailure(Constants.FIND_ELEMENT_ERROR + "Value to Select from Tree - " + valueToSelect);
			}

		}

		try {
			String valueToSelect_xpath = "//span[.='" + valueToSelect + "']";
			driver.findElement(By.xpath(valueToSelect_xpath)).click();
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure(Constants.FIND_ELEMENT_ERROR + "Value to Select from Tree - " + valueToSelect);
		}
		wait(3);
		test.log(LogStatus.INFO, "Exiting selectValueFromTree function");
		System.out.println("Exiting selectValueFromTree function");
	}

	// DOUBLE CLICK ON VALUE FROM TREE

	public void doubleClickOnTreeValue(String valueToSelect) {
		test.log(LogStatus.INFO, "Executing selectValueFromTree: " + valueToSelect);
		System.out.println("Executing selectValueFromTree: " + valueToSelect);
		wait(2);

		try {
			String valueToSelect_xpath = "//span[.='" + valueToSelect + "']";
			Actions act = new Actions(driver);
			act.doubleClick(driver.findElement(By.xpath(valueToSelect_xpath))).build().perform();
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure(Constants.FIND_ELEMENT_ERROR + "Value to Click from Tree - " + valueToSelect);
		}
		wait(3);
		test.log(LogStatus.INFO, "Exiting selectValueFromTree function");
		System.out.println("Exiting selectValueFromTree function");
	}

	// TO CHECK IF CLASS/MENU OPTION IS DISABLED
	public boolean isOptionDisabled(String ModuleName) {
		test.log(LogStatus.INFO, "Verifying if Locator/Option: " + ModuleName + " is Disabled");
		System.out.println("Verifying if Locator/Option: " + ModuleName + " is Disabled");

		try {
			String valueToSelect_xpath = "//li[.='" + ModuleName + "']";
			if (driver.findElement(By.xpath(valueToSelect_xpath)).getAttribute("class").contains("disabled"))
				return true;
			else
				return false;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
			return false;
		}
	}

	// TO SELECT TEXT FROM TREE
	public void selectValueFromTreeList(String valueToSelect) {
		test.log(LogStatus.INFO, "Executing selectValueFromTreeList: " + valueToSelect);
		System.out.println("Executing selectValueFromTreeList: " + valueToSelect);
		wait(2);
		try {
			String valueToSelect_xpath = "//span[contains(text(),'" + valueToSelect + "')]";
			driver.findElement(By.xpath(valueToSelect_xpath)).click();
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure(Constants.FIND_ELEMENT_ERROR + "Value to Select from Tree - " + valueToSelect);
		}
		wait(3);
		test.log(LogStatus.INFO, "Exiting selectValueFromTreeList function");
		System.out.println("Exiting selectValueFromTreeList function");
	}

	// TO VERIFY IF SEARCHED ITEMS ARE PRESENT IN SEARCH GRID
	public boolean isSearchedValuePresent(String valueToSearch, Boolean isQuickSearchModule) {
		try {
			String search_xpath = null;
			if (isQuickSearchModule) {
				search_xpath = "//input[@id='SearchText'][contains(text(),'" + valueToSearch + "')]";
			} else {
				search_xpath = "//div[contains(text(),'" + valueToSearch + "')]";
			}
			test.log(LogStatus.INFO, "Checking existence of [" + valueToSearch + "] in [" + search_xpath + "]");
			System.out.println("Checking existence of [" + valueToSearch + "] in [" + search_xpath + "]");
			int size = driver.findElements(By.xpath(search_xpath)).size();
			if (size != 0) {
				test.log(LogStatus.INFO, "Existence of [" + valueToSearch + "] in [" + search_xpath
						+ "] FOUND; Exiting isSearchedValuePresent function");
				return true;
			} else
				test.log(LogStatus.INFO, "Existence of [" + valueToSearch + "] in [" + search_xpath
						+ "] NOT FOUND; Exiting isSearchedValuePresent function");
			System.out.println("Existence of [" + valueToSearch + "] in [" + search_xpath
					+ "] NOT FOUND; Exiting isSearchedValuePresent function");
			return false;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			reportFailure(e.getMessage());
			test.log(LogStatus.INFO, "Exiting isSearchedValuePresent function");
			return false;
		}
	}

	// THIS FUNCTION WILL SEARCH FRO AN ASSET AND THEN SELECT IT
	public void searchAndSelectAsset(String assetName) {

		test.log(LogStatus.INFO, "Inside - searchAndSelectAsset function");
		System.out.println("Inside - searchAndSelectAsset function");
		type("searchBox_homepage_xpath", assetName);
		wait(2);
		click("searchBox_searchIcon_xpath");
		String loadingIconXpath = "//span[.='Loading...']";
		WebDriverWait wait = new WebDriverWait(driver, 60);
		// wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(loadingIconXpath)));
		// wait(5);

		test.log(LogStatus.INFO, "Searching for Asset - " + assetName);
		System.out.println("Searching for Asset - " + assetName);

		String search_xpath = "//div[contains(text(),'" + assetName + "')]";

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(search_xpath)));
		} catch (Exception e1) {
			System.out.println("Searched Asset \"" + assetName + "\" not found - " + e1.getMessage());
			reportFailure("Searched Asset \"" + assetName + "\" not found - " + e1.getMessage());
		}

		// wait(5);
		try {
			int size = driver.findElements(By.xpath(search_xpath)).size();

			if (size != 0) {
				test.log(LogStatus.INFO, assetName + " - Asset Found");
				driver.findElement(By.xpath(search_xpath)).click();
				wait(5);
			} else {
				test.log(LogStatus.INFO, "Searched Asset is not Present");
				reportFailure("Searched Asset is not Present");
			}
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure(e.getMessage());
		}
		test.log(LogStatus.INFO, "Exiting - searchAndSelectAsset function");
		System.out.println("Exiting - searchAndSelectAsset function");

	}

	// THIS FUNCTION WILL SELECT THE COMPONENT FROM ASSET HIERARCHY
	public void selectComponent(String componentName) {

		test.log(LogStatus.INFO, "Inside - selectComponent function");
		System.out.println("Inside - selectComponent function");

		String xpathToUse = "//span[contains(text(),'" + componentName + "')]";

		try {
			if (driver.findElements(By.xpath(xpathToUse)).size() != 0) {
				driver.findElement(By.xpath(xpathToUse)).click();
			} else {
				test.log(LogStatus.INFO, "Component Name provided is not associated with the Asset");
				System.out.println("Component Name provided is not associated with the Asset");
				reportFailure("Fail: Component Name provided is not associated with the Asset");
			}

		} catch (Exception e) {
			e.printStackTrace();
			reportFailure(e.getMessage());
		}

		test.log(LogStatus.INFO, "Exiting - selectComponent function");
		System.out.println("Exiting - selectComponent function");

	}

	// CLICKS INCREASE/DECREASE n AMOUNT OF TIMES
	public void increaseIntBox(String locator, String delta) {
		int digits = (int) Double.parseDouble(delta);
		for (int i = 0; i < digits; i++) {
			String xpath = prop.getProperty(locator);
			driver.findElement(By.xpath(xpath)).click();
		}
	}

	// SEARCH TABLE OF RESULTS
	public boolean isSearchedValuePresentInTable(String valueToSearch) {
		try {
			String search_xpath = "//td[contains(text(),'" + valueToSearch + "')]";
			test.log(LogStatus.INFO, "Checking existence of [" + valueToSearch + "] in [" + search_xpath + "]");
			System.out.println("Checking existence of [" + valueToSearch + "] in [" + search_xpath + "]");
			int size = driver.findElements(By.xpath(search_xpath)).size();
			if (size != 0) {
				test.log(LogStatus.INFO, "Existence of [" + valueToSearch + "] in [" + search_xpath
						+ "] FOUND; Exiting isSearchedValuePresentInTable function");
				return true;
			} else {
				test.log(LogStatus.INFO, "Existence of [" + valueToSearch + "] in [" + search_xpath
						+ "] NOT FOUND; Exiting isSearchedValuePresentInTable function");
				System.out.println("Existence of [" + valueToSearch + "] in [" + search_xpath
						+ "] NOT FOUND; Exiting isSearchedValuePresentInTable function");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure(e.getMessage());
			test.log(LogStatus.INFO, "Exiting isSearchedValuePresentInTable function");
			return false;
		}
	}

	// SEARCH FOR VALUE IN TABLE (WHERE ONLY ONE TABLE EXISTS, AND UNIQUE VALUE
	// IS IN FIRST COLUMN)
	public void searchValueInTable(String valueToSearch) {
		searchValueInTable(valueToSearch, "grid", 1);
		// try {
		// wait(2);
		// driver.findElement(
		// By.xpath(".//*[@id='grid']/table/thead/tr/th[1]/a[1]/span"))
		// .click();
		// wait(1);
		// driver.findElement(By.xpath("html/body/div/form/div[1]/input[1]"))
		// .sendKeys(valueToSearch);
		// } catch (Exception e) {
		// e.printStackTrace();
		// reportFailure(e.getMessage());
		// test.log(LogStatus.INFO,
		// "Failure - unable to locate search button.");
		// }
		//
		// driver.findElement(
		// By.xpath("html/body/div/form/div[1]/div[2]/button[1]")).click();
		// wait(3);
	}

	// SEARCH FOR VALUE IN TABLE (WHERE > ONE TABLE EXISTS, AND UNIQUE VALUE IS
	// IN FIRST COLUMN)
	public void searchValueInTable(String valueToSearch, String gridLocator) {
		searchValueInTable(valueToSearch, gridLocator, 1);

		// try {
		// String xpath = ".//*[@id='" + gridLocator
		// + "']/table/thead/tr/th[1]/a[1]/span";
		// wait(2);
		// driver.findElement(By.xpath(xpath)).click();
		// wait(1);
		// driver.findElement(By.xpath("html/body/div/form/div[1]/input[1]"))
		// .sendKeys(valueToSearch);
		// } catch (Exception e) {
		// e.printStackTrace();
		// reportFailure(e.getMessage());
		// test.log(LogStatus.INFO,
		// "Failure - unable to locate search button.");
		// }
		//
		// driver.findElement(
		// By.xpath("html/body/div/form/div[1]/div[2]/button[1]")).click();
		// wait(3);

	}

	// SEARCH FOR VALUE IN TABLE WHERE UNIQUE VALUE IS NOT IN FIRST COLUMN AND
	// => 1 TABLE EXISTS
	public void searchValueInTable(String valueToSearch, String gridLocator, int columnNumber) {
		try {
			String xpath = ".//*[@id='" + gridLocator + "']/table/thead/tr/th[" + columnNumber + "]/a[1]/span";
			wait(2);
			driver.findElement(By.xpath(xpath)).click();
			wait(1);
			driver.findElement(By.xpath("html/body/div/form/div[1]/input[1]")).sendKeys(valueToSearch);
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure(e.getMessage());
			test.log(LogStatus.INFO, "Failure - unable to locate search button.");
		}

		driver.findElement(By.xpath("html/body/div/form/div[1]/div[2]/button[1]")).click();
		wait(3);

	}

	// TO VERIFY IF VALUE IS PRESENT IN LIST
	public boolean isValueDisplayedInList(String valueToSearch) {

		try {
			String search_xpath = "//li[.='" + valueToSearch + "']";
			// System.out.println(search_xpath);
			test.log(LogStatus.INFO, "Checking existence of [" + valueToSearch + "] in [" + search_xpath + "]");
			System.out.println("Checking existence of [" + valueToSearch + "] in [" + search_xpath + "]");
			boolean displayFlag = driver.findElement(By.xpath(search_xpath)).isDisplayed();
			if (displayFlag) {
				test.log(LogStatus.INFO, "Existence of [" + valueToSearch + "] in [" + search_xpath
						+ "] FOUND; Exiting isSearchedValuePresentInList function");
				System.out.println("Existence of [" + valueToSearch + "] in [" + search_xpath
						+ "] FOUND; Exiting isSearchedValuePresentInList function");
				return true;
			} else {
				test.log(LogStatus.INFO, "Existence of [" + valueToSearch + "] in [" + search_xpath
						+ "] NOT FOUND; Exiting isSearchedValuePresentInList function");
				System.out.println("Existence of [" + valueToSearch + "] in [" + search_xpath
						+ "] NOT FOUND; Exiting isSearchedValuePresentInList function");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure(e.getMessage());
			test.log(LogStatus.INFO, "Exiting isSearchedValuePresentInList function");
			return false;
		}

	}

	// TO VERIFY IF SEARCHED ITEMS ARE PRESENT IN SEARCH GRID

	public void selectSearchedValue(String selectValue) {
		try {
			String search_xpath = "//div[contains(text(),'" + selectValue + "')]";

			test.log(LogStatus.INFO, "Checking existence of [" + selectValue + "] in [" + search_xpath + "]");
			System.out.println("Checking existence of [" + selectValue + "] in [" + search_xpath + "]");
			driver.findElement(By.xpath(search_xpath)).click();
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure(e.getMessage());
		}
		wait(2);
		test.log(LogStatus.INFO, "Exiting selectSearchedValue function");
		System.out.println("Exiting selectSearchedValue function");
	}

	// TO VERIFY TEXT FROM TREE STRUCTURES
	public boolean verifyTextFromTree(String searchableText) {
		test.log(LogStatus.INFO, "Verify if Text: " + searchableText + " is present in Tree Hirarchy");
		System.out.println("Verify if Text: " + searchableText + " is present in Tree Hirarchy");

		String xpathToUse = "//span[.='" + searchableText + "']";
		System.out.println(xpathToUse);

		try {
			if (driver.findElements(By.xpath(xpathToUse)).size() != 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure(e.getMessage());
			return false;
		}
	}

	// TO VERIFY TEXT FROM TREE STRUCTURES WITH DROP DOWNS (CURRENTLY ONLY ASSET
	// TYPE, SUBTYPE)
	// UNSURE WHY THIS IS RUNNING SO SLOWLY...
	public boolean verifyTextFromExpandableTree(String parent, String child) {
		test.log(LogStatus.INFO, "Verify if Text: " + child + " is present in Tree Hierarchy");
		System.out.println("Verify if Text: " + child + " is present in Tree Hierarchy");

		for (int i = 1; i < 100; i++) {
			try {
				WebElement element = driver
						.findElement(By.xpath(".//*[@id='treeNameLabel']/ul/li[" + i + "]/div/span[2]"));
				if (element.getText().equals(parent)) {
					driver.findElement(By.xpath(".//*[@id='treeNameLabel']/ul/li[" + i + "]/div/span[1]")).click();
					return verifyTextPresent(child);
				}
			} catch (Exception e) {
				continue;
			}
		}
		return false;
	}

	/*
	 * This function will take the text which user want to search in table and will
	 * verify if text is present or not
	 */
	public boolean verifyTextFromTable(String searchableText) {
		test.log(LogStatus.INFO, "Verify if Text is present in Table - " + searchableText);
		System.out.println("Verify if Text is present in Table - " + searchableText);

		try {
			if (driver.findElements(By.xpath("//td[.='" + searchableText + "']")).size() != 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure(e.getMessage());
			return false;
		}
	}

	// THIS FUNCTION WILL ALLOW WAIT UNTIL THE INVISIBILITY OF THE ELEMENT
	public void waitUntilNotVisible(String locator) {

		test.log(LogStatus.INFO, "Waiting for element to be invisible - " + locator);
		System.out.println("Waiting for element to be invisible - " + locator);

		try {
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(prop.getProperty(locator))));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// WAIT UNTI ELEMENT IS PRESENT

	public void waitUntilElementPresent(String locator) {

		test.log(LogStatus.INFO, "Waiting for element to be visible - " + locator);
		System.out.println("Waiting for element to be visible - " + locator);

		try {
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(prop.getProperty(locator))));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	//
	public void selectTextFromDropDown(String dropDownLocator, String valueToSelect) {
		test.log(LogStatus.INFO, "Selecting Value: " + valueToSelect + " from Drop Down");
		System.out.println("Selecting Value: " + valueToSelect + " from Drop Down");
		wait(1);
		try {
			click(dropDownLocator);
			wait(2);
			driver.findElement(By.xpath(".//ul/li[text() = '" + valueToSelect + "']")).click();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(Constants.FIND_ELEMENT_ERROR + "Value to Select from Drop Down: " + valueToSelect);
			reportFailure(Constants.FIND_ELEMENT_ERROR + "Value to Select from Drop Down: " + valueToSelect);
		}
	}

	// THIS FUNCTION IS SELECTING ITEM FORM DROP DOWN WITH SELECT TAG
	public void selectValueFromDropDown(String locatorKey, String valueToSelect) {
		test.log(LogStatus.INFO, "Selecting Value: " + valueToSelect + " from Drop Dowm");
		System.out.println("Selecting Value: " + valueToSelect + " from Drop Dowm");

		wait(2);
		try {
			Select sel = new Select(driver.findElement(By.xpath(prop.getProperty(locatorKey))));
			wait(2);

			sel.selectByVisibleText(valueToSelect);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(Constants.FIND_ELEMENT_ERROR + "Value to Select from Drop Down: " + valueToSelect);
			reportFailure(Constants.FIND_ELEMENT_ERROR + "Value to Select from Drop Down: " + valueToSelect);
		}
		wait(2);
		test.log(LogStatus.INFO, "Exiting selectValueFromDropDown function");
		System.out.println("Exiting selectValueFromDropDown function");
	}

	// TO SELECT FINANCIAL YEAR FROM TREE
	public void selectFinancialYearFromTreeList(String financialYear) {
		test.log(LogStatus.INFO, "Executing selectFinancialYearFromTreeList: " + financialYear);
		test.log(LogStatus.INFO, "Searching for Financial Year - " + financialYear);
		System.out.println("Searching for Financial Year - " + financialYear);
		wait(2);
		try {
			String valueToSelect_xpath = "//span[contains(text(),'" + financialYear + "')]";
			driver.findElement(By.xpath(valueToSelect_xpath)).click();
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure(Constants.FIND_ELEMENT_ERROR + "Financial Year to Select from Tree - " + financialYear);
		}
		wait(3);
		test.log(LogStatus.INFO, "Exiting selectFinancialYearFromTreeList function");
		System.out.println("Exiting selectFinancialYearFromTreeList function");
	}

	// TO VERIFY FINANCIAL YEAR IS SET TO CURRENT YEAR
	public boolean verifyFYSetToCurrentYear(String financialYear) {

		test.log(LogStatus.INFO, "Verifying if financial year - " + financialYear + " is set to Current Year");
		System.out.println("Verifying if financial year - " + financialYear + " is set to Current Year");

		String textToVerify = "Current Financial Year : " + financialYear;

		try {
			if (verifyTextPresent(textToVerify))
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure(
					"Some problem while trying to verify if Financial Year is set to Current : " + e.getMessage());
			return false;

		}

	}

	// VALIDATES IF INFORMATION IS UPDATED OR NOT
	public boolean isDetailsUpdated() {

		test.log(LogStatus.INFO, "Verifying if details are updated");
		System.out.println("Verifying if details are updated");

		try {
			if (isElementPresent("assets_updateDetails_successMsg_xpath"))
				return true;
			else
				return false;
		} catch (Exception e) {
			reportFailure(e.getMessage());
			return false;
		}

	}

	// SELECT THE VALUE FROM THE TABLE
	public void searchAndClickOnValueFromTable(String valueToSelect) {

		int count = 0;
		while (!verifyTextFromTable(valueToSelect) && count < 5) {
			click("workGroupList_nextPage_xpath");
			count++;
		}

		if (count >= 5) {
			// throw new SkipException("Skipped: Component Name not present");
			reportFailure("Fail: Value provided is not present - " + valueToSelect);
		}
	}

	// SEARCH AND ADD PRODUCT TO BASKET

	public void addToBasket(String ProductId) {

		try {

			type("searchProduct_input_xpath", ProductId);
			pressEnter("searchProduct_input_xpath");
			if (getElementsSize("productNotFound_text_xpath") > 0) {
				reportFailure("Not valid product, please input valid product Id");
			} else {
				scrollDown(0, 370);
				if (getElementsSize("productHolderName_input_xpath") > 0) {
					type("productHolderName_input_xpath", Constants.PRODUCT_HOLDER_NAME);
					selectValueFromDropDown("prodctHolderDOB_day_xpath", Constants.PRODUCT_HOLDER_DOB_DAY);
					selectValueFromDropDown("prodctHolderDOB_month_xpath", Constants.PRODUCT_HOLDER_DOB_MONTH);
					selectValueFromDropDown("prodctHolderDOB_year_xpath", Constants.PRODUCT_HOLDER_DOB_YEAR);
				}

				Character cartQuantityBefore = getText("cart_quantity_text_xpath").charAt(0);
				System.out.println("Bef" + cartQuantityBefore);
				/*
				 * if(getElementsSize("acceptCookies_btn_xpath")>0){
				 * click("acceptCookies_btn_xpath"); }
				 */
				click("addToBasket_btn_xpath");
				wait(2);
				Character cartQuantityAfter = getText("cart_quantity_text_xpath").charAt(0);
				System.out.println("After" + cartQuantityAfter);
				wait(2);

				/*
				 * if (Character.getNumericValue(cartQuantityAfter) > Character
				 * .getNumericValue(cartQuantityBefore)) { test.log(LogStatus.INFO,
				 * "Product Added in cart"); } else { test.log(LogStatus.INFO,
				 * "Product Not Added in cart"); reportFailure("Product not added");
				 * 
				 * }
				 */
			}
		} catch (Exception e) {
			reportFailure(e.getMessage());
		}
	}

	// PAY WITH NEW CREDIT CARD

	public void payWithNewCreditCard() {

		try {
			click("creditcardPay_Btn_xpath");

			// switching to iframe
			List<WebElement> frames = driver.findElements(By.tagName("iframe"));
			System.out.println("num of frames" + frames.size());
			driver.switchTo().frame("paymentIframe");
			wait(3);
			clear("creditCard_nameoncard_input_xpath");
			type("creditCard_nameoncard_input_xpath", Constants.FIRST_NAME);
			clear("creditCard_cardnumber_input_xpath");
			type("creditCard_cardnumber_input_xpath", Constants.CARD_NO_VISA);
			selectValueFromDropDown("creditCard_expdate_dropDown_xpath", "07");
			selectValueFromDropDown("creditCard_expyear_dropDown_xpath", "2028");
			driver.switchTo().defaultContent();
			scrollDown(0, 450);
			wait(3);
			driver.switchTo().frame("paymentIframe");
			clear("creditCard_cvv_input_xpath");
			type("creditCard_cvv_input_xpath", Constants.CARD_CVV);
			click("creditCard_savecard_checkBox_xpath");
			click("creditCard_placeorder_btn_xpath");
			wait(2);

		} catch (Exception e) {

			reportFailure(e.getMessage());
		}

	}

	// PAY WITH CREDIT CARD
	/*
	 * @param=use deliver methods as=CnC,UK,bfpo,International
	 */
	public void payWithCreditCard(String deliveryMethod) {

		try {
			if (deliveryMethod.equals(Constants.DELIVERY_OPTION_CnC)) {
				click("creditcardPay_Btn_xpath");
				// scrollDown(0, 1900);
				scrollToElement("addNewAdr_Btn_xpath");
				wait(2);
				click("addNewAdr_Btn_xpath");// Add new billing adr
				type("billingAdrEmail_input_xpath", Constants.BILLING_EMAIL);
				type("billingAdrHouseNum_input_xpath", Constants.BILLING_HOUSE_NUM);
				type("adrSearchPostalCode_input_xpath", Constants.POSTAL_CODE);
				scrollToBottomofAPage();
				click("findAdr_Btn_xpath");
				wait(2);
				click("selectNewBillingAdr_dropDown_xpath");
				pressDown("selectNewBillingAdr_dropDown_xpath");
				pressEnter("selectNewBillingAdr_dropDown_xpath");
				scrollToBottomofAPage();
				wait(5);
				click("submitBill_Btn_xpath");
				// click("first_creditCard_xpath");
				// changed by roopam
				click("savedCard_card_xpath");
				type("creditCard_CVV_input_xpath", Constants.CARD_CVV);
				click("creditCard_placeOrder_Btn_xpath");
				wait(2);
			} else if (deliveryMethod.equals(Constants.DELIVERY_OPTION_BFPO)
					|| deliveryMethod.equals(Constants.DELIVERY_OPTION_INTERNATIONAL)
					|| deliveryMethod.equals(Constants.DELIVERY_OPTION_UK)) {

				click("creditcardPay_Btn_xpath");
				wait(2);

				// switching to iframe
				List<WebElement> frames = driver.findElements(By.tagName("iframe"));
				System.out.println("num of frames" + frames.size());
				driver.switchTo().frame("paymentIframe");

				clear("creditCard_nameoncard_input_xpath");
				type("creditCard_nameoncard_input_xpath", Constants.FIRST_NAME);
				clear("creditCard_cardnumber_input_xpath");
				type("creditCard_cardnumber_input_xpath", Constants.CARD_NO_VISA);
				selectValueFromDropDown("creditCard_expdate_dropDown_xpath", "02");
				selectValueFromDropDown("creditCard_expyear_dropDown_xpath", "2020");
				driver.switchTo().defaultContent();
				scrollDown(0, 450);
				driver.switchTo().frame("paymentIframe");
				clear("creditCard_cvv_input_xpath");
				type("creditCard_cvv_input_xpath", Constants.CARD_CVV);
				click("creditCard_savecard_checkBox_xpath");
				click("creditCard_placeorder_btn_xpath");

				// validating order page
				System.out.println("Order success message " + getText("orderSuccessMessage_text_xpath"));
				if (getText("orderSuccessMessage_text_xpath").equals(Constants.SUCCESS_ORDER_MESSAGE)) {

					reportPass("Order placed successfully!");
				} else {
					reportFailure("Failed to place order!");
				}

			}
		} catch (Exception e) {

			reportFailure(e.getMessage());
		}

	}

	// PAY WITH SAVED CARD
	public void payWithSavedCard(String deliveryMethod) {

		try {
			if (deliveryMethod.equals(Constants.DELIVERY_OPTION_CnC)) {

				/**
				 * BODY
				 * 
				 */

			} else if (deliveryMethod.equals(Constants.DELIVERY_OPTION_BFPO)
					|| deliveryMethod.equals(Constants.DELIVERY_OPTION_INTERNATIONAL)
					|| deliveryMethod.equals(Constants.DELIVERY_OPTION_UK)) {

				click("creditcardPay_Btn_xpath");
				scrollToElement("savedCard_card_xpath");
				wait(2);
				if (getElementsSize("savedCard_card_xpath") > 0) {
					click("savedCard_card_xpath");

					scrollToElement("creditCard_CVV_input_xpath");
					type("creditCard_CVV_input_xpath", Constants.CARD_CVV);

					// clicking place order button
					click("creditCard_placeOrder_Btn_xpath");

				}
				// if there is no saved card
				else {
					click("paywith_NewCreditCard_button_xpath");
					wait(2);
					payWithNewCreditCard();
				}
			}
		} catch (Exception e) {

			reportFailure(e.getMessage());
		}

	}

	// SCROLL TO ELEMENT

	public void scrollToElement(String locatorKey) {

		try {
			Actions action = new Actions(driver);
			action.moveToElement(getElement(locatorKey)).build().perform();
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Not able to find element with locator" + prop.getProperty(locatorKey));
			reportFailure(e.getMessage());
		}

	}

	// ENTER ADDRESS MANUALLY FOR UK STANDARD DELIVERY OPTION
	public void enterAddressManually(String AddressNickname, String AddressLine1, String AddressLine2, String Town,
			String Postcode) {
		try {
			type("addressNickName_input_xpath", AddressNickname);
			type("addLine1_input_xpath", AddressLine1);
			type("addLine2_input_xpath", AddressLine2);

			scrollDown(0, 450);

			type("town_input_xpath", Town);
			type("postcode_input_xpath", Postcode);

			// clicking deliver to this address button
			click("manualDeliverToUKAdd_Btn_xpath");

		} catch (Exception e) {
			reportFailure(e.getMessage());
		}
	}

	// SELECT BFPO DELIVERY OPTION AND PROCEED TO PAYMENT AFTER ADDING NEW BFPO
	// ADDRESS
	public void selectBfpoNewAdd() {
		try {
			wait(2);
			scrollDown(0, 350);
			wait(4);
			// clicking bfpo delivery option
			click("bfpo_radioBtn_xpath");
			wait(5);
			scrollDown(0, 450);

			// checking if user is guest or registered
			if ((isElementPresent("addNewBfpoAdd_btn_xpath"))) {
				// proceed as registered user
				scrollToElement("addNewBfpoAdd_btn_xpath");
				click("addNewBfpoAdd_btn_xpath");
				wait(5);
				scrollDown(0, 800);
				// entering bfpo form mandatory fields
				type("bfpoServiceNumber_input_xpath", Constants.SERVICE_NUMBER);
				type("bfpoUnit_input_xpath", Constants.UNIT);
				type("bfpoNumber_input_xpath", Constants.BFPO_NUMBER);

				// clicking deliver to this address button
				click("deliverToThisAdr_Btn_xpath");
				wait(2);
				// clicking proceed to payment button
				click("bfpoProceedToPayment_btn_xpath");
				wait(2);

			} else {
				// proceed as guest user
				// entering personal information fields
				selectValueFromDropDown("selectTitle_dropDown_xpath", Constants.TITLE);
				scrollDown(0, 400);
				type("bfpoFirstName_input_xpath", Constants.FIRST_NAME);
				type("bfpoLastName_input_xpath", Constants.LAST_NAME);
				// type("bfpoEmail_input_xpath", Constants.EMAIL);
				type("bfpoPhone_input_xpath", Constants.PHONE_NUMBER);

				scrollDown(0, 800);
				// entering bfpo form mandatory fields
				type("bfpoServiceNumber_input_xpath", Constants.SERVICE_NUMBER);
				type("bfpoUnit_input_xpath", Constants.UNIT);
				type("bfpoNumber_input_xpath", Constants.BFPO_NUMBER);

				scrollToBottomofAPage();

				// clicking deliver to this address button
				click("deliverToThisAdr_Btn_xpath");

				// clicking proceed to payment button
				click("bfpoProceedToPayment_btn_xpath");
				wait(2);
			}

		} catch (Exception e) {
			reportFailure(e.getMessage());
		}
	}

	// PAY WITH PAYPAL
	public void payWithPayPal(String email, String password) {
		try {
			scrollDown(0, 250);

			// clicking proceed to payment button
			// click("proceedToPayment_btn_xpath");

			wait(2);
			scrollDown(0, 150);
			// Selecting Pay with PAyPal
			click("payWithPayPal_radioBtn_xpath");
			wait(2);
			scrollDown(0, 150);

			// clicking proceed to PayPal button
			click("proceedToPayPal_btn_xpath");

			wait(6);

			clear("payPal_AdtnlScr_email_input_xpath");
			type("payPal_AdtnlScr_email_input_xpath", email);
			type("payPal_AdtnlScr_password_input_xpath", password);
			click("payPal_payment_btn_xpath");
			wait(15);
			click("payPal_adtnlScr_login_btn_xpath");
			wait(12);

			/*
			 * System.out.println(driver.getWindowHandles().size()); List<WebElement> frames
			 * = driver.findElements(By.tagName("iframe"));
			 * System.out.println("Num of frames: " + frames.size());
			 * 
			 * for (int i = 0; i < frames.size(); i++) { System.out.println("frame id" + i +
			 * " " + frames.get(i).getAttribute("id")); driver.switchTo().frame(
			 * driver.findElements(By.tagName("iframe")).get(i)); if
			 * (getElementsSize("payPal_AdtnlScr_email_input_xpath") > 0) {
			 * clear("payPal_AdtnlScr_email_input_xpath");
			 * type("payPal_AdtnlScr_email_input_xpath", email);
			 * type("payPal_AdtnlScr_password_input_xpath", password);
			 * click("payPal_payment_btn_xpath"); driver.switchTo().defaultContent(); while
			 * ((getElementsSize("payPal_processing_spinner_xpath") > 0)) { wait(18); }
			 * click("payPal_adtnlScr_login_btn_xpath"); wait(2);
			 * 
			 * break; }
			 * 
			 * }
			 */
		} catch (Exception e) {
			reportFailure(e.getMessage());
		}

	}

	// Mouse Over function
	public void mouseHover(String locatorKey) {
		// WebElement element = driver.findElement(By.linkText("Product Category"));
		test.log(LogStatus.INFO, "Hovering mouse on element: " + locatorKey);
		System.out.println("Hovering mouse on element: " + locatorKey);

		try {
			wait(1);
			Actions act = new Actions(driver);
			act.moveToElement(getElement(locatorKey)).build().perform();
		} catch (Exception e) {
			System.out.println(Constants.FIND_ELEMENT_ERROR + "Value to Hover: " + prop.getProperty("locatorKey")
					+ e.getMessage());
			reportFailure(Constants.FIND_ELEMENT_ERROR + "Value to Hover: " + prop.getProperty("locatorKey")
					+ e.getMessage());
		}
	}

}
