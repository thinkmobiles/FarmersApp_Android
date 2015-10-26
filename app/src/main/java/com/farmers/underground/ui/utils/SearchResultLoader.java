package com.farmers.underground.ui.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.farmers.underground.BuildConfig;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.remote.models.SearchHint;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by omar on 10/22/15.
 */
public class SearchResultLoader extends AsyncTaskLoader<List<LastCropPricesModel>> {


    private String newQuerry;
    private List<LastCropPricesModel> mCropList;
    private List<LastCropPricesModel> output;

    public SearchResultLoader(Context context, String newQuerry, List<LastCropPricesModel> mCropList) {
        super(context);
        this.newQuerry = newQuerry;
        this.mCropList = mCropList;
    }


    @Override
    public List<LastCropPricesModel> loadInBackground() {
        List<LastCropPricesModel> searchResult = new LinkedList<>();

        for (LastCropPricesModel item : mCropList)
            if (BuildConfig.DEBUG)
                if (item.englishName.toLowerCase().contains(newQuerry.toLowerCase()))
                    searchResult.add(item);
                else if (item.displayName.toLowerCase().contains(newQuerry.toLowerCase()))
                    searchResult.add(item);

        return searchResult;
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
    public void deliverResult(List<LastCropPricesModel> output) {
        this.output = output;
        if (isStarted()) {
            super.deliverResult(output);
        }
    }

}
