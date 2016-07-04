/**
 * @Author 		LeDaniel Leung
 * @Filename 	WriteExcel.java
 * @Description Writes data to the file
 */

package excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import entry_data.Books;
import entry_data.Entry;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class WriteExcel extends ExcelParser{
	/* constant variables */
	/* constants for constructor */
	private static final String SHEET_NAME = "Sheet1",
								EXTENSION  = ".xlsx";

	/* constants for default constructor */
	/* constant integers are for extracting info from Date */
	private static final int DOW  = 0,
							 DD   = 11,
							 YYYY = 24;
	private static final String DEF_FILE_NAME = " Book Purchase Database";



	/**
	 * @function	WriteExcel
	 * @param 		filename (String) - name of the file to be written
	 * @description	Constructor for WriteExcel, which creates a new excel file
	 * 				with the correct template.
	 */
	public WriteExcel(String filename){
		this.filename = filename;
		try{
			//create a new excel file using passed in filename
			file	 = new File(filename + EXTENSION);
			fos 	 = new FileOutputStream(file);
			workbook = new XSSFWorkbook();
			sheet 	 = workbook.createSheet(SHEET_NAME);
		}catch(FileNotFoundException e){e.printStackTrace();}
		
		//Writes the labels of the columns
		writeColumnLabels();
	}

	/**
	 * @function	WriteExcel (default constructor)
	 * @param		none
	 * @description	Default constructor for WriteExcel, creates a new excel
	 * 				file with the date (dow mon dd yyyy) as the file name
	 */
	public WriteExcel(){
		//create a new excel file using the date as the filename
		this(new Date().toString().substring(DOW, DD) + 
			 new Date().toString().substring(YYYY) 	  + DEF_FILE_NAME);
	}

	/**
	 * @function	insertEntry
	 * @param 		entry (Entry) - entry to be inserted into 
	 * @return		0 if entry was successfully inserted
	 * 				1 if existing entry was updated
	 * @description	inserts an entry into the database in alphabetical order
	 * 				if the entry already exists, update instead
	 */
	public int insertEntry(Entry entry){
		Iterator<Row> rowIterator = sheet.rowIterator();
		
		return 0;
	}
	
	/**
	 * @function	end
	 * @param		none
	 * @description	Writes the workbook to the file and closes it
	 */
	public void end(){
		try{
			workbook.write(fos);
			fos.close();
		}catch(IOException e){e.printStackTrace();}
	}

	/**
	 * @function	WriteColumnLabels
	 * @param		none
	 * @description	Writes the labels of the required columns
	 */
	private void writeColumnLabels(){
		//create the first row
		Row row = sheet.createRow(0);
		
		//iterates the row, create new cells, write labels
		for(int col = 0; col < NUM_COLS; col++){
			Cell cell = row.createCell(col, Cell.CELL_TYPE_STRING);
			cell.setCellValue(LABELS[col]);
			sheet.autoSizeColumn(col);
		}
	}

	/**
	 * @function	writeName
	 * @param		name (String) - name to be written
	 * @param 		cell (Cell) - cell to be written to
	 * @description	Writes the name of an entry to the sheet
	 */
	private void writeName(String name, Cell cell){
		cell.setCellValue(name);
		sheet.autoSizeColumn(cell.getColumnIndex());
	}
	
	/**
	 * @function	writeID
	 * @param		ID (int) - ID to be written
	 * @param		cell (Cell) - cell to be written to
	 * @description	Writes the ID of an entry to the sheet
	 */
	private void writeID(int ID, Cell cell){
		cell.setCellValue(ID);
		sheet.autoSizeColumn(cell.getColumnIndex());
	}
	
	/**
	 * @function	writeBooks
	 * @param		books (List<Books>) - list of books to be written
	 * @param		dates (List<String>) - list of dates to be written
	 * @param		startCell (Cell) - initial cell to start writing at
	 * @description	Writes the list of books and dates to the sheet
	 */
	private void writeBooks(List<Books> books, List<String> dates, Cell startCell){
		//goes through the list of books
		for(int i = 0; i < books.size(); i++){
			Cell dateCell;
			
			//if first book, write to the startCell
			if(i == 0){
				startCell.setCellValue(books.get(i).toString());
				
				//create the corresponding dateCell
				dateCell = startCell.getRow().createCell
						(DATE_COL, Cell.CELL_TYPE_STRING);
			}
		
			//if there is more than 1 book, create new row at next index,
			//create new cell at correct column, write to new cell
			else{
				Row row   = sheet.createRow(startCell.getRowIndex() + i);
				Cell cell = row.createCell(startCell.getColumnIndex(), 
						Cell.CELL_TYPE_STRING);
				cell.setCellValue(books.get(i).toString());
				
				//create the corresponding dateCell
				dateCell = row.createCell(DATE_COL, Cell.CELL_TYPE_STRING);
			}
			sheet.autoSizeColumn(startCell.getColumnIndex());
			
			//assign the proper date
			dateCell.setCellValue(dates.get(i));
			sheet.autoSizeColumn(dateCell.getColumnIndex());
		}
	}
	
	/**
	 * @function	writeEntry
	 * @param 		entry (Entry) - entry to be written to the sheet
	 * @param 		row (Row) - row to be written to
	 * @description	Writes an entry to the specified row in the sheet
	 */
	private void writeEntry(Entry entry, Row row){
		Cell nameCell = row.createCell(NAME_COL, Cell.CELL_TYPE_STRING),
			 famiCell = row.createCell(FAMI_COL, Cell.CELL_TYPE_NUMERIC),
			 bookCell = row.createCell(BOOK_COL, Cell.CELL_TYPE_STRING);

		writeName (entry.getName(),  nameCell);
		writeID	  (entry.getID(), 	 famiCell);
		writeBooks(entry.getBooks(), entry.getDates(), bookCell);
	}
	
	/**
	 * @function	updateEntry
	 * @param 		entry (Entry) - variable containing the updated information
	 * @param		row (Row) - row of the entry to be updated
	 * @description	Updates the entry at the row passed in
	 */
	private void updateEntry(Entry newEntry, Row row){
		//variables for old info
		Entry oldEntry = getEntry(row);
		
		//if no entry exists at the row, return
		if(oldEntry == null) return;
		
		//list variables
		List<Books> oldBooksList = oldEntry.getBooks();
		List<Books> newBooksList = newEntry.getBooks();
		
		//name & family ID must be equal in order to execute the update
		if(newEntry.getName().equals(oldEntry.getName()) &&
				newEntry.getID() == oldEntry.getID()){
			int numNewBooks = newBooksList.size();
		
			//newEntry must have at least 1 book
			if(numNewBooks >= 1){
				
				//used math to calculate lastEntryRowNum because two distinct
				//cases have identical outcomes (entry has 0 or 1 book(s))
				int numOldBooks	 	= oldBooksList.size(),
					lastEntryRowNum = row.getRowNum() + 
						(numOldBooks >= 1 ? numOldBooks : 1) - 1,
					lastSheetRowNum = sheet.getLastRowNum();
				
				//if current entry is NOT last entry, shift entries down
				if(lastEntryRowNum < sheet.getLastRowNum())
					sheet.shiftRows(
							lastEntryRowNum + 1, lastSheetRowNum, numNewBooks);
				
				//add new books/dates to old books/dates list
				for(Books book: newBooksList) oldBooksList.add(book);
				for(String date: newEntry.getDates())
					oldEntry.getDates().add(date);
				
				//write updated oldEntry
				writeEntry(oldEntry, row);
			}
		}
	}
	

	/* Test Code */
	public static void main(String args[]){
		WriteExcel test = new WriteExcel();

		Entry LDentry = new Entry("Leung", 1927);
		test.writeEntry(LDentry,  test.getSheet().createRow(1));
		
		List<Books> bookList = new ArrayList<Books>();
		List<String> dateList = new ArrayList<String>();
		
		bookList.add(Books.ECT2); bookList.add(Books.L2P1T); bookList.add(Books.TPY4);
		dateList.add("1/5/2016"); dateList.add("3/13/2016"); dateList.add("11/26/2016");
		
		Entry JLentry = new Entry("Lim", 2000, bookList, dateList);

		test.writeEntry(JLentry, test.getSheet().createRow(2));
		
		bookList.remove(0);
		dateList.remove(0);
		
		Entry CCentry = new Entry("Cheng", 516, bookList, dateList);
		
		test.writeEntry(CCentry, test.getSheet().createRow(5));
		
		bookList.remove(0);
		dateList.remove(0);
		
		Entry JSentry = new Entry("Sy", 3210, bookList, dateList);
		
		test.writeEntry(JSentry,  test.getSheet().createRow(7));
		
		bookList.remove(0);
		dateList.remove(0);
		
		Entry CPentry = new Entry("Pung", 1021, bookList, dateList);
		
		test.writeEntry(CPentry,  test.getSheet().createRow(8));
		
		System.out.println(test.getEntry(test.getSheet().getRow(5)));

		bookList.add(Books.ECW1); dateList.add("02/21/2016");
		
		Entry UPentry = new Entry("Pung", 1021, bookList, dateList);
		
		test.updateEntry(UPentry, test.getSheet().getRow(8));
		
		test.end();
		

		try{
			Desktop.getDesktop().open(test.getFile());
		}catch(IOException e){}
	}
}
