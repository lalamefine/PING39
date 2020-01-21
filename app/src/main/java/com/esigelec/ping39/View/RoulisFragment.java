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
    public static View rootView;

    public RoulisFragment() {
        periodExtractor = new PeriodExtractor();
        nextTry = SystemClock.uptimeMillis()+100;
    }

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
                    gm = gm*(2*(float)Math.PI/periodExtractor.getPeriodX())*(2*(float)Math.PI/periodExtractor.getPeriodX());
                }
                gm = (float) (Math.round(gm*100))/100;
                //AFFICHAGE
                //Envoi aux graphs
                mLogicRealTime.AddData(roulis,tangage);
                mLogicPhaseDiagram.AddData(roulis);
                mLogicFullTime.AddData(periodExtractor.getPeriodX(),periodExtractor.getPeriodY());
                try{
                    mLogicGMGraph.AddData(gm);
                }catch(Exception e){
                    e.printStackTrace();
                }
                //Remplissage TextView
                if(bat != null) {
                    ((TextView) rootView.findViewById(R.id.infoText)).setText("Bateau sélectionné: " + bat.getNom() + "\n" +
                        "Périodes de Roulis: " + ((float)Math.round(periodExtractor.getPeriodX() * 10)) / 10 + "s \n" +
                        "Périodes de Tangage: " + ((float)Math.round(periodExtractor.getPeriodY() * 10)) / 10 + "s \n" +
                        "GM : " + gm
                    );
                }else{
                    ((TextView) rootView.findViewById(R.id.infoText)).setText("Aucun bateau sélectionné \n" +
                        "Périodes de Roulis: " + ((float)Math.round(periodExtractor.getPeriodX() * 10)) / 10 + "s \n" +
                        "Périodes de Tangage: " + ((float)Math.round(periodExtractor.getPeriodY() * 10)) / 10 + "s \n"
                    );
                }
                //ENREGISTREMENT
                GlobalHolder.Save(new GlobalHolder.Entry(
                        sensorEvent.values[0],
                        sensorEvent.values[1],
                        roulis,
                        tangage,
                        periodExtractor.getPeriodX(),
                        periodExtractor.getPeriodY()));
            }
        }
    };


    public static RoulisFragment newInstance() {
        RoulisFragment fragment = new RoulisFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorGrav = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        onAttach(this.getContext());
        mLogicRealTime = new RealtimeScrolling();
        mLogicPhaseDiagram = new LineGraph();
        mLogicFullTime = new FullTimeGraph();
        mLogicGMGraph = new GMGraph();
        GlobalHolder.context = this.getContext();
        GlobalHolder.getSelected();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
            sensorManager.registerListener(gravEventListener, sensorGrav, SensorManager.SENSOR_DELAY_NORMAL);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
