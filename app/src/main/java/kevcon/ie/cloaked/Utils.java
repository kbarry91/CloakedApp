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
 * Utils is a class to hold generic methods
 *
 * @author kevin barry
 * @since 19/3/2018
 */
public class Utils {
    // private static final String TAG = "MESSAGE_SEND";
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

    /**
     * addCountryCode removes the 0 from the number and appends country formating
     */
    public static String addCountryCode(String countryCode, String number) {

        String newNumber = number.replaceFirst("0", "+" + countryCode);
        return newNumber;
    }

    //https://www.codeproject.com/Articles/1044639/Android-Java-How-To-Send-SMS-Receive-SMS-Get-SMS-M
    public static void sendMessage(Contacts testContact, String message, Context ctx) {


        // Log.d(TAG, "Attempting to send sms");
        //  String strMessage = "sent from cloaked app: " + user_message.getText().toString();

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
        // sms.sendTextMessage(userNumber, null, strMessage, sentPending, deliveredPending);
        sms.sendTextMessage(testContact.getNumber(), null, "Please Open This In Cloaked:" + message, sentPending, deliveredPending);
        // display notification of message sent
        Toast.makeText(thisContext, "Sent", Toast.LENGTH_SHORT).show();

        // create a new message object
        //Message newMessage = new Message("Sent From Cloaked:" + message, testContact.getNumber(), Long.toString(System.currentTimeMillis()), 2);

        // add the new message to the list
        // listMessageData.add(newMessage);
        // must notify adapter of changes to update message list
        // messageAdp.notifyItemInserted(listMessageData.size() - 1);


        //reset text field
        //  user_message.setText(null);
        // user_message.setHint(R.string.send_message_hint);


    }

    /*
       * Method to get country code for a number will be moved to add contact*/
    /*
    public static String GetCountryZipCode(Context ctx) {

        String CountryID;
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) this.getApplicationContext(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = this.getResources().getStringArray(R.array.CountryCodes);

        //optimised for loop
        for (int i = 0, rlLength = rl.length; i < rlLength; i++) {
            String aRl = rl[i];
            String[] g = aRl.split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return CountryZipCode;
    }

*/

    /*
    * keyStringToHex takes in a key in byte form and converts it to a hexadecimal string
    *
    * @Returns the hex value of the byte array
    * */
    public static String keyStringToHex(byte[] ba) {
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < ba.length; i++)
               str.append(String.format("%x", ba[i]));
        return str.toString();
    }

    /*
     * hexKeyToString takes in a hexadecimal string and converts it to its original string value
     *
     * @Returns the string in normal form
     * */
    public static String hexKeyToString(String hex) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < hex.length(); i+=2) {
            str.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
        }
        return str.toString();
    }

}
