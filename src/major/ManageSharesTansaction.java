package major;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;


/**
 * A class to help manage retrieval of shares transaction list record
 * Created by chinakalight on 6/15/18.
 */
public class ManageSharesTansaction {

    public static int CREDIT = 1;
    public static int DEBIT = 2;
    public static int BOTH = 3;
    public static int MINIMUM_AMOUNT_TO_WITHDRAW_DEPOSIT = 100;


    public static ObservableList<SharesTransaction> getSharesTransactionsForAccountNo(String accountNo) throws Exception {
        return getSharesTransactionsForAccountNo(accountNo, ManageSharesTansaction.BOTH);
    }

    public static ObservableList<SharesTransaction> getSharesTransactionsForAccountNo(String accountNo, int type) throws Exception {
        String hql = "";
        if(type == ManageSharesTansaction.DEBIT ){
            hql = "FROM SharesTransaction A WHERE A.accountNo='"+accountNo+"' AND A.transaction_type='DEBIT'";
        }else if(type == ManageSharesTansaction.CREDIT ){
            hql = "FROM SharesTransaction A WHERE A.accountNo='"+accountNo+"' AND A.transaction_type='CREDIT'";
        }else if(type == ManageSharesTansaction.BOTH){
            hql = "FROM SharesTransaction A WHERE A.accountNo='"+accountNo+"'";
        }else{
            throw new Exception("AccountType Required");
        }

            SessionFactory sessionFactory = CustomUtility.getSessionFactory();
//        Session session = sessionFactory.getCurrentSession();
            Session session = sessionFactory.openSession();

            Transaction transactionA = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<SharesTransaction> query = builder.createQuery(SharesTransaction.class);

            Query<SharesTransaction> memQ = session.createQuery(hql, SharesTransaction.class);

            ObservableList<SharesTransaction> currentObserveAccountTransactionList = FXCollections.observableArrayList();
            currentObserveAccountTransactionList.setAll(memQ.getResultList());
            transactionA.commit();

            return currentObserveAccountTransactionList;

    }

    public static BigDecimal getSharesBalance(String accountNo) throws Exception {
        BigDecimal totalDebited =  getTotalDebited(accountNo);
        BigDecimal totalCredited = getTotalCredited(accountNo);
        return totalCredited.subtract(totalDebited);
    }

    public static BigDecimal getTotalDebited(String accountNo) throws Exception {

        BigDecimal totalCredited = getTotal(getSharesSellsTransactions(accountNo));
        return totalCredited;
    }

    public static ObservableList<SharesTransaction> getSharesSellsTransactions(String accountNo) throws Exception{
        ObservableList<SharesTransaction> observableListDebit = FXCollections.observableArrayList();
        observableListDebit.setAll(ManageSharesTansaction.getSharesTransactionsForAccountNo(accountNo, ManageSharesTansaction.DEBIT));
        return  observableListDebit;
    }

    /**
     * Gets the total amount credited to the user
     * @param accountNo
     * @return
     * @throws Exception
     */
    public static BigDecimal getTotalCredited(String accountNo) throws Exception {

        BigDecimal totalCredited = getTotal(getCreditTransactions(accountNo));
        return totalCredited;
    }

    public static ObservableList<SharesTransaction> getCreditTransactions(String accountNo) throws Exception{
        ObservableList<SharesTransaction> observableListCredit = FXCollections.observableArrayList();
        observableListCredit.setAll(ManageSharesTansaction.getSharesTransactionsForAccountNo(accountNo, ManageSharesTansaction.CREDIT));
        return observableListCredit;
    }


    public static BigDecimal getTotal(ObservableList<SharesTransaction> sharesTransactions){

//        CustomUtility.pln("TE-ST-1: " +sharesTransactions.size());

        BigDecimal sum = BigDecimal.ZERO;

        if(sharesTransactions == null)
            return sum;

//        CustomUtility.pln("TE-ST-2: " +sharesTransactions.size());
        /*sharesTransactions.forEach((temp)->{
            sum = sum.add(temp.getCollectedAmount());
        });*/

        Iterator<SharesTransaction> iterator =  sharesTransactions.iterator();
        while (iterator.hasNext()){
            sum = sum.add(iterator.next().getAmount());
        }

//        CustomUtility.pln("SUMATION: " + sum.toString());
//        CustomUtility.pln("TE-ST-3: " +sharesTransactions.size());

        return sum;
    }


    public static ObservableList<SharesTransaction> getAllSharesTransactionList(){

        SessionFactory sessionFactory = CustomUtility.getSessionFactory();
        Session session = sessionFactory.openSession();

        ObservableList<SharesTransaction> currentObserveSharesTransactionList = FXCollections.observableArrayList();

        Transaction transactionA = session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<SharesTransaction> query = builder.createQuery(SharesTransaction.class);
        query.from(SharesTransaction.class);

        //get this information from the database
        List<SharesTransaction> memberAccountList = session.createQuery(query).getResultList();

        SharesTransaction tempSharesTransactionList;

        for (SharesTransaction sharesTransaction:
                memberAccountList) {
            tempSharesTransactionList = new SharesTransaction();
            tempSharesTransactionList.setId(sharesTransaction.getId());
            tempSharesTransactionList.setAccountNo(sharesTransaction.getAccountNo());
            tempSharesTransactionList.setAmount(sharesTransaction.getAmount());
            tempSharesTransactionList.setTransaction_date(sharesTransaction.getTransaction_date());
            tempSharesTransactionList.setTransaction_type(sharesTransaction.getTransaction_type());
            tempSharesTransactionList.setStatus(sharesTransaction.getStatus());
            tempSharesTransactionList.setDescription(sharesTransaction.getDescription());

            currentObserveSharesTransactionList.add(tempSharesTransactionList);
        }

        transactionA.commit();

        return currentObserveSharesTransactionList;
    }



}
