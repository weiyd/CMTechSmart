package com.cmtech.android.globalcommon;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import org.litepal.LitePalApplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by gdmc on 2017/7/9.
 */

public class MyApplication extends Application {
    public static Context context;
    public static String imageFilePath;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        imageFilePath = getImageFilePath();

        LitePalApplication.initialize(context);

    }

    public static Context getContext() {
        return context;
    }

    /*
    public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024]; // 用数据装
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            outstream.write(buffer, 0, len);
        }
        outstream.close();
        // 关闭流一定要记得。
        return outstream.toByteArray();
    }
    */

    private static String getImageFilePath() {
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        }
        else {
            return context.getFilesDir().getAbsolutePath() + Environment.DIRECTORY_PICTURES;
        }
    }
}
