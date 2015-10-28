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
import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.RetrofitSingleton;
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

    @Bind(R.id.tv_NoItemsBaseListFragment)
    protected TextView tv_NoItems;

    private AllPricesAdapter.AllPricesCallback allPricesCallback;
    private AllPricesAdapter adapter;
    private LastCropPricesModel mCropModel;
    private DateRange mDateRange;

    public static AllPricesFragment getInstance(LastCropPricesModel cropModel) {
        Bundle args = new Bundle();
        Gson gson = new GsonBuilder().create();
        args.putString(ProjectConstants.KEY_DATA, gson.toJson(cropModel));

        AllPricesFragment fragment = new AllPricesFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new GsonBuilder().create();
        mCropModel = gson.fromJson(getArguments().getString(ProjectConstants.KEY_DATA), LastCropPricesModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, v);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new CropsItemDivider(ResourceRetriever.retrievePX(getContext(), R.dimen.margin_default_normal)));
        adapter = new AllPricesAdapter();
        recyclerView.setAdapter(adapter);
        getHostActivity().makeRequestGetPriceForPeriod(null, this);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setAdapterData(generateTestCropsList());
    }

    @Override
    public void onResume() {
        super.onResume();
        onGetResult(getHostActivity().getPricesAdapterData());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        allPricesCallback = ((AllPricesAdapter.AllPricesCallback) context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        allPricesCallback = null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_base_list;
    }

    @Override
    public void setDateRange(DateRange dateRange) {
        mDateRange = dateRange;
        //todo - test it  search request
        getHostActivity().makeRequestGetPriceForPeriod(dateRange,this);
    }

    @Override
    public void onGetResult(List<PricesByDateModel> result) {
        adapter.setDataList(generateDH(result));
        adapter.notifyDataSetChanged();
    }

    private List<AllPricesDH> generateDH(List<PricesByDateModel> result) {
        List<AllPricesDH> allPricesDHs = new ArrayList<>();
        for (PricesByDateModel item : result) {
            AllPricesDH allPricesDH = new AllPricesDH(item, allPricesCallback);
            allPricesDHs.add(allPricesDH);
        }
        return allPricesDHs;
    }
}