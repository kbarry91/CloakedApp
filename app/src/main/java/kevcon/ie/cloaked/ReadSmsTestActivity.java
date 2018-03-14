package kevcon.ie.cloaked;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

// a test class to work on reading sms
public class ReadSmsTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_sms_test);
        readSmsTest();

    }

    public void readSmsTest() {
        Cursor cur = getContentResolver().query(Uri.parse("content://sms/"), null, null, null, null);

        if (cur.moveToFirst()) { /* false = no sms */
            do {
                String msgInfo = "";

                for (int i = 0; i < cur.getColumnCount(); i++) {
                    msgInfo += " " + cur.getColumnName(i) + ":" + cur.getString(i);
                }

                Toast.makeText(this, msgInfo, Toast.LENGTH_SHORT).show();
            } while (cur.moveToNext());
        }
    }
}
