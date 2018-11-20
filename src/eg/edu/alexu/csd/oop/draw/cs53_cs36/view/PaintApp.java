package eg.edu.alexu.csd.oop.draw.cs53_cs36.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PaintApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		 FXMLLoader loader = new FXMLLoader(getClass().getResource("Paint.fxml"));
			Parent root = loader.load();
			Controller controller = (Controller)loader.getController();
			controller.init(primaryStage);
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

	}
    public static void main(String[] args) {
        launch(args); 
    } 
}
