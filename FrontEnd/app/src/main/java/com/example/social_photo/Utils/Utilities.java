package com.example.social_photo.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class Utilities {


    //--------------------------------------------------------------------------------
    //Method used to transform a bitmap picture in a string
    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    //--------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------
    //Method used to retrieve from a string a bitmap picture
    public static Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    //--------------------------------------------------------------------------------

    public static String[] getAlbumsNames(HashMap<String,String> map,Context ctx){
        String[] temp = SaveSharedPreference.getUserAlbums(ctx).replace("null","").split("[\\{\\}]");
        for (int i = 0; i < temp.length;i++){
            String[] app = temp[i].split(",");
            String[] first_string = app[0].split(":");
            String[] second_string = app[app.length-1].split(":");
            map.put(first_string[first_string.length-1].replaceAll("\"",""),second_string[second_string.length-1].replaceAll("\"",""));
        }

        Object[] ris_2 = map.keySet().toArray();
        ris_2[0] = "Select Album";
        return Arrays.asList(ris_2).toArray(new String[ris_2.length]);
    }

    //--------------------------------------------------------------------------------

    public static String getCity(double lat,double lng, Context ctx) throws IOException{

        if(Geocoder.isPresent()){
            List<Address> ris;
            Geocoder geocoder = new Geocoder(ctx, Locale.ENGLISH);
            ris = geocoder.getFromLocation(lat,lng,5);
            if(ris.size()>0)
                return ris.get(ris.size()-1).getLocality();
            }
        return "";
    }

    public static String[] getYears(){
        String[] data = new String[]{"Select Date","2009","2010","2011","2012","2013","2014","2015","2016","2017","2018"};
        return data;
    }

    public static String[] getMonth(){
        String[] data = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
        return data;
    }

}

