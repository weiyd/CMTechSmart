package com.cmtech.android.device;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cmtech.android.fragmenttest.R;
import com.vise.baseble.CMBluetoothGatt;
import com.vise.baseble.callback.IConnectCallback;
import com.vise.baseble.exception.BleException;
import com.vise.baseble.model.resolver.GattAttributeResolver;
import com.vise.log.ViseLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gdmc on 2017/7/27.
 */

public class DeviceFragment extends Fragment {
    public static final String DEVICE_INFO = "DEVICE_INFO";

    public static final String LIST_NAME = "NAME";
    public static final String LIST_UUID = "UUID";

    private ScanDeviceInfo deviceInfo;
    private CMBluetoothGatt gatt;
    private Activity activity;

    private TextView tvState;
    private RecyclerView rvServiceView;

    private DeviceServiceAdapter serviceAdapter;

    private List<BluetoothGattService> services = new ArrayList<>();
    private List<Map<String, String>> gattServiceData = new ArrayList<>();

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
                services = gatt.getServices();
                for (final BluetoothGattService gattService : services) {
                    final Map<String, String> currentServiceData = new HashMap<>();
                    String uuid = gattService.getUuid().toString();
                    currentServiceData.put(LIST_NAME, GattAttributeResolver.getAttributeName(uuid, "unknown Service"));
                    currentServiceData.put(LIST_UUID, uuid);
                    gattServiceData.add(currentServiceData);
                }
                serviceAdapter.setGattServiceData(gattServiceData);
                serviceAdapter.notifyDataSetChanged();
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
        args.putParcelable(DEVICE_INFO, info);
        DeviceFragment fragment = new DeviceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceInfo = getArguments().getParcelable(DEVICE_INFO);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_device, container, false);

        tvState = (TextView) view.findViewById(R.id.tv_devicestate);
        rvServiceView = (RecyclerView) view.findViewById(R.id.rv_deviceservice);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvServiceView.setLayoutManager(manager);
        services.clear();
        gattServiceData.clear();
        serviceAdapter = new DeviceServiceAdapter(gattServiceData);
        rvServiceView.setAdapter(serviceAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        gatt = new CMBluetoothGatt(activity);
        gatt.setConnectTimeout(20000).connect(deviceInfo.getDevice(), false, connectCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gatt.clear();
    }
}
