package major;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.math.BigDecimal;
import java.util.Iterator;

/**
 * Created by chinakalight on 7/12/18.
 */
public class ManageLoanTransaction {

    public static final int TAKEN_LOAN_SUCCESS = 1;
    public static final int TAKEN_LOAN_RETURNED = 2;
    public static final int RETURNED_LOAN_SUCCESS = 1;


    public static ObservableList<TakeLoanTransaction> getTakenLoanTransactionsForAccount(String accountNo) {

        String hql = "";
        hql = "FROM TakeLoanTransaction T WHERE T.accountNo='"+accountNo+"'";


        SessionFactory sessionFactory = CustomUtility.getSessionFactory();
//        Session session = sessionFactory.getCurrentSession();
        Session session = sessionFactory.openSession();

        Transaction transactionA = session.beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<TakeLoanTransaction> query = builder.createQuery(TakeLoanTransaction.class);

        Query<TakeLoanTransaction> memQ = session.createQuery(hql, TakeLoanTransaction.class);

        ObservableList<TakeLoanTransaction> takeLoanTransactionObservableList = FXCollections.observableArrayList();
        takeLoanTransactionObservableList.setAll(memQ.getResultList());
        transactionA.commit();

        return takeLoanTransactionObservableList;

    }


    public static BigDecimal getTotalTakenLoanTransactions(String accountNo) {

        ObservableList<TakeLoanTransaction> loanTransactions = getTakenLoanTransactionsForAccount(accountNo);

        BigDecimal sum = BigDecimal.ZERO;

        if(loanTransactions == null)
            return sum;

        Iterator<TakeLoanTransaction> iterator =  loanTransactions.iterator();
        while (iterator.hasNext()){
            sum = sum.add(iterator.next().getAmount());
        }

        return sum;

    }

    public static BigDecimal getTotalReturnedLoanTransactions(String accountNo) {

        ObservableList<ReturnLoanTransaction> returnLoanTransactions = getReturnedLoanTransactionsForAccount(accountNo);
        BigDecimal sum = BigDecimal.ZERO;

        if(returnLoanTransactions == null)
            return sum;

        Iterator<ReturnLoanTransaction> iterator =  returnLoanTransactions.iterator();
        while (iterator.hasNext()){
            sum = sum.add(iterator.next().getCollectedAmount());
        }

        return sum;
    }

    public static ObservableList<ReturnLoanTransaction> getReturnedLoanTransactionsForAccount(String accountNo) {

        String hql = "";
        hql = "FROM ReturnLoanTransaction R WHERE R.accountNo='"+accountNo+"'";


        SessionFactory sessionFactory = CustomUtility.getSessionFactory();
        Session session = sessionFactory.openSession();

        Transaction transactionA = session.beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ReturnLoanTransaction> query = builder.createQuery(ReturnLoanTransaction.class);

        Query<ReturnLoanTransaction> memQ = session.createQuery(hql, ReturnLoanTransaction.class);

        ObservableList<ReturnLoanTransaction> returnLoanTransactionObservableList = FXCollections.observableArrayList();
        returnLoanTransactionObservableList.setAll(memQ.getResultList());
        transactionA.commit();

        return returnLoanTransactionObservableList;
    }



    public static ObservableList<ReturnLoanTransaction> getReturnedLoanTransactionsForLedgerNo(String ledgerNo) {

        String hql = "";
        hql = "FROM ReturnLoanTransaction R WHERE R.ledgerNo='"+ledgerNo+"'";


        SessionFactory sessionFactory = CustomUtility.getSessionFactory();
        Session session = sessionFactory.openSession();

        Transaction transactionA = session.beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ReturnLoanTransaction> query = builder.createQuery(ReturnLoanTransaction.class);

        Query<ReturnLoanTransaction> memQ = session.createQuery(hql, ReturnLoanTransaction.class);

        ObservableList<ReturnLoanTransaction> returnLoanTransactionObservableList = FXCollections.observableArrayList();
        returnLoanTransactionObservableList.setAll(memQ.getResultList());

        transactionA.commit();

        return returnLoanTransactionObservableList;
    }

    public static TakeLoanTransaction getTakenLoanTransactionsForLedgerNo(String ledgerNo) {

        String hql = "";
        hql = "FROM TakeLoanTransaction R WHERE R.ledgerNo='"+ledgerNo+"'";


        SessionFactory sessionFactory = CustomUtility.getSessionFactory();
        Session session = sessionFactory.openSession();

        Transaction transactionA = session.beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<TakeLoanTransaction> query = builder.createQuery(TakeLoanTransaction.class);

        Query<TakeLoanTransaction> memQ = session.createQuery(hql, TakeLoanTransaction.class);

        TakeLoanTransaction returnLoanTransaction = memQ.getSingleResult();
        transactionA.commit();

        return returnLoanTransaction;
    }

    @NotNull
    public static BigDecimal getAvailableAmount() {

        //TODO Calculate the amount that is available
        return new BigDecimal(100000);
    }

    public static BigDecimal getIncuredInterest(BigDecimal loanAmount, Integer period){

        BigDecimal ONE_HUNDRED = new BigDecimal(100);

        BigDecimal expectedAmount;
        BigDecimal percentage = getPercentage(period);


        expectedAmount = percentage.divide(ONE_HUNDRED).multiply(loanAmount);

        return expectedAmount;

    }

    /**
     * For the first month, grace period of 5 days. Subsequently
     * its 30 days calculation
     * @param period
     * @return
     */
    public static BigDecimal getPercentage(Integer period){

        Double percentage = 15.0;

        Integer whole = period/30;
        Integer remiander = period % 30;
        Integer noOfMonths = 1;

        if(remiander == period && whole == 0) percentage = percentage;
        else if(whole == 0 && remiander > 0 ) percentage = percentage;
        else if (whole > 0 && remiander == 0 ){

            noOfMonths = ( whole - 1 );

            percentage = percentage + (5 * noOfMonths);

        }else if(whole > 0 && remiander > 0 ){
            noOfMonths = ( whole - 1 );

            if(remiander > 5) noOfMonths++;

            percentage = percentage + (5 * noOfMonths);
        }

        return new BigDecimal(percentage);
    }

    public static BigDecimal getCurrentLoanBalance(String accountNo) {
        return getTotalTakenLoanTransactions(accountNo).subtract(getTotalReturnedLoanTransactions(accountNo));
    }
}
