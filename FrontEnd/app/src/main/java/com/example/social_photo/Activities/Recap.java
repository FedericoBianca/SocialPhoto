package com.example.social_photo.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.social_photo.R;

import static java.security.AccessController.getContext;

public class Recap extends AppCompatActivity {
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);

        String[] itemname ={
                "Most Liked Photo",
                "Most Commented Photo",
                "Best Friend",
                "Best Location"

        };
        listView =findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(
                this, R.layout.mylist,
                R.id.textView3,itemname));
    }
}
