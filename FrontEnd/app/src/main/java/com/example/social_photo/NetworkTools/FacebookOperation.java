package com.example.social_photo.NetworkTools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.social_photo.Utils.SaveSharedPreference;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class FacebookOperation {
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
                "/"+ SaveSharedPreference.getUserID(ctx)+"/albums",
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


    //--------------------------------------------------------------------------------

    public static void getFacebookPhoto(String albumID, final ArrayList<String> array, final String location, final String date) {
        final GraphRequest.Callback graphCallback = new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {

                if (!response.toString().equals("")) {
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
                        } else if (location.equals("") && date.equals("Select Date")) {
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject currentElement = data.getJSONObject(i);
                                JSONArray image = currentElement.getJSONArray("images");
                                JSONObject firstSource = image.getJSONObject(0);
                                String source = firstSource.getString("source");
                                array.add(source);
                            }
                        } else {

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
            }
        };

        Bundle param = new Bundle();
        param.putString("fields","images,place,created_time");
        GraphRequest request2 = new GraphRequest(AccessToken.getCurrentAccessToken(),
                "/"+albumID+"/photos",param, HttpMethod.GET, graphCallback);
        request2.setParameters(param);
        request2.executeAndWait();
    }
}
