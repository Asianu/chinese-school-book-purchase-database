/**
 * @author		LeDaniel Leung
 * @filename	Alerts.java
 * @description	Class that holds the Alerts used by Start.java
 */

package gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import entry_data.Books;
import entry_data.Entry;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

public class Alerts implements GUI_VARS{

	/**
	 * @function	alert_error_invalidFile
	 * @param		none
	 * @description	shows when an invalid file is read during fileChoosing
	 */
	public void alert_error_invalidFile(){
		Alert invalidFileAlert = new Alert(Alert.AlertType.ERROR);
		invalidFileAlert.setTitle(STAGE_TITLE);
		invalidFileAlert.setHeaderText(INV_FILE_HEADER);
		invalidFileAlert.setContentText(INV_FILE_CONTENT);
		invalidFileAlert.show();
	}
	
	/**
	 * @function	alert_error_invalidDateFormat
	 * @param		none
	 * @description	shows when a date is not formatted correctly
	 */
	public void alert_error_invalidDateFormat(){
		Alert invalidDateFormatAlert = new Alert(Alert.AlertType.ERROR);
		invalidDateFormatAlert.setTitle(STAGE_TITLE);
		invalidDateFormatAlert.setHeaderText(INV_DATE_HEADER);
		invalidDateFormatAlert.setContentText(INV_DATE_CONTENT);
		invalidDateFormatAlert.show();
	}
	
	/**
	 * @function	alert_error_invalidIDFormat
	 * @param		none
	 * @description	shows when a ID is not formatted correctly
	 */
	public void alert_error_invalidIDFormat(){
		Alert invalidIDFormatAlert = new Alert(Alert.AlertType.ERROR);
		invalidIDFormatAlert.setTitle(STAGE_TITLE);
		invalidIDFormatAlert.setHeaderText(INV_ID_HEADER);
		invalidIDFormatAlert.setContentText(INV_ID_CONTENT);
		invalidIDFormatAlert.show();
	}
	
	/**
	 * @function	alert_error_invalidNameFormat
	 * @param		none
	 * @description	shows when nameField is empty
	 */
	public void alert_error_invalidNameFormat(){
		Alert invalidNameFormatAlert = new Alert(Alert.AlertType.ERROR);
		invalidNameFormatAlert.setTitle(STAGE_TITLE);
		invalidNameFormatAlert.setHeaderText(INV_NAME_HEADER);
		invalidNameFormatAlert.setContentText(INV_NAME_CONTENT);
		invalidNameFormatAlert.show();
	}

	/**
	 * @function	alert_confirmation_additionalEntry
	 * @param		none
	 * @return 		true if "yes" is selected, false if "no" is selected
	 * @description	opens up a confirmation dialog to determine if the user
	 * 				would like to input a new entry
	 */
	public boolean alert_confirmation_additionalEntry(){
		Alert additionalEntryAlert = new Alert(Alert.AlertType.CONFIRMATION);
		additionalEntryAlert.setTitle(STAGE_TITLE);
		additionalEntryAlert.setHeaderText(CON_ADD_ENTRY_HEADER);
		additionalEntryAlert.setContentText(CON_ADD_ENTRY_CONTENT);
		
		//buttons with custom actions
		ButtonType yesButton = new ButtonType(YES, ButtonData.YES),
				noButton = new ButtonType(NO, ButtonData.NO);

		additionalEntryAlert.getButtonTypes().setAll(yesButton, noButton);
		
		Optional<ButtonType> result = additionalEntryAlert.showAndWait();
		
		if(result.get() == yesButton) return true;
		else return false;
	}
	
	/**
	 * @function	alert_information_bookCount
	 * @param		takes in a list of entries.
	 * @description	takes in the list of entries, counts books, and displays
	 * 				information regarding them.
	 */
	public void alert_information_bookCount(ArrayList<Entry> entries){
		Alert bookCountAlert = new Alert(Alert.AlertType.INFORMATION);
		bookCountAlert.setTitle(STAGE_TITLE);
		bookCountAlert.setHeaderText(INFO_BOOK_COUNT_HEADER);
		
		//treeMap will keep track of the books and their counts
		//Books will be the key, and Integer (count) will be the value
		TreeMap<Books, Integer> bookCount = new TreeMap<Books, Integer>();
		
		//loops through existing entries
		for(Entry entry: entries){
			List<Books> bookList = entry.getBooks();
			
			//loops through the bookList of each entry
			for(Books book: bookList){
				
				//increments count of existing books
				if(bookCount.containsKey(book))
					bookCount.put(book, bookCount.get(book) + 1);
				
				//otherwise put the book in
				else
					bookCount.put(book, 1);
			}
		}

		//begin building the string to display in the alert
		String stats = "";
		int total = 0;

		Set<Books> keys = bookCount.keySet();
		Iterator<Books> keysIter = keys.iterator();
		
		while(keysIter.hasNext()){
			Books book = keysIter.next();
			if(book.equals(Books.NULL)) continue;
			
			stats += book.toString() + ": " + bookCount.get(book) + "\n";
			total += bookCount.get(book);
		}
		
		stats += "\n" + TOTAL + total;
		
		if(total == 0) stats = INFO_BOOK_COUNT_CONTENT;
		
		bookCountAlert.setContentText(stats);
		bookCountAlert.show();
	}

}