package com.example.phuong.blockcallapp.ui.contact;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.phuong.blockcallapp.R;
import com.example.phuong.blockcallapp.adapters.ListContactAdapter;
import com.example.phuong.blockcallapp.models.Contact;
import com.example.phuong.blockcallapp.ui.BaseFragment;
import com.example.phuong.blockcallapp.utils.Constant;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by phuong on 03/02/2017.
 */
@EFragment(R.layout.fragment_contact)
public class ContactFragment extends BaseFragment {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100;
    final private int REQUEST_CODE_READ_CONTACT_DEVICE = 101;

    @ViewById(R.id.recyclerViewContact)
    RecyclerView mRecyclerViewContact;
    @ViewById(R.id.progressBar)
    ProgressBar mProgressBar;
    @ViewById(R.id.edtSearch)
    EditText mEdtSearch;

    private Contact mContact;
    private boolean isFirst = true;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mContact = intent.getExtras().getParcelable(ListContactAdapter.REQUEST_CALL_PHONE);
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }
    };
    private ListContactAdapter mAdapter;
    private List<Contact> mContacts;

    @Override
    public void inits() {
        mContacts = new ArrayList<>();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(ListContactAdapter.REQUEST_CALL_PHONE));
        mEdtSearch.setVisibility(View.GONE);

        mRecyclerViewContact.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ListContactAdapter(mContacts, getActivity());
        mRecyclerViewContact.setAdapter(mAdapter);

        new getDataAction().execute();
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

    public void request() {
        if (isFirst) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    public void accessGetDataFromSim() {
        Uri simUri = Uri.parse("content://icc/adn");
        List<Contact> contacts = new ArrayList<>();
        Cursor cursorSim = getActivity().getContentResolver().query(simUri, null, null, null, null);
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
        mAdapter.notifyDataSetChanged();
    }

    public void accessGetDataFromDevice() {
        Contact contact;
        List<Contact> contacts = new ArrayList<>();
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
                        contacts.add(contact);
                    }
                    pCur.close();
                }
            }
        }
        mContacts.addAll(contacts);
        mAdapter.notifyDataSetChanged();

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
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + mContact.getPhoneNumber()));
                    startActivity(callIntent);
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT)
                            .show();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @TextChange(R.id.edtSearch)
    void onTextChangesSearch(CharSequence query) {
        query = query.toString().toLowerCase();
        final List<Contact> filteredList = new ArrayList<>();
        if (mContacts != null) {
            for (int i = 0; i < mContacts.size(); i++) {

                final String text = mContacts.get(i).getName().toLowerCase();

                if (Constant.unAccent(text).contains(Constant.unAccent(query.toString()))) {
                    filteredList.add(mContacts.get(i));
                }
            }
        }
        mAdapter = new ListContactAdapter(filteredList, getActivity());
        mRecyclerViewContact.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private class getDataAction extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void result) {
            mProgressBar.setVisibility(View.GONE);
            Collections.sort(mContacts, new Comparator<Contact>() {
                public int compare(Contact v1, Contact v2) {
                    return v1.getName().compareTo(v2.getName());
                }
            });
            mEdtSearch.setVisibility(View.VISIBLE);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            mRecyclerViewContact.setLayoutManager(layoutManager);
            mAdapter = new ListContactAdapter(mContacts, getContext());
            mRecyclerViewContact.setAdapter(mAdapter);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getDataFromDevice();
            getDataFromSimCard();
            return null;
        }

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}
