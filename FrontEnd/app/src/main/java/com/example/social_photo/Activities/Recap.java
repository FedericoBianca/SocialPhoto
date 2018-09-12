package com.example.social_photo.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.social_photo.Fragments.OverviewTab;
import com.example.social_photo.NetworkTools.NetworkUtilities;
import com.example.social_photo.R;
import com.example.social_photo.Utils.SaveSharedPreference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


public class Recap extends AppCompatActivity {
    protected ListView listView;
    protected String year;
    protected String month;
    protected TextView textView;
    protected ProgressBar progressBar;
    protected SwipeRefreshLayout swipeRefresh;
    protected boolean exec = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);


        new getInfos().execute();
        swipeRefresh = findViewById(R.id.swiperefreshInfo);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getInfos().execute();

            }
        });
    }

    public class getInfos extends AsyncTask<Void,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(exec) {
                Intent intent = getIntent();
                year = intent.getStringExtra(OverviewTab.YEAR);
                month = intent.getStringExtra(OverviewTab.MONTH);
                progressBar = findViewById(R.id.progressBarInfo);
                textView = findViewById(R.id.textViewInfo);
                textView.setText(R.string.FetchInfos);
                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                exec = false;
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            NetworkUtilities.getDataUser(SaveSharedPreference.getUserToken(Recap.this), SaveSharedPreference.getUserID(Recap.this), year, month);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String trigger = NetworkUtilities.Trigger(SaveSharedPreference.getUserID(Recap.this));
            if(trigger == null) {
                   return "";
            }
            else {

               Log.d("TAG",   trigger);
                   return trigger;
            }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            if (s.equals("")) Toast.makeText(Recap.this,"Host Unreachable",Toast.LENGTH_SHORT).show();
            else if(s.trim().equals("Nothing to show"))  Toast.makeText(Recap.this,"No Results Found!",Toast.LENGTH_SHORT).show();
            else {
                String[] itemname = {
                        "Most Liked Photo",
                        "Most Commented Photo",
                        "Best Friend",
                        "Best Location"

                };
                ArrayList<String> images = new ArrayList<>();
                listView = findViewById(R.id.listView);
                listView.setAdapter(new ArrayAdapter<>(
                        Recap.this, R.layout.mylist,
                        R.id.textView3, itemname));
                //listView.setAdapter(new ArrayAdapter<>(Recap.this,R.layout.mylist,R.id.imageInfo,itemname));
                /*ImageAdapter adapter = new ImageAdapter(Recap.this, images);
                listView.setAdapter(adapter);*/
            }
            swipeRefresh.setRefreshing(false);
            /*
            String[] resultArray = s.split(",");
            String photoId = resultArray[0];
            int photoLikes = Integer.parseInt(resultArray[1]);
            */
            /*Intent intent = new Intent(getContext(),Recap.class);
            startActivity(intent);*/

        }

    }
}
