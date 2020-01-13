package com.esigelec.ping39.View;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
        bateaux = Bateau.GetTestArray();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bats, container, false);
        bateauListView = v.findViewById(R.id.bateauListView);
        bateauListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("BateauListView","Item selected : "+ i);
                //LANCER ACTIVITE ICI
            }
        });

        BatAdapter batAdapter = new BatAdapter(getContext(), bateaux);
        bateauListView.setAdapter(batAdapter);
        return v;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
