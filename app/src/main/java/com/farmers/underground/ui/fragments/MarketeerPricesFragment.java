package com.farmers.underground.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.remote.models.MarketeerPriceModel;
import com.farmers.underground.ui.activities.PricesActivity;
import com.farmers.underground.ui.activities.TransparentActivity;
import com.farmers.underground.ui.adapters.MarketeerPricesAdapter;
import com.farmers.underground.ui.base.BaseFragment;
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
public class MarketeerPricesFragment extends BaseFragment<PricesActivity>
        implements PricesActivity.DateRangeSetter {

    @Bind(R.id.rv_BaseListFragment)
    protected RecyclerView recyclerView;

    @Bind(R.id.tv_NoItemsBaseListFragment)
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, v);
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
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapterData(generateTestPriceList());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_marketeer_prices;
    }


    @Override
    public void setDateRange(DateRange dateRange) {
        mDateRange = dateRange;
    }


    private List<PriceMarketeerPricesDH> generateDH(List<MarketeerPriceModel> cropModelList) {
        List<PriceMarketeerPricesDH> holders = new ArrayList<>();
        for (int i = 0; i < cropModelList.size(); i++) {
           /* if (i == 0) holders.add(generateDateDH(cropModelList.get(i)));*/

            holders.add(generatePriceDH(cropModelList.get(i)));

         /*   if (i > 0 && !equalsDate(cropModelList.get(i), cropModelList.get(i - 1)))
                holders.add(generateDateDH(cropModelList.get(i)));*/
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
                TransparentActivity.startWithFragmentForResult(getHostActivity(), new WhyCanISeeThisPriceDialogFragment(), PricesActivity.REQUEST_CODE_DIALOG_WHY);
            }
        };
    }

    @OnClick(R.id.ll_AddPrice_MP)
    protected void addPriceClicked() {

    }

}