
package major;


import javafx.scene.control.Alert;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

public class CustomUtility{


    private static final SessionFactory sessionFactory;

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

//        LocalDate dt = getLocalDateFromString("12-06-2018");
//        LocalDate dt = getLocalDateFromString("2018-06-12");
        LocalDate dt = getLocalDateFromString("2018/12/6");


        Date td2 = getDateFromLocalDate(dt);
        pln(td2.toString());
        LocalDate dt2 = getLocalDateFromDate(td2);
        pln("STR_FROM_LD: "+getStringFromLocalDate(dt2));

        System.exit(0);

//        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        AccountNumberGenerator accountNumberGenerator = new AccountNumberGenerator();
        SessionFactory sessionFactory = CustomUtility.getSessionFactory();
        Session session = sessionFactory.openSession(); //sessionFactory.getCurrentSession();
        /*Transaction transaction = session.beginTransaction();
        MembershipAccount membA = new MembershipAccount();
        membA.setAccountNo(accountNumberGenerator.getNewAccountNo(10));
        membA.setFirstName("MusaJane");
        membA.setLastName("Saminu");
        membA.setOtherName("Kenw");
        membA.setAddress("Opp Sch Of Agric");
        membA.setClosing_date(new Date());
        membA.setOpening_date(new Date());
        membA.setPhoneNo("08031289230");
        membA.setStatus(1);
        session.save(membA);
        transaction.commit();
        CustomUtility.println("Successfully inserted");
        sessionFactory.close();*/

        String hql = "FROM MembershipAccount M WHERE M.accountNo='3501697105'";
//        String hql = "FROM MembershipAccount M WHERE M.firstName LIKE 'Chinaka'";

        Transaction transactionA = session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<MembershipAccount> query = builder.createQuery(MembershipAccount.class);
//        query.from(MembershipAccount.class);
        Root<MembershipAccount> root = query.from(MembershipAccount.class);
//        query.select(builder.construct(MembershipAccount.class, root.get("accountNo")));

        Query<MembershipAccount> memQ = session.createQuery(hql, MembershipAccount.class);
//        List memberAccountList = session.createQuery(query).getResultList();
        List<MembershipAccount> memberAccountList = memQ.list();
        MembershipAccount membershipAccount;

        println("ACCOUNT_LISTING");
        for (Object memberAccount:
             memberAccountList) {
            membershipAccount = (MembershipAccount)memberAccount;
            print(membershipAccount.getId() + " ");
            print(membershipAccount.getAccountNo() + " ");
            println(membershipAccount.getFullName() + " ");
        }



//        query.select(root).where(builder.equal(root.get("accountNo"), "3501697105"));
//                .where(builder.);



        /*org.hibernate.query.Query<MembershipAccount> q= session.createQuery(query);
        MembershipAccount membershipAccountNow = q.getSingleResult();
        println(membershipAccountNow.getAccountNo());
        println(membershipAccountNow.getFullName());
        println(membershipAccountNow.getAddress());*/


//        Query<Long> longQuery = session.createQuery(queryCount);

//        long count = queryCount..getSingleResult();
        /*List<Object[]> countObj = session.createNativeQuery(
                "SELECT count(*) as number FROM account_info" )
                .list();

        println("Count = "+(countObj.get(0) ));*/

//        List<MembershipAccount> =

        transactionA.commit();



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
}