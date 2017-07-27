package com.cmtech.android.device;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cmtech.android.fragmenttest.R;
import com.vise.baseble.CMBluetoothGatt;
import com.vise.baseble.callback.IConnectCallback;
import com.vise.baseble.exception.BleException;
import com.vise.log.ViseLog;

/**
 * Created by gdmc on 2017/7/27.
 */

public class DeviceFragment extends Fragment {
    public static final String DEVICE_INFO = "DEVICE_INFO";

    private ScanDeviceInfo deviceInfo;
    private CMBluetoothGatt gatt;
    private Activity activity;

    private TextView tvState;

    /**
     * 连接回调
     */
    private IConnectCallback connectCallback = new IConnectCallback() {
        @Override
        public void onConnectSuccess(BluetoothGatt gatt, int status) {
            ViseLog.i("Connect Success!");
            tvState.setText("已连接");
            //invalidateOptionsMenu();
            if (gatt != null) {
                //simpleExpandableListAdapter = displayGattServices(gatt.getServices());
            }
        }

        @Override
        public void onConnectFailure(BleException exception) {
            ViseLog.i("Connect Failure!");
            tvState.setText("连接错误");
            //invalidateOptionsMenu();
            //clearUI();
        }

        @Override
        public void onDisconnect() {
            ViseLog.i("Disconnect!");
            tvState.setText("已断开");
            //invalidateOptionsMenu();
            //clearUI();
        }
    };

    public static DeviceFragment newInstance(ScanDeviceInfo info) {
        Bundle args = new Bundle();
        args.putSerializable(DEVICE_INFO, info);
        DeviceFragment fragment = new DeviceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceInfo = (ScanDeviceInfo) getArguments().getSerializable(DEVICE_INFO);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_device, container, false);

        tvState = (TextView) view.findViewById(R.id.tv_devicestate);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        gatt = new CMBluetoothGatt(activity);
        gatt.connectByMac(deviceInfo.getAddress(), false, connectCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gatt.clear();
    }
}
