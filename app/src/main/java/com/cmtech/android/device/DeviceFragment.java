package com.cmtech.android.device;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cmtech.android.device.DetailScanDeviceAdapter;
import com.cmtech.android.device.DeviceActivity;
import com.cmtech.android.device.SimpleScanDeviceAdapter;
import com.cmtech.android.device.ScanDeviceInfo;
import com.cmtech.android.fragmenttest.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gdmc on 2017/7/10.
 */

@TargetApi(21)
public class DeviceFragment extends Fragment {
    private List<ScanDeviceInfo> deviceList = new ArrayList<>();
    private DetailScanDeviceAdapter adapter;

    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_device, container, false);

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

        final Activity activity = getActivity();
        Button btnScan = (Button) activity.findViewById(R.id.btnscan);
        Button btnConnect = (Button) activity.findViewById(R.id.btnconnect);

        mHandler = new Handler();
        if (!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(activity, "BLE Not Supported", Toast.LENGTH_SHORT).show();
            btnScan.setEnabled(false);
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }

        if (Build.VERSION.SDK_INT >= 21) {
            mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
            settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();
            filters = new ArrayList<ScanFilter>();
        }

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clearDeviceList();
                scanLeDevice(true);
            }
        });

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ScanDeviceInfo> list = adapter.getNeedConnectDevice();
                if(list.size() == 0) return;
                scanLeDevice(false);
                DeviceActivity.actionStart(activity, list);
            }
        });

        scanLeDevice(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO request success
                }
                break;
        }
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT < 21) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    } else {
                        mLEScanner.stopScan(mScanCallback);

                    }
                }
            }, SCAN_PERIOD);
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mLEScanner.startScan(filters, settings, mScanCallback);
            }
        } else {
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            } else {
                mLEScanner.stopScan(mScanCallback);
            }
        }
    }

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.i("callbackType", String.valueOf(callbackType));
            Log.i("result", result.toString());
            BluetoothDevice btDevice = result.getDevice();
            String address = btDevice.getAddress();
            int pos = adapter.searchDeviceUsingAddress(address);

            if(pos == -1) {
                ScanDeviceInfo info = new ScanDeviceInfo(btDevice.getName(), "uuid", btDevice.getAddress(), result.getRssi());
                adapter.addOneDeviceInfo(info);
            }
            else
            {
                adapter.changeItemRssi(pos, result.getRssi());
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                Log.i("ScanResult - Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {new Runnable() {
                        @Override
                        public void run() {
                            Log.i("onLeScan", device.toString());
                            //connectToDevice(device);
                        }
                    };
                }
            };

}
