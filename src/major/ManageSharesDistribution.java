package major;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;


/**
 * A class to help manage retrieval of shares transaction list record
 * Created by chinakalight on 6/15/18.
 */
public class ManageSharesDistribution {

    final static Logger logger = Logger.getLogger(ManageSharesDistribution.class.getName());

    static {
        SystemOutToLog4.enableForClass(ManageSharesDistribution.class);
    }


    public static boolean addNewDistributedShares(BigDecimal monthTotalShare, BigDecimal profit, String monthYear, LocalDate creditLocalDate, int noOfTransactions){

        boolean ok = false;
        BigDecimal totalRevenue = profit.add(monthTotalShare);
        SessionFactory sessionFactory = CustomUtility.getSessionFactory();
        Session session = sessionFactory.openSession(); //sessionFactory.getCurrentSession();
        Transaction transactionA = session.beginTransaction();
        SharesDistributionTransaction newSharesDistributed = new SharesDistributionTransaction();
        newSharesDistributed.setCreditedDate(CustomUtility.getDateFromLocalDate(creditLocalDate));
        newSharesDistributed.setDescription("This is the shares distributed for a month");
        newSharesDistributed.setMonthTotalShare(monthTotalShare);
        newSharesDistributed.setMonthYear(monthYear);
        newSharesDistributed.setNo_of_transactions(noOfTransactions);
        newSharesDistributed.setProfit(profit);
        newSharesDistributed.setTotalRevenue(totalRevenue);
        newSharesDistributed.setStatus(1);
        session.save(newSharesDistributed);
        transactionA.commit();
        ok = true;

        return ok;
    }


}
