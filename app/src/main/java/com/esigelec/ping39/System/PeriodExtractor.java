package com.esigelec.ping39.System;

import android.os.SystemClock;
import android.util.Log;

import com.esigelec.ping39.Model.GlobalHolder;

import java.util.ArrayList;
import java.util.LinkedList;

public class PeriodExtractor {
    LinkedList<Float> valsX = new LinkedList<Float>();
    LinkedList<Float> valsY = new LinkedList<Float>();
    LinkedList<Long> time = new LinkedList<Long>();
    private final int MIN_SIZE = 200;
    private boolean longenough = false;

    public void addInList(float x, float y){
        valsX.addFirst(x);
        valsY.addFirst(y);
        time.addFirst(SystemClock.uptimeMillis());
        while(time.size()>GlobalHolder.tailleHistoriqueXY){
            valsX.removeLast();
            valsY.removeLast();
            time.removeLast();
        }
        if(time.size()>MIN_SIZE && !longenough) {
            longenough = true;
        }
    }

    private float getMoyenne(Float tmpArrCp[]){
        float total = 0;
        for(int i = 0; i<tmpArrCp.length; i++){
            total += tmpArrCp[i];
        }
        return total/tmpArrCp.length;
    }
    public float getPeriodX(){
        return getPeriod(valsX);
    }
    public float getPeriodY(){
        //Log.d("PeriodExtractorDebug","valsX:"+valsX.size() + ", valsY:"+valsY.size()+", time:"+time.size());
        return getPeriod(valsY);
    }
    private float getPeriod(LinkedList<Float> tab){
        Float tmpArrCp[] = tab.toArray(new Float[tab.size()]);
        float moy = getMoyenne(tmpArrCp);
        int firstCross = getFirstCross(tmpArrCp, moy);
        int nthCross = getNthCross(tmpArrCp,GlobalHolder.nbDemiePeriod, moy);
        float deltaTime = (time.get(firstCross) - time.get(nthCross))/1000;
        if(nthCross!=0)
            return 2*deltaTime/GlobalHolder.nbDemiePeriod;
        else{
            int crossing = 0;
            boolean above = (tab.get(firstCross) > moy);
            for (int i = firstCross; i < nthCross; i++) {
                if (above) {
                    if ((tmpArrCp[i] < moy) && (tmpArrCp[i - 1] < moy) && (tmpArrCp[i - 2] < moy)){
                        crossing++;
                        above = !above;
                    }
                } else {
                    if ((tmpArrCp[i] > moy) && (tmpArrCp[i - 1] > moy) && (tmpArrCp[i - 2] > moy)){
                        crossing++;
                        above = !above;
                    }
                }
            }
            if(crossing>=GlobalHolder.crossingSeuil)
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
        float lastVal = tmpArrCp[tab.size()-1);
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
    private int getFirstCross(Float tmpArrCp[],float moy){
        float lastVal = tmpArrCp[0];
        boolean above = (lastVal>moy) ;
        if(tmpArrCp.length>=3)
            for (int i=2; i<tmpArrCp.length-1; i++){
                if(above){
                    if((tmpArrCp[i] < moy) && (tmpArrCp[i - 1] < moy) && (tmpArrCp[i - 2] < moy)) return i;
                }else{
                    if((tmpArrCp[i] > moy) && (tmpArrCp[i - 1] > moy) && (tmpArrCp[i - 2] > moy)) return i;
                }
            }
        return 0;
    }
    private int getNthCross(Float tmpArrCp[],int n, float moy){
        float lastVal = tmpArrCp[0];
        boolean above = (lastVal>moy) ;
        for (int i=2; i<tmpArrCp.length-1; i++){
            if(above){
                if((tmpArrCp[i]<moy) && (tmpArrCp[i - 1] < moy) && (tmpArrCp[i - 2] < moy)){
                    above = false;
                    n--;
                    if(n<0){
                        return i;
                    }
                }
            }else{
                if((tmpArrCp[i]>moy) && (tmpArrCp[i - 1] < moy) && (tmpArrCp[i - 2] < moy)){
                    above = true;
                    n--;
                    if(n<0){
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
