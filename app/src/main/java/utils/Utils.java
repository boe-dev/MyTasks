package utils;

/**
 * Created by benny on 16.05.16.
 */
public class Utils {

    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

}
