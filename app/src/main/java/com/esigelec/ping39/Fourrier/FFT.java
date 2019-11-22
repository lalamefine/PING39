package com.esigelec.ping39.Fourrier;
/*
Copyright (C) 2011  Kircher Engineering, LLC (http://www.kircherEngineering.com)

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

import android.util.Log;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * A class that performs the Fast Fourier Transform.
 * @author Kaleb Kircher
 *
 */
public class FFT
{
    public double dtEchantillonage;
    public double[] amplitude;
    public double[] phase;
    public double[] cosSignal;
    public double[] degreesSignal;
    public double[] IMCSum;
    public double[] radiansSignal;
    public double[] RECSum;
    public double[][] IMC;
    public double[][] REC;
    public int harmonics;
    public static FFT calculate(double[] signal, int harmonics,double dtEchantillonage)
    {
        FFT result = new FFT();
        // instantiate classes to perform calculations
        CalcReal calcReal = new CalcReal();
        CalcImag calcImag = new CalcImag();
        SumCoefficiants sumC = new SumCoefficiants();

        // size of table
        int N = signal.length;
        // samples per cycle
        int NN = signal.length;
        // harmonics
        int K = harmonics;
        result.harmonics = harmonics;
        // initialize arrays
        result.dtEchantillonage = dtEchantillonage*0.294;
        result.amplitude = new double[K];
        result.phase = new double[K];
        result.cosSignal = new double[N];
        result.degreesSignal = new double[N];
        result.IMCSum = new double[K];
        result.radiansSignal = new double[N];
        result.RECSum = new double[K];
        result.IMC = new double[K][NN];
        result.REC = new double[K][NN];

        // generate the radians for the signal
        for (int i = 0; i < N; i++)
        {
            result.degreesSignal[i] = (360 / (double) N) * i;
            //System.out.println(degreesSignal[i]);
            result.radiansSignal[i] = result.degreesSignal[i] * (Math.PI / 180);
        }

        // get the real coefficients
        for (int i = 0; i < K; i++)
        {
            result.REC[i] = calcReal.calc(result.radiansSignal, signal, i + 1);
        }

        // sum the real coefficients
        for (int i = 0; i < K; i++)
        {
            result.RECSum[i] = (sumC.sum(result.REC[i], (double) NN));
            // System.out.println(RECSum[i]);
        }

        // get the imag coefficients
        for (int i = 0; i < K; i++)
        {
            result.IMC[i] = calcImag.calc(result.radiansSignal, signal, i + 1);
        }

        // sum the imag coefficients
        for (int i = 0; i < K; i++)
        {
            result.IMCSum[i] = (sumC.sum(result.IMC[i], (double) NN));
            // System.out.println(IMCSum[i]);
        }

        // calculate amplitude (Fourier coefficients)
        // a[i] = 2(abs(RECSum[i]*abs(IMCSum[i])
        for (int i = 0; i < K; i++)
        {
            result.amplitude[i] = 2 * (Math.sqrt(Math.pow(result.RECSum[i], 2)
                    + Math.pow(result.IMCSum[i], 2)));
            // System.out.println(amplitude[i]);
        }

        // calculate phase
        // atan(abs(b/a))
        for (int i = 0; i < K; i++)
        {
            double temp = (Math.sqrt(Math.pow(result.IMCSum[i], 2)
                    / Math.pow(result.RECSum[i], 2)));
            result.phase[i] = Math.atan(temp);
            // System.out.println(phase[i]);
        }

        // produce output
        //Log.d("FFT Calculation: " ," RESULT : ");
        /*for (int i = 0; i < K; i++)
        {
            Log.d("FFT Calculation: " ,"RE" + (i + 1) + ": " + result.RECSum[i]);
            Log.d("FFT Calculation: " ,"IM" + (i + 1) + ": " + result.IMCSum[i]);
            Log.d("FFT Calculation: " ,"Amplitude(K=" + (i + 1) + "): " + result.amplitude[i]);
            Log.d("FFT Calculation: " ,"Phase(K=" + (i + 1) + "): " + result.phase[i]);
            Log.d("FFT Calculation: " ,"Degrees(K=" + (i + 1) + "): " + result.phase[i]*(180/Math.PI));
        }*/
        return result;
    }

    public String toString(){
        return "FFT(dt="+dtEchantillonage+
                "\n;Amplidude="+amplitude[0]+
                "\n;Phase="+phase[0]+
                "\n;CosSignal="+cosSignal[0]+
                "\n;Degree="+degreesSignal[0]+
                "\n;SumRE="+RECSum[0]+
                "\n;SumIM="+IMCSum[0]+
                "\n;RE="+REC[0][0]+
                "\n;IM="+IMC[0][0]+")";
    }
    public String ampString(){
        String s = "";
        for(int i = 0; i<harmonics; i++)
            s += "p:"+1/(i*dtEchantillonage*0.001f)+"=> "+(int)(amplitude[i]*100)+"\n";
        return s;
    }

}