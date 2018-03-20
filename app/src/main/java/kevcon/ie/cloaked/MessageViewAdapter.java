package kevcon.ie.cloaked;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * <h1>MessageViewAdapter</h1>
 * MessageViewAdapter is a RecyclerView holder to bind a large data set of @Message objects to a limited view
 *
 * @author kevin barry
 * @since 15/3/2018
 */
public class MessageViewAdapter extends RecyclerView.Adapter {
    private Context ctx;
    private List<Message> listMessageData;

    //Constructor
    public MessageViewAdapter(Context ctx, List<Message> listMessageData) {
        this.ctx = ctx;
        this.listMessageData = listMessageData;
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

    // bind message object to viewholder
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


    private class messageOutHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        messageOutHolder(View view) {

            super(view);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());
            timeText.setText(message.getTime());
            // Format the stored timestamp into a readable String using method.
            //timeText.setText(Utils.formatDateTime(message.getCreatedAt()));/////////////////////////need to make a utill class for generic methods
        }
    }

    private class messageInHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        messageInHolder(View view) {

            super(view);

            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            nameText = itemView.findViewById(R.id.text_message_name);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(Utils.convertDate(message.getTime()));

            // timeText.setText(message.getTime());
            nameText.setText(message.getSender());

            // Insert the profile image from the URL into the ImageView.
            // Utils.displayRoundImageFromUrl(mContext, message.getSender(), profileImage);
        }
    }
}
