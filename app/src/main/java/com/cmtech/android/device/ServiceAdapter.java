package com.cmtech.android.device;

import android.bluetooth.BluetoothGattService;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmtech.android.center.R;
import com.vise.baseble.model.resolver.GattAttributeResolver;

import java.util.List;

/**
 * Created by gdmc on 2017/7/28.
 */

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder>  {
    private List<BluetoothGattService> services;

    private final DeviceFragment fragment;

    private static final String UNKNOWN_SERVIE = "Unknown service";

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

    public ServiceAdapter(List<BluetoothGattService> services, DeviceFragment fragment) {
        this.services = services;
        this.fragment = fragment;
    }

    public void setGattServices(List<BluetoothGattService> services) {
        this.services = services;
        notifyDataSetChanged();
    }

    @Override
    public ServiceAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_serviceinfo, parent, false);
        final ServiceAdapter.ViewHolder holder = new ServiceAdapter.ViewHolder(view);

        holder.serviceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //v.setBackgroundColor(Color.BLUE);
                int position = holder.getAdapterPosition();
                fragment.updateCharaView(services.get(position).getCharacteristics());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ServiceAdapter.ViewHolder holder, int position) {
        String uuid = services.get(position).getUuid().toString();
        holder.tvServiceName.setText( GattAttributeResolver.getAttributeName(uuid, UNKNOWN_SERVIE) );
        holder.tvServiceUuid.setText(uuid.substring(4,8));
    }

    @Override
    public int getItemCount() {
        return services.size();
    }
}
