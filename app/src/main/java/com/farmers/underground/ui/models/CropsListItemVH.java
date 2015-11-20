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
import com.farmers.underground.R;
import com.farmers.underground.config.ApiConstants;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.remote.models.CropPrices;
import com.farmers.underground.ui.adapters.CropsListAdapter;
import com.farmers.underground.ui.utils.DateHelper;
import com.farmers.underground.ui.utils.StringFormatterUtil;
import java.text.SimpleDateFormat;
import java.util.Locale;

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

    /*public CardView getContainer() {
        return container;
    }*/

    private CropsListItemDH dateHolder;

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

        String fullDate = model.prices.get(0).data;

        SimpleDateFormat format = new SimpleDateFormat(ProjectConstants.SERVER_DATE_FORMAT, Locale.getDefault());
        try {
            long time = format.parse(fullDate).getTime();
            String[] date = dateHelper.getDate(time);
            tv_Number.setText(date[0]);
            tv_MonthWord.setText(date[1]);
            tv_YearText.setText(date[2]);

        } catch (Exception e) {
            e.printStackTrace();
        }

        cb_Fav.setChecked(model.isInFavorites);

        final String url = !TextUtils.isEmpty(dateHolder.getModel().image) ? ApiConstants.BASE_URL + dateHolder.getModel().image : null;

        CropsListAdapter.getImageLoader().displayImage(url, iv_CropsImage);

        for (int i = 0; i < ll_PriceContainer.getChildCount(); i++) {

            TextView tv_refresh = (TextView) ll_PriceContainer.getChildAt(i).findViewById(R.id.tv_RefresPrice_CropsItem);
            TextView tv_Price = (TextView) ll_PriceContainer.getChildAt(i).findViewById(R.id.tv_Price);
            TextView tv_Marketeer_CropItem = (TextView) ll_PriceContainer.getChildAt(i).findViewById(R.id.tv_Marketeer_CropItem);

            CropPrices priceModel = model.prices.get(i);

            if (priceModel != null) {

                tv_Price.setText(StringFormatterUtil.parsePrice(priceModel.price));
                tv_Marketeer_CropItem.setText(priceModel.source.name);

            }
            if (i != 0) {

                tv_refresh.setOnClickListener(null);
                tv_refresh.setVisibility(View.INVISIBLE);
            }

        }

    }

    @SuppressWarnings("unused")
    @OnClick(R.id.cb_FavouriteCropsItem)
    protected void onFaveChecked() {
        dateHolder.getCallback().onFavChecked(dateHolder.getModel(), cb_Fav);
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
