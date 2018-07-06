package major;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

/**
 * Created by chinakalight on 6/22/18.
 */
public class TableMemberAccount {

    private IntegerProperty Id;

    private StringProperty firstName;

    private StringProperty lastName;

    private StringProperty otherName;

    private StringProperty phoneNo;

    private StringProperty address;

    private StringProperty opening_date;

    private StringProperty closing_date;

    private StringProperty status;

    private StringProperty accountNo;

    public int getId() {
        return Id.get();
    }

    public IntegerProperty idProperty() {
        return Id;
    }

    public void setId(int id) {
        if (this.Id == null) this.Id = new SimpleIntegerProperty(this, String.valueOf(id));
        this.Id.set(id);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (this.firstName == null) this.firstName = new SimpleStringProperty(this, firstName);
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (this.lastName == null) this.lastName = new SimpleStringProperty(this, lastName);
        this.lastName.set(lastName);
    }

    public String getOtherName() {
        return otherName.get();
    }

    public StringProperty otherNameProperty() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        if (this.otherName == null) this.otherName = new SimpleStringProperty(this, otherName);
        this.otherName.set(otherName);
    }

    public String getPhoneNo() {
        return phoneNo.get();
    }

    public StringProperty phoneNoProperty() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        if (this.phoneNo == null) this.phoneNo = new SimpleStringProperty(this, phoneNo);
        this.phoneNo.set(phoneNo);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        if (this.address == null) this.address = new SimpleStringProperty(this, address);
        this.address.set(address);
    }

    public String getOpening_date() {
        return opening_date.get();
    }

    public StringProperty opening_dateProperty() {
        return opening_date;
    }

    public void setOpening_date(String opening_date) {
        if (this.opening_date == null) this.opening_date = new SimpleStringProperty(this, opening_date);
        this.opening_date.set(opening_date);
    }

    public String getClosing_date() {
        return closing_date.get();
    }

    public StringProperty closing_dateProperty() {
        return closing_date;
    }

    public void setClosing_date(String closing_date) {
        if (this.closing_date == null) this.closing_date = new SimpleStringProperty(this, closing_date);
        this.closing_date.set(closing_date);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        if (this.status == null) this.status = new SimpleStringProperty(this, String.valueOf(status));
        this.status.set(status);
    }

    public String getAccountNo() {
        return accountNo.get();
    }

    public StringProperty accountNoProperty() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        if (this.accountNo == null) this.accountNo = new SimpleStringProperty(this, String.valueOf(accountNo));
        this.accountNo.set(accountNo);
    }
}
