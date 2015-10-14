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
import com.farmers.underground.BuildConfig;
import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.remote.models.base.PriceBase;
import com.farmers.underground.ui.utils.DateHelper;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by omar on 10/2/15.
 */
public class CropsListItemVH extends RecyclerView.ViewHolder {

    @Bind(R.id.iv_CropsItemImage)
    protected ImageView iv_CropsImage;

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

    @Bind(R.id.crops_item_view)
    protected CardView container;

    public CardView getContainer() {
        return container;
    }

    private CropsListItemDH dateHolder;
    private LastCropPricesModel model;
    private DateHelper dateHelper;
    private SimpleDateFormat format;
    float radius;


    public CropsListItemVH(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        radius = ((CardView) itemView).getRadius();

    }

    public void bindData(CropsListItemDH dateHolder) {
        this.dateHolder = dateHolder;
        this.model = this.dateHolder.getModel();
        dateHelper = new DateHelper(container.getContext());

        if (!BuildConfig.PRODUCTION) tv_CropsName.setText(model.englishName);
        else tv_CropsName.setText(model.displayName);

        String fulldate = model.prices.get(0).data;
        format = new SimpleDateFormat(ProjectConstants.SERVER_DATE_FORMAT);
        try {
            long time = format.parse(fulldate).getTime();
            String[] date = dateHelper.getDate(time);
            tv_Number.setText(date[0]);
            tv_MonthWord.setText(date[1]);
            tv_YearText.setText(date[2]);

        } catch (Exception e) {
            e.printStackTrace();
        }

        cb_Fav.setChecked(model.isInFavorites);
        Picasso.with(iv_CropsImage.getContext()).load(dateHolder.getModel().image).into(iv_CropsImage);


        for (int i = 0; i < ll_PriceContainer.getChildCount(); i++) {
            TextView tv_refreshDate = (TextView) ll_PriceContainer.getChildAt(i).findViewById(R.id.tv_PriceDate_CropItem);
            TextView tv_refresh = (TextView) ll_PriceContainer.getChildAt(i).findViewById(R.id.tv_RefresPrice_CropsItem);
            TextView tv_Price = (TextView) ll_PriceContainer.getChildAt(i).findViewById(R.id.tv_Price);
            TextView tv_Marketeer_CropItem = (TextView) ll_PriceContainer.getChildAt(i).findViewById(R.id.tv_Marketeer_CropItem);


            PriceBase priceModel = model.prices.get(i);
            double price  =  Double.parseDouble(priceModel.value);
             tv_Price.setText(price!= 0 ? String.format("%.2f",price) : "- -");
            tv_Marketeer_CropItem.setText(priceModel.source.name);
            try {
                long time = format.parse(priceModel.data).getTime();
                tv_refreshDate.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(time));
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (i != 0) {
                tv_refreshDate.setVisibility(View.INVISIBLE);
                tv_refresh.setOnClickListener(null);
                tv_refresh.setVisibility(View.INVISIBLE);
            }

        }

    }


    @OnClick(R.id.cb_FavouriteCropsItem)
    protected void onFaveChecked() {
        dateHolder.getCallback().onFavChecked(dateHolder.getModel(), cb_Fav.isChecked());
    }

    @OnClick(R.id.crops_item_view)
    protected void onImageCLicked() {
        dateHolder.getCallback().onItemClicked(dateHolder.getModel());
    }

    @OnClick(R.id.tv_RefresPrice_CropsItem)
    protected void onRefreshClicked() {
        dateHolder.getCallback().onPriceRefreshClicked(dateHolder.getModel());
    }

}
