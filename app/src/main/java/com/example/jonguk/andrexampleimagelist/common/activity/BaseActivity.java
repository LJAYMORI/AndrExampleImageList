package com.example.jonguk.andrexampleimagelist.common.activity;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Jonguk on 2017. 3. 28..
 */

public class BaseActivity extends RxActivity {
    private Unbinder mUnbinder;
    private Snackbar mSnackbar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mSnackbar != null && mSnackbar.isShown()) {
            mSnackbar.dismiss();
            mSnackbar = null;
        }
        super.onDestroy();
    }

    protected void showToast(@StringRes int resId) {
        showToast(getString(resId));
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showSnackBar(View view, String msg) {
        if (view != null) {
            mSnackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
            mSnackbar.show();
        }
    }

    protected void showSnackBar(View view, String msg, String actionString, View.OnClickListener listener) {
        if (view != null) {
            mSnackbar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE);
            mSnackbar.setActionTextColor(Color.RED)
                    .setAction(actionString, listener).show();
        }
    }

    protected void hideSnackBar() {
        if (mSnackbar != null && mSnackbar.isShown()) {
            mSnackbar.dismiss();
        }
    }

    protected String getStringWithoutException(@StringRes int resId) {
        try {
            return getString(resId);
        } catch (Exception e) {
            return "(null)";
        }
    }
}
