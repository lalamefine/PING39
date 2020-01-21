package com.esigelec.ping39.System;

import android.graphics.Paint;
import android.os.SystemClock;

import com.esigelec.ping39.Model.GlobalHolder;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GMGraph {

    private LineGraphSeries<DataPoint> mSeriesX;

    private long initTime;
    private long capTime;
    private double lastval;

    private final int NB_DATAPOINT_ARCHIVED = 6000;

    public void initGraph(GraphView graph) {
        capTime = SystemClock.uptimeMillis()+1000;
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(60.000);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(20);
        graph.getViewport().setScrollable(true);
        graph.getGridLabelRenderer().setVerticalAxisTitle("GM");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Temps en secondes");
        graph.getGridLabelRenderer().setLabelVerticalWidth(50);

        initTime = SystemClock.uptimeMillis();

        Paint csPaint = new Paint();
        csPaint.setStrokeWidth(GlobalHolder.lineDrawWidth);
        csPaint.setColor(0xff073C66);
        mSeriesX = new LineGraphSeries<>();
        mSeriesX.setDrawDataPoints(false);
        mSeriesX.setDrawBackground(true);
        mSeriesX.setCustomPaint(csPaint);
        graph.addSeries(mSeriesX);

    }

    public void AddData(float val) {
        if(SystemClock.uptimeMillis()-capTime>500) {
            capTime = SystemClock.uptimeMillis();
            lastval = val;
        }
        mSeriesX.appendData(new DataPoint(((double) (SystemClock.uptimeMillis() - initTime)) / 1000, lastval), true, NB_DATAPOINT_ARCHIVED);
    }

}

