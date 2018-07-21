package major;


import com.smattme.MysqlExportService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.table.TableFilter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;

import static major.CustomUtility.getLocalDateFromDate;
import static major.CustomUtility.println;
import static major.ManageSharesTansaction.ALL_SHARES;

/**
 * @author: Chinaka Light
 * Created On: 6/20/18.
 * Purpose: To Handle the Main View Of This Application
 * MainViewDashboard Controller
 *
 * Controls the main application screen */
public class MainViewDashboardController implements Initializable {
    @FXML private Button logoutButton;
    @FXML private Button naAddButton;

    @FXML private TextField naFirstName;
    @FXML private TextField naLastName;
    @FXML private TextField naOtherName;
    @FXML private TextField naPhoneNo;
    @FXML private TextArea naAddress;
    @FXML private TextField naAccountNo;
    @FXML private DatePicker naOpenDate;

    @FXML private TabPane mainAppTabPane;

    @FXML private Button buttonBackupData;
    @FXML private Label backupProcessDisplayLabel;


    @FXML private Tab accountListTabSection;
    @FXML private Tab addAccountTabSection;
    @FXML private TabPane accountMgmtSection;


    @FXML private TableView<TableMemberAccount> tableViewAccountList;

    @FXML private TableColumn<TableMemberAccount, String> raclId;
    @FXML private TableColumn<TableMemberAccount, String> raclFirstName;
    @FXML private TableColumn<TableMemberAccount, String> raclLastName;
    @FXML private TableColumn<TableMemberAccount, String> raclOtherName;
    @FXML private TableColumn<TableMemberAccount, String> raclPhoneNo;
    @FXML private TableColumn<TableMemberAccount, String> raclAccountNo;
    @FXML private TableColumn<TableMemberAccount, String> raclOpenDate;
    @FXML private TableColumn<TableMemberAccount, String> raclCloseDate;
    @FXML private TableColumn<TableMemberAccount, String> raclStatus;
    @FXML private TableColumn<TableMemberAccount, String> raclAction;
    @FXML private TableColumn<TableMemberAccount, String> raclAddress;

    private FilteredList<TableMemberAccount> filteredList;

    private ObservableList<TableMemberAccount> observeMemberAccountListData = FXCollections.observableArrayList();

    SessionFactory sessionFactory = CustomUtility.getSessionFactory();
    Session session = sessionFactory.openSession();

    private AppLoginManager loginManager;


    private TableMemberAccount currentTableMemberAccount;
    private MembershipAccount currentMembershipAccount;
    public  boolean outcome_task = false;



    @FXML
    private Tab addAccountTabSection1;

    @FXML
    private TextField naFirstName1;

    @FXML
    private TextField naLastName1;

    @FXML
    private TextField naOtherName1;

    @FXML
    private TextField naPhoneNo1;

    @FXML
    private TextArea naAddress1;

    @FXML
    private TextField naAccountNo1;

    @FXML
    private DatePicker naOpenDate1;

    @FXML
    private Button updateAccountButton;

    @FXML
    private ComboBox statusSelect1;

    @FXML
    private ComboBox statusSelect;

    @FXML
    private Button updateCloseAccountButton;


    @FXML
    private Button buttonPrintAccountList;


    @FXML
    private TextField searchTextFieldAccountNumber;

    // SHARES TABLE SECTION
    @FXML Label displayMonthShareTotalLabel;
    private FilteredList<SharesTransaction> sharesAccountFilterList;
    @FXML
    private TextField filterAllShareDisplayListTextField;
    @FXML Label filteredSharedSumLabel;
    private SortedList<SharesTransaction> sharesSortedList;
    @FXML Button buttonAddSharesTrigger;
    @FXML
    private DatePicker shareMonthDatePicker;
    @FXML
    private DatePicker sharesDistributedLocalDate;
    @FXML private TextField sharesProfitAmountTextField;
    @FXML private TableView<SharesTransaction> shareslisttableview;
    @FXML private TableView<SharesDistributionTransaction> sharedMonthlyAmountTableView;
    private ObservableList<SharesTransaction> observeSharesTransactionSpecifiedAccountListData = FXCollections.observableArrayList();
    private ObservableList<SharesDistributionTransaction> observeSharesDistributedTransactionListData = FXCollections.observableArrayList();
    private BigDecimal sharesMonthTotalAmount = BigDecimal.ZERO;

    private static int ALL_SHARES_CAT = 4;
    private static int ALL_PENDING_SHARES_CAT = 1;
    private static int ALL_REWARDED_SHARES_CAT = 2;
    //END OF SHARES TABLE SECTION

    // TAKE LOAN SECTION TABLE
    @FXML
    private DatePicker viewMonthLoanDatePicker;
    @FXML
    private TableView<TakeLoanTransaction> loanTakenListTableView;
    private SortedList<TakeLoanTransaction> takeLoanSortedList;
    private ObservableList<TakeLoanTransaction> observeTakeLoanTransactionSpecifiedAccountListData = FXCollections.observableArrayList();
    private FilteredList<TakeLoanTransaction> takeLoanAccountFilterList;
    @FXML Label totalMonthTakenLoanAmountLabel;
    @FXML Label totalExpectedMonthReturnLoanAmountLabel;
    @FXML Label totalFilteredMonthTakenLoanAmountLabel;
    @FXML private TextField filterTakenLoanTextField;
    // END TAKE LOAN SECTION TABLE

    // RETURN LOAN SECTION TABLE
    @FXML
    private TableView<ReturnLoanTransaction> loanReturnListTableView;
    private SortedList<ReturnLoanTransaction> returnLoanSortedList;
    private ObservableList<ReturnLoanTransaction> observeReturnLoanTransactionSpecifiedAccountListData = FXCollections.observableArrayList();
    private FilteredList<ReturnLoanTransaction> returnLoanAccountFilterList;
    @FXML Label totalMonthReturnedLoanAmountLabel;
    @FXML Label totalMonthReturnedLoanProfitAmountLabel;
    @FXML Label totalFilteredMonthReturnLoanAmountLabel;
    @FXML private TextField filterReturnLoanTextField;
    // END RETURN LOAN SECTION TABLE


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initialize() {}

    public void initSessionID(final AppLoginManager loginManager, String sessionID) {
        this.loginManager = loginManager;
//        sessionLabel.setText(sessionID);
        logoutButton.setOnAction(event -> {loginManager.logout();});

        initiateAccountListTablePopulation();
    }


    @FXML
    public void addNewAccount(ActionEvent actionEvent) {

        String firstName = naFirstName.getText();
        String lastName = naLastName.getText();
        String otherName = naOtherName.getText();
        String phoneNo = naPhoneNo.getText();
        String address = naAddress.getText();
        LocalDate openDate = naOpenDate.getValue();

        if(
                firstName.equals(null) ||
                firstName.trim() == "" ||
                lastName.equals(null) ||
                lastName.trim() == "" ||
                phoneNo.equals(null) ||
                phoneNo.trim() == "" ||
                address.equals(null) ||
                address.trim() == "" ||
                openDate == null ||
                openDate.toString().trim() == ""

                ){
                CustomUtility.AlertHelper("New Account Creation Information", "Error: Account Creation Information",
                        "Please The Form Fields Are Required", Alert.AlertType.INFORMATION).show();
            return;
        }

        if(otherName.equals(null) ||
                otherName.trim() == ""){
                    otherName = " ";
        }

        naAddButton.setDisable(true);

        Task<MembershipAccount> task = new Task<MembershipAccount>() {
            String otherName = naOtherName.getText().toString().equals(null)? " ": naOtherName.getText();
            @Override protected MembershipAccount call() throws Exception {

                AccountNumberGenerator accountNumberGenerator = new AccountNumberGenerator();
                SessionFactory sessionFactory = CustomUtility.getSessionFactory();
                Session session = sessionFactory.openSession();
                Transaction transaction = session.beginTransaction();
                MembershipAccount membA = new MembershipAccount();
                membA.setAccountNo(accountNumberGenerator.getNewAccountNo(10));
                membA.setFirstName(firstName);
                membA.setLastName(lastName);
                membA.setOtherName(otherName);
                membA.setAddress(address);
                membA.setClosing_date(new Date());
                membA.setOpening_date(CustomUtility.getDateFromLocalDate(openDate));
                membA.setPhoneNo(phoneNo);
                membA.setStatus(statusSelect.getSelectionModel().getSelectedItem().equals("Active")? 1 : 0);
                session.save(membA);
                transaction.commit();

                println("Successfully inserted");

                boolean outcome = true;

                updateMessageA(membA);

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

    private void updateMessageA(MembershipAccount outcome) {

        if(outcome != null){
            CustomUtility.AlertHelper("Successfully Created", "Successfully Created",
                    "Successfully Created", Alert.AlertType.INFORMATION).show();
            clearAllButton();
            naAddButton.setDisable(false);
            initiateAccountListTablePopulation();
        }else{
            CustomUtility.AlertHelper("Error Creating New Account", "New Account was UnSuccessfully",
                    "New Account was Not Successfully Created", Alert.AlertType.ERROR).show();
            naAddButton.setDisable(false);
        }

    }

    public void clearAllButton(){
        this.naOpenDate.setValue(null);
        this.naFirstName.setText("");
        this.naLastName.setText("");
        this.naOtherName.setText("");
        this.naPhoneNo.setText("");
        this.naAddress.setText("");
        this.naAccountNo.setText("");
    }

    public void updateTableAccountListDisplay(){

        Task<Boolean> task = new Task<Boolean>() {

            @Override protected Boolean call() throws Exception {
                observeMemberAccountListData.clear();
                boolean outcome;

                observeMemberAccountListData = getAccountList();

                tableViewAccountList.setItems(observeMemberAccountListData);

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


    public ObservableList<TableMemberAccount> getAccountList(){

        SessionFactory sessionFactory = CustomUtility.getSessionFactory();
        Session session = sessionFactory.openSession();

        ObservableList<TableMemberAccount> currentObserveMemberAccountDataList = FXCollections.observableArrayList();

        Transaction transactionA = session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<MembershipAccount> query = builder.createQuery(MembershipAccount.class);
        query.from(MembershipAccount.class);

        //get this information from the database
        List<MembershipAccount> memberAccountList = session.createQuery(query).getResultList();

        TableMemberAccount tempTableMemberAccount;


        for (MembershipAccount memberAccount:
                memberAccountList) {

            tempTableMemberAccount = new TableMemberAccount();
            tempTableMemberAccount.setId(memberAccount.getId());
            tempTableMemberAccount.setFirstName(memberAccount.getFirstName());
            tempTableMemberAccount.setLastName(memberAccount.getLastName());
            tempTableMemberAccount.setOtherName(memberAccount.getOtherName());
            tempTableMemberAccount.setPhoneNo(memberAccount.getPhoneNo());
            tempTableMemberAccount.setAccountNo(memberAccount.getAccountNo());
            tempTableMemberAccount.setAddress(memberAccount.getAddress());
            tempTableMemberAccount.setOpening_date(memberAccount.getOpening_date().toString());
            tempTableMemberAccount.setClosing_date(memberAccount.getClosing_date().toString());

            if(memberAccount.getStatus() == 1){
                tempTableMemberAccount.setStatus("Active");
            }else{
                tempTableMemberAccount.setStatus("InActive");
            }

            currentObserveMemberAccountDataList.add(tempTableMemberAccount);

        }

        transactionA.commit();

        return currentObserveMemberAccountDataList;
    }

    public void initiateAccountListTablePopulation()
    {
        accountListTabSection.setDisable(true);
        addAccountTabSection1.setDisable(true);
        updateCloseAccountButton.setVisible(false);

        observeMemberAccountListData.clear();
        raclId = new TableColumn("ID");
        raclAccountNo = new TableColumn("Account No");
        raclFirstName = new TableColumn("First Name");
        raclLastName = new TableColumn("Last Name");
        raclOtherName = new TableColumn("Other Name");
        raclPhoneNo = new TableColumn("Phone No");
        raclStatus = new TableColumn("Status");
        raclOpenDate = new TableColumn("OpenDate");
        raclCloseDate = new TableColumn("CloseDate");
        raclAddress = new TableColumn("Address");

        println("After - ADDING LISTING ACCOUNT");

        raclId.setCellValueFactory(new PropertyValueFactory<TableMemberAccount, String>("Id"));
//        raclId.setVisible(false);
        raclAccountNo.setCellValueFactory(new PropertyValueFactory<TableMemberAccount, String>("accountNo"));
        raclFirstName.setCellValueFactory(new PropertyValueFactory<TableMemberAccount, String>("firstName"));
        raclLastName.setCellValueFactory(new PropertyValueFactory<TableMemberAccount, String>("lastName"));
        raclOtherName.setCellValueFactory(new PropertyValueFactory<TableMemberAccount, String>("otherName"));
        raclPhoneNo.setCellValueFactory(new PropertyValueFactory<TableMemberAccount, String>("phoneNo"));
        raclAddress.setCellValueFactory(new PropertyValueFactory<TableMemberAccount, String>("address"));
        raclStatus.setCellValueFactory(new PropertyValueFactory<TableMemberAccount, String>("status"));
        raclOpenDate.setCellValueFactory(new PropertyValueFactory<TableMemberAccount, String>("opening_date"));
        raclCloseDate.setCellValueFactory(new PropertyValueFactory<TableMemberAccount, String>("closing_date"));
        raclCloseDate.setVisible(false);

        tableViewAccountList.getColumns().setAll(raclId, raclAccountNo, raclFirstName,
                raclLastName, raclOtherName, raclPhoneNo, raclAddress,
                raclStatus, raclOpenDate, raclCloseDate);

        tableViewAccountList.setTooltip(new Tooltip("Click On Any Of the Account List To Update Information"));

        tableViewAccountList.setItems(observeMemberAccountListData);


        statusSelect1.getItems().addAll(
                "Active",
                        "InActive");

        statusSelect.getItems().addAll(
                "Active",
                "InActive");

        statusSelect.getSelectionModel().select("Active");

        statusSelect1.getSelectionModel().select("InActive");

        updateTableAccountListDisplay();

        tableViewAccountList.setOnMouseClicked(event -> {

            if(event.getClickCount() == 2) {
                currentTableMemberAccount = tableViewAccountList.getSelectionModel().getSelectedItem();
                currentMembershipAccount = ManageMembershipAccount.getMemberAccountFromTableMemberAccount(currentTableMemberAccount);
                showUpdateView();
            }
        });

    }

    public void showUpdateView(){

        if(this.currentMembershipAccount == null || this.currentTableMemberAccount == null) return;

        accountMgmtSection.getSelectionModel().select(addAccountTabSection1);
        addAccountTabSection.setDisable(true);
        accountListTabSection.setDisable(true);
        addAccountTabSection1.setDisable(false);

        naFirstName1.setText(currentMembershipAccount.getFirstName());
        naLastName1.setText(currentMembershipAccount.getLastName());
        naOtherName1.setText(currentMembershipAccount.getOtherName());
        naPhoneNo1.setText(currentMembershipAccount.getPhoneNo());
        naAddress1.setText(currentMembershipAccount.getAddress());
        naAccountNo1.setText(currentMembershipAccount.getAccountNo());
        LocalDate localDateOpening = CustomUtility.getLocalDateFromDate(currentMembershipAccount.getOpening_date());
        naOpenDate1.setValue(localDateOpening);
        naOpenDate1.setEditable(false);
        statusSelect1.getSelectionModel().select(currentMembershipAccount.getStatus()==1? "Active" : "InActive");

        //Button To Update the Record
        updateAccountButton.setOnAction(event -> {

            String firstName = naFirstName1.getText();
            String lastName = naLastName1.getText();
            String otherName = naOtherName1.getText();
            String phoneNo = naPhoneNo1.getText();
            String address = naAddress1.getText();
            LocalDate openDate = naOpenDate1.getValue();

            if(
                    firstName.equals(null) ||
                            firstName.trim() == "" ||
                            lastName.equals(null) ||
                            lastName.trim() == "" ||
                            phoneNo.equals(null) ||
                            phoneNo.trim() == "" ||
                            address.equals(null) ||
                            address.trim() == "" ||
                            openDate == null ||
                            openDate.toString().trim() == ""

                    ){
                CustomUtility.AlertHelper("Update Account Information", "Error: Account Update Information",
                        "Please The Form Fields Are Required", Alert.AlertType.INFORMATION).show();
                return;
            }

            Session session = sessionFactory.openSession(); //sessionFactory.getCurrentSession();
            Transaction transactionA = session.beginTransaction();
            currentMembershipAccount.setAddress(address);
            currentMembershipAccount.setFirstName(firstName);
            currentMembershipAccount.setLastName(lastName);
            currentMembershipAccount.setOtherName(otherName);
            currentMembershipAccount.setAccountNo(naAccountNo1.getText());
            currentMembershipAccount.setPhoneNo(phoneNo);
            currentMembershipAccount.setStatus(statusSelect1.getSelectionModel().getSelectedItem()=="Active"? 1 : 0);
            session.saveOrUpdate(currentMembershipAccount);
            transactionA.commit();

            CustomUtility.AlertHelper("Update Account Information", "Account Update Information",
                    "Account Has Been Updated Successfully", Alert.AlertType.INFORMATION).show();
            updateTableAccountListDisplay();

        });
    }

    public void clearUpdateView(){
        naFirstName1.setText("");
        naLastName1.setText("");
        naOtherName1.setText("");
        naPhoneNo1.setText("");
        naAddress1.setText("");
        naAccountNo1.setText("");
        naOpenDate1.setValue(null);
    }

    public static void main(String[] args) {}

    public void accountListTabTrigger(ActionEvent actionEvent) {
        addAccountTabSection.setDisable(true);
        accountListTabSection.setDisable(false);
        addAccountTabSection1.setDisable(true);
        accountMgmtSection.getSelectionModel().select(accountListTabSection);
    }

    public void addAccountTabTrigger(ActionEvent actionEvent) {
        addAccountTabSection.setDisable(false);
        addAccountTabSection1.setDisable(true);
        accountListTabSection.setDisable(true);
        accountMgmtSection.getSelectionModel().select(addAccountTabSection);
    }

    public void viewRegisteredAccount(KeyEvent keyEvent) {
        CustomUtility.println(tableViewAccountList.getSelectionModel().getTableView().getItems().getClass().getName());
    }


    @FXML
    public void searchRecordsHere(KeyEvent keyEvent) {
        filteredList = new FilteredList<TableMemberAccount>(observeMemberAccountListData, p->true);
        searchTextFieldAccountNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(pere -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String typedText = newValue.toLowerCase();

                if(pere.getAccountNo().toLowerCase().indexOf(typedText) != -1){
                    return true;
                }

                if(pere.getFirstName().toLowerCase().indexOf(typedText) != -1){
                    return true;
                }

                if(pere.getLastName().toLowerCase().indexOf(typedText) != -1){
                    return true;
                }

                if(pere.getOtherName().toLowerCase().indexOf(typedText) != -1){
                    return true;
                }

                if(pere.getPhoneNo().toLowerCase().indexOf(typedText) != -1){
                    return true;
                }

                return false;
            });

            SortedList<TableMemberAccount> sortedList = new SortedList<TableMemberAccount>(filteredList);
            sortedList.comparatorProperty().bind(tableViewAccountList.comparatorProperty());
            tableViewAccountList.setItems(sortedList);
        });
    }


    @FXML
    public void makeDepositAction(ActionEvent actionEvent) throws Exception {

        if(this.currentTableMemberAccount==null  || this.currentMembershipAccount == null) return;

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("MakeDepositView.fxml")
        );

        Scene scene = new Scene(loader.load(), 1000, 950);

        Stage makeDepositScene = new Stage();

        makeDepositScene.setScene(scene);
        makeDepositScene.setTitle("Make & View Deposits As Well As Initiate And Track Withdrawals");
        makeDepositScene.initModality(Modality.APPLICATION_MODAL);

        makeDepositScene.setMaximized(true);

        makeDepositScene.setOnCloseRequest(e->{
            e.consume();
        });



        Label fullNameDisplay = (Label)loader.getNamespace().get("fullnameDisplay");
        Label accountNumberDisplay = (Label)loader.getNamespace().get("accountNumberDisplay");
        Label  labelID = (Label)loader.getNamespace().get("labelID");
        Label totalLabelSumDeposit = (Label)loader.getNamespace().get("totalLabelSumDeposit");
        Label totalLabelSumWithdrawn = (Label)loader.getNamespace().get("totalLabelSumWithdrawn");
        Label accountBalance = (Label)loader.getNamespace().get("accountBalance");

        //use this to set the values displayed in the FXML file
        fullNameDisplay.setText(this.currentMembershipAccount.getFullName());
        accountNumberDisplay.setText(this.currentMembershipAccount.getAccountNo());
        // set but hidden
        labelID.setId(String.valueOf(this.currentMembershipAccount.getId()));


        // reference to the crediting/depositing table
        TableView<AccountTransaction> tableViewDeposits = (TableView) loader.getNamespace().get("tableViewDeposits");
        TableColumn<AccountTransaction, String> colID =  (TableColumn)loader.getNamespace().get("colID");
        TableColumn<AccountTransaction, String> colSN = (TableColumn)loader.getNamespace().get("colSN");
        TableColumn<AccountTransaction, BigDecimal> colAmount = (TableColumn)loader.getNamespace().get("colAmount");
        TableColumn<AccountTransaction, String> colType = (TableColumn)loader.getNamespace().get("colType");
        TableColumn<AccountTransaction, String> colComment = (TableColumn)loader.getNamespace().get("colComment");
        TableColumn<AccountTransaction, Date> colDate = (TableColumn)loader.getNamespace().get("colDate");
        TableColumn<AccountTransaction, String> colAccountNo = (TableColumn)loader.getNamespace().get("colAccountNo");

        colID.setCellValueFactory(new PropertyValueFactory<AccountTransaction, String>("Id"));
        colSN.setCellValueFactory(new PropertyValueFactory<AccountTransaction, String>("Id"));
        colType.setCellValueFactory(new PropertyValueFactory<AccountTransaction, String>("transaction_type"));
        colAmount.setCellValueFactory(new PropertyValueFactory<AccountTransaction, BigDecimal>("amount"));
        colComment.setCellValueFactory(new PropertyValueFactory<AccountTransaction, String>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<AccountTransaction, Date>("transaction_date"));
        colAccountNo.setCellValueFactory(new PropertyValueFactory<AccountTransaction, String>("accountNo"));

        colID.setVisible(false);
        colSN.setVisible(false);

        
        // reference to the debiting/withdrawals table
        TableView<AccountTransaction> tableViewWithdrawals = (TableView) loader.getNamespace().get("tableViewWithdrawals");
        TableColumn<AccountTransaction, String> colID1 =  (TableColumn)loader.getNamespace().get("colID1");
        TableColumn<AccountTransaction, String> colSN1= (TableColumn)loader.getNamespace().get("colSN1");
        TableColumn<AccountTransaction, BigDecimal> colAmount1= (TableColumn)loader.getNamespace().get("colAmount1");
        TableColumn<AccountTransaction, String> colType1 = (TableColumn)loader.getNamespace().get("colType1");
        TableColumn<AccountTransaction, String> colComment1 = (TableColumn)loader.getNamespace().get("colComment1");
        TableColumn<AccountTransaction, Date> colDate1 = (TableColumn)loader.getNamespace().get("colDate1");
        TableColumn<AccountTransaction, String> colAccountNo1 = (TableColumn)loader.getNamespace().get("colAccountNo1");



        colID1.setCellValueFactory(new PropertyValueFactory<AccountTransaction, String>("Id"));
        colSN1.setCellValueFactory(new PropertyValueFactory<AccountTransaction, String>("Id"));
        colType1.setCellValueFactory(new PropertyValueFactory<AccountTransaction, String>("transaction_type"));
        colAmount1.setCellValueFactory(new PropertyValueFactory<AccountTransaction, BigDecimal>("amount"));
        colComment1.setCellValueFactory(new PropertyValueFactory<AccountTransaction, String>("description"));
        colDate1.setCellValueFactory(new PropertyValueFactory<AccountTransaction, Date>("transaction_date"));
        colAccountNo1.setCellValueFactory(new PropertyValueFactory<AccountTransaction, String>("accountNo"));

        colID1.setVisible(false);
        colSN1.setVisible(false);

        tableViewDeposits.getColumns().setAll(colID, colSN, colAmount, colType, colComment, colDate);
        tableViewWithdrawals.getColumns().setAll(colID1, colSN1, colAmount1, colType1, colComment1, colDate1);
        
        tableViewDeposits.setItems(ManageAccountTansaction.getCreditTransactions(this.currentMembershipAccount.getAccountNo()));

        tableViewWithdrawals.setItems(ManageAccountTansaction.getDebitTransactions(this.currentMembershipAccount.getAccountNo()));


        totalLabelSumDeposit.setText(String.format("%s%.3f","TOTAL DEPOSITED SUM IS: ", ManageAccountTansaction.getTotalCredited(this.currentMembershipAccount.getAccountNo()).doubleValue()));
        totalLabelSumWithdrawn.setText(String.format("%s%.3f","TOTAL WITHDRAWN SUM IS: ", ManageAccountTansaction.getTotalDebited(this.currentMembershipAccount.getAccountNo()).doubleValue()));

        accountBalance.setText(String.format("%s%.3f","", ManageAccountTansaction.getAccountBalance(this.currentMembershipAccount.getAccountNo()).doubleValue()));

        Button closeButton = (Button)loader.getNamespace().get("closeButton");

        closeButton.setOnAction(event -> {
            makeDepositScene.close();
        });

        makeDepositScene.showAndWait();

    }

    public void buySellSharesAction(ActionEvent actionEvent) throws Exception {
        if(this.currentTableMemberAccount==null  || this.currentMembershipAccount == null) return;

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("BuySellSharesView.fxml")
        );

        Scene scene = new Scene(loader.load(), 1000, 950);

        Stage buySellSharesScene = new Stage();

        buySellSharesScene.setScene(scene);
        buySellSharesScene.setTitle("Make Deposit");
        buySellSharesScene.initModality(Modality.APPLICATION_MODAL);

        buySellSharesScene.setMaximized(true);

        buySellSharesScene.setOnCloseRequest(e->{
            e.consume();
        });



        Label fullNameDisplay = (Label)loader.getNamespace().get("fullnameDisplay");
        Label accountNumberDisplay = (Label)loader.getNamespace().get("accountNumberDisplay");
        Label  labelID = (Label)loader.getNamespace().get("labelID");
        Label totalLabelSumDeposit = (Label)loader.getNamespace().get("totalLabelSumDeposit");
        Label totalLabelSumWithdrawn = (Label)loader.getNamespace().get("totalLabelSumWithdrawn");
        Label accountBalance = (Label)loader.getNamespace().get("accountBalance");
        Label shareBalance = (Label)loader.getNamespace().get("accountBalance1");

        //use this to set the values displayed in the FXML file
        fullNameDisplay.setText(this.currentMembershipAccount.getFullName());
        accountNumberDisplay.setText(this.currentMembershipAccount.getAccountNo());
        // set but hidden
        labelID.setId(String.valueOf(this.currentMembershipAccount.getId()));


        // reference to the crediting/depositing table
        TableView<SharesTransaction> tableViewDeposits = (TableView) loader.getNamespace().get("tableViewDeposits");
        TableColumn<SharesTransaction, String> colID =  (TableColumn)loader.getNamespace().get("colID");
        TableColumn<SharesTransaction, String> colSN = (TableColumn)loader.getNamespace().get("colSN");
        TableColumn<SharesTransaction, BigDecimal> colAmount = (TableColumn)loader.getNamespace().get("colAmount");
        TableColumn<SharesTransaction, String> colType = (TableColumn)loader.getNamespace().get("colType");
        TableColumn<SharesTransaction, String> colComment = (TableColumn)loader.getNamespace().get("colComment");
        TableColumn<SharesTransaction, Date> colDate = (TableColumn)loader.getNamespace().get("colDate");
        TableColumn<SharesTransaction, String> colAccountNo = (TableColumn)loader.getNamespace().get("colAccountNo");

        colID.setCellValueFactory(new PropertyValueFactory<SharesTransaction, String>("Id"));
        colSN.setCellValueFactory(new PropertyValueFactory<SharesTransaction, String>("Id"));
        colType.setCellValueFactory(new PropertyValueFactory<SharesTransaction, String>("transaction_type"));
        colAmount.setCellValueFactory(new PropertyValueFactory<SharesTransaction, BigDecimal>("amount"));
        colComment.setCellValueFactory(new PropertyValueFactory<SharesTransaction, String>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<SharesTransaction, Date>("transaction_date"));
        colAccountNo.setCellValueFactory(new PropertyValueFactory<SharesTransaction, String>("accountNo"));

        colID.setVisible(false);
        colSN.setVisible(false);


        // reference to the debiting/withdrawals table
        TableView<SharesTransaction> tableViewWithdrawals = (TableView) loader.getNamespace().get("tableViewWithdrawals");
        TableColumn<SharesTransaction, String> colID1 =  (TableColumn)loader.getNamespace().get("colID1");
        TableColumn<SharesTransaction, String> colSN1= (TableColumn)loader.getNamespace().get("colSN1");
        TableColumn<SharesTransaction, BigDecimal> colAmount1= (TableColumn)loader.getNamespace().get("colAmount1");
        TableColumn<SharesTransaction, String> colType1 = (TableColumn)loader.getNamespace().get("colType1");
        TableColumn<SharesTransaction, String> colComment1 = (TableColumn)loader.getNamespace().get("colComment1");
        TableColumn<SharesTransaction, Date> colDate1 = (TableColumn)loader.getNamespace().get("colDate1");
        TableColumn<SharesTransaction, String> colAccountNo1 = (TableColumn)loader.getNamespace().get("colAccountNo1");


        colID1.setCellValueFactory(new PropertyValueFactory<SharesTransaction, String>("Id"));
        colSN1.setCellValueFactory(new PropertyValueFactory<SharesTransaction, String>("Id"));
        colType1.setCellValueFactory(new PropertyValueFactory<SharesTransaction, String>("transaction_type"));
        colAmount1.setCellValueFactory(new PropertyValueFactory<SharesTransaction, BigDecimal>("amount"));
        colComment1.setCellValueFactory(new PropertyValueFactory<SharesTransaction, String>("description"));
        colDate1.setCellValueFactory(new PropertyValueFactory<SharesTransaction, Date>("transaction_date"));
        colAccountNo1.setCellValueFactory(new PropertyValueFactory<SharesTransaction, String>("accountNo"));

        colID1.setVisible(false);
        colSN1.setVisible(false);

        tableViewDeposits.getColumns().setAll(colID, colSN, colAmount, colType, colComment, colDate);
        tableViewWithdrawals.getColumns().setAll(colID1, colSN1, colAmount1, colType1, colComment1, colDate1);

        tableViewDeposits.setItems(ManageSharesTansaction.getCreditTransactions(this.currentMembershipAccount.getAccountNo()));

        tableViewWithdrawals.setItems(ManageSharesTansaction.getSharesSellsTransactions(this.currentMembershipAccount.getAccountNo()));


        totalLabelSumDeposit.setText(String.format("%s%.3f","TOTAL SHARES BOUGHT SUM IS: ", ManageSharesTansaction.getTotalCredited(this.currentMembershipAccount.getAccountNo()).doubleValue()));
        totalLabelSumWithdrawn.setText(String.format("%s%.3f","TOTAL SHARES SOLD IS: ", ManageSharesTansaction.getTotalDebited(this.currentMembershipAccount.getAccountNo()).doubleValue()));

        accountBalance.setText(String.format("%s%.3f","", ManageAccountTansaction.getAccountBalance(this.currentMembershipAccount.getAccountNo()).doubleValue()));
        shareBalance.setText(String.format("%s%.3f","", ManageSharesTansaction.getSharesBalance(this.currentMembershipAccount.getAccountNo()).doubleValue()));

        Button closeButton = (Button)loader.getNamespace().get("closeButton");

        closeButton.setOnAction(event -> {
            buySellSharesScene.close();
        });

        buySellSharesScene.showAndWait();
    }

    @FXML
    public void manageLoanAction(ActionEvent actionEvent) throws Exception {
        if(this.currentTableMemberAccount==null  || this.currentMembershipAccount == null) return;

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("TakeLoanGiveLoan.fxml")
        );

        Scene scene = new Scene(loader.load(), 1000, 950);

        Stage takeAndReturnLoanScene = new Stage();

        takeAndReturnLoanScene.setScene(scene);
        takeAndReturnLoanScene.setTitle("Take Loan Or Return Loan");
        takeAndReturnLoanScene.initModality(Modality.APPLICATION_MODAL);

        takeAndReturnLoanScene.setMaximized(true);

        takeAndReturnLoanScene.setOnCloseRequest(e->{
            e.consume();
        });


        Label fullNameDisplay = (Label)loader.getNamespace().get("fullnameDisplay");
        Label accountNumberDisplay = (Label)loader.getNamespace().get("accountNumberDisplay");
        Label labelID = (Label)loader.getNamespace().get("labelID");
        Label totalLoanTakenLabel = (Label)loader.getNamespace().get("totalLoanTakenLabel");
        Label totalLoanReturnedLabel = (Label)loader.getNamespace().get("totalLoanReturnedLabel");
        Label accountBalance = (Label)loader.getNamespace().get("accountBalance");
        Label totalCollectedLoanBalance = (Label)loader.getNamespace().get("totalCollectedLoanBalance");

        TextField nLoanLedgerNo = (TextField)loader.getNamespace().get("nLoanLedgerNo");
        TextField nLoanAccountNo = (TextField)loader.getNamespace().get("nLoanAccountNo");


        //use this to set the values displayed in the FXML file
        fullNameDisplay.setText(this.currentMembershipAccount.getFullName());
        accountNumberDisplay.setText(this.currentMembershipAccount.getAccountNo());
        nLoanAccountNo.setText(this.currentMembershipAccount.getAccountNo());
        // set but hidden
        labelID.setId(String.valueOf(this.currentMembershipAccount.getId()));


        String uLegNo = CustomUtility.getUniqueLedgerNoString();
        // set unique ledger no
        nLoanLedgerNo.setText(uLegNo);

        // reference to the crediting/depositing table
        TableView<TakeLoanTransaction> tableViewTakeLoans = (TableView) loader.getNamespace().get("tableViewTakeLoans");
        TableColumn<TakeLoanTransaction, String> colId =  (TableColumn)loader.getNamespace().get("colId");
        TableColumn<TakeLoanTransaction, String> colSN = (TableColumn)loader.getNamespace().get("colSN");
        TableColumn<TakeLoanTransaction, String> colAccountNo = (TableColumn)loader.getNamespace().get("colAccountNo");
        TableColumn<TakeLoanTransaction, String> colLedgerNo = (TableColumn)loader.getNamespace().get("colLedgerNo");
        TableColumn<TakeLoanTransaction, BigDecimal> colLoanAmount = (TableColumn)loader.getNamespace().get("colLoanAmount");
        TableColumn<TakeLoanTransaction, Integer> colInterestRate = (TableColumn)loader.getNamespace().get("colInterestRate");
        TableColumn<TakeLoanTransaction, Integer> colLoanPeriod = (TableColumn)loader.getNamespace().get("colLoanPeriod");
        TableColumn<TakeLoanTransaction, Date> colCollectedDate = (TableColumn)loader.getNamespace().get("colCollectedDate");
        TableColumn<TakeLoanTransaction, Date> colRepayDate = (TableColumn)loader.getNamespace().get("colRepayDate");
        TableColumn<TakeLoanTransaction, String> colLoanDescription = (TableColumn)loader.getNamespace().get("colLoanDescription");


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

        // reference to the returned loan table
        TableView<ReturnLoanTransaction> tableViewReturnedLoans             = (TableView) loader.getNamespace().get("tableViewReturnedLoans");
        TableColumn<ReturnLoanTransaction, String>      colRLId             = (TableColumn)loader.getNamespace().get("colRLId");
        TableColumn<ReturnLoanTransaction, String>      colRLSn             = (TableColumn)loader.getNamespace().get("colRLSn");
        TableColumn<ReturnLoanTransaction, String>      colRLAccountNo      = (TableColumn)loader.getNamespace().get("colRLAccountNo");
        TableColumn<ReturnLoanTransaction, BigDecimal>  colRLLedgerNo       = (TableColumn)loader.getNamespace().get("colRLLedgerNo");
        TableColumn<ReturnLoanTransaction, String>      colRLLoanAmount     = (TableColumn)loader.getNamespace().get("colRLLoanAmount");
        TableColumn<ReturnLoanTransaction, String>      colRLInterestRate   = (TableColumn)loader.getNamespace().get("colRLInterestRate");
        TableColumn<ReturnLoanTransaction, Date>        colRLLoanPeriod     = (TableColumn)loader.getNamespace().get("colRLLoanPeriod");
        TableColumn<ReturnLoanTransaction, String>      colRLCollectedDate  = (TableColumn)loader.getNamespace().get("colRLCollectedDate");
        TableColumn<ReturnLoanTransaction, String>      colRLRepayDate      = (TableColumn)loader.getNamespace().get("colRLRepayDate");


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

        tableViewReturnedLoans.getColumns().setAll(colRLId, colRLSn, colRLAccountNo, colRLLedgerNo, colRLLoanAmount, colRLCollectedDate, colRLRepayDate, colRLLoanPeriod, colRLInterestRate);

        tableViewTakeLoans.setItems(ManageLoanTransaction.getTakenLoanTransactionsForAccount(this.currentMembershipAccount.getAccountNo()));

        tableViewReturnedLoans.setItems(ManageLoanTransaction.getReturnedLoanTransactionsForAccount(this.currentMembershipAccount.getAccountNo()));


        totalLoanTakenLabel.setText(String.format("%s%.3f","TOTAL LOAN TAKEN IS: ", ManageLoanTransaction.getTotalTakenLoanTransactions(this.currentMembershipAccount.getAccountNo()).doubleValue()));
        totalLoanReturnedLabel.setText(String.format("%s%.3f","TOTAL LOAN RETURNED IS: ", ManageLoanTransaction.getTotalReturnedLoanTransactions(this.currentMembershipAccount.getAccountNo()).doubleValue()));

        accountBalance.setText(String.format("%s%.3f","", ManageAccountTansaction.getAccountBalance(this.currentMembershipAccount.getAccountNo()).doubleValue()));
        totalCollectedLoanBalance.setText(String.format("%s%.3f","", ManageLoanTransaction.getCurrentLoanBalance(this.currentMembershipAccount.getAccountNo()).doubleValue()));

        Button closeButton = (Button)loader.getNamespace().get("closeButton");

        closeButton.setOnAction(event -> {
            takeAndReturnLoanScene.close();
        });

        takeAndReturnLoanScene.showAndWait();

    }

    public void showSharesTabAction(ActionEvent actionEvent) {

        mainAppTabPane.getSelectionModel().select(1);

    }

    public void goHomeMgmtAccountButtonAction(ActionEvent actionEvent) {
        mainAppTabPane.getSelectionModel().select(0);
    }

    public void buttonBackupDataAction(ActionEvent actionEvent) {
        //TODO complete this export Database CODE - NOT WORKING STILL BEING DEVELOPED

        buttonBackupData.setDisable(true);
        Task<Boolean> task = new Task<Boolean>() {

            @Override protected Boolean call() throws Exception {




                if(CustomUtility.netIsAvailable()) {

                    Thread.sleep(500);
                    //TODO  START A THREAD TASK THAT UPLOADS DATA TO THE INTERNET
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            backupProcessDisplayLabel.setText("Network Connection Detected");
                        }
                    });

                    Thread.sleep(100);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            backupProcessDisplayLabel.setText("BackUp Database Process Initiating...");
                        }
                    });


                    try {

                        Properties properties = new Properties();
                        properties.setProperty(MysqlExportService.DB_NAME, "sharesdb");
                        properties.setProperty(MysqlExportService.DB_USERNAME, "homestead");
                        properties.setProperty(MysqlExportService.DB_PASSWORD, "secret");

                        //properties relating to email config
                        properties.setProperty(MysqlExportService.EMAIL_HOST, "smtp.mailtrap.io");
                        properties.setProperty(MysqlExportService.EMAIL_PORT, "25");
                        properties.setProperty(MysqlExportService.EMAIL_USERNAME, "0c524f09a14ef6");
                        properties.setProperty(MysqlExportService.EMAIL_PASSWORD, "89e6f0d0ad4531");
                        properties.setProperty(MysqlExportService.EMAIL_FROM, "sharesapp@payer.ng");
                        properties.setProperty(MysqlExportService.EMAIL_TO, "light@payer.ng");

                        //set the outputs temp dir
                        File currentDirectory = new File(new File(".").getAbsolutePath());
                        String folderPath = currentDirectory.getCanonicalPath() + File.separator + "databaseBackUpFolder";

                        properties.setProperty(MysqlExportService.TEMP_DIR, new File(folderPath).getPath());

                        Thread.sleep(500);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                backupProcessDisplayLabel.setText("BackUp Database is in .Processing...");
                            }
                        });

                        MysqlExportService mysqlExportService = new MysqlExportService(properties);
                        Thread.sleep(500);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                backupProcessDisplayLabel.setText("Currently Uploading & Emailing Database Backup....");
                            }
                        });
                        mysqlExportService.export();
                        Thread.sleep(50);



                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                backupProcessDisplayLabel.setText("Error Occured While Backing Up the Database. Please Try Again");
                            }
                        });
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            backupProcessDisplayLabel.setText("DATABASE BACKUP WAS SUCCESSFULL");
                            CustomUtility.pln(String.format("%s: %s - %s", "UPLOAD REPORT", "DATABASE BACKUP PROCESS INFORMATION", "DATABASE BACKUP SUCCESSFULL"));
                            CustomUtility.AlertHelper("UPLOAD REPORT", "DATABASE BACKUP PROCESS INFORMATION", "DATABASE BACKUP SUCCESSFULL", "I").showAndWait();
                            backupProcessDisplayLabel.setText("DATABASE BACKUP COMPLETED SUCCESSFULLY AT " + (new Date()));

                            buttonBackupData.setDisable(false);
                            outcome_task = true;
                        }
                    });

                }else{

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            backupProcessDisplayLabel.setText("NO INTERNET CONNECTION - AVAILABLE CURRENTLY");
                            CustomUtility.AlertHelper("BACKUP PROCESSING INFORMATION", "DATABASE BACKUP - INFO", "NO INTERNET CONNECTION - AVAILABLE CURRENTLY", "I").showAndWait();
                            backupProcessDisplayLabel.setText("DATABASE BACKUP STATUS");
                            buttonBackupData.setDisable(false);
                            outcome_task = false;
                        }
                    });
                }
                return outcome_task;

            }

            @Override
            protected void done() {


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

        new Thread(task).start();

    }


    private void deleteFileIfExists(File dropAndCreateDdlFile) {
        if (dropAndCreateDdlFile.exists()) {
            if (!dropAndCreateDdlFile.isFile()) {
                String msg = MessageFormat.format("File is not a normal file {0}", dropAndCreateDdlFile);
                throw new IllegalStateException(msg);
            }

            if (!dropAndCreateDdlFile.delete()) {
                String msg = MessageFormat.format("Unable to delete file {0}", dropAndCreateDdlFile);
                throw new IllegalStateException(msg);
            }
        }
    }

    public void buttonAddSharesTriggerAction(ActionEvent actionEvent) throws Exception{
        //get the revenue
        BigDecimal revenueAmount;
        LocalDate localsharesDistributedLocalDate = sharesDistributedLocalDate.getValue() ;

        try {
            revenueAmount = new BigDecimal(sharesProfitAmountTextField.getText());

            if(revenueAmount.doubleValue() < sharesMonthTotalAmount.doubleValue()){
                CustomUtility.AlertHelper("ERROR Monthly Share Information", "ERROR IN Total Revenue", "Your Revenue Cannot Be Less Than Month Total Shares", "E").show();
                return;
            }

            if(localsharesDistributedLocalDate == null || localsharesDistributedLocalDate.isBefore(getLocalDateFromDate(this.observeSharesTransactionSpecifiedAccountListData.get(0).getTransaction_date()))){
                CustomUtility.AlertHelper("ERROR Monthly Share Information", "ERROR IN Shares Date", "Only Dates After Shares Were Bought. Not Before", "E").show();
                return;
            }

            BigDecimal profit = revenueAmount.subtract(sharesMonthTotalAmount);

            int ans = CustomUtility.ConfirmationWithOptionsAlertHelper("Shares Distribution Proceed Confirmation", "Are You Sure You Want To PROCEED? With Distributing the Profit Of "+ profit.setScale(2, RoundingMode.DOWN));

            if(ans == CustomUtility.OK_PLUS)
                ManageSharesTansaction.distributeSharesAmongThese(this.observeSharesTransactionSpecifiedAccountListData, sharesMonthTotalAmount, profit, localsharesDistributedLocalDate,  ManageSharesTansaction.AUTO_RENEW_SHARES);
            else if(ans == CustomUtility.OK)
                ManageSharesTansaction.distributeSharesAmongThese(this.observeSharesTransactionSpecifiedAccountListData, sharesMonthTotalAmount, profit, localsharesDistributedLocalDate, ManageSharesTansaction.MANUAL_RENEW_SHARES);


        }catch (NumberFormatException ex){
            ex.printStackTrace();
        }

        sharesDistributedLocalDate.setValue(null);
        sharesProfitAmountTextField.setText("");
    }

    public void buttonPrintSharesTriggerAction(ActionEvent actionEvent) {
    }

    public void getAllSharesAction(ActionEvent actionEvent) {

        prepareSharesDisplayTable();
        populateDisplayMonthlyDistributedShares();

        shareslisttableview.setItems(observeSharesTransactionSpecifiedAccountListData);

        observeSharesTransactionSpecifiedAccountListData.setAll(ManageSharesTansaction.getAllSharesTransactionList());
        String totalSum =String.format("%,.2f",ManageSharesTansaction.getTotal(ManageSharesTansaction.getAllSharesTransactionList(), ALL_SHARES).setScale(2, RoundingMode.DOWN));
        displayMonthShareTotalLabel.setText(totalSum);
        filteredSharedSumLabel.setText("FILTERED SUM: " +totalSum);
        filteredSharedSumLabel.setFont(Font.font("arial", FontWeight.EXTRA_BOLD,20 ));

    }

    public void buttonFindAllMonthlyLoanAction(ActionEvent actionEvent) {
        if(viewMonthLoanDatePicker.getValue() == null){
            CustomUtility.AlertHelper("Error Getting Monthly Loan", "Error Getting Loan Transaction:", "Please SELECT Loan Date Before Clicking This Button", "I").show();
            return;
        }
        LocalDate localDateMonth = viewMonthLoanDatePicker.getValue();

        prepareLoanDisplayTable();

        loanTakenListTableView.setItems(observeTakeLoanTransactionSpecifiedAccountListData);
        loanReturnListTableView.setItems(observeReturnLoanTransactionSpecifiedAccountListData);

        observeTakeLoanTransactionSpecifiedAccountListData.setAll(ManageLoanTransaction.getTakenLoanTransactionsForMonth(localDateMonth));

        observeReturnLoanTransactionSpecifiedAccountListData.setAll(ManageLoanTransaction.getReturnLoanTransactionsForMonth(localDateMonth));

        BigDecimal takenLoanMonthTotalAmount = ManageLoanTransaction.getTotalTakenLoan(ManageLoanTransaction.getTakenLoanTransactionsForMonth(localDateMonth), "COLLECTED_AMOUNT");
        BigDecimal returnedELoanMonthTotalAmount = ManageLoanTransaction.getTotalTakenLoan(ManageLoanTransaction.getTakenLoanTransactionsForMonth(localDateMonth), "EXPECTED_RETURNED_AMOUNT");
        BigDecimal returnedALoanMonthTotalAmount = ManageLoanTransaction.getTotalReturnLoan(ManageLoanTransaction.getReturnLoanTransactionsForMonth(localDateMonth), "ACTUAL_RETURNED_AMOUNT");

        BigDecimal returnedALoanProfitFromMonthTotalAmount = ManageLoanTransaction.getTotalReturnLoan(ManageLoanTransaction.getReturnLoanTransactionsForMonth(localDateMonth), "PROFIT_EARNED_FROM_RETURNED_AMOUNT");

        String sumTotal = String.format("%,.2f",takenLoanMonthTotalAmount.setScale(2, RoundingMode.DOWN));
        String sumActualReturnTotal = String.format("%,.2f",returnedALoanMonthTotalAmount.setScale(2, RoundingMode.DOWN));
        totalExpectedMonthReturnLoanAmountLabel.setText(String.format("%,.2f", returnedELoanMonthTotalAmount.setScale(2, RoundingMode.DOWN) ));
        String sumActualReturnProfitTotal = String.format("%,.2f",returnedALoanProfitFromMonthTotalAmount.setScale(2, RoundingMode.DOWN));
        totalMonthTakenLoanAmountLabel.setText(sumTotal);
        totalMonthReturnedLoanAmountLabel.setText(sumActualReturnTotal);
        totalMonthReturnedLoanProfitAmountLabel.setText(sumActualReturnProfitTotal);


        if(observeTakeLoanTransactionSpecifiedAccountListData.isEmpty() && observeReturnLoanTransactionSpecifiedAccountListData.isEmpty()) {
            CustomUtility.AlertHelper("Loan Month Date Alert", "Loan Month Display Information", "No LOAN RECORDS FOUND  FOR " + localDateMonth.getMonth().toString() + " OF "+ localDateMonth.getYear(), "I").show();

        }else{

                CustomUtility.AlertHelper("Loan Record For Month Date Alert", "Loan Records Found Information", observeTakeLoanTransactionSpecifiedAccountListData.size() + " Taken Loans and " + observeReturnLoanTransactionSpecifiedAccountListData.size() + " Returned Loans RECORDS FOUND FOR " + localDateMonth.getMonth().toString() + " OF " + localDateMonth.getYear(), "I").show();
            }

    }

    public void getAllLoansAction(ActionEvent actionEvent) {
        prepareLoanDisplayTable();
        loanTakenListTableView.setItems(observeTakeLoanTransactionSpecifiedAccountListData);
        loanReturnListTableView.setItems(observeReturnLoanTransactionSpecifiedAccountListData);

        observeTakeLoanTransactionSpecifiedAccountListData.setAll(ManageLoanTransaction.getTakenLoanTransactions());
        observeReturnLoanTransactionSpecifiedAccountListData.setAll(ManageLoanTransaction.getReturnLoanTransactions());
        String tMonthTLAL = ManageLoanTransaction.getTotalTakenLoan(ManageLoanTransaction.getTakenLoanTransactions(), "COLLECTED_AMOUNT").setScale(2, RoundingMode.DOWN).toString();
        totalMonthTakenLoanAmountLabel.setText(tMonthTLAL);
        String tEmonthTLAL = ManageLoanTransaction.getTotalTakenLoan(ManageLoanTransaction.getTakenLoanTransactions(), "EXPECTED_RETURNED_AMOUNT").setScale(2, RoundingMode.DOWN).toString();
        totalExpectedMonthReturnLoanAmountLabel.setText(tEmonthTLAL);
        String tMonthRLAL = ManageLoanTransaction.getTotalReturnLoan(ManageLoanTransaction.getReturnLoanTransactions(), "ACTUAL_RETURNED_AMOUNT").setScale(2, RoundingMode.DOWN).toString();
        totalMonthReturnedLoanAmountLabel.setText(tMonthRLAL);
        BigDecimal returnedALoanProfitFromMonthTotalAmount = ManageLoanTransaction.getTotalReturnLoan(ManageLoanTransaction.getReturnLoanTransactions(), "PROFIT_EARNED_FROM_RETURNED_AMOUNT");

        String sumActualReturnProfitTotal = String.format("%,.2f",returnedALoanProfitFromMonthTotalAmount.setScale(2, RoundingMode.DOWN));
        totalMonthReturnedLoanProfitAmountLabel.setText(sumActualReturnProfitTotal);


        totalFilteredMonthTakenLoanAmountLabel.setText("FILTERED: "+tMonthTLAL);
        totalFilteredMonthReturnLoanAmountLabel.setText("FILTERED: " +tMonthRLAL);
    }


    public void buttonAddSharesFindMonthlyAction(ActionEvent actionEvent) throws Exception{

        getAllSharesTransactionForSpecifiedMonth(ALL_SHARES);
    }

    private void getAllSharesTransactionForSpecifiedMonth(int sharesCategory) throws Exception{
        if(shareMonthDatePicker.getValue() == null){
            CustomUtility.AlertHelper("Error Getting Monthly Shares", "Error Getting Shares Transaction:", "Please SELECT Shares Date Before Clicking This Button", "I").show();
            return;
        }

        if(sharesCategory != ALL_SHARES_CAT && sharesCategory != ALL_PENDING_SHARES_CAT && sharesCategory != ALL_REWARDED_SHARES_CAT){
            throw new IllegalArgumentException("Please sharesCategory Must be ALL_SHARES Or ALL_PENDING_SHARES Or ALL_REWARDED_SHARES");
        }

        LocalDate localDateMonth = shareMonthDatePicker.getValue();

        prepareSharesDisplayTable();
        populateDisplayMonthlyDistributedShares();

        shareslisttableview.setItems(observeSharesTransactionSpecifiedAccountListData);

        observeSharesTransactionSpecifiedAccountListData.setAll(ManageSharesTansaction.getSharesTransactionsForMonth(localDateMonth, sharesCategory));
        sharesMonthTotalAmount = ManageSharesTansaction.getTotal(ManageSharesTansaction.getSharesTransactionsForMonth(localDateMonth, sharesCategory), ALL_SHARES);

        String sumTotal = String.format("%,.2f",sharesMonthTotalAmount.setScale(2, RoundingMode.DOWN));
        displayMonthShareTotalLabel.setText(sumTotal);
        filteredSharedSumLabel.setText("FILTERED SUM: " + sumTotal);
        filteredSharedSumLabel.setFont(Font.font("arial", FontWeight.EXTRA_BOLD,20 ));

        if(observeSharesTransactionSpecifiedAccountListData.isEmpty()) {
            CustomUtility.AlertHelper("Shares Date Alert", "Shares Display Information", "No SHARES RECORDS FOUND  FOR " + localDateMonth.getMonth().toString() + " OF "+ localDateMonth.getYear(), "I").show();
            buttonAddSharesTrigger.setDisable(true);
        }else{

            if(sharesCategory == ALL_PENDING_SHARES_CAT) {

                buttonAddSharesTrigger.setDisable(false);

                CustomUtility.AlertHelper("Shares Date Alert", "Shares Distribution Enabled Information", "DISTRIBUTING SHARES AMONG ALL PARTICIPANTS FOR " + localDateMonth.getMonth().toString() + " OF " + localDateMonth.getYear(), "I").show();
            }

        }
    }

    private void populateDisplayMonthlyDistributedShares(){
        prepareSharesDisplayTable();
        sharedMonthlyAmountTableView.setItems(observeSharesDistributedTransactionListData);

        observeSharesDistributedTransactionListData.setAll(ManageSharesTansaction.getAllMonthlyDistributedTransactionList());

    }

    private void prepareSharesDisplayTable(){

        if(shareslisttableview.getColumns().isEmpty()) {

            TableColumn<SharesTransaction, String> sColId = new TableColumn<>("Id");
            TableColumn<SharesTransaction, String> sColDesc = new TableColumn<>("Description");
            TableColumn<SharesTransaction, String> sColTransactionDate = new TableColumn<>("Transaction Date");
            TableColumn<SharesTransaction, String> sColTransactionType = new TableColumn<>("Transaction Type");
            TableColumn<SharesTransaction, String> sColStatus = new TableColumn<>("Status");
            TableColumn<SharesTransaction, String> sColAmount = new TableColumn<>("Shares Amount");
            TableColumn<SharesTransaction, String> sColAccountNo = new TableColumn<>("Account No");

            sColId.setCellValueFactory(new PropertyValueFactory<>("Id"));
            sColDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
            sColTransactionDate.setCellValueFactory(new PropertyValueFactory<>("transaction_date"));
            sColTransactionType.setCellValueFactory(new PropertyValueFactory<>("transaction_type"));
            sColStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            sColAccountNo.setCellValueFactory(new PropertyValueFactory<>("accountNo"));
            sColAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

            sColId.setVisible(false);

            shareslisttableview.getColumns().setAll(sColId, sColAccountNo, sColAmount, sColTransactionType, sColTransactionDate, sColDesc, sColStatus);
        }

        // For the shared Monthly Amount Table View Details
        if(sharedMonthlyAmountTableView.getColumns().isEmpty()) {

            TableColumn<SharesDistributionTransaction, String> sTColId = new TableColumn<>("Id");
            TableColumn<SharesDistributionTransaction, String> sTColDesc = new TableColumn<>("Description");
            TableColumn<SharesDistributionTransaction, Date> sTColCreditedDate = new TableColumn<>("Shares Distribution Date");
            TableColumn<SharesDistributionTransaction, BigDecimal> sTColTotalRevenue = new TableColumn<>("Total Revenue");
            TableColumn<SharesDistributionTransaction, BigDecimal> sTColNoTrasactions = new TableColumn<>("No Shares Transactions");
            TableColumn<SharesDistributionTransaction, Integer> sTColStatus = new TableColumn<>("Status");
            TableColumn<SharesDistributionTransaction, BigDecimal> sTColTotalMonthlyShare = new TableColumn<>("Month Total Shares");
            TableColumn<SharesDistributionTransaction, BigDecimal> sTColSharedProfit = new TableColumn<>("Shared Profit");
            TableColumn<SharesDistributionTransaction, String> sTColMonthYear = new TableColumn<>("Share Month Year");

            sTColId.setCellValueFactory(new PropertyValueFactory<>("Id"));
            sTColDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
            sTColCreditedDate.setCellValueFactory(new PropertyValueFactory<>("creditedDate"));
            sTColMonthYear.setCellValueFactory(new PropertyValueFactory<>("monthYear"));
            sTColStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            sTColTotalRevenue.setCellValueFactory(new PropertyValueFactory<>("totalRevenue"));
            sTColTotalMonthlyShare.setCellValueFactory(new PropertyValueFactory<>("monthTotalShare"));
            sTColSharedProfit.setCellValueFactory(new PropertyValueFactory<>("profit"));
            sTColNoTrasactions.setCellValueFactory(new PropertyValueFactory<>("no_of_transactions"));

            sTColId.setVisible(false);

            sharedMonthlyAmountTableView.getColumns().setAll(sTColId, sTColMonthYear, sTColTotalRevenue, sTColTotalMonthlyShare, sTColNoTrasactions, sTColSharedProfit, sTColCreditedDate, sTColDesc, sTColStatus);

        }

    }

    private void prepareLoanDisplayTable(){

        if(loanTakenListTableView.getColumns().isEmpty()){
            TableColumn<TakeLoanTransaction, String> lTColId = new TableColumn<>("Id");
            TableColumn<TakeLoanTransaction, String> lTColDesc = new TableColumn<>("Description");
            TableColumn<TakeLoanTransaction, Date> lTColDateCollected = new TableColumn<>("Loan Collected Date");
            TableColumn<TakeLoanTransaction, BigDecimal> lTColAmount = new TableColumn<>("Loan Amount");
            TableColumn<TakeLoanTransaction, Integer> lTColInterestRate = new TableColumn<>("Loan Interest Rate");
            TableColumn<TakeLoanTransaction, Integer> lTColStatus = new TableColumn<>("Status");
            TableColumn<TakeLoanTransaction, Integer> lTColLoanPeriod = new TableColumn<>("Loan Period");
            TableColumn<TakeLoanTransaction, String> lTColLedgerNo = new TableColumn<>("Loan Ledger No");
            TableColumn<TakeLoanTransaction, String> lTColAccountNo = new TableColumn<>("Account No");

            lTColId.setCellValueFactory(new PropertyValueFactory<>("Id"));
            lTColDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
            lTColDateCollected.setCellValueFactory(new PropertyValueFactory<>("dateCollected"));
            lTColAccountNo.setCellValueFactory(new PropertyValueFactory<>("accountNo"));
            lTColStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            lTColAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            lTColLoanPeriod.setCellValueFactory(new PropertyValueFactory<>("loanPeriod"));
            lTColLedgerNo.setCellValueFactory(new PropertyValueFactory<>("ledgerNo"));
            lTColInterestRate.setCellValueFactory(new PropertyValueFactory<>("interestRate"));

            lTColId.setVisible(false);

            loanTakenListTableView.getColumns().setAll(lTColId, lTColAccountNo, lTColAmount, lTColLoanPeriod, lTColInterestRate, lTColLedgerNo, lTColDateCollected, lTColDesc, lTColStatus);

        }

        if(loanReturnListTableView.getColumns().isEmpty()){
            TableColumn<ReturnLoanTransaction, String> lRColId = new TableColumn<>("Id");
            TableColumn<ReturnLoanTransaction, String> lRColDesc = new TableColumn<>("Description");
            TableColumn<ReturnLoanTransaction, Date> lRColDateCollected = new TableColumn<>("Loan Collected Date");
            TableColumn<ReturnLoanTransaction, BigDecimal> lRColCollectedAmount = new TableColumn<>("Loan Amount Collected");
            TableColumn<ReturnLoanTransaction, BigDecimal> lRColExpectedAmount = new TableColumn<>("Loan Amount Returned");
            TableColumn<ReturnLoanTransaction, Integer> lRColInterestRate = new TableColumn<>("Loan Interest Rate");
            TableColumn<ReturnLoanTransaction, Integer> lRColStatus = new TableColumn<>("Status");
            TableColumn<ReturnLoanTransaction, Integer> lRColLoanPeriod = new TableColumn<>("Loan Period");
            TableColumn<ReturnLoanTransaction, String> lRColLedgerNo = new TableColumn<>("Loan Ledger No");
            TableColumn<ReturnLoanTransaction, String> lRColAccountNo = new TableColumn<>("Account No");
            TableColumn<ReturnLoanTransaction, String> lRColDatePaid = new TableColumn<>("Date Loan Was Returned");

            lRColId.setCellValueFactory(new PropertyValueFactory<>("Id"));
            lRColDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
            lRColDateCollected.setCellValueFactory(new PropertyValueFactory<>("dateCollected"));
            lRColAccountNo.setCellValueFactory(new PropertyValueFactory<>("accountNo"));
            lRColStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            lRColCollectedAmount.setCellValueFactory(new PropertyValueFactory<>("collectedAmount"));
            lRColExpectedAmount.setCellValueFactory(new PropertyValueFactory<>("expectedAmount"));
            lRColLoanPeriod.setCellValueFactory(new PropertyValueFactory<>("loanPeriod"));
            lRColLedgerNo.setCellValueFactory(new PropertyValueFactory<>("ledgerNo"));
            lRColInterestRate.setCellValueFactory(new PropertyValueFactory<>("interestRate"));
            lRColDatePaid.setCellValueFactory(new PropertyValueFactory<>("datePaid"));

            lRColId.setVisible(false);

            loanReturnListTableView.getColumns().setAll(lRColId, lRColAccountNo, lRColCollectedAmount, lRColLoanPeriod, lRColInterestRate, lRColExpectedAmount, lRColLedgerNo, lRColDateCollected, lRColDesc, lRColStatus);

        }

    }

    public void setFilterMonthlySharesList(KeyEvent keyEvent) {

        if(this.observeSharesTransactionSpecifiedAccountListData.isEmpty()){
            return;
        }

        sharesAccountFilterList = new FilteredList<SharesTransaction>(observeSharesTransactionSpecifiedAccountListData, p->true);
        filterAllShareDisplayListTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            sharesAccountFilterList.setPredicate(pere -> {
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

            sharesSortedList = new SortedList<>(sharesAccountFilterList);
            sharesSortedList.comparatorProperty().bind(shareslisttableview.comparatorProperty());
            shareslisttableview.setItems(sharesSortedList);
            tempSumVal = BigDecimal.ZERO;
            sharesSortedList.getSource().forEach(e ->{
                tempSumVal = tempSumVal.add(e.getAmount());
                filteredSharedSumLabel.setText(String.format("FILTERED SUM: %,.2f", tempSumVal.setScale(2, RoundingMode.DOWN)));
            });

        });
    }


    public BigDecimal tempSumVal = BigDecimal.ZERO;

    public void buttonAddSharesFindPendingMonthlyAction(ActionEvent actionEvent) throws Exception {
        getAllSharesTransactionForSpecifiedMonth(ALL_PENDING_SHARES_CAT);
    }

    public void setFilterMonthlyTakenLoanList(KeyEvent keyEvent) {
        if(this.observeTakeLoanTransactionSpecifiedAccountListData.isEmpty()){
            return;
        }

        takeLoanAccountFilterList = new FilteredList<TakeLoanTransaction>(observeTakeLoanTransactionSpecifiedAccountListData, p->true);
        filterTakenLoanTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            takeLoanAccountFilterList.setPredicate(pere -> {
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

                if(pere.getRepayDate().toString().indexOf(typedText) != -1){
                    return true;
                }

                if(pere.getLedgerNo().toString().indexOf(typedText) != -1){
                    return true;
                }

                if(pere.getInterestRate().toString().indexOf(typedText) != -1){
                    return true;
                }

                if(pere.getAmount().toString().indexOf(typedText) != -1){
                    return true;
                }

                return false;
            });

            takeLoanSortedList = new SortedList<>(takeLoanAccountFilterList);
            takeLoanSortedList.comparatorProperty().bind(loanTakenListTableView.comparatorProperty());
            loanTakenListTableView.setItems(takeLoanSortedList);
            tempSumVal = BigDecimal.ZERO;
            takeLoanSortedList.getSource().forEach(e ->{
                tempSumVal = tempSumVal.add(e.getAmount());
                totalFilteredMonthTakenLoanAmountLabel.setText(String.format("FILTERED SUM: %,.2f", tempSumVal.setScale(2, RoundingMode.DOWN)));
            });
    });

    }

    public void setFilterMonthlyReturnLoanList(KeyEvent keyEvent) {
        if(this.observeTakeLoanTransactionSpecifiedAccountListData.isEmpty()){
            return;
        }

        returnLoanAccountFilterList = new FilteredList<ReturnLoanTransaction>(observeReturnLoanTransactionSpecifiedAccountListData, p->true);
        filterReturnLoanTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            returnLoanAccountFilterList.setPredicate(pere -> {
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

                if(pere.getDatePaid().toString().indexOf(typedText) != -1){
                    return true;
                }

                if(pere.getLedgerNo().toString().indexOf(typedText) != -1){
                    return true;
                }

                if(pere.getInterestRate().toString().indexOf(typedText) != -1){
                    return true;
                }

                if(pere.getExpectedAmount().toString().indexOf(typedText) != -1){
                    return true;
                }

                if(pere.getCollectedAmount().toString().indexOf(typedText) != -1){
                    return true;
                }

                return false;
            });

            returnLoanSortedList = new SortedList<>(returnLoanAccountFilterList);
            returnLoanSortedList.comparatorProperty().bind(loanReturnListTableView.comparatorProperty());
            loanReturnListTableView.setItems(returnLoanSortedList);
            tempSumVal = BigDecimal.ZERO;
            returnLoanSortedList.getSource().forEach(e ->{
                tempSumVal = tempSumVal.add(e.getCollectedAmount());
                totalFilteredMonthReturnLoanAmountLabel.setText(String.format("FILTERED SUM: %,.2f", tempSumVal.setScale(2, RoundingMode.DOWN)));
            });
        });

    }
}
