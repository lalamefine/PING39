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

    public Bateau(int id, String nom, String fabriquant, String imageUrl, float longueur, float largeur, float poids, boolean favori, float kg, Vector3 centre_gravite) {
        this.id = id;
        this.nom = nom;
        this.fabriquant = fabriquant;
        this.imageUrl = imageUrl;
        this.longueur = longueur;
        this.largeur = largeur;
        this.poids = poids;
        this.favori = favori;
        this.kg = kg;
        this.centre_gravite = centre_gravite;
    }

    public Bateau(String nom, String imageUrl) {
        this.nom = nom;
        this.imageUrl = imageUrl;
    }

    public static int nbBateau(){
        return 0;
    }

    public static Bateau[] GetAll(){
        return null;
    }

    public static Bateau[] GetTestArray(){
        Bateau[] ar = {new Bateau("Atlantis","https://www.armada.org/template/img/bateau/8fc58b38200cffe45da439c4acb8e9cad1395b71.jpg"),
                new Bateau("Atyla","https://www.armada.org/template/img/bateau/bcda14e1691416e683dbd8a7374aa1381fdc2722.jpg"),
                new Bateau("Cuauhtémoc","https://www.armada.org/template/img/bateau/a5c8cdb6584aa58445f6648a2d68fc160f16950d.jpg")};
        return ar;
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
