package com.farmers.underground.ui.utils;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.farmers.underground.R;
import com.farmers.underground.remote.models.SearchHint;
import com.farmers.underground.ui.adapters.SearchHintAdapter;

import java.util.List;

/**
 * Created by omar
 * on 9/30/15.
 */
abstract public class SearchHintController implements AdapterView.OnItemClickListener {

    private ListView lv_Container;
    private SearchHintAdapter adapter;
    private List<SearchHint> hintList;
    private boolean isShowing;
    private SearchHint querry;


    public void initAdapter(SearchHintAdapter adapter) {
        this.adapter = adapter;
    }

    public SearchHint getQuerry() {
        return querry;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public SearchHintController(ListView hintContainer) {
        setContainerListView(hintContainer);
    }

    public void setContainerListView(ListView hitnContainer) {
        this.lv_Container = hitnContainer;
        hitnContainer.setOnItemClickListener(this);
        lv_Container.setTranslationY(-ResourceRetriever.retrievePX(lv_Container.getContext(), R.dimen
                .search_hint_offset));

    }

    public void setHinsList(List<SearchHint> hintList) {
        this.hintList = hintList;
        initAdapter(new SearchHintAdapter());
        adapter.setItems(hintList);
        lv_Container.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void setQuerry(SearchHint querry) {
        this.querry = querry;
    }

    public void show() {
        lv_Container.setVisibility(View.VISIBLE);
        isShowing = true;
        lv_Container.animate().translationYBy(-ResourceRetriever.retrievePX(lv_Container.getContext(), R.dimen
                .search_hint_offset))
                .translationY(0)
                .setDuration(lv_Container.getContext().getResources().getInteger(android.R.integer
                        .config_longAnimTime));
    }

    public void hide() {
        isShowing = false;
        lv_Container.animate().translationYBy(0)
                .translationY(-ResourceRetriever.retrievePX(lv_Container.getContext(), R.dimen
                        .search_hint_offset))
                .setDuration(lv_Container.getContext().getResources().getInteger(android.R.integer
                        .config_longAnimTime));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        searchByHint(hintList.get(i));
    }

    abstract public void searchByHint(SearchHint query);
}
