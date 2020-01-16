package com.esigelec.ping39.View;

import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.esigelec.ping39.Model.Bateau;
import com.esigelec.ping39.Model.GlobalHolder;
import com.esigelec.ping39.R;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(view -> finish());
        mToolbar.setNavigationIcon(R.drawable.back_arrow);
        mToolbar.setTitle("Settings");

        Button bArreter = findViewById(R.id.bArreter);
        Button bDemarrer = findViewById(R.id.bDemarrer);
        Button bPartager = findViewById(R.id.bPartager);

        bArreter.setOnClickListener(v -> {
            GlobalHolder.enregistrer = false;
            bArreter.setEnabled(false);
            bDemarrer.setEnabled(true);
            bPartager.setEnabled(GlobalHolder.entrees.size()>0);
        });
        bDemarrer.setOnClickListener(v -> {
            GlobalHolder.enregistrer = true;
            bArreter.setEnabled(true);
            bDemarrer.setEnabled(false);
            bPartager.setEnabled(false);
        });
        bPartager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                send();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Button bArreter = findViewById(R.id.bArreter);
        Button bDemarrer = findViewById(R.id.bDemarrer);
        Button bPartager = findViewById(R.id.bPartager);
        bArreter.setEnabled(GlobalHolder.enregistrer);
        bDemarrer.setEnabled(!GlobalHolder.enregistrer);
    }

    public void save() {
        ArrayList<GlobalHolder.Entry> datas = GlobalHolder.entrees;
        if (datas != null) {
            try {
                FileWriter fw = new FileWriter("/data/data/com.esigelec.ping39/shipSensorData.csv");
                //写入数据并换行
                Log.d("save#Fichier", "writting data");
                fw.write("Timestamp;acceleration X;acceleration Y;degre X;degre Y;periode X;periode Y" + "\r\n");
                for (int i = 0; i < datas.size(); i++) {
                    //Log.d("save#Fichier", "writting data: "+datas.get(i).toRow());
                    fw.write(datas.get(i).toRow() + "\r\n");
                }
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("save#Fichier", "****Save Error****");
            }
        }
    }

    public void send(){
        File filePath = new File(getFilesDir().getParentFile().getParentFile().getParentFile().getParentFile().getPath(), "data/com.esigelec.ping39");
        File file = new File(filePath, "shipSensorData.csv");
        Log.d("send#Fichier", "from path: "+filePath.getAbsolutePath());
        Log.d("send#Fichier", "from path: "+file.getAbsolutePath());

        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".share", file);

        Intent intent = ShareCompat.IntentBuilder.from(this)
                .setType("application/txt")
                .setStream(uri)
                .setChooserTitle("Choix du moyen de partage")
                .createChooserIntent()
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }
}
