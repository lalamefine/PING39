package com.esigelec.ping39.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esigelec.ping39.Model.Bateau;
import com.esigelec.ping39.Model.GlobalHolder;
import com.esigelec.ping39.System.FullTimeGraph;
import com.esigelec.ping39.System.GMGraph;
import com.esigelec.ping39.System.LineGraph;
import com.esigelec.ping39.System.PeriodExtractor;
import com.esigelec.ping39.R;
import com.esigelec.ping39.System.RealtimeScrolling;
import com.jjoe64.graphview.GraphView;

import static java.lang.Math.PI;
import static java.lang.Math.acos;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RoulisFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RoulisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class RoulisFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private SensorManager sensorManager;
    private RealtimeScrolling mLogicRealTime;
    private LineGraph mLogicPhaseDiagram;
    private FullTimeGraph mLogicFullTime;
    private GMGraph mLogicGMGraph;
    private Sensor sensorGrav;
    long nextTry;
    private PeriodExtractor periodExtractor;
    @SuppressLint("StaticFieldLeak")
    public static View rootView; // Très légère fuite mémoire

    public RoulisFragment() {
        Log.d("RoulisFragment","Constructeur");
        periodExtractor = new PeriodExtractor();
        nextTry = SystemClock.uptimeMillis()+50;
    }


    public static RoulisFragment newInstance() {
        RoulisFragment fragment = new RoulisFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("RoulisFragment","OnCreate");
        super.onCreate(savedInstanceState);
        sensorGrav = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        onAttach(this.getContext());
        mLogicRealTime = new RealtimeScrolling();
        mLogicPhaseDiagram = new LineGraph();
        mLogicFullTime = new FullTimeGraph();
        mLogicGMGraph = new GMGraph();
        GlobalHolder.context = this.getContext();
        GlobalHolder.getSelected();

        //EVENEMENTS SUR L'ACCELEROMETTRE
        final SensorEventListener gravEventListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(SystemClock.uptimeMillis() > nextTry){
                    nextTry+=50;
                    //CALCULS
                    float roulis = (float)(360/(2*PI)*acos(sensorEvent.values[0]/9.8)-90);
                    float tangage = (float)(360/(2*PI)*acos(sensorEvent.values[1]/9.8)-90);
                    //Calcul de la période
                    periodExtractor.addInList(roulis,tangage);
                    //Calcul du gm
                    Bateau bat = GlobalHolder.selected;
                    float gm = 0;
                    if(bat != null){
                        gm = (float)(bat.getInertie()/(bat.getDeplacementNominal()*9.81));
                        gm = gm*(2*(float)Math.PI/periodExtractor.getPeriodX()*GlobalHolder.ajustagePeriode)*(2*(float)Math.PI/periodExtractor.getPeriodX()*GlobalHolder.ajustagePeriode);
                    }
                    gm = (float) (Math.round(gm*100))/100;
                    //AFFICHAGE
                    //Envoi aux graphs
                    mLogicRealTime.AddData(roulis,tangage);
                    mLogicPhaseDiagram.AddData(roulis);
                    mLogicFullTime.AddData(periodExtractor.getPeriodX()*GlobalHolder.ajustagePeriode,periodExtractor.getPeriodY()*GlobalHolder.ajustagePeriode);
                    mLogicGMGraph.AddData(gm);
                    //Remplissage TextView
                    if(bat != null) {
                        ((TextView) rootView.findViewById(R.id.infoText)).setText(String.format(
                                "Bateau sélectionné: %s\nPériodes de Roulis: %ss \nPériodes de Tangage: %ss \nGM : %s",
                                bat.getNom(),
                                (float) Math.round(periodExtractor.getPeriodX() * GlobalHolder.ajustagePeriode * 10) / 10,
                                (float) Math.round(periodExtractor.getPeriodY() * GlobalHolder.ajustagePeriode * 10) / 10,
                                gm)
                        );
                    }else{
                        ((TextView) rootView.findViewById(R.id.infoText)).setText(String.format(
                                "Aucun bateau sélectionné \nPériodes de Roulis: %ss \nPériodes de Tangage: %ss \n",
                                (float) Math.round(periodExtractor.getPeriodX() * GlobalHolder.ajustagePeriode * 10) / 10,
                                (float) Math.round(periodExtractor.getPeriodY() * GlobalHolder.ajustagePeriode * 10) / 10)
                        );
                    }
                    //ENREGISTREMENT
                    GlobalHolder.Save(new GlobalHolder.Entry(
                            sensorEvent.values[0],
                            sensorEvent.values[1],
                            roulis,
                            tangage,
                            periodExtractor.getPeriodX()*GlobalHolder.ajustagePeriode,
                            periodExtractor.getPeriodY()*GlobalHolder.ajustagePeriode));
                }
            }
        };
        sensorManager.registerListener(gravEventListener, sensorGrav, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("RoulisFragment","OnCreateView");
        View rootView = inflater.inflate(R.layout.fragment_roulis, container, false);
        GraphView graph = rootView.findViewById(R.id.graphDirect);
        mLogicRealTime.initGraph(graph);
        GraphView diagram = rootView.findViewById(R.id.graphPhase);
        mLogicPhaseDiagram.initGraph(diagram);
        mLogicPhaseDiagram.drawEnveloppe();
        GraphView graphPeriod = rootView.findViewById(R.id.graphPeriod);
        mLogicFullTime.initGraph(graphPeriod);
        GraphView graphGM = rootView.findViewById(R.id.graphDirectGM);
        mLogicGMGraph.initGraph(graphGM);
        RoulisFragment.rootView = rootView;
        return rootView;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        Log.d("RoulisFragment","OnAttach");
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //sensorManager.unregisterListener(gravEventListener, sensorGrav);
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
