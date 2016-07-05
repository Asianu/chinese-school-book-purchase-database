/**
 * @Author 		LeDaniel Leung
 * @Filename 	WriteExcel.java
 * @Description Reads file to data
 */

package excel;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;

public class ReadExcel extends ExcelParser{
	/* local variable */
	private FileInputStream fis;

	public ReadExcel(String filename){
		this.filename = filename;
		try{
			file 	 = new File(filename);
			fis 	 = new FileInputStream(file);
			workbook = new XSSFWorkbook(file);
			sheet 	 = workbook.getSheet(SHEET_NAME);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(InvalidFormatException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		//WriteExcel write = new WriteExcel();

		//write.main(args);
		ReadExcel read = new ReadExcel("Mon Jul 04 2016 Book Purchase Database.xlsx");
		
		System.out.println(read.getEntry(read.getSheet().getRow(1)));

		try{
			Desktop.getDesktop().open(read.getFile());
		}catch(IOException e){}
	}

}
