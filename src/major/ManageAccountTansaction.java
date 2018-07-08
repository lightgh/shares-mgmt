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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * A class to help manage retrieval of account transaction list record
 * Created by chinakalight on 6/15/18.
 */
public class ManageAccountTansaction {

    public static int CREDIT = 1;
    public static int DEBIT = 2;
    public static int BOTH = 3;

    public static boolean exists(String accountNo){

        CustomUtility customUtility = new CustomUtility();
        ResultSet rs = customUtility.queryDB("SELECT `id`, `first_name`, `last_name`, `other_name`, `phone_no`, `address`, " +
                " `account_no`, `opening_date`, `closing_date`, `status` FROM `account_info` WHERE `account_no`='"+accountNo+"'");
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
//        Session session = sessionFactory.getCurrentSession();
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

    public static BigDecimal getTotal(ObservableList<AccountTransaction> accountTransactions){

        CustomUtility.pln("TE-ST-1: " +accountTransactions.size());

        BigDecimal sum = BigDecimal.ZERO;

        if(accountTransactions == null)
            return sum;

        CustomUtility.pln("TE-ST-2: " +accountTransactions.size());
        /*accountTransactions.forEach((temp)->{
            sum = sum.add(temp.getAmount());
        });*/

        Iterator<AccountTransaction> iterator =  accountTransactions.iterator();
        while (iterator.hasNext()){
            sum = sum.add(iterator.next().getAmount());
        }

        CustomUtility.pln("SUMATION: " + sum.toString());
        CustomUtility.pln("TE-ST-3: " +accountTransactions.size());

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



}
