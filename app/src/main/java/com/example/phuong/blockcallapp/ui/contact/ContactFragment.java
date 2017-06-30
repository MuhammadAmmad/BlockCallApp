package com.example.phuong.blockcallapp.ui.contact;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.phuong.blockcallapp.R;
import com.example.phuong.blockcallapp.models.Contact;
import com.example.phuong.blockcallapp.models.ContactBlock;
import com.example.phuong.blockcallapp.ui.BaseFragment;
import com.example.phuong.blockcallapp.ui.dialog.DialogComfirm;
import com.example.phuong.blockcallapp.ui.dialog.DialogComfirm_;
import com.example.phuong.blockcallapp.ui.welcome.MainActivity;
import com.example.phuong.blockcallapp.utils.Constant;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phuong on 03/02/2017.
 */
@EFragment(R.layout.fragment_contact)
public class ContactFragment extends BaseFragment implements ListContactAdapter.itemClickHandle {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100;

    @ViewById(R.id.recyclerViewContact)
    RecyclerView mRecyclerViewContact;
    @ViewById(R.id.progressBar)
    ProgressBar mProgressBar;
    @ViewById(R.id.edtSearch)
    EditText mEdtSearch;

    private Contact mContact;


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

        mContacts = ((MainActivity) getActivity()).getListContact();
        mRecyclerViewContact.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ListContactAdapter(mContacts, getActivity(), this);
        mRecyclerViewContact.setAdapter(mAdapter);
        mProgressBar.setVisibility(View.GONE);

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
        mAdapter = new ListContactAdapter(filteredList, getActivity(), this);
        mRecyclerViewContact.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
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


    @Override
    public void clickBlockCall(final Contact contact) {
        DialogComfirm dialogComfirm = DialogComfirm_.builder().mTitle(getString(R.string.title_dialog_block_call)).mMessage(getString(R.string.msg_dialog_block_call)).build();
        dialogComfirm.show(getFragmentManager(), new DialogComfirm.OnItemClickListener() {
            @Override
            public void onConfirmClick() {
                ContactBlock contactBlock = new ContactBlock(contact.getName(), contact.getPhoneNumber(), false);
                contactBlock.save();
                Toast.makeText(getContext(), getString(R.string.msg_success), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void clickBlockMessage(Contact contact) {

    }
}
