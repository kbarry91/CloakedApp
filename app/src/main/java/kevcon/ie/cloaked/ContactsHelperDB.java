package kevcon.ie.cloaked;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>SendMessageActivity</h1>
 * This class is used for handling our SQLite database.
 * It contains SQL queries for example creating a table.
 *
 * @author Conor Raftery
 * @Since 27/03/2018
 */
public class ContactsHelperDB extends SQLiteOpenHelper {

    //Variables for DB
    public static final String DATABASE_NAME = "cloaked.db";
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_PHONE = "phone";
    public static final String CONTACTS_COLUMN_KEY = "cKey";
    public static final String CONTACTS_COLUMN_ISKEYSET = "isKeySet";

    //Constructor
    public ContactsHelperDB(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    // create a table

    /**
     * <h2>onCreate</h2>
     * This creates the table for our DB
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        //SQL query to create table "contacts" with rows
        db.execSQL(
                "create table contacts " +
                        "(name text primary key,phone text,cKey text,isKeySet int)"
        );
    }


    /**
     * <h2>onUpgrade</h2>
     * This updates our DB when new data is added
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    /**
     * <h2>insertContact</h2>
     * This inserts a contact to the database
     */
    public boolean insertContact(Contacts newContact) {

        //Initialize DB for writing
        SQLiteDatabase db = this.getWritableDatabase();
        //Create new instance of Content Values
        ContentValues contentValues = new ContentValues();

        //Put contact details into correct table row
        contentValues.put("name", newContact.getName());
        contentValues.put("phone", newContact.getNumber());
        contentValues.put("cKey", newContact.getKey());
        int flag = (newContact.getKeySet()) ? 1 : 0;
        contentValues.put("isKeySet", flag);

        //If inserting contact into DB == -1
        if (db.insert("contacts", null, contentValues) == -1) {

            db.close();
            return false;
        }
        db.close();
        return true;
    }

    //For checking certain contacts, never used
    public Cursor getData(Contacts contact) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contacts where name like " + contact.getName() + "", null);
        db.close();
        return res;
    }

    //For getting how many contacts in DB, never used
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        db.close();
        return numRows;
    }

    //Used to edit a contacts details, for example editing phone number to add countryCode prefix
    public boolean editContact(Contacts newContact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", newContact.getName());
        contentValues.put("phone", newContact.getNumber());
        contentValues.put("cKey", newContact.getKey());
        int flag = (newContact.getKeySet()) ? 1 : 0;
        contentValues.put("isKeySet", flag);
        db.update("contacts", contentValues, "name like ? ", new String[]{newContact.getName()});
        db.close();
        return true;
    }

    // remove a contact from database
    public Integer deleteContact(Contacts contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleteStatus = db.delete("contacts",
                "name like ? ",
                new String[]{contact.getName()});
        db.close();
        return deleteStatus;

    }

    //load a list of Contacts
    public List<Contacts> getAllContacts() {
        List<Contacts> contactList = new ArrayList<>();

        Contacts contact;
        String name;
        String phone;
        String cKey;
        Boolean isKeySet;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();
        res.close();

        while(!res.isAfterLast()){
            name = res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME));
            phone = res.getString(res.getColumnIndex(CONTACTS_COLUMN_PHONE));
            cKey = res.getString(res.getColumnIndex(CONTACTS_COLUMN_KEY));
            isKeySet = res.getInt(res.getColumnIndex(CONTACTS_COLUMN_ISKEYSET)) == 1;
            res.moveToNext();
            contact = new Contacts(name, phone, cKey, isKeySet);
            contactList.add(contact);
        }
        db.close();
        return contactList;
    }
}