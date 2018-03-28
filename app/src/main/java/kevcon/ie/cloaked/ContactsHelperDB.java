package kevcon.ie.cloaked;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by conor on 27/03/2018.
 */

public class ContactsHelperDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "cloaked.db";
    public static final String CONTACTS_TABLE_NAME = "contacts";

    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_PHONE = "phone";
    public static final String CONTACTS_COLUMN_KEY = "cKey";
    public static final String CONTACTS_COLUMN_ISKEYSET = "isKey";
    private HashMap hp;

    public ContactsHelperDB(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    // create a table
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table contacts " +
                        "(name text primary key,phone text,cKey text,isKey text)"
        );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    //insert a contact to the database
    public boolean insertContact(Contacts newContact) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", newContact.getName());
        contentValues.put("phone", newContact.getNumber());
        contentValues.put("cKey", newContact.getKey());
        contentValues.put("isKey", newContact.getKeySet());

        db.insert("contacts", null, contentValues);
        return true;
    }

    public Cursor getData(Contacts contact) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contacts where name like " + contact.getName() + "", null);
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean editContact(Contacts newContact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", newContact.getName());
        contentValues.put("phone", newContact.getNumber());
        contentValues.put("cKey", newContact.getKey());
        contentValues.put("isSet", newContact.getKeySet());
        db.update("contacts", contentValues, "name like ? ", new String[]{newContact.getName()});
        return true;
    }

    // remove a contact from database
    public Integer deleteContact(Contacts contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "name like ? ",
                new String[]{contact.getName()});
    }

    //load a list of Contacts
    public List<Contacts> getAllContacts() {
        List<Contacts> contactList = new ArrayList<>();

        Contacts contact;

        String name;
        String phone;
        String cKey;
        Boolean isKey;

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){


            //array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            name = res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME));
            phone = res.getString(res.getColumnIndex(CONTACTS_COLUMN_PHONE));
            cKey = res.getString(res.getColumnIndex(CONTACTS_COLUMN_KEY));
            // might have to change
            isKey = res.getInt(res.getColumnIndex(CONTACTS_COLUMN_ISKEYSET)) > 0;

            res.moveToNext();
            contact = new Contacts(name, phone, cKey, isKey);
            contactList.add(contact);
        }
        return contactList;
    }
}