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
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.ui.activities.FragmentViewsCreatedCallback;
import com.farmers.underground.ui.activities.MainActivity;
import com.farmers.underground.ui.adapters.CropsListAdapter;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.custom_views.CropsItemDivider;
import com.farmers.underground.ui.models.CropsListFragmentModel;
import com.farmers.underground.ui.models.CropsListItemDH;
import com.farmers.underground.ui.utils.ResourceRetriever;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar
 * on 9/30/15.
 */
public class CropsListFragment
        extends BaseFragment<MainActivity>
        implements CropsFragmentCallback {

    @Bind(R.id.rv_BaseListFragment)
    protected RecyclerView recyclerView;

    @Bind(R.id.tv_NoItemsBaseListFragment)
    protected TextView tv_NoItems;

    private CropsListFragmentModel mFragmentModel;
    private CropsListAdapter adapter;
  /*  private FragmentViewsCreatedCallback stateCallback;*/
    private CropsListAdapter.CropsAdapterCallback listCallback;

    @Override
    public void onDetach() {
        super.onDetach();
        /*stateCallback = null;*/
        listCallback = null;
    }


    public static CropsListFragment getInstance(CropsListFragmentModel.TYPE type) {
        CropsListFragmentModel fragmentModel = new CropsListFragmentModel(type);
        Bundle args = new Bundle();
        args.putSerializable(ProjectConstants.KEY_DATA, fragmentModel);
        CropsListFragment fragment = new CropsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_base_list;
    }

  /*  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, v);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new CropsItemDivider(ResourceRetriever.retrievePX(getContext(), R.dimen
                .crops_card_layout_margin)));
        return v;
    }
*/
    @Override
    public void onDestroyView() {
        getHostActivity().onFragmentViewDestroyed();
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new CropsItemDivider(ResourceRetriever.retrievePX(getHostActivity(), R.dimen
                .crops_card_layout_margin)));

        if (savedInstanceState != null)
            mFragmentModel = (CropsListFragmentModel) savedInstanceState.getSerializable(ProjectConstants.KEY_DATA);
        else mFragmentModel = (CropsListFragmentModel) getArguments().getSerializable(ProjectConstants.KEY_DATA);
        //showNoItems();
        adapter = new CropsListAdapter();
        recyclerView.setAdapter(adapter);
        getHostActivity().onFragmentViewCreated();
    }

    private void showNoItems(String querry) {
        tv_NoItems.setVisibility(View.VISIBLE);
        String itemsText;

        itemsText = getActivity().getString(R.string.no_crops_found) + querry;

        tv_NoItems.setText(itemsText);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ProjectConstants.KEY_DATA, mFragmentModel);
    }


    @Override
    public void setListCallback(CropsListAdapter.CropsAdapterCallback callback) {
        listCallback = callback;
    }


    @Override
    public void onReceiveCrops(List<LastCropPricesModel> cropsList, String query) {

        if (cropsList != null && mFragmentModel != null && query != null) {
            if (mFragmentModel.getType() == CropsListFragmentModel.TYPE
                    .FAVOURITES) {
                List<LastCropPricesModel> favouritesList = new ArrayList<>();
                for (LastCropPricesModel item : cropsList)
                    if (item.isInFavorites)
                        favouritesList.add(item);
                adapter.setDataList(generateCropsDataHolders(favouritesList));
            } else {
                adapter.setDataList(generateCropsDataHolders(cropsList));
            }

            adapter.notifyDataSetChanged();

            if (cropsList.size() == 0) showNoItems(query);
            else hideNoItems();

        } else {
            showNoItems("");
        }

    }

    private void hideNoItems() {
        tv_NoItems.setVisibility(View.GONE);
    }


    private List<CropsListItemDH> generateCropsDataHolders(List<LastCropPricesModel> cropsList) {
        List<CropsListItemDH> dhList = new ArrayList<>();
        if (cropsList != null)
            for (LastCropPricesModel model : cropsList) {
                switch (mFragmentModel.getType()) {
                    case ALL_CROPS:
                        CropsListItemDH dh = new CropsListItemDH(model, mFragmentModel.getType(), listCallback);
                        dhList.add(dh);
                        break;
                    case FAVOURITES:
                        if (model.isInFavorites)
                            dhList.add(new CropsListItemDH(model, mFragmentModel.getType(), listCallback));
                        break;
                }

            }
        return dhList;
    }

}
