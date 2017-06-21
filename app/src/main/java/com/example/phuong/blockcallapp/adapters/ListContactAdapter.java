package com.example.phuong.blockcallapp.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phuong.blockcallapp.R;
import com.example.phuong.blockcallapp.models.Contact;
import com.example.phuong.blockcallapp.models.ContactBlock;
import com.example.phuong.blockcallapp.utils.Permissions;

import java.util.List;

/**
 * Created by phuong on 03/02/2017.
 */

public class ListContactAdapter extends RecyclerView.Adapter<ListContactAdapter.MyHolder> {

    public static final String REQUEST_CALL_PHONE = "request_call_phone";
    private List<Contact> mContacts;
    private Context mContext;

    public ListContactAdapter(List<Contact> mContacts, Context mContext) {
        this.mContacts = mContacts;
        this.mContext = mContext;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        final Contact contact = mContacts.get(position);
        holder.mTvNameContact.setText(contact.getName());
        holder.mTvPhoneContact.setText(contact.getPhoneNumber());

        holder.mImgBlockCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactBlock contactBlock = new ContactBlock(contact.getName(), contact.getPhoneNumber(), false);
                contactBlock.save();
            }
        });

        holder.mImgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    /*
                    * Send message to fragment to request permission
                    * */
                    broadcastIntent(contact);

                   /* Permissions permissions = new Permissions(mContext);
                    permissions.checkCallPhonePermission();*/
                    return;
                }
                mContext.startActivity(callIntent);
            }
        });
    }

    public void broadcastIntent(Contact contact) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(REQUEST_CALL_PHONE,contact);
        intent.putExtras(bundle);
        intent.setAction(REQUEST_CALL_PHONE);
        mContext.sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return mContacts == null ? 0 : mContacts.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView mTvNameContact;
        private TextView mTvPhoneContact;
        private ImageView mImgBlockCall;
        private ImageView mImgCall;
        private ImageView mImgMessage;
        private ImageView mImgHidden;

        public MyHolder(View itemView) {
            super(itemView);
            mTvNameContact = (TextView) itemView.findViewById(R.id.tvNameContact);
            mTvPhoneContact = (TextView) itemView.findViewById(R.id.tvPhoneContact);
            mImgBlockCall = (ImageView) itemView.findViewById(R.id.imgBlockCall);
            mImgCall = (ImageView) itemView.findViewById(R.id.imgCall);
            mImgMessage = (ImageView) itemView.findViewById(R.id.imgMessage);
            mImgHidden = (ImageView) itemView.findViewById(R.id.imgHidden);
        }
    }
}
