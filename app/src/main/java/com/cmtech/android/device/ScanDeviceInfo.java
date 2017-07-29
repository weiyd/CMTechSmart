package com.cmtech.android.device;

import android.os.Parcel;
import android.os.Parcelable;

import com.cmtech.android.fragmenttest.R;
import com.vise.baseble.model.BluetoothLeDevice;

/**
 * Created by gdmc on 2017/7/11.
 */

public class ScanDeviceInfo implements Parcelable {
    private BluetoothLeDevice device;
    private String uuid = "";
    private int imageId = R.drawable.unknown;
    private String nickName = "";
    private Boolean isConnect = false;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(device, flags);
        dest.writeString(uuid);
        dest.writeInt(imageId);
        dest.writeString(nickName);
        dest.writeInt(isConnect ? 1 : 0);
    }

    public static final Parcelable.Creator<ScanDeviceInfo> CREATOR = new Parcelable.Creator<ScanDeviceInfo>() {
        @Override
        public ScanDeviceInfo createFromParcel(Parcel source) {
            ScanDeviceInfo info = new ScanDeviceInfo();
            info.device = source.readParcelable(ScanDeviceInfo.class.getClassLoader());
            info.uuid = source.readString();
            info.imageId = source.readInt();
            info.nickName = source.readString();
            info.isConnect = (source.readInt()==1) ? true : false;
            return info;
        }

        @Override
        public ScanDeviceInfo[] newArray(int size) {
            return new ScanDeviceInfo[size];
        }
    };

    public BluetoothLeDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothLeDevice device) {
        this.device = device;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Boolean getIsConnect() {
        return isConnect;
    }

    public void setIsConnect(Boolean isConnect) {
        this.isConnect = isConnect;
    }

}
