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
 * @author kevin barry
 * @since 15/3/2018
 */
public class ContactsViewAdapter extends RecyclerView.Adapter {


    private Context ctx;
    private List<Contacts> listContacts;


    //Constructor
    public ContactsViewAdapter(Context ctx, List<Contacts> listContacts) {
        this.ctx = ctx;
        this.listContacts = listContacts;
    }

    @Override
    public int getItemCount() {
        return listContacts.size();
    }


    // getItemViewType returns 0 as default so must override to return a value for sent and received messages
    @Override
    public int getItemViewType(int position) {
        Contacts contact = listContacts.get(position);

        if(contact.getKeySet()){
            return 1;
        }else{
            return 2;
        }
    }

    // must confirm if message is sent or received and assign the appropriate view
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == 2) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_contact_nokeyset, parent, false);
            return new contactOutHolder(view);
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_contact_keyset, parent, false);
            return new contactInHolder(view);
        }

        return null;
    }

    // bind message object to viewholder
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


    private class contactOutHolder extends RecyclerView.ViewHolder {
        TextView contactName;

        contactOutHolder(View view) {

            super(view);
            contactName = itemView.findViewById(R.id.contact_body);
        }

        void bind(final Contacts contact) {
           // final String contactIndex = contact.getName();
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

                                case R.id.option4:
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
