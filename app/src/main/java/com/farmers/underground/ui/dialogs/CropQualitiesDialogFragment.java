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
import com.farmers.underground.remote.models.CropPrices;
import com.farmers.underground.remote.models.MarketeerPrices;
import com.farmers.underground.remote.models.PriceModel;
import com.farmers.underground.remote.models.SourceModel;
import com.farmers.underground.ui.activities.TransparentActivity;
import com.farmers.underground.ui.adapters.CropQualityPiecesAdapter;
import com.farmers.underground.ui.adapters.MorePriecesAdapter;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.custom_views.CustomTextView;
import com.farmers.underground.ui.models.MorePriceItemModel;
import com.farmers.underground.ui.models.QualityPriceItemModel;
import com.farmers.underground.ui.utils.DateHelper;
import com.farmers.underground.ui.utils.StringFormaterUtil;

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

    private static final String KEY_PRICE_BASE = "price_base";

    private static final String KEY_DATE = "date";

    private static final String KEY_CROP_NAME = "crop";

    public static CropQualitiesDialogFragment newInstance (MarketeerPrices priceBase, String displayDate, String cropName) {
        CropQualitiesDialogFragment fragment = new CropQualitiesDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_PRICE_BASE, priceBase);
        args.putString(KEY_DATE, displayDate);
        args.putString(KEY_CROP_NAME,cropName);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dialog_crop_qualities;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setData();
    }

    @OnClick(R.id.im_close_dialog_crop_qualities)
    void closeDialog(){
        getHostActivity().finish();
    }


    private void setData() {
        MarketeerPrices model = (MarketeerPrices) getArguments().getSerializable(KEY_PRICE_BASE);

        if(model==null){
            closeDialog();
            return;
        }

        String displayDate = getArguments().getString(KEY_DATE);
        String cropName = getArguments().getString(KEY_CROP_NAME);

        tvTitle.setText(model.name);
        tvFoot.setText(displayDate);
        tcSubtitle.setVisibility(View.VISIBLE);
        tcSubtitle.setText(model.location);

        List<QualityPriceItemModel> list = new ArrayList<>();
        for (MarketeerPrices.More more : model.more) {
            QualityPriceItemModel itemModel = new QualityPriceItemModel();
            itemModel.setQuality(more.quality);
            itemModel.setPrice(StringFormaterUtil.parsePrice(more.price));
            itemModel.setCropName(cropName);
            list.add(itemModel);
        }
        listView.setAdapter(new CropQualityPiecesAdapter(list, getHostActivity()));
    }

   /* private CropQualityPiecesAdapter initListAdapterTest(){

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

        return new CropQualityPiecesAdapter(mListItems, getHostActivity());
    }*/
}
