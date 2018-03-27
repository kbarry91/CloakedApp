package kevcon.ie.cloaked;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by c-raf on 08/03/2018.
 */

public class Data extends Activity {

    EditText editName,editNumber;
    String key = null;
    boolean isKeySet = false;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data);

        editName= findViewById(R.id.editName);
        editNumber= findViewById(R.id.editNumber);

        save= findViewById(R.id.save);

        final SharedPreferences prefs = this.getSharedPreferences(
                "kevcon.ie.cloaked", Context.MODE_PRIVATE);







        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Contacts contacts=new Contacts(editName.getText().toString(),
               //        editNumber.getText().toString(), key, isKeySet);

                String usernameKey = "kevcon.ie.cloaked.username";
                String numberKey = "kevcon.ie.cloaked.number";
                String keyKey = "kevcon.ie.cloaked.key";
                String isKeySetKey = "kevcon.ie.cloaked.isKeySet";

                Log.d("before Test username: ", editName.getText().toString());
                //Save details
                prefs.edit().putString(usernameKey, editName.getText().toString()).apply();
                prefs.edit().putString(numberKey, editNumber.getText().toString()).apply();
                prefs.edit().putString(keyKey, key).apply();
                prefs.edit().putBoolean(isKeySetKey, isKeySet).apply();

                // Reads in details
                String un = prefs.getString(usernameKey, "no value for un");
                 String pn = prefs.getString(numberKey, "no value for pn");
                 String k = prefs.getString(keyKey, "no value for k");
                 boolean ks = prefs.getBoolean(isKeySetKey, false);

               Log.d("Test username: ", un);
                Log.d("Test number: ", pn);
                Log.d("Test key: ", k);
                //Log.d("Test isKeySet: ", ks);

                Contacts contacts=new Contacts(un,
                        pn, k, ks);

                Intent intent5=new Intent(Data.this,ContactsMainActivity.class);

                intent5.putExtra("data",contacts);
                setResult(2, intent5);

                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}

/*
To store values in shared preferences:

SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
SharedPreferences.Editor editor = preferences.edit();
editor.putString("Name","Harneet");
editor.apply();


To retrieve values from shared preferences:

SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
String name = preferences.getString("Name", "");
if(!name.equalsIgnoreCase(""))
{
    name = name + "  Sethi";   Edit the value here
}


 */