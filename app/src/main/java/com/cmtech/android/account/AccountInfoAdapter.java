package com.cmtech.android.account;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmtech.android.center.R;
import com.cmtech.android.common.LogUtil;
import com.cmtech.android.common.MyApplication;

/**
 * Created by gdmc on 2017/7/11.
 */

public class AccountInfoAdapter extends RecyclerView.Adapter<AccountInfoAdapter.ViewHolder>  {
    private int[] listImgId;
    private String[] listName;

    private AccountFragment fragment;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View infoView;
        ImageView ivImage;
        TextView tvName;

        public ViewHolder(View view) {
            super(view);
            infoView = view;
            ivImage = (ImageView) view.findViewById(R.id.iv_accountinfoimage);
            tvName = (TextView) view.findViewById(R.id.tv_accountinfoname);
        }
    }

    public AccountInfoAdapter(AccountFragment fragment) {
        this.fragment = fragment;
        listImgId = new int[]{R.drawable.account, R.drawable.device, R.drawable.mall};
        listName = new String[]{"完善信息", "配置设备", "订单"};
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accountinfo, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountInfo account = ((MyApplication)fragment.getActivity().getApplicationContext()).getAccountInfo();
                if(account == null) {
                    Toast.makeText(fragment.getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                }
                int position = holder.getAdapterPosition();
                //if(curPos == position) return;
                //notifyItemChanged(curPos);
                //notifyItemChanged(position);
                //curPos = position;
                //((MainActivity)activity).changeFragment(curPos);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ivImage.setImageResource(listImgId[position]);
        holder.tvName.setText(listName[position]);
        LogUtil.i("Account", "bind " + position);
    }

    @Override
    public int getItemCount() {
        return listName.length;
    }
}
