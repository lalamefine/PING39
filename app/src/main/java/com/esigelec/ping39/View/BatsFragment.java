package com.esigelec.ping39.View;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.esigelec.ping39.Model.Bateau;
import com.esigelec.ping39.R;
import com.esigelec.ping39.System.BatAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BatsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BatsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ListView bateauListView;
    private Bateau[] bateaux;
    // Required empty public constructor
    public BatsFragment() {}

    public static BatsFragment newInstance() {
        BatsFragment fragment = new BatsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bateaux = Bateau.GetAll();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //bateauListView = (ListView) findViewById(R.id.bateauListView);
        BatAdapter batAdapter = new BatAdapter(getContext(), bateaux);
        bateauListView.setAdapter(batAdapter);
        return inflater.inflate(R.layout.fragment_bats, container, false);
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
