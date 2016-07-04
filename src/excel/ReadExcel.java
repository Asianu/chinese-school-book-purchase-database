/**
 * @Author 		LeDaniel Leung
 * @Filename 	WriteExcel.java
 * @Description Reads file to data
 */

package excel;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;

public class ReadExcel{

	/* variables */
	private FileInputStream file;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	public ReadExcel(String filename){
		
	}
	
	public static void main(String[] args){

	}

}
