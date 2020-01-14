package com.esigelec.ping39.Model;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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

    public Bateau() { }

    public static Bateau getBateau(Context context,int id) {
        ArrayList<Bateau> liste = GetAll(context);
        for(int i = 0; i<liste.size();i++){
            if(liste.get(i).getId()==id){
                return liste.get(i);
            }
        }
        return null;
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

    public static int nbBateau(Context context){
        return GetAll(context).size();
    }

    public static ArrayList<Bateau> GetAll(Context context){
        ArrayList<Bateau> listeBat = new ArrayList<Bateau>();
        try{
            AssetManager assetManager = context.getAssets();
            InputStream istream= assetManager.open("liste_bateaux.xml");
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(istream);
            NodeList nList = doc.getElementsByTagName("bateau");
            for(int i =0;i<nList.getLength();i++) {
                if (nList.item(0).getNodeType() == Node.ELEMENT_NODE) {
                    Element elm = (Element) nList.item(i);
                    Bateau bat = new Bateau();
                    bat.setId(Integer.parseInt(getXmlNodeValue("id", elm)));
                    bat.setNom(getXmlNodeValue("nom", elm));
                    bat.setFabriquant(getXmlNodeValue("fabriquant", elm));
                    bat.setImageUrl(getXmlNodeValue("imageUrl", elm));
                    bat.setLongueur(Float.parseFloat(getXmlNodeValue("longueur", elm)));
                    bat.setLargeur(Float.parseFloat(getXmlNodeValue("largeur", elm)));
                    bat.setPoids(Float.parseFloat(getXmlNodeValue("poids", elm)));
                    bat.setKg(Float.parseFloat(getXmlNodeValue("kg", elm)));
                    bat.setCentre_gravite(new Vector3(Float.parseFloat(getXmlNodeValue("Vector3x", elm)),
                        Float.parseFloat(getXmlNodeValue("Vector3y", elm)),
                        Float.parseFloat(getXmlNodeValue("Vector3z", elm))));
                    listeBat.add(bat);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.d("BateauListView","erreur 1");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            Log.d("BateauListView","erreur 2");
        } catch (SAXException e) {
            e.printStackTrace();
            Log.d("BateauListView","erreur 3");
        }
        return listeBat;
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

    protected static String getXmlNodeValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        Node node = nodeList.item(0);
        if(node!=null){
            if(node.hasChildNodes()){
                Node child = node.getFirstChild();
                while (child!=null){
                    if(child.getNodeType() == Node.TEXT_NODE){
                        return  child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }
}
