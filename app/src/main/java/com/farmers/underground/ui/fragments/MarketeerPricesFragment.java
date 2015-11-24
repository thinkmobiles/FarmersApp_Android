package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.remote.models.MarketeerPriceModel;
import com.farmers.underground.remote.models.MarketeerPrices;
import com.farmers.underground.remote.models.MarketeerPricesByDateModel;
import com.farmers.underground.ui.activities.AddPriceActivity;
import com.farmers.underground.ui.activities.PricesActivity;
import com.farmers.underground.ui.activities.TransparentActivity;
import com.farmers.underground.ui.adapters.MarketeerPricesAdapter;
import com.farmers.underground.ui.base.BasePagerPricesFragment;
import com.farmers.underground.ui.dialogs.CropQualitiesDialogFragment;
import com.farmers.underground.ui.dialogs.WhyCanISeeThisPriceDialogFragment;
import com.farmers.underground.ui.models.DateRange;
import com.farmers.underground.ui.models.PriceMarketeerPricesDH;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar
 * on 10/9/15.
 */
public class MarketeerPricesFragment extends BasePagerPricesFragment<MarketeerPriceModel>
        implements PricesActivity.MarketeerAllPricesCallback {

    @Bind(R.id.rv_BaseListFragment)
    protected RecyclerView recyclerView;

 /* no need it yet
    @Bind(R.id.tv_NoItemsBaseListFragmentM)
    protected TextView tv_NoItems; */

    private MarketeerPricesAdapter adapter = new MarketeerPricesAdapter();

    private MarketeerPricesAdapter.Callback adapterCallback;


    public static MarketeerPricesFragment getInstance(LastCropPricesModel cropModel) {
        Bundle args = new Bundle();
        Gson gson = new GsonBuilder().create();
        args.putString(ProjectConstants.KEY_DATA, gson.toJson(cropModel));

        MarketeerPricesFragment fragment = new MarketeerPricesFragment();
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
    private LinearLayoutManager mLayoutManager;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        generateAdapterCB();

        mLayoutManager = new LinearLayoutManager(getHostActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);


        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        recyclerView.addItemDecoration(headersDecor);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });

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

    @Override
    public void onResume() {
        super.onResume();

        if(getHostActivity().getTemp() != null){
            mTypeRequest = getHostActivity().getTemp();
        }


        if (mTypeRequest == null) {
            // load default (first month items)
            getHostActivity().makeRequestGetMarketeerPriceForPeriod(true, this);
            mTypeRequest = TypeRequest.Add;

        } else if (mTypeRequest != TypeRequest.Nothing) {

            if (mTypeRequest == TypeRequest.Refresh)
                getHostActivity().makeRequestGetMarketeerPriceForPeriod(true, this);
            else if (mTypeRequest == TypeRequest.Search)
                getHostActivity().makeRequestGetMarketeerPriceForPeriod(false, this);

        } else if (!dataFetched.isEmpty()) {
            adapter.setDataList(generateDH(new ArrayList<>(dataFetched.values())));
        }
    }

    private void addMonth() {
        if (mTypeRequest != TypeRequest.Search) {
            mTypeRequest = TypeRequest.Add;
            getHostActivity().makeRequestGetMarketeerPriceForPeriodAddMonth(this);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_marketeer_prices;
    }

    private List<PriceMarketeerPricesDH> generateDH(List<MarketeerPriceModel> cropModelList) {
        List<PriceMarketeerPricesDH> holders = new ArrayList<>();
        for (int i = 0; i < cropModelList.size(); i++) {
            holders.add(generatePriceDH(cropModelList.get(i)));
        }
        return holders;
    }

    private PriceMarketeerPricesDH generatePriceDH(MarketeerPriceModel model) {
        PriceMarketeerPricesDH dataHolder = new PriceMarketeerPricesDH();
        dataHolder.setModel(model);
        dataHolder.setCallback(adapterCallback);
        return dataHolder;
    }

   /* private boolean equalsDate(MarketeerPriceModel first, MarketeerPriceModel second) {
        return first.getDate().equalsIgnoreCase(second.getDate());
    }*/

    private void generateAdapterCB() {
        adapterCallback = new MarketeerPricesAdapter.Callback() {
            @Override
            public void onMorePricesClicked(MarketeerPriceModel marketeerPriceModel) {
                TransparentActivity.startWithFragmentForResult(getHostActivity(),
                        CropQualitiesDialogFragment.newInstance(marketeerPriceModel.getMarketeerPrices(),
                                marketeerPriceModel.getDisplayDate(),
                                getHostActivity().getCropModel().displayName), ProjectConstants.REQUEST_CODE_MORE_QUALITIES);
            }

            @Override
            public void onNoPricesClicked(MarketeerPriceModel model) {
                WhyCanISeeThisPriceDialogFragment fragment = new WhyCanISeeThisPriceDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Date", model.getDate());
                fragment.setArguments(bundle);
                TransparentActivity.startWithFragmentForResult(getHostActivity(), fragment, ProjectConstants.REQUEST_CODE_DIALOG_WHY);
            }
        };
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.ll_AddPrice_MP)
    protected void addPriceClicked() {
        AddPriceActivity.start(getHostActivity(), getHostActivity().getCropModel());
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
    public void onGetResult(List<MarketeerPricesByDateModel> result) {

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

    private List<MarketeerPriceModel> updateCachedPrices(List<MarketeerPricesByDateModel> result, boolean doClear) {
        if (doClear)
            dataFetched.clear();

        for (MarketeerPricesByDateModel marketeerPricesByDateModel : result) {

            for (MarketeerPrices price : marketeerPricesByDateModel.prices) {
                MarketeerPriceModel model = new MarketeerPriceModel();

                model.setMarketeerPrices(price);

                dataFetched.put(model.getDate() + model.getName(), model);
            }
        }
        return new ArrayList<>(dataFetched.values());
    }

}