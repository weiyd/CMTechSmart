package com.cmtech.android.center;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gdmc on 2017/7/11.
 */

public class UnderTitleAdapter extends RecyclerView.Adapter<UnderTitleAdapter.ViewHolder>  {
    private List<UnderTitleItem> listItem;
    private int curPos;
    private Activity activity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View productView;
        ImageView ivImage;
        TextView tvName;

        public ViewHolder(View view) {
            super(view);
            productView = view;
            ivImage = (ImageView) view.findViewById(R.id.ivimage_item);
            tvName = (TextView) view.findViewById(R.id.tvname_item);
        }
    }

    public UnderTitleAdapter(List<UnderTitleItem> listItem, int initPos, Activity activity) {
        this.listItem = listItem;
        curPos = initPos;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_undertitle, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.productView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if(curPos == position) return;
                notifyItemChanged(curPos);
                notifyItemChanged(position);
                curPos = position;
                ((MainActivity)activity).changeFragment(curPos);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UnderTitleItem item = listItem.get(position);
        holder.ivImage.setImageResource(item.getImgId());
        holder.tvName.setText(item.getName());
        if(position == curPos) {
            holder.tvName.setTextColor(Color.RED);
            holder.productView.setBackgroundColor(Color.GREEN);
        }
        else {
            holder.tvName.setTextColor(Color.BLACK);
            holder.productView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }
}
