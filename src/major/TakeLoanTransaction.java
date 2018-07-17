package major;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by chinakalight on 6/15/18.
 */
@Entity
@Table(name = "loans_taken")
public class TakeLoanTransaction implements Serializable {

    // id	account_no	amount	interest_rate	date_collected	repay_date	loan_period	ledger_no	description	status

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int Id;

    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "interest_rate")
    private Integer interestRate;

    @Column(name = "date_collected")
    private Date dateCollected;

    @Column(name = "repay_date")
    private Date repayDate;

    @Column(name = "loan_period")
    private Integer loanPeriod;

    @Column(name = "ledger_no")
    private String ledgerNo;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private int status;

    public TakeLoanTransaction(String accountNo, BigDecimal amount, Integer interestRate, Date dateCollected, Date repayDate, Integer loanPeriod, String ledgerNo, String description, int status) {
        this.accountNo = accountNo;
        this.amount = amount;
        this.interestRate = interestRate;
        this.dateCollected = dateCollected;
        this.repayDate = repayDate;
        this.loanPeriod = loanPeriod;
        this.ledgerNo = ledgerNo;
        this.description = description;
        this.status = status;
    }

    public TakeLoanTransaction(){}

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public Integer getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(Integer loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public String getLedgerNo() {
        return ledgerNo;
    }

    public void setLedgerNo(String ledgerNo) {
        this.ledgerNo = ledgerNo;
    }

    public int getId(){
        return this.Id;
    }

    public void setId(int Id){
        this.Id = Id;
    }

    public Integer getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Integer interestRate) {
        this.interestRate = interestRate;
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

    public Date getDateCollected() {
        return dateCollected;
    }

    public void setDateCollected(Date dateCollected) {
        this.dateCollected = dateCollected;
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


}
