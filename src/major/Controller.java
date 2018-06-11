package major;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
        javax.swing.JOptionPane.showMessageDialog(null, "Hello");
    }

    @FXML
    public void addNewAccount(ActionEvent actionEvent) {

        String firstName = this.naFirstName.getText().toString().trim();
        String lastName = this.naLastName.getText().toString().trim();
        String otherName = this.naOtherName.getText().toString().trim();
        String phoneNo = this.naPhoneNo.getText().toString().trim();
        String Address = this.naAddress.getText().toString().trim();
        String openingDate = this.naOpenDate.getValue().format(DateTimeFormatter.ofPattern("yyyy MM dd"));

        String accountNo = myUtility.getNewAccountNo(10);

        print(accountNo);

        CustomUtility cu = new CustomUtility();

        java.util.Date dateNow = new java.util.Date();

//        Date.format(DateTimeFormatter.ofPattern("yyyy MM dd"))

        cu.queryDB(" INSERT INTO account_info(first_name, last_name, other_name, " +
                "phone_no, address, account_no, opening_date, closing_date, status) " +
                "VALUES('"+firstName+"', '"+lastName+"', '"+otherName+"', '"+phoneNo+"', '"+
                accountNo+"', '"+ openingDate +"', 1 ");

        print(cu.getNewAccountNo(10));

        javax.swing.JOptionPane.showMessageDialog(null, cu.getNewAccountNo(10));
    }



    public static void print(String string){
        System.out.print(string);
    }

    public static void println(String string){
        System.out.println(string);
    }


}
