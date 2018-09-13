package com.example.social_photo.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.social_photo.Activities.DisplayPhotos;
import com.example.social_photo.NetworkTools.FacebookOperation;
import com.example.social_photo.R;
import com.example.social_photo.Utils.SaveSharedPreference;
import com.example.social_photo.Utils.Utilities;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchTab extends Fragment implements View.OnClickListener {
    private TextView textView;
    private Spinner spinner;
    private Spinner spinner2;
    private CircleImageView circleImageView;
    private HashMap<String,String> map = new HashMap<>();
    public static final String ALBUM = "com.example.social_photo.ALBUM";
    public static final String LOCATION = "com.example.social_photo.LOCATION";
    public static final String DATE = "com.example.social_photo.DATE";
    private Button submit;

    private PlaceAutocompleteFragment autocompleteFragment;
    protected String plc="";
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.research_tab, container, false);
        textView = rootView.findViewById(R.id.textView);
        textView.setText(SaveSharedPreference.getUserName(getContext()));

        circleImageView = rootView.findViewById(R.id.profile_image);
        circleImageView.setImageBitmap(Utilities.StringToBitMap(SaveSharedPreference.getProfilePicture(getContext())));

        spinner = rootView.findViewById(R.id.dropDown);
        spinner2 = rootView.findViewById(R.id.dropDown2);


        submit = rootView.findViewById(R.id.submit);
        submit.setOnClickListener(this);


        //----------------------------------------------------------------------------------------------

        autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.findRidePlaceAutocompleteFragment);

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
                    plc = Utilities.getCity(lat,lng,getContext());
                    try {

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Status status) {

            }

        });



        if(SaveSharedPreference.getUserAlbums(getContext()) == ""){
            FacebookOperation.getAlbum(getContext());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, Utilities.getAlbumsNames(map,getContext()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, Utilities.getYears());
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        textView.setText(SaveSharedPreference.getUserName(getContext()));
        circleImageView.setImageBitmap(Utilities.StringToBitMap(SaveSharedPreference.getProfilePicture(getContext())));


    }



    //----------------------------------------------------------------------------------------------





    //----------------------------------------------------------------------------------------------

    public boolean getInternetState(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService (Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return  true;
        }
        else
            return false;
    }


    @Override
    public void onClick(View v) {
        if(getInternetState()) {
            if (spinner.getSelectedItem().toString().equals("Select Album")) {
                Toast.makeText(getContext(), "You have to select one album!", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(getContext(), DisplayPhotos.class);
                intent.putExtra(ALBUM, map.get(spinner.getSelectedItem().toString()));
                intent.putExtra(LOCATION, plc);
                intent.putExtra(DATE, spinner2.getSelectedItem().toString());


                startActivity(intent);
                autocompleteFragment.setText("");
                plc="";

            }
        }
        else{
            Toast.makeText(getContext(), "You have to connect to Internet!", Toast.LENGTH_SHORT).show();

        }

    }
}
