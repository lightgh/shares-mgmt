package major;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/** Shares Management Application class Entry Section */
public class SharesManagementApplication extends Application {
    final static Logger logger = Logger.getLogger(SharesManagementApplication.class.getName());

    static {
        SystemOutToLog4.enableForClass(SharesManagementApplication.class);
    }

    public SharesManagementApplication(){

    }

    public static void main(String[] args) { launch(args); }

    Image img;

    @Override
    public void start(Stage stage) throws IOException {
        logger.debug("Hello this is a debug message");
        logger.info("Starting Scene initialization");
        Scene scene = new Scene(new StackPane());

        AppLoginManager appLoginManager = new AppLoginManager(scene, stage);

            img = new Image(this.getClass().getResourceAsStream("images/co-op-stronger-together_icon.png"));

            logger.info("About Showing Login Screen");
            appLoginManager.showLoginScreen();
            stage.getIcons().add(img);

//            appLoginManager.authenticated("");


            stage.setOnCloseRequest(e-> {
                e.consume();
                boolean answer = false;

                try {

                    answer = CustomUtility.ConfirmationAlertHelper("Close Application Confirmation",
                            "Are You Sure that you want to Exit this Application");

                    if(answer){
                        stage.close();
                        System.exit(0);
                    }

                } catch (IOException e1) {
                    logger.trace(e1);
                }
            });

//            BufferedImage image = ImageIO.read(this.getClass().getResourceAsStream("images/co-op-stronger-together.jpg"));
            stage.setScene(scene);
            stage.show();

    }

    @Override
    public void init() throws Exception {
        super.init();
        img = new Image(this.getClass().getResourceAsStream("images/co-op-stronger-together_icon.png"));
        logger.info("INITIALIZING");
    }

    @Override
    public void stop() throws Exception {
        CustomUtility.AlertHelper("Close Application Confirmation", "Closing Application Confirmation",
                "Are You Sure That You Want To Exit", Alert.AlertType.CONFIRMATION).show();
        logger.info("CLOSING");
//        super.stop();
    }
}