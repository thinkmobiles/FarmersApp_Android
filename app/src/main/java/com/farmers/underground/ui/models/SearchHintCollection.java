package com.farmers.underground.ui.models;

import com.farmers.underground.remote.models.SearchHint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar
 * on 9/30/15.
 */
public class SearchHintCollection {
    private List<SearchHint> hintsList;

    public SearchHintCollection(){
        hintsList = new ArrayList<>();
    }

    public List<SearchHint> getHintsList() {
        return hintsList;
    }

    public void add(SearchHint query) {
       int index =  hintsList.indexOf(query);
        if(index >= 0 )
            hintsList.remove(index);
        hintsList.add(0, query);
        if(hintsList.size() > 5)
            hintsList.remove(5);
    }
}
