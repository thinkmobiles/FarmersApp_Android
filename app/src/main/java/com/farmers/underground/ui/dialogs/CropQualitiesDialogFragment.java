package com.farmers.underground.ui.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.farmers.underground.R;
import com.farmers.underground.remote.models.PriceModel;
import com.farmers.underground.remote.models.SourceModel;
import com.farmers.underground.ui.activities.TransparentActivity;
import com.farmers.underground.ui.adapters.CropQualityPriecesAdapter;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.custom_views.CustomTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tZpace
 * on 11-Oct-15.
 */
public class CropQualitiesDialogFragment extends BaseFragment<TransparentActivity> {

    @Bind(R.id.im_close_dialog_crop_qualities)
    ImageView imClose;

    @Bind(R.id.im_logo_dialog_crop_qualities)
    ImageView imLogo;

    @Bind(R.id.tv_title_dialog_crop_qualities)
    CustomTextView tvTitle;

    @Bind(R.id.tv_subtitle_dialog_crop_qualities)
    CustomTextView tcSubtitle;

    @Bind(R.id.lv_items_dialog_crop_qualities)
    ListView listView;

    @Bind(R.id.tv_foot_dialog_crop_qualities)
    TextView tvFoot;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dialog_crop_qualities;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        listView.setAdapter(initListAdapterTest());
    }

    @OnClick(R.id.im_close_dialog_crop_qualities)
    void closeDialog(){
        getHostActivity().finish();
    }


    private CropQualityPriecesAdapter initListAdapterTest(){

        PriceModel priceModel = new PriceModel();
        priceModel.setPrice(4.50d);
        SourceModel sourceModel = new SourceModel();
        sourceModel.setName("MarketeerName");
        sourceModel.setType("QualityName (Type)");
        priceModel.setSource(sourceModel);
        List<PriceModel> mListItems =new ArrayList<>(0);
        mListItems.add(priceModel);
        mListItems.add(priceModel);
        mListItems.add(priceModel);
        mListItems.add(priceModel);
        mListItems.add(priceModel);

        return new CropQualityPriecesAdapter(mListItems, getHostActivity());
    }
}
