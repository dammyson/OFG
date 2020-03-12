package com.ofidy.ofidyshoppingbrowser.ofidyApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Brand;

import java.util.List;

/**
 * Created by damil on 8/3/2017.
 */
public class BrandAdapter extends BaseAdapter {

    private LayoutInflater linflater;
    private List<Brand> listnews;
    Context contet;

    public BrandAdapter(Context context, List<Brand> customisedListView){
        linflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listnews = customisedListView;
        contet = context;

    }

    @Override
    public int getCount() {
        return listnews.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder listViewHolder;
        if(view == null){


            listViewHolder = new ViewHolder();
            view=linflater.inflate(R.layout.brand_item, viewGroup, false);

            listViewHolder.textInListView = (TextView)view.findViewById(R.id.textbrand);


            view.setTag(listViewHolder);
        }else {
            listViewHolder = (ViewHolder)view.getTag();
        }


        listViewHolder.textInListView.setText(String.valueOf(listnews.get(i).getName()));





        return view;
    }




    static class ViewHolder{
        TextView textInListView;

    }
}
