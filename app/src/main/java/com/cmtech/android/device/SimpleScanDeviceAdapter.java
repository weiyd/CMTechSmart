package com.cmtech.android.device;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmtech.android.center.R;

import java.util.List;

/**
 * Created by gdmc on 2017/7/11.
 */

public class SimpleScanDeviceAdapter extends RecyclerView.Adapter<SimpleScanDeviceAdapter.ViewHolder>  {
    private List<ScanDeviceInfo> listItem;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View productView;
        TextView tvInfo;

        public ViewHolder(View view) {
            super(view);
            productView = view;
            tvInfo = (TextView) view.findViewById(R.id.btdeviceinfo);
        }
    }

    public SimpleScanDeviceAdapter(List<ScanDeviceInfo> listItem) {
        this.listItem = listItem;
    }

    public void changeDeviceList(List<ScanDeviceInfo> deviceList) {
        listItem = deviceList;
        notifyDataSetChanged();
    }

    public void addOneDeviceInfo(ScanDeviceInfo info) {
        listItem.add(info);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_btsimpledevice, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScanDeviceInfo item = listItem.get(position);
        holder.tvInfo.setText(item.toString());
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }
}
