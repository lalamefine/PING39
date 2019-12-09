package com.esigelec.ping39;

import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;

public class PeriodExtractor {
    LinkedList<Float> valsX = new LinkedList<Float>();
    LinkedList<Float> valsY = new LinkedList<Float>();
    LinkedList<Long> time = new LinkedList<Long>();
    private final int MAX_SIZE = 200;
    private boolean filled = false;

    void addInList(float x, float y){
        valsX.addFirst(x);
        valsY.addFirst(y);
        time.addFirst(SystemClock.uptimeMillis());
        if(time.size()>MAX_SIZE){
            valsX.removeLast();
            valsY.removeLast();
            time.removeLast();
            if(filled == false){
                for (int i = 0; i < 50; i+=2)
                    Log.d("PeriodExtractor.filled","valsX("+i+")="+valsX.get(i)+"valsX("+i+1+")="+valsX.get(i+1));
            }
            filled = true;
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
        if(filled == false){
            return 0;
        }else {
            float moy = getMoyenne(tab);
            int lastCross = getLastCross(tab, moy);
            float deltaTime = tab.get(lastCross) - time.get(0);
            int crossing = 0;
            boolean above = (tab.get(0) > moy);
            for (int i = 5; i < lastCross; i++) {
                if (above) {
                    if ((tab.get(i) < moy) && (tab.get(i - 1) < moy) && (tab.get(i - 2) < moy))
                        crossing++;
                } else {
                    if ((tab.get(i) > moy) && (tab.get(i - 1) > moy) && (tab.get(i - 2) > moy))
                        crossing++;
                }
            }
            Log.i("PeriodExtractor.get","Moy:"+moy+" ,deltaTime:"+deltaTime+" ,crossing:"+crossing);
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
    public boolean isFilled(){
        return filled;
    }

}
