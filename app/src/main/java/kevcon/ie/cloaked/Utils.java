package kevcon.ie.cloaked;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <h1>Utils</h1>
 * Utils is a class to hold generic methods.
 *
 * @author kevin barry
 * @since 25/4/2018
 */
public class Utils {

    /**
     * convertDate converts the date from time in milliseconds to MM/dd/yy hh:mm format.
     *
     * @return a formatted string date.
     */
    static String convertDate(String date) {

        String dateString;
        long millisecond = Long.parseLong(date);
        dateString = new SimpleDateFormat("MM/dd/yy hh:mm").format(new Date(millisecond));
        return dateString;
    }

    /**
     * addCountryCode removes the 0 from the number and appends country formatting.
     */
    public static String addCountryCode(String countryCode, String number) {

        return number.replaceFirst("0", "+" + countryCode);
    }

    //https://www.codeproject.com/Articles/1044639/Android-Java-How-To-Send-SMS-Receive-SMS-Get-SMS-M

    /**
     * sendMessage sends a sms Message to a contact.
     *
     * @param testContact A Contact Object with the number to send to.
     * @param message     The message to send.
     * @param ctx         the Context of the view to return to.
     */
    public static void sendMessage(Contacts testContact, String message, Context ctx) {

        SmsManager sms = SmsManager.getDefault();

        final Context thisContext = ctx;

        // must create intents to Check if sms is sent and delivered
        PendingIntent sentPending = PendingIntent.getBroadcast(ctx,
                0, new Intent("SENT"), 0);

        // receiver intent to return result of  Broadcast
        ctx.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:

                        Toast.makeText(thisContext, "Key Sent.",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(thisContext, "Key Not Sent: Generic failure.",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(thisContext, "Key Not Sent: No service ",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(thisContext, "Key Not Sent: Null PDU.",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(thisContext, "Key Not Sent: Ensure Airplane mode is disabled",
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }, new IntentFilter("SENT"));

        PendingIntent deliveredPending = PendingIntent.getBroadcast(thisContext,
                0, new Intent("DELIVERED"), 0);

        thisContext.registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context arg0, Intent arg1) {
                        switch (getResultCode()) {
                            case Activity.RESULT_OK:
                                Toast.makeText(thisContext, "Delivered Key.",
                                        Toast.LENGTH_LONG).show();
                                break;
                            case Activity.RESULT_CANCELED:
                                Toast.makeText(thisContext, "Key Not Delivered: Canceled.",
                                        Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                }, new IntentFilter("DELIVERED"));

        //send the message and set receivers
        sms.sendTextMessage(testContact.getNumber(), null, "Please Open This In Cloaked:" + message, sentPending, deliveredPending);
        // display notification of message sent
        Toast.makeText(thisContext, "Sent", Toast.LENGTH_SHORT).show();

    }

    /**
     * keyStringToHex takes in a key in byte form and converts it to a hexadecimal string.
     */
    public static String keyStringToHex(byte[] ba) {
        StringBuilder str = new StringBuilder();
        for (byte aBa : ba) str.append(String.format("%x", aBa));
        return str.toString();
    }

    /**
     * hexKeyToString takes in a hexadecimal string and converts it to its original string value.
     *
     * @param hex A hexadecimal representation of a String.
     */
    public static String hexKeyToString(String hex) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            str.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
        }
        return str.toString();
    }

}
