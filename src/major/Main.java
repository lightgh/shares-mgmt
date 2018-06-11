package major;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("BasicApplication.fxml"));
        primaryStage.setTitle("Hello World");
        Scene theScene = new Scene(root, 700, 790);
        primaryStage.setScene(theScene);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
