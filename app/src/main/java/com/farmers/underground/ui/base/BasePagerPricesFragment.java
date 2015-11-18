package com.farmers.underground.ui.base;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.farmers.underground.ui.activities.PricesActivity;
import java.util.LinkedHashMap;

/**
 * Created by tZpace
 * on 11-Nov-15.
 */
public abstract class BasePagerPricesFragment<Model> extends BaseFragment<PricesActivity> implements PricesActivity.DateRangeSetter {

    /**
     * Used for preventing duplicate date items prices in list
     * items order is saved;
     * interesting thing, by the way
     */
    protected LinkedHashMap<String, Model> dataFetched = new LinkedHashMap<>();

    protected TypeRequest mTypeRequest;

    public void setCurrentTypeRequest(TypeRequest typeRequest) {
        mTypeRequest = typeRequest;

        if( getHostActivity()!=null)
            getHostActivity().setTemp(mTypeRequest);
    }

    public TypeRequest getCurrentTypeRequest() {
        return mTypeRequest;
    }

    public enum TypeRequest {

        /**
         * reload - same date range
         */
        Refresh,

        /**
         * request - changed date range
         */
        Search,

        /**
         * request - date range +1 month
         */
        Add,

        /**
         * no request, set list data if cached
         */
        Nothing

    }

    protected void setTypeRequestNothing() {
        mTypeRequest = TypeRequest.Nothing;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("BasePagerPrices", "onViewCreated " + getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("BasePagerPrices", "onResume " + getClass().getSimpleName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("BasePagerPrices", "onDestroyView " + getClass().getSimpleName());
    }

}

