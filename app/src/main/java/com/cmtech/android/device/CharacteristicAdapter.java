package com.cmtech.android.device;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cmtech.android.center.R;
import com.vise.baseble.model.resolver.GattAttributeResolver;

import java.util.List;

/**
 * Created by gdmc on 2017/7/28.
 */

public class CharacteristicAdapter extends RecyclerView.Adapter<CharacteristicAdapter.ViewHolder>  {
    private List<BluetoothGattCharacteristic> characteristics;

    private final DeviceFragment fragment;

    private static final String UNKNOWN_CHARACTERISTIC = "Unknown characteristic";

    static class ViewHolder extends RecyclerView.ViewHolder {
        View charaView;
        TextView tvCharaName;
        TextView tvCharaUuid;
        Button btnRead;
        Button btnWrite;
        Button btnNotify;
        Button btnIndicate;
        boolean hasNotify;
        boolean hasIndicate;

        public ViewHolder(View view) {
            super(view);
            charaView = view;
            tvCharaName = (TextView) view.findViewById(R.id.tv_charaname);
            tvCharaUuid = (TextView) view.findViewById(R.id.tv_charauuid);
            btnRead = (Button) view.findViewById(R.id.btn_readchara);
            btnWrite = (Button) view.findViewById(R.id.btn_writechara);
            btnNotify = (Button) view.findViewById(R.id.btn_notifychara);
            btnIndicate = (Button) view.findViewById(R.id.btn_indicatechara);
            hasNotify = false;
            hasIndicate = false;
        }
    }

    public CharacteristicAdapter(List<BluetoothGattCharacteristic> characteristics, DeviceFragment fragment) {
        this.characteristics = characteristics;
        this.fragment = fragment;
    }

    public void setGattCharacteristic(List<BluetoothGattCharacteristic> characteristic) {
        this.characteristics = characteristic;
        notifyDataSetChanged();

    }

    @Override
    public CharacteristicAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_charainfo, parent, false);
        final CharacteristicAdapter.ViewHolder holder = new CharacteristicAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final CharacteristicAdapter.ViewHolder holder, int position) {
        final BluetoothGattCharacteristic characteristic = characteristics.get(position);
        String uuid = characteristic.getUuid().toString();
        holder.tvCharaName.setText( GattAttributeResolver.getAttributeName(uuid, UNKNOWN_CHARACTERISTIC) );
        holder.tvCharaUuid.setText(uuid.substring(4,8));

        int charaProp = characteristic.getProperties();
        if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
            holder.btnRead.setEnabled(true);
            holder.btnRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.readCharacteristic(characteristic);
                }
            });
        }
        else {
            holder.btnRead.setEnabled(false);
        }
        if((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
            holder.btnWrite.setEnabled(true);
            holder.btnWrite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.writeCharacteristic(characteristic);
                }
            });
        }
        else {
            holder.btnWrite.setEnabled(false);
        }
        if((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
            holder.btnNotify.setEnabled(true);
            holder.btnNotify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(fragment.enableCharacteristicNotify(characteristic, !holder.hasNotify)) {
                        holder.hasNotify = !holder.hasNotify;
                    }
                }
            });
        }
        else {
            holder.btnNotify.setEnabled(false);
        }
        if((charaProp & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
            holder.btnIndicate.setEnabled(true);
            holder.btnIndicate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(fragment.enableCharacteristicIndicate(characteristic, !holder.hasIndicate)) {
                        holder.hasIndicate = !holder.hasIndicate;
                    }
                }
            });
        }
        else {
            holder.btnIndicate.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return characteristics.size();
    }
}
