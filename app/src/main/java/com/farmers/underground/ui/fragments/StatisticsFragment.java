package com.farmers.underground.ui.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import com.farmers.underground.FarmersApp;
import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.remote.models.StaticticModel;
import com.farmers.underground.ui.activities.PricesActivity;
import com.farmers.underground.ui.adapters.ToolbarSpinnerAdapter;
import com.farmers.underground.ui.base.BasePagerPricesFragment;
import com.farmers.underground.ui.custom_views.PriceView;
import com.farmers.underground.ui.models.ChartDataModel;
import com.farmers.underground.ui.models.DateRange;
import com.farmers.underground.ui.utils.ResUtil;
import com.farmers.underground.ui.utils.StringFormatterUtil;
import com.farmers.underground.ui.utils.TypefaceManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by omar
 * on 10/9/15.
 */
public class StatisticsFragment extends BasePagerPricesFragment<String>
        implements PricesActivity.PageListener, ToolbarSpinnerAdapter.SpinnerCallback, PricesActivity.MonthPickerCallback, PricesActivity.StatisticCallback {

    private final static int POPUP_SHOW_TIME_MILIS = 3000;

    //head block
    @Bind(R.id.tv_HeadTitle_SF)
    protected TextView tv_HeadTitle_SF;
    @Bind(R.id.tv_Month_SF)
    protected TextView tv_Month_SF; //picker
    @Bind(R.id.ll_Month_pick_Container_SF)
    protected LinearLayout ll_Month_pick_Container_SF;

    @Bind(R.id.ll_PricesContainer_SF)
    protected LinearLayout ll_PricesContainer_SF;

    @Bind(R.id.layout_marketer_SF)
    protected LinearLayout layout_marketer_SF_Container;
    @Bind(R.id.layout_market_two_SF)
    protected LinearLayout layout_market_two_SF_Container;
    @Bind(R.id.layout_market_one_SF)
    protected LinearLayout layout_market_one_SF_Container;

    private PriceView layout_marketer_SF = new PriceView();
    private PriceView layout_market_two_SF = new PriceView();
    private PriceView layout_market_one_SF = new PriceView();

    //chart views
    @Bind(R.id.chart_SF)
    protected BarChart mChart;
    @Bind(R.id.tv_GraphDescription_SF)
    protected TextView tv_GraphDescription_SF;
    @Bind({R.id.rb0_SF, R.id.rb1_SF, R.id.rb2_SF})
    protected List<RadioButton> mRadioButtons;

    //bottom switcher
    private static final int defaultPage = 1; // 0 - not used
    private int currentPage;
    @Bind(R.id.llPageSwitcherContainer)
    protected LinearLayout llPageSwitcherContainer;
    @Bind(R.id.im_arrow_left)
    protected ImageView imArrowLeft;
    @Bind(R.id.im_arrow_right)
    protected ImageView imArrowRight;
    @Bind(R.id.im_dot_one)
    protected ImageView imDotOne;
    @Bind(R.id.im_dot_two)
    protected ImageView imDotTwo;
    @Bind(R.id.tv_page_number_title)
    protected TextView tvPageNumberTitle;
    @Bind(R.id.rb0_SF)
    protected RadioButton rb0;
    @Bind(R.id.rb1_SF)
    protected RadioButton rb1;
    @Bind(R.id.rb2_SF)
    protected RadioButton rb2;

    private float popupValue;
    private int popupIndexSelected;
    private final int[] chartColor = {  R.color.bg_graph_aqua,
                                        R.color.bg_graph_golden,
                                        R.color.bg_graph_light_blue,
                                        R.color.opaque,
                                        R.color.bg_graph_aqua,
                                        R.color.bg_graph_golden,
                                        R.color.bg_graph_light_blue,
                                        R.color.opaque,
                                        R.color.bg_graph_aqua,
                                        R.color.bg_graph_golden,
                                        R.color.bg_graph_light_blue};
    private String[] months;

    private String spinerQuality;

    private int selectedMonth = -1;

    public enum TypeStatistic{Quality, Month}

    private void initPageToShow(int pageNumber) {
        final Resources res = getResources();
        if (pageNumber == 1) { //1
            imArrowLeft.setImageDrawable(ResUtil.getDrawable(res, R.drawable.line_right_not_active));
            imArrowRight.setImageDrawable(ResUtil.getDrawable(res, R.drawable.line_left_active));
            imDotOne.setImageDrawable(ResUtil.getDrawable(res, R.drawable.dot_blue));
            imDotTwo.setImageDrawable(ResUtil.getDrawable(res, R.drawable.dot_gray));
            tv_GraphDescription_SF.setText(getHostActivity().getString(R.string.statistics_description_1));
            tvPageNumberTitle.setText(res.getString(R.string.page_number_one_statistics_fragment));
            getHostActivity().setmTypeStatistic(TypeStatistic.Quality);
        } else if (pageNumber == 2) { //2
            imArrowLeft.setImageDrawable(ResUtil.getDrawable(res, R.drawable.line_rigth_active));
            imArrowRight.setImageDrawable(ResUtil.getDrawable(res, R.drawable.line_left_not_active));
            imDotOne.setImageDrawable(ResUtil.getDrawable(res, R.drawable.dot_gray));
            imDotTwo.setImageDrawable(ResUtil.getDrawable(res, R.drawable.dot_blue));
            tvPageNumberTitle.setText(res.getString(R.string.page_number_two_statistics_fragment));
            getHostActivity().setmTypeStatistic(TypeStatistic.Month);
        }
        currentPage = pageNumber;
        clearChartAndPrices();
        getHostActivity().makeRequestGetStatistic(spinerQuality);
        onPageSelected(currentPage);
        llPageSwitcherContainer.requestLayout();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.im_arrow_left)
    protected void leftArrowClicked() {
        if (currentPage != 1) {
            initPageToShow(1);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.im_arrow_right)
    protected void rightArrowClicked() {
        if (currentPage != 2) {
            initPageToShow(2);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.ll_Month_pick_Container_SF)
    protected void selectMonth() {
        initMonthPicker();
    }

    public void initMonthPicker() {
        getHostActivity().showMonthPicker(this);
    }

    public void setMonth(String month){
        tv_Month_SF.setText(month);
    }

    private void setupDefaultPage() {
        initPageToShow(defaultPage);
        //set click listeners maybe ;
    }

    private void colorPriceView(PriceView priceView, @ColorInt int color) {
        priceView.tv_Price_Prefix.setTextColor(color);
        priceView.tv_Price.setTextColor(color);
        priceView.tv_Marketeer_CropItem.setTextColor(color);

        downLightPriceView(priceView)  ; //for now todo
    }

    public static StatisticsFragment getInstance(LastCropPricesModel cropModel) {
        Bundle args = new Bundle();
        Gson gson = new GsonBuilder().create();
        args.putString(ProjectConstants.KEY_DATA, gson.toJson(cropModel));

        StatisticsFragment fragment = new StatisticsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_statistics;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        ButterKnife.bind(this, v);

        ButterKnife.bind(layout_marketer_SF, layout_marketer_SF_Container);
        ButterKnife.bind(layout_market_one_SF, layout_market_one_SF_Container);
        ButterKnife.bind(layout_market_two_SF, layout_market_two_SF_Container);

        setupDefaultPage();
        //use currentPage int;

        colorPriceView(layout_marketer_SF, ResUtil.getColor(getResources(), R.color.bg_graph_aqua));
        colorPriceView(layout_market_one_SF, ResUtil.getColor(getResources(), R.color.bg_graph_golden));
        colorPriceView(layout_market_two_SF, ResUtil.getColor(getResources(), R.color.bg_graph_light_blue));

        layout_marketer_SF.tv_RefresPrice_CropsItem.setVisibility(View.GONE);
        layout_marketer_SF.tv_Marketeer_CropItem.setText(R.string.your_dealer);

        layout_market_one_SF.tv_RefresPrice_CropsItem.setVisibility(View.GONE);
        layout_market_one_SF.tv_Marketeer_CropItem.setText(R.string.whole_sales);

        layout_market_two_SF.tv_RefresPrice_CropsItem.setVisibility(View.GONE);
        layout_market_two_SF.tv_Marketeer_CropItem.setText(R.string.plant_counsil);

        defChart();
        defRadioButtons();
        tv_GraphDescription_SF.setText(getHostActivity().getString(R.string.statistics_description_1));

        setChartData(generateChartData());
        getHostActivity().setmStatisticCallback(this);

        months = getResources().getStringArray(R.array.all_month);
    }

    private void defChart() {
        mChart.setDescription("");
        mChart.setMaxVisibleValueCount(11);
        mChart.setHighlightEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setScaleEnabled(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);
        mChart.getLegend().setEnabled(false);
        mChart.getAxisRight().setEnabled(false);
        mChart.getAxisLeft().setDrawAxisLine(false);
        mChart.getAxisLeft().setDrawGridLines(true);
        mChart.setDrawHighlightArrow(false);
        mChart.setDoubleTapToZoomEnabled(false);

        mChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

                float x = me.getX();
                float y = me.getY();

                popupIndexSelected = mChart.getHighlightByTouchPoint(x, y).getXIndex();
                popupValue = mChart.getEntryByTouchPoint(x, y).getVal();

                showPopup(x, y, popupValue);

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });


        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setDrawGridLines(false);
        final Resources res = getResources();

        YAxis y = mChart.getAxisLeft();
        y.setTypeface(TypefaceManager.getInstance().getArialBold());
        y.setTextSize(12f);
        y.setTextColor(ResUtil.getColor(res, R.color.bg_graph_year));
        y.setAxisLineColor(ResUtil.getColor(res, R.color.bg_graph_devider));

        mChart.animateY(2500);
    }

    private void setChartData(ChartDataModel mChartModel) {
        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        int count = 0;
        for (ChartDataModel.ChartModel item : mChartModel.charts) {
            for (Float childItem : item.prices) {
                yVals1.add(new BarEntry(childItem, count));
                count++;
            }
            if (count < 10) {
                yVals1.add(new BarEntry(0, count));
                count++;
            }
        }
        ArrayList<String> xVals = new ArrayList<>();
        for (int i = 0; i < 11; ++i)
            xVals.add("");
        final BarDataSet set1 = new BarDataSet(yVals1, "Data Set");

        set1.setColors(chartColor, FarmersApp.getInstance());
        set1.setDrawValues(false);

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);


        BarData data = new BarData(xVals, dataSets);
        mChart.setData(data);
        mChart.invalidate();
    }

    private ChartDataModel generateChartData() {
        ChartDataModel.ChartModel chart1 = new ChartDataModel.ChartModel(0.0f, 0.0f, 0.0f);
        ChartDataModel.ChartModel chart2 = new ChartDataModel.ChartModel(0.0f, 0.0f, 0.0f);
        ChartDataModel.ChartModel chart3 = new ChartDataModel.ChartModel(0.0f, 0.0f, 0.0f);
        return new ChartDataModel(chart1, chart2, chart3);
    }

    private void defRadioButtons() {
        for (int i = 0; i < mRadioButtons.size(); i++) {
            mRadioButtons.get(i).setTypeface(TypefaceManager.getInstance().getArialBold());
        }
        mRadioButtons.get(0).setChecked(true);
    }

    private void showPopup(float touchX, float touchY, float value) {
        final View popupView = LayoutInflater.from(getHostActivity()).inflate(R.layout.statistic_popup, new LinearLayout(getHostActivity()), false);
        final PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        //popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        boolean show;
        switch (popupIndexSelected) {
            case 0:
            case 1:
            case 2:
            case 4:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
                show = true;
                break;
            case 7:
            case 3:
                show = false;
                break;
            default:
                show = false;
                break;
        }

        if (show) {
            ((TextView) popupWindow.getContentView().findViewById(R.id.tv_Value_Popup))
                    .setText(StringFormatterUtil.parsePrice(value));
            int offsetPos;

            if (popupIndexSelected < 3) offsetPos = 0;
            else if (popupIndexSelected < 7) offsetPos = 1;
            else offsetPos = 2;

            final PriceView priceView = getPriceViewBySelPos(popupIndexSelected - offsetPos);

            if (priceView !=null) {
                String popUpDescription = priceView.tv_Marketeer_CropItem.getText().toString();
                ((TextView) popupWindow.getContentView().findViewById(R.id.tv_Name_Popup))
                        .setText(popUpDescription);
            }

            Resources res = getResources();
            ((TextView) popupWindow.getContentView().findViewById(R.id.tv_Value_Popup))
                    .setTextColor(ResUtil.getColor(res, chartColor[popupIndexSelected]));
            ((TextView) popupWindow.getContentView().findViewById(R.id.tv_Name_Popup))
                    .setTextColor(ResUtil.getColor(res, chartColor[popupIndexSelected]));
            ((TextView) popupWindow.getContentView().findViewById(R.id.tv_Symbol_Popup))
                    .setTextColor(ResUtil.getColor(res, chartColor[popupIndexSelected]));

            popupView.invalidate();

            popupView.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            float offsetX = touchX - mChart.getLeft() - popupView.getMeasuredWidth() / 2 ;
            float offsetY = touchY - mChart.getBottom() - popupView.getMeasuredHeight() / 4 * 3;

            popupWindow.showAsDropDown(mChart, (int) offsetX, (int) offsetY);

            popupView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getHostActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (getHostActivity() != null) {

                                    if (popupWindow.isShowing()) {
                                        popupWindow.dismiss();
                                    }
                                }
                            }
                        });
                    mChart.highlightValue(popupIndexSelected, -1);
                }
            }, POPUP_SHOW_TIME_MILIS);

            setItemHighlight(popupIndexSelected - offsetPos, priceView);
        }
    }

    @SuppressWarnings("unused")
    @OnCheckedChanged(R.id.rb0_SF)
    protected void radio0(boolean isChecked) {
        onPickRadio(isChecked, R.id.rb0_SF, 0);
    }

    @SuppressWarnings("unused")
    @OnCheckedChanged(R.id.rb1_SF)
    protected void radio1(boolean isChecked) {
        onPickRadio(isChecked, R.id.rb1_SF, 1);
    }

    @SuppressWarnings("unused")
    @OnCheckedChanged(R.id.rb2_SF)
    protected void radio2(boolean isChecked) {
        onPickRadio(isChecked, R.id.rb2_SF, 2);
    }

    private void onPickRadio(boolean isChecked, int idRb, int posRb){
        if (isChecked) {
            for (RadioButton item : mRadioButtons)
                if (item.getId() != idRb) item.setChecked(false);
        }
        if(chart != null) {
            layout_marketer_SF.tv_Price.setText(StringFormatterUtil.parsePrice(chart[posRb].prices.get(0)));
            layout_market_one_SF.tv_Price.setText(StringFormatterUtil.parsePrice(chart[posRb].prices.get(1)));
            layout_market_two_SF.tv_Price.setText(StringFormatterUtil.parsePrice(chart[posRb].prices.get(2)));
        }
    }

    @Override
    public void onPageSelected(int page) {
        if (page == 1) {
            tv_HeadTitle_SF.setText(String.format(getString(R.string.page_head_one_statistics_fragment), getHostActivity().getCropModel().displayName));
            ll_Month_pick_Container_SF.setVisibility(View.GONE);
            tv_GraphDescription_SF.setText(getHostActivity().getString(R.string.statistics_description_1));
        } else if (page == 2) {
            tv_HeadTitle_SF.setText(getString(R.string.page_head_two_statistics_fragment));
            ll_Month_pick_Container_SF.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onSpinnerItemSelected(String s) {
        spinerQuality=s;
        clearChartAndPrices();
        getHostActivity().makeRequestGetStatistic(s);
    }

    private void clearChartAndPrices(){

        if (currentPage == 2 && selectedMonth == -1){
            tv_GraphDescription_SF.setText("");
        } else if(currentPage == 2 && selectedMonth > -1){
            showDifColoredText(months[selectedMonth]);
        } else if (currentPage == 1) {
            tv_GraphDescription_SF.setText(getHostActivity().getString(R.string.statistics_description_1));
        }

        if (mChart!=null)
            mChart.clear();

        chooseRB();

        layout_marketer_SF.tv_Price.setText("- -");
        layout_market_one_SF.tv_Price.setText("- -");
        layout_market_two_SF.tv_Price.setText("- -");

    }

    private void downLightPriceView(PriceView priceView) {
        priceView.tv_Price_Prefix.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F);
        priceView.tv_Price_Prefix.setTypeface(TypefaceManager.getInstance().getArial());

        priceView.tv_Price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25F);
        priceView.tv_Price.setTypeface(TypefaceManager.getInstance().getRobotoLight());

        priceView.tv_Marketeer_CropItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11F);
        priceView.tv_Marketeer_CropItem.setTypeface(TypefaceManager.getInstance().getArial());
    }
    private void heightLightPriceView(PriceView priceView) {
        priceView.tv_Price_Prefix.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.6F);
        priceView.tv_Price_Prefix.setTypeface(TypefaceManager.getInstance().getArialBold());

        priceView.tv_Price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30.0F);
        priceView.tv_Price.setTypeface(TypefaceManager.getInstance().getArialBold());

        priceView.tv_Marketeer_CropItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13.0F);
        priceView.tv_Marketeer_CropItem.setTypeface(TypefaceManager.getInstance().getArialBold());
    }

    /** item = [0;8] */
    @Nullable
    private PriceView getPriceViewBySelPos(int item) {
        //remark: items coming  in 3 sets LTR:  0-1-2,  3-4-5,  6-7-8

        final PriceView priceView;
        switch (item){
            case 0:
            case 3:
            case 6:
                priceView = layout_marketer_SF;
                break;
            case 1:
            case 4:
            case 7:
                priceView = layout_market_one_SF;
                break;
            case 2:
            case 5:
            case 8:
                priceView = layout_market_two_SF;
                break;
            default:
                priceView = null;
        }

        return priceView;

    }

    /** item = [0;8] */
    private void setItemHighlight(int item, final PriceView priceView) {
        //remark: items coming  in 3 sets LTR:  0-1-2,  3-4-5,  6-7-8

        //skip Highlight price is this case =)
        if((rb0.isChecked() && !(item == 0 || item == 1 || item == 2)) ||
                (rb1.isChecked() && !(item == 3 || item == 4 || item == 5)) ||
                (rb2.isChecked() && !(item == 6 || item == 7 || item == 8)))
            return;

        if (priceView == null)
            return;

        heightLightPriceView(priceView);

        ll_PricesContainer_SF.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getHostActivity() != null) {
                    downLightPriceView(priceView);
                }
            }
        }, POPUP_SHOW_TIME_MILIS);
    }

    ChartDataModel.ChartModel[] chart;

    private ChartDataModel createChartData(List<StaticticModel> result) {
        chart = new ChartDataModel.ChartModel[3];
        StaticticModel model;
        for(int i = 0; i < 3; ++i){
            model = result.get(i);
            chart[i] = new ChartDataModel.ChartModel(toFloat(model.priceMk), toFloat(model.priceWs), toFloat(model.pricePc));
        }
        return new ChartDataModel(chart[0], chart[1], chart[2]);
    }

    private float toFloat(Double val){
        return  val != null ? Float.valueOf(Double.toString(val)) : 0.0f;
    }

    private void chooseRB(){
        for(int i = 0; i < 3; ++i){
            if(mRadioButtons.get(i).isChecked()){
                onPickRadio(true, mRadioButtons.get(i).getId(), i);
            }
        }
    }

    @Override
    public void setDateRange(DateRange dateRange, boolean isAllTime) {
        /*stub*/
    }

    @Override
    public void onPickMonth(int numMonth) {
        selectedMonth = numMonth;
        setMonth(months[numMonth]);
        showDifColoredText(months[numMonth]);
    }

    @Override
    public void onGetResult(List<StaticticModel> result) {

        Collections.reverse(result); // hebrew order =)

        setChartData(createChartData(result));

        rb0.setText(getTextForRBFromResult(result, 0));
        rb1.setText(getTextForRBFromResult(result, 1));
        rb2.setText(getTextForRBFromResult(result, 2));

        chooseRB();
    }

    private String getTextForRBFromResult(List<StaticticModel> result, int pos){
        return months[Integer.valueOf(result.get(pos).month)] + " " + result.get(pos).year;
    }

    @Override
    public void onError() {

    }

    /*sub-page 2*/
    private void showDifColoredText(String monthToShowInOtherColor){

        String text = String.format(getResources().getString(R.string.chart_head_page_two_statistics_fragment), monthToShowInOtherColor);

        Spannable wordToSpan = new SpannableString(text);
        wordToSpan.setSpan(new ForegroundColorSpan(ResUtil.getColor(getResources(),R.color.stat_color_month_in_text)),
                text.indexOf(monthToShowInOtherColor), text.indexOf(monthToShowInOtherColor) + monthToShowInOtherColor.length() , 0);

        tv_GraphDescription_SF.setText(wordToSpan);
    }
}