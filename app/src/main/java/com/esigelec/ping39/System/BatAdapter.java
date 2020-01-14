package com.esigelec.ping39.System;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.esigelec.ping39.Model.Bateau;
import com.esigelec.ping39.R;

import java.util.ArrayList;

public class BatAdapter extends BaseAdapter {
    Context context;
    ArrayList<Bateau> batList;
    LayoutInflater inflter;

    public BatAdapter(Context context, ArrayList<Bateau> batList) {
        this.context = context;
        this.batList = batList;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return batList.size();
    }

    @Override
    public Bateau getItem(int position) {
        return batList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return batList.get(position).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.bateau_list_element, null);
        TextView name = view.findViewById(R.id.batName);
        TextView descr = view.findViewById(R.id.batDescr);
        ImageView icon = view.findViewById(R.id.batIcon);
        ImageView fav = view.findViewById(R.id.favView);
        name.setText(batList.get(i).getNom());
        descr.setText("Description");
        new ImageLoadTask(batList.get(i).getImageUrl(), icon).execute();
        fav.setAlpha(batList.get(i).isFavori()?1f:0f);
        return view;
    }
}
