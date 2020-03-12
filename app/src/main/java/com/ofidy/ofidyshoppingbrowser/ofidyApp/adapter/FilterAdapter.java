package com.ofidy.ofidyshoppingbrowser.ofidyApp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.Materials.model.FilterObject;

import java.util.ArrayList;

/**
 * Created by damil on 7/28/2017.
 */
public class FilterAdapter extends BaseAdapter {

    Context c;
    ArrayList<FilterObject> objects;

    public FilterAdapter(Context context, ArrayList<FilterObject> objects) {
        super();
        this.c = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        FilterObject cur_obj = objects.get(position);
        LayoutInflater inflater = ((Activity) c).getLayoutInflater();
        View row = inflater.inflate(R.layout.filterrow, parent, false);
        TextView label = (TextView) row.findViewById(R.id.filter);
        label.setText(cur_obj.getCompany());
        ImageView icon = (ImageView) row.findViewById(R.id.image);
        if(position > 0){  icon.setImageResource(cur_obj.getImage_id());}


        return row;
    }
}
