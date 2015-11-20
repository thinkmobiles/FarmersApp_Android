package com.farmers.underground.ui.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.remote.models.SearchHint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar
 * on 10/22/15.
 */
public class SearchHintLoader extends AsyncTaskLoader<List<SearchHint>> {

    private String newQuery;
    private List<LastCropPricesModel> mCropList;
    private List<SearchHint> output;

    public SearchHintLoader(Context context, String newQuery, List<LastCropPricesModel> mCropList) {
        super(context);
        this.newQuery = newQuery;
        this.mCropList = mCropList;
    }

    @Override
    public List<SearchHint> loadInBackground() {
        List<SearchHint> newHintList = new ArrayList<>();

        for (LastCropPricesModel item : mCropList) {
            if (item.displayName.toLowerCase().contains(newQuery.toLowerCase()))
                newHintList.add(new SearchHint(item.displayName, SearchHint.HintType.FROM_CROPS_LIST));
        }
        return newHintList;
    }

    @Override
    protected void onStartLoading() {
        if (output == null) {
            forceLoad();
        } else {
            deliverResult(output);
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void deliverResult(List<SearchHint> output) {
        this.output = output;
        if (isStarted()) {
            super.deliverResult(output);

        }
    }

}
