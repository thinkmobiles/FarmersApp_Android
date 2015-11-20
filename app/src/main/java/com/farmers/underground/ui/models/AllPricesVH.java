package com.farmers.underground.ui.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.models.CropPrices;
import com.farmers.underground.ui.utils.DateHelper;
import com.farmers.underground.ui.utils.ResUtil;
import com.farmers.underground.ui.utils.StringFormatterUtil;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by omar
 * on 10/2/15.
 */
public class AllPricesVH extends RecyclerView.ViewHolder {
    public static final int MAX_PRICES_USER_CAN_ADD = 100 ;

    @Bind(R.id.tv_DayAllPrices)
    protected TextView tv_Number;

    @Bind(R.id.tv_MonthCalendarAllPrices)
    protected TextView tv_MonthWord;

    @Bind(R.id.tv_YearAllPrices)
    protected TextView tv_YearText;

    @Bind(R.id.layout_marketer)
    protected LinearLayout layout_marketer;

    @Bind(R.id.layout_market_one)
    protected LinearLayout layout_market_one;

    @Bind(R.id.layout_market_two)
    protected LinearLayout layout_market_two;

    @Bind(R.id.ll_PricesContainer_CropItem)
    protected LinearLayout ll_PriceContainer;

    @Bind(R.id.v_Devider_AllPrices)
    View devider;

    @Bind(R.id.all_prices_item_view)
    protected View container;

    public View getContainer() {
        return container;
    }

    private AllPricesDH dateHolder;
    private LinearLayout[] layouts;

    public AllPricesVH(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        layouts = new LinearLayout[3];
        layouts[0] = layout_marketer;
        layouts[1] = layout_market_one;
        layouts[2] = layout_market_two;
    }

    public void bindData(final AllPricesDH dateHolder, boolean hideDevider) {

        this.dateHolder = dateHolder;

        List<CropPrices> prices =  dateHolder.getModel().prices;

        for(int i = 0; i < 3; ++i) {
            setSource(layouts[i].findViewById(R.id.tv_Marketeer_CropItem), prices.get(i).source.name);
            setPrice(layouts[i].findViewById(R.id.tv_Price), prices.get(i).price);
            setVisibilityAndListener(layouts[i].findViewById(R.id.tv_RefresPrice_CropsItem), i);
        }

        setDate(prices.get(0).data);

        if (hideDevider)
            devider.setVisibility(View.GONE);
        else
            devider.setVisibility(View.VISIBLE);
    }

    private void setSource(View tvSource, String nameSource){
        ((TextView) tvSource).setText(nameSource);
    }

    private void setPrice(View tvPrice, Double price){
        ((TextView) tvPrice).setText(StringFormatterUtil.parsePrice(price));
    }

    private void setVisibilityAndListener(View view, final int pos){
        final CropPrices priceBase = dateHolder.getModel().prices.get(pos);
        if(pos != 0) {
            int sizeMore = priceBase.more.size();
            view.setVisibility(sizeMore > 0 ? View.VISIBLE : View.INVISIBLE);
            ((TextView) view).setText(priceBase.quality);
            if(sizeMore == 1)
                ((TextView) view).setTextColor(ResUtil.getColor(container.getResources(),R.color.text_light_grey));
            layouts[pos].setOnClickListener(sizeMore > 1 ? new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dateHolder.getCallback().onMorePricesClicked(priceBase);
                }
            } : null);

        } else {
            boolean isEnable = priceBase.more.size() < MAX_PRICES_USER_CAN_ADD; //great
            view.setVisibility(isEnable ? View.VISIBLE : View.INVISIBLE);
            layouts[pos].setOnClickListener(isEnable ? new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dateHolder.getCallback().onAddPricesClicked(priceBase.data);
                }
            } : null);
        }
    }

    private void setDate(String day){
        SimpleDateFormat format = new SimpleDateFormat(ProjectConstants.SERVER_DATE_FORMAT, Locale.getDefault());
        try {
            long time = format.parse(day).getTime();
            String[] date = DateHelper.getInstance(container.getContext()).getDate(time);
            tv_Number.setText(date[0]);
            tv_MonthWord.setText(date[1]);
            tv_YearText.setText(date[2]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
