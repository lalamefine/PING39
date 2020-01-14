package com.esigelec.ping39.System;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.esigelec.ping39.Model.Bateau;
import com.esigelec.ping39.R;

import android.widget.ImageView;
import android.widget.TextView;

public class BateauDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bateau_detail);
        Bundle extras = getIntent().getExtras();
        int id = extras.getInt("idBateau");
        Bateau bat = Bateau.getBateau(getBaseContext(),id);
        // Lien avec la vue
        ImageView img = findViewById(R.id.imageView);
        TextView valNom = findViewById(R.id.valNom);
        // Remplissage des valeurs
        new ImageLoadTask(bat.getImageUrl(), img).execute();
        valNom.setText(bat.getNom());
    }

}
