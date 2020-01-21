package com.esigelec.ping39.System;

import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;

import com.esigelec.ping39.Model.GlobalHolder;
import com.esigelec.ping39.Model.Vector2;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.LinkedList;

public class LineGraph {
    private LineGraphSeries<DataPoint>[] mSeriesX = new LineGraphSeries[1000];
    private LinkedList<Float> valList;
    private LinkedList<Long> initTime;
    private LinkedList<LineGraphSeries<DataPoint>> series = new LinkedList<LineGraphSeries<DataPoint>>();
    private LinkedList<LineGraphSeries<DataPoint>> envellope = new LinkedList<LineGraphSeries<DataPoint>>();
    private GraphView graph = null;

    public void initGraph(GraphView graph) {
        this.graph = graph;
        valList = new LinkedList<Float>();
        initTime = new LinkedList<Long>();
        series = new LinkedList<LineGraphSeries<DataPoint>>();
        envellope = new LinkedList<LineGraphSeries<DataPoint>>();
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinX(-110);
        graph.getViewport().setMaxX(110);
        graph.getViewport().setMinY(-60);
        graph.getViewport().setMaxY(60);
        graph.getGridLabelRenderer().setLabelVerticalWidth(50);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Vitesse angulaire en degrés/s");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Angle en degrés");

    }

    public void drawEnveloppe(){
        if(GlobalHolder.selected != null && graph != null){

            Paint csPaint = new Paint();
            csPaint.setStrokeWidth(GlobalHolder.lineDrawWidth);
            csPaint.setColor(0xff880000);
            while(envellope.size()>0){
                graph.removeSeries(envellope.getFirst());
            }
            ArrayList<Vector2> pointList = GlobalHolder.selected.getBassinAttraction();
            int k;
            for(k=1;k<pointList.size();k++){
                LineGraphSeries<DataPoint> temp = new LineGraphSeries<DataPoint>();
                temp.setDrawDataPoints(false);
                temp.setDrawBackground(false);
                temp.setCustomPaint(csPaint);
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
                graph.addSeries(temp);
                envellope.add(temp);
            }
            Log.d("LineGraph","Enveloppe tracée ("+k+" points)");
        }else Log.d("LineGraph","Enveloppe non tracée");
    }

    public void AddData(float val) {

        Paint csPaint = new Paint();
        csPaint.setStrokeWidth(GlobalHolder.lineDrawWidth);
        csPaint.setColor(0xff073C66);

        valList.addLast(val);
        initTime.addLast(SystemClock.uptimeMillis());
        if (valList.size()> GlobalHolder.nbPointPhaseDiagram ){
            valList.removeFirst();
            initTime.removeFirst();
        }
        int i = valList.size()-1;
        int ARRONDI = 5;
        if(i>ARRONDI){
            LineGraphSeries<DataPoint> temp = new LineGraphSeries<DataPoint>();
            temp.setDrawDataPoints(false);
            temp.setDrawBackground(false);
            temp.setCustomPaint(csPaint);
            double x1 = X(i,ARRONDI);
            double x2 = X(i-1,ARRONDI);
            double y1 = Y(i,ARRONDI);
            double y2 = Y(i-1,ARRONDI);
            if(x1<x2){
                temp.appendData(new DataPoint(x1,y1),false,2);
                temp.appendData(new DataPoint(x2,y2),false,2);
            }else{
                temp.appendData(new DataPoint(x2,y2),false,2);
                temp.appendData(new DataPoint(x1,y1),false,2);
            }
            graph.addSeries(temp);
            series.addLast(temp);
        }
        if(series.size()> GlobalHolder.nbPointPhaseDiagram){
            graph.removeSeries(series.getFirst());
            series.removeFirst();
        }
    }
    private double Y(int i,int arrondi){
        return (valList.get(i)-valList.get(i-arrondi))/((double)(initTime.get(i)-initTime.get(i-arrondi))/1000);
    }
    private double X(int i,int arrondi){
        float tempX = 0;
        for(int k=i;k>i-arrondi;k--) {
            tempX += valList.get(i);
        }
        return tempX/arrondi;
    }

}
