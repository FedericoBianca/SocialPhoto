package com.example.social_photo.Activities;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.social_photo.R;
import com.squareup.picasso.Picasso;

public class ListAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] web;
    private final String[] imageId;
    public ListAdapter(Activity context,
                      String[] web, String[] imageId) {
        super(context, R.layout.mylist, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.mylist, null, true);
        TextView txtTitle =  rowView.findViewById(R.id.textViewInfo);

        ImageView imageView =  rowView.findViewById(R.id.imageInfo);
        txtTitle.setText(web[position]);
        Picasso.get().load(imageId[position]).resize(300,300).centerCrop().into(imageView);
        return rowView;
    }
}
