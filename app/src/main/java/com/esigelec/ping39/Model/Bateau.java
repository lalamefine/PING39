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

    public static Bateau[] GetAll(){
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getFabriquant() {
        return fabriquant;
    }

    public void setFabriquant(String fabriquant) {
        this.fabriquant = fabriquant;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getLongueur() {
        return longueur;
    }

    public void setLongueur(float longueur) {
        this.longueur = longueur;
    }

    public float getLargeur() {
        return largeur;
    }

    public void setLargeur(float largeur) {
        this.largeur = largeur;
    }

    public float getPoids() {
        return poids;
    }

    public void setPoids(float poids) {
        this.poids = poids;
    }

    public boolean isFavori() {
        return favori;
    }

    public void setFavori(boolean favori) {
        this.favori = favori;
    }

    public float getKg() {
        return kg;
    }

    public void setKg(float kg) {
        this.kg = kg;
    }

    public Vector3 getCentre_gravite() {
        return centre_gravite;
    }

    public void setCentre_gravite(Vector3 centre_gravite) {
        this.centre_gravite = centre_gravite;
    }
}
