package com.esigelec.ping39.System;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class LineGraph {

    private LineGraphSeries<DataPoint> mSeriesX;
    private final int NB_DATAPOINT_ARCHIVED = 5000;

    public void initGraph(GraphView graph) {
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(-30);
        graph.getViewport().setMaxX(30);
        graph.getViewport().setMinY(-90);
        graph.getViewport().setMaxY(90);
        graph.getViewport().setScalableY(false);
        graph.getViewport().setScrollable(false);
        graph.getGridLabelRenderer().setLabelVerticalWidth(50);

    }

    public void AddData(float valX) {

        mSeriesX.appendData(new DataPoint(0, valX), true, NB_DATAPOINT_ARCHIVED);
    }
}
