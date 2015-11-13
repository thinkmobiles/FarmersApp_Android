package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.remote.models.MarketeerPriceModel;
import com.farmers.underground.ui.activities.AddPriceActivity;
import com.farmers.underground.ui.activities.PricesActivity;
import com.farmers.underground.ui.activities.TransparentActivity;
import com.farmers.underground.ui.adapters.MarketeerPricesAdapter;
import com.farmers.underground.ui.base.BasePagerPricesFragment;
import com.farmers.underground.ui.dialogs.CropQualitiesDialogFragment;
import com.farmers.underground.ui.dialogs.WhyCanISeeThisPriceDialogFragment;
import com.farmers.underground.ui.models.DateMarketeerPricesDH;
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
public class MarketeerPricesFragment extends BasePagerPricesFragment {

    @Bind(R.id.rv_BaseListFragment)
    protected RecyclerView recyclerView;

    @Bind(R.id.tv_NoItemsBaseListFragmentM)
    protected TextView tv_NoItems;

    private MarketeerPricesAdapter adapter;
    private LastCropPricesModel mCropModel;
    private DateRange mDateRange;
    private MarketeerPricesAdapter.Callback adapterCallback;


    public static MarketeerPricesFragment getInstance(LastCropPricesModel cropModel) {
        Bundle args = new Bundle();
        Gson gson = new GsonBuilder().create();
        args.putString(ProjectConstants.KEY_DATA, gson.toJson(cropModel));

        MarketeerPricesFragment fragment = new MarketeerPricesFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new GsonBuilder().create();
        mCropModel = gson.fromJson(getArguments().getString(ProjectConstants.KEY_DATA), LastCropPricesModel.class);
        generateAdapterCB();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        /**/
        adapter = new MarketeerPricesAdapter();
        recyclerView.setAdapter(adapter);

        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        recyclerView.addItemDecoration(headersDecor);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });
        setAdapterData(generateTestPriceList());
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

    private DateMarketeerPricesDH generateDateDH(MarketeerPriceModel model) {
        DateMarketeerPricesDH dataHolder =  new DateMarketeerPricesDH();
        dataHolder.setDate(model.getDate());
        return dataHolder;
    }

    private PriceMarketeerPricesDH generatePriceDH(MarketeerPriceModel model) {
        PriceMarketeerPricesDH dataHolder = new PriceMarketeerPricesDH();
        dataHolder.setModel(model);
        dataHolder.setCallback(adapterCallback);
        return dataHolder;
    }

    private boolean equalsDate(MarketeerPriceModel first, MarketeerPriceModel second) {
        return first.getDate().equalsIgnoreCase(second.getDate());
    }

    private void setAdapterData(List<MarketeerPriceModel> cropModels) {
        adapter.setDataList(generateDH(cropModels));
    }


    /*todo remove later*/
    private List<MarketeerPriceModel> generateTestPriceList() {
        List<MarketeerPriceModel> priceModelList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MarketeerPriceModel model = new MarketeerPriceModel();
            model.setDate("21.05.15");
            priceModelList.add(model);
        }
        for (int i = 0; i < 5; i++) {
            MarketeerPriceModel model = new MarketeerPriceModel();
            model.setDate("20.05.15");
            priceModelList.add(model);
        }
        for (int i = 0; i < 5; i++) {
            MarketeerPriceModel model = new MarketeerPriceModel();
            model.setDate("19.05.15");
            priceModelList.add(model);
        }
        return priceModelList;
    }


    private void generateAdapterCB(){
        adapterCallback = new MarketeerPricesAdapter.Callback() {
            @Override
            public void onMorePricesClicked(MarketeerPriceModel marketeerPriceModel) {
                TransparentActivity.startWithFragment(getHostActivity(), new CropQualitiesDialogFragment());
            }

            @Override
            public void onNoPricesClicked(MarketeerPriceModel model) {
                WhyCanISeeThisPriceDialogFragment fragment =  new WhyCanISeeThisPriceDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Date",model.getDate());
                fragment.setArguments(bundle);
                TransparentActivity.startWithFragmentForResult(getHostActivity(), fragment, PricesActivity.REQUEST_CODE_DIALOG_WHY);
            }
        };
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.ll_AddPrice_MP)
    protected void addPriceClicked() {
        AddPriceActivity.start(getHostActivity(), mCropModel);
    }

    @Override
    public void setDateRange(DateRange dateRange, boolean isAllTime) {
        mDateRange = dateRange;
    }
}