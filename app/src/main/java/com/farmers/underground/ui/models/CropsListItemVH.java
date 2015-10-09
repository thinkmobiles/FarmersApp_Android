package com.farmers.underground.ui.models;

import android.support.v7.widget.CardView;
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
import com.squareup.picasso.Picasso;

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

    @Bind (R.id.crops_item_view)
    protected CardView container;

    public CardView getContainer() {
        return container;
    }

    private CropsListItemDH dateHolder;

    float radius;


    public CropsListItemVH(final View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        radius =   ((CardView) itemView).getRadius();

    }

    public void bindData(CropsListItemDH dateHolder) {
        this.dateHolder = dateHolder;

        cb_Fav.setChecked(false);

        Picasso.with(iv_CropsImage.getContext()).load(dateHolder.getModel().getImgLink())
                .into(iv_CropsImage);

        for ( int  i = 0 ; i  < ll_PriceContainer.getChildCount(); i ++)
        {
            TextView tv_refreshDate = (TextView) ll_PriceContainer.getChildAt(i).findViewById(R.id.tv_PriceDate_CropItem);
            TextView tv_refresh = (TextView) ll_PriceContainer.getChildAt(i).findViewById(R.id.tv_RefresPrice_CropsItem);

            if(i != 0 ){
                tv_refreshDate.setVisibility(View.INVISIBLE);
                tv_refresh.setOnClickListener(null);
                tv_refresh.setVisibility(View.INVISIBLE);
            }

        }

    }


    @OnClick(R.id.cb_FavouriteCropsItem)
    protected void onFaveChecked(){
        dateHolder.getCallback().onFavChecked(dateHolder.getModel(), cb_Fav.isChecked());
    }

    @OnClick(R.id.crops_item_view)
    protected void onImageCLicked(){
        dateHolder.getCallback().onItemClicked(dateHolder.getModel());
    }

    @OnClick(R.id.tv_RefresPrice_CropsItem)
    protected void onRefreshClicked(){
        dateHolder.getCallback().onPriceRefreshClicked(dateHolder.getModel());
    }

}
