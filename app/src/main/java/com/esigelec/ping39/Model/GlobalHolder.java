package com.esigelec.ping39.Model;

import java.util.ArrayList;
import java.util.Date;

public class GlobalHolder {
    public static Bateau selected = null;
    public static boolean enregistrer = false;
    public static ArrayList<Entry> entrees = new ArrayList<Entry>();
    public static int nbPointPhaseDiagram = 50; // MAX : 500

    public static void Save(Entry e){
        if (enregistrer)
            entrees.add(e);
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
            timestamp = System.currentTimeMillis()/1000;
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
