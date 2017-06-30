package com.example.phuong.blockcallapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

/**
 * Copyright@ AsianTech.Inc
 * Created by Phuong Pham T. on 29/06/2017.
 */
@EFragment
public abstract class BaseDialog extends android.support.v4.app.DialogFragment {
    @AfterViews
    protected abstract void init();

    @Override
    public void onResume() {
        super.onResume();
        /**
         * Set width & height of dialog on onResume method follow this
         * http://stackoverflow.com/questions/14946887/setting-the-size-of-a-dialogfragment
         */
        if (getDialog().getWindow() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = getWidthDialog();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setAttributes(params);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        if (dialog.getWindow() != null) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return dialog;
    }

    public void show(android.support.v4.app.FragmentManager manager) {
        if (manager != null && manager.beginTransaction() != null) {
            super.show(manager, "");
        }
    }

    protected int getWidthDialog() {
        return getResources().getDimensionPixelSize(R.dimen.dialog_width);
    }

}
