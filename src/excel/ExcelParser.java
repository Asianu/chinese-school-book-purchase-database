/**
 * @Author		LeDaniel Leung
 * @Filename	ExcelParser.java
 * @Description	Parent class of Write/ReadExcel.
 */

package excel;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import entry_data.Books;
import entry_data.Entry;

public class ExcelParser{
	/* constant variables */
	public static final String SHEET_NAME = "Sheet1";
	
	/* constants for column label writing */
	protected static final int NUM_COLS = 4,
							   NAME_COL = 0,
							   FAMI_COL = 1,
							   BOOK_COL = 2,
							   DATE_COL = 3;
	protected static final String[] LABELS = {"Last Name", 
										      "Family ID", 
										      "Book(s) Bought", 
										   	  "Date Bought"};
	
	/* constants for toString */
	private static final String TO_STRING_LABELS =
			"Name\tID\tBooks\t\t\t\tDates\n" +
				"------- ------- ------------------------------- -------\n";

	/* variables */
	public String filename;
	public File file;
	public XSSFWorkbook workbook;
	public XSSFSheet sheet;

	/* getter methods */
	public File				getFile()			 {return file;	  }
	public String			getFileName()		 {return filename;}
	public XSSFSheet 		getSheet()			 {return sheet;	  }
	public XSSFWorkbook 	getWorkbook()		 {return workbook;}
	


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
		if((nameCell = row.getCell(NAME_COL)) == null ||
		   (famiCell = row.getCell(FAMI_COL)) == null) return null;
		
		//cell type mismatch = not a valid entry
		if(nameCell.getCellType() != Cell.CELL_TYPE_STRING ||
		   famiCell.getCellType() != Cell.CELL_TYPE_NUMERIC) return null;

		//if the row specified does not have a name or fam ID, return null
		if(nameCell.getStringCellValue().compareTo("") <= 0 ||
		   famiCell.getNumericCellValue() == 0) return null;
		
		//assign name and ID
		name = row.getCell(NAME_COL).getStringCellValue();
		ID   = (int)row.getCell(FAMI_COL).getNumericCellValue();
		
		//get the cells containing the book and date at the specified row
		if((bookCell = row.getCell(BOOK_COL)) == null ||
		   (dateCell = row.getCell(DATE_COL)) == null)
			return new Entry(name, ID, bookList, dateList);
		else if(bookCell.getCellType() != Cell.CELL_TYPE_STRING ||
				dateCell.getCellType() != Cell.CELL_TYPE_STRING)
			return null;
		
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
				
				//if ID cell is not null, something is wrong
				if(currentRow.getCell(FAMI_COL) != null) return null;
				
				//the name Cell is null, check book/date
				try{
					bookCell = currentRow.getCell(BOOK_COL);
					dateCell = currentRow.getCell(DATE_COL);
				}catch(NullPointerException e){return null;}
				
				if(bookCell.getCellType() != Cell.CELL_TYPE_STRING ||
				   dateCell.getCellType() != Cell.CELL_TYPE_STRING)
					return null;
					
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
	 * @function	getEntry
	 * @param 		entry (Entry) - entry to retrieve from the database
	 * @return		the entry in the database
	 * 				null if entry is not found
	 * @description	this exists in order to retrieve the books/dates associated
	 * 				with the passed in entry (that does not have books/dates).
	 * 				Delegates to getRow() and getEntry(Row).
	 */
	public Entry getEntry(Entry entry){
		if(hasEntry(entry)) return getEntry(getRow(entry));
		return null;
	}
	
	/**
	 * @function	getRow
	 * @param 		entry (Entry) - the entry to be located
	 * @return		the Row where the entry can be found. If not found, then
	 * 				returns null
	 * @description	Determines if the entry exists, if not then return null,
	 * 				otherwise iterate through the sheet and return the row
	 * 				where entry is found.
	 */
	public Row getRow(Entry entry){
		//entry does not exist, return null immediately
		if(!hasEntry(entry)) return null;
		
		Iterator<Row> rowIterator = sheet.rowIterator();
		
		//skip the first row (label row)
		rowIterator.next();
		
		//go through the rows and...
		while(rowIterator.hasNext()){
			Entry tmpEntry;
			Row currentRow = rowIterator.next();
			
			//... compare entries
			if((tmpEntry = getEntry(currentRow)) != null)
				if(entry.isEqual(tmpEntry)) return currentRow;
		}
		return null;
	}
	
	/**
	 * @function	hasEntry
	 * @param 		entry (Entry) - the entry to check for in the database
	 * @return		true if sheet contains entry
	 * 				false otherwise
	 * @description	iterates row by row and compares to see if the passed in
	 * 				entry is present in the sheet by checking name and ID
	 * 				columns.
	 */
	public boolean hasEntry(Entry entry){
		Iterator<Row> rowIterator = sheet.rowIterator();
		
		//skip the first row (label row)
		rowIterator.next();
		
		//go through the rows and compare names & IDs
		while(rowIterator.hasNext()){
			Entry tmpEntry;
			Row currentRow = rowIterator.next();
			
			//compare entries
			if((tmpEntry = getEntry(currentRow)) != null)
				if(entry.isEqual(tmpEntry)) return true;
			}
		return false;
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
			if((nameCell = rowIterator.next().getCell(NAME_COL)) != null)
				
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
	/* probably not necessary
	private boolean hasID(int ID){
		Iterator<Row> rowIterator = sheet.rowIterator();
		
		//skip the first row (label row)
		rowIterator.next();
		
		//go through the name column and compare names
		while(rowIterator.hasNext()){
			Cell IDCell;

			//if cell is null, current row contains only books
			if((IDCell = rowIterator.next().getCell(FAMI_COL)) != null)
			
				//ensure IDCell contains an integer
				if(IDCell.getCellType() == Cell.CELL_TYPE_NUMERIC)
					if(IDCell.getNumericCellValue() == ID) return true;
		}
		return false;
	}
	*/

	/**
	 * @function	toString()
	 * @param		none
	 * @description	prints out all the information in the sheet
	 */
	public String toString(){
		//initialize the return string with the labels
		String ret = TO_STRING_LABELS;
		
		//initialize rowIterator and skip the first row (labels)
		Iterator<Row> rowIterator = sheet.rowIterator();
		rowIterator.next();
		
		while(rowIterator.hasNext()){
			Entry tmpEntry;
		
			//check to see if name is not null, if so then create the entry
			if((tmpEntry = getEntry(rowIterator.next())) != null)
				ret += tmpEntry.toString() + "\n";
		}
		
		return ret;
	}
}
