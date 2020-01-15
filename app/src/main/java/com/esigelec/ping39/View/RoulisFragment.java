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

import com.esigelec.ping39.System.FullTimeGraph;
import com.esigelec.ping39.System.LineGraph;
import com.esigelec.ping39.System.PeriodExtractor;
import com.esigelec.ping39.R;
import com.esigelec.ping39.System.RealtimeScrolling;
import com.jjoe64.graphview.GraphView;


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
                nextTry+=100;
                float roulis = (float)(360/(2*Math.PI)*Math.acos((double)sensorEvent.values[0]/9.8)-90);
                float tangage = (float)(360/(2*Math.PI)*Math.acos((double)sensorEvent.values[1]/9.8)-90);
                mLogicRealTime.AddData(roulis,tangage);
                mLogicPhaseDiagram.AddData(roulis);
                try{
                    mLogicFullTime.AddData(Float.parseFloat(periodExtractor.getPeriodX()),Float.parseFloat(periodExtractor.getPeriodY()));
                }catch(Exception e){
                    e.printStackTrace();
                }
                periodExtractor.addInList(roulis,tangage);
                ((TextView)rootView.findViewById(R.id.infoText)).setText("Périodes: \n" +
                        "Roulis: " + ((float)Math.round(Float.parseFloat(periodExtractor.getPeriodX())*100))/100 + "s, Tangage: " + Math.round(Float.parseFloat(periodExtractor.getPeriodY())*100)/100 +"s");

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_roulis, container, false);

        GraphView graph = rootView.findViewById(R.id.graphDirect);
        mLogicRealTime.initGraph(graph);
        GraphView diagram = rootView.findViewById(R.id.graphPhase);
        mLogicPhaseDiagram.initGraph(diagram);
        GraphView graphPeriod = rootView.findViewById(R.id.graphPeriod);
        mLogicFullTime.initGraph(graphPeriod);
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
