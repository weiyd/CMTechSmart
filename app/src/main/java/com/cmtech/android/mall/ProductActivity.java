package com.cmtech.android.mall;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cmtech.android.device.DeviceActivity;
import com.cmtech.android.device.ScanDeviceInfo;
import com.cmtech.android.fragmenttest.R;

import java.io.Serializable;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    public static void actionStart(Context context, String url) {
        Intent intent = new Intent(context, ProductActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        WebView webView = (WebView)findViewById(R.id.product_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }
}
