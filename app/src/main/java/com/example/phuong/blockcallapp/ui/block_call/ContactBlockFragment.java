package com.example.phuong.blockcallapp.ui.block_call;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.example.phuong.blockcallapp.R;
import com.example.phuong.blockcallapp.adapters.ListContactBlockAdapter;
import com.example.phuong.blockcallapp.models.ContactBlock;
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
@EFragment(R.layout.fragment_contact_block)
public class ContactBlockFragment extends BaseFragment {
    @ViewById(R.id.recyclerViewBlock)
    RecyclerView mRecyclerViewBlock;
    @ViewById(R.id.edtSearch)
    EditText mEdtSearch;
    private ListContactBlockAdapter mAdapter;
    private List<ContactBlock> mContactBlocks;

    @Override
    public void inits() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewBlock.setLayoutManager(layoutManager);
        getDataBlockContact();
        Collections.sort(mContactBlocks, new Comparator<ContactBlock>() {
            public int compare(ContactBlock v1, ContactBlock v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });
        mAdapter = new ListContactBlockAdapter(mContactBlocks, getContext());
        mRecyclerViewBlock.setAdapter(mAdapter);
    }

    public void getDataBlockContact() {
        mContactBlocks = new ArrayList<>();
        mContactBlocks = ContactBlock.listAll(ContactBlock.class);
    }

    @TextChange(R.id.edtSearch)
    void onTextChangesSearch(CharSequence query) {
        query = query.toString().toLowerCase();
        final List<ContactBlock> filteredList = new ArrayList<>();

        for (int i = 0; i < mContactBlocks.size(); i++) {

            final String text = mContactBlocks.get(i).getName().toLowerCase();

            if (Constant.unAccent(text).contains(Constant.unAccent(query.toString()))) {
                filteredList.add(mContactBlocks.get(i));
            }
        }

        mRecyclerViewBlock.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ListContactBlockAdapter(filteredList, getActivity());
        mRecyclerViewBlock.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

}
