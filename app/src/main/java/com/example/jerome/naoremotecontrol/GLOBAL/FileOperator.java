package com.example.jerome.naoremotecontrol.GLOBAL;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by jerome on 20/10/16.
 */

public class FileOperator {

    public static boolean createDirectory(String path) {

        File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + path);
        boolean succes = true;
        if (!myDir.exists())
            succes = myDir.mkdir();

        return succes;
    }

    public static boolean writeFile(String path, String file, String text) {

        text += "\n";

        FileOutputStream output = null;
        File myFile = new File(Environment.getExternalStorageDirectory() + File.separator + path + File.separator, file);
        try {
            output = new FileOutputStream(myFile, true);
            output.write(text.getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false ;
        } catch (IOException e) {
            e.printStackTrace();
            return false ;
        }

        return true ;

    }

    public static boolean fileContain(String path, String file, String ligne){
        boolean result = false;

        File myFile = new File(Environment.getExternalStorageDirectory() + File.separator + path + File.separator, file);

        //lecture du fichier texte
        try{

            InputStream ips=new FileInputStream(myFile);
            InputStreamReader ipsr=new InputStreamReader(ips);
            BufferedReader br=new BufferedReader(ipsr);

            String l;
            while ((l=br.readLine())!=null){
                if(ligne.equals(l))
                    result = true ;
            }
            br.close();
        }
        catch (Exception e){

        }
        return result;
    }

    public static String[] getLignes(String path, String file){
        String[] lignes = new String[getNbLignes(path, file)] ;
        int i=0 ;

        File myFile = new File(Environment.getExternalStorageDirectory() + File.separator + path + File.separator, file);

        //lecture du fichier texte
        try{
            InputStream ips=new FileInputStream(myFile);
            InputStreamReader ipsr=new InputStreamReader(ips);
            BufferedReader br=new BufferedReader(ipsr);

            String l;
            while ((l=br.readLine())!=null){
                lignes[i] = l;
                i++;
            }
            br.close();
        }
        catch (Exception e){

        }

        return lignes ;
    }

    public static int getNbLignes(String path, String file){
        int nb = 0 ;

        File myFile = new File(Environment.getExternalStorageDirectory() + File.separator + path + File.separator, file);

        //lecture du fichier texte
        try{
            InputStream ips=new FileInputStream(myFile);
            InputStreamReader ipsr=new InputStreamReader(ips);
            BufferedReader br=new BufferedReader(ipsr);

            while ((br.readLine())!=null){
                nb++;
            }
            br.close();
        }
        catch (Exception e){

        }

        return nb ;

    }

    public static boolean removeLigne(String path, String file, String ligne){
        File myFile = new File(Environment.getExternalStorageDirectory() + File.separator + path + File.separator, file);
        ArrayList<String> list = new ArrayList<String>();

        try{
            InputStream ips=new FileInputStream(myFile);
            InputStreamReader ipsr=new InputStreamReader(ips);
            BufferedReader br=new BufferedReader(ipsr);

            String l ;
            while ((l=br.readLine())!=null){
                if(!l.equals(ligne))
                    list.add(l + "\n");
            }
            br.close();
        }
        catch (Exception e){

        }

        myFile.delete();

        FileOutputStream output = null;
        try {
            output = new FileOutputStream(myFile, true);
            for(int i=0 ; i<list.size() ; i++)
                output.write(list.get(i).getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false ;
        } catch (IOException e) {
            e.printStackTrace();
            return false ;
        }

        return true ;

    }

}