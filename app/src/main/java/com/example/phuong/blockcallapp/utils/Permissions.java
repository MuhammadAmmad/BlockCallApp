package com.example.phuong.blockcallapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by phuong on 09/02/2017.
 */

public class Permissions extends AppCompatActivity {
    private Context mContext;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100;

    public Permissions(Context context) {
        this.mContext = context;
    }

    public void checkCallPhonePermission() {
        ContextCompat.checkSelfPermission(mContext,Manifest.permission.CALL_PHONE);
        if (ContextCompat.checkSelfPermission(mContext,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
            Log.d("tag11","abc");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                Log.d("tag11","abc1");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("tag11","granted");
                    Toast.makeText(mContext, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("tag11","denied");
                    Toast.makeText(mContext, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
