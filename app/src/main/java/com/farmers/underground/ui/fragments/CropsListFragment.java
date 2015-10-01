package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.models.CropsListFragmentModel;

/**
 * Created by omar on 9/30/15.
 */
public class CropsListFragment extends BaseFragment implements SearchQueryFragmentCallback {

    @Bind(R.id.rv_FragmentCrops)
    protected RecyclerView recyclerView;

    @Bind(R.id.tv_NoItemsCropsFragment)
    protected TextView tv_NoItems;

    @Bind(R.id.crops_item_view)
    protected CardView cardView;

    private CropsListFragmentModel thisModel;



    public static BaseFragment getInstance(CropsListFragmentModel.TYPE type, String query){
        CropsListFragmentModel fragmentModel = new CropsListFragmentModel(type, query);
        Bundle args = new Bundle();
        args.putSerializable(ProjectConstants.KEY_DATA, fragmentModel);
        CropsListFragment fragment = new CropsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_crops_list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this,v);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        cardView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                cardView.getLayoutParams().height    = cardView.getMeasuredWidth();
                cardView.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState!= null)
            thisModel = (CropsListFragmentModel)savedInstanceState.getSerializable(ProjectConstants.KEY_DATA);
        else
            thisModel = (CropsListFragmentModel)getArguments().getSerializable(ProjectConstants.KEY_DATA);
        showNoItems();
    }

    private void showNoItems(){
        tv_NoItems.setVisibility(View.VISIBLE);
        if(!thisModel.getQuerry().isEmpty())
        tv_NoItems.setText(getActivity().getString(R.string.no_crops_found) + thisModel.getQuerry());
        else
            tv_NoItems.setText(getActivity().getString(R.string.no_crops_found) + getActivity().getString(R.string.all));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ProjectConstants.KEY_DATA, thisModel);
    }

    @Override
    public void onReceive(String querry) {
        thisModel.setQuerry(querry);
        showNoItems();
    }

}
