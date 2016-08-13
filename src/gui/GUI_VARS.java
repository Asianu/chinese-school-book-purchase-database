package gui;

public interface GUI_VARS{
	/* variables used throughout all stages */
	final String STAGE_TITLE = "SDCC-NCCS Textbook Database";
	final String EXTENSION = "*.xlsx", EXT = "XLSX";
			
	/* text variables */
	/* "floating" text */
	final String SELECT_TEXT = "Select an option";
	
	/* buttons */
	final String NEW_FILE_B = "Create new file...";
	final String OLD_FILE_B = "Select existing file...";
	
	/* system text (for file paths, etc.) */
	final String INIT_DIR = "user.home";
	
	/* alerts */
	final String INV_FILE_HEADER = "ERROR: File is invalid";
	final String INV_FILE_CONTENT = "The file you are trying to open is not " + 
			"compatible with the program. Please make sure the selected file " +
			"is intended to be read with this program or, if it is, check to " +
			"make sure that it has not been corrupted.";
}
