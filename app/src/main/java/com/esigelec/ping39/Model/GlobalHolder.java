package com.esigelec.ping39.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

public class GlobalHolder {
    public static Bateau selected = null;
    @SuppressLint("StaticFieldLeak")
    public static Context context; // Très légère fuite mémoire
    public static boolean enregistrer = false;
    public static ArrayList<Entry> entrees = new ArrayList<>();
    public static float lineDrawWidth = 3;

    // PARAMETRES MODIFIABLES:
    public static int nbPointPhaseDiagram = 50; // MAX : 1000
    public static int nbDemiePeriod = 8;
    public static int crossingSeuil = 3;
    public static int tailleHistoriqueXY = 1000;
    public static float ajustagePeriode = 1;

    public static void Save(Entry e){
        if (enregistrer)
            entrees.add(e);
    }

    public static Bateau getSelected(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("bateau_info", Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("sel",-1);
        selected = Bateau.getBateau(context,id);
        Log.d("GlobalHolder", "GetSelected: " + (selected != null ? selected.getId() : 0));
        return selected;
    }

    public static void SaveParams() {
        SharedPreferences sh = context.getSharedPreferences("Paramètres", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sh.edit();
        e.putInt("nbPointPhaseDiagram",nbPointPhaseDiagram);
        e.putInt("nbDemiePeriod",nbDemiePeriod);
        e.putInt("crossingSeuil",crossingSeuil);
        e.putInt("tailleHistoriqueXY",tailleHistoriqueXY);
        e.putFloat("ajustagePeriode",ajustagePeriode);
        e.apply();
    }

    public static void LoadParams() {
        SharedPreferences sh = context.getSharedPreferences("Paramètres", Context.MODE_PRIVATE);
        nbPointPhaseDiagram = sh.getInt("nbPointPhaseDiagram",50); // MAX : 1000
        nbDemiePeriod = sh.getInt("nbDemiePeriod",6);
        crossingSeuil = sh.getInt("crossingSeuil",3);
        tailleHistoriqueXY = sh.getInt("tailleHistoriqueXY",5000);
        ajustagePeriode = sh.getFloat("ajustagePeriode",1);
    }
    public static class Entry{
        private long timestamp;
        private float rawX;
        private float rawY;
        private float degX;
        private float degY;
        private float perX;
        private float perY;

        public Entry(){}
        public Entry(float rawX, float rawY, float degX, float degY, float perX, float perY) {
            timestamp = System.currentTimeMillis();
            this.rawX = rawX;
            this.rawY = rawY;
            this.degX = degX;
            this.degY = degY;
            this.perX = perX;
            this.perY = perY;
        }

        public String toRow(){
            return ""+timestamp+";"+rawX+";"+rawY+
                    ";"+degX+";"+degY+
                    ";"+perX+";"+perY;
        }

        public void setRaw(float rawX, float rawY){
            this.rawX = rawX;
            this.rawY = rawY;
        }
        public void setDeg(float degX, float degY){
            this.degX = degX;
            this.degY = degY;
        }
        public void setPer(float perX, float perY){
            this.perX = perX;
            this.perY = perY;
        }
    }
}
