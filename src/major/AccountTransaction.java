package major;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by chinakalight on 6/15/18.
 */
@Entity
@Table(name = "account_transaction")
public class AccountTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int Id;

    @Column(name = "description")
    private String description;

    @Column(name = "transaction_date")
    private Date transaction_date;


    @Column(name = "transaction_type")
    private String transaction_type;

    @Column(name = "status")
    private int status;

    @Column(name = "account_no")
    private String accountNo;


    @Column(name = "amount")
    private BigDecimal amount;



    public AccountTransaction(){}



    public AccountTransaction(String description, Date transaction_date, String transaction_type, int status, String accountNo, BigDecimal amount) {
        this.description = description;
        this.transaction_date = transaction_date;
        this.transaction_type = transaction_type;
        this.status = status;
        this.accountNo = accountNo;
        this.amount = amount;
    }


    public int getId(){
        return this.Id;
    }

    public void setId(int Id){
        this.Id = Id;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(Date transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
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

    @Override
    public String toString() {
        return String.format("ID: %d | STATUS: %d | DESC: %s | ACC-NO: %s | Amount: %s | TRAN_DATE: %s | TRAN_TYPE: %s",
                this.Id, this.status, this.description, this.accountNo, this.amount, this.transaction_date, this.transaction_type);
    }
}
