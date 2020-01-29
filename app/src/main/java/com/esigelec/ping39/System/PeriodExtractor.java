package com.esigelec.ping39.System;

import android.os.SystemClock;
import android.util.Log;

import com.esigelec.ping39.Model.GlobalHolder;

import java.util.ArrayList;
import java.util.LinkedList;

public class PeriodExtractor {
    private LinkedList<Float> valsX = new LinkedList<>();
    private LinkedList<Float> valsY = new LinkedList<>();
    private LinkedList<Long> time = new LinkedList<>();
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

    private float getMoyenne(Float[] tmpArrCp){
        float total = 0;
        for (Float aFloat : tmpArrCp) {
            total += aFloat;
        }
        return total/tmpArrCp.length;
    }
    public float getPeriodX(){
        return getPeriod(valsX);
    }
    public float getPeriodY(){
        Log.d("PeriodExtractor","valsX:"+valsX.size() + ", valsY:"+valsY.size()+", time:"+time.size());
        return getPeriod(valsY);
    }
    private float getPeriod(LinkedList<Float> tab){
        // ----------- Initialisation:
        // Récupération de la source
        Float[] tmpArrCp = tab.toArray(new Float[0]);
        // Calcul de la moyenne
        float moy = getMoyenne(tmpArrCp);
        // Récupération de la premiere intersection à la moyenne
        int firstCross = getNthCross(tmpArrCp,0, moy);
        // Récupération de la de la Nieme(N: paramètre) intersection à la moyenne
        int nthCross = getNthCross(tmpArrCp,GlobalHolder.nbDemiePeriod, moy);
        Log.d("PeriodExtractor","first:");
        float deltaTime = (time.get(firstCross) - time.get(nthCross))/1000;

        if(nthCross!=0)
            return 2*deltaTime/GlobalHolder.nbDemiePeriod;
        else
            return 0;
    }
    private int getNthCross(Float[] tmpArrCp, int n, float moy){
        float lastVal = tmpArrCp[0];
        boolean above = (lastVal>moy);

        if(tmpArrCp.length>=n)
            for (int i=2; i<tmpArrCp.length-1; i++){
                boolean check = true;
                for(int k = 0; k<GlobalHolder.crossingSeuil;k++){
                    if(above){
                        if(tmpArrCp[i - k] > moy) check = false;
                    }else{
                        if(tmpArrCp[i - k] < moy) check = false;
                    }
                }
                if(check){
                    above = !above;
                    n--;
                }
                if(n<0) return i;
            }
        return 0;
    }
    public boolean isLongenough(){
        return longenough;
    }

}
