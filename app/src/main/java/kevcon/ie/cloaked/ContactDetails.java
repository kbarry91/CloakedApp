package kevcon.ie.cloaked;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * <h1>ContactDetails</h1>
 * This class is used to display to the user the contacts details
 * in which they selected.
 *
 * @author Conor Raftery
 * @since 08/03/2018
 */

public class ContactDetails extends Activity {

    //Constants & variables
    Contacts ContactDetails;
    private String mContactName,mContactNumber, mKey;
    boolean mIsKeySet;
    TextView details_name,details_number;
    Button backButton;

/**
 * <h2>onCreate</h2>
 * This class is loaded from ContactsMainActivity.java when a
 *  the intent for this class is called. Its the back end code
 *  for displaying the contacts details.
 */
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the layout
        setContentView(R.layout.contact_details);

        //Get reference to the back button and text boxes
        backButton= findViewById(R.id.backButton);
        details_name= findViewById(R.id.details_name);
        details_number= findViewById(R.id.details_number);

        //Create empty intent
        Intent intent;
        intent=getIntent();
        //Use intent to get contact details from ContactsViewAdapater.java
        ContactDetails = (Contacts) intent.getSerializableExtra("details");

        //Populate local variables with contact details
        mContactNumber=ContactDetails.getNumber();
        mContactName=ContactDetails.getName();
        mKey = ContactDetails.getKey();
        mIsKeySet = ContactDetails.getKeySet();

        //Display contact details into the textboxes
        details_name.setText(mContactName);
        details_number.setText(mContactNumber);

        //Add listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}