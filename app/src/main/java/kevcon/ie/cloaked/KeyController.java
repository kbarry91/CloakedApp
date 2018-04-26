package kevcon.ie.cloaked;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * <h1>KeyController</h1>
 * KeyController is a class to hold key setting methods.
 *
 * @author kevin barry
 * @since 25/4/2018
 */
public class KeyController extends Activity {

    private ContactsHelperDB myDb;
    private boolean keyWasSet;

    /**
     * setNewKey allows a new key to be set.
     * The set key is updated in the SQL lite database.
     *
     * @param contact A Contact Object to set the key for.
     * @param ctx     the Context of the view to return to.
     * @param title   The title to display for the dialog.
     */
    public void setNewKey(final Contacts contact, final Context ctx, String title) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);

        // Set up the input view
        View viewInflated = LayoutInflater.from(ctx).inflate(R.layout.key_set_dialog, null, false);

        final EditText input = viewInflated.findViewById(R.id.input);

        // Inflate the view.
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
                    Log.d("TEST KEY", contact.toString()
                    );

                    // Initialize database helper .
                    myDb = new ContactsHelperDB(ctx);

                    // Returns true if key success.
                    if (myDb.editContact(contact)) {
                        Toast.makeText(ctx, "Key Set Success",
                                Toast.LENGTH_LONG).show();

                        // Send key request to contact.
                        Utils.sendMessage(contact, scrambleKey(contact), ctx);

                    } else {
                        Toast.makeText(ctx, "Could not set key at this time",
                                Toast.LENGTH_LONG).show();
                    }
                    // Close database
                    myDb.close();

                } else {
                    Toast.makeText(ctx, "Key must be 4 or more characters!",
                            Toast.LENGTH_LONG).show();
                }
                // DEBUG
                Log.d("ENTERED KEY in dialog", keyEntered);

                dialog.dismiss();

            }

        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Inflate the dialog.
        builder.show();

    }

    /**
     * scrambleKey Scrambles a key within a message.
     *
     * @param contact The Contact object the key is to be set for.
     * @return a key set String
     */
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

        // convert the scrambled key to hex and return it.
        return Utils.keyStringToHex(scrambledKey.toString().getBytes());
    }

    /**
     * unScrambleKey deScrambles a key from a message.
     *
     * @param newKeySet the new key set string.
     * @return a new key
     */
    public String unScrambleKey(String newKeySet) {
        StringBuilder deScrambledKey = new StringBuilder();
        // final int prefixLength = "Please Open This In Cloaked:".length();
        final String hexStr = newKeySet.substring(newKeySet.lastIndexOf(":") + 1);
        final String keyStr = Utils.hexKeyToString(hexStr);

        // DEBUG
        Log.d("SCRAMBLE KEY: hex:", hexStr + " str:" + keyStr);

        for (int i = 1; i < keyStr.length(); i += 2) {
            deScrambledKey.append(keyStr.charAt(i));
        }

        return deScrambledKey.toString();
    }


    /**
     * resetKeys displays a dialog of a newly requested key set.
     * If confirmed the new key is set
     * Must be synchronised to stop program advancing before user makes a choice
     *
     * @param contact      The Contact in use.
     * @param keyText      The key set string.
     * @param ctx          The context view to inflate from.
     * @param lookValidKey A method that implements runnable to ensure key is valid.
     * @return a boolean value for success/failure.
     */
    public boolean resetKey(final String keyText, final Contacts contact, final Context ctx, final Runnable lookValidKey) {

        keyWasSet = false;

        //pop up dialog to display message
        final Dialog resetDialog = new Dialog(ctx);

        resetDialog.setContentView(R.layout.key_reset_dialog);
        resetDialog.setTitle("Cloaked Key Set Request");

        TextView keyTextView = resetDialog.findViewById(R.id.key_request_text);

        // set the pop up text
        keyTextView.setText(keyText);

        // DEBUG
        Log.d("RESET_KEY_POP", "reset key" + keyText);

        Button confirm = resetDialog.findViewById(R.id.confirm_reset);

        // Close the dialog on click
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set the key
                contact.setKey(keyText);
                contact.setKeySet(true);

                myDb = new ContactsHelperDB(ctx);
                //returns true if key success
                if (myDb.editContact(contact)) {
                    Toast.makeText(ctx, "Key Set Success",
                            Toast.LENGTH_LONG).show();

                    keyWasSet = true;
                    //notify that thread has finished
                    lookValidKey.run();

                } else {
                    Toast.makeText(ctx, "Could not set key at this time",
                            Toast.LENGTH_LONG).show();
                    keyWasSet = false;
                    //notify that thread has finished

                    lookValidKey.run();

                }
                myDb.close();
                resetDialog.dismiss();
            }
        });

        Button deny = resetDialog.findViewById(R.id.deny_reset_button);

        // Close the dialog on click
        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyWasSet = false;
                Toast.makeText(ctx, "Key Request Cancelled",
                        Toast.LENGTH_LONG).show();
                resetDialog.dismiss();
                lookValidKey.run();
            }
        });

        resetDialog.show();

        return keyWasSet;
    }

}

