package com.xtel.demolibrary;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Mr. M.2 on 1/4/2017.
 */

public class DemoLibrary {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
