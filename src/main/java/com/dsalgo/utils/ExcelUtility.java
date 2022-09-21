package com.dsalgo.utils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//Utility class created to read values from an Excel (.xlsx format)
public class ExcelUtility {

	// Method to read the data from Excel
	public static Object[][] ReadDataFromExcel(String filePath, String sheetName) throws Exception {

		Object[][] testData;
		File sourceFile = new File(filePath);

		try (// Get the workbook from the file path specified
			XSSFWorkbook workBook = new XSSFWorkbook(sourceFile)) {
			
			// Get the sheet based on the sheet name specified
			XSSFSheet sheet = workBook.getSheet(sheetName);

			int rowsCount = sheet.getLastRowNum(); // no.of rows
			int cellCount = sheet.getRow(0).getLastCellNum(); // no.of columns
			testData = new Object[rowsCount][cellCount];

			/*Uncomment below line to handle cells with date value 
			 DateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy"); */
			
			for (int i = 1; i <= rowsCount; i++) // Start from i=1 to skip header row
			{
				Row row = sheet.getRow(i);

				for (int j = 0; j < cellCount; j++) {

					Cell cell = row.getCell(j);
					switch (cell.getCellType()) {
					case STRING:
						testData[i - 1][j] = cell.getStringCellValue();
						break;
						/* uncomment to handle numeric/date/boolean cell values
					case NUMERIC: // Numeric or date field
						testData[i - 1][j] = DateUtil.isCellDateFormatted(cell)
								? dateformat.format(cell.getDateCellValue())
								: cell.getNumericCellValue();
						break;
					case BOOLEAN:
						testData[i - 1][j] = cell.getBooleanCellValue();
						break;
						*/
					default:
						testData[i - 1][j] = cell.getStringCellValue();
						break;
					}
				}
			}
		}

		return testData;
	}

	// Alternate Method to read the data from Excel using Iterator
	public static Object[][] ReadDataFromExcelAlt(String filePath, String sheetName) throws Exception {

		Object[][] testData;
		File sourceFile = new File(filePath);

		try (// Get the workbook from the file path specified
			XSSFWorkbook workBook = new XSSFWorkbook(sourceFile)) {
			// Get the sheet based on the sheet name specified
			XSSFSheet sheet = workBook.getSheet(sheetName);

			int rowsCount = sheet.getLastRowNum();
			int cellCount = sheet.getRow(0).getLastCellNum();
			int rowNumber, cellNumber;

			testData = new Object[rowsCount][cellCount];

			// To handle cells with date value
			DateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");

			Iterator<Row> rows = sheet.rowIterator();
			Row row = rows.next(); // skip header
			while (rows.hasNext()) {
				row = rows.next();

				Iterator<Cell> cells = row.cellIterator();
				while (cells.hasNext()) {
					Cell cell = cells.next();
					rowNumber = cell.getRowIndex();
					cellNumber = cell.getColumnIndex();
					switch (cell.getCellType()) {
					case STRING:
						testData[rowNumber - 1][cellNumber] = cell.getStringCellValue();
						break;
					case NUMERIC: // Numeric or date field
						testData[rowNumber - 1][cellNumber] = DateUtil.isCellDateFormatted(cell)
								? dateformat.format(cell.getDateCellValue())
								: cell.getNumericCellValue();
						break;
					case BOOLEAN:
						testData[rowNumber - 1][cellNumber] = cell.getBooleanCellValue();
						break;
					default:
						testData[rowNumber - 1][cellNumber] = cell.getStringCellValue();
						break;
					}
				}
			}
		}

		return testData;
	}
}
