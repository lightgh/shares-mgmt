package major;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by chinakalight on 6/22/18.
 */
public class TableAccountTransaction {

    private IntegerProperty Id;


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
