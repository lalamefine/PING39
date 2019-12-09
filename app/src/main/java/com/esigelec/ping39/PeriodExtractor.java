package com.esigelec.ping39;

import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;

public class PeriodExtractor {
    LinkedList<Float> valsX = new LinkedList<Float>();
    LinkedList<Float> valsY = new LinkedList<Float>();
    LinkedList<Long> time = new LinkedList<Long>();
    private final int MAX_SIZE = 500;
    private final int MIN_SIZE = 200;
    private boolean longenough = false;

    void addInList(float x, float y){
        valsX.addFirst(x);
        valsY.addFirst(y);
        time.addFirst(SystemClock.uptimeMillis());
        if(time.size()>MAX_SIZE){
            valsX.removeLast();
            valsY.removeLast();
            time.removeLast();
        }
        if(time.size()>MIN_SIZE) {
            longenough = true;
        }
    }

    private float getMoyenne(LinkedList<Float> tab){
        float total = 0;
        for (Float val:tab) total += val;
        return total/tab.size();
    }
    public float getPeriodX(){
        return getPeriod(valsX);
    }
    public float getPeriodY(){
        return getPeriod(valsY);
    }
    private float getPeriod(LinkedList<Float> tab){
        if(longenough == false){
            return 0;
        }else {
            float moy = getMoyenne(tab);
            int firstCross = getFirstCross(tab, moy);
            int lastCross = getLastCross(tab, moy);
            float deltaTime = (time.get(firstCross) - time.get(lastCross))/1000;
            int crossing = 0;
            boolean above = (tab.get(firstCross) > moy);
            for (int i = firstCross; i < lastCross; i++) {
                if (above) {
                    if ((tab.get(i) < moy) && (tab.get(i - 1) < moy) && (tab.get(i - 2) < moy)){
                        crossing++;
                        above = !above;
                    }
                } else {
                    if ((tab.get(i) > moy) && (tab.get(i - 1) > moy) && (tab.get(i - 2) > moy)){
                        crossing++;
                        above = !above;
                    }
                }
            }
            //Log.d("PeriodExtractor.get","Moy:"+moy+" ,deltaTime:"+deltaTime+" ,crossing:"+crossing);
            return deltaTime * 2 / crossing;
        }
    }

    private int getLastCross(LinkedList<Float> tab,float moy){
        float lastVal = tab.get(tab.size()-1);
        boolean above = (lastVal>moy) ;
        for (int i=tab.size()-1; i>=0; i--){
            if(above){
                if(tab.get(i)<moy) return i;
            }else{
                if(tab.get(i)>moy) return i;
            }
        }
        return 0;
    }
    private int getFirstCross(LinkedList<Float> tab,float moy){
        float lastVal = tab.get(0);
        boolean above = (lastVal>moy) ;
        for (int i=0; i<tab.size()-1; i++){
            if(above){
                if(tab.get(i)<moy) return i;
            }else{
                if(tab.get(i)>moy) return i;
            }
        }
        return 0;
    }
    public boolean isLongenough(){
        return longenough;
    }

}
