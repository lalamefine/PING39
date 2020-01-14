package com.esigelec.ping39.System;

import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.LinkedList;

public class LineGraph {
    private final int nbDataPoint = 50;
    private LineGraphSeries<DataPoint>[] mSeriesX = new LineGraphSeries[500];
    LinkedList<Float> valList;
    LinkedList<Long> initTime;
    LinkedList<LineGraphSeries> series;
    private Paint csPaint = new Paint();
    private GraphView graph;

    public void initGraph(GraphView graph) {
        this.graph = graph;
        valList = new LinkedList<Float>();
        initTime = new LinkedList<Long>();
        series = new LinkedList<LineGraphSeries>();
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinX(-45);
        graph.getViewport().setMaxX(45);
        graph.getViewport().setMinY(-90);
        graph.getViewport().setMaxY(90);
        graph.getGridLabelRenderer().setLabelVerticalWidth(50);

        csPaint.setStrokeWidth(5);
        csPaint.setColor(0xff073C66);
        csPaint.setColor(0xff880000);

    }

    public void AddData(float val) {
        valList.addLast(val);
        initTime.addLast(SystemClock.uptimeMillis());
        if (valList.size()> nbDataPoint ){
            valList.removeFirst();
            initTime.removeFirst();
        }

        int i = valList.size()-1;
        if(i>5){
            LineGraphSeries temp = new LineGraphSeries();
            temp.setDrawDataPoints(false);
            temp.setDrawBackground(false);
            int arrondi = 2;
            double x1 = valList.get(i);
            double x2 = valList.get(i-1);
            double y1 = (valList.get(i)-valList.get(i-1))/((double)(initTime.get(i)-initTime.get(i-1))/1000);
            double y2 = (valList.get(i-1)-valList.get(i-2))/((double)(initTime.get(i-1)-initTime.get(i-2))/1000);
            if(valList.get(i)<valList.get(i-1)){
                temp.appendData(new DataPoint(x1,y1),false,2);
                temp.appendData(new DataPoint(x2,y2),false,2);
            }else{
                temp.appendData(new DataPoint(x2,y2),false,2);
                temp.appendData(new DataPoint(x1,y1),false,2);
            }
            graph.addSeries(temp);
            series.add(temp);
        }
        if(series.size()>nbDataPoint){
            graph.removeSeries(series.getFirst());
            series.removeFirst();
        }
    }

    private long ArL(LinkedList<Long> list, int i, int rounder){
        long var = 0;
        for(int k = i; k>i-rounder; i--){
            var +=list.get(k);
        }
        return var/rounder;
    }
    private float ArF(LinkedList<Float> list, int i, int rounder){
        long var = 0;
        for(int k = i; k>i-rounder; i--){
            var +=list.get(k);
        }
        return var/rounder;
    }
}
