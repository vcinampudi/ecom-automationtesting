package com.selenium.core.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Xls_Writer {

	public XSSFWorkbook wb;
	public XSSFCell cell1, cell2, cell3;
	public XSSFRow row;
	public XSSFSheet sheet;
	public FileOutputStream fos;
	public FileInputStream fis;
	public String filepath = null;
	public Date date = new Date();
	public SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
	public String strDate = null;

	Xls_Reader xlsRead;
	public int rowCnt;

	public Xls_Writer(String path) throws IOException {
		filepath = path;
		sdf = new SimpleDateFormat("dd/MM/yyyy");
		strDate = sdf.format(date);
		xlsRead = new Xls_Reader(path);
	}

	public void saveDataToXls(String testName, String orderNo) {

		rowCnt = xlsRead.getRowCount(Constants.TESTORDERS_SHEET);
		System.out.println("Row COunt" + rowCnt);

		try {
			fis = new FileInputStream(filepath);
			wb = new XSSFWorkbook(fis);
			fos = new FileOutputStream(filepath);
			sheet = wb.getSheet(Constants.TESTORDERS_SHEET);
			row = sheet.getRow(rowCnt);
			if (row == null) {
				row = sheet.createRow(rowCnt);
			}

			cell1 = row.getCell(0);
			cell2 = row.getCell(1);
			cell3 = row.getCell(2);

			if (cell1 == null) {
				cell1 = row.createCell(0);
				cell1.setCellValue(testName);
			}
			if (cell2 == null) {
				cell2 = row.createCell(1);
				cell2.setCellValue(orderNo);
			}
			if (cell3 == null) {
				cell3 = row.createCell(2);
				cell3.setCellValue(strDate);
			}

			wb.write(fos);

			fis.close();
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}
