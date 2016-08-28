/**
 * @author		LeDaniel Leung
 * @filename	GUI_VARS.java
 * @description	Interface used to hold all variable constants used
 * 				in Start.java
 */

package gui;

public interface GUI_VARS{
	final String
	/* variables used throughout all stages */
	NAME = "Name", ID = "ID", BOOKS = "Books", DATES = "Dates",
	STAGE_TITLE = "SDCC-NCCS Textbook Database",
	EXTENSION = "*.xlsx", EXT = "XLSX",
	NEXT = "Next", CANCEL = "Cancel", BACK = "Back",
	YES = "Yes", NO = "No",

	/* text variables */
	/* misc. titles */
	INSERT_ENTRY_TITLE = "Insert an Entry",
	VIEW_ENTRIES_TITLE = "Search for an Entry",

	/* "floating" text */
	SELECT_TEXT = "Select an option",

	/* buttons */
	NEW_FILE_B = "Create new file...",
	OLD_FILE_B = "Select existing file...",

	/* labels */
	NAME_LABEL = "Name: ",
	ID_LABEL = "ID: ",
	BOOK_LABEL = "Book(s) Purchased: ",
	DATE_LABEL = "Date(s): ",
	PLUS_LABEL = "+",
	MINUS_LABEL = "-",
	BLANK_LABEL = " ",

	/* text fields */
	NAME_FIELD = "Last Name",
	ID_FIELD = "####",
	DATE_FIELD = "MM/DD/YYYY",

	/* system text (for file paths, etc.) */
	INIT_DIR = "user.home",

	/* alerts */
	INV_FILE_HEADER = "ERROR: File is invalid",
	INV_DATE_HEADER = "ERROR: Date Format is invalid",
	INV_ID_HEADER = "ERROR: ID is invalid",
	INV_NAME_HEADER = "ERROR: Name is invalid",
	CON_ADD_ENTRY_HEADER = "Enter another Entry",

	INV_FILE_CONTENT = "The file you are trying to open is not " + 
			"compatible with the program. Please make sure the selected file " +
			"is intended to be read with this program or, if it is, check to " +
			"make sure that it has not been corrupted.",
	INV_DATE_CONTENT = "The format for all dates must be in MM/DD/YYYY " +
			"format.\n\nFor example, for the date January 2, 2001, the " +
			"inputted date format would be: 01/02/2001.",
	INV_ID_CONTENT = "The family's ID must be input as a number",
	INV_NAME_CONTENT = "The family's name cannot be left empty",
	CON_ADD_ENTRY_CONTENT = "Would you like to create another entry?";

	final int
	/* used for ChooseBox de/increments */
	MIN_BOOK_ROW = 6, MAX_BOOK_ROW = 12;
}
