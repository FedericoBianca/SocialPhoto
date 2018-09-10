package com.example.social_photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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


    //--------------------------------------------------------------------------------
    //Method used to retrieve the profile picture of the logged user
    public static Bitmap getFacebookProfilePicture(String userID) throws IOException {
        URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
        Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        return bitmap;


    }
    //--------------------------------------------------------------------------------

    public static void getAlbum(final Context ctx){
        final String[] str = {null};
        Bundle param = new Bundle();
        param.putString("fields","name");
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+SaveSharedPreference.getUserID(ctx)+"/albums",
                param,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {


                        JSONObject ris = response.getJSONObject();
                        try {
                            String album = ris.getString("data");
                            JSONArray arr = new JSONArray(album);
                            for (int i = 0; i < arr.length();i++){
                                str[0] += arr.optString(i)+"";
                            }
                            Log.i("LOGGER","SONO DENTRO LA FUNZIONE GET ALBUM");
                            SaveSharedPreference.setUserAlbums(ctx, str[0]);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
        request.setParameters(param);
        request.executeAndWait();
    }

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

    public static void getFacebookPhoto(String albumID, final ArrayList<String> array, final String location, final String date) {
        final GraphRequest.Callback graphCallback = new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {

                try {
                    JSONObject obj = response.getJSONObject();
                    JSONArray data = obj.getJSONArray("data");
                    if (!location.equals("") && date.equals("Select Date")) {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject currentElement = data.getJSONObject(i);
                            if (currentElement.has("place")) {
                                if (currentElement.getJSONObject("place").has("location")) {
                                    if (currentElement.getJSONObject("place").getJSONObject("location").has("city")) {
                                        String place = currentElement.getJSONObject("place").getJSONObject("location").getString("city");
                                        if (place.equals(location)) {
                                            JSONArray image = currentElement.getJSONArray("images");
                                            JSONObject firstSource = image.getJSONObject(0);
                                            String source = firstSource.getString("source");
                                            array.add(source);
                                        }

                                    }
                                }
                            }
                        }
                    } else if (location.equals("") && !date.equals("Select Date")) {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject currentElement = data.getJSONObject(i);
                            String created = currentElement.getString("created_time");
                            if (created.contains(date)) {
                                JSONArray image = currentElement.getJSONArray("images");
                                JSONObject firstSource = image.getJSONObject(0);
                                String source = firstSource.getString("source");
                                array.add(source);
                            }

                        }
                    }

                    else if(location.equals("") && date.equals("Select Date")){
                        for (int i = 0; i < data.length(); i++){
                            JSONObject currentElement = data.getJSONObject(i);
                            JSONArray image = currentElement.getJSONArray("images");
                            JSONObject firstSource = image.getJSONObject(0);
                            String source = firstSource.getString("source");
                            array.add(source);
                        }
                    }

                    else {

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject currentElement = data.getJSONObject(i);
                            if (currentElement.has("place")) {
                                if (currentElement.getJSONObject("place").has("location")) {
                                    if (currentElement.getJSONObject("place").getJSONObject("location").has("city")) {
                                        String place = currentElement.getJSONObject("place").getJSONObject("location").getString("city");
                                        String created = currentElement.getString("created_time");
                                        if (place.equals(location) && created.contains(date)) {
                                            JSONArray image = currentElement.getJSONArray("images");
                                            JSONObject firstSource = image.getJSONObject(0);
                                            String source = firstSource.getString("source");
                                            array.add(source);
                                        }

                                    }
                                }
                            }
                        }

                    }

                    GraphRequest nextRequest = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
                    if (nextRequest != null) {
                        nextRequest.setCallback(this);
                        nextRequest.executeAndWait();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        Bundle param = new Bundle();
        param.putString("fields","images,place,created_time");
        GraphRequest request2 = new GraphRequest(AccessToken.getCurrentAccessToken(),
                "/"+albumID+"/photos",param, HttpMethod.GET, graphCallback);
        request2.setParameters(param);
        request2.executeAndWait();
    }
    //--------------------------------------------------------------------------------



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





}

