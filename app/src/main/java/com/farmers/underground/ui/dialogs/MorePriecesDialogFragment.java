package com.farmers.underground.ui.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.farmers.underground.R;
import com.farmers.underground.remote.models.CropPrices;
import com.farmers.underground.ui.activities.TransparentActivity;
import com.farmers.underground.ui.adapters.MorePriecesAdapter;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.custom_views.CustomTextView;
import com.farmers.underground.ui.models.MorePriceItemModel;
import com.farmers.underground.ui.utils.StringFormaterUtil;

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

    private static final String KEY_PRICE_BASE = "price_base";
    private static final String KEY_CROP_NAME = "crop_name";

    public static MorePriecesDialogFragment newInstanse(CropPrices priceBase, String cropName){
        MorePriecesDialogFragment fragment = new MorePriecesDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_PRICE_BASE, priceBase);
        args.putString(KEY_CROP_NAME, cropName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dialog_more_prieces;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setData();
    }

    @OnClick(R.id.im_close_dialog_more_prieces)
    void closeDialog(){
        getHostActivity().finish();
    }

    private void setData(){
        CropPrices model = (CropPrices) getArguments().getSerializable(KEY_PRICE_BASE);
        String cropName = getArguments().getString(KEY_CROP_NAME);

        tvTitle.setText(model.source.name);
        tvFoot.setText(model.data.substring(0,10)); //todo parse date
        tcSubtitle.setVisibility(View.INVISIBLE);

        List<MorePriceItemModel> list = new ArrayList<>();
        for(CropPrices.More more : model.more){
            MorePriceItemModel itemModel =  new MorePriceItemModel();
            itemModel.setQuality(more.quality);
            itemModel.setPrice(StringFormaterUtil.parsePrice(more.price));
            itemModel.setCropName(cropName);
            list.add(itemModel);
        }
        listView.setAdapter(new MorePriecesAdapter(list, getHostActivity()));
    }
}
