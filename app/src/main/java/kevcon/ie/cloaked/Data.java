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
 * <h1>Data</h1>
 * This class is used to populate and manipulate the contact
 *  and its details, whilst adding the contacts to the database.
 *
 * @author Conor Raftery
 * @since 08/03/2018
 */
public class Data extends Activity {

    //Used for referencing the textboxes and button
    EditText editName, editNumber;
    Button saveButton;
    //Create instance of DB
    ContactsHelperDB myDb;

    //global Variables to send message
    private BroadcastReceiver sendBroadcastReceiver;
    private BroadcastReceiver deliveryBroadcastReceiver;
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";

    /**
     * <h2>onCreate</h2>
     * onCreate is ran when Data is opened. It contains listeners, and
     * also sets certain objects and variables.
     *
     * @author kevin barry
     * @since 25/4/2018
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Sets the layout
        setContentView(R.layout.data);

        // set receivers when page loaded
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

        // set broadcast when page loaded
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
        //Assigns the receiver and broadcast
        registerReceiver(deliveryBroadcastReceiver, new IntentFilter(DELIVERED));
        registerReceiver(sendBroadcastReceiver, new IntentFilter(SENT));


        //initialize database
        myDb = new ContactsHelperDB(this);

        // bind elements to variables
        editName = findViewById(R.id.editName);
        editNumber = findViewById(R.id.editNumber);
        saveButton = findViewById(R.id.save);

        // set listener for add contact button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get values from user
                String contactName = editName.getText().toString();
                String contactNumber = editNumber.getText().toString();
                String cc = GetCountryZipCode();
                String contactCheck = Utils.addCountryCode(cc, contactNumber);

                //Create a list of all current contacts (Used for Debugging)
                List<Contacts> contactList = myDb.getAllContacts();

                /*
                Create an initialize a boolean for if there is
                 no contact with the number entered by the user
                */
                boolean canAdd = true;

                //DEBUGGING - to check what contacts are already created
                for (Contacts con : contactList) {
                    Log.e("DEBUG CONTACT LIST", con.getNumber());
                }

                //Loop through every contact in Contact list
                for (Contacts con : contactList) {
                    Log.e("Nums", con.getNumber() + " : " + contactNumber);
                    //If number already exists
                    if (con.getNumber().contains(contactCheck)) {
                        //toast the user
                        Toast.makeText(getApplicationContext(), "Number already exists", Toast.LENGTH_LONG).show();
                        //DEBUGGING
                        Log.e("Num Exists", "Number is already in db : " + contactNumber);
                        //Set boolean to false, contact cannot be created
                        canAdd = false;
                        break;
                    }
                }

                // if number not in database
                if (canAdd) {
                    String editedNumber = "";
                    // if the entered number does not contain country code must edit the number
                    if (!contactNumber.startsWith("+")) {
                        //test getting country code
                        editedNumber = Utils.addCountryCode(cc, contactNumber);
                    }


                    // create new contact object and add to database
                    Contacts newContact = new Contacts(contactName, editedNumber);


                    //If contact is created
                    if (myDb.insertContact(newContact)) {

                        String initialMsg = "I would encrypt our messages, Please download Cloaked and add me as a contact!";

                        //Send an sms to contact number with above message
                        sendRequestSMS(newContact, initialMsg);
                        //Close DB instance
                        myDb.close();

                    } else {
                        //If contact cannot be created, do nothing

                        //Close DB instance
                        myDb.close();
                    }

                }//Close dont add if

                finish();
            }
        });

    }

    /**
     * <h2>GetCountryZipCode</h2>
     * Method to get country code for a number.
     */
    public String GetCountryZipCode() {

        String CountryID;
        String CountryZipCode = "";

        //Create instance of TelephonyManager
        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso from current country
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = this.getResources().getStringArray(R.array.CountryCodes);

        //optimised for loop for adding country code
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

    /**
     * <h2>sendRequestSMS</h2>
     * This message is called to send an sms to a selected contact.
     */
    public void sendRequestSMS(final Contacts testContact, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(testContact.getNumber(), null, "Sent From Cloaked:" + message, sentPI, deliveredPI);
    }

//Stop to broadcast and receiver
    @Override
    protected void onStop() {
        unregisterReceiver(sendBroadcastReceiver);
        unregisterReceiver(deliveryBroadcastReceiver);
        super.onStop();
    }
}
