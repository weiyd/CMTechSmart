package com.vise.baseble.callback.scan;

import android.annotation.TargetApi;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.vise.baseble.CMBluetoothScanner;
import com.vise.baseble.common.BleConstant;
import com.vise.baseble.common.State;
import com.vise.baseble.model.BluetoothLeDevice;

import java.util.List;

/**
 * @Description: Android L扫描
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 16/10/29 14:45.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public abstract class PeriodLScanCallback extends ScanCallback {
    protected Handler handler = new Handler(Looper.getMainLooper());
    protected CMBluetoothScanner scanner;
    protected List<ScanFilter> filters;
    protected ScanSettings settings;
    protected int scanTimeout = BleConstant.TIME_FOREVER; //表示一直扫描
    protected boolean isScan = true;
    protected boolean isScanning = false;

    public CMBluetoothScanner getScanner() {
        return scanner;
    }

    public PeriodLScanCallback setScanner(CMBluetoothScanner scanner) {
        this.scanner = scanner;
        return this;
    }

    public PeriodLScanCallback setScanTimeout(int scanTimeout) {
        this.scanTimeout = scanTimeout;
        return this;
    }

    public PeriodLScanCallback setScan(boolean scan) {
        isScan = scan;
        return this;
    }

    public PeriodLScanCallback setFilters(List<ScanFilter> filters) {
        this.filters = filters;
        return this;
    }

    public PeriodLScanCallback setSettings(ScanSettings settings) {
        this.settings = settings;
        return this;
    }

    public boolean isScanning() {
        return isScanning;
    }

    public int getScanTimeout() {
        return scanTimeout;
    }

    public void scan() {
        if (isScan) {
            if (isScanning) {
                return;
            }
            if (scanTimeout > 0) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isScanning = false;
                        if (scanner != null) {
                            scanner.setState(State.SCAN_TIMEOUT);
                            scanner.stopLeScan(PeriodLScanCallback.this);
                        }
                        scanTimeout();
                    }
                }, scanTimeout);
            }
            isScanning = true;
            if (scanner != null) {
                if (filters != null) {
                    scanner.startLeScan(filters, settings, PeriodLScanCallback.this);
                } else {
                    scanner.startLeScan(PeriodLScanCallback.this);
                }
            }
        } else {
            isScanning = false;
            if (scanner != null) {
                scanner.stopLeScan(PeriodLScanCallback.this);
            }
        }
    }

    public PeriodLScanCallback removeHandlerMsg() {
        handler.removeCallbacksAndMessages(null);
        return this;
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        if (result == null) {
            return;
        }
        onDeviceFound(new BluetoothLeDevice(result.getDevice(), result.getRssi(), result.getScanRecord().getBytes(), System
                .currentTimeMillis()));
    }

    public abstract void scanTimeout();

    public abstract void onDeviceFound(BluetoothLeDevice bluetoothLeDevice);
}
