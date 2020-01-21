package com.esigelec.ping39.System;

import android.graphics.Paint;
import android.os.SystemClock;

import com.esigelec.ping39.Model.GlobalHolder;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


/**
 * Created by jonas on 10.09.16.
 */
public class RealtimeScrolling {
    private LineGraphSeries<DataPoint> mSeriesX;
    private LineGraphSeries<DataPoint> mSeriesY;

    private long initTime;

    private final int NB_DATAPOINT_ARCHIVED = 1500;

    public void initGraph(GraphView graph) {
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(60.000);
        graph.getViewport().setMinY(-45);
        graph.getViewport().setMaxY(45);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setScalableY(false);
        graph.getViewport().setScrollable(true);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Angle en degrés");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Temps en secondes");
        graph.getGridLabelRenderer().setLabelVerticalWidth(50);

        initTime = SystemClock.uptimeMillis();

        Paint csPaint = new Paint();
        csPaint.setStrokeWidth(GlobalHolder.lineDrawWidth);
        csPaint.setColor(0xff073C66);
        mSeriesX = new LineGraphSeries<>();
        mSeriesX.setDrawDataPoints(false);
        mSeriesX.setDrawBackground(false);
        mSeriesX.setCustomPaint(csPaint);
        graph.addSeries(mSeriesX);

        Paint csPaint2 = new Paint();
        csPaint2.setStrokeWidth(GlobalHolder.lineDrawWidth);
        csPaint2.setColor(0xff880000);
        mSeriesY = new LineGraphSeries<>();
        mSeriesY.setDrawDataPoints(false);
        mSeriesY.setDrawBackground(false);
        mSeriesY.setCustomPaint(csPaint2);
        graph.addSeries(mSeriesY);
    }

    public void AddData(float valX,float valY) {
        mSeriesX.appendData(new DataPoint(((double)(SystemClock.uptimeMillis()-initTime))/1000, valX), true, NB_DATAPOINT_ARCHIVED);
        mSeriesY.appendData(new DataPoint(((double)(SystemClock.uptimeMillis()-initTime))/1000, valY), true, NB_DATAPOINT_ARCHIVED);
    }


}
