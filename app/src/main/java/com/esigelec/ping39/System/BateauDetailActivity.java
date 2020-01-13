package com.esigelec.ping39.System;

import android.app.Activity;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.esigelec.ping39.R;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class BateauDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bateau_detail);
        try{
            ArrayList<HashMap<String, String>> userList = new ArrayList<>();
            ListView lv = (ListView) findViewById(R.id.bat_liste);
            //InputStream istream = getAssets().open("liste_bateaux.xml");
            AssetManager assetManager = getAssets();
            InputStream istream= assetManager.open("liste_bateaux.xml");
           // File fXmlFile = getResources.getXml("res/xml/liste_bateaux.xml");


            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(istream);
            NodeList nList = doc.getElementsByTagName("bateau");
            Log.d("BateauListView","Item nList : "+ nList);

            for(int i =0;i<nList.getLength();i++){
                if(nList.item(0).getNodeType() == Node.ELEMENT_NODE){
                    HashMap<String,String> user = new HashMap<>();
                    Element elm = (Element)nList.item(i);
                    user.put("id", getNodeValue("id",elm));
                    user.put("nom", getNodeValue("nom",elm));
                    user.put("fabriquant", getNodeValue("fabriquant",elm));
                    user.put("imageUrl", getNodeValue("imageUrl",elm));
                    user.put("longueur", getNodeValue("longueur",elm));
                    user.put("largeur", getNodeValue("largeur",elm));
                    user.put("poids", getNodeValue("poids",elm));
                    user.put("favori", getNodeValue("favori",elm));
                    user.put("kg", getNodeValue("kg",elm));
                    user.put("vector3x", getNodeValue("vector3x",elm));
                    user.put("vector3y", getNodeValue("vector3y",elm));
                    user.put("vector3z", getNodeValue("vector3z",elm));
                    userList.add(user);
                    Log.d("BateauListView","Bateau detail : "+ userList);
                }
            }
            ListAdapter adapter = new SimpleAdapter(BateauDetailActivity.this, userList, R.layout.liste_row,new String[]{"id","nom","fabriquant", "imageUrl", "longueur", "largeur", "poids", "favori", "kg", "vector3x", "vector3y", "vector3z"}, new int[]{R.id.idBat, R.id.nomBat, R.id.fabriquantBat, R.id.imageUrlBat, R.id.longueurBat, R.id.largeurBat, R.id.poidsBat, R.id.favoriBat, R.id.kgBat, R.id.vector3xBat, R.id.vector3yBat, R.id.vector3zBat});
            lv.setAdapter(adapter);
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
    }

    protected String getNodeValue(String tag, Element element) {
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
