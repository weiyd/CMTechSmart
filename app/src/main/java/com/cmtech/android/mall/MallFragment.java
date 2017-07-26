package com.cmtech.android.mall;

import android.app.Activity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cmtech.android.fragmenttest.R;
import com.cmtech.android.globalcommon.MyApplication;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gdmc on 2017/7/10.
 */

public class MallFragment extends Fragment {
    private List<ProductInfo> productList = new ArrayList<>();
    private ProductInfoAdapter adapter;
    public static final String IMAGEFILEPATH = MyApplication.imageFilePath + "/mall/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_mall, container, false);

        productList = readProductInfoFromDatabase();

        RecyclerView productView = (RecyclerView) view.findViewById(R.id.rvproductview);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        productView.setLayoutManager(manager);
        adapter = new ProductInfoAdapter(productList);
        productView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();
        Button btnAdd = (Button) activity.findViewById(R.id.btnmalladd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DataSupport.deleteAll(ProductInfo.class);
                ProductInfo keytag = new ProductInfo();//("智能钥匙扣", R.drawable.keytag, 199, 199, ProductInfo.SMARTHOME, "智能钥匙扣简介");
                keytag.setName("智能钥匙扣");
                keytag.setImgId(R.drawable.keytag);
                keytag.setImageName("img1.jpg");
                keytag.setPrice(199);
                keytag.setOldPrice(200);
                keytag.setType(ProductInfo.SMARTHOME);
                keytag.setIntroduction("智能钥匙扣简介");
                keytag.setUrl("https://item.taobao.com/item.htm?spm=a230r.1.14.46.ebb2eb22msSoW&id=520676216486&ns=1&abbucket=13#detail");
                keytag.save();
                ProductInfo hrring = new ProductInfo();//("心率手环", R.drawable.hrring, 299, 299, ProductInfo.SMARTHEALTH, "心率手环简介");
                hrring.setName("心率手环");
                hrring.setImgId(R.drawable.hrring);
                hrring.setImageName("img2.jpg");
                hrring.setPrice(299);
                hrring.setOldPrice(300);
                hrring.setType(ProductInfo.SMARTHEALTH);
                hrring.setIntroduction("心率手环简介");
                hrring.setUrl("https://detail.tmall.com/item.htm?spm=a230r.1.14.48.ebb2eb2nKBjoP&id=537192529069&ns=1&abbucket=13");
                hrring.save();
                ProductInfo thermo = new ProductInfo();//("智能体温计", R.drawable.thermometer, 99, 99, ProductInfo.SMARTHEALTH, "智能体温计简介");
                thermo.setName("智能体温计");
                thermo.setImgId(R.drawable.thermometer);
                thermo.setImageName("img3.jpg");
                thermo.setPrice(99);
                thermo.setOldPrice(100);
                thermo.setType(ProductInfo.SMARTHEALTH);
                thermo.setIntroduction("智能体温计简介");
                thermo.setUrl("https://item.taobao.com/item.htm?spm=a230r.1.14.28.ebb2eb2qSMD9k&id=528385229426&ns=1&abbucket=13#detail");
                thermo.save();
            }
        });
    }


    private List<ProductInfo> readProductInfoFromDatabase() {
        return DataSupport.findAll(ProductInfo.class);
        //return DataSupport.where("type = ?", ProductInfo.SMARTHEALTH).find(ProductInfo.class);
    }

}
