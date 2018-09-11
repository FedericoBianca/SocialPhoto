package com.example.social_photo.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.social_photo.Activities.DisplayPhotos;
import com.example.social_photo.R;
import com.example.social_photo.Utils.Utilities;
import com.james602152002.floatinglabelspinner.FloatingLabelSpinner;

import static com.example.social_photo.R.layout.mylist;

public class OverviewTab extends Fragment implements View.OnClickListener {
    private ListView listView;
    private FloatingLabelSpinner dropDown;
    private FloatingLabelSpinner dropDown2;
    private Button button;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.overview_tab, container, false);
        String[] itemname ={
                "Most Liked Photo",
                "Most Commented Photo",
                "Best Friend",
                "Best Location"

        };
        dropDown = rootView.findViewById(R.id.dropDown);
        dropDown.setHint("Select Year");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, Utilities.getYears());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(adapter);

        dropDown2 = rootView.findViewById(R.id.dropDown2);
        dropDown2.setHint("Select Month");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, Utilities.getMonth());
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown2.setAdapter(adapter2);

        button = rootView.findViewById(R.id.buttonRecap);
        button.setOnClickListener(this);

        /*listView = rootView.findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<>(
                getContext(), R.layout.mylist,
                R.id.textView3,itemname));*/
        return rootView;

    }
    @Override
    public void onClick(View v) {
        //TODO
    }
}
