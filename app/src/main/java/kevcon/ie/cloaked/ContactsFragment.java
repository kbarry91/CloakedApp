package kevcon.ie.cloaked;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * <p>
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment{




        //===========================================================================
       // @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            //===============================================================================
            super.onCreate(savedInstanceState);
           // setContentView(R.layout.activity_main);


            //===============================================================================

            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_contacts, container, false);
        }


}
