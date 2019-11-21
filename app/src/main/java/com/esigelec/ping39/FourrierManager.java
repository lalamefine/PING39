package com.esigelec.ping39;

import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import com.esigelec.ping39.Fourrier.FFT;

import java.util.Arrays;

public class FourrierManager {


    private final int NB_DATAPOINT_ARCHIVED = 100; //dt moyens 140 ms donc 500 points == 70secondes
    private double lastCall;
    private double[] arrX = new double[NB_DATAPOINT_ARCHIVED];
    private double[] arrY = new double[NB_DATAPOINT_ARCHIVED];

    public FourrierManager(){
        lastCall = SystemClock.uptimeMillis();
        for(int i = 0; i<NB_DATAPOINT_ARCHIVED;i++){
            arrX[i] =0;
            arrY[i] =0;
        }
    }
    public void FourrierCalc(){
        //Log.d("FOURRIER: X->", FFT.calculate(arrX,1,SystemClock.uptimeMillis()-lastCall).toString());
        //Log.d("FOURRIER: Y->", Arrays.toString(FFT.calculate(arrY,1).amplitude));
        ((TextView)RoulisFragment.rootView.findViewById(R.id.infoText)).setText(FFT.calculate(arrX,20,SystemClock.uptimeMillis()-lastCall).ampString());
        lastCall = SystemClock.uptimeMillis();
    }

    public void arrXInsert( double value){
        for(int i = NB_DATAPOINT_ARCHIVED-1; i>0;i--){
            arrX[i] = arrX[i-1];
        }
        arrX[0] = value;
    }
    public void arrYInsert( double value){
        for(int i = NB_DATAPOINT_ARCHIVED-1; i>0;i--){
            arrY[i] = arrY[i-1];
        }
        arrY[0] = value;
    }
}
