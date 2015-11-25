package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.models.CropPrices;
import com.farmers.underground.remote.models.CropPricesByDateModel;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.ui.activities.PricesActivity;
import com.farmers.underground.ui.activities.TransparentActivity;
import com.farmers.underground.ui.adapters.AllPricesAdapter;
import com.farmers.underground.ui.base.BasePagerPricesFragment;
import com.farmers.underground.ui.custom_views.CropsItemDivider;
import com.farmers.underground.ui.dialogs.MorePricesDialogFragment;
import com.farmers.underground.ui.models.AllPricesDH;
import com.farmers.underground.ui.models.DateRange;
import com.farmers.underground.ui.utils.AnalyticsTrackerUtil;
import com.farmers.underground.ui.utils.ResourceRetriever;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar
 * on 10/9/15.
 */
public class AllPricesFragment extends BasePagerPricesFragment<CropPricesByDateModel>
        implements PricesActivity.CropAllPricesCallback, AllPricesAdapter.AllPricesCallback {

    @Bind(R.id.rv_BaseListFragment)
    protected RecyclerView recyclerView;

   /* no need it yet
      @Bind(R.id.tv_NoItemsBaseListFragment)
      protected TextView tv_NoItems;  */

    private AllPricesAdapter adapter = new AllPricesAdapter();

    private LinearLayoutManager mLayoutManager;

    public static AllPricesFragment getInstance(LastCropPricesModel cropModel) {

        final Bundle args = new Bundle();
        final Gson gson = new GsonBuilder().create();
        final AllPricesFragment fragment = new AllPricesFragment();

        args.putString(ProjectConstants.KEY_DATA, gson.toJson(cropModel));
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * RecyclerView Scroll: if true - load next month
     */
    private boolean loading = true;
    /**
     * RecyclerView Scroll
     */
    private int pastVisiblyItems, visibleItemCount, totalItemCount;

    private void setSettingRecycler() {

        mLayoutManager = new LinearLayoutManager(getHostActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new CropsItemDivider(ResourceRetriever.retrievePX(getHostActivity(), R.dimen.margin_default_normal)));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

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

    private void addMonth() {
        if (mTypeRequest != TypeRequest.Search) {
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

        if(getHostActivity().getTemp() != null){
            mTypeRequest = getHostActivity().getTemp();
        }

        if (mTypeRequest == null) {
            // load default (first month items)
            getHostActivity().makeRequestGetCropPriceForPeriod(true, this);
            mTypeRequest = TypeRequest.Add;

        } else if (mTypeRequest != TypeRequest.Nothing) {

            if (mTypeRequest == TypeRequest.Refresh)
                getHostActivity().makeRequestGetCropPriceForPeriod(true, this);
            else if (mTypeRequest == TypeRequest.Search)
                getHostActivity().makeRequestGetCropPriceForPeriod(false, this);

        } else if (!dataFetched.isEmpty()) {
            adapter.setDataList(generateDH(new ArrayList<>(dataFetched.values())));
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_base_list;
    }

    @Override
    public void setDateRange(DateRange dateRange, boolean isAllTime) {

        /*allow or prevent loading more on scroll */
        if (mTypeRequest == TypeRequest.Search) {
            loading = isAllTime;
        } else if (mTypeRequest == TypeRequest.Add) {
            loading = true;
        }

    }

    @Override
    public void onGetResult(List<CropPricesByDateModel> result) {

        if (mTypeRequest == TypeRequest.Nothing) {
            return;
        } else if (mTypeRequest == TypeRequest.Add) {
            adapter.addDataList(generateDH(updateCachedPrices(result, false)));
            loading = true;
        } else if (mTypeRequest == TypeRequest.Refresh) {
            adapter.setDataList(generateDH(updateCachedPrices(result, true)));
        } else if (mTypeRequest == TypeRequest.Search) {
            adapter.setDataList(generateDH(updateCachedPrices(result, true)));
        }

        setTypeRequestNothing();

    }

    @Override
    public void onError() {
        setTypeRequestNothing();
        loading = false;
    }

    private List<CropPricesByDateModel> updateCachedPrices(List<CropPricesByDateModel> result, boolean doClear) {
        if (doClear)
            dataFetched.clear();

        for (CropPricesByDateModel pricesByDateModel : result) {
            dataFetched.put(pricesByDateModel.prices.get(0).data, pricesByDateModel);
        }

        return new ArrayList<>(dataFetched.values());
    }

    private List<AllPricesDH> generateDH(List<CropPricesByDateModel> result) {
        List<AllPricesDH> allPricesDHs = new ArrayList<>();
        for (CropPricesByDateModel item : result) {
            AllPricesDH allPricesDH = new AllPricesDH(item, this);
            allPricesDHs.add(allPricesDH);
        }
        return allPricesDHs;
    }

    @Override
    public void onAddPricesClicked(String date) {
        setCurrentTypeRequest(TypeRequest.Refresh);
        getHostActivity().showWhyDialogs(date);
    }

    @Override
    public void onMorePricesClicked(CropPrices priceModel) {
        setTypeRequestNothing();
        TransparentActivity.startWithFragmentForResult(
                getHostActivity(),
                MorePricesDialogFragment.newInstance(priceModel,
                        getHostActivity().getCropModel().displayName), ProjectConstants.REQUEST_CODE_MORE_PRICE);
    }
}