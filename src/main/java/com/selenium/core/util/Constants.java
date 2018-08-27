package com.selenium.core.util;

public class Constants {

	// XLS PATHS  

	public static final String TESTSUITE_XLSPATH = System.getProperty("user.dir") + "\\executioninfo\\TestSuite.xlsx";

	public static final String SUITE_CHECKOUT_XLS_PATH = System.getProperty("user.dir") + "\\executioninfo\\SuiteCheckout.xlsx";
	
	public static final String SUITE_CARTPAGE_XLS_PATH = System.getProperty("user.dir") + "\\executioninfo\\SuiteCartPage.xlsx";
	
	public static final String SUITE_CLP_XLS_PATH = System.getProperty("user.dir") + "\\executioninfo\\SuiteCLP.xlsx";
	
	//Sunay
	public static final String SUITE_DELCHARGES_XLS_PATH = System.getProperty("user.dir") + "\\executioninfo\\SuiteDeliveryCharges.xlsx";
	
	public static final String SUITE_PDP_XLS_PATH = System.getProperty("user.dir") + "\\executioninfo\\SuitePDP.xlsx";
	
	public static final String SUITE_PLP_XLS_PATH = System.getProperty("user.dir") + "\\executioninfo\\SuitePLP.xlsx";
	
	public static final String SUITE_PROMOTION_XLS_PATH = System			.getProperty("user.dir") + "\\executioninfo\\SuitePromotion.xlsx";
			
	//Roopam
	public static final String SUITE_EDITBASKET_XLS_PATH = System
			.getProperty("user.dir") + "\\executioninfo\\SuiteEditBasket.xlsx";
			
	public static final String SUITE_DELIVERYOPTION_XLS_PATH = System
			.getProperty("user.dir") + "\\executioninfo\\SuiteDeliveryOption.xlsx";
	
	//Ankur
	public static final String SUITE_HEADERFOOTER_XLS_PATH = System
			.getProperty("user.dir") + "\\executioninfo\\SuiteHeaderFooter.xlsx";
	
	public static final String SUITE_HOMEPAGE_XLS_PATH = System
			.getProperty("user.dir") + "\\executioninfo\\SuiteHomePage.xlsx";
	
	public static final String SUITE_STOREFINDER_XLS_PATH = System
			.getProperty("user.dir") + "\\executioninfo\\SuiteStoreFinder.xlsx";
	
	public static final String SUITE_MYACCOUNT_XLS_PATH = System
			.getProperty("user.dir") + "\\executioninfo\\SuiteMyAccount.xlsx";

	public static final String TEST_ORDERS_XLS = System.getProperty("user.dir")
			+ "\\executioninfo\\TestExecutionData.xlsx";

	public static final String SCREENSHOTS_PATH = "file:\\NTVMFILESVR0301\\Public Share\\Public Share\\AsseticAutomation\\Screenshots\\";

	// DRIVER LOCATIONS
	public static final String CHROMEDRIVER_LOCATION = System
			.getProperty("user.dir") + "\\drivers\\chromedriver_2.29.exe";

	public static final String IEDRIVER_LOCATION = System
			.getProperty("user.dir")
			+ "\\drivers\\IEDriverServer_Win32_2.47.0\\IEDriverServer.exe";

	public static final String GECKODRIVER_LOCATION = System
			.getProperty("user.dir") + "\\drivers\\geckodriver.exe";

	// SHEET NAMES
	public static final String TESTCASE_SHEET = "TestCases";
	public static final String TESTSUITE_SHEET = "SuiteName";
	public static final String TESTORDERS_SHEET = "OrderDetails";
	

	// COLUMN NAMES IN XLS
	public static final String COL_RUNMODENAME = "Runmode";
	public static final String TESTCASE_NAME = "TCID";
	public static final String TESTSUITE_COL_SUITENAME = "TSID";

	// RUNMODES
	public static final String RUNMODE_YES = "Y";
	public static final String RUNMODE_NO = "N";

	// ERRORS
	public static final String OPENBROWSER_ERROR = "Error - Error while opening the browser ";
	public static final String NAVIGATE_ERROR = "Error - Error while navigating to url ";
	public static final String FIND_ELEMENT_ERROR = "Error - Not able to find element ";
	public static final String LOCATOR_ERROR = "Error - Invalid Locator ";
	public static final String GENERAL_ERROR = "ERROR - FAILED KEYWORD - ";
	public static final String TEXTNOTPRESENT_ERROR = "ERROR - NOT ABLE TO LOCATE TEXT";	

	// FAILURES
	public static final String ELEMENT_NOT_FOUND_FAILURE = "Failure - Element not found ";
	public static final String TITLE_NOT_MATCH_FAILURE = "Failure - Title do not match ";
	public static final String LOGIN_FAILURE = "Failure - Login not successful ";
	public static final String CLICKANDWAIT_FAILURE = "FAIL - Could not click and wait - ";
	public static final String PORTFOLIONAME_NOTPRESENT_FAILURE = "Portfolio name not present - ";
	public static final String DUPLICATE_FAILURE = "Fail - Duplicate portfolio name error was expected but NOT found for portfolio name - ";
	public static final String LOGIN_INVALID_FAILURE = "Failure - Able to login with invalid credentials";

	public static final String TEXTNOTPRESENT_FAILURE = "Failure - Text is not present on the page";

	// TEXTS
	public static final String POSTAL_CODE = "wd17 2sd";
	public static final String CONTACT_NUM = "12345678";
	public static final String BILLING_EMAIL = "test@test.com";
	public static final String BILLING_HOUSE_NUM = "D11";
	public static final String NEW_BILLING_ADR = "B & Q Plc,Unit 2,Watford Arches Retail Park,Lower High Street,Watford,WD17 2SD";
	public static final String NICKNAME = "Test";
	public static final String CARD_CVV = "123";
	public static final String CARD_NO_MAESTRO = "58682416082553338";
	public static final String DELIVERY_TYPE_STANDARD = "Standard";
	public static final String DELIVERY_ADR_NEW = "New";
	public static final String DELIVERY_OPTION_UK = "UK";
	public static final String DELIVERY_OPTION_CnC = "CnC";
	public static final String DELIVERY_OPTION_BFPO = "bfpo";
	public static final String DELIVERY_OPTION_INTERNATIONAL = "International";
	public static final String PAYMENT_MODE_CREDIT_CARD = "CreditCard";
	public static final String CARD_TYPE_SAVED = "Saved";
	public static final String OUT_OF_STOCK_PRODUCT = "134146";
	// Sunay
	public static final String FIRST_NAME = "Vikrant";
	public static final String LAST_NAME = "Rathod";
	public static final String EMAIL_ID = "vikrant.rathod@nihilent.com";
	public static final String GIFT_CARD_NO = "6331134125762704499";
	public static final String SUCCESS_USER_REG_MESSAGE = "thank you for registering with mothercare";
	public static final String CHANNELISLAND_CHARGES_MESSAGE = "13.95";
	public static final String INTERNATIONAL_CHARGES_MESSAGE = "will be calculated shortly";
	// Roopam
	public static final String AVAILABLE_DELIVERY_OPTION = "Available delivery options:";
	public static final String SERVICE_NUMBER = "bfpo service number";
	public static final String UNIT = "bfpo unit";
	public static final String BFPO_NUMBER = "bfpo14";
	public static final String SUCCESS_ORDER_MESSAGE = "thank you for your order";
	public static final String TITLE = "Dr";
	public static final String EMAIL = "vikrant.rathod@nihilent.com";
	public static final String PHONE_NUMBER = "123456";
	public static final String ADD_LINE_1 = "Address Line 1";
	public static final String ADD_LINE_2 = "Address Line 2";
	public static final String TOWN = "Test town";
	public static final String COUNTRY = "Guernsey";
	public static final String INT_POSTCODE = "GY1 3EL";
	public static final String CARD_NO_VISA = "4444333322221111";
	public static final String CARD_NO_MASTER = "5105105105105100";
	public static final String COUNTRY_REST_OF_THE_WORLD = "India";
	public static final String REST_OF_THE_WORLD_POSTCODE = "411014";
	public static final String REDEMPTION_SUCCESS_TEXT = "The redemption of your Gift Card was successful!";
	public static final String FURNITURE_DELIVERY_ESTIMATED_DELIVERY_DAYS = "7-10";
	//ANKUR
	public static final String PRODUCT_HOLDER_NAME = "TEST";
	public static final String PRODUCT_HOLDER_DOB_DAY = "01";
	public static final String PRODUCT_HOLDER_DOB_MONTH = "March";
	public static final String PRODUCT_HOLDER_DOB_YEAR = "2014";
		
	
	public static final String NOITEMAVAILABLE = "No items to display";

	public static final String MYTICKETS_PAGETITLE = "";

	public static final String SUCCESSFULLYCREATED = "Successfully created";
	public static final String UNAUTHORIZEDACCESS_MESSAGE = "Authorization has been denied for this request.";
	public static final String FINANCIALYEAR_SUCCESS = "Financial Year created successfully";
	public static final String FINANCIALYEAR_ERROR = "The Financial Year field is required";
	public static final String UPDATEFINANCIALYEAR_SUCCESS = "Financial Year updated successfully";
	public static final String DELETE_FINANCIALYEAR_SUCCESS = "Financial Year deleted successfully";
	public static final String DELETE_FINANCIALYEAR_NOTSUCCESS = "Please delete subsequent financial years before deleting selected year.";
	public static final String ADD_NEWASSESSMENT_SUCCESS = "Assessment task    has been created.";
	public static final String ASSESSMENT_ADDPROJECT_SUCCESS = "Successfully saved.";
	public static final String ASSETS_EDITASSET_SUCCESS = "Successfully updated. Asset Name Value";
	public static final String ASSETS_UPDATE_STANDARDANDCODES_SUCCESS = "CAGRStandardsandCode updated successfully";
	public static final String ASSETS_EDITASSET_INVALIDNAME_MSG = "Update not Successful {\"Label 1\":\"Invalid Object Label\"}";
	public static final String ASSETS_UPDATE_MANUFACTURERDETAILS_SUCCESS = "CAGRManufacturerDetail updated successfully";
	public static final String ASSETS_UPDATE_AUSTRALIANSTANDARDDETAILS_SUCCESS = "CAGRAustralianStandardsandCode updated successfully";
	public static final String ASSETS_UPDATE_BUILDINGDETAILS_SUCCESS = "CAGRBuildingDetail updated successfully";
	public static final String ASSETS_UPDATE_ELECTRICMETERDETAILS_SUCCESS = "CAGRElectricalMeterDetail updated successfully";
	public static final String ASSETS_UPDATE_GASMETERDETAILS_SUCCESS = "CAGRGasMeterDetail updated successfully";
	public static final String ASSETS_UPDATE_WATERMETERDETAILS_SUCCESS = "CAGRWaterMeterDetail updated successfully";
	public static final String ASSETS_UPDATE_OTHERCLASSIFICATION_SUCCESS = "CAGROtherClassification updated successfully";
	public static final String ASSETS_UPDATE_SERVICEHIERARCHY_SUCCESS = "CAGRServiceHierarchy updated successfully";
	public static final String ASSETS_UPDATE_LANDINFO_SUCCESS = "CAGRPropertyandLotDetail updated successfully";
	public static final String ASSETS_UPDATE_ADDITIONALINFO_SUCCESS = "CAGRAdditionalInfo updated successfully";
	public static final String ASSETS_UPDATE_EXTERNALLINKS_SUCCESS = "CAGRExternalLink updated successfully";
	public static final String ASSETS_UPDATE_SERVICING_SUCCESS = "CAGRServicing updated successfully";
	public static final String ASSETS_UPDATE_SERVICEFEATURES_SUCCESS = "CAGRGrantsCommission updated successfully";
	public static final String ASSETS_UPDATE_ASSETCOMMISSIONING_SUCCESS = "CAGRAssetCommissioning updated successfully";
	public static final String ASSETS_COMPONENT_UPDATE_SUCCESS = "Component has been updated.";
	public static final String CAPEX_CREATE_ERRORMSG = "Valuation record already exists for Date and time of day";
	public static final String CAPEX_CREATE_SUCCESSMSG = "Successfully added the cost change request";
	public static final String CAPEX_EDIT_VALUATIONDATE_ERROR = "Valuation date is required.";

	public static final String ASSETS_ATTRIBUTE_DASHBOARD = "Dashboard";
	public static final String ASSETS_ATTRIBUTE_BRANDING = "Branding";
	public static final String ASSETS_ATTRIBUTE_DESIGN = "Design";
	public static final String ASSETS_ATTRIBUTE_CLASSIFICATION = "Classification";
	public static final String ASSETS_ATTRIBUTE_LINKSANDNOTES = "Links and Notes";
	public static final String ASSETS_ATTRIBUTE_MANAGEMENT = "Management";
	public static final String ASSETS_ATTRIBUTE_SERVICEFEATURES = "Service Features";
	public static final String ASSETS_ATTRIBUTE_SUBDIVISIONDETAILS = "Subdivision Details";
	public static final String ASSETS_ATTRIBUTE_AUDITTRAIL = "Audit Trail";
	public static final String ASSETS_ATTRIBUTE_LANDINFORMATION = "Land Information";

	public static final int CATALOGUECOUNT = 20;

	public static final int EXPECTED_SEARCHRESULT_POPULATED = 5;

	public static final String Username = "test_runner_clientadmin@assetic.com";

	public static final String Password = "Test-runner@123";

	public static final String ACCOUNTING_MODULE = "SuiteAccounting";

	public static final String DELETE_ASSESSMENTPROJECT = "Deleted successfully.";

}
