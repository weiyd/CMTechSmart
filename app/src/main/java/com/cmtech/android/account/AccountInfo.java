package com.cmtech.android.account;

import org.litepal.crud.DataSupport;

/**
 * Created by gdmc on 2017/8/1.
 */

public class AccountInfo extends DataSupport{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    private String name;
    private String password;
    private int imgId;


}
