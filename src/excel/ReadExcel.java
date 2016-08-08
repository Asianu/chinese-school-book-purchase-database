/**
 * @Author 		LeDaniel Leung
 * @Filename 	WriteExcel.java
 * @Description Reads file to data
 */

package excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class ReadExcel extends ExcelParser{
	/* local variable */
	private FileInputStream fis;
	private boolean isValid = true;

	public ReadExcel(File file){
		try{
			this.file = file;
			fis 	  = new FileInputStream(file);
			workbook  = new XSSFWorkbook(fis);
			sheet 	  = workbook.getSheet(SHEET_NAME);
		}catch(FileNotFoundException e){
			e.printStackTrace();
			isValid = false;
		}catch(IOException e){
			e.printStackTrace();
			isValid = false;
		}
	}
	
	/**
	 * @function	isValidFile
	 * @param		none
	 * @return		true if file read is valid
	 * 				false if file read is not valid
	 * @description	analyzes the file and makes sure all cells are correct
	 * 				this will be called by CALLER
	 */
	public boolean isValidFile(){
		if(!isValid) return isValid;
		else{
			
			//first row contains the labels
			Row first;
			if((first = sheet.getRow(0)) == null) return false;

			//iterate through the labels in the first row, if at any time they
			//do not comply with expected labels (written in ExcelParser.java),
			//then return false
			for(int i = 0; i < NUM_COLS; i++){
				
				//checks for Cell properties
				Cell label;
				if((label = first.getCell(i)) == null) return false; 
				else if(label.getCellType() != Cell.CELL_TYPE_STRING)
					return false;
				
				//cross checking with expected labels (ExcelParser.java)
				if(!label.getStringCellValue().equals(LABELS[i])) return false;
			}
			
			//to check if every row contains a valid entry
			Iterator<Row> rowIterator = sheet.rowIterator();
			
			//skips first row (label row)
			rowIterator.next();
			
			while(rowIterator.hasNext()){
				Row currentRow = rowIterator.next();
				
				//nameCell @ the current row is null, no entry found
				if(currentRow.getCell(NAME_COL) == null) continue;
				
				//any row with a name that does not beget an entry invalidates
				//the entire file (or it has been corrupted)
				if(getEntry(currentRow) == null) return false;
			}
			//tests passed
			return true;
		}
	}
	
	/* Test code */
/*	public static void main(String[] args){
		//WriteExcel write = new WriteExcel();

		//write.main(args);
		ReadExcel read = new ReadExcel("Mon Aug 01 2016 Book Purchase Database.xlsx");
		
		System.out.println(read.getEntry(read.getSheet().getRow(1)));

		System.out.println(read.isValidFile());
		
		if(read.isValidFile())
			try{
				Desktop.getDesktop().open(read.getFile());
			}catch(IOException e){}
	}
*/
}
