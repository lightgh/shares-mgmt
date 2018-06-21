package major;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

/** Shares Management Application class Entry Section */
public class SharesManagementApplication extends Application {
    public static void main(String[] args) { launch(args); }
        @Override public void start(Stage stage) throws IOException {
            Scene scene = new Scene(new StackPane());

            AppLoginManager appLoginManager = new AppLoginManager(scene, stage);
            appLoginManager.showLoginScreen();

            stage.setScene(scene);
            stage.show();

    }
}