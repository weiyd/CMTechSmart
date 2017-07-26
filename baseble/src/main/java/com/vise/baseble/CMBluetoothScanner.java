package com.vise.baseble;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Build;

import com.vise.baseble.callback.scan.PeriodLScanCallback;
import com.vise.baseble.callback.scan.PeriodScanCallback;
import com.vise.baseble.common.State;

import java.util.List;

import static com.vise.baseble.common.BleConstant.DEFAULT_SCAN_TIME;

/**
 * Created by gdmc on 2017/7/27.
 */

public class CMBluetoothScanner {

    private Context context;//上下文
    private BluetoothManager bluetoothManager;//蓝牙管理
    private BluetoothAdapter bluetoothAdapter;//蓝牙适配器
    private State state = State.DISCONNECT;//设备状态描述
    private int scanTimeout = DEFAULT_SCAN_TIME;//扫描超时时间
    private boolean isFound = false;//是否发现设备

    private static CMBluetoothScanner scanner;

    public static CMBluetoothScanner getInstance() {
        if (scanner == null) {
            synchronized (CMBluetoothScanner.class) {
                if (scanner == null) {
                    scanner = new CMBluetoothScanner();
                }
            }
        }
        return scanner;
    }

    private CMBluetoothScanner() {
    }

    public void init(Context context) {
        if (this.context == null) {
            this.context = context.getApplicationContext();
            bluetoothManager = (BluetoothManager) this.context.getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = bluetoothManager.getAdapter();
        }
    }

    /*==================Android API 18 Scan========================*/

    /**
     * 开始扫描
     * @param leScanCallback 回调
     */
    public void startLeScan(BluetoothAdapter.LeScanCallback leScanCallback) {
        if (bluetoothAdapter != null) {
            bluetoothAdapter.startLeScan(leScanCallback);
            state = State.SCAN_PROCESS;
        }
    }

    /**
     * 停止扫描
     * @param leScanCallback 回调
     */
    public void stopLeScan(BluetoothAdapter.LeScanCallback leScanCallback) {
        if (bluetoothAdapter != null) {
            bluetoothAdapter.stopLeScan(leScanCallback);
        }
    }

    /**
     * 开始扫描
     * @param periodScanCallback 自定义回调
     */
    public void startScan(PeriodScanCallback periodScanCallback) {
        if (periodScanCallback == null) {
            throw new IllegalArgumentException("this PeriodScanCallback is Null!");
        }
        periodScanCallback.setScanner(this).setScan(true).setScanTimeout(scanTimeout).scan();
    }

    /**
     * 停止扫描
     * @param periodScanCallback 自定义回调
     */
    public void stopScan(PeriodScanCallback periodScanCallback) {
        if (periodScanCallback == null) {
            throw new IllegalArgumentException("this PeriodScanCallback is Null!");
        }
        periodScanCallback.setScanner(this).setScan(false).removeHandlerMsg().scan();
    }

    /*==================Android API 21 Scan========================*/

    /**
     * 开始扫描
     * @param leScanCallback 回调
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startLeScan(ScanCallback leScanCallback) {
        if (bluetoothAdapter != null && bluetoothAdapter.getBluetoothLeScanner() != null) {
            bluetoothAdapter.getBluetoothLeScanner().startScan(leScanCallback);
            state = State.SCAN_PROCESS;
        }
    }

    /**
     * 开始扫描
     * @param filters 过滤条件
     * @param settings 设置
     * @param leScanCallback 回调
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startLeScan(List<ScanFilter> filters, ScanSettings settings, ScanCallback leScanCallback) {
        if (bluetoothAdapter != null && bluetoothAdapter.getBluetoothLeScanner() != null) {
            bluetoothAdapter.getBluetoothLeScanner().startScan(filters, settings, leScanCallback);
            state = State.SCAN_PROCESS;
        }
    }

    /**
     * 停止扫描
     * @param leScanCallback 回调
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void stopLeScan(ScanCallback leScanCallback) {
        if (bluetoothAdapter != null && bluetoothAdapter.getBluetoothLeScanner() != null) {
            bluetoothAdapter.getBluetoothLeScanner().stopScan(leScanCallback);
        }
    }

    /**
     * 开始扫描
     * @param periodScanCallback 自定义回调
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startScan(PeriodLScanCallback periodScanCallback) {
        if (periodScanCallback == null) {
            throw new IllegalArgumentException("this PeriodScanCallback is Null!");
        }
        periodScanCallback.setScanner(this).setScan(true).setScanTimeout(scanTimeout).scan();
    }

    /**
     * 开始扫描
     * @param filters 过滤条件
     * @param settings 设置
     * @param periodScanCallback 自定义回调
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startScan(List<ScanFilter> filters, ScanSettings settings, PeriodLScanCallback periodScanCallback) {
        if (periodScanCallback == null) {
            throw new IllegalArgumentException("this PeriodScanCallback is Null!");
        }
        periodScanCallback.setScanner(this).setScan(true).setScanTimeout(scanTimeout).setFilters(filters).setSettings(settings)
                .scan();
    }

    /**
     * 停止扫描
     * @param periodScanCallback 自定义回调
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void stopScan(PeriodLScanCallback periodScanCallback) {
        if (periodScanCallback == null) {
            throw new IllegalArgumentException("this PeriodScanCallback is Null!");
        }
        periodScanCallback.setScanner(this).setScan(false).removeHandlerMsg().scan();
    }

    /**
     * 获取设备当前状态
     * @return 返回设备当前状态
     */
    public State getState() {
        return state;
    }

    /**
     * 设置设备状态
     * @param state 设备状态
     * @return 返回ViseBluetooth
     */
    public CMBluetoothScanner setState(State state) {
        this.state = state;
        return this;
    }

    /**
     * 获取扫描超时时间
     * @return 返回扫描超时时间
     */
    public int getScanTimeout() {
        return scanTimeout;
    }

    /**
     * 设置扫描超时时间
     * @param scanTimeout 扫描超时时间
     * @return 返回ViseBluetooth
     */
    public CMBluetoothScanner setScanTimeout(int scanTimeout) {
        this.scanTimeout = scanTimeout;
        return this;
    }

}
