package com.esigelec.ping39.System;

import android.graphics.Paint;
import android.os.SystemClock;

import com.esigelec.ping39.Model.GlobalHolder;
import com.esigelec.ping39.Model.Vector2;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.LinkedList;

public class LineGraph {
    private LineGraphSeries<DataPoint>[] mSeriesX = new LineGraphSeries[500];
    private LinkedList<Float> valList;
    private LinkedList<Long> initTime;
    private LinkedList<LineGraphSeries<DataPoint>> series;
    private LinkedList<LineGraphSeries<DataPoint>> envellope;
    private Paint csPaint = new Paint();
    private GraphView graph;

    public void initGraph(GraphView graph) {
        this.graph = graph;
        valList = new LinkedList<Float>();
        initTime = new LinkedList<Long>();
        series = new LinkedList<LineGraphSeries<DataPoint>>();
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinX(-45);
        graph.getViewport().setMaxX(45);
        graph.getViewport().setMinY(-90);
        graph.getViewport().setMaxY(90);
        graph.getGridLabelRenderer().setLabelVerticalWidth(50);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Angle en degrés");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Vitesse angulaire en degrés/s");

        csPaint.setStrokeWidth(5);
        csPaint.setColor(0xff880000);
    }

    public void drawEnvellope(){
        if(GlobalHolder.selected != null){
            while(envellope.size()>0){
                graph.removeSeries(envellope.getFirst());
            }
            ArrayList<Vector2> pointList = GlobalHolder.selected.getBassinAttraction();
            LineGraphSeries<DataPoint> temp = new LineGraphSeries<DataPoint>();
            temp.setDrawDataPoints(false);
            temp.setDrawBackground(false);
            for(int k=1;k<pointList.size();k--){
                double x1 = pointList.get(k-1).getX();
                double x2 = pointList.get(k).getX();
                double y1 = pointList.get(k-1).getY();
                double y2 = pointList.get(k).getY();
                if(x1<x2){
                    temp.appendData(new DataPoint(x1,y1),false,2);
                    temp.appendData(new DataPoint(x2,y2),false,2);
                }else{
                    temp.appendData(new DataPoint(x2,y2),false,2);
                    temp.appendData(new DataPoint(x1,y1),false,2);
                }
            }
            graph.addSeries(temp);
            envellope.add(temp);
        }
    }

    public void AddData(float val) {
        valList.addLast(val);
        initTime.addLast(SystemClock.uptimeMillis());
        if (valList.size()> GlobalHolder.nbPointPhaseDiagram ){
            valList.removeFirst();
            initTime.removeFirst();
        }
        int i = valList.size()-1;
        if(i>5){
            LineGraphSeries<DataPoint> temp = new LineGraphSeries<DataPoint>();
            temp.setDrawDataPoints(false);
            temp.setDrawBackground(false);
            int arrondi = 3;
            double x1 = 0;
            double x2 = 0;
            double y1 = 0;
            double y2 = 0;
            for(int k=i;k>i-arrondi;k--){
                x1 += X(k) / arrondi;
                x2 += X(k-1) / arrondi;
                y1 += Y(k) / arrondi;
                y2 += Y(k-1) / arrondi;
            }
            if(x1<x2){
                temp.appendData(new DataPoint(x1,y1),false,2);
                temp.appendData(new DataPoint(x2,y2),false,2);
            }else{
                temp.appendData(new DataPoint(x2,y2),false,2);
                temp.appendData(new DataPoint(x1,y1),false,2);
            }
            graph.addSeries(temp);
            series.add(temp);
        }
        if(series.size()> GlobalHolder.nbPointPhaseDiagram){
            graph.removeSeries(series.getFirst());
            series.removeFirst();
        }
    }
    private double Y(int i){
        return (valList.get(i)-valList.get(i-1))/((double)(initTime.get(i)-initTime.get(i-1))/1000);
    }
    private double X(int i){
        return valList.get(i);
    }

}
