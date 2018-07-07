package major;

/**
 * Created by chinakalight on 6/20/18.
 */

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Manages control flow for logins */
public class AppLoginManager {
    private Scene scene;
    private Stage stage;

    private double stageWidth;
    private double stageHeight;
    private final int ZERO = 0;


    public AppLoginManager(Scene scene, Stage stage) {
        this.scene = scene;
        this.stage = stage;
    }

    /**
     * Callback method invoked to notify that a user has been authenticated.
     * Will show the main application screen.
     */
    public void authenticated(String sessionID) {
        showMainView(sessionID);
    }

    /**
     * Callback method invoked to notify that a user has logged out of the main application.
     * Will show the login application screen.
     */
    public void logout() {
        showLoginScreen();
    }

    public void showLoginScreen() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("AppLoginLayout.fxml")
            );
            scene.setRoot((Parent) loader.load());
            AppLoginController controller =
                    loader.<AppLoginController>getController();
            controller.initManager(this);
            if(this.stageWidth > ZERO || this.stageHeight > ZERO){
                this.stage.setMaximized(false);
                this.stage.setMinWidth(stageWidth);
                this.stage.setMinHeight(stageHeight);
                this.stage.setWidth(stageWidth);
                this.stage.setHeight(stageHeight);
                this.stage.setTitle("Application Login Menu");
                this.stage.sizeToScene();
            }else{

                this.stage.setMinWidth(700);
                this.stage.setMinHeight(490);
                this.stage.setResizable(true);
                this.stage.sizeToScene();
                this.stage.setTitle("Application Login Menu");

            }
        } catch (IOException ex) {
            Logger.getLogger(AppLoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showMainView(String sessionID) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("MainViewDashboard.fxml")
            );
            scene.setRoot((Parent) loader.load());
            MainViewDashboardController controller =
                    loader.<MainViewDashboardController>getController();
            controller.initSessionID(this, sessionID);
            this.stageWidth = stage.getWidth();
            this.stageHeight = stage.getHeight();
            this.stage.setResizable(true);
            this.stage.sizeToScene();
            this.stage.setMaximized(true);
            this.stage.setTitle("Shares Application Dashboard");

        } catch (IOException ex) {
            Logger.getLogger(AppLoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
