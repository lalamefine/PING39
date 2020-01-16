package com.esigelec.ping39.View;

import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.esigelec.ping39.Model.Bateau;
import com.esigelec.ping39.Model.SortByFavori;
import com.esigelec.ping39.R;
import com.esigelec.ping39.System.BatAdapter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


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
    private ArrayList<Bateau> bateaux;
    private ViewGroup viewGroupContainer;
    private boolean view_refreshed = false;
    // Required empty public constructor
    public BatsFragment() {}

    public static BatsFragment newInstance() {
        BatsFragment fragment = new BatsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewStateRestored(Bundle bundle) {
        super.onViewStateRestored(bundle);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(view_refreshed == false){
            view_refreshed = true;
            getFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroupContainer,
            Bundle savedInstanceState) {
        this.viewGroupContainer = viewGroupContainer;
        bateaux = Bateau.GetAll(getContext());bateaux = Bateau.GetAll(getContext());
        Collections.sort(bateaux, new Comparator<Bateau>() {
            @Override
            public int compare(Bateau b1, Bateau b2) {
                if (b1.isFavori() && !b2.isFavori())
                    return -1;
                if (!b1.isFavori() && b2.isFavori())
                    return 1;
                return 0;
            }
        });

        View v = getLayoutInflater().inflate(R.layout.fragment_bats, viewGroupContainer, false);
        bateauListView = v.findViewById(R.id.bateauListView);
        final BatAdapter batAdapter = new BatAdapter(getContext(), bateaux);
        bateauListView.setAdapter(batAdapter);
        bateauListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view_refreshed = false;
                Log.d("BateauListView","Item selected -> i:"+ i + " l:"+l);
                Intent batIntent = new Intent(getActivity(), BateauDetailActivity.class);
                batIntent.putExtra("idBateau",l);
                startActivity(batIntent);
            }
        });
        return v;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
