/**
 * @author 		LeDaniel Leung
 * @filename	Start.java
 * @description	The application that will run
 */

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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
	
	private Alerts alert;
	
	/**
	 * @function	start
	 * @param		stage (Stage) - the stage to be displayed
	 * @description	starts the application
	 */
	@Override
	public void start(Stage stage) throws Exception{
		//configures the stage
		stage.setTitle(STAGE_TITLE);
		
		//default (and minimum) stage size
		stage.setMinWidth(STAGE_SIZE);
		stage.setMinHeight(STAGE_SIZE);
		
		stage.setOnCloseRequest(e->{
			if(parser != null) ((WriteExcel)parser).end();
		});
		
		alert = new Alerts();
		
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
					scene_viewEntries(stage);
				}
				else alert.alert_error_invalidFile();
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

		
		/**********************************************************************
		 * The following code creates and positions the labels for this scene.
		 */
		
		Label nameLabel = new Label(NAME_LABEL),
				IDLabel = new Label(ID_LABEL),
				bookLabel = new Label(BOOK_LABEL),
				dateLabel = new Label(DATE_LABEL);
		
		grid.add(nameLabel, 1, 1);
		grid.add(IDLabel, 2, 1);
		grid.add(bookLabel, 1, 4);
		grid.add(dateLabel, 2, 4);
		
		
		/**********************************************************************
		 * The following code creates and positions the textFields for this
		 * scene.
		 */
		
		TextField IDField = new TextField(),
				nameField = new TextField(),
				dateField = new TextField();

		IDField.setPromptText(ID_FIELD);
		nameField.setPromptText(NAME_FIELD);
		dateField.setPromptText(DATE_FIELD);
		
		grid.add(nameField, 1, 2);
		grid.add(IDField, 2, 2);
		grid.add(dateField, 2, 5);

		
		/**********************************************************************
		 * The following code creates the comboBox(es) that are used to
		 * determine the books that the entry bought. The ArrayLists are used
		 * in case the entry buys multiple books.
		 */		
		
		//used if there are multiple ComboBoxes
		ArrayList<ComboBox<Books>> chooseBookList = 
				new ArrayList<ComboBox<Books>>();
		ArrayList<TextField> chooseDateList = new ArrayList<TextField>();
		
		//this will always exist, cannot be removed by minusLabel
		ObservableList<Books> oBookList = 
				FXCollections.observableArrayList(Books.getAllBooks());
		final ComboBox<Books> chooseBook = new ComboBox<Books>(oBookList);
		chooseBook.setValue(Books.NULL);

		grid.add(chooseBook, 1, 5);
		
		//array lists used for possibility of multiple books added
		chooseBookList.add(chooseBook);
		chooseDateList.add(dateField);
		
		
		/**********************************************************************
		 * The following code creates and formats the next/cancel buttons that
		 * will be located at the top-right of every scene.
		 */
		
		Button nextButton = new Button(NEXT),
				cancelButton = new Button(CANCEL);

		HBox hbtns = new HBox(5);
		hbtns.setAlignment(Pos.TOP_RIGHT);
		hbtns.getChildren().addAll(nextButton, cancelButton);
		
		grid.add(hbtns, 2, 0);

		//setting actions for next/cancel buttons
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
						month = Integer.parseInt(date.substring(0,2));
						day = Integer.parseInt(date.substring(3,5));
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
			}

				
			//if ever error is set, show proper alert and do nothing
			if(error) alert.alert_error_invalidDateFormat();
			else{
				bookList = new ArrayList<Books>();

				//if name is blank, show appropriate error message
				name = nameField.getText();
				if(name.compareTo("") <= 0){
					alert.alert_error_invalidNameFormat();
					return;
				}

				//ID must be a number
				try{
					ID = Integer.parseInt(IDField.getText());
				}catch (NumberFormatException ex){
					alert.alert_error_invalidIDFormat();
					return;
				}

				//collect all the selected books
				for(ComboBox<Books> bookBox: chooseBookList)
					bookList.add(bookBox.getValue());

				//inserts the new entry with proper parameters
				((WriteExcel)parser).insertEntry(
						new Entry(name, ID, bookList, dateList));

				//asks user if an additional entry will be inserted
				if(alert.alert_confirmation_additionalEntry())
					scene_insertEntry(stage);
				else{
					scene_viewEntries(stage);
				}
			}

		});
		
		//cancel will go back (or skip to) viewEntreis
		cancelButton.setOnAction(e->{
			scene_viewEntries(stage);
		});
		

		/**********************************************************************
		 * The following code creates and formats the "+" and "-" labels that
		 * are used in this scene. This controls the number of books that the
		 * user can enter. The max number of books is determined by the max
		 * number of rows that the gridPane may have (specified in GUI_VARS).
		 */
		
		Label plusLabel = new Label(PLUS_LABEL),
				minusLabel = new Label(MINUS_LABEL);
		
		grid.add(plusLabel, 0, 6);

		//used to determine number of rows, 0 will be minimum
		ArrayList<Integer> gridSize = new ArrayList<Integer>();
		gridSize.add(getRowCount(grid));
		
		//setting actions for the plus and minus labels
		//plus will add a new row for user to add an additional book to the list
		plusLabel.setOnMouseClicked(e->{
			int numRows = gridSize.get(gridSize.size() - 1);
			if(numRows - 1 < MAX_BOOK_ROW){
				final ComboBox<Books> tmpChooseBook = 
						new ComboBox<Books>(oBookList);
				tmpChooseBook.setValue(Books.NULL);
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
		
		
		Scene scene = new Scene(grid);
		stage.setScene(scene);
		
		stage.show();
	}
	
	/**
	 * @function		scene_viewEntries
	 * @param			stage (Stage) the stage where the entries are displayed
	 * @description		sets the stage to display all the entries in the file
	 */
	@SuppressWarnings("unchecked")
	private void scene_viewEntries(Stage stage){
		stage.setTitle(VIEW_ENTRIES_TITLE);
		
		GridPane grid = new GridPane();
		configureGrid(grid, Pos.TOP_CENTER);
		
		
		/**********************************************************************
		 * The following code creates and formats the table. The size of the
		 * table is bound to the size of the stage, and will resize itself with
		 * it. The columns will likewise bind its size to the table.
		 */
		
		TableView<Entry> table = new TableView<Entry>();
		ObservableList<Entry> entryList = 
				FXCollections.observableArrayList(parser.getAllEntries());
		
		table.setItems(entryList);
		
		//creating column for the names
		TableColumn<Entry, String> nameCol = 
				new TableColumn<Entry, String>(NAME);
		nameCol.setCellValueFactory(
				new PropertyValueFactory<Entry, String>("name"));
		nameCol.prefWidthProperty().bind(table.widthProperty().multiply(.2));
		
		//creating column for the IDs
		TableColumn<Entry, Integer> IDCol = 
				new TableColumn<Entry, Integer>(ID);
		IDCol.setCellValueFactory(
				new PropertyValueFactory<Entry, Integer>("ID"));
		IDCol.prefWidthProperty().bind(table.widthProperty().multiply(.1));
		
		//creating column for the books
		TableColumn<Entry, String> bookCol = 
				new TableColumn<Entry, String>(BOOKS);
		bookCol.setCellValueFactory(
				new PropertyValueFactory<Entry, String>("booksStr"));
		bookCol.prefWidthProperty().bind(table.widthProperty().multiply(.5));
		
		//creating column for the dates
		TableColumn<Entry, String> dateCol =
				new TableColumn<Entry, String>(DATES);
		dateCol.setCellValueFactory(
				new PropertyValueFactory<Entry, String>("datesStr"));
		dateCol.prefWidthProperty().bind(table.widthProperty().multiply(.2));
		
		table.getColumns().setAll(nameCol, IDCol, bookCol, dateCol);

		//binding the size of the table to be proportional to the stage's size
		table.prefWidthProperty().bind(stage.widthProperty().multiply(.8));
		table.prefHeightProperty().bind(stage.heightProperty().multiply(.8));
		
		grid.add(table, 0, 1, 8, 1);
		
		
		/**********************************************************************
		 * The following code takes care of the search field existing in this
		 * scene. As the user types in the person's name, the table will change
		 * its content accordingly.
		 */
		
		TextField searchField = new TextField();
		searchField.setPromptText(SEARCH_FIELD);
		
		//typing in the searchField will restrict the search results to match
		//whatever the search query is
		searchField.textProperty().addListener((observable, oldValue, newValue) ->{
			String query = searchField.getText().toLowerCase();
			ArrayList<Entry> tmpEntryList = new ArrayList<Entry>();
			
			//goes through and adds only the entries that match the search
			//query
			for(Entry entry: parser.getAllEntries()){
				String name = entry.getName().toLowerCase();

				if(query.length() <= name.length())
					if(query.equals(name.substring(0, query.length())))
						tmpEntryList.add(entry);
			}
			
			table.setItems(FXCollections.observableArrayList(tmpEntryList));
		});
		
		grid.add(searchField, 0, 0, 3, 1);
		
		
		/**********************************************************************
		 * The following code formats and places the buttons into the gridPane.
		 * At this stage, user will be able to insert a new entry, or go back
		 * to the beginning (scene_welcome).
		 */
		
		Button newButton = new Button(NEW);
		
		HBox hbtns = new HBox(5);
		
		hbtns.setAlignment(Pos.TOP_RIGHT);
		hbtns.getChildren().addAll(newButton);
		
		//manually adding a buffer between search bar and buttons
		grid.add(new Label("                              "), 3, 0, 3, 1);
		grid.add(hbtns, 6, 0, 2, 1);
		
		//newButton goes to insertEntry's scene to let user create a new entry
		newButton.setOnAction(e->{
			scene_insertEntry(stage);
		});
		
		Scene scene = new Scene(grid);
		stage.setScene(scene);
		
		stage.show();
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
	private int getRowCount(GridPane grid){
        int numRows = grid.getRowConstraints().size();
        for(int i = 0; i < grid.getChildren().size(); i++){
            Node child = grid.getChildren().get(i);
            if (child.isManaged()){
                Integer rowIndex = GridPane.getRowIndex(child);
                if(rowIndex != null){
                    numRows = Math.max(numRows, rowIndex + 1);
                }
            }
        }
        return numRows;
    }

	public static void main(String[] args){
		launch(args);

	}
}
