package kevcon.ie.cloaked;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by c-raf on 08/03/2018.
 */

public class Data extends Activity {

    EditText editName,editNumber;
    String key = null;
    boolean isKeySet = false;
    Button saveButton;
    ContactsHelperDB myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data);

        //initlise database
        myDb = new ContactsHelperDB(this);

        editName= findViewById(R.id.editName);
        editNumber= findViewById(R.id.editNumber);

        saveButton = findViewById(R.id.save);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get values from user
                String contactName = editName.getText().toString();
                String contactNumber = editNumber.getText().toString();

                // create new contact object and add to database
                Contacts newContact = new Contacts(contactName, contactNumber);
                myDb.insertContact(newContact);

                Intent intent5=new Intent(Data.this,ContactsMainActivity.class);


                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}
