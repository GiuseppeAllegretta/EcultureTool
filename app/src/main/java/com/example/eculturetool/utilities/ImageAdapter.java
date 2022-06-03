package com.example.eculturetool.utilities;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.example.eculturetool.entities.Oggetto;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private List<Oggetto> objectsList;
    private Context context;

    public ImageAdapter(List<Oggetto> objectsList, Context context){
        this.objectsList = objectsList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return objectsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return objectsList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = (ImageView) convertView;

        if(imageView == null){
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(350, 400));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        Glide.with(context).load(objectsList.get(position).getUrl()).centerCrop().into(imageView);
        return imageView;
    }
}
