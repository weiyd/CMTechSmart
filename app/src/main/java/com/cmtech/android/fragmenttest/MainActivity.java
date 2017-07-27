package com.cmtech.android.fragmenttest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cmtech.android.device.DeviceFragment;
import com.cmtech.android.globalcommon.ActivityCollector;
import com.cmtech.android.globalcommon.BasicActivity;
import com.cmtech.android.globalcommon.MyApplication;
import com.cmtech.android.mall.MallFragment;
import com.cmtech.android.news.NewsFragment;

import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BasicActivity {
    public static final int POS_NEWS = 0;
    public static final int POS_MALL = 1;
    public static final int POS_DEVICE = 2;
    public static final int POS_SOCIAL = 3;
    public static final int POS_ACCOUNT = 4;

    public static final String STR_NEWS = "新闻";
    public static final String STR_MALL = "商城";
    public static final String STR_DEVICE = "设备";
    public static final String STR_SOCIAL = "社区";
    public static final String STR_ACCOUNT = "我的";
    public static final String[] FRAG_TITLE = new String[]{STR_NEWS,STR_MALL,STR_DEVICE,STR_SOCIAL,STR_ACCOUNT};

    private static NewsFragment newsFragment = new NewsFragment();
    private static MallFragment mallFragment = new MallFragment();
    private static DeviceFragment deviceFragment = new DeviceFragment();
    private static SocialFragment socialFragment = new SocialFragment();
    private static AccountFragment accountFragment = new AccountFragment();
    private static Fragment[] fragments = new Fragment[]{newsFragment, mallFragment, deviceFragment, socialFragment, accountFragment};

    public static final int INITPOS = POS_DEVICE;
    public int curPos = -1;

    public static final List<UnderTitleItem> itemList = new ArrayList<>();
    static {
        UnderTitleItem news = new UnderTitleItem(R.drawable.news, STR_NEWS);
        itemList.add(news);
        UnderTitleItem mall = new UnderTitleItem(R.drawable.mall, STR_MALL);
        itemList.add(mall);
        UnderTitleItem device = new UnderTitleItem(R.drawable.device, STR_DEVICE);
        itemList.add(device);
        UnderTitleItem social = new UnderTitleItem(R.drawable.social, STR_SOCIAL);
        itemList.add(social);
        UnderTitleItem account = new UnderTitleItem(R.drawable.account, STR_ACCOUNT);
        itemList.add(account);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Connector.getDatabase();

        changeFragment(INITPOS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //changeFragment(curPos);
        //replaceFragment(fragments[curPos]);
        //setTitle(FRAG_TITLE[curPos]);
    }

    /**
     * 打开或关闭蓝牙后的回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == DeviceFragment.REQUEST_BT_OPEN) {
                deviceFragment.startScan();
            }
        } else if (resultCode == RESULT_CANCELED) {
            ActivityCollector.finishAll();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void changeFragment(int position) {
        if(curPos == position) return;

        replaceFragment(fragments[position]);
        setTitle(FRAG_TITLE[position]);
        curPos = position;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if(curPos >= 0 && curPos < fragments.length){
            transaction.hide(fragments[curPos]);
        }
        if(!fragment.isAdded()) {
            transaction.add(R.id.top_layout, fragment);
        }
        else {
            transaction.show(fragment);
        }
        //transaction.replace(R.id.top_layout, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("退出");
        builder.setMessage("确定退出吗？");
        builder.setCancelable(true);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCollector.finishAll();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aboutus:
                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.exit:
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }
}
