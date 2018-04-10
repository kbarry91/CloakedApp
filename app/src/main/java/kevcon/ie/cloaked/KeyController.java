package kevcon.ie.cloaked;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
        builder.show();


    }

    /**
     * Scrambles a key within a message
     *
     * @return a key set message
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
        return scrambledKey.toString();
    }

    /**
     * deScrambles a key from a message
     *
     * @return a new key
     */
    public String unScrambleKey(String newKeySet) {
        StringBuilder deScrambledKey = new StringBuilder();
        for (int i = "Please Open This In Cloaked:".length() - 1; i < newKeySet.length(); i += 2) {
            deScrambledKey.append(newKeySet.charAt(i));
        }
        return deScrambledKey.toString();
    }


    /**
     * resetKeys displays a dialog of a newly requested key set.
     * If confirmed the new key is set
     */
    public void resetKey(final String keyText, final Contacts contact, final Context ctx) {


        //pop up dialog to display message
        final Dialog resetDialog = new Dialog(ctx);
        resetDialog.setContentView(R.layout.key_reset_dialog);
        resetDialog.setTitle("Cloaked Key Set Request");

        TextView keyTextView = resetDialog.findViewById(R.id.key_request_text);

        // set the pop up text
        keyTextView.setText(keyText);

        Log.d("RESETKEYPOP", "reset key" + keyText);

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


                } else {
                    Toast.makeText(ctx, "Could not set key at this time",
                            Toast.LENGTH_LONG).show();
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
                Toast.makeText(ctx, "Key Request Cancelled",
                        Toast.LENGTH_LONG).show();
                resetDialog.dismiss();
            }
        });

        resetDialog.show();


    }

    /**
     * deleteKeyRequest Deletes the key request from the devices sms history so that it does not request key again
     */
    public void deleteKeyRequest(Message message, Context ctx) {
        Log.d("DELETEREQ", message.toString());
        //   String cc = Data.GetCountryZipCode();
        // contactNumber = Utils.addCountryCode(cc, contactNumber);
        try {
            Uri uriSms = Uri.parse("content://sms/inbox");
            Cursor c = ctx.getContentResolver().query(
                    uriSms,
                    new String[]{"_id", "thread_id", "address", "person",
                            "date", "body"}, "read=0", null, null);

            if (c != null && c.moveToFirst()) {
                do {
                    long id = c.getLong(0);
                    long threadId = c.getLong(1);
                    String address = c.getString(2);
                    String body = c.getString(5);
                    String date = c.getString(3);
                    Log.e("log>>>",
                            "0--->" + c.getString(0) + "1---->" + c.getString(1)
                                    + "2---->" + c.getString(2) + "3--->"
                                    + c.getString(3) + "4----->" + c.getString(4)
                                    + "5---->" + c.getString(5));
                    Log.e("log>>>", "date" + c.getString(0));

                    ContentValues values = new ContentValues();
                    values.put("read", true);
                    getContentResolver().update(Uri.parse("content://sms/"),
                            values, "_id=" + id, null);

                    if (body.equals(message.getMessage()) && address.equals(message.getSender())) {
                        // mLogger.logInfo("Deleting SMS with id: " + threadId);
                        ctx.getContentResolver().delete(
                                Uri.parse("content://sms/" + id), "date=?",
                                new String[]{c.getString(4)});
                        Log.e("log>>>", "Delete success.........");
                    }
                } while (c.moveToNext());
            }
            // close cursor
            c.close();
        } catch (Exception e) {
            Log.e("log>>>", e.toString());
        }
    }

}

