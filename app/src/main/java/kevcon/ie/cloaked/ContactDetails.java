package kevcon.ie.cloaked;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by c-raf on 08/03/2018.
 */

public class ContactDetails extends Activity {

    Contacts ContactDetails;
    private String mContactName,mContactNumber, mKey;
    boolean mIsKeySet;
    TextView details_name,details_number;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_details);

        backButton= findViewById(R.id.backButton);

        details_name= findViewById(R.id.details_name);
        details_number= findViewById(R.id.details_number);

        Intent intent9;
        intent9=getIntent();
        ContactDetails = (Contacts) intent9.getSerializableExtra("details");

        mContactNumber=ContactDetails.getNumber();
        mContactName=ContactDetails.getName();
        mKey = ContactDetails.getKey();
        mIsKeySet = ContactDetails.getKeySet();



        details_name.setText(mContactName);
        details_number.setText(mContactNumber);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}