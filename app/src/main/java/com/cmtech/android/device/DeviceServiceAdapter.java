package com.cmtech.android.device;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmtech.android.fragmenttest.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cmtech.android.device.DeviceFragment.LIST_NAME;
import static com.cmtech.android.device.DeviceFragment.LIST_UUID;

/**
 * Created by gdmc on 2017/7/28.
 */

public class DeviceServiceAdapter extends RecyclerView.Adapter<DeviceServiceAdapter.ViewHolder>  {
    private List<Map<String, String>> gattServiceData;

    //private static final String LIST_NAME = "NAME";
    //private static final String LIST_UUID = "UUID";

    static class ViewHolder extends RecyclerView.ViewHolder {
        View serviceView;
        TextView tvServiceName;
        TextView tvServiceUuid;

        public ViewHolder(View view) {
            super(view);
            serviceView = view;
            tvServiceName = (TextView) view.findViewById(R.id.tv_servicename);
            tvServiceUuid = (TextView) view.findViewById(R.id.tv_serviceuuid);
        }
    }

    public DeviceServiceAdapter(List<Map<String, String>> gattServiceData) {
        this.gattServiceData = gattServiceData;
    }

    public void setGattServiceData(List<Map<String, String>> gattServiceData) {
        this.gattServiceData = gattServiceData;
    }

    @Override
    public DeviceServiceAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_serviceinfo, parent, false);
        final DeviceServiceAdapter.ViewHolder holder = new DeviceServiceAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(DeviceServiceAdapter.ViewHolder holder, int position) {
        Map<String, String> item = gattServiceData.get(position);
        holder.tvServiceName.setText( item.get(LIST_NAME) );
        holder.tvServiceUuid.setText(item.get(LIST_UUID));
    }

    @Override
    public int getItemCount() {
        return gattServiceData.size();
    }
}
