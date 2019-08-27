package com.example.adelelephant.assignmenthygiene;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Adelelephant on 3/21/2018.
 * @author Adele Parker
 * creates a custom list view using the layout in listview_layout and lists title, description and image
 * to set the TextViews and ImageView
 */

public class CustomListView extends ArrayAdapter<String>{

    private String[] title;
    private String[] description;
    private Integer[] image;
    private Activity context;

    //Sets the parameters to the corresponding variable.
    public CustomListView(Activity context, String[] title, String[] description, Integer[] image) {
        super(context, R.layout.activity_main,title);

        this.title = title;
        this.description = description;
        this.image = image;
        this.context = context;
    }

    //Sets the information in the TextViews and ImageView
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        ViewHolder viewHolder = null;
        if(view==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.listview_layout,null,true);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvTitle.setText(title[position]);
        viewHolder.tvDescription.setText(description[position]);
        viewHolder.imImage.setImageResource(image[position]);

        return view;
    }

    //sets the TextViews and ImagesView for use. Makes the title of the restaurant darker and larger so it stands out.
    class ViewHolder{
        TextView tvTitle;
        TextView tvDescription;
        ImageView imImage;
        LinearLayout layoutBase;

        ViewHolder(View view){
            tvTitle = view.findViewById(R.id.tvTitle);
            tvDescription = view.findViewById(R.id.tvDescription);
            imImage = view.findViewById(R.id.ivImage);
            layoutBase = view.findViewById(R.id.llBaseList);
            tvTitle.setVisibility(View.VISIBLE);
            tvDescription.setVisibility(View.VISIBLE);
            imImage.setVisibility(View.VISIBLE);
            tvTitle.setTextSize(15);
            tvTitle.setTextColor(Color.parseColor("#000000"));
            imImage.setMaxHeight(250);
            layoutBase.setMinimumHeight(750);

        }

    }

}
