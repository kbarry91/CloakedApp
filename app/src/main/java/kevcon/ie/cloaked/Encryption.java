package kevcon.ie.cloaked;

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


/**
 * <h1>Encryption</h1>
 * Encryption is a class that implements a Porta Cipher to encrypt a message.
 *
 * @author kevin barry
 * @since 25/4/2018
 */
public class Encryption {

    // tag for debugging
    private final static String DTAG = "ENC_DEBUG:";
    private static int keyLetter = 0;
    private static final String[][] tableau = {
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

    /**
     * Apply swap cipher to letter using keyLetter.
     *
     * @param letter    The char to be encrypted
     * @param cipherKey The cipher key
     * @return The encrypted char
     */
    public static char encrypt(char letter, String cipherKey) {
        // DEBUG
        Log.d(DTAG, "-----key letter    : " + keyLetter + "-");

        int row = 0;
        int col;

        // can improve this loop
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

    /**
     * Parse the message char by char to the cipher.
     *
     * @param message    The message to be encrypted
     * @param cloakedKey The encryption key
     * @return The encrypted message
     */
    public static String DecryptMessage(String message, String cloakedKey) {
        StringBuilder cloakedMessage = new StringBuilder();

        // DEBUG
        Log.d(DTAG, "-----Original Message : " + message + "-");
        Log.d(DTAG, "-----Original Key     : " + cloakedKey + "-");

        // start at 18 , first 18 chars are defaulted
        int pos = 18;
        int sentLength = message.length();
        // DEBUG
        Log.d(DTAG, "-----Message length: " + (message.length() - 18) + "-");

        while (pos < sentLength) {// while end of sentence not reached

            char letter = encrypt(Character.toUpperCase(message.charAt(pos)), cloakedKey.toUpperCase());
            cloakedMessage.append(letter);

            // DEBUG
            Log.d(DTAG, "-----cloak after     : " + cloakedMessage.toString() + "-");

            pos++;
        } // while

        // Reset keyLetter to 0 for next encryption
        keyLetter = 0;
        return cloakedMessage.toString();
    }

    /**
     * startDecrypt Displays a pop up window to enter a Cloaked key.
     * If the key is valid the the message is decrypted ,decrypted message will be shown in view.
     *
     * @param messageText The message to be decrypted.
     * @param contact     The contact relation.
     */
    public static void startDecrypt(final String messageText, final Contacts contact, final Context ctx) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Cloaked Key");

        // Set up the input
        View viewInflated = LayoutInflater.from(ctx).inflate(R.layout.key_entry_dialog, null, false);

        final EditText input = viewInflated.findViewById(R.id.input);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String keyEntered;
                keyEntered = input.getText().toString();
                if (keyEntered.equals(contact.getKey())) {

                    //pop up dialog to display message
                    final Dialog msgDialog = new Dialog(ctx);
                    msgDialog.setContentView(R.layout.view_message_dialog);
                    msgDialog.setTitle("Decrypted message");

                    TextView decMsg = msgDialog.findViewById(R.id.decrypted_text);

                    // decrypt the message
                    String decryptMsg = DecryptMessage(messageText, contact.getKey());
                    // set the pop up text
                    decMsg.setText(decryptMsg);

                    Log.d("DEC CHECK", "imported message" + messageText);

                    Button dialogButton = msgDialog.findViewById(R.id.decrypted_button);

                    // Close the dialog on click
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            msgDialog.dismiss();
                        }
                    });

                    msgDialog.show();

                } else {
                    Toast.makeText(ctx, "Invalid key",
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

}
