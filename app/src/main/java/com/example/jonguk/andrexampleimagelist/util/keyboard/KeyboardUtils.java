package com.example.jonguk.andrexampleimagelist.util.keyboard;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Jonguk on 2017. 4. 1..
 */

public class KeyboardUtils {

    public static void hide(View view) {
        if (view == null) { return; }
        view.post(() -> {
            view.clearFocus();
            InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });
    }

}
