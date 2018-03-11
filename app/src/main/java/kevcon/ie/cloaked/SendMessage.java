package kevcon.ie.cloaked;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * @author kevin barry
 */
public class SendMessage extends AppCompatActivity {
    // Tag for debugging
    private static final String TAG = "SendMessage";

    private EditText user_message;
    private Button send_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: opened");
        setContentView(R.layout.activity_send_message);

        // set up a toolbar and back button to parent activity
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //getSupportActionBar().setHomeButtonEnabled(true);
        //assign user_message and button to view
        user_message = findViewById(R.id.edit_message);
        send_button = findViewById(R.id.button_sms_send);

        // assign on click listener to button
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSms();
            }
        });
    }

    public void sendSms() {
        Log.d(TAG, "Attempting to send sms");
        String strPhone = "0857453822";
        String strMessage = user_message.getText().toString();
        //String strMessage = "Hello this is a debug text";
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

        sms.sendTextMessage(strPhone, null, strMessage, sentPending, deliveredPending);

        // display notification of message sent
        Toast.makeText(this, "Sent.", Toast.LENGTH_SHORT).show();
    }

}
