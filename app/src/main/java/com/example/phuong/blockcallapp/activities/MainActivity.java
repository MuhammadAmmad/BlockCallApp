package com.example.phuong.blockcallapp.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.phuong.blockcallapp.R;
import com.example.phuong.blockcallapp.adapters.TabAdapter;
import com.example.phuong.blockcallapp.eventbus.BusProvider;
import com.example.phuong.blockcallapp.eventbus.ObjectListContact;
import com.example.phuong.blockcallapp.eventbus.RequestEvent;
import com.example.phuong.blockcallapp.fragments.ContactBlockFragment_;
import com.example.phuong.blockcallapp.fragments.ContactFragment_;
import com.example.phuong.blockcallapp.fragments.SettingsFragment_;
import com.example.phuong.blockcallapp.models.Contact;
import com.example.phuong.blockcallapp.utils.Constant;
import com.orm.SugarContext;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @ViewById(R.id.tabs)
    TabLayout mTab;
    @ViewById(R.id.viewPagerTab)
    ViewPager mViewPagerTab;

    @Override
    void inits() {
        SugarContext.init(this);
        setupViewPager(mViewPagerTab);
        mTab.setupWithViewPager(mViewPagerTab);
    }

    private void setupViewPager(ViewPager viewPager) {
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        String[] title = getResources().getStringArray(R.array.title_tab_layout);
        adapter.addFragment(ContactFragment_.builder().build(), title[0]);
        adapter.addFragment(ContactBlockFragment_.builder().build(), title[1]);
        adapter.addFragment(SettingsFragment_.builder().build(), title[2]);
        viewPager.setAdapter(adapter);
    }

}
