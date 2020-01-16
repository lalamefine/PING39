package com.esigelec.ping39.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.esigelec.ping39.Model.Bateau;
import com.esigelec.ping39.Model.GlobalHolder;
import com.esigelec.ping39.R;
import com.esigelec.ping39.System.ImageLoadTask;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

public class BateauDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bateau_detail);
        //Paramètrage toolbar

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(view -> finish());
        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        Bundle extras = getIntent().getExtras();
        int id = (int)extras.getLong("idBateau");
        final Bateau bat = Bateau.getBateau(getBaseContext(),id);
        // Lien avec la vue
        ImageView img = findViewById(R.id.imageView);
        TextView valNom = findViewById(R.id.valNom);
        TextView valLongueur = findViewById(R.id.valLongueur);
        TextView valLargeur = findViewById(R.id.valLargeur);
        TextView valKg = findViewById(R.id.valKg);
        TextView valDeplacementNominal = findViewById(R.id.valDeplacementNominal);
        TextView valInertie = findViewById(R.id.valInertie);
        TextView valGmMini = findViewById(R.id.valGmMini);
        TextView valBassinAttraction = findViewById(R.id.valBassinAttraction);
        TextView valAngleChavirement = findViewById(R.id.valAngleChavirement);

        Button btnSelect = findViewById(R.id.btnSelect);
        final Button btnFavori = findViewById(R.id.btnFavori);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            GlobalHolder.selected = bat.getId();
            Log.d("bateauDetail", "button Select clicked");
            }
        });
        btnFavori.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            SharedPreferences sharedPreferences = getSharedPreferences("bateau_info", Context.MODE_PRIVATE);
            //获取操作SharedPreferences实例的编辑器（必须通过此种方式添加数据）
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //添加数
            if(sharedPreferences.getBoolean("fav"+bat.getId(),false)){
                editor.putBoolean("fav"+bat.getId(), false);
                btnFavori.setText("Ajouter aux favori");
            }else{
                editor.putBoolean("fav"+bat.getId(), true);
                btnFavori.setText("Supprimer du favori");
            }
            //提交
            editor.commit();
            }

        });
        // Remplissage des valeurs
        new ImageLoadTask(bat.getImageUrl(), img).execute();
        valNom.setText(bat.getNom());
        mToolbar.setTitle(bat.getNom());
        valLongueur.setText(bat.getLongueur() + " m");
        valLargeur.setText(bat.getLargeur() + " m");
        valKg.setText(bat.getKg() + " m");
        valDeplacementNominal.setText(String.valueOf(bat.getDeplacementNominal()));
        valInertie.setText(bat.getInertie() + " t.m²");
        valGmMini.setText(String.valueOf(bat.getGmMini()));
        valBassinAttraction.setText(String.valueOf(bat.getBassinAttraction()));
        valAngleChavirement.setText(bat.getAngleChavirement() +" °");

        SharedPreferences sharedPreferences = getSharedPreferences("bateau_info", Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("fav"+bat.getId(),false)){
            btnFavori.setText("Supprimer du favori");
        }else{
            btnFavori.setText("Ajouter aux favori");
        }
    }

}
