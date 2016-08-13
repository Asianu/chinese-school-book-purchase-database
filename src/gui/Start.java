package gui;

import java.io.File;

import excel.ExcelParser;
import excel.ReadExcel;
import excel.WriteExcel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
		
		stage_welcome(stage);
	}
	
	/**
	 * @function	stage_welcome
	 * @param 		stage (Stage) - where user will choose to create a new file
	 * 					or open an old one
	 * @description	Welcomes the user, asks them if they would like to create a
	 * 				new excel database or analyze an old one.
	 */
	private void stage_welcome(Stage stage){
		
		//grid to contain all relevant parts of the application
		GridPane grid = new GridPane();
		configureGrid(grid, Pos.CENTER);
		
		//title of the scene
		Text title = new Text(SELECT_TEXT);
		title.setFont(Font.font("Courier", FontWeight.NORMAL, 16));
		grid.add(title, 0, 0, 2, 1);
		
		//buttons to determine what the user will do
		Button newDataButton = new Button(NEW_FILE_B);
		Button oldDataButton = new Button(OLD_FILE_B);
		HBox hbBtns = new HBox(10);
		hbBtns.setAlignment(Pos.BOTTOM_CENTER);
		hbBtns.getChildren().add(newDataButton);
		hbBtns.getChildren().add(oldDataButton);
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
				stage_insertEntry(stage);
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
				else alert_invalidFile();
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
	private void stage_insertEntry(Stage stage){
		GridPane grid = new GridPane();
		configureGrid(grid, Pos.TOP_LEFT);
	}
	
	/**
	 * @function	alert dialog for invalid file reads
	 * @param		none
	 * @description	shows when an invalid file is read during fileChoosing
	 */
	private void alert_invalidFile(){
		Alert invalidFileAlert = new Alert(Alert.AlertType.ERROR);
		invalidFileAlert.setTitle(STAGE_TITLE);
		invalidFileAlert.setHeaderText(INV_FILE_HEADER);
		invalidFileAlert.setContentText(INV_FILE_CONTENT);
		invalidFileAlert.show();
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

	public static void main(String[] args){
		launch(args);

	}
}
