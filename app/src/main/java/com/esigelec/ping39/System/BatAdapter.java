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

public class BatAdapter extends BaseAdapter {
    Context context;
    Bateau[] batList;
    LayoutInflater inflter;

    public BatAdapter(Context context, Bateau[] batList) {
        this.context = context;
        this.batList = batList;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return batList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.bateau_list_element, null);
        TextView name = (TextView) view.findViewById(R.id.textView);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        name.setText(batList[i].getNom());
        new ImageLoadTask(batList[i].getImageUrl(), icon).execute();
        return view;
    }
}
