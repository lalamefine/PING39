package com.esigelec.ping39.System;

import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


/**
 * Created by jonas on 10.09.16.
 */
public class FullTimeGraph {
    private LineGraphSeries<DataPoint> mSeriesX;
    private LineGraphSeries<DataPoint> mSeriesY;

    private long initTime;
    private long capTime;
    private final int NB_DATAPOINT_ARCHIVED = 6000;
    private GraphView graph;

    public void initGraph(GraphView graph) {
        this.graph = graph;
        graph.getGridLabelRenderer().setLabelVerticalWidth(50);
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(1);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(1);
        initTime = SystemClock.uptimeMillis();
        capTime = SystemClock.uptimeMillis()+1000;
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
        if(SystemClock.uptimeMillis()-capTime>250){
            capTime = SystemClock.uptimeMillis();
            graph.getViewport().setMaxX(((float)(SystemClock.uptimeMillis()-initTime))/60000);
            double val =  graph.getViewport().getMaxY(true);
            if(valX>val) val = Math.floor(valX+1);
            if(valY>val) val = Math.floor(valY+1);
            graph.getViewport().setMaxY(val);
            mSeriesX.appendData(new DataPoint(((double)(SystemClock.uptimeMillis()-initTime))/60000, valX), false, NB_DATAPOINT_ARCHIVED);
            mSeriesY.appendData(new DataPoint(((double)(SystemClock.uptimeMillis()-initTime))/60000, valY), false, NB_DATAPOINT_ARCHIVED);
        }
    }


}

