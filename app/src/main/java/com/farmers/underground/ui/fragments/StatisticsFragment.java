package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.ui.activities.PricesActivity;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.models.ChartDataModel;
import com.farmers.underground.ui.models.DateRange;
import com.farmers.underground.ui.utils.TypefaceManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar on 10/9/15.
 */
public class StatisticsFragment extends BaseFragment implements PricesActivity.DateRangeSetter, OnChartValueSelectedListener{


    @Bind(R.id.chart_SF)
    protected BarChart mChart;
    @Bind (R.id.tv_GraphDescription_SF)
    protected TextView tv_GraphDescription_SF;

    @Bind({R.id.rb0_SF, R.id.rb1_SF, R.id.rb2_SF})
    protected List<RadioButton> mRadioButtons;


    private LastCropPricesModel mCropModel;
    private DateRange mDateRange;


    public static BaseFragment getInstance(LastCropPricesModel cropModel) {
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
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        ButterKnife.bind(this, v);

        defChart();
        defRadioButtons();
        tv_GraphDescription_SF.setText(getContext().getString(R.string.statistics_description_1));

        setChartData(generateChartData());

    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_statistics;
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


        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setDrawGridLines(false);

        YAxis y = mChart.getAxisLeft();
        y.setTypeface(TypefaceManager.getInstance().getArialBold());
        y.setTextColor(getContext().getResources().getColor(R.color.bg_graph_year));
        y.setTextSize(12f);
        y.setAxisLineColor(getContext().getResources().getColor(R.color.bg_graph_devider));

        mChart.animateY(2500);
        mChart.setOnChartValueSelectedListener(this);
    }

    private void setChartData(ChartDataModel mChartModel){
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
        ChartDataModel.ChartModel chart3 = new ChartDataModel.ChartModel( 3.5f, 0, 7.0f);
        return new ChartDataModel(chart1, chart2, chart3);
    }

    private void defRadioButtons(){
        for (int i = 0; i < mRadioButtons.size(); i++) {
            mRadioButtons.get(i).setTypeface(TypefaceManager.getInstance().getArialBold());
        }
    }

    @OnCheckedChanged(R.id.rb0_SF)
    protected void radio0(boolean isChecked){
        if(isChecked);
    }
    @OnCheckedChanged(R.id.rb1_SF)
    protected void radio1(boolean isChecked){
        if(isChecked);
    }
    @OnCheckedChanged(R.id.rb2_SF)
    protected void radio2(boolean isChecked){
        if(isChecked);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.d("lalal", String.valueOf(e.getVal()));
        //todo show black popup here
    }

    @Override
    public void onNothingSelected() {

    }
}