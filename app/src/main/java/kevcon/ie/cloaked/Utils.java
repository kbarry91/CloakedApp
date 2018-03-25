package kevcon.ie.cloaked;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <h1>Utils</h1>
 * Utils is a class to hold generic methods
 *
 * @author kevin barry
 * @since 19/3/2018
 */
public class Utils {

    /**
     * convertDate converts the date from time in milliseconds to MM/dd/yy hh:mm format
     *
     * @return a formatted string date
     */
    static String convertDate(String date) {

        String dateString;
        long millisecond = Long.parseLong(date);
        dateString = new SimpleDateFormat("MM/dd/yy hh:mm").format(new Date(millisecond));
        return dateString;
    }

}
