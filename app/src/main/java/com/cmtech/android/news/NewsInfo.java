package com.cmtech.android.news;

import org.litepal.crud.DataSupport;

/**
 * Created by gdmc on 2017/7/15.
 */

public class NewsInfo extends DataSupport{
    private int id;
    private long time;
    private String headTitle;
    private String headImage;
    private String news1Title;
    private String news1Image;
    private String news2Title;
    private String news2Image;
    private String news3Title;
    private String news3Image;

    public int getId() {return id;}
    public long getTime() {return time;}
    public String getHeadTitle() {return headTitle;}
    public String getHeadImage() {return headImage;}
    public String getNews1Title() {return news1Title;}
    public String getNews1Image() {return news1Image;}
    public String getNews2Title() {return news2Title;}
    public String getNews2Image() {return news2Image;}
    public String getNews3Title() {return news3Title;}
    public String getNews3Image() {return news3Image;}

    public void setId(int id) {this.id = id;}
    public void setTime(long time) {this.time = time;}
    public void setHeadTitle(String headTitle) {this.headTitle = headTitle;}
    public void setHeadImage(String headImage) {this.headImage = headImage;}
    public void setNews1Title(String news1Title) {this.news1Title = news1Title;}
    public void setNews1Image(String news1Image) {this.news1Image = news1Image;}
    public void setNews2Title(String news2Title) {this.news2Title = news2Title;}
    public void setNews2Image(String news2Image) {this.news2Image = news2Image;}
    public void setNews3Title(String news3Title) {this.news3Title = news3Title;}
    public void setNews3Image(String news3Image) {this.news3Image = news3Image;}

}
