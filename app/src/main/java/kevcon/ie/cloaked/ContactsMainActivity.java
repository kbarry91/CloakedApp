package kevcon.ie.cloaked;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by c-raf on 08/03/2018.
 */

public class ContactsMainActivity extends Activity {
    Button contactAddButton;
    ListView listContacts;

    ArrayList<Contacts> arrayListContact;
    ContactsAdapter contactAdapter;
    Contacts contacts;

    final int C_View=1,C_Delete=2,C_SendMessage=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_activity_main);

        arrayListContact=new ArrayList<Contacts>();

        listContacts= (ListView) findViewById(R.id.listView);

        contactAddButton= (Button) findViewById(R.id.contactAddButton);

        //add button listener
        contactAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ContactsMainActivity.this, Data.class);
                startActivityForResult(intent, 1);


            }
        });

        contactAdapter=new ContactsAdapter(ContactsMainActivity.this,arrayListContact);

        listContacts.setAdapter(contactAdapter);

        listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                registerForContextMenu(listContacts);

            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.listView) {
            menu.add(0, C_View, 1, "View");
            menu.add(0, C_Delete, 2, "Delete");
            menu.add(0, C_SendMessage, 3, "Send Message");

        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        switch (item.getItemId())
        {
            case C_View:

                Intent intent6=new Intent(ContactsMainActivity.this,ContactDetails.class);
                AdapterView.AdapterContextMenuInfo info1 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int index1 = info1.position;

                intent6.putExtra("details", arrayListContact.get(index1));

                startActivity(intent6);

                break;

            case C_Delete:
                Toast.makeText(ContactsMainActivity.this,"Delete",Toast.LENGTH_SHORT).show();

                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int index = info.position;

                Log.e("index",index+" ");
                arrayListContact.remove(index);
                contactAdapter.notifyDataSetChanged();

                break;

            case C_SendMessage:
                                        //Change ContactDetails to a SendSms.java
                                        //or something, this will pass the contents into the selected
                                        //index into a class. Ask Kevin which class suits.
                Intent intent7=new Intent(ContactsMainActivity.this,SendMessage.class);
                AdapterView.AdapterContextMenuInfo info2 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int index2 = info2.position;

                intent7.putExtra("send_msg", arrayListContact.get(index2));

                startActivity(intent7);

                break;

        }
        return  true;


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode==2) {

            contacts = (Contacts) data.getSerializableExtra("data");


            arrayListContact.add(contacts);
            contactAdapter.notifyDataSetChanged();



        }


    }

}

/*

//Retrieve the values
Set<String> set = myScores.getStringSet("key", null);

//Set the values
Set<String> set = new HashSet<String>();
set.addAll(listOfExistingScores);
scoreEditor.putStringSet("key", set);
scoreEditor.commit();
 */