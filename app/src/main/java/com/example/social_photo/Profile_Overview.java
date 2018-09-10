package com.example.social_photo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Overview extends AppCompatActivity {
    private TextView textView;
    private Spinner spinner;
    private Spinner spinner2;
    private CircleImageView circleImageView;
    private HashMap<String,String> map = new HashMap<>();
    public static final String ALBUM = "com.example.social_photo.ALBUM";
    public static final String LOCATION = "com.example.social_photo.LOCATION";
    public static final String DATE = "com.example.social_photo.DATE";

    private PlaceAutocompleteFragment autocompleteFragment;
    protected String plc="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //----------------------------------------------------------------------------------------------

        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_profile_overview);

        textView = findViewById(R.id.textView);
        textView.setText(SaveSharedPreference.getUserName(Profile_Overview.this));

        circleImageView = findViewById(R.id.profile_image);
        circleImageView.setImageBitmap(Utilities.StringToBitMap(SaveSharedPreference.getProfilePicture(Profile_Overview.this)));

        spinner = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);

        //----------------------------------------------------------------------------------------------

         autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.findRidePlaceAutocompleteFragment);

        /*
         * The following code example shows setting an AutocompleteFilter on a PlaceAutocompleteFragment to
         * set a filter returning only results with a precise address.
         */
        autocompleteFragment.setHint("Search Place...");

        final AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                double lat = place.getLatLng().latitude;
                double lng = place.getLatLng().longitude;
                try {
                    plc = Utilities.getCity(lat,lng,Profile_Overview.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                }

                @Override
            public void onError(Status status) {

            }

        });

        //----------------------------------------------------------------------------------------------

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null){
                    SharedPreferences.Editor editor = SaveSharedPreference.getSharedPreferences(Profile_Overview.this).edit();

                    editor.clear();
                    editor.apply();


                    Intent intent1 = new Intent(Profile_Overview.this, Login.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent1);

                }
            }
        };
        accessTokenTracker.startTracking();

        //----------------------------------------------------------------------------------------------


        if(SaveSharedPreference.getUserAlbums(Profile_Overview.this) == ""){
            Utilities.getAlbum(Profile_Overview.this);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Utilities.getAlbumsNames(map,Profile_Overview.this));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Utilities.getYears());
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);




        }
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        textView.setText(SaveSharedPreference.getUserName(this));
        circleImageView.setImageBitmap(Utilities.StringToBitMap(SaveSharedPreference.getProfilePicture(this)));
        autocompleteFragment.setHint("Search Place...");

    }

    @Override
    protected void onPause() {
        super.onPause();
        plc="";
        autocompleteFragment.setHint("Search Place...");
    }

    //----------------------------------------------------------------------------------------------





    //----------------------------------------------------------------------------------------------


    public void sendParams(View view){
        if(getInternetState()) {
            if (spinner.getSelectedItem().toString().equals("Select Album")) {
                Toast.makeText(Profile_Overview.this, "You have to select one album!", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(Profile_Overview.this, DisplayPhotos.class);
                intent.putExtra(ALBUM, map.get(spinner.getSelectedItem().toString()));
                intent.putExtra(LOCATION, plc);
                intent.putExtra(DATE, spinner2.getSelectedItem().toString());

                startActivity(intent);

            }
        }
        else{
            Toast.makeText(Profile_Overview.this, "You have to connect to Internet!", Toast.LENGTH_SHORT).show();

        }
    }


    public boolean getInternetState(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return  true;
        }
        else
            return false;
    }
}
