package major;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

/** Shares Management Application class Entry Section */
public class SharesManagementApplication extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage stage) throws IOException {
            Scene scene = new Scene(new StackPane());

            AppLoginManager appLoginManager = new AppLoginManager(scene, stage);
            //TODO - FIX THIS LATER
//            appLoginManager.showLoginScreen();
            appLoginManager.authenticated("");

            stage.setScene(scene);
            stage.show();

    }

    @Override
    public void init() throws Exception {
        super.init();
        CustomUtility.println("INITIALIZING");
    }

    @Override
    public void stop() throws Exception {
        CustomUtility.AlertHelper("Close Application Confirmation", "Closing Application Confirmation",
                "Are You Sure That You Want To Exit", Alert.AlertType.CONFIRMATION).show();
        CustomUtility.println("CLOSING");
//        super.stop();
    }
}