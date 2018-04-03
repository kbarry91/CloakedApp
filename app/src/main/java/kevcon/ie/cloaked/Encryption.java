package kevcon.ie.cloaked;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Encryption is a class that implements a Porta Cipher to encrypt a message
 *
 * @author kevin barry
 */
public class Encryption {

    static int keyLetter = 0;
    public static final String[][] tableau = {
            {"KEYS", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
                    "T", "U", "V", "W", "X", "Y", "Z"},
            {"AB", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "A", "B", "C", "D", "E", "F", "G",
                    "H", "I", "J", "K", "L", "M"},
            {"CD", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "N", "M", "A", "B", "C", "D", "E", "F",
                    "G", "H", "I", "J", "K", "L"},
            {"EF", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "N", "O", "L", "M", "A", "B", "C", "D", "E",
                    "F", "G", "H", "I", "J", "K"},
            {"GH", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "N", "O", "P", "K", "L", "M", "A", "B", "C", "D",
                    "E", "F", "G", "H", "I", "J"},
            {"IJ", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "N", "O", "P", "Q", "J", "K", "L", "M", "A", "B", "C",
                    "D", "E", "F", "G", "H", "I"},
            {"KL", "S", "T", "U", "V", "W", "X", "Y", "Z", "N", "O", "P", "Q", "R", "I", "J", "K", "L", "M", "A", "B",
                    "C", "D", "E", "F", "G", "H"},
            {"MN", "T", "U", "V", "W", "X", "Y", "Z", "N", "O", "P", "Q", "R", "S", "H", "I", "J", "K", "L", "M", "A",
                    "B", "C", "D", "E", "F", "G"},
            {"OP", "U", "V", "W", "X", "Y", "Z", "N", "O", "P", "Q", "R", "S", "T", "G", "H", "I", "J", "K", "L", "M",
                    "A", "B", "C", "D", "E", "F"},
            {"QR", "V", "W", "X", "Y", "Z", "N", "O", "P", "Q", "R", "S", "T", "U", "F", "G", "H", "I", "J", "K", "L",
                    "M", "A", "B", "C", "D", "E"},
            {"ST", "W", "X", "Y", "Z", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "E", "F", "G", "H", "I", "J", "K",
                    "L", "M", "A", "B", "C", "D"},
            {"UV", "X", "Y", "Z", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "D", "E", "F", "G", "H", "I", "J",
                    "K", "L", "M", "A", "B", "C"},
            {"WX", "Y", "Z", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "C", "D", "E", "F", "G", "H", "I",
                    "J", "K", "L", "M", "A", "B"},
            {"YZ", "Z", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "B", "C", "D", "E", "F", "G", "H",
                    "I", "J", "K", "L", "M", "A"}};// public static final


    public static char encrypt(char letter, String cipherKey) {

        int row = 0;
        int col;

        // can remove this loop
        if ((int) letter > 64 && (int) letter < 91) {
            for (int i = 1; i < 14; i++) {
                if (tableau[i][0].indexOf(cipherKey.charAt(keyLetter)) >= 0) {
                    row = i;
                    break;
                } // if
            } // for row

            col = (((int) letter) - 65) + 1;

            //increment keyLetters location in encryption key
            keyLetter++;
            if (keyLetter == cipherKey.length()) {
                keyLetter = 0;
            }
            letter = tableau[row][col].charAt(0);
        }
        return letter;
    }// encrypt

    // Method to verify key
    public static void startDecrypt(String messageText, final Contacts contact, final Context ctx) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Cloaked Key");
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        //  View viewInflated = LayoutInflater.from(ctx).inflate(R.layout.key_entry_dialog, (ViewGroup) findViewById(android.R.id.content), false);
        // Set up the input
        View viewInflated = LayoutInflater.from(ctx).inflate(R.layout.key_entry_dialog, null, false);

        final EditText input = viewInflated.findViewById(R.id.input);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String keyEntered = "";
                keyEntered = input.getText().toString();
                if (keyEntered.equals(contact.getKey())) {
                    //pop up dialog to display message
                    Dialog msgDialog = new Dialog(ctx);
                } else {
                    Toast.makeText(ctx, "Invalid key",
                            Toast.LENGTH_LONG).show();
                }
                Log.d("ENTERED KEYY in dialog", keyEntered);
                // if(verifyEnteredKey.equals(testContact.getKey())){
                //    return true;
                // }
                //   keyChecker(verifyEnteredKey);
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

}
