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
import com.farmers.underground.remote.models.CropModel;
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
 * Created by omar on 10/9/15.
 */
public class StatisticsFragment extends BaseFragment implements PricesActivity.DateRangeSetter {
    @Bind(R.id.rv_BaseListFragment)
    protected RecyclerView recyclerView;

    @Bind(R.id.tv_NoItemsBaseListFragment)
    protected TextView tv_NoItems;

    private AllPricesAdapter.AllPricesCallback allPricesCallback;
    private AllPricesAdapter adapter;
    private CropModel mCropModel;
    private DateRange mDateRange;

    public static BaseFragment getInstance(CropModel cropModel) {
        Bundle args = new Bundle();
        Gson gson = new GsonBuilder().create();
        args.putString(ProjectConstants.KEY_DATA, gson.toJson(cropModel));

        StatisticsFragment fragment = new StatisticsFragment();
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
        adapter = new AllPricesAdapter();
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapterData(generateTestCropsList());
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
    }

    private List<AllPricesDH> generateDH(List<CropModel> cropModelList) {
        List<AllPricesDH> allPricesDHs = new ArrayList<>();
        for (CropModel item : cropModelList) {
            AllPricesDH allPricesDH = new AllPricesDH(item, allPricesCallback);
            allPricesDHs.add(allPricesDH);
        }
        return allPricesDHs;
    }

    private void setAdapterData( List<CropModel> cropModels){
            adapter.setDataList(generateDH(cropModels));
    }

    //dev test methods
    private  List<CropModel>  generateTestCropsList(){
        List<CropModel> cropsList = new ArrayList<>();


        for (int i = 0; i < 100; i++) {
            CropModel basquiatCropModel = new CropModel();
            basquiatCropModel.setID(String.valueOf(i));
            basquiatCropModel.setImgLink("http://www.potomitan.info/ki_nov/images/basquiat_brownspots.jpg");
            cropsList.add(basquiatCropModel);
        }
         return cropsList;
    }
}