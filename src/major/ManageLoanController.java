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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static major.CustomUtility.pln;
import static major.CustomUtility.println;
import static major.ManageLoanTransaction.getTakenLoanTransactionsForLedgerNo;

/**
 * Created by chinakalight on 7/7/18.
 */
public class ManageLoanController {

    final static Logger logger = Logger.getLogger(ManageLoanController.class.getName());

    static {
        SystemOutToLog4.enableForClass(ManageLoanController.class);
    }

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
    private Label totalCollectedLoanBalance;

    @FXML
    private Label totalLoanTakenLabel;

    @FXML
    private Button naAddButton;

    @FXML
    private TextField filterTakenLoansList;

    @FXML
    private TableView<TakeLoanTransaction> tableViewTakeLoans;

    @FXML
    private DatePicker rLoanCollectedDate;

    @FXML
    private TextArea rLoanDescription;

    @FXML
    private Button rLoanAddButton;

    @FXML
    TableColumn<TakeLoanTransaction, String> colId;

    @FXML
    TableColumn<TakeLoanTransaction, String> colSN;
    @FXML
    TableColumn<TakeLoanTransaction, String> colAccountNo;
    @FXML
    TableColumn<TakeLoanTransaction, String> colLedgerNo;
    @FXML
    TableColumn<TakeLoanTransaction, BigDecimal> colLoanAmount;
    @FXML
    TableColumn<TakeLoanTransaction, Integer> colInterestRate;
    @FXML
    TableColumn<TakeLoanTransaction, Integer> colLoanPeriod;
    @FXML
    TableColumn<TakeLoanTransaction, Date> colCollectedDate;
    @FXML
    TableColumn<TakeLoanTransaction, Date> colRepayDate;
    @FXML
    TableColumn<TakeLoanTransaction, String> colLoanDescription;


    @FXML
    private TableView<ReturnLoanTransaction> tableViewReturnedLoans;


    //Track Credit Transactions
    ObservableList<TakeLoanTransaction> observeTakenLoanTransactionListData = FXCollections.observableArrayList();

    //Track Debit Transactions
    ObservableList<ReturnLoanTransaction> observeReturnedLoanTransactionListData = FXCollections.observableArrayList();


    @FXML
    private Button naAddButton1;

    @FXML
    private TabPane tabpaneActionSection;

    private TakeLoanTransaction currentTableLoanTransaction;


    @FXML
    private TabPane tabpaneViewSection;

    @FXML
    private Tab makeDepostViewTab;

    @FXML
    private Tab makeWithdrawalViewTab;

    @FXML
    private Label totalLoanReturnedLabel;

    @FXML
    private TextField filterWithdrawalList;


    @FXML
    private Button closeButton;

    @FXML
    private Label labelID;

    private FilteredList<TakeLoanTransaction> takeLoanTransactionfilteredList;
    private FilteredList<ReturnLoanTransaction> returnLoanTransactionfilteredList;


    private SortedList<TakeLoanTransaction> takeLoanTransactionSortedList;
    private SortedList<ReturnLoanTransaction> returnLoanTransactionSortedList;



    private boolean error = false;
    private BigDecimal amount, collectedLoanAmount, expectedReturnedLoan ;
    private LocalDate nLoanCollectedDateVal, nLoanRepayDateValue, returnLoanDate;
    private Integer interestRate,loanPeriod;
    private String ledgerNo;
    private String takeLoanDescription ="", returnLoanDescription = "";

    @FXML
    private TextField LoanTakenAmount;

    @FXML
    private TextField nLInterestRate;

    @FXML
    private TextField naAccountNo;

    @FXML
    private DatePicker nLoanCollectedDate;

    @FXML
    private DatePicker nLoanRepayDate;

    @FXML
    private TextArea textAreaLoanDescription;

    @FXML
    private Button nLGiveLoanButton;

    @FXML
    private Tab makeWithdrawalActionTab;

    @FXML
    private Label nLoanExpectedRepayDateLabelDisplay;

    @FXML
    private Label rLoanCollectedDateLabelDisplay;

    @FXML
    private TextField rLoanLedgerNo;

    @FXML
    private TextField nLoanLedgerNo;

    @FXML
    private TextField rLoanTotalAmountExpected;

    @FXML
    private Label rAccuredInterestAmount;

    @FXML
    private DatePicker rLoanReturnedDate;


    @FXML
    private TextField nLPeriod;

    @FXML
    private TextField rLoanPeriod;

    @FXML
    TableColumn<ReturnLoanTransaction, String>      colRLId            ;
    @FXML
    TableColumn<ReturnLoanTransaction, String>      colRLSn            ;
    @FXML
    TableColumn<ReturnLoanTransaction, String>      colRLAccountNo     ;
    @FXML
    TableColumn<ReturnLoanTransaction, BigDecimal>  colRLLedgerNo      ;
    @FXML
    TableColumn<ReturnLoanTransaction, String>      colRLLoanAmount    ;
    @FXML
    TableColumn<ReturnLoanTransaction, String>      colRLInterestRate  ;
    @FXML
    TableColumn<ReturnLoanTransaction, Date>        colRLLoanPeriod    ;
    @FXML
    TableColumn<ReturnLoanTransaction, Date>        colRLRepayDate    ;

    @FXML
    TableColumn<ReturnLoanTransaction, String>      colRLCollectedDate ;

    @FXML
    private TextField rLoanAmount;

    @FXML
    private TextField rLoanAccountNo;

    @FXML
    private TextField rLoanInterestRate;
    private Map<String, Object> paramenters;
    @FXML
    private Button btnPrintAccountDeposit;
    private JRViewer jrViewer;
    private JRViewer jrViewer_Return;

    Map<String, Object> paramenters_return_loan;


    @FXML
    void initialize() {
        assert fullnameDisplay != null : "fx:id=\"fullnameDisplay\" was not injected: check your FXML file 'MakeDepositView.fxml'.";
        assert accountNumberDisplay != null : "fx:id=\"accountNumberDisplay\" was not injected: check your FXML file 'MakeDepositView.fxml'.";
    }


    @FXML
    void takeNewLoanRecord(ActionEvent event) throws Exception {

        error = false;

        try {

            amount = new BigDecimal(LoanTakenAmount.getText().trim());
            interestRate = new Integer(nLInterestRate.getText().trim());
            ledgerNo = nLoanLedgerNo.getText();
            nLoanCollectedDateVal = nLoanCollectedDate.getValue();
            loanPeriod = new Integer(nLPeriod.getText());
            takeLoanDescription = textAreaLoanDescription.getText();
            nLoanRepayDateValue = nLoanRepayDate.getValue();

            if(
               amount.equals(null) ||
               amount.equals(0) ||
               amount.doubleValue() < ManageAccountTansaction.MINIMUM_AMOUNT_TO_WITHDRAW_DEPOSIT ||
               takeLoanDescription.equals(null) ||
               takeLoanDescription.trim() == "" ||
               nLoanCollectedDateVal == null ||
               nLoanCollectedDateVal.toString().trim() == ""
                    ){

                error = true;

            }
        }
        catch(NumberFormatException ex){
            logger.trace(ex);
                error = true;
        }

        BigDecimal totalAvailableAmount = ManageLoanTransaction.getAvailableAmount();
        BigDecimal availableAccountBalance = ManageAccountTansaction.getAccountBalance(this.accountNumberDisplay.getText());

        if(amount.doubleValue() > totalAvailableAmount.doubleValue()){
            CustomUtility.AlertHelper("Loan Taking Information", "Error:\nSUCH AMOUNT OF MONEY IS CURRENTLY UNAVAILABLE",
                    "Available Amount Can't Permit Giving Such Amount Of Loan", "I").show();

            naAddButton.setDisable(false);

            return;
        }

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


        Task<TakeLoanTransaction> task = new Task<TakeLoanTransaction>() {

            @Override protected TakeLoanTransaction call() throws Exception {

                SessionFactory sessionFactory = CustomUtility.getSessionFactory();

                Session session = sessionFactory.openSession();

                Transaction transaction = session.beginTransaction();

                TakeLoanTransaction takeLoanTransaction = new TakeLoanTransaction();
                takeLoanTransaction.setAccountNo(accountNumberDisplay.getText());
                takeLoanTransaction.setDescription(takeLoanDescription);
                takeLoanTransaction.setAmount(amount);
                takeLoanTransaction.setDateCollected(CustomUtility.getDateFromLocalDate(nLoanCollectedDateVal));
                takeLoanTransaction.setInterestRate(interestRate);
                takeLoanTransaction.setStatus(ManageLoanTransaction.TAKEN_LOAN_SUCCESS);
                takeLoanTransaction.setLedgerNo(ledgerNo);
                takeLoanTransaction.setLoanPeriod(loanPeriod);
                takeLoanTransaction.setRepayDate(CustomUtility.getDateFromLocalDate(nLoanRepayDateValue));
                session.save(takeLoanTransaction);

                transaction.commit();

                println("Successfully inserted take Loan Transaction");

                boolean outcome = true;

                updateTableView(takeLoanTransaction);

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
    void returnLoanTransactionRecord(ActionEvent event) throws Exception {
        error = false;

            //TODO - FIX THESE
            collectedLoanAmount = new BigDecimal(rLoanAmount.getText().trim());
            expectedReturnedLoan = new BigDecimal(rLoanTotalAmountExpected.getText().trim());
            String thisAccountNo = accountNumberDisplay.getText();
            loanPeriod = Integer.parseInt(rLoanPeriod.getText());
            interestRate = Integer.valueOf((int)Double.parseDouble(rLoanInterestRate.getText()));
            pln("INTERRRRREEEEEEST-RATE: " + rLoanInterestRate.getText());
            returnLoanDate = rLoanReturnedDate.getValue();
            Date dateCollected = CustomUtility.getDateFromLocalDate(rLoanCollectedDate.getValue());
            ledgerNo = rLoanLedgerNo.getText();
            returnLoanDescription = rLoanDescription.getText();


            if (
               collectedLoanAmount.equals(null) ||
               collectedLoanAmount.equals(0) ||
               collectedLoanAmount.doubleValue() < ManageAccountTansaction.MINIMUM_AMOUNT_TO_WITHDRAW_DEPOSIT ||
               returnLoanDescription.equals(null) ||
               returnLoanDescription.trim() == "" ||
               returnLoanDate == null ||
               returnLoanDate.toString().trim() == "" ||
                       returnLoanDate.isBefore(rLoanCollectedDate.getValue())
                    ){

                    CustomUtility.AlertHelper("Return Loan Notification Error", "Error:\nPlease You Need to Select Return Date To Proceed",
                            "Valid Loan Return Date Is Required PLEASE!!!: (IT MUST >> COLLECTION DATE)", "I").show();

               return;

            }

                    boolean res = CustomUtility.ConfirmationAlertHelper("Returned Loan Confirmation?", "Please Are You Show That You Are Collecting the Correct Returned (N "+collectedLoanAmount+ ") Loan  With Interest Of (N "+rAccuredInterestAmount.getText()+") Totaling: (N " + expectedReturnedLoan + ")" );

               if(!res){
                    return;
                }

        rLoanAddButton.setDisable(true);

        Task<ReturnLoanTransaction> task = new Task<ReturnLoanTransaction>() {

            @Override protected ReturnLoanTransaction call() throws Exception {

                SessionFactory sessionFactory = CustomUtility.getSessionFactory();

                Session session = sessionFactory.openSession();

                Transaction transaction = session.beginTransaction();

                println("RETURN - STAGE_START");
                ReturnLoanTransaction returnLoanTransaction = new ReturnLoanTransaction();
                returnLoanTransaction.setAccountNo(accountNumberDisplay.getText());
                returnLoanTransaction.setDescription(returnLoanDescription);
                returnLoanTransaction.setCollectedAmount(collectedLoanAmount);
                returnLoanTransaction.setExpectedAmount(expectedReturnedLoan);
                returnLoanTransaction.setInterestRate(interestRate);
                returnLoanTransaction.setDatePaid(CustomUtility.getDateFromLocalDate(returnLoanDate));
                returnLoanTransaction.setDateCollected(CustomUtility.getDateFromLocalDate(rLoanCollectedDate.getValue()));
                returnLoanTransaction.setLedgerNo(ledgerNo);
                returnLoanTransaction.setLoanPeriod(loanPeriod);
                returnLoanTransaction.setStatus(ManageLoanTransaction.RETURNED_LOAN_SUCCESS);
                session.save(returnLoanTransaction);

                TakeLoanTransaction relatedLoanTakenTransaction = getTakenLoanTransactionsForLedgerNo(ledgerNo);
                relatedLoanTakenTransaction.setStatus(ManageLoanTransaction.TAKEN_LOAN_RETURNED);
                session.saveOrUpdate(relatedLoanTakenTransaction);
                transaction.commit();

                println("Successfully inserted");

                boolean outcome = true;
                updateTableView(returnLoanTransaction);

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


    private void updateTableView(TakeLoanTransaction takeLoanTransacions) throws Exception {
        String title;
        String headerText;
        String contentText;


        if(takeLoanTransacions != null){

           title = "Loan Taken Successfully";
           headerText = "Loan Transactions Completed Successfully.";
           contentText = "Loan Taken Successfully Successfully";

           clearAllButton("TAKE_LOAN");

           prepareTakeLoanTransactionListTable();
           updateTakeTransactionList(takeLoanTransacions.getAccountNo());
        }else{

            title = "Loan Was Not Taken Successfully";
            headerText = "Loan Taken Transaction Was Not Successfull";
            contentText = "Loan Taken Transaction Was Not Successful. Try Again";
        }

        CustomUtility.AlertHelper(title, headerText,
                contentText, Alert.AlertType.INFORMATION).show();

        nLGiveLoanButton.setDisable(false);
        rLoanAddButton.setDisable(false);


        totalLoanTakenLabel.setText(String.format("%s%.3f","TOTAL LOAN TAKEN SO FAR IS: ", ManageLoanTransaction.getTotalTakenLoanTransactions(takeLoanTransacions.getAccountNo()).doubleValue()));
        totalLoanReturnedLabel.setText(String.format("%s%.3f","TOTAL SHARES SOLD IS: ", ManageLoanTransaction.getTotalReturnedLoanTransactions(takeLoanTransacions.getAccountNo()).doubleValue()));
        accountBalance.setText(String.format("%s%.3f","", ManageAccountTansaction.getAccountBalance(takeLoanTransacions.getAccountNo()).doubleValue()));
        totalCollectedLoanBalance.setText(String.format("%s%.3f","", ManageSharesTansaction.getSharesBalance(takeLoanTransacions.getAccountNo()).doubleValue()));

    }

    private void updateTableView(ReturnLoanTransaction returnLoanTransaction) throws Exception {
        String title;
        String headerText;
        String contentText;


        if(returnLoanTransaction != null){

           title = "Loan Returned Successfully";
           headerText = "Loan Returned Transactions Completed Successfully.";
           contentText = "Loan Returned Successfully";

           clearAllButton("RETURN_LOAN");

           prepareTakeLoanTransactionListTable();
           updateTakeTransactionList(returnLoanTransaction.getAccountNo());
        }else{

            title = "Loan Was Not Taken Successfully";
            headerText = "Loan Taken Transaction Was Not Successfull";
            contentText = "Loan Taken Transaction Was Not Successful. Try Again";
        }

        CustomUtility.AlertHelper(title, headerText,
                contentText, Alert.AlertType.INFORMATION).show();

        nLGiveLoanButton.setDisable(false);
        rLoanAddButton.setDisable(false);


        totalLoanTakenLabel.setText(String.format("%s%.3f","TOTAL LOAN TAKEN SO FAR IS: ", ManageLoanTransaction.getTotalTakenLoanTransactions(returnLoanTransaction.getAccountNo()).doubleValue()));
        totalLoanReturnedLabel.setText(String.format("%s%.3f","TOTAL SHARES SOLD IS: ", ManageLoanTransaction.getTotalReturnedLoanTransactions(returnLoanTransaction.getAccountNo()).doubleValue()));
        accountBalance.setText(String.format("%s%.3f","", ManageAccountTansaction.getAccountBalance(returnLoanTransaction.getAccountNo()).doubleValue()));
        totalCollectedLoanBalance.setText(String.format("%s%.3f","", ManageSharesTansaction.getSharesBalance(returnLoanTransaction.getAccountNo()).doubleValue()));

    }

    /**
     * popultate Account Transaction List Table Method
     */
    private void prepareTakeLoanTransactionListTable() {


        colId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colSN.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colAccountNo.setCellValueFactory(new PropertyValueFactory<>("accountNo"));
        colLedgerNo.setCellValueFactory(new PropertyValueFactory<>("ledgerNo"));
        colLoanAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colInterestRate.setCellValueFactory(new PropertyValueFactory<>("interestRate"));
        colLoanPeriod.setCellValueFactory(new PropertyValueFactory<>("loanPeriod"));
        colCollectedDate.setCellValueFactory(new PropertyValueFactory<>("dateCollected"));
        colRepayDate.setCellValueFactory(new PropertyValueFactory<>("repayDate"));
        colLoanDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        colId.setVisible(false);
        colSN.setVisible(false);

        colRLId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colRLSn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colRLAccountNo.setCellValueFactory(new PropertyValueFactory<>("accountNo"));
        colRLLedgerNo.setCellValueFactory(new PropertyValueFactory<>("ledgerNo"));
        colRLLoanAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colRLInterestRate.setCellValueFactory(new PropertyValueFactory<>("interestRate"));
        colRLLoanPeriod.setCellValueFactory(new PropertyValueFactory<>("loanPeriod"));
        colRLRepayDate.setCellValueFactory(new PropertyValueFactory<>("datePaid"));
        colRLCollectedDate.setCellValueFactory(new PropertyValueFactory<>("dateCollected"));

        colRLId.setVisible(false);
        colRLSn.setVisible(false);

        tableViewTakeLoans.getColumns().setAll(colId, colSN, colAccountNo, colLedgerNo, colLoanAmount, colInterestRate, colLoanPeriod, colCollectedDate, colRepayDate, colLoanDescription);

        tableViewTakeLoans.setItems(observeTakenLoanTransactionListData);


        tableViewReturnedLoans.getColumns().setAll(colRLId, colRLSn, colRLAccountNo, colRLLedgerNo, colRLLoanAmount, colRLCollectedDate, colRLRepayDate, colRLLoanPeriod, colRLInterestRate);

        tableViewReturnedLoans.setItems(observeReturnedLoanTransactionListData);

    }


    public void updateTakeTransactionList(String accountNo){

        Task<Boolean> task = new Task<Boolean>() {

            @Override protected Boolean call() throws Exception {
                observeTakenLoanTransactionListData.clear();
                observeReturnedLoanTransactionListData.clear();
                boolean outcome;

                observeTakenLoanTransactionListData = ManageLoanTransaction.getTakenLoanTransactionsForAccount(accountNo);
                observeReturnedLoanTransactionListData = ManageLoanTransaction.getReturnedLoanTransactionsForAccount(accountNo);

                tableViewTakeLoans.setItems(observeTakenLoanTransactionListData);
                tableViewReturnedLoans.setItems(observeReturnedLoanTransactionListData);

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
    public void setFilterTakeLoanTransactionList() throws Exception{

        this.observeTakenLoanTransactionListData = ManageLoanTransaction.getTakenLoanTransactionsForAccount(accountNumberDisplay.getText());

        takeLoanTransactionfilteredList = new FilteredList<TakeLoanTransaction>(observeTakenLoanTransactionListData, p->true);
        filterTakenLoansList.textProperty().addListener((observable, oldValue, newValue) -> {
            takeLoanTransactionfilteredList.setPredicate(pere -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }

                String typedText = newValue.toLowerCase();

                if(pere.getAccountNo().toLowerCase().indexOf(typedText) != -1){
                    return true;
                }

                if(pere.getDateCollected().toString().indexOf(typedText) != -1){
                    return true;
                }

                if(pere.getAmount().toString().indexOf(typedText) != -1){
                    return true;
                }

                if(pere.getLedgerNo().toString().indexOf(typedText) != -1){
                    return true;
                }

                if(pere.getLoanPeriod().toString().indexOf(typedText) != -1){
                    return true;
                }

                return false;
            });

            takeLoanTransactionSortedList = new SortedList<TakeLoanTransaction>(takeLoanTransactionfilteredList);
            takeLoanTransactionSortedList.comparatorProperty().bind(tableViewTakeLoans.comparatorProperty());
            tableViewTakeLoans.setItems(takeLoanTransactionSortedList);
        });
    }

    public void clearAllButton(String type){
        if(type.equals("TAKE_LOAN")){
            this.nLoanCollectedDate.setValue(null);
            this.nLoanLedgerNo.setText(CustomUtility.getUniqueLedgerNoString());
            this.textAreaLoanDescription.setText("Take Loan By Member");
            this.LoanTakenAmount.setText("");
            this.nLoanExpectedRepayDateLabelDisplay.setText("");
        }else {
            this.rLoanCollectedDate.setValue(null);
            this.rLoanLedgerNo.setText(CustomUtility.getUniqueLedgerNoString());
            this.rLoanDescription.setText("Return Loan By Member");
            this.rLoanAmount.setText("");
//            this.rLoan.setText("");
        }
    }

    public void btnPrintAccountDeposit(ActionEvent actionEvent) {
        btnPrintAccountDeposit.setDisable(true);

        new Thread(new Task() {
            @Override
            protected Boolean call() throws Exception {

                InputStream reportStream = ManageLoanController.this.getClass().getResourceAsStream("TakeLoanTransactionListReport.jrxml");
                InputStream reportStream_return_loan = ManageLoanController.this.getClass().getResourceAsStream("ReturnLoanTransactionListReport.jrxml");

                ObservableList<TakeLoanTransaction> accountTransactionObservableList = ManageLoanTransaction.getTakenLoanTransactionsForAccount((accountNumberDisplay.getText()));
                ObservableList<ReturnLoanTransaction> returnLoanTransactionObservableList = ManageLoanTransaction.getReturnedLoanTransactionsForAccount((accountNumberDisplay.getText()));

                paramenters = new HashMap<>();
                paramenters.put("title", fullnameDisplay.getText()+" Take Loan Transaction Details: "+accountNumberDisplay.getText());
                paramenters.put("summary", "Complete Loan Taken Transaction. "+ accountTransactionObservableList.size()+" Transactions" );
                paramenters.put("totalLoan", "Total Loan Taken: "+ String.format("N %,.2f", Double.parseDouble(ManageLoanTransaction.getTotalTakenLoanTransactions(accountNumberDisplay.getText()).toString())));
                BufferedImage image = ImageIO.read(ManageLoanController.this.getClass().getResourceAsStream("/major/images/co-op-stronger-together.jpg"));
                paramenters.put("logo", image);

                paramenters_return_loan = new HashMap<>();
                paramenters_return_loan.put("title", fullnameDisplay.getText()+" Returned Loan Transaction Details: "+accountNumberDisplay.getText());
                paramenters_return_loan.put("summary", "Complete Loan Returned Transaction. "+ accountTransactionObservableList.size()+" Transactions" );
                paramenters_return_loan.put("totalLoan", "Total Returned Loan: "+ String.format("N %,.2f", Double.parseDouble(ManageLoanTransaction.getTotalReturnedLoanTransactions(accountNumberDisplay.getText()).toString())));
                paramenters_return_loan.put("logo", image);
                JasperReport jasperReport = null, jasperReport_return_loan = null;
                try {
                    jasperReport = JasperCompileManager.compileReport(reportStream);
                    jasperReport_return_loan = JasperCompileManager.compileReport(reportStream_return_loan);
                } catch (JRException e) {
                    logger.trace(e);
                }

                JasperPrint jasperPrint = null, jasperPrint_ReturnLoan = null;
                try {
                    JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(accountTransactionObservableList);
                    jasperPrint = JasperFillManager.fillReport(jasperReport, paramenters, beanCollectionDataSource);
                    JRBeanCollectionDataSource beanCollectionDataSource_Return_Loan = new JRBeanCollectionDataSource(returnLoanTransactionObservableList);
                    jasperPrint_ReturnLoan = JasperFillManager.fillReport(jasperReport_return_loan, paramenters_return_loan, beanCollectionDataSource_Return_Loan);
                } catch (JRException e) {
                    e.printStackTrace();
                }

                jrViewer = new JRViewer(jasperPrint);
                jrViewer_Return = new JRViewer(jasperPrint_ReturnLoan);

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

                        JFrame printReturn = new JFrame("PrintOut");

                        printReturn.add(jrViewer_Return);
                        printReturn.setSize(900, 900);
                        printReturn.setVisible(true);
                        printReturn.setLocationRelativeTo(null);
//            printReturn.setIconImage();
                        printReturn.toFront();
                        printReturn.setAlwaysOnTop(true);
                        printReturn.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
    public void setFilterReturnLoanTransactionList(KeyEvent keyEvent) throws Exception {

        this.observeTakenLoanTransactionListData = ManageLoanTransaction.getTakenLoanTransactionsForAccount(accountNumberDisplay.getText());

        returnLoanTransactionfilteredList = new FilteredList<ReturnLoanTransaction>(observeReturnedLoanTransactionListData, p->true);
        filterWithdrawalList.textProperty().addListener((observable, oldValue, newValue) -> {
            returnLoanTransactionfilteredList.setPredicate(filteredParam -> {

                if(newValue == null || newValue.isEmpty()){return true;}

                String typedText = newValue.toLowerCase();

                if(filteredParam.getAccountNo().toLowerCase().indexOf(typedText) != -1){
                    return true;
                }

                if(filteredParam.getLedgerNo().toString().indexOf(typedText) != -1){
                    return true;
                }

                if(filteredParam.getCollectedAmount().toString().indexOf(typedText) != -1){
                    return true;
                }

                return false;
            });

            returnLoanTransactionSortedList = new SortedList<ReturnLoanTransaction>(returnLoanTransactionfilteredList);
            returnLoanTransactionSortedList.comparatorProperty().bind(tableViewReturnedLoans.comparatorProperty());
            tableViewReturnedLoans.setItems(returnLoanTransactionSortedList);
        });
    }

    @FXML
    public void collectedLoanDateUpdated(Event inputMethodEvent) {
        pln("UPDATE EXPECTED DATE FIELD");

        LocalDate selectedDate = nLoanCollectedDate.getValue();

        if(selectedDate == null){
            throw new NullPointerException("YOU MUST SELECT DATE");
        }

        Integer nLPeriodVal = new Integer(nLPeriod.getText());

        LocalDate expectedDate = selectedDate.plusDays(nLPeriodVal);

        nLoanRepayDate.setValue(expectedDate);
        nLoanExpectedRepayDateLabelDisplay.setText(CustomUtility.getStringFromLocalDate(expectedDate));

    }

    @FXML
    public void returnTakenLoansAction(MouseEvent mouseEvent) {

        if(mouseEvent.getClickCount() == 2) {
            currentTableLoanTransaction = this.tableViewTakeLoans.getSelectionModel().getSelectedItem();
            if(currentTableLoanTransaction.getStatus() == ManageLoanTransaction.TAKEN_LOAN_RETURNED){
                CustomUtility.AlertHelper("ERROR: RESTRICTED!: ", "Can't Proceed Processing This Loan.", "This LOAN Has Already Been Returned!", "I").show();
                return;
            }
            showUpdateView();
        }
    }

    //TODO _COMPLETE THIS
    private void showUpdateView() {

        rLoanAmount.setText(currentTableLoanTransaction.getAmount().toString());
        rLoanLedgerNo.setText(currentTableLoanTransaction.getLedgerNo());
        rLoanAccountNo.setText(currentTableLoanTransaction.getAccountNo());
        rLoanInterestRate.setText(String.valueOf(15));
        rLoanTotalAmountExpected.setText("YET TO BE ACERTAINED");
        rLoanAddButton.setDisable(true);
        rLoanDescription.setText("Loan Returned By Member");
        rLoanCollectedDate.setValue(CustomUtility.getLocalDateFromDate(currentTableLoanTransaction.getDateCollected()));
        rLoanCollectedDateLabelDisplay.setText(CustomUtility.getStringFromDate(currentTableLoanTransaction.getDateCollected()));
//        rLoanCollectedDate.setValue(CustomUtility.getLocalDateFromDate(currentTableLoanTransaction.getDateCollected()));
        tabpaneActionSection.getSelectionModel().select(1);
        tabpaneViewSection.getSelectionModel().select(1);

    }

    @FXML
    public void onSelectReturnPaidLoanDate(ActionEvent event) {
        CustomUtility.AlertHelperTest();
        if(rLoanReturnedDate.getValue() != null){
            int loanPeriod = CustomUtility.daysDiff(CustomUtility.getLocalDateFromDate(currentTableLoanTransaction.getDateCollected()), rLoanReturnedDate.getValue());
            rLoanPeriod.setText(loanPeriod+"");
            rLoanInterestRate.setText(String.valueOf(ManageLoanTransaction.getPercentage(loanPeriod)));
            BigDecimal interestAmount = ManageLoanTransaction.getIncuredInterest(
                    currentTableLoanTransaction.getAmount(), loanPeriod );
            rAccuredInterestAmount.setText("" + interestAmount );
//            rLoanCollectedDateLabelDisplay.setText("" + interestAmount );
            rLoanAddButton.setDisable(false);
            rLoanTotalAmountExpected.setText(interestAmount.add(currentTableLoanTransaction.getAmount()).toString());
        }else{
            rLoanAddButton.setDisable(true);
        }
    }


}
