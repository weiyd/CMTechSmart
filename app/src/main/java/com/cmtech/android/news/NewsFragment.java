package com.cmtech.android.news;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.cmtech.android.fragmenttest.R;
import com.cmtech.android.globalcommon.MyApplication;
import com.cmtech.android.mall.ProductInfo;
import com.cmtech.android.mall.ProductInfoAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gdmc on 2017/7/10.
 */

public class NewsFragment extends Fragment {
    private List<NewsInfo> newsList = new ArrayList<>();
    private NewsInfoAdapter adapter;
    public static final String IMAGEFILEPATH = MyApplication.imageFilePath + "/news/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_news, container, false);

        newsList = readNewsInfoFromDatabase();

        RecyclerView newsView = (RecyclerView) view.findViewById(R.id.rvnewsview);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        newsView.setLayoutManager(manager);
        adapter = new NewsInfoAdapter(newsList);
        newsView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();
        Button btnAdd = (Button) activity.findViewById(R.id.btnnewsadd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DataSupport.deleteAll(ProductInfo.class);
                NewsInfo news = new NewsInfo();
                news.setTime(System.currentTimeMillis());
                news.setHeadTitle("今天的新闻标题");
                news.setHeadImage("img1.png");
                news.setNews1Title("今天的第一条新闻标题");
                news.setNews1Image("img1.png");

                news.save();
            }
        });
    }

    private List<NewsInfo> readNewsInfoFromDatabase() {
        return DataSupport.findAll(NewsInfo.class);
    }
}
