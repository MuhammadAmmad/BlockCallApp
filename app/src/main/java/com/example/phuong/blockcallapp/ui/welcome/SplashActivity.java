package com.example.phuong.blockcallapp.ui.welcome;

import android.os.Handler;

import com.example.phuong.blockcallapp.R;
import com.example.phuong.blockcallapp.activities.MainActivity_;
import com.example.phuong.blockcallapp.ui.BaseActivity;

import org.androidannotations.annotations.EActivity;

/**
 * Created by phuong on 03/02/2017.
 */
@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {

    @Override
    void inits() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                intentMain();
            }
        }, 2000);
    }

    public void intentMain() {
        MainActivity_.intent(this).start();
    }

}
