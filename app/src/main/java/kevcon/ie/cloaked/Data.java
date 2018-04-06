package kevcon.ie.cloaked;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

/**
 * Created by c-raf on 08/03/2018.
 */

public class Data extends Activity {

    EditText editName, editNumber;
    String key = null;
    boolean isKeySet = false;
    Button saveButton;
    ContactsHelperDB myDb;

    //global Variables to send message
    private BroadcastReceiver sendBroadcastReceiver;
    private BroadcastReceiver deliveryBroadcastReceiver;
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";

    //ContactsHelperDB myDb2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data);
        // set recievers when page loaded
        sendBroadcastReceiver = new BroadcastReceiver() {

            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "REQUEST Sent", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        deliveryBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "REQUEST Delivered", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "REQUEST not delivered", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        registerReceiver(deliveryBroadcastReceiver, new IntentFilter(DELIVERED));
        registerReceiver(sendBroadcastReceiver, new IntentFilter(SENT));

        //initlise database
        myDb = new ContactsHelperDB(this);
        // myDb2 = new ContactsHelperDB(this);
        //  myDb2.close();
        // Log.d("IN PAAAAAGE", "MYDBJUST CLOSED------------- ");
        editName = findViewById(R.id.editName);
        editNumber = findViewById(R.id.editNumber);

        saveButton = findViewById(R.id.save);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get values from user
                String contactName = editName.getText().toString();
                String contactNumber = editNumber.getText().toString();

                // if the entered number does not contain country code must edit the number



                List<Contacts> contactList;
                contactList = myDb.getAllContacts();
                int dontAdd = 0;

                for (Contacts con : contactList) {
                    Log.e("Nums",  con.getNumber()+ " : " + contactNumber);
                    if(con.getNumber().contains(contactNumber)){
                       Toast.makeText(getApplicationContext(), "Number already exists", Toast.LENGTH_LONG).show();
                        Log.e("Num Exists", "Number is already in db : " + contactNumber);
                        dontAdd = 1;
                        break;
                    }
                }

                if(dontAdd != 1) {

                    if (!contactNumber.startsWith("+")) {
                        //test getting country code
                        String cc = GetCountryZipCode();
                        contactNumber = Utils.addCountryCode(cc, contactNumber);

                        Log.d("COUNTRY CHECK", "DEBUG number  is : " + contactNumber);
                    }


                    // create new contact object and add to database
                    Contacts newContact = new Contacts(contactName, contactNumber);


                    if (myDb.insertContact(newContact)) {

                    String initialMsg = "I would like to start a convo on cloaked";

                    sendRequestSMS(newContact, initialMsg);
                    myDb.close();
                    Log.d("ADD CONTACT", " contact added");
                } else {

                        Log.d("ADD CONTACT", " contact add failed");
                        myDb.close();
                    }

                }//Close dont add if


                Intent intent5 = new Intent(Data.this, ContactsMainActivity.class);


                finish();
            }
        });

    }

    /*
     * Method to get country code for a number will be moved to add contact*/
    public String GetCountryZipCode() {

        String CountryID;
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    public void sendRequestSMS(final Contacts testContact, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(testContact.getNumber(), null, "Sent From Cloaked:" + message, sentPI, deliveredPI);
    }

    /*
        //https://www.codeproject.com/Articles/1044639/Android-Java-How-To-Send-SMS-Receive-SMS-Get-SMS-M
        public void sendInitialMsg(final Contacts testContact, String txtMsg) {

            SmsManager mySms = SmsManager.getDefault();

            Context curContext = Data.this;

            // must create intents to Check if sms is sent and delivered
            PendingIntent sentPending = PendingIntent.getBroadcast(curContext,
                    0, new Intent("SENT"), 0);

            // receiver intent to return result of  Broadcast
            curContext.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:

                            Toast.makeText(getBaseContext(), "Sending request to " + testContact.getName() + " to download Cloaked",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(getBaseContext(), "request Not Sent: Generic failure.",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(getBaseContext(), "SMS Not Sent: No service ",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(getBaseContext(), "Not Sent: Null PDU.",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(getBaseContext(), "Not Sent: Ensure Airplane mode is disabled",
                                    Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }, new IntentFilter("SENT"));

            PendingIntent deliveredPending = PendingIntent.getBroadcast(curContext,
                    0, new Intent("DELIVERED"), 0);

            curContext.registerReceiver(
                    new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context arg0, Intent arg1) {
                            switch (getResultCode()) {
                                case Activity.RESULT_OK:
                                    Toast.makeText(getBaseContext(), "Delivered.",
                                            Toast.LENGTH_LONG).show();
                                    break;
                                case Activity.RESULT_CANCELED:
                                    Toast.makeText(getBaseContext(), "Not Delivered: Canceled.",
                                            Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    }, new IntentFilter("DELIVERED"));

            mySms.sendTextMessage(testContact.getNumber(), null, "Sent From Cloaked:" + txtMsg, sentPending, deliveredPending);

            unregisterReceiver(deliveredPending);


        }
        */
    @Override
    protected void onStop() {
        unregisterReceiver(sendBroadcastReceiver);
        unregisterReceiver(deliveryBroadcastReceiver);
        super.onStop();
    }
}
