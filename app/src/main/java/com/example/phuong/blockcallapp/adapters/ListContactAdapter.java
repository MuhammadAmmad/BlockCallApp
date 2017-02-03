package com.example.phuong.blockcallapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phuong.blockcallapp.R;
import com.example.phuong.blockcallapp.models.Contact;

import java.util.List;

/**
 * Created by phuong on 03/02/2017.
 */

public class ListContactAdapter extends RecyclerView.Adapter<ListContactAdapter.MyHolder> {
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
        Contact contact = mContacts.get(position);
        holder.mTvNameContact.setText(contact.getName());
        holder.mTvPhoneContact.setText(contact.getPhoneNumber());
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
