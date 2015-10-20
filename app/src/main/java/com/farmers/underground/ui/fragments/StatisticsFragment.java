package com.farmers.underground.ui.fragments;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.ui.activities.PricesActivity;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.models.ChartDataModel;
import com.farmers.underground.ui.models.DateRange;
import com.farmers.underground.ui.utils.ResUtil;
import com.farmers.underground.ui.utils.TypefaceManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar
 * on 10/9/15.
 */
public class StatisticsFragment extends BaseFragment<PricesActivity> implements PricesActivity.DateRangeSetter, OnChartValueSelectedListener, PricesActivity.PageListener {


    //head block
    @Bind(R.id.tv_HeadTitle_SF)
    protected TextView tv_HeadTitle_SF;
    @Bind(R.id.tv_Month_SF)
    protected TextView tv_Month_SF; //picker
    @Bind(R.id.ll_Month_pick_Container_SF)
    protected LinearLayout ll_Month_pick_Container_SF;

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

    private LastCropPricesModel mCropModel;
    private DateRange mDateRange;

    private float popupValue;

    private void initPageToShow(int pageNumber) {
        final Resources res = getResources();
        if (pageNumber == 1) { //1
            imArrowLeft.setImageDrawable(ResUtil.getDrawable(res, R.drawable.line_left_not_active));
            imArrowRight.setImageDrawable(ResUtil.getDrawable(res, R.drawable.line_rigth_active));
            imDotOne.setImageDrawable(ResUtil.getDrawable(res, R.drawable.dot_blue));
            imDotTwo.setImageDrawable(ResUtil.getDrawable(res, R.drawable.dot_gray));
            tvPageNumberTitle.setText(res.getString(R.string.page_number_one_statistics_fragment));
        } else if (pageNumber == 2) { //2
            imArrowLeft.setImageDrawable(ResUtil.getDrawable(res, R.drawable.line_left_active));
            imArrowRight.setImageDrawable(ResUtil.getDrawable(res, R.drawable.line_right_not_active));
            imDotOne.setImageDrawable(ResUtil.getDrawable(res, R.drawable.dot_gray));
            imDotTwo.setImageDrawable(ResUtil.getDrawable(res, R.drawable.dot_blue));
            tvPageNumberTitle.setText(res.getString(R.string.page_number_two_statistics_fragment));
        }
        currentPage = pageNumber;
        onPageSelected(currentPage);
        llPageSwitcherContainer.requestLayout();
    }

    @OnClick(R.id.im_arrow_left)
    protected void leftArrowClicked() {
        if (currentPage != 1) {
            initPageToShow(1);
        }
    }

    @OnClick(R.id.im_arrow_right)
    protected void rightArrowClicked() {
        if (currentPage != 2) {
            initPageToShow(2);
        }
    }

    private void setupDefaultPage() {
        initPageToShow(defaultPage);
        //set click listeners maybe ;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new GsonBuilder().create();
        mCropModel = gson.fromJson(getArguments().getString(ProjectConstants.KEY_DATA), LastCropPricesModel.class);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_statistics;
    }


    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        ButterKnife.bind(this, v);

        setupDefaultPage();
        //use currentPage int;

        defChart();
        defRadioButtons();
        tv_GraphDescription_SF.setText(getContext().getString(R.string.statistics_description_1));

        setChartData(generateChartData());
    }

    @Override
    public void setDateRange(DateRange dateRange) {
        mDateRange = dateRange;
    }

    private void defChart() {
        mChart.setDescription("");
        mChart.setMaxVisibleValueCount(11);
        mChart.setPinchZoom(false);
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

                showPopup(me.getX(), me.getY(), popupValue);

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
        final Resources res = getContext().getResources();

        YAxis y = mChart.getAxisLeft();
        y.setTypeface(TypefaceManager.getInstance().getArialBold());
        y.setTextSize(12f);
        y.setTextColor(ResUtil.getColor(res, R.color.bg_graph_year));
        y.setAxisLineColor(ResUtil.getColor(res, R.color.bg_graph_devider));

        mChart.animateY(2500);
        mChart.setOnChartValueSelectedListener(this);
    }

    private void setChartData(ChartDataModel mChartModel) {
        int[] color = new int[11];
        color[0] = R.color.bg_graph_aqua;
        color[1] = R.color.bg_graph_golden;
        color[2] = R.color.bg_graph_light_blue;
        color[3] = R.color.opaque;
        color[4] = R.color.bg_graph_aqua;
        color[5] = R.color.bg_graph_golden;
        color[6] = R.color.bg_graph_light_blue;
        color[7] = R.color.opaque;
        color[8] = R.color.bg_graph_aqua;
        color[9] = R.color.bg_graph_golden;
        color[10] = R.color.bg_graph_light_blue;


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
        count = 0;
        ArrayList<String> xVals = new ArrayList<>();
        for (ChartDataModel.ChartModel item : mChartModel.charts) {
            for (Float childItem : item.prices) {
                xVals.add("");
                count++;
            }
            if (count < 10) {
                xVals.add("");
                count++;
            }
        }
        final BarDataSet set1 = new BarDataSet(yVals1, "Data Set");

        set1.setColors(color, getContext());
        set1.setDrawValues(false);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);


        BarData data = new BarData(xVals, dataSets);
        mChart.setData(data);
        mChart.invalidate();
    }

    private ChartDataModel generateChartData() {
        ChartDataModel.ChartModel chart1 = new ChartDataModel.ChartModel(11.5f, 5.0f, 7.0f);
        ChartDataModel.ChartModel chart2 = new ChartDataModel.ChartModel(1.5f, 50.0f, 17.0f);
        ChartDataModel.ChartModel chart3 = new ChartDataModel.ChartModel(3.5f, 0, 7.0f);
        return new ChartDataModel(chart1, chart2, chart3);
    }

    private void defRadioButtons() {
        for (int i = 0; i < mRadioButtons.size(); i++) {
            mRadioButtons.get(i).setTypeface(TypefaceManager.getInstance().getArialBold());
        }
        mRadioButtons.get(2).setChecked(true);
    }

    @OnCheckedChanged(R.id.rb0_SF)
    protected void radio0(boolean isChecked) {
        if (isChecked) {
            for (RadioButton item : mRadioButtons)
                if (item.getId() != R.id.rb0_SF) item.setChecked(false);
        }
    }

    @OnCheckedChanged(R.id.rb1_SF)
    protected void radio1(boolean isChecked) {
        if (isChecked) {
            for (RadioButton item : mRadioButtons)
                if (item.getId() != R.id.rb1_SF) item.setChecked(false);
        }
    }

    @OnCheckedChanged(R.id.rb2_SF)
    protected void radio2(boolean isChecked) {
        if (isChecked) {
            for (RadioButton item : mRadioButtons)
                if (item.getId() != R.id.rb2_SF) item.setChecked(false);
        }
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        popupValue = e.getVal();

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onPageSelected(int page) {
        if (page == 1) {
            tv_HeadTitle_SF.setText(getString(R.string.page_head_one_statistics_fragment));
            ll_Month_pick_Container_SF.setVisibility(View.GONE);
        } else if (page == 2) {
            tv_HeadTitle_SF.setText(getString(R.string.page_head_two_statistics_fragment));
            ll_Month_pick_Container_SF.setVisibility(View.VISIBLE);
        }

    }

    private void showPopup(float touchX, float touchY, float value) {
        final View popupView = LayoutInflater.from(getContext()).inflate(R.layout.statistic_popup, null, false);
        final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupView.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ((TextView) popupWindow.getContentView().findViewById(R.id.tv_Value_Popup)).setText(String.format("%.2f",
                value));


        float offsetX = touchX - mChart.getLeft() - popupView.getMeasuredWidth() / 2;
        float offsetY = touchY - mChart.getBottom() - popupView.getMeasuredHeight() / 4 * 3;
        popupWindow.showAsDropDown(mChart, (int) offsetX, (int) offsetY);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (popupWindow.isShowing()) popupWindow.dismiss();
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}