package kevcon.ie.cloaked;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * .
 */
public class MessageFragment extends Fragment {


    //@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View view = inflater.inflate(R.layout.fragment_message, container, false);

        // Inflate the layout for this fragment(launch activity message list)
        // return inflater.inflate(R.layout.fragment_message, container, false);

        return inflater.inflate(R.layout.fragment_message, container, false);
    }


}
