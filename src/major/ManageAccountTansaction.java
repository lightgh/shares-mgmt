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
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import static major.ManageSharesTansaction.CREDIT_ACC;
import static major.ManageSharesTansaction.DEBIT_ACC;


/**
 * A class to help manage retrieval of account transaction list record
 * Created by chinakalight on 6/15/18.
 */
public class ManageAccountTansaction {

    public static int CREDIT = 1;
    public static int DEBIT = 2;
    public static int BOTH = 3;
    public static int MINIMUM_AMOUNT_TO_WITHDRAW_DEPOSIT = 100;


    public static ObservableList<AccountTransaction> getAccountTransactionsForAccountNo(String accountNo) throws Exception {
        return getAccountTransactionsForAccountNo(accountNo, ManageAccountTansaction.BOTH);
    }

    public static ObservableList<AccountTransaction> getAccountTransactionsForAccountNo(String accountNo, int type) throws Exception {
        String hql = "";
        if(type == ManageAccountTansaction.DEBIT ){
            hql = "FROM AccountTransaction A WHERE A.accountNo='"+accountNo+"' AND A.transaction_type='DEBIT'";
        }else if(type == ManageAccountTansaction.CREDIT ){
            hql = "FROM AccountTransaction A WHERE A.accountNo='"+accountNo+"' AND A.transaction_type='CREDIT'";
        }else if(type == ManageAccountTansaction.BOTH){
            hql = "FROM AccountTransaction A WHERE A.accountNo='"+accountNo+"'";
        }else{
            throw new Exception("AccountType Required");
        }

            SessionFactory sessionFactory = CustomUtility.getSessionFactory();

            Session session = sessionFactory.openSession();

            Transaction transactionA = session.beginTransaction();



            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AccountTransaction> query = builder.createQuery(AccountTransaction.class);

            Query<AccountTransaction> memQ = session.createQuery(hql, AccountTransaction.class);

            ObservableList<AccountTransaction> currentObserveAccountTransactionList = FXCollections.observableArrayList();
            currentObserveAccountTransactionList.setAll(memQ.getResultList());
            transactionA.commit();

            return currentObserveAccountTransactionList;

    }

    public static BigDecimal getAccountBalance(String accountNo) throws Exception {

        BigDecimal totalDebited =  getTotalDebited(accountNo);
        BigDecimal totalCredited = getTotalCredited(accountNo);
        return totalCredited.subtract(totalDebited);
    }

    public static BigDecimal getTotalDebited(String accountNo) throws Exception {

        BigDecimal totalCredited = getTotal(getDebitTransactions(accountNo));
        return totalCredited;
    }

    public static ObservableList<AccountTransaction> getDebitTransactions(String accountNo) throws Exception{
        ObservableList<AccountTransaction> observableListDebit = FXCollections.observableArrayList();
        observableListDebit.setAll(ManageAccountTansaction.getAccountTransactionsForAccountNo(accountNo, ManageAccountTansaction.DEBIT));
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

    public static ObservableList<AccountTransaction> getCreditTransactions(String accountNo) throws Exception{
        ObservableList<AccountTransaction> observableListCredit = FXCollections.observableArrayList();
        observableListCredit.setAll(ManageAccountTansaction.getAccountTransactionsForAccountNo(accountNo, ManageAccountTansaction.CREDIT));
        return observableListCredit;
    }


    public static BigDecimal getTotal(ObservableList<AccountTransaction> accountTransactions){

        BigDecimal sum = BigDecimal.ZERO;

        if(accountTransactions == null)
            return sum;

        Iterator<AccountTransaction> iterator =  accountTransactions.iterator();
        while (iterator.hasNext()){
            sum = sum.add(iterator.next().getAmount());
        }

        return sum;
    }


    public static ObservableList<AccountTransaction> getAllAccountTransactionList(){

        SessionFactory sessionFactory = CustomUtility.getSessionFactory();
        Session session = sessionFactory.openSession();

        ObservableList<AccountTransaction> currentObserveAccountTransactionList = FXCollections.observableArrayList();

        Transaction transactionA = session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<AccountTransaction> query = builder.createQuery(AccountTransaction.class);
        query.from(AccountTransaction.class);

        //get this information from the database
        List<AccountTransaction> memberAccountList = session.createQuery(query).getResultList();

        AccountTransaction tempAccountTrasactionList;

        for (AccountTransaction accountTransaction:
                memberAccountList) {
            tempAccountTrasactionList = new AccountTransaction();
            tempAccountTrasactionList.setId(accountTransaction.getId());
            tempAccountTrasactionList.setAccountNo(accountTransaction.getAccountNo());
            tempAccountTrasactionList.setAmount(accountTransaction.getAmount());
            tempAccountTrasactionList.setTransaction_date(accountTransaction.getTransaction_date());
            tempAccountTrasactionList.setTransaction_type(accountTransaction.getTransaction_type());
            tempAccountTrasactionList.setStatus(accountTransaction.getStatus());
            tempAccountTrasactionList.setDescription(accountTransaction.getDescription());

            currentObserveAccountTransactionList.add(tempAccountTrasactionList);
        }

        transactionA.commit();

        return currentObserveAccountTransactionList;
    }


    public static void creditOrDebitAccountNoWith(String accountNo, BigDecimal calculateAccountSharesBenefit, LocalDate creditLocalDate, String transactionDesc, String transType) throws IllegalArgumentException {

        if(!transType.equalsIgnoreCase(CREDIT_ACC) && !transType.equalsIgnoreCase(DEBIT_ACC))
            throw new IllegalArgumentException("Transaction TYPE MUST BE CREDIT OR DEBIT");
        SessionFactory sessionFactory = CustomUtility.getSessionFactory();
        Session session = sessionFactory.openSession(); //sessionFactory.getCurrentSession();
        Transaction transactionA = session.beginTransaction();
        AccountTransaction newAccountTransaction = new AccountTransaction();
        newAccountTransaction.setAccountNo(accountNo);
        newAccountTransaction.setAmount(calculateAccountSharesBenefit);
        newAccountTransaction.setDescription(transactionDesc);
        newAccountTransaction.setTransaction_type(transType);
        newAccountTransaction.setTransaction_date(CustomUtility.getDateFromLocalDate(creditLocalDate));
        newAccountTransaction.setStatus(1);
        newAccountTransaction.toString();
        session.save(newAccountTransaction);
        transactionA.commit();
    }

}
