package kevcon.ie.cloaked;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

    //ArrayList<Contacts> arrayListContact;
    //List<Contacts> contactList;

    ContactsAdapter contactAdapter;
    Contacts contacts;

    final int C_View=1,C_Delete=2,C_SendMessage=3;

    //database variables
    public final static String EXTRA_MESSAGE = "MESSAGE";
    private ListView obj;
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


        contactAddButton = findViewById(R.id.contactAddButton);


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

        listContacts = findViewById(R.id.listView);


        contactAdapter=new ContactsAdapter(ContactsMainActivity.this,contactList);

        listContacts.setAdapter(contactAdapter);

        listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                registerForContextMenu(listContacts);

            }
        });


        //add button listener
        contactAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ContactsMainActivity.this, Data.class);
                startActivityForResult(intent, 1);


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

                intent6.putExtra("details", contactList.get(index1));

                startActivity(intent6);

                break;

            case C_Delete:

                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int index = info.position;

                Log.e("index", index + " =======");

                // delete the contact from the database
                if (myDb.deleteContact(contactAdapter.getItem(index)) == -1) {
                    Toast.makeText(this, " Failed to delete", Toast.LENGTH_SHORT).show();

                } else {

                    contactList.remove(index);

                    contactAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Contact deleted", Toast.LENGTH_SHORT).show();
                }





                break;

            case C_SendMessage:
                                        //Change ContactDetails to a SendSms.java
                                        //or something, this will pass the contents into the selected
                                        //index into a class. Ask Kevin which class suits.

                Intent intent7=new Intent(ContactsMainActivity.this,SendMessage.class);
                AdapterView.AdapterContextMenuInfo info2 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int index2 = info2.position;

                intent7.putExtra("send_msg", contactAdapter.getItem(index2));

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


            contactList.add(contacts);
            contactAdapter.notifyDataSetChanged();



        }


    }


}

