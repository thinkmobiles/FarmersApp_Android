package com.farmers.underground.ui.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar on 9/30/15.
 */
public class SearchHint {
    private List<String> hintsList;

    public SearchHint(){
        hintsList = new ArrayList<>();
    }


    public List<String> getHintsList() {
        return hintsList;
    }

    public void setHintsList(List<String> hintsList) {
        this.hintsList = hintsList;
    }

    public void add(String query) {
       int index =  hintsList.indexOf(query);
        if(index >= 0 )
            hintsList.remove(index);
        hintsList.add(0, query);
    }
}
