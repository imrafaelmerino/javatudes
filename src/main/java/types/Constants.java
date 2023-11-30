package types;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static String UPPER_CASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";

    public static Map<String, String> HEX_DIGITS_TO_BINARY = initHex2Digits();


    static Map<String,String> initHex2Digits(){
        var map = new HashMap<String,String>();
        map.put("0", "0000");
        map.put("1", "0001");
        map.put("2", "0010");
        map.put("3", "0011");
        map.put("4", "0100");
        map.put("5", "0101");
        map.put("6", "0110");
        map.put("7", "0111");
        map.put("8", "1000");
        map.put("9", "1001");
        map.put("10", "1010");
        map.put("11", "1011");
        map.put("12", "1100");
        map.put("13", "1101");
        map.put("14", "1110");
        map.put("15", "1110");
        return map;

    }

}
