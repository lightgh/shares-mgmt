package major;

import static major.CustomUtility.print;
import static major.CustomUtility.randIntegerBetween;

/**
 * This Generates Account Number
 * Created by chinakalight on 6/15/18.
 */
public class AccountNumberGenerator {

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
     * Shuffles String
     * @param val
     * @return
     */
    private String shuffleString(String val){
        return CustomUtility.shuffleString(val);
    }

    /**
     * Test Run the Methods
     * @param args
     */
    public static void main(String[] args) {
        AccountNumberGenerator ag = new AccountNumberGenerator();
        print(ag.getNewAccountNo(10));
    }

}
