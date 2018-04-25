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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.AlertDialog.Builder;
import static kevcon.ie.cloaked.Encryption.DecryptMessage;

/**
 * <h1>SendMessageActivity</h1>
 * SendMessageActivity is the main message page.
 * Controls the sending and loading of sms messages
 *
 * @author kevin barry
 * @since 25/4/2018
 */
public class SendMessageActivity extends AppCompatActivity {
    // Tag for debugging
    private static final String TAG = "SendMessageActivity";
    private static final String READMSG = "CREATE MESSAGE LIST:";

    // define UI Components
    private EditText user_message;
    private Button send_button;

    private RecyclerView messageRec;
    private MessageViewAdapter messageAdp;

    private List<Message> listMessageData;

    private Contacts curContact;

    private String verifyEnteredKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        // receive information through intent regarding the contact
        Intent intent;
        intent = getIntent();
        this.curContact = (Contacts) intent.getSerializableExtra("send_msg");

        //populate message list first
        this.listMessageData = createMessageList(curContact.getNumber());

        Runnable lookValidKey = new Runnable() {
            @Override
            public void run() {
                // if key is not sent prompt a pop up to set key
                if (!curContact.getKeySet()) {
                    Toast.makeText(getBaseContext(), "No key Set",
                            Toast.LENGTH_LONG).show();
                    KeyController kc = new KeyController();
                    kc.setNewKey(curContact, SendMessageActivity.this, "No Key Set,Set Cloaked Key");

                }
            }
        };

        setContentView(R.layout.activity_send_message);

        // set up a toolbar with contacts name and back button to parent activity
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (curContact.getName() != null) getSupportActionBar().setTitle(curContact.getName());


        //assign Recycle view to view
        messageRec = findViewById(R.id.recycler_view_inbox_list);
        messageAdp = new MessageViewAdapter(this, listMessageData, curContact);
        messageRec.setLayoutManager(new LinearLayoutManager(this));
        messageRec.setAdapter(messageAdp);
        
        //assign user_message and button to view
        user_message = findViewById(R.id.edit_message);
        send_button = findViewById(R.id.button_sms_send);

        String newKeySet;
        Message messageCheck = null;
        boolean found = false;

        //if the list is not empty
        if (listMessageData.size() > 0) {
            //must be able to break out of outer loop
            outerloop:
            for (int i = listMessageData.size() - 1; i >= 0; i--) {
                messageCheck = listMessageData.get(i);
                if (messageCheck.getMessage().contains("Please Open This In Cloaked:")) {
                    found = true;
                    break outerloop;

                }//if pattern is found

            }// for

            // decipher the key from the message
            if (messageCheck != null && found && messageCheck.getType() == 1) {
                KeyController newKeySetter = new KeyController();

                newKeySet = newKeySetter.unScrambleKey(messageCheck.getMessage());
                if (!newKeySet.equals(curContact.getKey())) {
                    // reset the key for this contact
                    if (newKeySetter.resetKey(newKeySet, curContact, this, lookValidKey)) {
                        curContact.setKey(newKeySet);
                        curContact.setKeySet(true);
                    }
                }//if key set not equal new key
            }
            //lookValidKey.run();
        } else {
            lookValidKey.run();
        }

        // assign on click listener to button
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to send a message first an encryption key must be established
                if (curContact.getKeySet()) {
                    verifyKey();

                } else {
                    //   KeyController.setNewKey(curContact,getBaseContext());
                    Toast.makeText(getBaseContext(), "No key Set",
                            Toast.LENGTH_LONG).show();
                    KeyController kc = new KeyController();
                    kc.setNewKey(curContact, SendMessageActivity.this, "No Key Set,Set Cloaked Key");

                }
            }//on click
        });


    }

    /**
     * createMessageList populates a list of messages from the devices storage.
     *
     * @param userNumber The number for the contact to retrieve messages.
     * @return messageList a list of messages.
     */
    private List<Message> createMessageList(String userNumber) {
        List<Message> messageList = new ArrayList<>();
        String numberString = "address='" + userNumber + "'";
        Message message;
        String number = "";
        String body = "";
        String dateTime = "";
        String type = "";

        StringBuilder msgInfo = new StringBuilder();
        //get read access to message data
        Cursor cur = getContentResolver().query(Uri.parse("content://sms/"), null, numberString, null, null);

        // step through each result
        if (cur != null && cur.moveToFirst()) {
            do {

                for (int i = 0; i < cur.getColumnCount(); i++) {
                    msgInfo.append(" ").append(cur.getColumnName(i)).append(":").append(cur.getString(i)); /// KEEP THIS LINE FOR DEBUG
                    number = cur.getString(cur.getColumnIndexOrThrow("address"));
                    body = cur.getString(cur.getColumnIndexOrThrow("body"));
                    dateTime = cur.getString(cur.getColumnIndexOrThrow("date"));
                    type = cur.getString(cur.getColumnIndexOrThrow("type"));
                }

                // Convert type to int.
                int messageType = 0;
                if (type.equals("2")) {
                    messageType = 2;
                } else if (type.equals("1")) {
                    messageType = 1;
                }

                //create a message object
                message = new Message(body, number, dateTime, messageType);

                //add message to message list.
                messageList.add(message);

                //DEBUG
                Log.d(READMSG, "DEBUG: " + message.toString());

            } while (cur.moveToNext());
        } else {
            //DEBUG
            Log.d(READMSG, " No sms from this contact");
            Toast.makeText(this, " No messages to view", Toast.LENGTH_SHORT).show();
        }

        // cursor must be closed and recycled
        if (cur != null) {
            cur.close();
        }

        //reverse the list to display messages in order
        Collections.reverse(messageList);
        return messageList;
    }

    /**
     * keyChecker checks if key is correct, if true sends a message
     *
     * @param entKey the user entered key.
     */
    private void keyChecker(String entKey) {
        if (entKey.equals(curContact.getKey())) {
            Log.d("Valid key", "key confirmed");
            sendSms(curContact);

        } else {
            Toast.makeText(getBaseContext(), "Invalid Cloaked key",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * verifyKey displays a pop up dialog to allow user to enter a key
     */
    private void verifyKey() {
        verifyEnteredKey = "";
        Builder builder = new Builder(SendMessageActivity.this);
        builder.setTitle("Cloaked Key");

        // Inflate the key dialog view.
        View viewInflated = LayoutInflater.from(SendMessageActivity.this).inflate(R.layout.key_entry_dialog, (ViewGroup) findViewById(android.R.id.content), false);
        // Set up the input.
        final EditText input = viewInflated.findViewById(R.id.input);
        // Specify the type of input expected.
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                verifyEnteredKey = input.getText().toString();

                // verify if key is valid.
                keyChecker(verifyEnteredKey);

                dialog.dismiss();

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

    /**
     * sendSms sends a message to the specified contact
     *
     * @param curContact the contact to send message to.
     */
    private void sendSms(Contacts curContact) {
        // a test message to try encryption
        String testMessage = "Sent From Cloaked:" + user_message.getText().toString();

        String cloakedMessage = DecryptMessage(testMessage, curContact.getKey());

        // DEBUG
        Log.d(TAG, "Attempting to send sms");

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

        // send the message and set receivers.
        sms.sendTextMessage(curContact.getNumber(), null, "Sent From Cloaked:" + cloakedMessage, sentPending, deliveredPending);

        // display notification of message sent
        Toast.makeText(this, "Sent", Toast.LENGTH_SHORT).show();

        // create a new message object
        Message newMessage = new Message("Sent From Cloaked:" + cloakedMessage, curContact.getNumber(), Long.toString(System.currentTimeMillis()), 2);

        // add the new message to the list
        listMessageData.add(newMessage);
        // must notify adapter of changes to update message list
        messageAdp.notifyItemInserted(listMessageData.size() - 1);


        //reset text field
        user_message.setText(null);
        user_message.setHint(R.string.send_message_hint);


    }


}