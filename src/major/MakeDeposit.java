package major;

import javafx.application.Platform;
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
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.swing.*;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static major.CustomUtility.println;
import static major.MainViewDashboardController.print;

/**
 * Created by chinakalight on 7/7/18.
 */
public class MakeDeposit {

    @FXML
    public Button btnPrintAccountDeposit;
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
    private TableView<AccountTransaction> tableViewDeposits;

    @FXML
    private TableView<AccountTransaction> tableViewWithdrawals;

    @FXML
    private TableColumn<AccountTransaction, String> colID;

    @FXML
    private TableColumn<AccountTransaction, String> colSN;

    @FXML
    private TableColumn<AccountTransaction, String> colAccountNo;

    @FXML
    private TableColumn<AccountTransaction, BigDecimal> colAmount;

    @FXML
    private TableColumn<AccountTransaction, String> colType;

    @FXML
    private TableColumn<AccountTransaction, String> colComment;

    @FXML
    private TableColumn<AccountTransaction, Date> colDate;

    @FXML
    private TableColumn<AccountTransaction, String> colID1;

    @FXML
    private TableColumn<AccountTransaction, String> colSN1;

    @FXML
    private TableColumn<AccountTransaction, String> colAccountNo1;

    @FXML
    private TableColumn<AccountTransaction, BigDecimal> colAmount1;

    @FXML
    private TableColumn<AccountTransaction, String> colType1;

    @FXML
    private TableColumn<AccountTransaction, String> colComment1;

    @FXML
    private TableColumn<AccountTransaction, Date> colDate1;

    //Track Credit Transactions
    ObservableList<AccountTransaction> observeAccountCreditTransactionListData = FXCollections.observableArrayList();

    //Track Debit Transactions
    ObservableList<AccountTransaction> observeAccountDebitTransactionListData = FXCollections.observableArrayList();

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

    private FilteredList<AccountTransaction> filteredList;
    private FilteredList<AccountTransaction> filteredListWithdrawal;


    private SortedList<AccountTransaction> sortedList;
    private SortedList<AccountTransaction> sortedListWithdrawal;



    private boolean error = false, errorDebit;
    private BigDecimal amount = null, amountDebit = null;
    private LocalDate transactionDate = null, transactionDebitDate = null;
    private String trasactionDescription = "", transactionDebitDescription = "";
    private Map<String, Object> paramenters;
    private JRViewer jrViewer;


    @FXML
    void initialize() {
        assert fullnameDisplay != null : "fx:id=\"fullnameDisplay\" was not injected: check your FXML file 'MakeDepositView.fxml'.";
        assert accountNumberDisplay != null : "fx:id=\"accountNumberDisplay\" was not injected: check your FXML file 'MakeDepositView.fxml'.";

        //UpdateView();
    }

    public void UpdateView(){

        prepareAccountTransactionListTable();
        updateAccountTransactionList(accountNumberDisplay.getText());
        CustomUtility.pln("TESTING - TESTING-AccountNo-: " + accountNumberDisplay.getText());
    }



    @FXML
    void addNewTransactionRecord(ActionEvent event) {
        error = false;
        try {
            amount = new BigDecimal(naAmount.getText().trim());
            trasactionDescription = textAreaTransDescription.getText();
            transactionDate = naTransactionDate.getValue();

            if (
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
        catch(java.lang.NumberFormatException ex){
            ex.printStackTrace();
                error = true;
        }

        naAddButton.setDisable(true);

        if(error){
            CustomUtility.AlertHelper("Deposit Information", "Error:\nPlease Recheck the Amount and details And DATE of Transaction you wish to deposit",
                    "Please The Form Fields Are Required", "I").show();

            naAddButton.setDisable(true);
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
                accountTransaction.setTransaction_type("CREDIT");

                accountTransaction.setStatus(1);
                
                session.save(accountTransaction);
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
    void addNewWithdrawalRecord(ActionEvent event) throws Exception {
        error = false;


            amountDebit = new BigDecimal(naAmount1.getText().trim());
            transactionDebitDescription = textAreaTransDescription1.getText();
            transactionDebitDate = naTransactionDate1.getValue();

            BigDecimal currentAvailableAmount = ManageAccountTansaction.getAccountBalance(accountNumberDisplay.getText());

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

                    CustomUtility.AlertHelper("Withdrawal Information", "Error:\nPlease Recheck the Amount and details And DATE of Transaction you wish to withdraw",
                            "Please The Form Fields Are Required", "I").show();

               return;

            }

        if(amountDebit.doubleValue() > currentAvailableAmount.doubleValue()){
                    CustomUtility.AlertHelper("Withdrawal Information", "Error:\nINSUFFICIENT FUNDS CURRENTLY AVAILABLE. CAN'T EXCEED ",
                            "Error:\nINSUFFICIENT FUNDS CURRENTLY AVAILABLE. CAN'T EXCEED " + String.format("N %.2f ", currentAvailableAmount.doubleValue()), "I").show();
                    return;
        }


        if(error){
            CustomUtility.AlertHelper("Withdrawal Information", "Error:\nPlease Recheck the Amount and details And DATE of Transaction you wish to withdraw",
                    "Please The Form Fields Are Required", "I").show();

            CustomUtility.pln("EXECUTED-HERE");

        }

        naAddButton1.setDisable(true);

        Task<AccountTransaction> task = new Task<AccountTransaction>() {

            @Override protected AccountTransaction call() throws Exception {

                SessionFactory sessionFactory = CustomUtility.getSessionFactory();

                Session session = sessionFactory.openSession();

                Transaction transaction = session.beginTransaction();

                println("STAGE_START");
                AccountTransaction accountTransaction = new AccountTransaction();
                accountTransaction.setAccountNo(accountNumberDisplay.getText());
                accountTransaction.setDescription(transactionDebitDescription);
                accountTransaction.setAmount(amountDebit);
                accountTransaction.setTransaction_date(CustomUtility.getDateFromLocalDate(transactionDebitDate));
                accountTransaction.setTransaction_type("DEBIT");

                println("STAGE_PROCESSING-1");
                accountTransaction.setStatus(1);

                session.save(accountTransaction);
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
                title = "Amount Withdrawn Successfully";
                headerText = "Successfully Withdrawn";
                contentText = "Withdrawal Transaction Completed Successfully";
            }else{ //(type.equals("CREDIT")){
                title = "Amount Deposited/Credited Successfully";
                headerText = "Successfully Deposited/Credited";
                contentText = "Deposit/Credit Transaction Completed Successfully";
            }

            clearAllButton(type);

            prepareAccountTransactionListTable();
            updateAccountTransactionList(accountTransaction.getAccountNo());
        }else{

            if(type.equalsIgnoreCase("DEBIT")){
                title = "Error Making Withdrawals";
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


        totalLabelSumDeposit.setText(String.format("%s%.3f","TOTAL DEPOSITED SUM IS: ", ManageAccountTansaction.getTotalCredited(accountTransaction.getAccountNo()).doubleValue()));
        totalLabelSumWithdrawn.setText(String.format("%s%.3f","TOTAL SUM WITHDRAWN IS: ", ManageAccountTansaction.getTotalDebited(accountTransaction.getAccountNo()).doubleValue()));
        accountBalance.setText(String.format("%s%.3f","", ManageAccountTansaction.getAccountBalance(accountTransaction.getAccountNo()).doubleValue()));


    }

    /**
     * popultate Account Transaction List Table Method
     */
    private void prepareAccountTransactionListTable() {

        colID.setCellValueFactory(new PropertyValueFactory<AccountTransaction, String>("Id"));
        colSN.setCellValueFactory(new PropertyValueFactory<AccountTransaction, String>("Id"));
        colType.setCellValueFactory(new PropertyValueFactory<AccountTransaction, String>("transaction_type"));
        colAmount.setCellValueFactory(new PropertyValueFactory<AccountTransaction, BigDecimal>("amount"));
        colComment.setCellValueFactory(new PropertyValueFactory<AccountTransaction, String>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<AccountTransaction, Date>("transaction_date"));
        colAccountNo.setCellValueFactory(new PropertyValueFactory<AccountTransaction, String>("accountNo"));

        colID.setVisible(false);
        colSN.setVisible(false);

        tableViewDeposits.getColumns().setAll(colID, colSN, colAccountNo, colAmount, colType, colComment, colDate);

        tableViewDeposits.setItems(observeAccountCreditTransactionListData);

        
        colID1.setCellValueFactory(new PropertyValueFactory<AccountTransaction, String>("Id"));
        colSN1.setCellValueFactory(new PropertyValueFactory<AccountTransaction, String>("Id"));
        colType1.setCellValueFactory(new PropertyValueFactory<AccountTransaction, String>("transaction_type"));
        colAmount1.setCellValueFactory(new PropertyValueFactory<AccountTransaction, BigDecimal>("amount"));
        colComment1.setCellValueFactory(new PropertyValueFactory<AccountTransaction, String>("description"));
        colDate1.setCellValueFactory(new PropertyValueFactory<AccountTransaction, Date>("transaction_date"));
        colAccountNo1.setCellValueFactory(new PropertyValueFactory<AccountTransaction, String>("accountNo"));

        colID1.setVisible(false);
        colSN1.setVisible(false);

        tableViewWithdrawals.getColumns().setAll(colID1, colSN1, colAccountNo1, colAmount1, colType1, colComment1, colDate1);

        tableViewWithdrawals.setItems(observeAccountDebitTransactionListData);

    }


    public void updateAccountTransactionList(String accountNo){

        Task<Boolean> task = new Task<Boolean>() {

            @Override protected Boolean call() throws Exception {
                observeAccountCreditTransactionListData.clear();
                observeAccountDebitTransactionListData.clear();
                boolean outcome;

                observeAccountCreditTransactionListData = ManageAccountTansaction.getAccountTransactionsForAccountNo(accountNo, ManageAccountTansaction.CREDIT);
                observeAccountDebitTransactionListData = ManageAccountTansaction.getAccountTransactionsForAccountNo(accountNo, ManageAccountTansaction.DEBIT);

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

        this.observeAccountCreditTransactionListData = ManageAccountTansaction.getCreditTransactions(accountNumberDisplay.getText());

        filteredList = new FilteredList<AccountTransaction>(observeAccountCreditTransactionListData, p->true);
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

            sortedList = new SortedList<AccountTransaction>(filteredList);
            sortedList.comparatorProperty().bind(tableViewDeposits.comparatorProperty());
            tableViewDeposits.setItems(sortedList);
        });
    }

    public void clearAllButton(String type){
        if(type.equals("DEBIT")){
            this.naTransactionDate1.setValue(null);
            this.textAreaTransDescription1.setText("Debit Account");
            this.naAccountNo1.setText("");
            this.naAmount1.setText("");
        }else {
            this.naTransactionDate.setValue(null);
            this.textAreaTransDescription.setText("Credit Account");
            this.naAccountNo.setText("");
            this.naAmount.setText("");
        }
    }



    public void btnPrintAccountDeposit(ActionEvent actionEvent) throws Exception {

        btnPrintAccountDeposit.setDisable(true);


        new Thread(new Task() {
            @Override
            protected Boolean call() throws Exception {

                InputStream reportStream = MainViewDashboardController.class.getClass().getResourceAsStream("/major/AccountTransactionListReport.jrxml");

                ObservableList<AccountTransaction> accountTransactionObservableList = ManageAccountTansaction.getAccountTransactionsForAccountNo(accountNumberDisplay.getText());
                paramenters = new HashMap<>();
                paramenters.put("title", fullnameDisplay.getText()+" Account Transaction Details: "+accountNumberDisplay.getText());
                paramenters.put("summary", "Complete Transaction Statement. "+ accountTransactionObservableList.size()+" Transactions" );
                paramenters.put("accbal", "AccBal: "+ String.format("N %,.2f", Double.parseDouble(accountBalance.getText().trim()) ) );
                String path = MainViewDashboardController.class.getClass().getResource("/major/images/co-op-stronger-together.jpg").getPath();
                paramenters.put("logo", path);
                JasperReport jasperReport = null;
                try {
                    jasperReport = JasperCompileManager.compileReport(reportStream);
                } catch (JRException e) {
                    e.printStackTrace();
                }

                JasperPrint jasperPrint = null;
                try {
                    JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(accountTransactionObservableList);
                    jasperPrint = JasperFillManager.fillReport(jasperReport, paramenters, beanCollectionDataSource);
                } catch (JRException e) {
                    e.printStackTrace();
                }
                jrViewer = new JRViewer(jasperPrint);

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JFrame print = new JFrame("PrintOut");
                        print.add(jrViewer);
                        print.setSize(900, 900);
                        print.setVisible(true);
                        print.setLocationRelativeTo(null);
//            print.setIconImage();
                        print.toFront();
                        print.setAlwaysOnTop(true);
                        print.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    }
                });

                return true;
            }

            @Override
            public void done(){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        btnPrintAccountDeposit.setDisable(false);
                    }
                });
            }


        }).start();
    }

    public void makeWithdrawalSelection(Event event) {

        tabpaneViewSection.getSelectionModel().select(1);
    }

    @FXML
    public void makeDepositSelection(Event event){
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
        this.observeAccountCreditTransactionListData = ManageAccountTansaction.getAccountTransactionsForAccountNo(accountNumberDisplay.getText());

        filteredListWithdrawal = new FilteredList<AccountTransaction>(observeAccountDebitTransactionListData, p->true);
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

            sortedListWithdrawal = new SortedList<AccountTransaction>(filteredListWithdrawal);
            sortedListWithdrawal.comparatorProperty().bind(tableViewWithdrawals.comparatorProperty());
            tableViewWithdrawals.setItems(sortedListWithdrawal);
        });
    }
}
