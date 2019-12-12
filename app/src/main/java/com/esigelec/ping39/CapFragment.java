package com.esigelec.ping39;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import static android.content.Context.SENSOR_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CapFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ImageView imageView;

    private RealtimeScrolling mLogicRealTime;
    private SensorManager mSensorManager;
    private SensorEventListener mSensorEventListener;
    private ChaosCompassView chaosCompassView;
    private ChaosCompassView chaosCompassView2;
    private float val;
    private float valButton;

    private Button button;

    public CapFragment() {
        // Required empty public constructor
    }

    public static CapFragment newInstance() {
        CapFragment fragment = new CapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        * */
        mSensorManager = (SensorManager)getContext().getSystemService(SENSOR_SERVICE);


        mSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                val = event.values[0];
                chaosCompassView.setVal(val);
                chaosCompassView2.setVal(val);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        mSensorManager.registerListener(mSensorEventListener,mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
        //onAttach(this.getContext());
        mLogicRealTime = new RealtimeScrolling();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cap, container, false);
        chaosCompassView = (ChaosCompassView) rootView.findViewById(R.id.ccv);
        chaosCompassView2 = (ChaosCompassView) rootView.findViewById(R.id.ccv2);
        button =  rootView.findViewById(R.id.buttonCap);
        button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    switch (v.getId()) {
                        case R.id.buttonCap:
                            Log.d("buttonCap", "buttonCap pressed");
                            Log.d("buttonCap", String.valueOf(val));
                            valButton = val;
                            break;
                        default:
                            Log.d("buttonTag", "default onclick");
                            break;
                    }
            }});
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
            mSensorManager = (SensorManager)context.getSystemService(SENSOR_SERVICE);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        void onFragmentInteraction(Uri uri);
    }
}
