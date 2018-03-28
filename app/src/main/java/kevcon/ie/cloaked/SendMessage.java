package kevcon.ie.cloaked;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.app.AlertDialog.Builder;

/**
 * <h1>SendMessage</h1>
 * SendMessage controls the sending and loading of sms messages
 *
 * @author kevin barry
 */
public class SendMessage extends AppCompatActivity {
    // Tag for debugging
    private static final String TAG = "SendMessage";
    private static final String READMSG = "CREATE MESSAGE LIST:";

    // define UI Components
    private EditText user_message;
    private Button send_button;

    private RecyclerView messageRec;
    private MessageViewAdapter messageAdp;

    // initialize contact
    Contacts contact;
    Contacts testContact;


    private String verifyEnteredKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        // receive information through intent regarding the contact
        Intent intent;
        intent = getIntent();
        this.testContact = (Contacts) intent.getSerializableExtra("send_msg");

        //test getting country code
        String cc = GetCountryZipCode();
        Log.d(READMSG, "DEBUG Country code is : " + cc);


        // may have to move to an adapter for dynamic binding!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //populate message list first
        List<Message> listMessageData = createMessageList(testContact.getNumber());

        //test list is populating
        for (Message msg : listMessageData) {
            Log.d(READMSG, "DEBUG: " + msg.toString());
        }

        Log.d(TAG, "onCreate: opened");
        setContentView(R.layout.activity_send_message);

        // set up a toolbar with contacts name and back button to parent activity
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setTitle(testContact.getName());

        //assign Recycle view to view
        messageRec = findViewById(R.id.recycler_view_inbox_list);
        messageAdp = new MessageViewAdapter(this, listMessageData);
        messageRec.setLayoutManager(new LinearLayoutManager(this));
        messageRec.setAdapter(messageAdp);

        //getSupportActionBar().setHomeButtonEnabled(true);
        //assign user_message and button to view

        user_message = findViewById(R.id.edit_message);
        send_button = findViewById(R.id.button_sms_send);

        // assign on click listener to button
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to send a message first an encryption key must be established
                if (testContact.getKeySet()) {
                    verifyKey();
                    /*
                    if (keyChecker(verifyEnteredKey)) {
                        Log.d("Valid key", "!!!!!!!!!!!!!!!!!");
                        sendSms(testContact);
                    } else {
                        Toast.makeText(getBaseContext(), "Invalid  Key",
                                Toast.LENGTH_LONG).show();
                    }
                    Log.d("After verifyInvalid key", "!!!!!!!!!!!!!!!!!");

                    Log.d("OUTSDIDE KEY CEHCK", "outside");
                    */
                } else {
                    Toast.makeText(getBaseContext(), "No key Set",
                            Toast.LENGTH_LONG).show();
                }
            }//on click
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

    // a method to populate a list of messages
    public List<Message> createMessageList(String userNumber) {
        List<Message> messageList = new ArrayList<>();
        String numberString = "address='" + userNumber + "'";
        Message message;
        String number = "";
        String body = "";
        String dateTime = "";
        String type = "";

        //get read access to message data
        Cursor cur = getContentResolver().query(Uri.parse("content://sms/"), null, numberString, null, null);

        // step through each result
        if (cur != null && cur.moveToFirst()) {
            do {

                for (int i = 0; i < cur.getColumnCount(); i++) {
                    //   msgInfo += " " + cur.getColumnName(i) + ":" + cur.getString(i); /// KEEP THIS LINE FOR DEBUG
                    number = cur.getString(cur.getColumnIndexOrThrow("address"));
                    body = cur.getString(cur.getColumnIndexOrThrow("body"));
                    dateTime = cur.getString(cur.getColumnIndexOrThrow("date"));
                    type = cur.getString(cur.getColumnIndexOrThrow("type"));
                }
                //convert type to int
                int messageType = 0;
                if (type.equals("2")) {
                    messageType = 2;
                } else if (type.equals("1")) {
                    messageType = 1;
                }
                //create a message object
                message = new Message(body, number, dateTime, messageType);

                //add message to message list
                messageList.add(message);

                //DEBUG
                //Log.d(READMSG, "DEBUG: " + message.toString());

            } while (cur.moveToNext());
        } else {
            //DEBUG
            Log.d(READMSG, " No sms from this contact");
            Toast.makeText(this, " No messages to view", Toast.LENGTH_SHORT).show();
        }

        // cursor must be closed and recycled
        cur.close();
        return messageList;
    }

    public void keyChecker(String entKey) {
        if (entKey.equals(testContact.getKey())) {
            Log.d("Valid key", "key confirmed");
            sendSms(testContact);

        }
        Toast.makeText(getBaseContext(), "Invalid Cloaked key",
                Toast.LENGTH_LONG).show();

    }

    public void verifyKey() {
        verifyEnteredKey = "";
        Builder builder = new Builder(SendMessage.this);
        builder.setTitle("Cloaked Key");
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(SendMessage.this).inflate(R.layout.key_entry_dialog, (ViewGroup) findViewById(android.R.id.content), false);
        // Set up the input
        final EditText input = viewInflated.findViewById(R.id.input);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                verifyEnteredKey = input.getText().toString();
                Log.d("ENTERED KEYY in dialog", verifyEnteredKey);
                // if(verifyEnteredKey.equals(testContact.getKey())){
                //    return true;
                // }
                keyChecker(verifyEnteredKey);
                Log.d("Before dialog dismiss", verifyEnteredKey);
                dialog.dismiss();
                Log.d("afteer dialog dismiss", verifyEnteredKey);

            }

        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        Log.d("Before builder show", verifyEnteredKey);
        builder.show();
        Log.d("After builder show", verifyEnteredKey);


    }

    //https://www.codeproject.com/Articles/1044639/Android-Java-How-To-Send-SMS-Receive-SMS-Get-SMS-M
    public void sendSms(Contacts testContact) {
// a test message to try encryption
        String testMessage = user_message.getText().toString();
        StringBuilder cloakedMessage = new StringBuilder();

        //to send a message first an encryption key must be established

        int pos = 0;
        int sentLength = testMessage.length();
        while (pos < sentLength) {// while end of sentence not reached
            char letter = Encryption.encrypt(Character.toUpperCase(testMessage.charAt(pos)), testContact.getKey().toUpperCase());
            cloakedMessage.append(letter);
            pos++;
        } // while

        Log.d(TAG, "Attempting to send sms");
        String strMessage = "sent from cloaked app: " + user_message.getText().toString();

        SmsManager sms = SmsManager.getDefault();

        Context curContext = this.getApplicationContext();

        // must create intents to Check if sms is sent and delivered
        PendingIntent sentPending = PendingIntent.getBroadcast(curContext,
                0, new Intent("SENT"), 0);

        // receiver intent to return result of  Broadcast
        curContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS Sent.",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "SMS Not Sent: Generic failure.",
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

        //send the message and set receivers
        // sms.sendTextMessage(userNumber, null, strMessage, sentPending, deliveredPending);
        sms.sendTextMessage(testContact.getNumber(), null, "Sent From Cloaked:" + cloakedMessage.toString(), sentPending, deliveredPending);
        // display notification of message sent
        Toast.makeText(this, "Sent", Toast.LENGTH_SHORT).show();

        // must notify adapter of changes to update message list
        messageAdp.notifyDataSetChanged();

        //reset text field
        user_message.setText(null);
        user_message.setHint(R.string.send_message_hint);

        //restart the activity
        finish();
        startActivity(getIntent());
    }

}
