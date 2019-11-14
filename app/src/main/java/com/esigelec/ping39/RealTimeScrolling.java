package com.esigelec.ping39;

import android.os.Handler;
import android.os.SystemClock;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

/**
 * Created by jonas on 10.09.16.
 */
class RealtimeScrolling {
    private final Handler mHandler = new Handler();
    private Runnable mTimer;
    private double graphLastXValue = 5d;
    private LineGraphSeries<DataPoint> mSeriesX;
    private LineGraphSeries<DataPoint> mSeriesY;
    private long initTime;
    public void initGraph(GraphView graph) {
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(150);
        graph.getViewport().setMinY(-10);
        graph.getViewport().setMaxY(10);
        graph.getViewport().setScalableY(false);
        graph.getViewport().setScrollable(true);
        graph.getGridLabelRenderer().setLabelVerticalWidth(100);

        initTime = SystemClock.uptimeMillis();

        // first mSeries is a line
        mSeriesX = new LineGraphSeries<>();
        mSeriesX.setDrawDataPoints(false);
        mSeriesX.setDrawBackground(true);
        graph.addSeries(mSeriesX);

        mSeriesY = new LineGraphSeries<>();
        mSeriesY.setDrawDataPoints(false);
        mSeriesY.setDrawBackground(true);
        graph.addSeries(mSeriesY);
    }

    public void AddData(float valX,float valY) {
        mSeriesX.appendData(new DataPoint((SystemClock.uptimeMillis()-initTime)/1000, valX), true, 500);
        mSeriesY.appendData(new DataPoint((SystemClock.uptimeMillis()-initTime)/1000, valY), true, 500);
    }
}
