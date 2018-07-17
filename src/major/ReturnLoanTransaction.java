package major;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by chinakalight on 6/15/18.
 */
@Entity
@Table(name = "loans_returned")
public class ReturnLoanTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int Id;

    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "collected_amount")
    private BigDecimal collectedAmount;

    @Column(name = "exptected_amount")
    private BigDecimal expectedAmount;

    @Column(name = "date_collected")
    private Date dateCollected;

    @Column(name = "interest_rate")
    private Integer interestRate;

    @Column(name = "date_paid")
    private Date datePaid;

    @Column(name = "loan_period")
    private Integer loanPeriod;

    @Column(name = "ledger_no")
    private String ledgerNo;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private int status;

    public ReturnLoanTransaction(String accountNo, BigDecimal collectedAmount, BigDecimal expectedAmount, Date dateCollected, Integer interestRate, Date datePaid, Integer loanPeriod, String ledgerNo, String description, int status) {
        this.accountNo = accountNo;
        this.collectedAmount = collectedAmount;
        this.expectedAmount = expectedAmount;
        this.dateCollected = dateCollected;
        this.interestRate = interestRate;
        this.datePaid = datePaid;
        this.loanPeriod = loanPeriod;
        this.ledgerNo = ledgerNo;
        this.description = description;
        this.status = status;
    }

    public ReturnLoanTransaction(){}

    public Date getDateCollected() {
        return dateCollected;
    }

    public void setDateCollected(Date dateCollected) {
        this.dateCollected = dateCollected;
    }

    public BigDecimal getExpectedAmount() {
        return expectedAmount;
    }

    public void setExpectedAmount(BigDecimal expectedAmount) {
        this.expectedAmount = expectedAmount;
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

    public BigDecimal getCollectedAmount() {
        return collectedAmount;
    }

    public void setCollectedAmount(BigDecimal amount) {
        this.collectedAmount = amount;
    }

    public Date getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(Date datePaid) {
        this.datePaid = datePaid;
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
