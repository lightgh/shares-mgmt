package major;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/** Controls the login screen */
public class AppLoginController {
    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private Button loginButton;

    private AppLoginManager appLoginManager;

    public void initialize() {}

    public void initManager(final AppLoginManager appLoginManager) {
        this.appLoginManager = appLoginManager;

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                String sessionID = authorize();
                if (sessionID != null) {
                    appLoginManager.authenticated(sessionID);
                }
            }
        });
    }

    /**
     * Check authorization credentials.
     *
     * If accepted, return a sessionID for the authorized session
     * otherwise, return null.
     */
    private String authorize() {

        Task<Boolean> task = new Task<Boolean>() {

            @Override protected Boolean call() throws Exception {

                CustomUtility cu = new CustomUtility();

                boolean outcome =  cu.validateCredentials(username.getText().toString(), password.getText().toString());

                updateMessageA(outcome);

                return outcome;

            }

            @Override protected void succeeded() {
                super.succeeded();
            }

            @Override protected void cancelled() {
                super.cancelled();
            }

            @Override protected void failed() {
                super.failed();
            }
        };

        task.run();

        return null;
    }

    private static int sessionID = 0;

    private void updateMessageA(boolean successful){
        if(successful){
            appLoginManager.authenticated(generateSessionID());
        }else{
            CustomUtility.AlertHelper("Login Information", "Login Was Unsuccessful",
                    "Invalid Credentials", Alert.AlertType.ERROR).show();
        }
    }

    private String generateSessionID() {
        sessionID++;
        return "xyzzy - session " + sessionID;
    }
}