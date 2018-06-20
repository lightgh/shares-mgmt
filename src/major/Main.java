package major;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Optional;

public class Main extends Application {

    Button loginButton;
    TextField userName;
    PasswordField userPassword;
    Scene loginScene;
    Scene dashboardScene;
    Stage primaryStage;
    Parent root;


    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;

        loginScene = new Scene(FXMLLoader.load(getClass().getResource("LoginLayout.fxml")));

        loginButton = (Button) loginScene.lookup("#loginButton");
        userName  = (TextField) loginScene.lookup("#username");
        userPassword  = (PasswordField) loginScene.lookup("#userpassword");

        loginButton.setOnAction(event -> {

            ((Button)event.getSource()).setDisable(true);

            Task<Boolean> task = new Task<Boolean>() {

                @Override protected Boolean call() throws Exception {

                    CustomUtility cu = new CustomUtility();

                    boolean outcome =  cu.validateCredentials(userName.getText().toString(), userPassword.getText().toString());

                    if(outcome)
                        updateMessageA(ActionStates.Succeded);
                    else
                        updateMessageA(ActionStates.Failed);

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


        });



        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Login");
        primaryStage.show();
        primaryStage.sizeToScene();
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());
        primaryStage.setResizable(false);

//        Parent root = FXMLLoader.load(getClass().getResource("BasicApplication.fxml"));
//        primaryStage.setTitle("Hello World");
//        Scene theScene = new Scene(root, 700, 790);
//        primaryStage.setScene(theScene);
//
//        primaryStage.show();
    }

    public void updateMessageA(ActionStates param) throws Exception{
        if(param == ActionStates.Succeded){
            loginButton.setDisable(true);
//            CustomUtility.AlertHelper("Successful Login Message", "Successful Login",
//                    "Login Was Successful", Alert.AlertType.INFORMATION).show();

            primaryStage.hide();
            dashboardScene = FXMLLoader.load(getClass().getResource("BasicApplication.fxml"));
            primaryStage.setScene(dashboardScene);
            primaryStage.setTitle("Welcome To Dashboard");
            primaryStage.show();
            primaryStage.sizeToScene();
            primaryStage.setMinWidth(primaryStage.getWidth());
            primaryStage.setMinHeight(primaryStage.getHeight());
            primaryStage.setMaximized(true);
//            primaryStage.setResizable(false);

//        primaryStage.setTitle("Hello World");
//        Scene theScene = new Scene(root, 700, 790);
//        primaryStage.setScene(theScene);
//
//        primaryStage.show();


        }else if(param == ActionStates.Failed){
            loginButton.setDisable(false);
            CustomUtility.AlertHelper("Unsuccessful Login Message", "Error Login",
                    "Login Was Unsuccessful", Alert.AlertType.ERROR).show();
        }
    }



    private enum ActionStates {
        Cancled,
        Failed,
        Succeded;
        ActionStates(){}
    }





    public void createCustomDialog(){
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Look! a custom Login Dialog");

        // Set the icon (must be included in the project).
        dialog.setGraphic(new ImageView(this.getClass().getResource("images/icons8-lock-100.png").toString()));

        // set the button types
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        //Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue)->{
                loginButton.setDisable(newValue.trim().isEmpty()); });

        dialog.getDialogPane().setContent(grid);

        //Request focus on the username field by default.
        Platform.runLater(()->username.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton ->{
            if(dialogButton == loginButtonType){
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            System.out.println("Username="+usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
        });

//        dialog.initModality(Modality.APPLICATION_MODAL);
//        dialog.initModality(null);

    }

    public static void main(String[] args) {
        launch(args);
    }


    @FXML
    public void checkPassword(ActionEvent actionEvent) {

    }
}
