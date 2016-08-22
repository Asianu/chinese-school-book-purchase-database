package gui;

import javafx.scene.control.Alert;

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
}
