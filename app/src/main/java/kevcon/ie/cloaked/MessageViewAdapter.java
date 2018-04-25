package kevcon.ie.cloaked;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static kevcon.ie.cloaked.Encryption.startDecrypt;

/**
 * <h1>ContactsViewAdapter</h1>
 * ContactsViewAdapter is a RecyclerView holder to bind a large data set of @Message objects to a limited view
 *
 * @author kevin barry
 * @since 25/4/2018
 */
public class MessageViewAdapter extends RecyclerView.Adapter {
    private Context ctx;
    private List<Message> listMessageData;
    private Contacts contact;


    // Package Private Constructor
    MessageViewAdapter(Context ctx, List<Message> listMessageData, Contacts contact) {
        this.ctx = ctx;
        this.listMessageData = listMessageData;
        this.contact = contact;
    }

    @Override
    public int getItemCount() {
        return listMessageData.size();
    }


    // getItemViewType returns 0 as default so must override to return a value for sent and received messages
    @Override
    public int getItemViewType(int position) {
        Message message = listMessageData.get(position);
        switch (message.getType()) {
            case 1:
                //type 1 is a received message
                return 1;
            case 2:
                //type 2 is a sent message
                return 2;
            default:

                return 0;
        }
    }

    // must confirm if message is sent or received and assign the appropriate view
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == 2) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new messageOutHolder(view);
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_recieved, parent, false);
            return new messageInHolder(view);
        }

        return null;
    }

    // bind message object to view holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = listMessageData.get(position);

        switch (holder.getItemViewType()) {
            case 2:
                ((messageOutHolder) holder).bind(message);
                break;
            case 1:
                ((messageInHolder) holder).bind(message);
        }
    }

    /**
     * messageOutHolder represents a sent message object. Extends ViewHolder to represent data in a large data set.
     */
    private class messageOutHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        messageOutHolder(View view) {

            super(view);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
        }

        /**
         * bind binds the message data and methods to a view.
         *
         * @param message a message object to bind to view.
         */
        void bind(Message message) {
            final String strMessage = message.getMessage();
            messageText.setText(message.getMessage());
            timeText.setText(Utils.convertDate(message.getTime()));

            // a pop down menu to display options on message
            messageText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // define a pop up menu
                    PopupMenu msgMenu = new PopupMenu(ctx, messageText);

                    // inflate the menu view
                    msgMenu.inflate(R.menu.message_menu);

                    // Add listener
                    msgMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu1:
                                    if (contact.getKeySet()) {
                                        startDecrypt(strMessage, contact, ctx);
                                    }
                                    break;
                                case R.id.menu2:
                                    break;
                                case R.id.menu3:
                                    break;
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    msgMenu.show();
                }
            });

        }
    }

    /**
     * messageInHolder represents a received message object. Extends ViewHolder to represent data in a large data set.
     */
    private class messageInHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;

        messageInHolder(View view) {

            super(view);

            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            nameText = itemView.findViewById(R.id.text_message_name);
        }

        /**
         * bind binds the message data and methods to a view.
         *
         * @param message a message object to bind to view.
         */
        void bind(Message message) {
            final String strMessage = message.getMessage();
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(Utils.convertDate(message.getTime()));

            // timeText.setText(message.getTime());
            nameText.setText(message.getSender());

            // a pop down menu to display options on message
            messageText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // define a pop up menu
                    PopupMenu msgMenu = new PopupMenu(ctx, messageText);

                    // inflate the menu view
                    msgMenu.inflate(R.menu.message_menu);

                    // Add listener
                    msgMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu1:
                                    if (contact.getKeySet()) {
                                        startDecrypt(strMessage, contact, ctx);
                                    }
                                    break;
                                case R.id.menu2:
                                    break;
                                case R.id.menu3:
                                    break;
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    msgMenu.show();
                }
            });

        }
    }
}
