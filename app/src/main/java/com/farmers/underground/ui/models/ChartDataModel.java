package com.farmers.underground.ui.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar on 10/15/15.
 */
public class ChartDataModel {
    public List<ChartModel> charts;

    public ChartDataModel(  ChartModel chart1, ChartModel chart2, ChartModel chart3) {
        this.charts = new ArrayList<>();
        this.charts.add(chart1);
        this.charts.add(chart2);
        this.charts.add(chart3);
    }

    public static class ChartModel {

        public List<Float> prices;

        public ChartModel( float data1, float data2, float data3) {

            this.prices = new ArrayList<>();
            this.prices.add(data1);
            this.prices.add(data2);
            this.prices.add(data3);

        }
    }


}
