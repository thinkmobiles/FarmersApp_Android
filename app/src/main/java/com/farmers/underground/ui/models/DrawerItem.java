package com.farmers.underground.ui.models;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * Created by omar
 * on 9/28/15.
 */
public class DrawerItem {
    public enum DRAWER_ITEM{
        HEADER,
        CONTENT_ITEM,
        DEVIDER
    }

    // item constructor
    public DrawerItem(int icon,int contentName) {
        this.item_type =  DRAWER_ITEM.CONTENT_ITEM;
        this.contentName = contentName;
        this.icon = icon;

    }

    //header Constructor
    public DrawerItem(String userIconPath, String userName) {
        this.item_type =  DRAWER_ITEM.HEADER;
        this.iconPath = userIconPath;
        this.userName = userName;
    }

    //devider construtor
    public DrawerItem(){
        this.item_type =  DRAWER_ITEM.DEVIDER;
    }


    public DRAWER_ITEM item_type;
    @DrawableRes  public int  icon;
    @StringRes public int contentName;
    public String  iconPath;
    public String userName;
}
