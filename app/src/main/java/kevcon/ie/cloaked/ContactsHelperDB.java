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
 * Created by Conor Raftery on 27/03/2018.
 */

public class ContactsHelperDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "cloaked.db";
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_PHONE = "phone";
    public static final String CONTACTS_COLUMN_KEY = "cKey";
    public static final String CONTACTS_COLUMN_ISKEYSET = "isKeySet";

    public ContactsHelperDB(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    // create a table
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub


        db.execSQL(
                "create table contacts " +
                        "(name text primary key,phone text,cKey text,isKeySet int)"
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
        int flag = (newContact.getKeySet()) ? 1 : 0;
        contentValues.put("isKeySet", flag);

        if (db.insert("contacts", null, contentValues) == -1) {

            db.close();
            return false;
        }
        db.close();
        return true;
    }

    public Cursor getData(Contacts contact) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contacts where name like " + contact.getName() + "", null);
        db.close();
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        db.close();
        return numRows;
    }

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
//res.isAfterLast() == false
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