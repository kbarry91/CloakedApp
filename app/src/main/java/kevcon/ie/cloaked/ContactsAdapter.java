package kevcon.ie.cloaked;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by c-raf on 08/03/2018.
 */

public class ContactsAdapter extends BaseAdapter {

    private Context context;
    private List<Contacts> contactList;

    public ContactsAdapter(Context context, List<Contacts> contact) {
        this.context = context;
        this.contactList = contact;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Contacts getItem(int position) {
        Contacts contacts;
        contacts = contactList.get(position);
        return contacts;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        if(convertView==null)
        {
            LayoutInflater layoutInflater= LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.lay_contact,null);
        }

        else
        {
            view=convertView;
        }

        TextView contactName= view.findViewById(R.id.contactName);

        //get data


        Contacts contacts=contactList.get(position);

        contactName.setText(contacts.getName());
        System.getProperty("line.separator");
        Log.e("CONTACTSADAPTERR", contacts.getName() + " ");

        return view;

    }
}
