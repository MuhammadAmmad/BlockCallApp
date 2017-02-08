package com.example.phuong.blockcallapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phuong.blockcallapp.R;
import com.example.phuong.blockcallapp.models.ContactBlock;

import java.util.List;

/**
 * Created by phuong on 03/02/2017.
 */

public class ListContactBlockAdapter extends RecyclerView.Adapter<ListContactBlockAdapter.MyHolder> {
    private List<ContactBlock> mContactBlocks;
    private Context mContext;

    public ListContactBlockAdapter(List<ContactBlock> mContactBlocks, Context mContext) {
        this.mContactBlocks = mContactBlocks;
        this.mContext = mContext;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_block, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        final ContactBlock contactBlock = mContactBlocks.get(position);
        holder.mTvNameContact.setText(contactBlock.getName());
        holder.mTvPhoneContact.setText(contactBlock.getNumberPhone());

        holder.mImgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactBlock.delete();
                mContactBlocks.remove(contactBlock);
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mContactBlocks == null ? 0 : mContactBlocks.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView mTvNameContact;
        private TextView mTvPhoneContact;
        private ImageView mImgDelete;
        private ImageView mImgVisible;

        public MyHolder(View itemView) {
            super(itemView);
            mTvNameContact = (TextView) itemView.findViewById(R.id.tvNameContact);
            mTvPhoneContact = (TextView) itemView.findViewById(R.id.tvPhoneContact);
            mImgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
            mImgVisible = (ImageView) itemView.findViewById(R.id.imgVisible);
        }
    }
}
