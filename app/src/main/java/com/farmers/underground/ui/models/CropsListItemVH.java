package com.farmers.underground.ui.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.farmers.underground.R;
import com.farmers.underground.ui.utils.CropsImageLoader;

/**
 * Created by omar on 10/2/15.
 */
public class CropsListItemVH extends RecyclerView.ViewHolder {
    @Bind(R.id.iv_CropsItemImage)
    protected ImageView  iv_CropsImage;

    @Bind(R.id.cb_FavouriteCropsItem)
    protected CheckBox cb_Fav;

    @Bind(R.id.tv_DayCropsItem)
    protected TextView tv_Number;

    @Bind(R.id.tv_MonthCropsCalendar)
    protected TextView tv_MonthWord;

    @Bind(R.id.tv_YearCropsItem)
    protected TextView tv_YearText;

    @Bind(R.id.tv_CropsNameCropItem)
    protected TextView tv_CropsName;

    @Bind(R.id.ll_PricesContainer_CropItem)
    protected LinearLayout ll_PriceContainer;


    private CropsListItemDH dateHolder;



    public CropsListItemVH(final View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void bindData(CropsListItemDH dateHolder) {

        this.dateHolder = dateHolder;
        cb_Fav.setChecked(false);
        CropsImageLoader.loadImage(iv_CropsImage, dateHolder.getModel().getImgLink());

    }


    @OnClick(R.id.cb_FavouriteCropsItem)
    protected void onFaveChecked(){
        dateHolder.getCallback().onFavChecked(dateHolder.getModel().getID(), cb_Fav.isChecked());
    }

    @OnClick(R.id.iv_CropsItemImage)
    protected void onImageCLicked(){
        dateHolder.getCallback().onItemClicked(dateHolder.getModel().getID());
    }

    @OnClick(R.id.tv_RefresPrice_CropsItem)
    protected void onRefreshClicked(){
        dateHolder.getCallback().onPriceRefreshClicked(dateHolder.getModel().getID());
    }

}
