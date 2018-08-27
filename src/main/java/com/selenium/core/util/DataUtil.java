package com.selenium.core.util;

import java.util.Hashtable;
//import org.testng.*;

public class DataUtil {

	public static Object[][] getTestData(Xls_Reader xls, String testCaseName) {
		String sheetName = "Data";
		int testStartRowNum = 1;
		while (!xls.getCellData(sheetName, 0, testStartRowNum).equals(
				testCaseName)) {
			testStartRowNum++;
		}

		int colStartRowNum = testStartRowNum + 1;
		int dataStartRowNum = testStartRowNum + 2;
		int rows = 0; // To calculate Rows of Data

		while (!xls.getCellData(sheetName, 0, dataStartRowNum + rows)
				.equals("")) {
			rows++;
		}
		int cols = 0; // To calculate total number of data columns
		while (!xls.getCellData(sheetName, cols, colStartRowNum).equals("")) {
			cols++;
		}

		System.out.println("[" + xls.filename + "]: Reading " + sheetName
				+ " for Test: [" + testCaseName
				+ "]; Reading data from columns: [0/" + cols + "] and rows: ["
				+ dataStartRowNum + "]/[" + dataStartRowNum + rows + "]");
		// Reading Data
		Object[][] data = new Object[rows][1];
		int dataRow = 0;
		Hashtable<String, String> table = null;
		for (int rNum = dataStartRowNum; rNum < dataStartRowNum + rows; rNum++) {
			table = new Hashtable<String, String>();
			for (int cNum = 0; cNum < cols; cNum++) {
				String key = xls.getCellData(sheetName, cNum, colStartRowNum);
				String value = xls.getCellData(sheetName, cNum, rNum);
				table.put(key, value);
			}
			data[dataRow][0] = table;
			dataRow++;
		}
		return data;
	}

	public static boolean isRunnable(String testName, Xls_Reader xls) {
		String sheet = Constants.TESTCASE_SHEET;
		int rows = xls.getRowCount(sheet);
		for (int r = 2; r <= rows; r++) {
			String tName = xls.getCellData(sheet, Constants.TESTCASE_NAME, r)
					.trim().toLowerCase();

			if (tName.equals(testName.trim().toLowerCase())) {
				String runmode = xls.getCellData(sheet,
						Constants.COL_RUNMODENAME, r);

				if (runmode.equals(Constants.RUNMODE_YES))
					return true;
				else
					return false;
			}
		}
		return false;
	}

	// CHECK IF SUITE IS RUNNABLE
	public static boolean isSuiteRunnable(String suiteName) {
		Xls_Reader xls = new Xls_Reader(Constants.TESTSUITE_XLSPATH);
		int rows = xls.getRowCount(Constants.TESTSUITE_SHEET);
		
		for (int rNum = 2; rNum <= rows; rNum++) {
			String testSuiteName = xls.getCellData(Constants.TESTSUITE_SHEET,
					Constants.TESTSUITE_COL_SUITENAME, rNum);

			if (testSuiteName.toLowerCase().equals(suiteName.toLowerCase())) {
				String suiteRunmode = xls.getCellData(
						Constants.TESTSUITE_SHEET, Constants.COL_RUNMODENAME,
						rNum);

				if (suiteRunmode.toLowerCase().equals(
						Constants.RUNMODE_YES.toLowerCase()))
					return true;
				else
					return false;
			}
		}
		return false;
	}

	/*
	 * // CHECK THE RUNMODE OF TEST CASE public static boolean
	 * isTestCaseRunnable(String testCaseName, Xls_Reader xls) { int row =
	 * xls.getRowCount(Constants.TESTCASE_SHEET);
	 * 
	 * for (int rNum = 2; rNum <= row; rNum++) {
	 * 
	 * String testCase = xls.getCellData(Constants.TESTCASE_SHEET,
	 * Constants.TESTCASE_NAME, rNum);
	 * 
	 * if (testCase.toLowerCase().trim()
	 * .equals(testCaseName.toLowerCase().trim())) {
	 * 
	 * String testCaseRunMode = xls.getCellData( Constants.TESTCASE_SHEET,
	 * Constants.COL_RUNMODENAME, rNum);
	 * 
	 * System.out.println("Test case : " + testCase + " .Runmode : " +
	 * testCaseRunMode);
	 * 
	 * if (testCaseRunMode.toLowerCase().equals(
	 * Constants.RUNMODE_YES.toLowerCase())) return true; else return false; } }
	 * 
	 * return false;
	 * 
	 * }
	 * 
	 * // VALIDATE IF THE SUITE, TESTCASE OR TEST DATA IS RUNNABLE public static
	 * void validateTestExecution(String testSuiteName, String testCaseName,
	 * String dataRunmode, Xls_Reader xls) {
	 * 
	 * // Check if suite is runnable if
	 * (!DataUtil.isSuiteRunnable(testSuiteName)) { throw new
	 * SkipException("Skipping the test case " + testCaseName +
	 * " as Rumode for Suite is NO"); } // Check if test case if Runnable if
	 * (!DataUtil.isTestCaseRunnable(testCaseName, xls)) { throw new
	 * SkipException("Skipping the test case " + testCaseName +
	 * " as Rumode for Test Case is NO"); }
	 * 
	 * if (dataRunmode.equals(Constants.RUNMODE_NO)) { throw new
	 * SkipException("Skipping the test data " + testCaseName +
	 * " as Rumode for Data is NO"); }
	 * 
	 * }
	 */
}