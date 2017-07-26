package com.cmtech.android.mall;

import org.litepal.crud.DataSupport;

/**
 * Created by gdmc on 2017/7/10.
 */

public class ProductInfo extends DataSupport{
    public static final String SMARTHOME = "智能家居";
    public static final String SMARTHEALTH = "智慧健康";
    public static final String SMARTTEACHING = "智能教学";

    private int id;
    private String name;
    private int imgId;
    private String imageName;
    private double price;
    private double oldPrice;
    private String type;
    private String introduction;
    private String url;

    public int getId() {return id;}
    public String getName() { return name; }
    public int getImgId() { return imgId; }
    public String getImageName() {return imageName;}
    public double getPrice() {
        return price;
    }
    public double getOldPrice() {return oldPrice;}
    public String getType() {
        return type;
    }
    public String getIntroduction() {return introduction;}
    public String getUrl() {return url;}

    public void setId(int id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setImgId(int imgId) {this.imgId = imgId;}
    public void setImageName(String imageName) {this.imageName = imageName;}
    public void setPrice(double price) {this.price = price;}
    public void setOldPrice(double oldPrice) {this.oldPrice = oldPrice;}
    public void setType(String type) {this.type = type;}
    public void setIntroduction(String introduction) {this.introduction = introduction;}
    public void setUrl(String url) {this.url = url;}

}
