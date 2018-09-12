package com.example.social_photo.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.social_photo.R;
import com.example.social_photo.Fragments.SearchTab;

import java.util.ArrayList;

public class DisplayPhotos extends AppCompatActivity {
    protected String album;
    protected String location;
    protected String date_2;
    protected GridView imageGrid;
    protected ImageAdapter adapter;
    protected ProgressBar progressBar;
    protected TextView textView;
    protected SwipeRefreshLayout swipeRefresh;
    protected boolean created = true;
    public static final String ID = "com.example.social_photo.ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_photos);
        new DownloadFilesTask().execute();
        swipeRefresh = findViewById(R.id.swiperefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new  DownloadFilesTask().execute();

            }
        });


    }
    private class DownloadFilesTask extends AsyncTask<Void, Void, ArrayList> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(created) {
                imageGrid = findViewById(R.id.gridview);
                progressBar = findViewById(R.id.progressBar2);
                textView = findViewById(R.id.textView2);
                textView.setText(R.string.FetchPhotos);
                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                created = false;
            }



        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            Intent intent = getIntent();
            album = intent.getStringExtra(SearchTab.ALBUM);
            location = intent.getStringExtra(SearchTab.LOCATION);
            date_2 = intent.getStringExtra(SearchTab.DATE);
            adapter = new ImageAdapter(DisplayPhotos.this,album,location,date_2);

            return adapter.getBitmapList();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(final ArrayList arrayList) {
            super.onPostExecute(arrayList);
            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            imageGrid.setAdapter(adapter);
            imageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    Intent i = new Intent(DisplayPhotos.this, FullImageActivity.class);
                    i.putExtra(ID, position);
                    i.putStringArrayListExtra("Array",arrayList);
                    startActivity(i);
                }
            });
            if(arrayList.contains("Error")) Toast.makeText(DisplayPhotos.this,"Error in fetching photos, try to connect to Internet!",Toast.LENGTH_LONG).show();

            else Toast.makeText(DisplayPhotos.this,String.valueOf(arrayList.size())+" photos found!",Toast.LENGTH_SHORT).show();

            swipeRefresh.setRefreshing(false);
        }
    }

}
