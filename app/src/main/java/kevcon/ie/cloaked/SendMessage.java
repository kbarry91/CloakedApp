package kevcon.ie.cloaked;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author kevin barry
 */
public class SendMessage extends Activity {
    // Tag for debugging
    private static final String TAG = "SendMessage";

    private EditText user_message;
    private Button send_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: opened");
        setContentView(R.layout.activity_send_message);

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

        sms.sendTextMessage(strPhone, null, strMessage, null, null);

        // display notification of message sent
        Toast.makeText(this, "Sent.", Toast.LENGTH_SHORT).show();
    }

}
