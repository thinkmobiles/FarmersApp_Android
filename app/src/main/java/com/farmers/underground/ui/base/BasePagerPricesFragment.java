package com.farmers.underground.ui.base;
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
     * */
    protected LinkedHashMap<String,Model> dataFetched = new LinkedHashMap<>();

    protected TypeRequest mTypeRequest;

    public void setCurrentTypeRequest(TypeRequest typeRequest) {
        mTypeRequest = typeRequest;
    }

    public TypeRequest getCurrentTypeRequest() {
        return mTypeRequest;
    }

    public enum TypeRequest {

        /** reload - same date range */
        Refresh,

        /** request - changed date range */
        Search,

        /** request - date range +1 month */
        Add,

        /** no request, set list data if cached */
        Nothing

    }

    public void setTypeRequestNothing(){
        mTypeRequest = TypeRequest.Nothing;
    }
}
