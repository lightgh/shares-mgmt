package major;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by chinakalight on 6/15/18.
 */
@Entity
@Table(name = "account_info")
public class MembershipAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int Id;

    @Column(name="first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "other_name")
    private String otherName;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "address")
    private String address;

    @Column(name = "opening_date")
    private Date opening_date;

    @Column(name = "closing_date")
    private Date closing_date;

    @Column(name = "status")
    private int status;

    @Column(name = "account_no")
    private String accountNo;


    public MembershipAccount(){}

    /**
     * @param firstName
     * @param lastName
     * @param otherName
     * @param phoneNo
     * @param address
     * @param opening_date
     * @param closing_date
     * @param status
     * @param accountNo
     */
    public MembershipAccount(
            String firstName, String lastName, String otherName, String phoneNo,
            String address, Date opening_date, Date closing_date, int status, String accountNo) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.otherName = otherName;
        this.phoneNo = phoneNo;
        this.address = address;
        this.opening_date = opening_date;
        this.closing_date = closing_date;
        this.status = status;
        this.accountNo = accountNo;
    }


    public int getId(){
        return this.Id;
    }

    public void setId(int Id){
        this.Id = Id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getOpening_date() {
        return opening_date;
    }

    public void setOpening_date(Date opening_date) {
        this.opening_date = opening_date;
    }

    public Date getClosing_date() {
        return closing_date;
    }

    public void setClosing_date(Date closing_date) {
        this.closing_date = closing_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getFullName(){ return this.firstName + " " + this.lastName+ " " + this.otherName; }

}
