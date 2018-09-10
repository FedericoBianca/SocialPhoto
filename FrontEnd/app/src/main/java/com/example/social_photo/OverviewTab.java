package com.example.social_photo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static com.example.social_photo.R.layout.mylist;

public class OverviewTab extends Fragment {
    private ListView listView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.overview_tab, container, false);
        String[] itemname ={
                "Most Liked Photo",
                "Most Commented Photo",
                "Best Friend",
                "Best Location"

        };
        listView = rootView.findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<>(
                getContext(), R.layout.mylist,
                R.id.textView3,itemname));
        return rootView;

    }
}
