package major;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


public class Controller {

    CustomUtility myUtility = new CustomUtility();

    @FXML
    TextField naFirstName,
            naLastName,
            naOtherName,
            naPhoneNo;
    @FXML
    TextArea naAddress;

    @FXML
    DatePicker naOpenDate;

    @FXML
    public void checkPassword(ActionEvent actionEvent) {

    }

    @FXML
    public void addNewAccount(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Account Information Display Notification");
        alert.setContentText("TEST");



       /* String firstName = this.naFirstName.getText().toString().trim();
        String lastName = this.naLastName.getText().toString().trim();
        String otherName = this.naOtherName.getText().toString().trim();
        String phoneNo = this.naPhoneNo.getText().toString().trim();
        String address = this.naAddress.getText().toString().trim();

        String openingDate = this.naOpenDate.getValue().toString();
        println(openingDate);
        int status = 1;

        String accountNo = myUtility.getNewAccountNo(10);

        print(accountNo);

        CustomUtility cu = new CustomUtility();
        java.util.Date dateNow = new java.util.Date();

//        Date.format(DateTimeFormatter.ofPattern("yyyy MM dd"))
        java.util.Date dt = new java.util.Date();

        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentTime = sdf.format(dt);

        println(currentTime);


        String insertQuery = "INSERT INTO `account_info` (`id`, `first_name`, `last_name`, `other_name`," +
                " `phone_no`, `address`, `account_no`, `opening_date`, `closing_date`, `status`) " +
                "VALUES (NULL, '"+firstName+"', '"+lastName+"', '"+otherName+"', '"+phoneNo+"', '"+address+"', '"+accountNo+
                "', '"+openingDate+"', '"+openingDate+"', '"+status+"')";

        println(insertQuery);

        cu.insQueryDB(insertQuery);


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Account Information Display Notification");
        alert.setContentText(cu.getNewAccountNo(10));

        alert.show();//.showAndWait();
*/
    }



    public static void print(String string){
        System.out.print(string);
    }

    public static void println(String string){
        System.out.println(string);
    }


}
