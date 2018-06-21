package major;

/**
 * Created by chinakalight on 6/20/18.
 *
 * Main View Dashboard Controller
 *
 */

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/** Controls the main application screen */
public class MainViewDashboardController {
    @FXML private Button logoutButton;
    @FXML private Button naAddButton;
//    @FXML private Label  sessionLabel;

    @FXML private TextField naFirstName;
    @FXML private TextField naLastName;
    @FXML private TextField naOtherName;
    @FXML private TextField naPhoneNo;
    @FXML private TextArea naAddress;
    @FXML private TextField naAccountNo;
    @FXML private DatePicker naOpenDate;

    public void initialize() {}

    public void initSessionID(final AppLoginManager loginManager, String sessionID) {
//        sessionLabel.setText(sessionID);
        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                loginManager.logout();
            }
        });
    }
}
