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
import com.farmers.underground.remote.models.CropModel;
import com.farmers.underground.remote.models.MarketeerPriceModel;
import com.farmers.underground.ui.activities.PricesActivity;
import com.farmers.underground.ui.adapters.MarketeerPricesAdapter;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.custom_views.CropsItemDivider;
import com.farmers.underground.ui.models.BaseMarketeerPricesDH;
import com.farmers.underground.ui.models.DateRange;
import com.farmers.underground.ui.utils.ResourceRetriever;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar on 10/9/15.
 */
public class MarketeerPricesFragment extends BaseFragment implements PricesActivity.DateRangeSetter {
    @Bind(R.id.rv_BaseListFragment)
    protected RecyclerView recyclerView;

    @Bind(R.id.tv_NoItemsBaseListFragment)
    protected TextView tv_NoItems;

    private MarketeerPricesAdapter adapter;
    private CropModel mCropModel;
    private DateRange mDateRange;



    public static BaseFragment getInstance(CropModel cropModel) {
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
        mCropModel = gson.fromJson(getArguments().getString(ProjectConstants.KEY_DATA), CropModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, v);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new CropsItemDivider(ResourceRetriever.retrievePX(getContext(), R.dimen.margin_default_normal)));
        adapter = new MarketeerPricesAdapter();
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapterData(generateTestCropsList());
    }

    private List<MarketeerPriceModel> generateTestCropsList() {

        return new ArrayList<MarketeerPriceModel>();
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

    private List<BaseMarketeerPricesDH> generateDH(List<MarketeerPriceModel> cropModelList) {
        List<BaseMarketeerPricesDH> allPricesDHs = new ArrayList<>();

        return allPricesDHs;
    }

    private void setAdapterData( List<MarketeerPriceModel> cropModels){
            adapter.setDataList(generateDH(cropModels));
    }


    @OnClick(R.id.ll_AddPrice_MP)
    protected void addPriceClicked(){}

}