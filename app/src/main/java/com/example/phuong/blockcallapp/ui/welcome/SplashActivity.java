package com.example.phuong.blockcallapp.ui.welcome;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.example.phuong.blockcallapp.R;
import com.example.phuong.blockcallapp.models.Contact;
import com.example.phuong.blockcallapp.ui.BaseActivity;

import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phuong on 03/02/2017.
 */
@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {
    private static final int REQUEST_CODE_READ_CONTACT_DEVICE = 101;
    private boolean isFirst = true;
    private List<Contact> mContacts = new ArrayList<>();

    @Override
    public void inits() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 2000);
    }

    public void intentMain() {
        MainActivity_.intent(this).mContacts(mContacts).start();
        finish();
    }

    public void accessGetDataFromSim() {
        Uri simUri = Uri.parse("content://icc/adn");
        List<Contact> contacts = new ArrayList<>();
        Cursor cursorSim = getContentResolver().query(simUri, null, null, null, null);
        Contact contact;
        if (cursorSim != null) {
            while (cursorSim.moveToNext()) {
                contact = new Contact();
                contact.setName(cursorSim.getString(cursorSim.getColumnIndex("name")));
                contact.setPhoneNumber(cursorSim.getString(cursorSim.getColumnIndex("number")));
                contacts.add(contact);
            }
            cursorSim.close();
        }
        mContacts.addAll(contacts);
    }

    public void accessGetDataFromDevice() {
        Contact contact;
        List<Contact> contacts = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contact = new Contact(name, phoneNo);
                        contacts.add(contact);
                    }
                    pCur.close();
                }
            }
        }
        mContacts.addAll(contacts);
        intentMain();
    }

    public void getDataFromDevice() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            request();
        } else {
            accessGetDataFromDevice();
        }
    }

    public void getDataFromSimCard() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            request();
        } else {
            accessGetDataFromSim();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void request() {
        if (isFirst) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , REQUEST_CODE_READ_CONTACT_DEVICE);
                        }
                    });
                    builder.show();
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                            REQUEST_CODE_READ_CONTACT_DEVICE);
                }
            }
            isFirst = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ_CONTACT_DEVICE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    accessGetDataFromDevice();
                    accessGetDataFromSim();

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void getData() {
        getDataFromDevice();
        getDataFromSimCard();
    }

}
