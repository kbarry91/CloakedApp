package kevcon.ie.cloaked;

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

}
