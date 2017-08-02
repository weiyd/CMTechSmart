package com.cmtech.android.common;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.cmtech.android.account.AccountInfo;

import org.litepal.LitePalApplication;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

/**
 * Created by gdmc on 2017/7/9.
 */

public class MyApplication extends Application {
    private static Context context;
    public static String imageFilePath;

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;

    private AccountInfo account;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();
        imageFilePath = getImageFilePath();

        LitePalApplication.initialize(context);
        Connector.getDatabase();
        account = getPrefAccountInfo();
    }

    public static Context getContext() {
        return context;
    }

    public AccountInfo getAccountInfo() {return account;}

    public void setAccountInfo(AccountInfo account) {
        this.account = account;
    }

    public static boolean accountExist(String name) {
        List<AccountInfo> accountInfos = DataSupport.where("name = ?", name).find(AccountInfo.class);
        return accountInfos.size() != 0;
    }

    public static void saveNewAccount(String name, String password) {
        AccountInfo info = new AccountInfo();
        info.setName(name);
        info.setPassword(password);
        info.save();
    }

    public static boolean accountInfoCorrect(String name, String password) {
        List<AccountInfo> accountInfos = DataSupport.where("name = ? and password = ?", name, password)
                .find(AccountInfo.class);
        return accountInfos.size() != 0;
    }

    public void saveAccountInPreference(String name, String password, boolean isRemember) {
        if(isRemember) {
            editor.putString("name", name);
            editor.putString("password", password);
            editor.putBoolean("isRemember", true);
            LogUtil.i("Account", "info saved");
        }
        else {
            LogUtil.i("Account", "info clear");
            editor.clear();
        }
        editor.commit();
    }


    private static String getImageFilePath() {
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        }
        else {
            return context.getFilesDir().getAbsolutePath() + Environment.DIRECTORY_PICTURES;
        }
    }

    private AccountInfo getPrefAccountInfo() {
        boolean isRemember = pref.getBoolean("isRemember", false);
        if(isRemember) {
            String name = pref.getString("name", "");
            String password = pref.getString("password", "");
            if(MyApplication.accountInfoCorrect(name, password)) {
                account = new AccountInfo();
                account.setName(name);
                account.setPassword(password);
                return account;
            }
        }
        return null;
    }
}
