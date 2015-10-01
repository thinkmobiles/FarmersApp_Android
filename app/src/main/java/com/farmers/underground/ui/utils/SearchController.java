package com.farmers.underground.ui.utils;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.farmers.underground.ui.adapters.SearchHintAdapter;

import java.util.List;

/**
 * Created by omar on 9/30/15.
 */
abstract public class SearchController implements AdapterView.OnItemClickListener {
    private ListView lv_Container;
    private SearchHintAdapter adapter;
    private List<String> hintList;

    public SearchController(ListView hitnContainer){
        setContainerListView(hitnContainer);
    }

    public void setContainerListView(ListView hitnContainer) {
        this.lv_Container = hitnContainer;
        hitnContainer.setOnItemClickListener(this);
    }

    public void setHinsList(List<String> hintList) {
        this.hintList = hintList;
        adapter = new SearchHintAdapter();
        adapter.setItems(hintList);
        lv_Container.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void show(){
        lv_Container.setVisibility(View.VISIBLE);
    }

    public void hide(){
        lv_Container.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        searchByHint(hintList.get(i));
    }
    abstract public void searchByHint(String query);
}
