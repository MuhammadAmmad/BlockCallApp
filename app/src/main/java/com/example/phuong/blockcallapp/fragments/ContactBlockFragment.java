package com.example.phuong.blockcallapp.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.phuong.blockcallapp.R;
import com.example.phuong.blockcallapp.adapters.ListContactBlockAdapter;
import com.example.phuong.blockcallapp.models.ContactBlock;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phuong on 03/02/2017.
 */
@EFragment(R.layout.fragment_contact_block)
public class ContactBlockFragment extends BaseFragment {
    @ViewById(R.id.recyclerViewBlock)
    RecyclerView mRecyclerViewBlock;
    private ListContactBlockAdapter mAdapter;
    private List<ContactBlock> mContactBlocks;

    @Override
    void inits() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewBlock.setLayoutManager(layoutManager);
        getDataBlockContact();
        mAdapter = new ListContactBlockAdapter(mContactBlocks, getContext());
        mRecyclerViewBlock.setAdapter(mAdapter);
    }

    public void getDataBlockContact() {
        mContactBlocks = new ArrayList<>();
        mContactBlocks = ContactBlock.listAll(ContactBlock.class);
        Log.d("tag11", mContactBlocks.size() + " 123 ");
    }
}
