package kevcon.ie.cloaked;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Intel Build on 09/03/2018.
 */

public class MessageListActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    //  private MessageListAdapter mMessageAdapter;
    private EditText edittext_chatbox;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);


        // mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        // mMessageAdapter = new MessageListAdapter(this, messageList);//// commented out until messageList implemented
        //  mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));

        // assign button and message to layout
        Button send_message = (Button) findViewById(R.id.button_chatbox_send);
        EditText chat_Message = (EditText) findViewById(R.id.edittext_chatbox);

        // declare a listener object for clicking send button
        send_message.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Send SMS", "button_chatbox_send clicked");
                sendSms();
            }
        });
    }

    public void sendSms() {
        Log.d("Send SMS", "Attempting to send sms");
        String strPhone = "0857453822";

        String strMessage = "Hello this is a debug text";
        // String strMessage = edittext_chatbox.getText().toString();
        SmsManager sms = SmsManager.getDefault();

/* ---- Preparing Intents To Check While Sms Sent & Delivered ---- */

        Context curContext = this.getApplicationContext();

        PendingIntent sentPending = PendingIntent.getBroadcast(curContext,
                0, new Intent("SENT"), 0);

        curContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "Sent.",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Not Sent: Generic failure.",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "Not Sent: No service (possibly, no SIM-card).",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Not Sent: Null PDU.",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Not Sent: Radio off (possibly, Airplane mode enabled in Settings).",
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

/* --------------------------------------------------------------- */

        sms.sendTextMessage(strPhone, null, strMessage,
                sentPending, deliveredPending);
    }


}
