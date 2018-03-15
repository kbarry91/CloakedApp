package kevcon.ie.cloaked;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listMessageData.size();
    }
}
