package com.selenium.core.util;

//http://relevantcodes.com/Tools/ExtentReports2/javadoc/index.html?com/relevantcodes/extentreports/ExtentReports.html

import java.io.File;
//import src.test.resources;
import java.util.Date;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager  {

	private static ExtentReports extent;

	public static ExtentReports getInstance() {
		if (extent == null) {
			Date d = new Date();
			//String fileName = "Assetic TestExecutionReport_"+ d.toString().replace(":", "_") + ".html";
			String fileName = "Mothercare TestExecutionReport.html";
			extent = new ExtentReports(System.getProperty("user.dir") + "//reports//" + fileName, true, DisplayOrder.NEWEST_FIRST);
			extent.loadConfig(new File(System.getProperty("user.dir")+ "//ReportsConfig.xml"));
			extent.addSystemInfo("Selenium version", "2.53.0").addSystemInfo("Environment", "QA");
		}
		return extent;
	}
}