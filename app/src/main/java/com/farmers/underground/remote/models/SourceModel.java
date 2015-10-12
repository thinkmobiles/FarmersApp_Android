package com.farmers.underground.remote.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by omar on 10/12/15.
 */
public class SourceModel {
    @SerializedName("type")
    private String type;
    @SerializedName("name")
    private String name;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
