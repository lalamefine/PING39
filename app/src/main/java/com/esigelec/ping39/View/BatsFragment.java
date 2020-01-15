package com.esigelec.ping39.View;

import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
        initialisation();

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialisation();
        send();
    }
    public void initialisation(){
        bateaux = Bateau.GetAll(getContext());
        save(bateaux);
        Log.d("BateauDetail", "bolean bat 1 " + bateaux.get(0).isFavori());
        Log.d("BateauDetail", "bolean bat 2 " + bateaux.get(1).isFavori());
        Log.d("BateauDetail", "bolean bat 3 " + bateaux.get(2).isFavori());
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
                Intent batIntent = new Intent(getActivity(), BateauDetailActivity.class);
                batIntent.putExtra("idBateau",i);
                startActivity(batIntent);
            }
        });

        BatAdapter batAdapter = new BatAdapter(getContext(), bateaux);
        bateauListView.setAdapter(batAdapter);
        return v;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void save(List<Bateau> datas) {
        if (datas != null) {
            try {
                FileWriter fw = new FileWriter("/data/data/com.esigelec.ping39/saveBateaux.txt");
                //写入数据并换行
                for (int i = 0; i < datas.size(); i++) {
                    Log.d("saveFichier", "data: "+datas.get(i).getNom());
                    fw.write(datas.get(i).getNom() + "\r\n");
                }
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("saveFichier", "****Save Error****");
            }
        }
    }
    public void send(){
        File filePath = new File(getContext().getFilesDir().getParentFile().getParentFile().getParentFile().getParentFile().getPath(), "data/com.esigelec.ping39");
        File file = new File(filePath, "saveBateaux.txt");
        Log.d("save", "path: "+filePath.getAbsolutePath());
        Log.d("save", "path: "+file.getAbsolutePath());

        Uri uri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".share", file);

        Intent intent = ShareCompat.IntentBuilder.from(getActivity())
                .setType("application/txt")
                .setStream(uri)
                .setChooserTitle("Choose bar")
                .createChooserIntent()
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        getContext().startActivity(intent);

//        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//        sharingIntent.setType("text/*");
//        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://" + file.getAbsolutePath()));
//        startActivity(Intent.createChooser(sharingIntent, "share file with"));
    }
}
