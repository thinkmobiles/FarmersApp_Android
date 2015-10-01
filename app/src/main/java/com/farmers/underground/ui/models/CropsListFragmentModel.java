package com.farmers.underground.ui.models;

import java.io.Serializable;

/**
 * Created by omar on 9/30/15.
 */
public class CropsListFragmentModel implements Serializable {
    public enum TYPE {
        ALL_CROPS,
        FAVORITIES
    }

    private  TYPE type;
    private String querry;
    private int pageCount = 0;
    private int itemRange = 10;


    public CropsListFragmentModel( TYPE type, String querry) {
        this.type = type;
        this.querry = querry;
    }


    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getItemRange() {
        return itemRange;
    }

    public void setItemRange(int itemRange) {
        this.itemRange = itemRange;
    }

    public  TYPE getType() {
        return type;
    }

    public void setType( TYPE type) {
        this.type = type;
    }

    public String getQuerry() {
        return querry;
    }

    public void setQuerry(String querry) {

          this.querry = querry;
    }
}
