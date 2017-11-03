package com.cmtech.android.mall;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.cmtech.android.center.R;
import com.cmtech.android.common.MyApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by gdmc on 2017/7/10.
 */

public class ProductInfoAdapter extends RecyclerView.Adapter<ProductInfoAdapter.ViewHolder> {
    private List<ProductInfo> mProductList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View productView;
        ImageView ivImage;
        TextView tvName;
        TextView tvPrice;
        TextView tvOldPrice;
        TextView tvIntroduction;

        public ViewHolder(View view) {
            super(view);
            productView = view;
            ivImage = (ImageView) view.findViewById(R.id.ivproductimage);
            tvName = (TextView) view.findViewById(R.id.tvproductname);
            tvPrice = (TextView) view.findViewById(R.id.tvproductprice);
            tvOldPrice = (TextView) view.findViewById(R.id.tvproductoldprice);
            tvIntroduction = (TextView) view.findViewById(R.id.tvproductintroduction);
        }
    }

    public ProductInfoAdapter(List<ProductInfo> productList) {
        mProductList = productList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productinfo, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.productView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ProductInfo product = mProductList.get(position);
                ProductActivity.actionStart(v.getContext(), product.getUrl());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductInfo product = mProductList.get(position);

        String imgName = MallFragment.IMAGEFILEPATH + product.getImageName();
        Uri imageUrl = Uri.fromFile(new File(imgName));
        Bitmap pic = null;
        try {
            pic = BitmapFactory.decodeStream(MyApplication.getContext().getContentResolver().openInputStream(imageUrl));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(pic != null) {
            holder.ivImage.setImageBitmap(pic);
        }
        else {
            holder.ivImage.setImageResource(product.getImgId());
        }

        holder.tvName.setText(product.getName());
        holder.tvPrice.setText("现价："+product.getPrice()+"元");
        holder.tvOldPrice.setText("原价："+product.getOldPrice()+"元");
        holder.tvIntroduction.setText(product.getIntroduction());
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }
}
