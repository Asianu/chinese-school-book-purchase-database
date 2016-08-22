package gui;

import java.io.File;
import java.util.ArrayList;
import entry_data.Books;
import entry_data.Entry;
import excel.ExcelParser;
import excel.ReadExcel;
import excel.WriteExcel;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Start extends Application implements GUI_VARS{
	/* variables */
	private File workingFile, workingDir;
	private ExcelParser parser;
	
	/**
	 * @function	start
	 * @param		stage (Stage) - the stage to be displayed
	 * @description	starts the application
	 */
	@Override
	public void start(Stage stage) throws Exception{
		//configures the stage
		stage.setTitle(STAGE_TITLE);
		stage.setOnCloseRequest(e->{
			if(parser != null) ((WriteExcel)parser).end();
		});
		
		scene_welcome(stage);
	}
	
	/**
	 * @function	stage_welcome
	 * @param 		stage (Stage) - where user will choose to create a new file
	 * 					or open an old one
	 * @description	Welcomes the user, asks them if they would like to create a
	 * 				new excel database or analyze an old one.
	 */
	private void scene_welcome(Stage stage){
		
		//grid to contain all relevant parts of the application
		GridPane grid = new GridPane();
		configureGrid(grid, Pos.CENTER);
		
		//title of the scene
		Text title = new Text(SELECT_TEXT);
		title.setFont(Font.font("Courier", FontWeight.NORMAL, 16));
		grid.add(title, 0, 0, 2, 1);
		
		//buttons to determine what the user will do
		Button newDataButton = new Button(NEW_FILE_B),
				oldDataButton = new Button(OLD_FILE_B);
		HBox hbBtns = new HBox(10);
		hbBtns.setAlignment(Pos.BOTTOM_CENTER);
		hbBtns.getChildren().addAll(newDataButton, oldDataButton);
		grid.add(hbBtns, 0, 1);
		
		//creates a WriteExcel for user to use to input data
		newDataButton.setOnAction(e->{
			
			//configures dirChooser
			DirectoryChooser dirChooser = new DirectoryChooser();
			dirChooser.setInitialDirectory(new File(
					System.getProperty(INIT_DIR)));
			
			workingDir = dirChooser.showDialog(stage);
			if(workingDir != null){
				parser = new WriteExcel(workingDir.getAbsolutePath());
				scene_insertEntry(stage);
			}
		});
		
		//reads a file, validates it, and user may display its contents
		oldDataButton.setOnAction(e->{

			//configures fileChooser
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File(
					System.getProperty(INIT_DIR)));
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter(EXT, EXTENSION));

			workingFile = fileChooser.showOpenDialog(stage);
			if(workingFile != null){
				if(((ReadExcel)(parser = 
						new ReadExcel(workingFile))).isValidFile()){
					parser = ((ReadExcel)parser).wE;
				}
				else alert_error_invalidFile();
			}
		});
		
		//scene where grid is displayed
		Scene scene = new Scene(grid);
		stage.setScene(scene);
		
		stage.show();
	}
	
	/**
	 * @function	stage_insertEntry
	 * @param 		stage (Stage) - where scene is displayed
	 * @description	This stage appears either: after user selects to create a
	 * 				new file, or if the user decides to insert a new entry or
	 * 				update an old entry (during read).
	 */
	private void scene_insertEntry(Stage stage){
		stage.setTitle(INSERT_ENTRY_TITLE);
		
		GridPane grid = new GridPane();
		configureGrid(grid, Pos.TOP_LEFT);
		
		//used if there are multiple ComboBoxes
		ArrayList<ComboBox<Books>> chooseBookList = 
				new ArrayList<ComboBox<Books>>();
		ArrayList<TextField> chooseDateList = new ArrayList<TextField>();
		
		Button nextButton = new Button(NEXT),
				backButton = new Button(BACK),
				cancelButton = new Button(CANCEL);

		HBox hbtns = new HBox(5);
		hbtns.setAlignment(Pos.TOP_RIGHT);
		hbtns.getChildren().addAll(nextButton, backButton, cancelButton);
		
		grid.add(hbtns, 2, 0);
		
		Label nameLabel = new Label(NAME_LABEL),
				IDLabel = new Label(ID_LABEL),
				bookLabel = new Label(BOOK_LABEL),
				dateLabel = new Label(DATE_LABEL),
				plusLabel = new Label(PLUS_LABEL),
				minusLabel = new Label(MINUS_LABEL);
		
		grid.add(nameLabel, 1, 1);
		grid.add(IDLabel, 2, 1);
		grid.add(bookLabel, 1, 4);
		grid.add(dateLabel, 2, 4);
		
		grid.add(plusLabel, 0, 6);
		
		TextField IDField = new TextField(),
				nameField = new TextField(),
				dateField = new TextField();

		IDField.setPromptText(ID_FIELD);
		nameField.setPromptText(NAME_FIELD);
		dateField.setPromptText(DATE_FIELD);
		
		grid.add(nameField, 1, 2);
		grid.add(IDField, 2, 2);
		grid.add(dateField, 2, 5);
		
		//this will always exist, cannot be removed by minusLabel
		ObservableList<Books> oBookList = 
				FXCollections.observableArrayList(Books.getAllBooks());
		final ComboBox<Books> chooseBook = new ComboBox<Books>(oBookList);

		grid.add(chooseBook, 1, 5);
		
		//array lists used for possibility of multiple books added
		chooseBookList.add(chooseBook);
		chooseDateList.add(dateField);
		
		
		Scene scene = new Scene(grid);
		stage.setScene(scene);

		//used to determine number of rows, 0 will be minimum
		ArrayList<Integer> gridSize = new ArrayList<Integer>();
		gridSize.add(getRowCount(grid));

		//setting actions for next/back/cancel buttons
		//next will collect all entry data and send it to next scene
		nextButton.setOnAction(e->{
			//used to determine if any errors were found, if so then do not
			//continue to the next stage
			boolean error = false;

			String name;
			int ID;
			ArrayList<Books> bookList;
			ArrayList<String> dateList = new ArrayList<String>();
			
			//first, check to see if all dates were correctly formatted
			for(TextField tmpDateField: chooseDateList){
				String date = tmpDateField.getText();
				if(date.length() != 10) error = true;
				else if(!(date.substring(2,3).equals("/") && 
						date.substring(5,6).equals("/"))) error = true;
				else{
					int day = 0, month = 0, year = 0;
					try{
						day = Integer.parseInt(date.substring(0,2));
						month = Integer.parseInt(date.substring(3,5));
						year = Integer.parseInt(date.substring(6));
					}catch(NumberFormatException ex){
						error = true;
					}
					
					if(!error){
						
						//adding restrictions to day/month/year values
						if(day <= 0 || day > 31 || 
								month <= 0 || month > 12 || 
								year < 2000 || year > 2100) error = true;
						
						//adds the date if it passes all checks
						else dateList.add(date);
					}
				}
				
				//if ever error is set, show proper alert and do nothing
				if(error) alert_error_invalidDateFormat();
				else{
					bookList = new ArrayList<Books>();
					
					//if name is blank, show appropriate error message
					name = nameField.getText();
					if(name.compareTo("") <= 0){
						alert_error_invalidNameFormat();
						break;
					}
					
					//ID must be a number
					try{
						ID = Integer.parseInt(IDField.getText());
					}catch (NumberFormatException ex){
						alert_error_invalidIDFormat();
						break;
					}
					
					//collect all the selected books
					for(ComboBox<Books> bookBox: chooseBookList)
						bookList.add(bookBox.getValue());
					
					//inserts the new entry with proper parameters
					((WriteExcel)parser).insertEntry(
							new Entry(name, ID, bookList, dateList));
				}
			}
		});
		
		//back will go back a scene, which happens to be scene_welcome
		backButton.setOnAction(e->{
			cancelButton.fire();
		});
		
		//cancel will go back to scene_welcome
		cancelButton.setOnAction(e->{
			scene_welcome(stage);
		});
		
		//setting actions for the plus and minus labels
		//plus will add a new row for user to add an additional book to the list
		plusLabel.setOnMouseClicked(e->{
			int numRows = gridSize.get(gridSize.size() - 1);
			if(numRows - 1 < MAX_BOOK_ROW){
				final ComboBox<Books> tmpChooseBook = 
						new ComboBox<Books>(oBookList);
				TextField tmpDateField = new TextField();
				
				tmpDateField.setPromptText(DATE_FIELD);
				
				//adds one to gridSize's last size and adds it to gridSize
				gridSize.add(gridSize.get(gridSize.size() - 1) + 1);
				
				grid.add(tmpChooseBook, 1, numRows - 1);
				grid.add(tmpDateField, 2, numRows - 1);

				//removes the "+", and if valid, then re-add it one row lower
				grid.getChildren().remove(plusLabel);
				if(numRows + 1 != MAX_BOOK_ROW)
					grid.add(plusLabel, 0, numRows);
				
				//removes the "-", and sets it to the final row
				grid.getChildren().remove(minusLabel);
				grid.add(minusLabel, 0, numRows - 1);
				
				chooseBookList.add(tmpChooseBook);
				chooseDateList.add(tmpDateField);
				
				stage.sizeToScene();
			}
		});
		
		//minus will delete the row that the minus is part of and shift
		//(if necessary)
		minusLabel.setOnMouseClicked(e->{
			int numRows = gridSize.get(gridSize.size() - 1);
			
			//first remove "-" label, determine if should be re-added later
			grid.getChildren().remove(minusLabel);
			grid.getChildren().remove(plusLabel);
			grid.getChildren().remove(
					chooseBookList.remove(chooseBookList.size() - 1));
			grid.getChildren().remove(
					chooseDateList.remove(chooseDateList.size() - 1));
			
			//remove the last entry in gridSize (update the size)
			gridSize.remove(gridSize.size() - 1);
			
			//always put the "+" label back
			grid.add(plusLabel, 0, numRows - 2);
			
			//add the "-" label back if minimum #s of rows not reached
			if(numRows - 3 >= MIN_BOOK_ROW)
				grid.add(minusLabel, 0, numRows - 3);
			
			stage.sizeToScene();
		});
		
		stage.show();
	}
	
	/**
	 * @function	alert_error_invalidFile
	 * @param		none
	 * @description	shows when an invalid file is read during fileChoosing
	 */
	private void alert_error_invalidFile(){
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
	private void alert_error_invalidDateFormat(){
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
	private void alert_error_invalidIDFormat(){
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
	private void alert_error_invalidNameFormat(){
		Alert invalidNameFormatAlert = new Alert(Alert.AlertType.ERROR);
		invalidNameFormatAlert.setTitle(STAGE_TITLE);
		invalidNameFormatAlert.setHeaderText(INV_NAME_HEADER);
		invalidNameFormatAlert.setContentText(INV_NAME_CONTENT);
		invalidNameFormatAlert.show();
	}
	
	/**
	 * @function	configureGrid
	 * @param 		grid (GridPane) - the GridPane to be configured
	 * @param 		position (Pos) - the alignment that the Grid will have
	 * @description	Takes in a GridPane and configures it. This will allow all
	 * 				scenes to have a standard layout.
	 */
	private void configureGrid(GridPane grid, Pos position){
		grid.setAlignment(position);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
	}
	
	/**
	 * @function	getRowCount
	 * @param 		grid (GridPane) - the GridPane from which rows are counted
	 * @return		numRows (int) - the number of rows that the GridPane contains
	 */
	private int getRowCount(GridPane grid) {
        int numRows = grid.getRowConstraints().size();
        for (int i = 0; i < grid.getChildren().size(); i++) {
            Node child = grid.getChildren().get(i);
            if (child.isManaged()) {
                Integer rowIndex = GridPane.getRowIndex(child);
                if(rowIndex != null){
                    numRows = Math.max(numRows,rowIndex+1);
                }
            }
        }
        return numRows;
    }

	public static void main(String[] args){
		launch(args);

	}
}
