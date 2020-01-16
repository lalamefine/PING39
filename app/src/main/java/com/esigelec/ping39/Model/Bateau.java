package com.esigelec.ping39.Model;

import android.content.Context;
import android.content.SharedPreferences;
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



public class Bateau{
    private int id;
    private String nom;
    private String imageUrl;
    private float longueur;
    private float largeur;
    private boolean favori;
    private float kg;

    private float deplacementNominal;
    private float inertie;
    private float gmMini;
    private float bassinAttraction;
    private float angleChavirement;

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

    public Bateau(int id, String nom, String imageUrl, float longueur, float largeur, boolean favori, float kg, float deplacementNominal, float inertie, float gmMini, float bassinAttraction, float angleChavirement) {
        this.id = id;
        this.nom = nom;
        this.imageUrl = imageUrl;
        this.longueur = longueur;
        this.largeur = largeur;
        this.favori = favori;
        this.kg = kg;
        this.deplacementNominal = deplacementNominal;
        this.inertie = inertie;
        this.gmMini = gmMini;
        this.bassinAttraction = bassinAttraction;
        this.angleChavirement = angleChavirement;
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
                    bat.setImageUrl(getXmlNodeValue("imageUrl", elm));
                    bat.setLongueur(Float.parseFloat(getXmlNodeValue("longueur", elm)));
                    bat.setLargeur(Float.parseFloat(getXmlNodeValue("largeur", elm)));
                    bat.setKg(Float.parseFloat(getXmlNodeValue("kg", elm)));
                    bat.setDeplacementNominal(Float.parseFloat(getXmlNodeValue("deplacementNominal", elm)));
                    bat.setInertie(Float.parseFloat(getXmlNodeValue("inertie", elm)));
                    bat.setGmMini(Float.parseFloat(getXmlNodeValue("gmMini", elm)));
                    bat.setBassinAttraction(Float.parseFloat(getXmlNodeValue("bassinAttraction", elm)));
                    bat.setAngleChavirement(Float.parseFloat(getXmlNodeValue("angleChavirement", elm)));

                    SharedPreferences sharedPreferences = context.getSharedPreferences("bateau_info", Context.MODE_PRIVATE);
                    if(sharedPreferences.contains("fav"+bat.getId()))
                        bat.setFavori(sharedPreferences.getBoolean("fav"+bat.getId(), true));
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

    public float getDeplacementNominal() {
        return deplacementNominal;
    }

    public void setDeplacementNominal(float deplacementNominal) {
        this.deplacementNominal = deplacementNominal;
    }

    public float getInertie() {
        return inertie;
    }

    public void setInertie(float inertie) {
        this.inertie = inertie;
    }

    public float getGmMini() {
        return gmMini;
    }

    public void setGmMini(float gmMini) {
        this.gmMini = gmMini;
    }

    public float getBassinAttraction() {
        return bassinAttraction;
    }

    public void setBassinAttraction(float bassinAttraction) {
        this.bassinAttraction = bassinAttraction;
    }

    public float getAngleChavirement() {
        return angleChavirement;
    }

    public void setAngleChavirement(float angleChavirement) {
        this.angleChavirement = angleChavirement;
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

    private static String getXmlNodeValue(String tag, Element element) {
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
