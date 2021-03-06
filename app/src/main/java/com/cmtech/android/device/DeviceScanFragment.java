package com.cmtech.android.device;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cmtech.android.center.R;
import com.cmtech.android.common.ActivityCollector;
import com.vise.baseble.CMBluetoothScanner;
import com.vise.baseble.callback.scan.PeriodScanCallback;
import com.vise.baseble.model.BluetoothLeDevice;
import com.vise.baseble.utils.BleUtil;
import com.vise.log.ViseLog;
import com.vise.log.inner.LogcatTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gdmc on 2017/7/10.
 */

public class DeviceScanFragment extends Fragment {
    private Activity activity;

    private List<ScanDeviceInfo> deviceList = new ArrayList<>();
    private DetailScanDeviceAdapter adapter;

    private static final int SCAN_PERIOD = 10000;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    public static final int REQUEST_BT_OPEN = 2;

    private PeriodScanCallback periodScanCallback = new PeriodScanCallback() {
        @Override
        public void scanTimeout() {
            ViseLog.i("scan timeout");
        }

        @Override
        public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {
            ViseLog.i("Founded Scan Device:" + bluetoothLeDevice);

            ScanDeviceInfo deviceInfo = new ScanDeviceInfo();
            deviceInfo.setDevice(bluetoothLeDevice);
            adapter.addOneDeviceInfo(deviceInfo);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_devicescan, container, false);

        RecyclerView scanView = (RecyclerView) view.findViewById(R.id.rvscandevice);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        scanView.setLayoutManager(manager);
        deviceList.clear();
        adapter = new DetailScanDeviceAdapter(deviceList);
        scanView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();

        ViseLog.getLogConfig().configAllowLog(true);//配置日志信息
        ViseLog.plant(new LogcatTree());//添加Logcat打印信息
        //蓝牙信息初始化，全局唯一，必须在应用初始化时调用
        CMBluetoothScanner.getInstance().init(activity);

        Button btnScan = (Button) activity.findViewById(R.id.btnscan);
        Button btnConnect = (Button) activity.findViewById(R.id.btnconnect);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clearDeviceList();
                checkBluetoothPermission();
            }
        });

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ScanDeviceInfo> list = adapter.getNeedConnectDevice();
                if(list.size() == 0) return;
                stopScan();
                DeviceActivity.actionStart(activity, list);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        checkBluetoothPermission();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopScan();
    }

    /**
     * 检查蓝牙权限
     */
    private void checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //校验是否已具有模糊定位权限
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_REQUEST_COARSE_LOCATION);
            } else {
                //具有权限
                scanBluetooth();
            }
        } else {
            //系统不高于6.0直接执行
            scanBluetooth();
        }
    }

    /**
     * 对返回的值进行处理，相当于StartActivityForResult
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    /**
     * 权限申请的下一步处理
     * @param requestCode 申请码
     * @param grantResults 申请结果
     */
    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_COARSE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //同意权限
                scanBluetooth();
            } else {
                // 权限拒绝，提示用户开启权限
                denyPermission();
            }
        }
    }

    /**
     * 权限申请被拒绝的处理方式
     */
    private void denyPermission() {
        ActivityCollector.finishAll();
    }

    private void scanBluetooth() {
        if (BleUtil.isBleEnable(activity)) {
            startScan();
        } else {
            BleUtil.enableBluetooth(activity, REQUEST_BT_OPEN);
        }
    }

    /**
     * 开始扫描
     */
    public void startScan() {
        CMBluetoothScanner.getInstance().setScanTimeout(SCAN_PERIOD).startScan(periodScanCallback);
    }

    /**
     * 停止扫描
     */
    private void stopScan() {
        CMBluetoothScanner.getInstance().stopScan(periodScanCallback);
    }

}
