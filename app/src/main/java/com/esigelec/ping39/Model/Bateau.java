package com.esigelec.ping39.Model;

public class Bateau {
    private int id;
    private String nom;
    private String fabriquant;
    private String imageUrl;
    private float longueur;
    private float largeur;
    private float poids;
    private boolean favori;
    private float kg;
    private Vector3 centre_gravite;

    public Bateau(int id) {
    }

    public static int nbBateau(){
        return 0;
    }

}
