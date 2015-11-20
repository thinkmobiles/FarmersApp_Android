package com.farmers.underground.ui.utils;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.remote.models.SearchHint;
import com.farmers.underground.ui.base.BaseActivity;

import java.util.List;

/**
 * Created by omar
 * on 10/22/15.
 */
public class SearchResultProvider {

    private LoaderManager loaderManager;
    private String query;
    private List<LastCropPricesModel> cropList;

    private  LoaderManager.LoaderCallbacks<List<SearchHint>> hintLoaderCallback;
    private  LoaderManager.LoaderCallbacks<List<LastCropPricesModel>> resultLoaderCallback;

    private SearchResultProvider(final Context context, final LoaderManager loaderManager,  final SearchCallback
            callback) {

        this.loaderManager = loaderManager;

        hintLoaderCallback = new LoaderManager.LoaderCallbacks<List<SearchHint>>() {
            @Override
            public Loader<List<SearchHint>> onCreateLoader(int i, Bundle bundle) {
                return
                        new SearchHintLoader(context, query, cropList);
            }

            @Override
            public void onLoadFinished(Loader<List<SearchHint>> loader, List<SearchHint> hintModels) {
                callback.onSearchHintLoadFinished(hintModels);
            }

            @Override
            public void onLoaderReset(Loader<List<SearchHint>> loader) {

            }
        };

        resultLoaderCallback = new LoaderManager.LoaderCallbacks<List<LastCropPricesModel>>() {
            @Override
            public Loader<List<LastCropPricesModel>> onCreateLoader(int i, Bundle bundle) {
                return
                        new SearchResultLoader(context, query, cropList);
            }

            @Override
            public void onLoadFinished(Loader<List<LastCropPricesModel>> loader, List<LastCropPricesModel> lastCropPricesModels) {
                callback.onSearchResultLoadFinished(lastCropPricesModels);
            }

            @Override
            public void onLoaderReset(Loader<List<LastCropPricesModel>> loader) {

            }
        };
    }

    public static <A extends BaseActivity> SearchResultProvider getInstance(A appCompatActivity, SearchCallback callback) {

        return new SearchResultProvider(appCompatActivity, appCompatActivity.getLoaderManager(), callback);
    }

    public void loadSearchHints(String query, List<LastCropPricesModel> cropList) {
        this.query = query;
        this.cropList = cropList;
        if (loaderManager.getLoader(0) != null)
            loaderManager.destroyLoader(0);
        if (cropList != null && cropList.size() > 0)
            loaderManager.initLoader(0, null, hintLoaderCallback);
    }

    public void loadSearchResults(String query, List<LastCropPricesModel> cropList) {
        this.query = query;
        this.cropList = cropList;
        if (loaderManager.getLoader(1) != null)
            loaderManager.destroyLoader(1);
        if (cropList != null && cropList.size() > 0)
            loaderManager.initLoader(1, null, resultLoaderCallback);
    }

    public interface SearchCallback {
        void onSearchHintLoadFinished(List<SearchHint> searchHintList);
        void onSearchResultLoadFinished(List <LastCropPricesModel> crops);
    }
}
