package kevcon.ie.cloaked;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by c-raf on 08/03/2018.
 */

public class Data extends Activity {

    EditText editName, editNumber;
    String key = null;
    boolean isKeySet = false;
    Button saveButton;
    ContactsHelperDB myDb;

    //ContactsHelperDB myDb2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data);

        //initlise database
        myDb = new ContactsHelperDB(this);
        // myDb2 = new ContactsHelperDB(this);
        //  myDb2.close();
        // Log.d("IN PAAAAAGE", "MYDBJUST CLOSED------------- ");
        editName = findViewById(R.id.editName);
        editNumber = findViewById(R.id.editNumber);

        saveButton = findViewById(R.id.save);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get values from user
                String contactName = editName.getText().toString();
                String contactNumber = editNumber.getText().toString();

                // if the entered number does not contain country code must edit the number
                if (!contactNumber.startsWith("+")) {
                    //test getting country code
                    String cc = GetCountryZipCode();
                    contactNumber = Utils.addCountryCode(cc, contactNumber);

                    Log.d("COUNTRY CHECK", "DEBUG number  is : " + contactNumber);
                }

                // create new contact object and add to database
                Contacts newContact = new Contacts(contactName, contactNumber);


                if (myDb.insertContact(newContact)) {
                    myDb.close();
                    Log.d("ADD CONTACT", " contact added");

                } else {

                    Log.d("ADD CONTACT", " contact add failed");
                    myDb.close();
                }


                ;

                Intent intent5 = new Intent(Data.this, ContactsMainActivity.class);


                finish();
            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}
