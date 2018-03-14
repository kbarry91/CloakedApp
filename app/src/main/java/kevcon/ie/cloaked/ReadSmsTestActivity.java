package kevcon.ie.cloaked;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

// a test class to work on reading sms
public class ReadSmsTestActivity extends AppCompatActivity {
    private static final String TAG = "READMSG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_sms_test);
        readSmsTest();

    }

    // a test method to play with reading messages
    public void readSmsTest() {
        String numberString = "address='+353858443049'";
        Cursor cur = getContentResolver().query(Uri.parse("content://sms/"), null, numberString, null, null);

        if (cur.moveToFirst()) { /* false = no sms */
            do {
                String msgInfo = "";

                for (int i = 0; i < cur.getColumnCount(); i++) {
                    msgInfo += " " + cur.getColumnName(i) + ":" + cur.getString(i); /// gets full message info
                    // String Number = cur.getString(cur.getColumnIndexOrThrow("address")).toString();
                    // String Body = cur.getString(cur.getColumnIndexOrThrow("body")).toString();
                    // msgInfo += Number + " " + Body;
                }
                //DEBUG
                Log.d(TAG, "DEBUG: " + msgInfo);
                Toast.makeText(this, msgInfo, Toast.LENGTH_SHORT).show();
            } while (cur.moveToNext());
        } else {
            //DEBUG
            Log.d(TAG, " No sms from this contact");
            Toast.makeText(this, " No sms from this contact", Toast.LENGTH_SHORT).show();
        }
    }
}
