package kevcon.ie.cloaked;

import android.icu.text.SimpleDateFormat;

import java.util.Date;

/**
 * <h1>Utils</h1>
 * Utils is a class to hold generic methods
 *
 * @author kevin barry
 * @since 19/3/2018
 */
public class Utils {
    public static String convertDate(String date) {

        String longV = date;
        String dateString;
        long millisecond = Long.parseLong(longV);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            dateString = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS").format(new Date(millisecond));
        } else {
            dateString = "received";
        }

        return dateString;
    }
}
