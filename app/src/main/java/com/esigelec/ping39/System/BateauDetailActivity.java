package com.esigelec.ping39.System;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.esigelec.ping39.Model.Bateau;
import com.esigelec.ping39.R;

import android.util.Log;
import android.view.View;
import android.widget.Button;
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
        TextView valFabriquant = findViewById(R.id.valFabriquant);
        TextView valLongueur = findViewById(R.id.valLongueur);
        TextView valLargeur = findViewById(R.id.valLargeur);
        TextView valPoids = findViewById(R.id.valPoids);
        TextView valKg = findViewById(R.id.valKg);
        TextView valVx = findViewById(R.id.valVectorx);
        TextView valVy = findViewById(R.id.valVectory);
        TextView valVz = findViewById(R.id.valVectorz);
        Button btnSelect = findViewById(R.id.btnSelect);
        Button btnFavori = findViewById(R.id.btnFavori);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
             Log.d("bateauDetail", "button Select clicked");
            }
        });
        btnFavori.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                Log.d("bateauDetail", "button Favori clicked");
            }
        });
        // Remplissage des valeurs
        new ImageLoadTask(bat.getImageUrl(), img).execute();
        valNom.setText(bat.getNom());
        valFabriquant.setText(bat.getFabriquant());
        valLongueur.setText(String.valueOf(bat.getLongueur()));
        valLargeur.setText(String.valueOf(bat.getLargeur()));
        valPoids.setText(String.valueOf(bat.getPoids()));
        valKg.setText(String.valueOf(bat.getKg()));
        valVx.setText(String.valueOf(bat.getCentre_gravite().getX()));
        valVy.setText(String.valueOf(bat.getCentre_gravite().getY()));
        valVz.setText(String.valueOf(bat.getCentre_gravite().getZ()));
    }

}
