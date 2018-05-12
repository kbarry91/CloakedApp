package kevcon.ie.cloaked;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.List;

/**
 * * <h1>SendMessageActivity</h1>
 * ContactsMainActivity is the main message page.
 * Controls the adding, deletion and displaying of contacts
 *
 * @author Conor Raftery
 * @Since 08/03/2018
 */
public class ContactsMainActivity extends AppCompatActivity {

    //Constants & Variables
    Button contactAddButton;
    private RecyclerView contactRec;
    private ContactsViewAdapter contactAdapter;
    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    Contacts contacts;
    private DrawerLayout mDrawerLayout;

    //database variables
    ContactsHelperDB myDb;
    List<Contacts> contactList;

    /**
     * * <h2>SendMessageActivity</h2>
     * OnCreate is the most important method in most classes. The onCreate method is ran first
     * when the class is opened. It is the equivalence to a 'main' method.
     * This method displays all contacts on the layout contacts_activity_main using a RecyclerView,
     * it also contains a DrawerLayout.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Sets the layout
        setContentView(R.layout.contacts_activity_main);


        //Sets the toolbar
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        //Sets the action bar
        setSupportActionBar(toolbar);
        //Gets reference to the action bar
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        //Add title to action bar
        getSupportActionBar().setTitle("CLOAKED CONTACTS");
        //Displays actionbar
        actionbar.setDisplayHomeAsUpEnabled(true);
        //Adds hamburger icon to actionbar
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        //gets reference to drawer_layout
        mDrawerLayout = findViewById(R.id.drawer_layout);

        //Event listener for if hamburger icon is clicked or user slides the screen
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

        //Creates reference for a container for the drawer_layout and adds a listener
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    //private MenuItem menuItem;

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                       // this.menuItem = menuItem;
                        Log.d("MenuItemMain", "item selected");

                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped

                        Log.d("MenuItemMain", "item selected");
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        switch (menuItem.getItemId()) {
                            case R.id.menu_notes_option:
                                Log.d("MenuItem", "item selected");
                                Toast.makeText(getBaseContext(), "Opening Menu Option:"+menuItem.getItemId(),
                                        Toast.LENGTH_LONG).show();
                                break;

                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });


        // initialise database, get contacts list and close database
        myDb = new ContactsHelperDB(this);
        contactList = myDb.getAllContacts();
        myDb.close();

        //Get reference for using button
        contactAddButton = findViewById(R.id.button_add_contact);


        // DEBUG TO CHECK DATABASE
        for (Contacts con : contactList) {
            Log.e("DATACHECK", con.toString());
        }


        //add button listener
        contactAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Create new intent for Data.java
                Intent intent = new Intent(ContactsMainActivity.this, Data.class);
                //Start intent
                startActivityForResult(intent, 1);


            }
        });

        //Used to reference recyler view for contact list
        contactRec = findViewById(R.id.recycler_view_contact_list);
        //Create instance of the ContactAdapter and pass it in the context and contact list
        contactAdapter = new ContactsViewAdapter(this, contactList);
        //Set the layout for the recyler view
        contactRec.setLayoutManager(new LinearLayoutManager(this));
        //Add contacts list to recycler view
        contactRec.setAdapter(contactAdapter);


        //add button listener
        contactAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If permission is allowed and not turned off
                if (ContextCompat.checkSelfPermission(ContactsMainActivity.this, android.Manifest.permission.READ_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    getPermissionToReadSMS();
                } else {
                    //Create and start new intent
                    Intent intent = new Intent(ContactsMainActivity.this, Data.class);
                    startActivityForResult(intent, 1);
                }

            }
        });

    }//onCreate

/**
 * <h2>onOptionsItemSelected</h2>
*This method is used to perform an action depending on what option is selected
* from the Drawer Menu, it will be used for future use. It is passed in the option
 * selected.
 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Switch on the item selected
        switch (item.getItemId()) {
            case android.R.id.home:
                //Open the drawer and keep it opened (true)
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        //Return the option selected
        return super.onOptionsItemSelected(item);
    }

    /**
     * <h2>getPermissionToReadSMS</h2>
     *This method is used to prompt the user to use permissions to read sms.
     * This is depended on the target API denoted by @TargetApi
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void getPermissionToReadSMS() {
        //If permission is not granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            //If permission is rejected
            if (shouldShowRequestPermissionRationale(
                    android.Manifest.permission.READ_SMS)) {
                //Toast the user
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
            }
            //Prompt for permission
            requestPermissions(new String[]{android.Manifest.permission.READ_SMS},
                    READ_SMS_PERMISSIONS_REQUEST);
        }
    }

    /**
     * <h2>onRequestPermissionsResult</h2>
     *This method is used to determine if the user allowed permissions to read sms.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        //If permission is requested
        if (requestCode == READ_SMS_PERMISSIONS_REQUEST) {
            //if permission is granted
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast the user
                Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show();
                //Create and start new intent
                Intent intent = new Intent(ContactsMainActivity.this, Data.class);
                startActivityForResult(intent, 1);
            } else {
                //Toast the user
                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
            }

        } else {
            //Request permission
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}

