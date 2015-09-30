package com.farmers.underground.ui.models;

import com.farmers.underground.ui.fragments.CropsListFragment;

import java.io.Serializable;

/**
 * Created by omar on 9/30/15.
 */
public class CropsListFragmentModel implements Serializable {
    private CropsListFragment.TYPE type;
    private String querry;

    public CropsListFragmentModel(CropsListFragment.TYPE type, String querry) {
        this.type = type;
        this.querry = querry;
    }

    public CropsListFragment.TYPE getType() {
        return type;
    }

    public void setType(CropsListFragment.TYPE type) {
        this.type = type;
    }

    public String getQuerry() {
        if (querry == null)  querry = "";
        return querry;
    }

    public void setQuerry(String querry) {

          this.querry = querry;
    }
}
