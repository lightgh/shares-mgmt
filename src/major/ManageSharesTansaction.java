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
import java.math.MathContext;
import java.time.LocalDate;
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
    public static String DEBIT_ACC = "DEBIT";
    public static String CREDIT_ACC = "CREDIT";

    public static int AUTO_RENEW_SHARES = 1;
    public static int MANUAL_RENEW_SHARES = 2;

    public static int SHARES_REWARD_PENDING = 1;
    public static int SHARES_REWARD_SUCCESSFUL = 2;

    public static int ALL_SHARES  = 4;
    public static int ALL_REWARD_PENDING_SHARES  = 1;
    public static int ALL_REWARD_SUCCESSFUL_SHARES  = 2;

    public static ObservableList<SharesTransaction> getSharesTransactionsForAccountNo(String accountNo) throws Exception {
        return getSharesTransactionsForAccountNo(accountNo, ManageSharesTansaction.BOTH);
    }

    /**
     * Distribute Shares Across Accounts
     * @param sharesTransactionObservableList
     * @param monthlySharesTotal
     * @param profit
     * @param creditLocalDate
     * @param DISTRIBUTION_TYPE
     * @return
     * @throws Exception
     */
    public static boolean distributeSharesAmongThese(ObservableList<SharesTransaction> sharesTransactionObservableList, BigDecimal monthlySharesTotal, BigDecimal profit, LocalDate creditLocalDate, int DISTRIBUTION_TYPE) throws Exception{
        boolean complete = false;
        BigDecimal calculateAccountSharesBenefit, sum = BigDecimal.ZERO;
        if(sharesTransactionObservableList.isEmpty() || creditLocalDate == null){
            return complete;
        }else{

            Iterator<SharesTransaction> iterator =  sharesTransactionObservableList.iterator();

            MathContext mathCntxt = MathContext.DECIMAL64;
            while (iterator.hasNext()){

                SharesTransaction tempST = iterator.next();
                calculateAccountSharesBenefit = tempST.getAmount().divide(monthlySharesTotal, mathCntxt).multiply(profit, mathCntxt);
                if(DISTRIBUTION_TYPE == AUTO_RENEW_SHARES){

                    //CREDIT ACCOUNT NO -- SHARES BENEFIT FOR MONTH OF YEAR (USING CURRENT SPECIFIED DATE)
                    ManageAccountTansaction.creditOrDebitAccountNoWith(tempST.getAccountNo(), calculateAccountSharesBenefit, creditLocalDate, "Shares Benefit For "+ tempST.getTransaction_date(), CREDIT_ACC);
                    //CREDIT SHARES WITH SAME ACCOUNT NO (SHARED PROFIT)-- BUY SHARES FOR THE MONTH OF YEAR (USING CURRENT SPECIFIED DATE)
                    ManageSharesTansaction.buySellSharesForAccounNoOfAmount(tempST.getAccountNo(), calculateAccountSharesBenefit, creditLocalDate, "Automated Purchase Of Shares For " + creditLocalDate.getMonth().name()+ " Of "+creditLocalDate.getYear(), CREDIT_ACC, SHARES_REWARD_PENDING);

                    //SELL or DEBIT SHARES WITH SAME ACCOUNT NO (OLD AMOUNT AND )-- BUY SHARES FOR THE MONTH OF YEAR (USING CURRENT SPECIFIED DATE)
                    ManageSharesTansaction.buySellSharesForAccounNoOfAmount(tempST.getAccountNo(), tempST.getAmount(), creditLocalDate, "Automated Shares Sell For Transfer To The Specified New Month", DEBIT_ACC, SHARES_REWARD_SUCCESSFUL);

                    //BUY or CREDIT SHARES WITH SAME ACCOUNT NO (OLD AMOUNT AND )-- BUY SHARES FOR THE MONTH OF YEAR (USING CURRENT SPECIFIED DATE)
                    ManageSharesTansaction.buySellSharesForAccounNoOfAmount(tempST.getAccountNo(), tempST.getAmount(), creditLocalDate, "Automated Shares Purchase For " + creditLocalDate.getMonth().name()+ " Of "+creditLocalDate.getYear(), CREDIT_ACC, SHARES_REWARD_PENDING);

                }else if(DISTRIBUTION_TYPE == MANUAL_RENEW_SHARES){

                    //ONLY CREDIT ACCOUNT NO - SHARES BENEFIT FOR MONTH OF YEAR (USING CURRENT SPECIFIED DATE)
                    ManageAccountTansaction.creditOrDebitAccountNoWith(tempST.getAccountNo(), calculateAccountSharesBenefit, creditLocalDate, "Manual Shares Benefit For "+ tempST.getTransaction_date(), CREDIT_ACC);

                    //DEBIT SHARES WITH SAME ACCOUNT NO (OLD AMOUNT AND )-- BUY SHARES FOR THE MONTH OF YEAR (USING CURRENT SPECIFIED DATE)
                    ManageSharesTansaction.buySellSharesForAccounNoOfAmount(tempST.getAccountNo(), tempST.getAmount(), creditLocalDate, "Automated Shares Purchase For " + creditLocalDate.getMonth().name()+ " Of "+creditLocalDate.getYear(), DEBIT_ACC, SHARES_REWARD_PENDING);

                }

                //UPDATE sharesTransaction Status  = 2 (GOTTEN SHARES)
                ManageSharesTansaction.updateShareTransaction(tempST, SHARES_REWARD_SUCCESSFUL);

                //TRACK THE SHARE DISTRIBUTED FOR THIS MONTH AND DISPLAY IT ON THE SIDE TABLE
                sum = sum.add(calculateAccountSharesBenefit);
                CustomUtility.pln(String.format("SHARED: %s to AccountNo: %s", calculateAccountSharesBenefit, tempST.getAccountNo()));
            }

            ManageSharesDistribution.addNewDistributedShares(monthlySharesTotal, profit, creditLocalDate.getMonth().name() + " Of " +creditLocalDate.getYear(), creditLocalDate, sharesTransactionObservableList.size());

            CustomUtility.pln("TOTAL-SUMMED: " + sum);

        }
        return complete;
    }

    private static void updateShareTransaction(SharesTransaction tempST, int sharesRewardSuccessful) {

        Session session = CustomUtility.getSessionFactory().openSession();
        Transaction transactionA = session.beginTransaction();
        tempST.setStatus(sharesRewardSuccessful);
        session.saveOrUpdate(tempST);
        transactionA.commit();

    }

    private static void buySellSharesForAccounNoOfAmount(String accountNo, BigDecimal calculateAccountSharesBenefit, LocalDate creditLocalDate, String transactionDesc, String transType, int sharesRewardStatus) throws Exception {

        if(!transType.equalsIgnoreCase(CREDIT_ACC) && !transType.equalsIgnoreCase(DEBIT_ACC))
            throw new IllegalArgumentException("Transaction TYPE MUST BE CREDIT OR DEBIT");

        if(sharesRewardStatus != SHARES_REWARD_PENDING && sharesRewardStatus != SHARES_REWARD_SUCCESSFUL){
            throw new IllegalArgumentException("SHARES REWARD STATUS MUST BE EITHER SHARES_REWARD_PENDING OR SHARES_REWARD_SUCCESSFUL");
        }

        SessionFactory sessionFactory = CustomUtility.getSessionFactory();
        Session session = sessionFactory.openSession(); //sessionFactory.getCurrentSession();
        Transaction transactionA = session.beginTransaction();

        if(transType == CREDIT_ACC) {
            BigDecimal currentAccountBalance = ManageAccountTansaction.getAccountBalance(accountNo);
            if (currentAccountBalance.doubleValue() < calculateAccountSharesBenefit.doubleValue()) {
                CustomUtility.AlertHelper("INSUFFICIENT ACCOUNT BALANCE", "Insuffucient Account Balance", "Your Account Has Insuffucient Amount To Peform This Operation", "I").show();
                return;
            }
            AccountTransaction newAccountTransaction = new AccountTransaction();
            newAccountTransaction.setAccountNo(accountNo);
            newAccountTransaction.setAmount(calculateAccountSharesBenefit);
            newAccountTransaction.setDescription(transactionDesc);
            newAccountTransaction.setTransaction_type(DEBIT_ACC);
            newAccountTransaction.setTransaction_date(CustomUtility.getDateFromLocalDate(creditLocalDate));
            newAccountTransaction.setStatus(1);

            session.save(newAccountTransaction);

            SharesTransaction sharesTransaction = new SharesTransaction();
            sharesTransaction.setAccountNo(accountNo);
            sharesTransaction.setDescription(transactionDesc);
            sharesTransaction.setAmount(calculateAccountSharesBenefit);
            sharesTransaction.setTransaction_date(CustomUtility.getDateFromLocalDate(creditLocalDate));
            sharesTransaction.setTransaction_type(CREDIT_ACC);
            sharesTransaction.setStatus(sharesRewardStatus);

            session.save(sharesTransaction);
            transactionA.commit();

        }else if(transType == DEBIT_ACC){
            AccountTransaction newAccountTransaction = new AccountTransaction();
            newAccountTransaction.setAccountNo(accountNo);
            newAccountTransaction.setAmount(calculateAccountSharesBenefit);
            newAccountTransaction.setDescription(transactionDesc);
            newAccountTransaction.setTransaction_type(CREDIT_ACC);
            newAccountTransaction.setTransaction_date(CustomUtility.getDateFromLocalDate(creditLocalDate));
            newAccountTransaction.setStatus(1);

            session.save(newAccountTransaction);

            SharesTransaction sharesTransaction = new SharesTransaction();
            sharesTransaction.setAccountNo(accountNo);
            sharesTransaction.setDescription(transactionDesc);
            sharesTransaction.setAmount(calculateAccountSharesBenefit);
            sharesTransaction.setTransaction_date(CustomUtility.getDateFromLocalDate(creditLocalDate));
            sharesTransaction.setTransaction_type(DEBIT_ACC);
            sharesTransaction.setStatus(sharesRewardStatus);

            session.save(sharesTransaction);
            transactionA.commit();
        }
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

    public static ObservableList<SharesTransaction> getSharesTransactionsForMonth(LocalDate localDate, int shareCategoryType) throws Exception {
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        String hql = "";

        if(shareCategoryType != ALL_SHARES  && shareCategoryType != ALL_REWARD_PENDING_SHARES && shareCategoryType != ALL_REWARD_SUCCESSFUL_SHARES){
            throw new Exception("Invalid Shares Status Specified. Please Specify Valid Ones");
        }

        if(shareCategoryType == ALL_REWARD_SUCCESSFUL_SHARES)
            hql = "FROM SharesTransaction A WHERE month(A.transaction_date)="+month + " and year(A.transaction_date)="+year + " and A.status= "+SHARES_REWARD_SUCCESSFUL;
        else if(shareCategoryType == ALL_REWARD_PENDING_SHARES)
            hql = "FROM SharesTransaction A WHERE month(A.transaction_date)="+month + " and year(A.transaction_date)="+year + " and A.status= "+SHARES_REWARD_PENDING + " and A.transaction_type='CREDIT'";
        else
            hql = "FROM SharesTransaction A WHERE month(A.transaction_date)="+month + " and year(A.transaction_date)="+year;

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

//        CustomUtility.pln("ST: "+currentObserveAccountTransactionList.size());

            return currentObserveAccountTransactionList;

    }

    public static BigDecimal getSharesBalance(String accountNo) throws Exception {
        BigDecimal totalDebited =  getTotalDebited(accountNo);
        BigDecimal totalCredited = getTotalCredited(accountNo);
        return totalCredited.subtract(totalDebited);
    }

    public static BigDecimal getTotalDebited(String accountNo) throws Exception {

        BigDecimal totalCredited = getTotal(getDebitTransactions(accountNo), ALL_SHARES);
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

        BigDecimal totalCredited = getTotal(getCreditTransactions(accountNo), ALL_SHARES);
        return totalCredited;
    }

    public static ObservableList<SharesTransaction> getCreditTransactions(String accountNo) throws Exception{
        ObservableList<SharesTransaction> observableListCredit = FXCollections.observableArrayList();
        observableListCredit.setAll(ManageSharesTansaction.getSharesTransactionsForAccountNo(accountNo, ManageSharesTansaction.CREDIT));
        return observableListCredit;
    }

    public static ObservableList<SharesTransaction> getDebitTransactions(String accountNo) throws Exception{
        ObservableList<SharesTransaction> observableListCredit = FXCollections.observableArrayList();
        observableListCredit.setAll(ManageSharesTansaction.getSharesTransactionsForAccountNo(accountNo, ManageSharesTansaction.DEBIT));
        return observableListCredit;
    }


    public static BigDecimal getTotal(ObservableList<SharesTransaction> sharesTransactions, int shareRewardCategory){

        BigDecimal sum = BigDecimal.ZERO;

        if(shareRewardCategory != ALL_REWARD_SUCCESSFUL_SHARES && shareRewardCategory != ALL_REWARD_PENDING_SHARES && shareRewardCategory != ALL_SHARES){

            throw new IllegalArgumentException("Please sharedRewardCategoty Must Be ALL_REWARD_SUCCESSFUL_SHARES or ALL_REWARD_PENDING_SHARES or ALL_SHARES");

        }

        if(sharesTransactions == null)
            return sum;
        Iterator<SharesTransaction> iterator =  sharesTransactions.iterator();
        if(shareRewardCategory == ALL_SHARES) {
            while (iterator.hasNext()) {
                sum = sum.add(iterator.next().getAmount());
            }
        }else if(shareRewardCategory == ALL_REWARD_PENDING_SHARES) {

            while (iterator.hasNext()) {
                if (iterator.next().getStatus() == SHARES_REWARD_PENDING)
                    sum = sum.add(iterator.next().getAmount());
            }
        }else if( shareRewardCategory == ALL_REWARD_SUCCESSFUL_SHARES){
            while (iterator.hasNext()) {
                if (iterator.next().getStatus() == SHARES_REWARD_SUCCESSFUL)
                    sum = sum.add(iterator.next().getAmount());
            }
        }

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


    public static ObservableList<SharesDistributionTransaction> getAllMonthlyDistributedTransactionList() {
        SessionFactory sessionFactory = CustomUtility.getSessionFactory();
        Session session = sessionFactory.openSession();

        ObservableList<SharesDistributionTransaction> currentObserveSharesDistributionTransactionList = FXCollections.observableArrayList();

        Transaction transactionA = session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<SharesDistributionTransaction> query = builder.createQuery(SharesDistributionTransaction.class);
        query.from(SharesDistributionTransaction.class);

        //get this information from the database
        List<SharesDistributionTransaction> memberAccountList = session.createQuery(query).getResultList();
        currentObserveSharesDistributionTransactionList.setAll(memberAccountList);


        transactionA.commit();

        return currentObserveSharesDistributionTransactionList;
    }
}
