package major;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import static major.CustomUtility.println;

/**
 * Created by chinakalight on 7/7/18.
 */
public class ManageSharesController {

    //TODO - Eliminate these variables
    /*public static String fullName;
    public static String accountNumber;
    public static int id;
    */

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label fullnameDisplay;

    @FXML
    private Label accountNumberDisplay;


    @FXML
    private Label accountBalance;

    @FXML
    private Label accountBalance1;

    @FXML
    private Label totalLabelSumDeposit;

    @FXML
    private TextField naAmount;

    @FXML
    private TextArea textAreaTransDescription;

    @FXML
    private TextField naAccountNo;

    @FXML
    private DatePicker naTransactionDate;

    @FXML
    private Button naAddButton;

    @FXML
    private TextField filterTransactionList;

    @FXML
    private TableView<SharesTransaction> tableViewDeposits;

    @FXML
    private TableView<SharesTransaction> tableViewWithdrawals;

    @FXML
    private TableColumn<SharesTransaction, String> colID;

    @FXML
    private TableColumn<SharesTransaction, String> colSN;

    @FXML
    private TableColumn<SharesTransaction, String> colAccountNo;

    @FXML
    private TableColumn<SharesTransaction, BigDecimal> colAmount;

    @FXML
    private TableColumn<SharesTransaction, String> colType;

    @FXML
    private TableColumn<SharesTransaction, String> colComment;

    @FXML
    private TableColumn<SharesTransaction, Date> colDate;

    @FXML
    private TableColumn<SharesTransaction, String> colID1;

    @FXML
    private TableColumn<SharesTransaction, String> colSN1;

    @FXML
    private TableColumn<SharesTransaction, String> colAccountNo1;

    @FXML
    private TableColumn<SharesTransaction, BigDecimal> colAmount1;

    @FXML
    private TableColumn<SharesTransaction, String> colType1;

    @FXML
    private TableColumn<SharesTransaction, String> colComment1;

    @FXML
    private TableColumn<SharesTransaction, Date> colDate1;

    //Track Credit Transactions
    ObservableList<SharesTransaction> observeAccountCreditTransactionListData = FXCollections.observableArrayList();

    //Track Debit Transactions
    ObservableList<SharesTransaction> observeAccountDebitTransactionListData = FXCollections.observableArrayList();

    @FXML
    private TextField naAmount1;

    @FXML
    private TextArea textAreaTransDescription1;

    @FXML
    private TextField naAccountNo1;

    @FXML
    private DatePicker naTransactionDate1;

    @FXML
    private Button naAddButton1;

    @FXML
    private TabPane tabpaneActionSection;

    @FXML
    private Tab makeDepostActionTab;

    @FXML
    private Tab makeWithdrawalActionTab;

    @FXML
    private TabPane tabpaneViewSection;

    @FXML
    private Tab makeDepostViewTab;

    @FXML
    private Tab makeWithdrawalViewTab;

    @FXML
    private Label totalLabelSumWithdrawn;

    @FXML
    private TextField filterWithdrawalList;


    @FXML
    private Button closeButton;

    @FXML
    private Label labelID;

    private FilteredList<SharesTransaction> filteredList;
    private FilteredList<SharesTransaction> filteredListWithdrawal;


    private SortedList<SharesTransaction> sortedList;
    private SortedList<SharesTransaction> sortedListWithdrawal;



    private boolean error = false, errorDebit;
    private BigDecimal amount = null, amountDebit = null;
    private LocalDate transactionDate = null, transactionDebitDate = null;
    private String trasactionDescription = "", transactionDebitDescription = "";


    @FXML
    void initialize() {
        assert fullnameDisplay != null : "fx:id=\"fullnameDisplay\" was not injected: check your FXML file 'MakeDepositView.fxml'.";
        assert accountNumberDisplay != null : "fx:id=\"accountNumberDisplay\" was not injected: check your FXML file 'MakeDepositView.fxml'.";

        //UpdateView();
    }

    public void UpdateView(){

        prepareSharesTransactionListTable();
        updateSharesTransactionList(accountNumberDisplay.getText());
        CustomUtility.pln("TESTING - TESTING-AccountNo-: " + accountNumberDisplay.getText());
    }


    @FXML
    void buyNewSharesRecord(ActionEvent event) throws Exception {
        error = false;
        try {
            amount = new BigDecimal(naAmount.getText().trim());
            trasactionDescription = textAreaTransDescription.getText();
            transactionDate = naTransactionDate.getValue();


            if(
               amount.equals(null) ||
               amount.equals(0) ||
               amount.doubleValue() < ManageAccountTansaction.MINIMUM_AMOUNT_TO_WITHDRAW_DEPOSIT ||
               trasactionDescription.equals(null) ||
               trasactionDescription.trim() == "" ||
               transactionDate == null ||
               transactionDate.toString().trim() == ""
                    ){

                error = true;

            }
        }
        catch(NumberFormatException ex){
            ex.printStackTrace();
                error = true;
        }

        BigDecimal availableAccountBalance = ManageAccountTansaction.getAccountBalance(this.accountNumberDisplay.getText());

        if(amount.doubleValue() > availableAccountBalance.doubleValue()){
            CustomUtility.AlertHelper("Shares Buying Information", "Error:\nINSUFFICIENT FUNDS IN YOUR ACCOUNT",
                    "Amount To Buy Shares Exceeds Available Amount In Your Account Balance ", "I").show();

            naAddButton.setDisable(false);

            return;
        }


        if(error){
            CustomUtility.AlertHelper("Deposit Information", "Error:\nPlease Recheck the Amount and details And DATE of Transaction you wish to deposit",
                    "Please The Form Fields Are Required", "I").show();

            naAddButton.setDisable(false);
            return;
        }


        Task<AccountTransaction> task = new Task<AccountTransaction>() {

            @Override protected AccountTransaction call() throws Exception {

                SessionFactory sessionFactory = CustomUtility.getSessionFactory();

                Session session = sessionFactory.openSession();

                Transaction transaction = session.beginTransaction();

                AccountTransaction accountTransaction = new AccountTransaction();
                accountTransaction.setAccountNo(accountNumberDisplay.getText());
                accountTransaction.setDescription(trasactionDescription);
                accountTransaction.setAmount(amount);
                accountTransaction.setTransaction_date(CustomUtility.getDateFromLocalDate(transactionDate));
                accountTransaction.setTransaction_type("DEBIT");
                accountTransaction.setStatus(1);
                session.save(accountTransaction);

                SharesTransaction sharesTransaction = new SharesTransaction();
                sharesTransaction.setAccountNo(accountNumberDisplay.getText());
                sharesTransaction.setDescription(trasactionDescription);
                sharesTransaction.setAmount(amount);
                sharesTransaction.setTransaction_date(CustomUtility.getDateFromLocalDate(transactionDate));
                sharesTransaction.setTransaction_type("CREDIT");
                sharesTransaction.setStatus(1);
                session.save(sharesTransaction);

                transaction.commit();

                println("Successfully inserted");

                boolean outcome = true;

                updateTableView(accountTransaction, "CREDIT");

                return null;

            }

            @Override protected void succeeded() {
                super.succeeded();
            }

            @Override protected void cancelled() {
                super.cancelled();
            }

            @Override protected void failed() {
                super.failed();
            }
        };

        task.run();

    }


    @FXML
    void addExitingSalesSharesRecord(ActionEvent event) throws Exception {
        error = false;


            amountDebit = new BigDecimal(naAmount1.getText().trim());
            transactionDebitDescription = textAreaTransDescription1.getText();
            transactionDebitDate = naTransactionDate1.getValue();

            BigDecimal currentAvailableShares = ManageSharesTansaction.getSharesBalance(accountNumberDisplay.getText());

            if (
               amountDebit.equals(null) ||
               amountDebit.equals(0) ||
               amountDebit.doubleValue() < ManageAccountTansaction.MINIMUM_AMOUNT_TO_WITHDRAW_DEPOSIT ||
               transactionDebitDescription.equals(null) ||
               transactionDebitDescription.trim() == "" ||
               transactionDebitDate == null ||
               transactionDebitDate.toString().trim() == ""
                    ){
                    error = true;

                    CustomUtility.AlertHelper("Shares Sales Error Notification", "Error:\nPlease You Need to Specify Fields To Sale Shares",
                            "Please The Shares Sales Form Fields Are Required", "I").show();

               return;

            }

        if(amountDebit.doubleValue() > currentAvailableShares.doubleValue()){
                    CustomUtility.AlertHelper("Shares Sales Error Information", "Error:\nYOU CANNOT SALE SHARES WORTH MORE THAN YOUR PURCHASED ACTIVE SHARES",
                            "Error:\nYOU CAN'T SALE SHARES WORTH MORE THAN WHAT'S CURRENTLY AVAILABLE. CAN'T EXCEED " + String.format("N %.2f ", currentAvailableShares.doubleValue()), "I").show();
                    return;
        }


        if(error){
            CustomUtility.AlertHelper("Withdrawal Information", "Error:\nPlease Recheck the Amount and details And DATE of Transaction you wish to withdraw",
                    "Please The Form Fields Are Required", "I").show();

            CustomUtility.pln("EXECUTED-HERE");

        }

        naAddButton1.setDisable(true);

        Task<SharesTransaction> task = new Task<SharesTransaction>() {

            @Override protected SharesTransaction call() throws Exception {

                SessionFactory sessionFactory = CustomUtility.getSessionFactory();

                Session session = sessionFactory.openSession();

                Transaction transaction = session.beginTransaction();

                println("STAGE_START");
                AccountTransaction accountTransaction = new AccountTransaction();
                accountTransaction.setAccountNo(accountNumberDisplay.getText());
                accountTransaction.setDescription(transactionDebitDescription);
                accountTransaction.setAmount(amountDebit);
                accountTransaction.setTransaction_date(CustomUtility.getDateFromLocalDate(transactionDebitDate));
                accountTransaction.setTransaction_type("CREDIT");
                accountTransaction.setStatus(1);
                session.save(accountTransaction);


                SharesTransaction sharesTransaction = new SharesTransaction();
                sharesTransaction.setAccountNo(accountNumberDisplay.getText());
                sharesTransaction.setDescription(transactionDebitDescription);
                sharesTransaction.setAmount(amountDebit);
                sharesTransaction.setTransaction_date(CustomUtility.getDateFromLocalDate(transactionDebitDate));
                sharesTransaction.setTransaction_type("DEBIT");
                sharesTransaction.setStatus(1);
                session.save(sharesTransaction);


                println("STAGE_PROCESSING-1");

                println("STAGE_PROCESSING-2");
                transaction.commit();
                println("STAGE_PROCESSING-3");

                println("Successfully inserted");

                boolean outcome = true;
                updateTableView(accountTransaction, "DEBIT");

                return null;
            }

            @Override protected void succeeded() {
                super.succeeded();
            }

            @Override protected void cancelled() {
                super.cancelled();
            }

            @Override protected void failed() {
                super.failed();
            }
        };

        task.run();

    }

    private void updateTableView(AccountTransaction accountTransaction, String type) throws Exception {
        String title;
        String headerText;
        String contentText;

        if(type == null){
            throw new TypeNotPresentException("Please Specify The Transaction Type", new Exception("Type Must Be Present."));
        }

        if(accountTransaction != null){

            if(type.equalsIgnoreCase("DEBIT")){
                title = "Shares Sales Completed Successfully";
                headerText = "Shares Successfully Withdrawn";
                contentText = "Shares Withdrawal Transaction Completed Successfully!\nAnd Your Account Has Been Successfully Credited";
            }else{ //(type.equals("CREDIT")){
                title = "Shares Amount Successfully Purchased";
                headerText = "Shares Successfully Purchased.";
                contentText = "Shares Sales Transaction Successfully Purchased Successfully\nAnd Your Account Has Been Successfully Debited";
            }

            clearAllButton(type);

            prepareSharesTransactionListTable();
            updateSharesTransactionList(accountTransaction.getAccountNo());
        }else{

            if(type.equalsIgnoreCase("DEBIT")){
                title = "Error Buying Shares";
                headerText = "UnSuccessfully Withdrawn";
                contentText = "Withdrawal Transaction Was UnSuccessfully";
            }else{ //(type.equals("CREDIT")){
                title = "Depositing/Crediting Account Was UnSuccessfully";
                headerText = "Successfully Deposited/Credited";
                contentText = "Deposit/Credit Transaction Was UnSuccessful";
            }

        }

        CustomUtility.AlertHelper(title, headerText,
                contentText, Alert.AlertType.INFORMATION).show();

        naAddButton1.setDisable(false);
        naAddButton.setDisable(false);


        totalLabelSumDeposit.setText(String.format("%s%.3f","TOTAL SHARES PURCHASED IS: ", ManageSharesTansaction.getTotalCredited(accountTransaction.getAccountNo()).doubleValue()));
        totalLabelSumWithdrawn.setText(String.format("%s%.3f","TOTAL SHARES SOLD IS: ", ManageSharesTansaction.getTotalDebited(accountTransaction.getAccountNo()).doubleValue()));
        accountBalance.setText(String.format("%s%.3f","", ManageAccountTansaction.getAccountBalance(accountTransaction.getAccountNo()).doubleValue()));
        accountBalance1.setText(String.format("%s%.3f","", ManageSharesTansaction.getSharesBalance(accountTransaction.getAccountNo()).doubleValue()));


    }

    /**
     * popultate Account Transaction List Table Method
     */
    private void prepareSharesTransactionListTable() {

        colID.setCellValueFactory(new PropertyValueFactory<SharesTransaction, String>("Id"));
        colSN.setCellValueFactory(new PropertyValueFactory<SharesTransaction, String>("Id"));
        colType.setCellValueFactory(new PropertyValueFactory<SharesTransaction, String>("transaction_type"));
        colAmount.setCellValueFactory(new PropertyValueFactory<SharesTransaction, BigDecimal>("amount"));
        colComment.setCellValueFactory(new PropertyValueFactory<SharesTransaction, String>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<SharesTransaction, Date>("transaction_date"));
        colAccountNo.setCellValueFactory(new PropertyValueFactory<SharesTransaction, String>("accountNo"));

        colID.setVisible(false);
        colSN.setVisible(false);

        tableViewDeposits.getColumns().setAll(colID, colSN, colAccountNo, colAmount, colType, colComment, colDate);

        tableViewDeposits.setItems(observeAccountCreditTransactionListData);

        
        colID1.setCellValueFactory(new PropertyValueFactory<SharesTransaction, String>("Id"));
        colSN1.setCellValueFactory(new PropertyValueFactory<SharesTransaction, String>("Id"));
        colType1.setCellValueFactory(new PropertyValueFactory<SharesTransaction, String>("transaction_type"));
        colAmount1.setCellValueFactory(new PropertyValueFactory<SharesTransaction, BigDecimal>("amount"));
        colComment1.setCellValueFactory(new PropertyValueFactory<SharesTransaction, String>("description"));
        colDate1.setCellValueFactory(new PropertyValueFactory<SharesTransaction, Date>("transaction_date"));
        colAccountNo1.setCellValueFactory(new PropertyValueFactory<SharesTransaction, String>("accountNo"));

        colID1.setVisible(false);
        colSN1.setVisible(false);

        tableViewWithdrawals.getColumns().setAll(colID1, colSN1, colAccountNo1, colAmount1, colType1, colComment1, colDate1);

        tableViewWithdrawals.setItems(observeAccountDebitTransactionListData);

    }


    public void updateSharesTransactionList(String accountNo){

        Task<Boolean> task = new Task<Boolean>() {

            @Override protected Boolean call() throws Exception {
                observeAccountCreditTransactionListData.clear();
                observeAccountDebitTransactionListData.clear();
                boolean outcome;

                observeAccountCreditTransactionListData = ManageSharesTansaction.getSharesTransactionsForAccountNo(accountNo, ManageAccountTansaction.CREDIT);
                observeAccountDebitTransactionListData = ManageSharesTansaction.getSharesTransactionsForAccountNo(accountNo, ManageAccountTansaction.DEBIT);

                tableViewDeposits.setItems(observeAccountCreditTransactionListData);
                tableViewWithdrawals.setItems(observeAccountDebitTransactionListData);

                outcome = true;

                return outcome;

            }

            @Override protected void succeeded() {
                super.succeeded();
                println("WORKED!!");
            }

            @Override protected void cancelled() {
                super.cancelled();
            }

            @Override protected void failed() {
                super.failed();
            }

        };

        task.run();

    }


    @FXML
    public void setFilterDepositTransactionList() throws Exception{

        this.observeAccountCreditTransactionListData = ManageSharesTansaction.getCreditTransactions(accountNumberDisplay.getText());

        filteredList = new FilteredList<SharesTransaction>(observeAccountCreditTransactionListData, p->true);
        filterTransactionList.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(pere -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }

                String typedText = newValue.toLowerCase();

                if(pere.getAccountNo().toLowerCase().indexOf(typedText) != -1){
                    return true;
                }

                if(pere.getTransaction_date().toString().indexOf(typedText) != -1){
                    return true;
                }

                if(pere.getAmount().toString().indexOf(typedText) != -1){
                    return true;
                }

                return false;
            });

            sortedList = new SortedList<SharesTransaction>(filteredList);
            sortedList.comparatorProperty().bind(tableViewDeposits.comparatorProperty());
            tableViewDeposits.setItems(sortedList);
        });
    }

    public void clearAllButton(String type){
        if(type.equals("DEBIT")){
            this.naTransactionDate1.setValue(null);
            this.textAreaTransDescription1.setText("Debit Shares/Credit Account");
            this.naAccountNo1.setText("");
            this.naAmount1.setText("");
        }else {
            this.naTransactionDate.setValue(null);
            this.textAreaTransDescription.setText("Credit Shares, Debit Account");
            this.naAccountNo.setText("");
            this.naAmount.setText("");
        }
    }

    public void btnPrintAccountDeposit(ActionEvent actionEvent) {
        Printer printer = Printer.getDefaultPrinter();
        Stage dialogStage = new Stage(StageStyle.DECORATED);
        PrinterJob printerJob = PrinterJob.createPrinterJob();

        if (printerJob != null) {
            boolean showDialog = printerJob.showPageSetupDialog(dialogStage);
            if (showDialog) {
                tableViewDeposits.setScaleX(0.60);
                tableViewDeposits.setScaleY(0.60);
                tableViewDeposits.setTranslateX(-220);
                tableViewDeposits.setTranslateY(-70);
                boolean success = printerJob.showPrintDialog(dialogStage);
//                boolean success = printerJob.printPage(tableViewDeposits);
                if (success) {
                    printerJob.endJob();
                    printerJob.showPrintDialog(dialogStage);
//                    printerJob.
                }
                tableViewDeposits.setTranslateX(0);
                tableViewDeposits.setTranslateY(0);
                tableViewDeposits.setScaleX(1.0);
                tableViewDeposits.setScaleY(1.0);
            }
        }

    }

    public void makeWithdrawalSelection(Event event) {

        tabpaneViewSection.getSelectionModel().select(1);

/*        tabpaneActionSection;

        makeDepostActionTab;

        makeWithdrawalActionTab;

        tabpaneViewSection;

        makeDepostViewTab;

        makeWithdrawalViewTab;*/

    }

    @FXML
    public void buySharesSelection(Event event){
        tabpaneViewSection.getSelectionModel().select(0);
    }

    @FXML
    public void changeActionViewTab(Event event){}

    @FXML
    public void changeDisplayViewTab(Event event){
        tabpaneActionSection.getSelectionModel().select(tabpaneViewSection.getSelectionModel().getSelectedIndex());
    }

    @FXML
    public void setFilterWithdrawalTransactionList(KeyEvent keyEvent) throws Exception {
        this.observeAccountCreditTransactionListData = ManageSharesTansaction.getSharesTransactionsForAccountNo(accountNumberDisplay.getText());

        filteredListWithdrawal = new FilteredList<SharesTransaction>(observeAccountDebitTransactionListData, p->true);
        filterWithdrawalList.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredListWithdrawal.setPredicate(filteredParam -> {

                if(newValue == null || newValue.isEmpty()){return true;}

                String typedText = newValue.toLowerCase();

                if(filteredParam.getAccountNo().toLowerCase().indexOf(typedText) != -1){
                    return true;
                }

                if(filteredParam.getTransaction_date().toString().indexOf(typedText) != -1){
                    return true;
                }

                if(filteredParam.getAmount().toString().indexOf(typedText) != -1){
                    return true;
                }

                return false;
            });

            sortedListWithdrawal = new SortedList<SharesTransaction>(filteredListWithdrawal);
            sortedListWithdrawal.comparatorProperty().bind(tableViewWithdrawals.comparatorProperty());
            tableViewWithdrawals.setItems(sortedListWithdrawal);
        });
    }
}
