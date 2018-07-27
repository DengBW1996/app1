package com.example.a_199.myapplication;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by a_199 on 2018/7/26.
 */

public class FavoriteItem {
    private String name;

    private String itemInformation;

    private int imageId;

    private Boolean saved;

    private String createTime;

    private SimpleDateFormat currentTime;

    public FavoriteItem(String name, int imageId){
        this.name = name;
        this.imageId = imageId;
        //设置日期格式
        currentTime = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        this.createTime = currentTime.format(new Date());
    }

    public String getName(){
        return name;
    }

    public int getImageId(){
        return imageId;
    }

    public Boolean getSaved() {
        return saved;
    }

    public void setSaved(Boolean saved) {
        this.saved = saved;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getItemInformation() {
        return itemInformation;
    }

    public void setItemInformation(String itemInformation) {
        this.itemInformation = itemInformation;
    }
}
