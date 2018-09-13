package com.example.social_photo.Activities;

import android.widget.*;

import java.util.*;

import android.view.*;
import android.content.*;

import com.squareup.picasso.Picasso;
import com.example.social_photo.NetworkTools.FacebookOperation;

public class ImageAdapter extends BaseAdapter {


    private Context context;
    private final ArrayList<String> bitmapList = new ArrayList<>();

    public ImageAdapter(Context context, String album, String location, String date) {
        this.context = context;
        FacebookOperation.getFacebookPhoto(album,bitmapList,location,date);

    }

    public int getCount() {
        return this.bitmapList.size();
    }

    public String getItem(int position) {
        return bitmapList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public ArrayList<String> getBitmapList() {
        return bitmapList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(this.context);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        try {

            Picasso.get().load(bitmapList.get(position)).resize(300,300).centerCrop().into(imageView);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageView;
    }



}
