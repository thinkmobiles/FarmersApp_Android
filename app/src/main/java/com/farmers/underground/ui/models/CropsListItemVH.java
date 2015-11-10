package com.farmers.underground.ui.models;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.farmers.underground.FarmersApp;
import com.farmers.underground.R;
import com.farmers.underground.config.ApiConstants;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.remote.models.base.PriceBase;
import com.farmers.underground.ui.utils.DateHelper;
import com.farmers.underground.ui.utils.ImageCacheManager;
import com.farmers.underground.ui.utils.StringFormaterUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

//import android.graphics.Bitmap;
//import com.squareup.picasso.Picasso;
//import android.view.ViewTreeObserver;
//import com.farmers.underground.ui.utils.PicassoHelper;
//import com.farmers.underground.BuildConfig;


/**
 * Created by omar
 * on 10/2/15.
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


//    static int width = 0;


    private final ImageLoader imageLoader = ImageCacheManager.getImageLoader(FarmersApp.ImageLoaders.CACHE_MAIN);


    private CropsListItemDH dateHolder;

//    private LastCropPricesModel model;
//    private DateHelper dateHelper;
//    private SimpleDateFormat format;

    float radius;


    public CropsListItemVH(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        radius = ((CardView) itemView).getRadius();

    }

    public void bindData(CropsListItemDH dateHolder) {
        this.dateHolder = dateHolder;
        LastCropPricesModel model = this.dateHolder.getModel();
        DateHelper dateHelper = new DateHelper(container.getContext());

        tv_CropsName.setText(model.displayName);

        String fulldate = model.prices.get(0).data;

        SimpleDateFormat format = new SimpleDateFormat(ProjectConstants.SERVER_DATE_FORMAT, Locale.getDefault());
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

        final String url = !TextUtils.isEmpty(dateHolder.getModel().image) ? ApiConstants.BASE_URL + dateHolder.getModel().image : null;

//        if (url != null) {
//
            imageLoader.displayImage(url, iv_CropsImage);
//
//            if (width == 0)
//                iv_CropsImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                    @Override
//                    public boolean onPreDraw() {
//                        PicassoHelper.getPicasso(iv_CropsImage.getContext())
//                                    .load(url)
//                                    .config(Bitmap.Config.RGB_565)
//                                    .resize(iv_CropsImage.getMeasuredWidth(), iv_CropsImage.getMeasuredWidth())
//                                    .centerInside()
//                                    .placeholder(R.drawable.ic_drawer_crops)
//                                    .error(R.drawable.ic_drawer_crops)
//                                    .into(iv_CropsImage); //todo error
//                        width = iv_CropsImage.getMeasuredWidth();
//                         iv_CropsImage.getViewTreeObserver().removeOnPreDrawListener(this);
//                        return false;
//                    }
//                });
//            else
//                PicassoHelper.getPicasso(iv_CropsImage.getContext())
//                        .load(url)
//                        .config(Bitmap.Config.RGB_565)
//                        .resize(width, width).centerInside()
//                        .placeholder(R.drawable.ic_drawer_crops)
//                        .error(R.drawable.ic_drawer_crops)
//                        .into(iv_CropsImage); //todo error
//        }

        for (int i = 0; i < ll_PriceContainer.getChildCount(); i++) {
            TextView tv_refreshDate = (TextView) ll_PriceContainer.getChildAt(i).findViewById(R.id.tv_PriceDate_CropItem);
            TextView tv_refresh = (TextView) ll_PriceContainer.getChildAt(i).findViewById(R.id.tv_RefresPrice_CropsItem);
            TextView tv_Price = (TextView) ll_PriceContainer.getChildAt(i).findViewById(R.id.tv_Price);
            TextView tv_Marketeer_CropItem = (TextView) ll_PriceContainer.getChildAt(i).findViewById(R.id.tv_Marketeer_CropItem);


            PriceBase priceModel = model.prices.get(i);

            if (priceModel != null) {

                tv_Price.setText(StringFormaterUtil.parsePrice(priceModel.price));
                tv_Marketeer_CropItem.setText(priceModel.source.name);

                tv_refreshDate.setVisibility(View.INVISIBLE); //don't show date here anyway

/*
                try {
                    long time = format.parse(priceModel.data).getTime();
                    tv_refreshDate.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(time));
                } catch (ParseException e) {
                    tv_refreshDate.setVisibility(View.INVISIBLE);
                }
*/

            }
            if (i != 0) {
                tv_refreshDate.setVisibility(View.INVISIBLE);
                tv_refresh.setOnClickListener(null);
                tv_refresh.setVisibility(View.INVISIBLE);
            }

        }

    }

    @SuppressWarnings("unused")
    @OnClick(R.id.cb_FavouriteCropsItem)
    protected void onFaveChecked() {
        dateHolder.getCallback().onFavChecked(dateHolder.getModel(), cb_Fav.isChecked());
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.crops_item_view)
    protected void onImageCLicked() {
        dateHolder.getCallback().onItemClicked(dateHolder.getModel());
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.tv_RefresPrice_CropsItem)
    protected void onRefreshClicked() {
        dateHolder.getCallback().onPriceRefreshClicked(dateHolder.getModel());
    }

}
