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

/**
 * Created by gdmc on 2017/7/11.
 */

public class DetailScanDeviceAdapter extends RecyclerView.Adapter<DetailScanDeviceAdapter.ViewHolder>  {
    private List<ScanDeviceInfo> listItem;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View deviceView;
        ImageView ivImage;
        TextView tvName;
        TextView tvAddress;
        TextView tvRssi;
        CheckBox cbAutoConnect;

        public ViewHolder(View view) {
            super(view);
            deviceView = view;
            ivImage = (ImageView) view.findViewById(R.id.ivimage_item);
            tvName = (TextView) view.findViewById(R.id.tvname_item);
            tvAddress = (TextView) view.findViewById(R.id.tvaddress_item);
            tvRssi = (TextView) view.findViewById(R.id.tvrssi_item);
            cbAutoConnect = (CheckBox) view.findViewById(R.id.cbauto_item);
        }
    }

    public DetailScanDeviceAdapter(List<ScanDeviceInfo> listItem) {
        this.listItem = listItem;
    }


    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_btdetaildevice, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.cbAutoConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                listItem.get(position).setIsConnect(((CheckBox)v).isChecked());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScanDeviceInfo item = listItem.get(position);
        holder.ivImage.setImageResource(item.getImageId());
        String nickName = item.getNickName();
        holder.tvName.setText( ("".equals(nickName)) ? item.getName() : nickName );
        holder.tvAddress.setText(item.getAddress());
        holder.tvRssi.setText(""+item.getRssi());
        holder.cbAutoConnect.setChecked(item.getIsConnect());
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public void clearDeviceList() {
        listItem.clear();
        notifyDataSetChanged();
    }


    public void changeDeviceList(List<ScanDeviceInfo> deviceList) {
        listItem = deviceList;
        notifyDataSetChanged();
    }

    public void addOneDeviceInfo(ScanDeviceInfo info) {
        listItem.add(info);
        notifyDataSetChanged();
    }

    public int searchDeviceUsingAddress(String address) {
        for(int i = 0; i < listItem.size(); i++)
        {
            if(listItem.get(i).getAddress().equals(address))
                return i;
        }
        return -1;
    }

    public void changeItemRssi(int pos, int rssi) {
        listItem.get(pos).setRssi(rssi);
        notifyItemChanged(pos);
    }

    public List<ScanDeviceInfo> getNeedConnectDevice() {
        List<ScanDeviceInfo> needConnect = new ArrayList<>();
        for(ScanDeviceInfo info : listItem) {
            if(info.getIsConnect())
                needConnect.add(info);
        }
        return needConnect;
    }
}
