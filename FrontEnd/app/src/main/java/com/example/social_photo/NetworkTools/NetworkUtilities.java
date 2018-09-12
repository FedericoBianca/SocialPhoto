package com.example.social_photo.NetworkTools;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class NetworkUtilities {
    private static final String LOG_TAG = NetworkUtilities.class.getSimpleName();
    private static final String FACEBOOK_TOKEN = "facebook_token";
    private static final String FACEBOOK_ID = "facebook_id";
    private static final String NODEJS_ADDRESS = "http://192.168.43.18:3000";

    public static String sendToken(String facebookToken, String facebookId) {
        //Build up your query URI
        Uri builtURI = Uri.parse(NODEJS_ADDRESS + "/users").buildUpon()
                .appendQueryParameter(FACEBOOK_TOKEN, facebookToken)
                .appendQueryParameter(FACEBOOK_ID, facebookId)
                .build();

        return sendRequest(builtURI, "POST");
    }
    public static String getDataUser(String facebookToken, String facebookId, String yy, String mm){
        Uri builtURI = Uri.parse(NODEJS_ADDRESS + "/users/myUser").buildUpon()
                .appendQueryParameter(FACEBOOK_TOKEN, facebookToken)
                .appendQueryParameter(FACEBOOK_ID, facebookId)
                .appendQueryParameter("year", yy)
                .appendQueryParameter("month", mm)
                .build();

        return sendRequest(builtURI, "GET");
    }

    public static void sendLogoutToNodejs(String facebookToken, String facebookId){
        Uri builtURI = Uri.parse(NODEJS_ADDRESS + "/users/logOut").buildUpon()
                .appendQueryParameter(FACEBOOK_TOKEN, facebookToken)
                .appendQueryParameter(FACEBOOK_ID, facebookId)
                .build();

        sendRequest(builtURI, "POST");
    }

    private static String sendRequest(Uri builtURI, String method) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JSONresponse = null;

        try {
            URL requestURL = new URL(builtURI.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod(method);
            //urlConnection.setRequestProperty("connection", "close");
            urlConnection.connect();

            Map<String, List<String>> map = urlConnection.getHeaderFields();

            Log.d(LOG_TAG, "Printing Response Header...\n");

            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                Log.d(LOG_TAG, "Key : " + entry.getKey()
                        + " ,Value : " + entry.getValue());
            }


            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {

            /* Since it's JSON, adding a newline isn't necessary (it won't affect
            parsing) but it does make debugging a *lot* easier if you print out the
            completed buffer for debugging. */
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            JSONresponse = buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.d(LOG_TAG, JSONresponse);

        return JSONresponse;
    }
}
