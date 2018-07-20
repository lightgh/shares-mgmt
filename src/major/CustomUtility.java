
package major;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Date;

public class CustomUtility{


    private static final SessionFactory sessionFactory;

    public static final int OK = 1;
    public static final int OK_PLUS = 2;
    public static final int CANCEL = 0;

    private static boolean okAnswer = false;
    private static int okMultiAnswer = CANCEL;

    private static ServiceRegistry serviceRegistry;

    static{
        try{
            StandardServiceRegistry standardServiceRegistry =
                    new StandardServiceRegistryBuilder().configure("/major/hibernate.cfg.xml").build();
            Metadata metadata =
                    new MetadataSources(standardServiceRegistry).getMetadataBuilder().build();

            sessionFactory = metadata.getSessionFactoryBuilder().build();
        }catch (Throwable throwable){
            System.err.println("Initial SessionFactory creation failed");
            throw new ExceptionInInitializerError(throwable);
        }
    }

    public static SessionFactory getSessionFactory() {

        return sessionFactory;
    }

	/**
     * shuffle String
     * @param string
     * @return
     */
    public static String shuffleString(String string){

        List<String> stringedCharacters = new ArrayList(Arrays.asList(string.split("")));

        Collections.shuffle(stringedCharacters);
        StringBuilder sb = new StringBuilder();
        for(String eachChar:
        stringedCharacters) {
            sb.append(eachChar);
        }

        return sb.toString();
    }

    /**
     * return random integer between two numbers
     * @param min
     * @param max
     * @return
     */
    public static int randIntegerBetween(int min, int max){

        int tempRandNumber = (int)(Math.random() *((max-min)+1)) + min;

        return  tempRandNumber;
    }


    /**
     * Alert Helper function
     * @param title
     * @param headerText
     * @param contentText
     * @param alertType
     * @return
     */
    public static Alert AlertHelper(String title, String headerText, String contentText, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        return alert;
    }


    public static Alert AlertHelper(String title, String headerText, String contentText, String E){
        Alert.AlertType alertType = Alert.AlertType.INFORMATION;

        Alert alert = new Alert(alertType);

        if(E == "E"){
             alertType = Alert.AlertType.ERROR;

        }else if(E == "C"){
            alertType = Alert.AlertType.CONFIRMATION;
        }


        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        return alert;
    }


    public static boolean ConfirmationAlertHelper(String title, String content) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                CustomUtility.class.getResource("ConfirmAlert.fxml")
        );

        Scene scene = new Scene((Parent) loader.load(), 363, 234);

        Stage dialogStage = new Stage();

        dialogStage.setScene(scene);
        dialogStage.setTitle("Alert " + title);
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        Label labelTitle = (Label)loader.getNamespace().get("alertTitleLabel");
        Label labelContent = (Label)loader.getNamespace().get("alertContentLabel");

        labelTitle.setText(title);
        labelContent.setText(content);

        Button btn_ok = (Button)loader.getNamespace().get("btnConfirmOk");
        Button btn_cancel = (Button)loader.getNamespace().get("btnConfirmCancel");

        btn_ok.setOnAction(arg0 -> {
            dialogStage.hide();
            okAnswer = true;
        });

        btn_cancel.setOnAction(arg0 -> {
            dialogStage.hide();
            okAnswer = false;
        });

        dialogStage.showAndWait();

        return okAnswer;
    }

    public static int ConfirmationWithOptionsAlertHelper(String title, String content) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                CustomUtility.class.getResource("MultiConfirmAlert.fxml")
        );

        Scene scene = new Scene((Parent) loader.load(), 563, 254);

        Stage dialogStage = new Stage();

        dialogStage.setScene(scene);
        dialogStage.setTitle("Alert " + title);
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        Label labelTitle = (Label)loader.getNamespace().get("alertTitleLabel");
        Label labelContent = (Label)loader.getNamespace().get("alertContentLabel");

        labelTitle.setText(title);
        labelContent.setText(content);

        Button btn_ok = (Button)loader.getNamespace().get("btnConfirmOk");
        Button btn_ok_plus = (Button)loader.getNamespace().get("btnConfirmOkPlus");
        Button btn_cancel = (Button)loader.getNamespace().get("btnConfirmCancel");

        btn_ok.setOnAction(arg0 -> {
            dialogStage.hide();
            okMultiAnswer = OK;
        });

        btn_ok_plus.setOnAction(arg0 -> {
            dialogStage.hide();
            okMultiAnswer = OK_PLUS;
        });

        btn_cancel.setOnAction(arg0 -> {
            dialogStage.hide();
            okMultiAnswer = CANCEL;
        });

        dialogStage.showAndWait();

        return okMultiAnswer;
    }


    public static void print(String string){
        System.out.print(string);
    }

    public static void p(String string){
        System.out.print(string);
    }

    public static void println(String string){
        System.out.println(string);
    }

    public static void pln(String string){
        System.out.println(string);
    }


    /**
     * This method builds the DB Connectivity
     * @return
     */
    public CustomUtility buildDBConnection(){
        try{
            connection = DriverManager.getConnection(
                    DATABASE_URL,
                    USERNAME,
                    PASSWORD
            );


            statement = connection.createStatement();

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        return this;
    }


    /**
     * This querys the database and returns ResultSet
     * @param query
     * @return
     */
    public ResultSet queryDB(String query){

        ResultSet thisResultSet = null;

        try {
            thisResultSet = this.buildDBConnection().statement.executeQuery(
               query
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return thisResultSet;

    }

    /**
     * This querys the database and returns ResultSet
     * @param query
     * @return
     */
    public boolean insQueryDB(String query){

    boolean  insertedQ = false;
    int result_Set = 0;

        try {
            result_Set = this.buildDBConnection().statement.executeUpdate(query, Statement.CLOSE_ALL_RESULTS
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result_Set != 0;
    }

    public boolean validateCredentials(String username, String password){

        String checkQuery = "SELECT `id`, `username`, `password`, `created_at` FROM `user` WHERE `username`='"+
                username+"' AND password=SHA1('"+password+"')";

        ResultSet rs = this.queryDB(checkQuery);
        int count = 0;
        try {
            while(rs.next()){
                count++;
            }
            CustomUtility.println(""+count);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(count == 0){
            return false;
        }

        return true;
    }

    public boolean cleanResource(){

        boolean clean = false;

        try{
            resultSet.close();
            statement.close();
            connection.close();

            return (clean = true);

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        return clean;
    }

    private static final String DATABASE_URL = "jdbc:mysql://localhost/sharesdb";
    static final String USERNAME = "root";
    static final String PASSWORD = "";
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public static void main(String args [] ){

        BigDecimal amount = new BigDecimal(5000);
        pln("INTEREST: " + ManageLoanTransaction.getIncuredInterest(amount,60));
        pln("EXPECTED AMOUNT: " + amount.add(ManageLoanTransaction.getIncuredInterest(amount,60)));

    }

    public static String getAppropriateStringFormat(String dateString){
        String formatString = "";

            if(dateString.matches("\\d{4}-\\d{2}-\\d{2}")){
                formatString = "yyyy-MM-dd";
            }else if(dateString.matches("\\d{2}-\\d{2}-\\d{4}")){
                formatString = "dd-MM-yyyy";
            }else if(dateString.matches("\\d{4}/\\d{2}/\\d{2}")){
                formatString = "yyyy/MM/dd";
            }else if(dateString.matches("\\d{2}/\\d{2}/\\d{4}")){
                formatString = "dd/MM/yyyy";
            }else if(dateString.matches("\\d{2}/\\d{1}/\\d{4}")){
                formatString = "dd/M/yyyy";
            }else if(dateString.matches("\\d{1}/\\d{2}/\\d{4}")){
                formatString = "d/MM/yyyy";
            }else if(dateString.matches("\\d{1}/\\d{1}/\\d{4}")){
                formatString = "d/M/yyyy";
            }else if(dateString.matches("\\d{4}/\\d{2}/\\d{1}")){
                formatString = "yyyy/MM/d";
            }else if(dateString.matches("\\d{4}/\\d{1}/\\d{1}")){
                formatString = "yyyy/M/d";
            }

        return formatString;
    }


    public static Date getDateFromString(String date){
        return getDateFromLocalDate(getLocalDateFromString(date));
    }

    public static LocalDate getLocalDateFromString(String date){
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern(getAppropriateStringFormat(date));

        dateTimeformatter.toString();

        LocalDate localDate = LocalDate.parse(date, dateTimeformatter);
        return localDate;
    }


    public static String getStringFromDate(Date date){
        return getStringFromLocalDate(getLocalDateFromDate(date));
    }

    /**
     * Convert LocalDate to String format
     * @param date
     * @return
     */
    public static String getStringFromLocalDate(LocalDate date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dateString = date.format(formatter);

        return dateString;
    }

    public static Date getDateFromLocalDate(LocalDate localDate){
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    public static LocalDate getLocalDateFromDate(Date date){
        Instant instant = Instant.ofEpochMilli(date.getTime());
        LocalDate localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        return localDate;
    }

    public static Alert AlertHelperTest(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Testing functionality such as working confirmation");
        alert.setHeaderText("Test Heading");
        alert.setContentText("Heading content");
        return alert;
    }

    public static String getUniqueLedgerNoString() {
        AccountNumberGenerator acng = new AccountNumberGenerator();
        return "ledgno"+CustomUtility.shuffleString(acng.getNewAccountNo(5) + CustomUtility.randIntegerBetween(10, 99));
    }

    public static int daysDiff(LocalDate firstDate, LocalDate secondDate){

        return (int)ChronoUnit.DAYS.between(firstDate, secondDate);

    }

    public static boolean netIsAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }
}