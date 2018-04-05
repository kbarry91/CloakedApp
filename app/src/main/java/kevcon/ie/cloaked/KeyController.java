package kevcon.ie.cloaked;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

/**
 * @author kevin barry
 */

public class KeyController extends Activity {
    // Method to verify key
    ContactsHelperDB myDb;

    public void setNewKey(final Contacts contact, final Context ctx, String title) {
        // final ContactsHelperDB myDb = new ContactsHelperDB();
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);

        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        //  View viewInflated = LayoutInflater.from(ctx).inflate(R.layout.key_entry_dialog, (ViewGroup) findViewById(android.R.id.content), false);
        // Set up the input
        View viewInflated = LayoutInflater.from(ctx).inflate(R.layout.key_set_dialog, null, false);

        final EditText input = viewInflated.findViewById(R.id.input);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String keyEntered;
                keyEntered = input.getText().toString();

                //key must be of at least length 4
                if (keyEntered.length() > 3) {
                    // set the key
                    contact.setKey(keyEntered);
                    contact.setKeySet(true);
                    Log.d("TESTEDIT", contact.toString()
                    );
                    myDb = new ContactsHelperDB(ctx);
                    //returns true if key success
                    if (myDb.editContact(contact)) {
                        Toast.makeText(ctx, "Key Set Success",
                                Toast.LENGTH_LONG).show();

                        Utils.sendMessage(contact, scrambleKey(contact), ctx);
                        //  SendMessage sm = new SendMessage();
                        //  sm.sendSms(contact,"Notification from Cloaked please launch app :"+scrambleKey(contact));
                        //  SendMessage.sendSms();


                    } else {
                        Toast.makeText(ctx, "Could not set key at this time",
                                Toast.LENGTH_LONG).show();
                    }
                    myDb.close();

                } else {
                    Toast.makeText(ctx, "Key must be 4 or more characters!",
                            Toast.LENGTH_LONG).show();
                }
                Log.d("ENTERED KEYY in dialog", keyEntered);

                dialog.dismiss();


            }

        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();


    }

    /*
    * Scrambles a key within a message
    * @returns a key set message
    * */
    public String scrambleKey(Contacts contact) {
        StringBuilder scrambledKey = new StringBuilder();

        String org = contact.getKey();
        Random r = new Random();
        // generate a random letter
        char c = (char) (r.nextInt(26) + 'a');

        for (int i = 0; i < org.length(); i++) {
            scrambledKey.append(c);
            scrambledKey.append(org.charAt(i));
            c = (char) (r.nextInt(26) + 'a');
        }
        Log.d("SCRAM", scrambledKey.toString());
        return scrambledKey.toString();
    }


}

