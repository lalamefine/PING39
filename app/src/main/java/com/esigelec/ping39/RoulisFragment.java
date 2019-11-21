package com.esigelec.ping39;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private Sensor sensorGrav;
    private Sensor sensorGyro;
    private FourrierManager fftManager;
    public static View rootView;
    public RoulisFragment() {
        fftManager = new FourrierManager();
    }

    //EVENEMENTS SUR L'ACCELEROMETTRE
    final SensorEventListener gravEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        public void onSensorChanged(SensorEvent sensorEvent) {
            mLogicRealTime.AddData(sensorEvent.values[0],sensorEvent.values[1]);
            fftManager.arrXInsert(sensorEvent.values[0]);
            fftManager.arrYInsert(sensorEvent.values[1]);
            fftManager.FourrierCalc();
            //Log.d("Sensors", "Gravité (z,x,y) : " + gravValues[0] + "," + gravValues[1] + "," + gravValues[2]);
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
        sensorGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        onAttach(this.getContext());
        mLogicRealTime = new RealtimeScrolling();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_roulis, container, false);
        GraphView graph = rootView.findViewById(R.id.graph2);
        mLogicRealTime.initGraph(graph);
        RoulisFragment.rootView = rootView;
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
