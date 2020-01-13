package com.esigelec.ping39.View;

import android.graphics.Paint;
import android.os.SystemClock;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


/**
 * Created by jonas on 10.09.16.
 */
class RealtimeScrolling {
    private LineGraphSeries<DataPoint> mSeriesX;
    private LineGraphSeries<DataPoint> mSeriesY;

    private long initTime;

    private final int NB_DATAPOINT_ARCHIVED = 5000;

    public void initGraph(GraphView graph) {
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(60.000);
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
        mSeriesX.appendData(new DataPoint(((double)(SystemClock.uptimeMillis()-initTime))/1000, valX), true, NB_DATAPOINT_ARCHIVED);
        mSeriesY.appendData(new DataPoint(((double)(SystemClock.uptimeMillis()-initTime))/1000, valY), true, NB_DATAPOINT_ARCHIVED);
    }


}
