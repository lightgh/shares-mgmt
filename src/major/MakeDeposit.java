package major;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
public class MakeDeposit {

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

    ObservableList<AccountTransaction> observeAccountTransactionListData = FXCollections.observableArrayList();



    @FXML
    private Button closeButton;

    @FXML
    private Label labelID;

    private FilteredList<AccountTransaction> filteredList;


    private SortedList<AccountTransaction> sortedList;



    private boolean error = false;
    private BigDecimal amount = null;
    private LocalDate transactionDate = null;
    private String trasactionDescription = "";


    @FXML
    void initialize() {
        assert fullnameDisplay != null : "fx:id=\"fullnameDisplay\" was not injected: check your FXML file 'MakeDepositView.fxml'.";
        assert accountNumberDisplay != null : "fx:id=\"accountNumberDisplay\" was not injected: check your FXML file 'MakeDepositView.fxml'.";

        //UpdateView();
    }

    public void UpdateView(){

        prepareAccountTransactionListTable();
        updateAccountTrasactionList(accountNumberDisplay.getText());
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
                            amount.doubleValue() < 100 ||
                            trasactionDescription.equals(null) ||
                            trasactionDescription.trim() == "" ||
                            transactionDate == null ||
                            transactionDate.toString().trim() == ""

                    ) {

                error = true;

            }
        }
        catch(java.lang.NumberFormatException ex){
            ex.printStackTrace();
                error = true;
        }

        if(error){
            CustomUtility.AlertHelper("Deposit Information", "Error:\nPlease Recheck the Amount and details And DATE of Transaction you wish to deposit",
                    "Please The Form Fields Are Required", "I").show();
        }


        naAddButton.setDisable(true);

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

                updateTableView(accountTransaction);

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

    private void updateTableView(AccountTransaction accountTransaction) {

        if(accountTransaction != null){
            CustomUtility.AlertHelper("Amount Deposited Sccessfully", "Successfully Deposited",
                    "Successfully Created", Alert.AlertType.INFORMATION).show();
            clearAllButton();
            naAddButton.setDisable(false);
            prepareAccountTransactionListTable();
            updateAccountTrasactionList(accountTransaction.getAccountNo());
        }else{
            CustomUtility.AlertHelper("Error Creating New Account", "New Account was UnSuccessfully",
                    "New Account was Not Successfully Created", Alert.AlertType.ERROR).show();
            naAddButton.setDisable(false);
        }

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

        tableViewDeposits.setItems(observeAccountTransactionListData);

    }

    public void updateAccountTrasactionList(String accountNo){

        Task<Boolean> task = new Task<Boolean>() {

            @Override protected Boolean call() throws Exception {
                observeAccountTransactionListData.clear();
                boolean outcome;

                observeAccountTransactionListData = getAccountTransactionList(accountNo);

                tableViewDeposits.setItems(observeAccountTransactionListData);

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

    public ObservableList<AccountTransaction> getAccountTransactionList(String accountNumber) throws Exception{

        ObservableList<AccountTransaction> currentObserveAccountTransactionList = FXCollections.observableArrayList();

        SessionFactory sessionFactory = CustomUtility.getSessionFactory();
        Session session = sessionFactory.openSession();

        if(accountNumber == null){
            currentObserveAccountTransactionList = ManageAccountTansaction.getAllAccountTransactionList();
        }else{
            currentObserveAccountTransactionList = ManageAccountTansaction.getAccountTransactionsForAccountNo(accountNumber);
        }


        return currentObserveAccountTransactionList;
    }

    @FXML
    public void setFilterTransactionList() throws Exception{

        this.observeAccountTransactionListData = ManageAccountTansaction.getAccountTransactionsForAccountNo(accountNumberDisplay.getText());

        filteredList = new FilteredList<AccountTransaction>(observeAccountTransactionListData, p->true);
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

    public void clearAllButton(){
        this.naTransactionDate.setValue(null);
        this.textAreaTransDescription.setText("");
        this.naAccountNo.setText("");
        this.naAmount.setText("");
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
}
