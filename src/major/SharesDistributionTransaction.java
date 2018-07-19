package major;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by chinakalight on 6/15/18.
 */
@Entity
@Table(name = "shares_distribution")
public class SharesDistributionTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int Id;

    @Column(name = "description")
    private String description;

    @Column(name = "credited_date")
    private Date creditedDate;


    @Column(name = "monthyear")
    private String monthYear;

    @Column(name = "status")
    private int status;

    @Column(name = "status")
    private int no_of_transactions;

    @Column(name = "total_revenue")
    private BigDecimal totalRevenue;

    @Column(name = "month_shares")
    private BigDecimal monthTotalShare;

    @Column(name = "profit")
    private BigDecimal profit;

    public SharesDistributionTransaction(String description, Date creditedDate, String monthYear, int status, int no_of_transactions, BigDecimal totalRevenue, BigDecimal monthTotalShare, BigDecimal profit) {
        this.description = description;
        this.creditedDate = creditedDate;
        this.monthYear = monthYear;
        this.status = status;
        this.no_of_transactions = no_of_transactions;
        this.totalRevenue = totalRevenue;
        this.monthTotalShare = monthTotalShare;
        this.profit = profit;
    }



    public SharesDistributionTransaction(){ }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreditedDate() {
        return creditedDate;
    }

    public void setCreditedDate(Date creditedDate) {
        this.creditedDate = creditedDate;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNo_of_transactions() {
        return no_of_transactions;
    }

    public void setNo_of_transactions(int no_of_transactions) {
        this.no_of_transactions = no_of_transactions;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getMonthTotalShare() {
        return monthTotalShare;
    }

    public void setMonthTotalShare(BigDecimal monthTotalShare) {
        this.monthTotalShare = monthTotalShare;
    }
}
