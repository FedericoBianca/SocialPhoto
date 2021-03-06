package com.example.social_photo.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.social_photo.Fragments.OverviewTab;
import com.example.social_photo.NetworkTools.NetworkUtilities;
import com.example.social_photo.R;
import com.example.social_photo.Utils.SaveSharedPreference;


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
                   return "Error";
            }
            else {
                return trigger;
            }

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            if (result.equals("Error"))
                    Toast.makeText(Recap.this, "Host Unreachable", Toast.LENGTH_SHORT).show();
            else {
                try {
                    String[] ris = result.split(",");

                    if (ris[0].equals(""))
                        Toast.makeText(Recap.this, "No Results Found!", Toast.LENGTH_SHORT).show();

                    else {
                        String[] item = {
                                "Most Liked Photo: " + ris[1] + " likes!",
                                "Most Commented Photo: " + ris[3] + "comments!"
                        };
                        String[] images = {ris[0], ris[2]};
                        listView = findViewById(R.id.listView);
                        ListAdapter adapter = new ListAdapter(Recap.this, item, images);
                        listView.setAdapter(adapter);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Recap.this, "An Error has Occurred", Toast.LENGTH_LONG).show();
                }
            }
            swipeRefresh.setRefreshing(false);
            }

    }
}
