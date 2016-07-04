/**
 * @Author		LeDaniel Leung
 * @Filename	ExcelParser.java
 * @Description	Parent class of Write/ReadExcel.
 */

package excel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import entry_data.Books;
import entry_data.Entry;

public abstract class ExcelParser{
	/* constants for column label writing */
	public static final int NUM_COLS = 4,
							 NAME_COL = 0,
							 FAMI_COL = 1,
							 BOOK_COL = 2,
							 DATE_COL = 3;
	public static final String[] LABELS = {"Last Name", 
											"Family ID", 
											"Book(s) Bought", 
											"Date Bought"};

	/* variables */
	public String filename;
	public File file;
	public FileOutputStream fos;
	public XSSFWorkbook workbook;
	public XSSFSheet sheet;

	/* getter methods */
	public File				getFile()			 {return file;	  }
	public String			getFileName()		 {return filename;}
	public XSSFSheet 		getSheet()			 {return sheet;	  }
	public XSSFWorkbook 	getWorkbook()		 {return workbook;}
	public FileOutputStream getFileOutputStream(){return fos;	  }
	


	/**
	 * @function	getEntry
	 * @param 		row (Row) - the row in the sheet where the entry is found
	 * @return		an Entry based on data in the database
	 * @description	Creates an entry using the information retrieved from the
	 * 				specified row
	 */
	public Entry getEntry(Row row){
		Iterator<Row> rowIterator = sheet.rowIterator();
		
		//variables required to construct an entry
		String name;
		int ID;
		List<Books> bookList = new ArrayList<Books>();
		List<String> dateList = new ArrayList<String>();
		
		//variables for null checking, etc.
		Cell nameCell, famiCell, bookCell, dateCell;
		
		//the row is null, return null
		if(row == null) return null;
		
		//if the name or family cell has not been initialized, return null
		try{
			nameCell = row.getCell(NAME_COL);
			famiCell = row.getCell(FAMI_COL);
		}catch(NullPointerException e){return null;}
		
		//cell type mismatch = not a valid entry
		if(nameCell.getCellType() != Cell.CELL_TYPE_STRING ||
				famiCell.getCellType() != Cell.CELL_TYPE_NUMERIC) return null;
		
		//if the row specified does not have a name or fam ID, return null
		try{
			if(nameCell.getStringCellValue().compareTo("") <= 0 ||
					famiCell.getNumericCellValue() == 0) return null;
		}catch(NullPointerException e){return null;}
		
		//assign name and ID
		name = row.getCell(NAME_COL).getStringCellValue();
		ID   = (int)row.getCell(FAMI_COL).getNumericCellValue();
		
		//get the cells containing the book and date at the specified row
		try{
			bookCell = row.getCell(BOOK_COL);
			dateCell = row.getCell(DATE_COL);
		}catch(NullPointerException e){return null;}
		
		//check to see if the cell is non-empty
		if(bookCell.getStringCellValue().compareTo("") > 0){
			
			//add the corresponding book and date (if they exist)
			Books book = Books.getBook(bookCell.getStringCellValue());
			if(book != null) bookList.add(book);
			
			String date = dateCell.getStringCellValue();
			if(date != null) dateList.add(date);
		}
		
		//moves rowIterator to the specified row
		while(rowIterator.hasNext()) if(rowIterator.next().equals(row)) break;
		
		//goes through the name column until there are no more rows or name
		//is encountered (nameCell is not null)
		while(rowIterator.hasNext()){
			Row currentRow = rowIterator.next();
			if(currentRow.getCell(NAME_COL) == null){
				
				//the name Cell is null, check book/date
				try{
					bookCell = currentRow.getCell(BOOK_COL);
					dateCell = currentRow.getCell(DATE_COL);
				}catch(NullPointerException e){return null;}

				//add values in book/dateCell(s) to bookList
				if(bookCell.getStringCellValue().compareTo("") > 0){
					Books book = Books.getBook(bookCell.getStringCellValue());
					if(book != null) bookList.add(book);
					
					String date = dateCell.getStringCellValue();
					if(date != null) dateList.add(date);
				}
			}
			//tmpCell is not null, break
			else break;
		}
		return new Entry(name, ID, bookList, dateList);
	}

	/**
	 * @function	hasEntry
	 * @param 		entry (Entry) - the entry to check for in the database
	 * @return		true if sheet contains entry
	 * 				false otherwise
	 * @description	delegates to hasName and hasID to see if the passed in
	 * 				entry matches any existing ones (books + dates not checked)
	 */
	public boolean hasEntry(Entry entry){
		return hasName(entry.getName()) && hasID(entry.getID());
	}

	/**
	 * @function	hasName
	 * @param 		name (String) - the name to check for in the database
	 * @return		true if sheet contains name
	 * 				false otherwise
	 * @description	iterates through name column and compares to see if the
	 * 				passed in name is present in the sheet
	 */
	public boolean hasName(String name){
		Iterator<Row> rowIterator = sheet.rowIterator();
		
		//skip the first row (label row)
		rowIterator.next();
		
		//go through the name column and compare names
		while(rowIterator.hasNext()){
			Cell nameCell;
			
			//if cell is null, current row contains only books
			if((nameCell = rowIterator.next().getCell(NAME_COL)) == null)
				continue;

			//ensure nameCell contains a string
			if(nameCell.getCellType() == Cell.CELL_TYPE_STRING)
				if(nameCell.getStringCellValue().equals(name)) return true;
		}
		return false;
	}
	
	/**
	 * @function	hasID
	 * @param 		ID (int) - the ID to check for in the database
	 * @return		true if sheet contains ID
	 * 				false otherwise
	 * @description	iterates through ID column and compares to see if the
	 * 				passed in ID is present in the sheet
	 */
	private boolean hasID(int ID){
		Iterator<Row> rowIterator = sheet.rowIterator();
		
		//skip the first row (label row)
		rowIterator.next();
		
		//go through the name column and compare names
		while(rowIterator.hasNext()){
			Cell IDCell;

			//if cell is null, current row contains only books
			if((IDCell = rowIterator.next().getCell(FAMI_COL)) == null)
					continue;

			if(IDCell.getCellType() == Cell.CELL_TYPE_NUMERIC)
				if(IDCell.getNumericCellValue() == ID) return true;
		}
		return false;
	}

}
