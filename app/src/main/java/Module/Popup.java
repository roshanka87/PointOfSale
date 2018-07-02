package Module;

import android.app.AlertDialog;
import android.content.Context;


public class Popup {
    public static void showDefaultMessage(Context c, String message) {
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setMessage(message)
                .setNegativeButton("OK", null)
                .create();
        dialog.show();
    }
}
