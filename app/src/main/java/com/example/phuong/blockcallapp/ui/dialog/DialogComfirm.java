package com.example.phuong.blockcallapp.ui.dialog;

import android.text.TextUtils;
import android.widget.TextView;

import com.example.phuong.blockcallapp.BaseDialog;
import com.example.phuong.blockcallapp.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

/**
 * Copyright@ AsianTech.Inc
 * Created by Phuong Pham T. on 29/06/2017.
 */
@EFragment(R.layout.confirm_dialog)
public class DialogComfirm extends BaseDialog {

    public void show(android.support.v4.app.FragmentManager fragmentManager, OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
        show(fragmentManager, "");
    }

    /**
     * OnItemClickListener
     */
    public interface OnItemClickListener {
        void onConfirmClick();
    }

    @FragmentArg
    String mTitle;
    @FragmentArg
    String mMessage;

    @ViewById(R.id.tvTitle)
    TextView mTvTitle;
    @ViewById(R.id.tvMessage)
    TextView mTvMessage;
    @ViewById(R.id.tvCancel)
    TextView mTvCancel;
    @ViewById(R.id.tvOk)
    TextView mTvOk;

    private OnItemClickListener mListener;

    @Click(R.id.tvCancel)
    public void cancel() {
        dismiss();
    }

    @Click(R.id.tvOk)
    public void ok() {
        if (mListener != null) {
            mListener.onConfirmClick();
        }
        dismiss();
    }

    @Override
    protected void init() {
        if (!TextUtils.isEmpty(mTitle)) {
            mTvTitle.setText(mTitle);
        }
        if (!TextUtils.isEmpty(mMessage)) {
            mTvMessage.setText(mMessage);
        }
    }
}
