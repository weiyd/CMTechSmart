package com.cmtech.android.device;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.cmtech.android.fragmenttest.R;
import com.cmtech.android.globalcommon.BasicActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeviceActivity extends BasicActivity {
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
        List<ScanDeviceInfo> deviceList = (ArrayList<ScanDeviceInfo>) intent.getSerializableExtra("device_list");
        String temp = "";
        for(ScanDeviceInfo info : deviceList) {
            temp += info.getAddress() + " ";
        }
        Toast.makeText(DeviceActivity.this, temp, Toast.LENGTH_SHORT).show();
    }
}
