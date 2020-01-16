package com.esigelec.ping39.System;

import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;

public class PeriodExtractor {
    LinkedList<Float> valsX = new LinkedList<Float>();
    LinkedList<Float> valsY = new LinkedList<Float>();
    LinkedList<Long> time = new LinkedList<Long>();
    private final int MAX_SIZE = 1000;
    private final int MIN_SIZE = 200;
    private boolean longenough = false;

    public void addInList(float x, float y){
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
        return getPeriod(valsX,6);
    }
    public float getPeriodY(){
        return getPeriod(valsY,6);
    }
    private float getPeriod(LinkedList<Float> tab,final int N_PERIOD){
        float moy = getMoyenne(tab);
        int firstCross = getFirstCross(tab, moy);
        int nthCross = getNthCross(tab,N_PERIOD, moy);
        float deltaTime = (time.get(firstCross) - time.get(nthCross))/1000;
        if(nthCross!=0)
            return 2*deltaTime/N_PERIOD;
        else{
            int crossing = 0;
            boolean above = (tab.get(firstCross) > moy);
            for (int i = firstCross; i < nthCross; i++) {
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
            if(crossing>3)
                try {
                    return Math.round((-2*deltaTime)*100)/100;
                }catch(Exception e){
                    return 0;
                }
            else
                return 0;
        }
    }

    /*private int getLastCross(LinkedList<Float> tab,float moy){
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
    }*/
    private int getFirstCross(LinkedList<Float> tab,float moy){
        float lastVal = tab.get(0);
        boolean above = (lastVal>moy) ;
        if(tab.size()>=3)
            for (int i=2; i<tab.size()-1; i++){
                if(above){
                    if((tab.get(i) < moy) && (tab.get(i - 1) < moy) && (tab.get(i - 2) < moy)) return i;
                }else{
                    if((tab.get(i) > moy) && (tab.get(i - 1) > moy) && (tab.get(i - 2) > moy)) return i;
                }
            }
        return 0;
    }
    private int getNthCross(LinkedList<Float> tab,int n, float moy){
        float lastVal = tab.get(0);
        boolean above = (lastVal>moy) ;
        for (int i=2; i<tab.size()-1; i++){
            if(above){
                if((tab.get(i)<moy) && (tab.get(i - 1) < moy) && (tab.get(i - 2) < moy)){
                    above = false;
                    n--;
                    if(n<=0){
                        return i;
                    }
                }
            }else{
                if((tab.get(i)>moy) && (tab.get(i - 1) < moy) && (tab.get(i - 2) < moy)){
                    above = true;
                    n--;
                    if(n<=0){
                        return i;
                    }
                }
            }
        }
        return 0;
    }
    public boolean isLongenough(){
        return longenough;
    }

}
