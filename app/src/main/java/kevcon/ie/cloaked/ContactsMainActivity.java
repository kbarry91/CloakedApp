package kevcon.ie.cloaked;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by c-raf on 08/03/2018.
 */

public class ContactsMainActivity extends Activity {
    Button contactAddButton;
    ListView listContacts;

    private RecyclerView contactRec;
    private ContactsViewAdapter contactAdapter;

    //ContactsAdapter contactAdapter;
    Contacts contacts;

    //database variables
    ContactsHelperDB myDb;
    List<Contacts> contactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_activity_main);

        // inilise database
        myDb = new ContactsHelperDB(this);
        contactList = myDb.getAllContacts();
        myDb.close();


        contactAddButton = findViewById(R.id.button_add_contact);


        // DEBUG TO CHECK DATABASE
        for (Contacts con : contactList) {
            Log.e("DATACHECK", con.toString());
        }


        //add button listener
        contactAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ContactsMainActivity.this, Data.class);
                startActivityForResult(intent, 1);


            }
        });


       // arrayListContact=new ArrayList<Contacts>();

        //listContacts = findViewById(R.id.listView);


       //contactAdapter=new ContactsAdapter(ContactsMainActivity.this,contactList);

        //listContacts.setAdapter(contactAdapter);


        contactAdapter = new ContactsViewAdapter(this, contactList);

        contactRec = findViewById(R.id.recycler_view_contact_list);
        contactAdapter = new ContactsViewAdapter(this, contactList);
        contactRec.setLayoutManager(new LinearLayoutManager(this));
        contactRec.setAdapter(contactAdapter);


        //add button listener
        contactAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ContactsMainActivity.this, Data.class);
                startActivityForResult(intent, 1);


            }
        });

    }






}

