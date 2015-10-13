package com.farmers.underground.ui.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.farmers.underground.R;
import com.farmers.underground.remote.models.PriceModel;
import com.farmers.underground.remote.models.SourceModel;
import com.farmers.underground.ui.activities.TransparentActivity;
import com.farmers.underground.ui.adapters.CropQualityPriecesAdapter;
import com.farmers.underground.ui.adapters.MorePriecesAdapter;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.custom_views.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tZpace
 * on 11-Oct-15.
 */
public class MorePriecesDialogFragment extends BaseFragment<TransparentActivity> {

    @Bind(R.id.im_close_dialog_more_prieces)
    ImageView imClose;

    @Bind(R.id.im_logo_dialog_more_prieces)
    ImageView imLogo;

    @Bind(R.id.tv_title_dialog_more_prieces)
    CustomTextView tvTitle;

    @Bind(R.id.tv_subtitle_dialog_more_prieces)
    CustomTextView tcSubtitle;

    @Bind(R.id.lv_items_dialog_more_prieces)
    ListView listView;

    @Bind(R.id.tv_foot_dialog_more_prieces)
    TextView tvFoot;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dialog_more_prieces;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        listView.setAdapter(initListAdapterTest());
    }

    @OnClick(R.id.im_close_dialog_more_prieces)
    void closeDialog(){
        getHostActivity().finish();
    }


    private MorePriecesAdapter initListAdapterTest(){

        PriceModel priceModel = new PriceModel();
        priceModel.setPrice(4.50f);
        SourceModel sourceModel = new SourceModel();
        sourceModel.setName("CropName");
        sourceModel.setType("QualityName");
        priceModel.setSource(sourceModel);
        List<PriceModel> mListItems =new ArrayList<>(0);
        mListItems.add(priceModel);
        mListItems.add(priceModel);
        mListItems.add(priceModel);
        mListItems.add(priceModel);
        mListItems.add(priceModel);

        return new MorePriecesAdapter(mListItems, getHostActivity());
    }
}
