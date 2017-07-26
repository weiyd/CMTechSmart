package com.cmtech.android.device;

import com.cmtech.android.fragmenttest.R;

import java.io.Serializable;

/**
 * Created by gdmc on 2017/7/11.
 */

public class ScanDeviceInfo implements Serializable{
    private String name;
    private String uuid;
    private String address;
    private int rssi;
    private int imageId = R.drawable.unknown;
    private String nickName = "";
    private Boolean isConnect = false;

    public ScanDeviceInfo(String name, String uuid, String address, int rssi) {
        this.name = name;
        this.uuid = uuid;
        this.address = address;
        this.rssi = rssi;
    }

    public ScanDeviceInfo(String name, String uuid, String address, int rssi,
                          int imageId, String nickName, Boolean isConnect) {
        this.name = name;
        this.uuid = uuid;
        this.address = address;
        this.rssi = rssi;
        this.imageId = imageId;
        this.nickName = nickName;
        this.isConnect = isConnect;
    }

    public String getName() {return name;}
    public String getUuid() {return uuid;}
    public String getAddress() {return address;}
    public int getRssi() {return rssi;}
    public int getImageId() {return imageId;}
    public String getNickName() {return nickName;}
    public Boolean getIsConnect() {return isConnect;}

    public void setName(String name) {this.name = name;}
    public void setUuid(String uuid) {this.uuid = uuid;}
    public void setAddress(String address) {this.address = address;}
    public void setRssi(int rssi) {this.rssi = rssi;}
    public void setImageId(int imageId) {this.imageId = imageId;}
    public void setNickName(String nickName) {this.nickName = nickName;}
    public void setIsConnect(Boolean isConnect) {this.isConnect = isConnect;}

    @Override
    public String toString() {
        return name + " " + uuid + '\n' + address;
    }
}
