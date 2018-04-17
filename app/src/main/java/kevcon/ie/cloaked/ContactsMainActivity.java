package kevcon.ie.cloaked;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
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
import android.support.v7.widget.Toolbar;

import java.util.List;

/**
 * Created by c-raf on 08/03/2018.
 */

public class ContactsMainActivity extends AppCompatActivity {
    Button contactAddButton;
    ListView listContacts;

    private RecyclerView contactRec;
    private ContactsViewAdapter contactAdapter;

    //ContactsAdapter contactAdapter;
    Contacts contacts;

    //database variables
    ContactsHelperDB myDb;
    List<Contacts> contactList;


    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_activity_main);


        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        getSupportActionBar().setTitle("Contacts");
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        //SearchView search = (SearchView) item.getActionView();
        //search.setLayoutParams(new ActionBar.LayoutParams(Gravity.RIGHT));


        mDrawerLayout = findViewById(R.id.drawer_layout);

        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });


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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




}

