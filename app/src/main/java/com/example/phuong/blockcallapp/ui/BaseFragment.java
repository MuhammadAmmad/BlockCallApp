package com.example.phuong.blockcallapp.ui;

import android.support.v4.app.Fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

/**
 * Created by phuong on 03/02/2017.
 */
@EFragment
public abstract class BaseFragment extends Fragment {
    @AfterViews
    public abstract void inits();
}
