package com.cmtech.android.device;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.cmtech.android.center.R;
import com.cmtech.android.common.BasicActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeviceActivity extends BasicActivity {
    private TabLayout mTabTl;
    private ViewPager mContentVp;

    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private List<ScanDeviceInfo> deviceList;
    private ContentPagerAdapter contentAdapter;

    public static void actionStart(Context context, List<ScanDeviceInfo> deviceConnect) {
        Intent intent = new Intent(context, DeviceActivity.class);
        intent.putExtra("device_list", (Serializable) deviceConnect);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        Intent intent = getIntent();
        deviceList = (ArrayList<ScanDeviceInfo>) intent.getSerializableExtra("device_list");

        mTabTl = (TabLayout) findViewById(R.id.tl_tab);
        mContentVp = (ViewPager) findViewById(R.id.vp_content);

        initContent();
        initTab();
    }

    private void initContent(){
        tabIndicators = new ArrayList<>();
        tabFragments = new ArrayList<>();
        for(ScanDeviceInfo deviceInfo : deviceList) {
            String tabName = ("".equals(deviceInfo.getNickName()) ? deviceInfo.getDevice().getName() : deviceInfo.getNickName());
            tabIndicators.add(tabName);
            tabFragments.add(DeviceFragment.newInstance(deviceInfo));
        }

        contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        mContentVp.setAdapter(contentAdapter);
    }

    private void initTab(){
        mTabTl.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabTl.setTabTextColors(Color.GRAY, Color.WHITE);
        mTabTl.setSelectedTabIndicatorColor(Color.WHITE);
        ViewCompat.setElevation(mTabTl, 10);
        mTabTl.setupWithViewPager(mContentVp);
    }

    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return tabIndicators.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabIndicators.get(position);
        }
    }

}
