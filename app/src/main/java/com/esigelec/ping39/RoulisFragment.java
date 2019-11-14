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
    private Sensor sensorGrav;
    private Sensor sensorGyro;
    private float[] gravValues;
    //private float[] gyroValues;

    //EVENEMENTS SUR L'ACCELEROMETTRE
    final SensorEventListener gravEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        public void onSensorChanged(SensorEvent sensorEvent) {
            gravValues = sensorEvent.values;
            Log.d("Sensors", "Gravité (z,x,y) : " + gravValues[0] + "," + gravValues[1] + "," + gravValues[2]);
        }
    };
    //EVENEMENTS SUR LE GYROSCOPE
    //final SensorEventListener gyroEventListener = new SensorEventListener() {
    //    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    //    public void onSensorChanged(SensorEvent sensorEvent) {
    //        gyroValues = sensorEvent.values;
    //        Log.d("Sensors", "Rotation sur l'axe z : " + gyroValues[0]);
    //        Log.d("Sensors", "Rotation sur l'axe x : " + gyroValues[1]);
    //        Log.d("Sensors", "Rotation sur l'axe y : " + gyroValues[2]);
    //    }
    //};

    public RoulisFragment() {}

    private void refreshView(){
        float[] values = new float[3];
        float[] R = new float[9];

    }

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_roulis, container, false);
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
            //sensorManager.registerListener(gyroEventListener, sensorGyro, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        sensorManager.unregisterListener(gravEventListener, sensorGrav);
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
