package NetworkTools;

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

    public static String sendToken(String facebookToken) {
        //Build up your query URI
        Uri builtURI = Uri.parse("http://192.168.1.219:3000/tasks").buildUpon()
                .appendQueryParameter(FACEBOOK_TOKEN, facebookToken)
                .build();

        return sendRequest(builtURI, "POST");
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
