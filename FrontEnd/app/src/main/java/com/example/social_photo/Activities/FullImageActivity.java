package com.example.social_photo.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.social_photo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FullImageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        // get intent data
        Intent i = getIntent();

        // Selected image id
        int position = i.getExtras().getInt(DisplayPhotos.ID);
        ArrayList<String> array = i.getStringArrayListExtra("Array");

        ImageView imageView = findViewById(R.id.full_image_view);
        try {
            //imageView.setImageBitmap(Utilities.urlImageToBitmap(array.get(position)));
            Picasso.get().load(array.get(position)).into(imageView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
