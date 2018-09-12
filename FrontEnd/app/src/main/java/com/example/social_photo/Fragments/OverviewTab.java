package com.example.social_photo.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.social_photo.Activities.Recap;
import com.example.social_photo.R;
import com.example.social_photo.Utils.Utilities;

public class OverviewTab extends Fragment implements View.OnClickListener {

    private Spinner dropDown;
    private Spinner dropDown2;
    private Button button;
    public static final String YEAR = "com.example.social_photo.YEAR";
    public static final String MONTH = "com.example.social_photo.MONTH";
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.overview_tab, container, false);

        dropDown = rootView.findViewById(R.id.dropDown);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, Utilities.getYears());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(adapter);

        dropDown2 = rootView.findViewById(R.id.dropDown2);


        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, Utilities.getMonth());
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown2.setAdapter(adapter2);

        button = rootView.findViewById(R.id.buttonRecap);
        button.setOnClickListener(this);

        return rootView;

    }
    @Override
    public void onClick(View v) {
        if(getInternetState()) {
            String year = dropDown.getSelectedItem().toString();
            String month = dropDown2.getSelectedItem().toString();
            if (year.equals("Select Year") || month.equals("Select Month")) {
                Toast.makeText(getContext(), "You have to select both Year and Month!", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("TAG", "Data richiesta: " + year + "-" + month);
                Intent intent = new Intent(getContext(), Recap.class);
                intent.putExtra(YEAR, year);
                intent.putExtra(MONTH, month);
                startActivity(intent);
            }
        }
        else{
            Toast.makeText(getContext(), "You have to connect to Internet!", Toast.LENGTH_SHORT).show();

        }
    }

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

}
