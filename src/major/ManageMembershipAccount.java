package major;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by chinakalight on 6/15/18.
 */
public class ManageMembershipAccount {

    private int Id;

    private String firstName;
    private String lastName;
    private String otherName;
    private String phoneNo;
    private String address;

    private Date opening_date;
    private Date closing_date;

    private int status;

    // Unique Account Number
    private String accountNo;

    private ArrayList<ManageMembershipAccount> allMemberAccount;

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
    public ManageMembershipAccount(
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

    private int getId(){
        return this.Id;
    }

    private int setsId(){
        return this.Id;
    }

    public void save(){

    }

    public void delete(){

    }

    public void update(){

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

    public ManageMembershipAccount setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
        return this;
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

    public ArrayList<ManageMembershipAccount> getAllAccount(){
        return null;
    }

    public static boolean exists(String accountNo){
        CustomUtility cu = new CustomUtility();
        ResultSet rs = cu.queryDB("SELECT `id`, `first_name`, `last_name`, `other_name`, `phone_no`, `address`, `account_no`, `opening_date`, `closing_date`, `status` FROM `account_info` WHERE 1");
        int count = 0;

        try {
            while (rs.next()){
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(count == 0)
            return false;
        else
            return true;
    }
}
