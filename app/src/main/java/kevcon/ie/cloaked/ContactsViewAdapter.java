package kevcon.ie.cloaked;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * <h1>ContactsViewAdapter</h1>
 * ContactsViewAdapter is a RecyclerView holder to bind a large data set of @Message objects to a limited view
 *
 * @author Conor Raftery
 * @since 15/3/2018
 */
public class ContactsViewAdapter extends RecyclerView.Adapter {

    //Create a  context and list of contacts
    private Context ctx;
    private List<Contacts> listContacts;

    //Constructor
    public ContactsViewAdapter(Context ctx, List<Contacts> listContacts) {
        this.ctx = ctx;
        this.listContacts = listContacts;
    }

    //Getter for getting size of contact list
    @Override
    public int getItemCount() {
        return listContacts.size();
    }


    // getItemViewType returns 0 as default so must override to return a value for
    // a contact with a key set or not set
    @Override
    public int getItemViewType(int position) {
        Contacts contact = listContacts.get(position);

        if(contact.getKeySet()){
            return 1;
        }else{
            return 2;
        }
    }

    // must confirm if contacts key is set or not set and assign the appropriate view
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        //If key is not set, set a certain view (item_contact_nokeyset)
        if (viewType == 2) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_contact_nokeyset, parent, false);
            //Return the view
            return new contactOutHolder(view);
            //If key is set, set a certain view (item_contact_keyset)
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_contact_keyset, parent, false);
            //Return the view
            return new contactInHolder(view);
        }

        return null;
    }

    // bind contact object to viewholder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Contacts message = listContacts.get(position);

        switch (holder.getItemViewType()) {
            case 2:
                ((contactOutHolder) holder).bind(message);
                break;
            case 1:
                ((contactInHolder) holder).bind(message);
        }
    }

    /**<h2>contactOutHolder</h2>
     * contactOutHolder class represents a contact with no key set.
     * Extends ViewHolder to represent data in a large data set.
     */
    private class contactOutHolder extends RecyclerView.ViewHolder {
        TextView contactName;

        contactOutHolder(View view) {

            super(view);
            contactName = itemView.findViewById(R.id.contact_body);
        }

        /** <h2>bind</h2>
         * This method binds the contact data and methods to a view.
         * @param contact a contact object to bind to view.
         */
        void bind(final Contacts contact) {
            contactName.setText(contact.getName());

            // a pop down menu to display options on contact
            contactName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // define a pop up menu
                    PopupMenu contactMenu = new PopupMenu(ctx, contactName);

                    // inflate the menu view
                    contactMenu.inflate(R.menu.contact_menu);

                    // Add listener
                    contactMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            //Switch on which option the user selects
                            switch (item.getItemId()) {
                                //View contact details
                                case R.id.option1:

                                    //Create new intent to ContactDetails, pass in the selected
                                    //contacts details,, and start the activity
                                    Intent intent=new Intent(ctx,ContactDetails.class);
                                    intent.putExtra("details", contact);
                                    ctx.startActivity(intent);

                                    break;
                                    //Delete selected contact
                                case R.id.option2:

                                    //Create instance of DB
                                    ContactsHelperDB myDb = new ContactsHelperDB(ctx);

                                    //If index is out of bounds
                                    if(myDb.deleteContact(contact) == -1){
                                        //Prompt user
                                        Toast.makeText(ctx, " Failed to delete", Toast.LENGTH_SHORT).show();
                                }else {
                                        //Remove contact
                                        listContacts.remove(contact);

                                        notifyDataSetChanged();
                                        //prompt user
                                        Toast.makeText(ctx, "Contact deleted", Toast.LENGTH_SHORT).show();
                                    }
                                    //close DB
                                    myDb.close();

                                    break;
                                    //Send message to selected user
                                case R.id.option3:
                                    //Create new intent to SendMessage, pass in the selected
                                    //contacts details, and start the activity
                                    Intent intent2 = new Intent(ctx, SendMessageActivity.class);
                                    intent2.putExtra("send_msg", contact);
                                    ctx.startActivity(intent2);


                                    break;
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    contactMenu.show();
                }
            });

        }
    }

    /**<h2>contactInHolder</h2>
     * contactOutHolder represents a contact with no key set.
     * Extends ViewHolder to represent data in a large data set.
     * Replicated from contactOutHolder with slight variations.
     */
    private class contactInHolder extends RecyclerView.ViewHolder {
        TextView contactName;

        contactInHolder(View view) {

            super(view);

            contactName = itemView.findViewById(R.id.contact_body);
        }

        void bind(final Contacts contact) {

            contactName.setText(contact.getName());


            // a pop down menu to display options on message
            contactName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // define a pop up menu
                    PopupMenu contactMenu = new PopupMenu(ctx, contactName);

                    // inflate the menu view
                    contactMenu.inflate(R.menu.contact_menu);

                    // Add listener
                    contactMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            //Same switch statement as in contactOutHolder
                            switch (item.getItemId()) {
                                case R.id.option1:

                                    Intent intent=new Intent(ctx,ContactDetails.class);
                                    intent.putExtra("details", contact);
                                    ctx.startActivity(intent);

                                    break;
                                case R.id.option2:


                                    ContactsHelperDB myDb = new ContactsHelperDB(ctx);

                                    if(myDb.deleteContact(contact) == -1){

                                        Toast.makeText(ctx, " Failed to delete", Toast.LENGTH_SHORT).show();
                                    }else {

                                        listContacts.remove(contact);

                                        notifyDataSetChanged();
                                        Toast.makeText(ctx, "Contact deleted", Toast.LENGTH_SHORT).show();
                                    }

                                    myDb.close();


                                    break;
                                case R.id.option3:
                                    ContactsMainActivity cma = new ContactsMainActivity();

                                    if (ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.READ_SMS)
                                            != PackageManager.PERMISSION_GRANTED) {
                                        cma.getPermissionToReadSMS();


                                    } else {
                                        Intent intent2 = new Intent(ctx, SendMessageActivity.class);
                                        intent2.putExtra("send_msg", contact);
                                        ctx.startActivity(intent2);

                                    }
                                    break;

                                    //Option to reset key, as a key is already set
                                case R.id.option4:
                                    //Create new instance of KeyController and call setNewKey
                                    KeyController kc = new KeyController();
                                    kc.setNewKey(contact, ctx, "Reset Cloaked Key");
                                    break;
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    contactMenu.show();
                }
            });

        }
    }


}
