package kevcon.ie.cloaked;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listMessageData.size();
    }

    private class messageOutHolder extends RecyclerView.ViewHolder {
        messageOutHolder(View view) {
            super(view);
        }
    }

    private class messageInHolder extends RecyclerView.ViewHolder {
        messageInHolder(View view) {
            super(view);
        }
    }
}
