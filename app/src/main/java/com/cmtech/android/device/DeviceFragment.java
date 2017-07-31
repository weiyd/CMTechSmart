package com.cmtech.android.device;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.cmtech.android.fragmenttest.R;
import com.vise.baseble.CMBluetoothGatt;
import com.vise.baseble.callback.IConnectCallback;
import com.vise.baseble.callback.data.ICharacteristicCallback;
import com.vise.baseble.exception.BleException;
import com.vise.baseble.utils.HexUtil;
import com.vise.log.ViseLog;

import java.util.ArrayList;
import java.util.List;

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
    private RecyclerView rvCharaView;

    private ServiceAdapter serviceAdapter;
    private CharacteristicAdapter charaAdapter;

    private EditText etInputData;
    private TextView tvDataInfo;

    private List<BluetoothGattService> services = new ArrayList<>();
    private List<BluetoothGattCharacteristic> characteristics = new ArrayList<>();

    private static final int MAX_CONNECT_TIME = 2;  //最大连接次数
    private int connectTime = 0;

    private final ICharacteristicCallback readCharaCallback = new ICharacteristicCallback() {
        @Override
        public void onSuccess(BluetoothGattCharacteristic characteristic) {
            if(characteristic == null)
                return;
            String uuid = "读UUID:"+characteristic.getUuid().toString().substring(4,8);
            String valueHex = HexUtil.encodeHexStr(characteristic.getValue());
            String valueStr = new String(characteristic.getValue());
            updateDataView(uuid + '\n'
                    + "As Array:" + valueHex + '\n'
                    + "As String:" + valueStr);
        }

        @Override
        public void onFailure(BleException exception) {
            if(exception == null)
                return;

            String error = "读错误";
            updateDataView(error);

        }
    };

    private final ICharacteristicCallback writeCharaCallback = new ICharacteristicCallback() {
        @Override
        public void onSuccess(BluetoothGattCharacteristic characteristic) {
            if(characteristic == null)
                return;
            String uuid = "写UUID:"+characteristic.getUuid().toString().substring(4,8);
            String valueHex = HexUtil.encodeHexStr(characteristic.getValue());
            String valueStr = new String(characteristic.getValue());
            updateDataView(uuid + '\n'
                    + "As Array:" + valueHex + '\n'
                    + "As String:" + valueStr);
        }

        @Override
        public void onFailure(BleException exception) {
            if(exception == null)
                return;

            String error = "写错误";
            updateDataView(error);

        }
    };

    private final ICharacteristicCallback notifyCharaCallback = new ICharacteristicCallback() {
        @Override
        public void onSuccess(BluetoothGattCharacteristic characteristic) {
            if(characteristic == null)
                return;
            String uuid = "Noti/Indi UUID:"+characteristic.getUuid().toString().substring(4,8);
            String valueHex = HexUtil.encodeHexStr(characteristic.getValue());
            String valueStr = new String(characteristic.getValue());
            updateDataView(uuid + '\n' + valueHex + '\n' + valueStr);
        }

        @Override
        public void onFailure(BleException exception) {
            if(exception == null)
                return;

            String error = "Noti/Indi错误";
            updateDataView(error);
        }
    };

    /**
     * 连接回调
     */
    private IConnectCallback connectCallback = new IConnectCallback() {
        @Override
        public void onConnectSuccess(BluetoothGatt gatt, int status) {
            ViseLog.i("Connect Success!");
            tvState.setText("已连接");
            if (gatt != null) {
                services = gatt.getServices();
                updateServiceView(services);
            }
        }

        @Override
        public void onConnectFailure(BleException exception) {
            ViseLog.i("Connect Failure!");
            if(connectTime <= MAX_CONNECT_TIME) {
                tvState.setText("连接错误，正在重连...");
                connectTime++;
                clearUI();
                connect();
            }
            else {
                onDisconnect();
            }
        }

        @Override
        public void onDisconnect() {
            ViseLog.i("Disconnect!");
            tvState.setText("已断开");
            clearUI();
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
        LinearLayoutManager serivceManager = new LinearLayoutManager(activity);
        serivceManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvServiceView.setLayoutManager(serivceManager);
        services.clear();
        serviceAdapter = new ServiceAdapter(services, this);
        rvServiceView.setAdapter(serviceAdapter);

        rvCharaView = (RecyclerView) view.findViewById(R.id.rv_servicechar);
        LinearLayoutManager charaManager = new LinearLayoutManager(activity);
        charaManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCharaView.setLayoutManager(charaManager);
        characteristics.clear();
        charaAdapter = new CharacteristicAdapter(characteristics, this);
        rvCharaView.setAdapter(charaAdapter);

        etInputData = (EditText) view.findViewById(R.id.et_inputdata);
        etInputData.setText("");

        tvDataInfo = (TextView) view.findViewById(R.id.tv_datainfo);
        tvDataInfo.setText("");

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        activity = getActivity();
        gatt = new CMBluetoothGatt(activity);
        if(gatt != null)
        {
            connect();
            connectTime = 1;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gatt.clear();
    }

    public void updateServiceView(List<BluetoothGattService> services) {
        serviceAdapter.setGattServices(services);
        //rvServiceView.getLayoutManager().smoothScrollToPosition(rvServiceView, null, 0);
    }

    public void updateCharaView(List<BluetoothGattCharacteristic> characteristics) {
        charaAdapter.setGattCharacteristic(characteristics);
    }

    public void updateDataView(String dataInfo) {
        tvDataInfo.setText(dataInfo);
    }

    public boolean readCharacteristic(BluetoothGattCharacteristic characteristic) {
        updateDataView("");
        return gatt.readCharacteristic(characteristic, readCharaCallback);
    }

    public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        String inputData = etInputData.getText().toString().trim();
        if("".equals(inputData) || !HexUtil.isHexData(inputData)) {
            return false;
        }
        return writeCharacteristic(characteristic, HexUtil.decodeHex(inputData.toCharArray()));
    }

    public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic, byte[] data) {
        return gatt.writeCharacteristic(characteristic, data, writeCharaCallback);
    }

    public boolean enableCharacteristicNotify(BluetoothGattCharacteristic characteristic, boolean enable) {
        return gatt.enableCharacteristicNotification(characteristic, notifyCharaCallback, enable, false);
    }

    public boolean enableCharacteristicIndicate(BluetoothGattCharacteristic characteristic, boolean enable) {
        return gatt.enableCharacteristicNotification(characteristic, notifyCharaCallback, enable, true);
    }

    private void clearUI() {
        services.clear();
        updateServiceView(services);
        characteristics.clear();
        updateCharaView(characteristics);
        updateDataView("");
    }

    private void connect() {
        gatt.setConnectTimeout(60000).setOperateTimeout(20000).connect(deviceInfo.getDevice(), false, connectCallback);
    }
}
