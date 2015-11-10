package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.remote.models.PricesByDateModel;
import com.farmers.underground.ui.activities.PricesActivity;
import com.farmers.underground.ui.adapters.AllPricesAdapter;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.custom_views.CropsItemDivider;
import com.farmers.underground.ui.models.AllPricesDH;
import com.farmers.underground.ui.models.DateRange;
import com.farmers.underground.ui.utils.ResourceRetriever;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar
 * on 10/9/15.
 */
public class AllPricesFragment extends BaseFragment<PricesActivity>
        implements PricesActivity.DateRangeSetter, PricesActivity.CropAllPricesCallback {

    @Bind(R.id.rv_BaseListFragment)
    protected RecyclerView recyclerView;

   /* @Bind(R.id.tv_NoItemsBaseListFragment)
      protected TextView tv_NoItems;  */

    private enum TypeRequest{Refresh, Search, Add, Nothing}
    private AllPricesAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private TypeRequest mTypeRequest;

    public static AllPricesFragment getInstance(LastCropPricesModel cropModel) {
        Bundle args = new Bundle();
        Gson gson = new GsonBuilder().create();
        args.putString(ProjectConstants.KEY_DATA, gson.toJson(cropModel));

        AllPricesFragment fragment = new AllPricesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private boolean loading = true;
    private  int pastVisiblyItems, visibleItemCount, totalItemCount;

    private void setSettingRecycler(){
        mLayoutManager = new LinearLayoutManager(getHostActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new CropsItemDivider(ResourceRetriever.retrievePX(getHostActivity(), R.dimen.margin_default_normal)));
        adapter = new AllPricesAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
          /*  @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(mLayoutManager.findLastVisibleItemPosition() == adapter.getItemCount() - 2){
                    addMonth();
                }
            }*/

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisiblyItems = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if ((visibleItemCount + pastVisiblyItems) >= totalItemCount) {
                        loading = false;
                        Log.d("onScrolled", "Last Item Now! total = " + totalItemCount);
                        addMonth();
                    }
                }
            }
        });
    }

    public void setTypeRequestNothing(){
        mTypeRequest = TypeRequest.Nothing;
    }

    private void addMonth(){
        if(mTypeRequest != TypeRequest.Search) {
            mTypeRequest = TypeRequest.Add;
            getHostActivity().makeRequestGetPriceForPeriodAddMonth(this);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        setSettingRecycler();
    }

    @Override
    public void onResume() {
        super.onResume();

        //test todo
        if( mTypeRequest==null ||  mTypeRequest != TypeRequest.Nothing){
            mTypeRequest = mTypeRequest != TypeRequest.Search ? TypeRequest.Refresh : TypeRequest.Search;

            if(mTypeRequest == TypeRequest.Refresh)
                getHostActivity().makeRequestGetPriceForPeriod(true,this);
            else
                getHostActivity().makeRequestGetPriceForPeriod(false,this);
        }
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_base_list;
    }

    @Override
    public void setDateRange(DateRange dateRange, boolean isAllTime) {
        mTypeRequest = isAllTime ? TypeRequest.Refresh : TypeRequest.Search;
        //todo - test it  search request
        getHostActivity().makeRequestGetPriceForPeriod(this);
    }

    @Override
    public void onGetResult(List<PricesByDateModel> result) {
        if(mTypeRequest == TypeRequest.Nothing)  return;
        else if(mTypeRequest == TypeRequest.Add)  adapter.addDataList(generateDH(result));
        else adapter.setDataList(generateDH(result));

        setTypeRequestNothing();

        loading = true;
    }

    private List<AllPricesDH> generateDH(List<PricesByDateModel> result) {
        List<AllPricesDH> allPricesDHs = new ArrayList<>();
        for (PricesByDateModel item : result) {
            AllPricesDH allPricesDH = new AllPricesDH(item, getHostActivity());
            allPricesDHs.add(allPricesDH);
        }
        return allPricesDHs;
    }
}