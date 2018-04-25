package kevcon.ie.cloaked;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * <h1>ReceiveMessage</h1>
 * ReceiveMessage is a class that accesses a received message.
 * extends BroadcastReceiver to intercept broadcast intents.
 *
 * @author kevin barry
 * @since 25/4/2018
 */
public class ReceiveMessage extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();

        if (intentExtras != null) {
            // Get Messages.
            Object[] sms = (Object[]) intentExtras.get("pdus");

            for (int i = 0; i < sms.length; ++i) {
                // Parse Each Message.
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String phone = smsMessage.getOriginatingAddress();
                String message = smsMessage.getMessageBody();

                // display toast notification to user only if a Cloaked message.
                if (message.contains("Cloaked")) {
                    Toast.makeText(context, phone + ": " + message, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
