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


        new getInfos().execute(year,month);
        swipeRefresh = findViewById(R.id.swiperefreshInfo);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getInfos().execute(year,month);

            }
        });
    }

    public class getInfos extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(exec) {
                Intent intent = getIntent();
                year = intent.getStringExtra(OverviewTab.YEAR);
                month = intent.getStringExtra(OverviewTab.MONTH);
                progressBar = findViewById(R.id.progressBarInfo);
                textView = findViewById(R.id.textViewInfo);
                textView.setText("Fetching Infos...");
                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                exec = false;
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String year = params[0];
            String month = params[1];

           String result = NetworkUtilities.getDataUser(SaveSharedPreference.getUserToken(Recap.this), SaveSharedPreference.getUserID(Recap.this), year, month);
           if(result == null) {
               return "";
           }
           else {

               Log.d("TAG", "LA FESSA DI PIERINO: " + result);
               return result;
           }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            if (s.equals("")) Toast.makeText(Recap.this,"Host Unreachable",Toast.LENGTH_SHORT).show();
            else {
                String[] itemname = {
                        "Most Liked Photo",
                        "Most Commented Photo",
                        "Best Friend",
                        "Best Location"

                };
                listView = findViewById(R.id.listView);
                listView.setAdapter(new ArrayAdapter<>(
                        Recap.this, R.layout.mylist,
                        R.id.textView3, itemname));
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
