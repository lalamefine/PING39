package com.esigelec.ping39;

import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

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
        graph.getViewport().setMaxX(60000);
        graph.getViewport().setMinY(-10);
        graph.getViewport().setMaxY(10);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setScalableY(false);
        graph.getViewport().setScrollable(true);
        graph.getGridLabelRenderer().setLabelVerticalWidth(50);

        initTime = SystemClock.uptimeMillis();

        // first mSeries is a line
        Paint csPaint = new Paint();
        csPaint.setStrokeWidth(5);
        csPaint.setColor(0xff073C66);
        mSeriesX = new LineGraphSeries<>();
        mSeriesX.setDrawDataPoints(false);
        mSeriesX.setDrawBackground(false);
        graph.addSeries(mSeriesX);
        mSeriesY = new LineGraphSeries<>();
        mSeriesY.setDrawDataPoints(false);
        mSeriesY.setDrawBackground(false);
        csPaint.setColor(0xff880000);
        mSeriesY.setCustomPaint(csPaint);
        graph.addSeries(mSeriesY);
    }

    public void AddData(float valX,float valY) {
        mSeriesX.appendData(new DataPoint((SystemClock.uptimeMillis()-initTime), valX), true, 500);
        mSeriesY.appendData(new DataPoint((SystemClock.uptimeMillis()-initTime), valY), true, 500);
    }

    public void FourrierCalc(){

    }
}
