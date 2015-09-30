package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.models.CropsListFragmentModel;

/**
 * Created by omar on 9/30/15.
 */
public class CropsListFragment extends BaseFragment {
    public enum TYPE {
        ALL_CROPS,
        FAVORITIES
    }

    @Bind(R.id.rv_FragmentCrops)
    protected RecyclerView recyclerView;


    private CropsListFragmentModel thisModel;

    public static BaseFragment getInstance(TYPE type, String query){
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
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisModel = (CropsListFragmentModel)getArguments().getSerializable(ProjectConstants.KEY_DATA);
    }
}
