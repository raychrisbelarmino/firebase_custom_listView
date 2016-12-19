package com.example.chiz.list_view_adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by chiz on 12/18/16.
 */

public class RecipeListAdapter extends BaseAdapter {
    Context context;
    List<Recipes> rowItems;

    public RecipeListAdapter ( Context context, List<Recipes> items ) {
        this.context = context;
        this.rowItems = items;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtUrl;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_list, null);
            holder = new ViewHolder();
            holder.txtUrl = (TextView) convertView.findViewById(R.id.Text2);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.Text1);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Recipes rowItem = (Recipes) getItem(position);

        holder.txtUrl.setText(rowItem.getUrl());
        holder.txtTitle.setText(rowItem.getTitle());
        Picasso.with(context).load(rowItem.getImage_url()).error(R.drawable.placeholder_error).into(holder.imageView);

        return convertView;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

}
