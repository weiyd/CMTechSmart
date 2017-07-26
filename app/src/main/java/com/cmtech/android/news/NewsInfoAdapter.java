package com.cmtech.android.news;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.cmtech.android.fragmenttest.R;
import com.cmtech.android.globalcommon.LogUtil;
import com.cmtech.android.globalcommon.MyApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by gdmc on 2017/7/10.
 */

public class NewsInfoAdapter extends RecyclerView.Adapter<NewsInfoAdapter.ViewHolder> {
    private List<NewsInfo> mNewsList;
    private String[] chinaWeek = {"一", "二", "三", "四", "五", "六", "天"};

    static class ViewHolder extends RecyclerView.ViewHolder {
        View newsView;
        TextView tvTime;
        ImageView ivHeadImage;
        TextView tvHeadTitle;
        ImageView ivNews1Image;
        TextView tvNews1Title;
        LinearLayout news1Layout;

        public ViewHolder(View view) {
            super(view);
            newsView = view;
            tvTime = (TextView) view.findViewById(R.id.tvnewstime);
            ivHeadImage = (ImageView) view.findViewById(R.id.ivnewsheadimage);
            tvHeadTitle = (TextView) view.findViewById(R.id.tvnewsheadtitle);
            ivNews1Image = (ImageView) view.findViewById(R.id.ivnews1image);
            tvNews1Title = (TextView) view.findViewById(R.id.tvnews1title);
            news1Layout = (LinearLayout) view.findViewById(R.id.llnews1);
        }
    }

    public NewsInfoAdapter(List<NewsInfo> newsList) {
        mNewsList = newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_newsinfo, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        /*
        holder.productView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ProductInfo product = mProductList.get(position);
                ProductActivity.actionStart(v.getContext(), product.getUrl());
            }
        });
        */
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsInfo news = mNewsList.get(position);

        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date(news.getTime()));
        int year=ca.get(Calendar.YEAR);
        int month=ca.get(Calendar.MONTH)+1;
        int day=ca.get(Calendar.DATE);
        int hour=ca.get(Calendar.HOUR);
        int minute=ca.get(Calendar.MINUTE);
        int second=ca.get(Calendar.SECOND);
        int week=ca.get(Calendar.DAY_OF_WEEK)-1;
        String str = ""+year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second+" 星期"+chinaWeek[week-1];
        holder.tvTime.setText(str);

        String imgHeadName = NewsFragment.IMAGEFILEPATH + news.getHeadImage();
        Bitmap pic = createBitmapFromFile(imgHeadName);
        if(pic != null) {
            holder.ivHeadImage.setImageBitmap(pic);
        }
        else {
            holder.ivHeadImage.setImageResource(R.drawable.news);
        }

        holder.tvHeadTitle.setText(news.getHeadTitle());

        if("".equals(news.getNews1Title())) {
            holder.news1Layout.setVisibility(View.GONE);
            holder.ivNews1Image.setVisibility(View.GONE);
            holder.tvNews1Title.setVisibility(View.GONE);
        }
        else {
            holder.tvNews1Title.setText(news.getNews1Title());

            String imgNews1Name = NewsFragment.IMAGEFILEPATH + news.getNews1Image();
            pic = createBitmapFromFile(imgNews1Name);
            if(pic != null) {
                holder.ivNews1Image.setImageBitmap(pic);
            }
            else {
                holder.ivNews1Image.setImageResource(R.drawable.news);
            }
        }



    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    private Bitmap createBitmapFromFile(String file) {
        Uri imageUrl = Uri.fromFile(new File(file));
        Bitmap pic = null;
        try {
            pic = BitmapFactory.decodeStream(MyApplication.getContext().getContentResolver().openInputStream(imageUrl));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return pic;
    }
}
