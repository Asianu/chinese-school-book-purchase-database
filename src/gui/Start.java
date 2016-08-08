package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Start extends Application{
	
	/**
	 * @function	start
	 * @param		Stage stage - the stage to be displayed
	 * @description	starts the application
	 */
	@Override
	public void start(Stage stage) throws Exception{
		welcomeStage(stage);
		
	}
	
	private void welcomeStage(Stage stage){
		//title of the window (the bar with the exit/resize/minimize buttons)
		stage.setTitle("SDCC-NCCS Textbook Database");
		
		//grid to contain all relevant parts of the application
		GridPane grid = new GridPane();
		
		//standard grid positions
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		
		//scene where grid is displayed
		Scene scene = new Scene(grid);
		stage.setScene(scene);
		
		//title of the scene
		Text title = new Text("Select an option");
		title.setFont(Font.font("Courier", FontWeight.NORMAL, 16));
		grid.add(title, 0, 0, 2, 1);
		
		//buttons to determine what the user will do
		Button newDataButton = new Button("Create new file...");
		Button oldDataButton = new Button("Select existing file...");
		HBox hbBtns = new HBox(10);
		hbBtns.setAlignment(Pos.BOTTOM_CENTER);
		hbBtns.getChildren().add(newDataButton);
		hbBtns.getChildren().add(oldDataButton);
		grid.add(hbBtns, 0, 1);
		
		//creates a WriteExcel for user to use to input data
		newDataButton.setOnAction(e->{
			
		});
		
		//reads a file, validates it, and user may display its contents
		oldDataButton.setOnAction(e->{
			
		});
		
		stage.show();
	}

	public static void main(String[] args){
		launch(args);

	}
}
