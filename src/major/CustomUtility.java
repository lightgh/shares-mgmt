
package major;


import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomUtility{
	

	/**
     * shuffle String
     * @param string
     * @return
     */
    public String shuffleString(String string){

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
    public  int randIntegerBetween(int min, int max){

        int tempRandNumber = (int)(Math.random() *((max-min)+1)) + min;

        return  tempRandNumber;
    }

    /**
     * Generate and Return A Unique Length of Stringed Numbers
     * @param  length [this is the length of the string that would be returned] <= 15 in length
     * @return        returns a string of length - length
     */
    public String getNewAccountNo(int length){

    		if(length > 15){
    			length = 15;
    		}

        String newAccountNo = "";

        String characters = "012345678910";

        String resString = shuffleString(shuffleString(characters) + characters);

        if(resString.length()< length){
        	int numbOfTimesToContatenate = (length/resString.length()) + 1;
        	for (int ijcount = 0; ijcount < numbOfTimesToContatenate; ijcount++ ) {
        		resString += shuffleString(resString);
        	}
        }

        int firstPartInteger = randIntegerBetween(0, characters.length());

        newAccountNo = resString.substring(0, firstPartInteger) + resString.substring(characters.length()-firstPartInteger, characters.length());

        if(newAccountNo.length() < length){
        	int lengthDifference = newAccountNo.length() - length;
        	StringBuilder tempAccountNo = new StringBuilder(newAccountNo);
        	for (int icount = 0; icount < lengthDifference ; icount++ ) {
        		tempAccountNo.append(characters.charAt((int)(Math.random()*(newAccountNo.length() - icount) + 1 )));
        	}
        	newAccountNo = tempAccountNo.toString();
        }else{
        	newAccountNo = newAccountNo.substring(0, length);
        }

        if(newAccountNo.length() != length){
        	return getNewAccountNo(length);
        }

        return newAccountNo;

    }

    /**
     * Alert Helper function
     * @param title
     * @param headerText
     * @param contentText
     * @param alertType
     * @return
     */
    public Alert AlertHelper(String title, String headerText, String contentText, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Account Information Display Notification");
        alert.setContentText("");

//        alert.show();//.showAndWait();
        return alert;//.showAndWait();
    }
    
    

    public static void main(String[] args) {
    	CustomUtility cu = new CustomUtility();

    	print(cu.getNewAccountNo(10));
    }

    public static void print(String string){
        System.out.print(string);
    }

    public static void println(String string){
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
        try {
            resultSet = this.buildDBConnection().statement.executeQuery(
               query
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
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

}