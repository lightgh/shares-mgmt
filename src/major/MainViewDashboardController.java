package major;

/**
 * Created by chinakalight on 6/20/18.
 *
 * Main View Dashboard Controller
 *
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.converter.LocalDateStringConverter;
import org.controlsfx.control.table.TableFilter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import sun.java2d.pipe.hw.AccelDeviceEventNotifier;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static major.CustomUtility.println;

/** Controls the main application screen */
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

    TableFilter.Builder<TableMemberAccount> tableFilterBuilder;
    TableFilter<TableMemberAccount> tableFilter;

    ObservableList<TableMemberAccount> observeMemberAccountListData = FXCollections.observableArrayList();

    SessionFactory sessionFactory = CustomUtility.getSessionFactory();
    Session session = sessionFactory.openSession();

    AppLoginManager loginManager;



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

        LocalDateStringConverter localDateStringConverter = new LocalDateStringConverter();
                String dateString = localDateStringConverter.toString(openDate);
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
                membA.setOpening_date(Date.from(openDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
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
        /*tableFilterBuilder = TableFilter.forTableView(tableViewAccountList);
        tableFilter = tableFilterBuilder.apply();

        tableFilter.setSearchStrategy((input,target) -> {
            try {
                return target.matches(input);
            } catch (Exception e) {
                return false;
            }
        });*/


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
                TableMemberAccount tableMemberAccount = tableViewAccountList.getSelectionModel().getSelectedItem();
                MembershipAccount membershipAccount = ManageMembershipAccount.getMemberAccountFromTableMemberAccount(tableMemberAccount);
                showUpdateView(membershipAccount);
            }
        });

    }

    public void showUpdateView(MembershipAccount membershipAccount){
        accountMgmtSection.getSelectionModel().select(addAccountTabSection1);
        addAccountTabSection.setDisable(true);
        accountListTabSection.setDisable(true);
        addAccountTabSection1.setDisable(false);

        naFirstName1.setText(membershipAccount.getFirstName());
        naLastName1.setText(membershipAccount.getLastName());
        naOtherName1.setText(membershipAccount.getOtherName());
        naPhoneNo1.setText(membershipAccount.getPhoneNo());
        naAddress1.setText(membershipAccount.getAddress());
        naAccountNo1.setText(membershipAccount.getAccountNo());
        LocalDate localDateOpening = CustomUtility.getLocalDateFromDate(membershipAccount.getOpening_date());
        naOpenDate1.setValue(localDateOpening);
        naOpenDate1.setEditable(false);
        statusSelect1.getSelectionModel().select(membershipAccount.getStatus()==1? "Active" : "InActive");

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
            membershipAccount.setAddress(address);
            membershipAccount.setFirstName(firstName);
            membershipAccount.setLastName(lastName);
            membershipAccount.setOtherName(otherName);
            membershipAccount.setAccountNo(naAccountNo1.getText());
            membershipAccount.setPhoneNo(phoneNo);
            session.saveOrUpdate(membershipAccount);
            membershipAccount.setStatus(statusSelect1.getSelectionModel().getSelectedItem()=="Active"? 1 : 0);
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
}
