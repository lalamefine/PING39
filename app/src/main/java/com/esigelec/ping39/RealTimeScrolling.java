package com.esigelec.ping39;

import android.graphics.Paint;
import android.os.SystemClock;
import com.esigelec.ping39.Fourrier.FFT;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


/**
 * Created by jonas on 10.09.16.
 */
class RealtimeScrolling {
    private final int NB_DATAPOINT_ARCHIVED = 500;
    private LineGraphSeries<DataPoint> mSeriesX;
    private LineGraphSeries<DataPoint> mSeriesY;
    private double[] arrX = new double[NB_DATAPOINT_ARCHIVED];
    private double[] arrY = new double[NB_DATAPOINT_ARCHIVED];

    private long initTime;

    public RealtimeScrolling(){
        for(int i = 0; i<NB_DATAPOINT_ARCHIVED;i++){
            arrX[i] =0;
            arrY[i] =0;
        }
    }

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
        mSeriesX.appendData(new DataPoint((SystemClock.uptimeMillis()-initTime), valX), true, NB_DATAPOINT_ARCHIVED);
        mSeriesY.appendData(new DataPoint((SystemClock.uptimeMillis()-initTime), valY), true, NB_DATAPOINT_ARCHIVED);
        arrayInsert(arrX,valX);
        arrayInsert(arrY,valY);
        if(arrX[NB_DATAPOINT_ARCHIVED-1] != 0)
            FourrierCalc();
    }

    public void FourrierCalc(){

        FFT FoX = FFT.calculate(arrX,2);
        FFT FoY = FFT.calculate(arrY,2);
    }

    public void arrayInsert(double[] ar, double value){
        for(int i = NB_DATAPOINT_ARCHIVED-1; i>0;i--){
            ar[i] = ar[i-1];
        }
        ar[0] = value;
    }

}
