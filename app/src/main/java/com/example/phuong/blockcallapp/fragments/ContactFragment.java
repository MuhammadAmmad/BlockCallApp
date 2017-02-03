package com.example.phuong.blockcallapp.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.phuong.blockcallapp.R;
import com.example.phuong.blockcallapp.adapters.ListContactAdapter;
import com.example.phuong.blockcallapp.models.Contact;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phuong on 03/02/2017.
 */
@EFragment(R.layout.fragment_contact)
public class ContactFragment extends BaseFragment {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    @ViewById(R.id.recyclerViewContact)
    RecyclerView mRecyclerViewContact;
    private ListContactAdapter mAdapter;
    private List<Contact> mContacts;

    @Override
    void inits() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewContact.setLayoutManager(layoutManager);
        getDataFromDevice();
        getDataFromSimCard();
        mAdapter = new ListContactAdapter(mContacts, getContext());
        mRecyclerViewContact.setAdapter(mAdapter);
    }

    public List<Contact> getDataFromDevice() {
        mContacts = new ArrayList<>();
        Contact contact;

        //check permission
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int check = getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS);
            if (check != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return null;
            }
        }

        ContentResolver cr = getActivity().getContentResolver();
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
                        mContacts.add(contact);
                    }
                    pCur.close();
                }
            }
        }
        return mContacts;
    }

    public List<Contact> getDataFromSimCard() {
        if (mContacts != null) {
            Uri simUri = Uri.parse("content://icc/adn");
            Cursor cursorSim = getContext().getContentResolver().query(simUri, null, null, null, null);
            Contact contact;
            while (cursorSim.moveToNext()) {
                contact = new Contact();
                contact.setName(cursorSim.getString(cursorSim.getColumnIndex("name")));
                contact.setPhoneNumber(cursorSim.getString(cursorSim.getColumnIndex("number")));
                mContacts.add(contact);

            }
        } else {
            mContacts = new ArrayList<>();
        }
        return mContacts;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getDataFromDevice();
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), getActivity().getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
