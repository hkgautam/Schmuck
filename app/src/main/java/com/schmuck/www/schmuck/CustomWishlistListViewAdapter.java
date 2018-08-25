package com.schmuck.www.schmuck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Admin on 1/3/2018.
 */

public class CustomWishlistListViewAdapter extends BaseAdapter {
    ArrayList<FeaturedItems> items;
    LayoutInflater inflater;
    Context c;

    CustomWishlistListViewAdapter(Context context,ArrayList<FeaturedItems> items){
        c=context;
        this.items=items;
        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public static class Hold{
        ImageView iimage;
        TextView ttitle;
        TextView tprice;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View listview;
        Hold hold=new Hold();

        FeaturedItems fitems =items.get(i);

        listview=inflater.inflate(R.layout.featured,null);

        hold.iimage=listview.findViewById(R.id.iimage);
        hold.ttitle=listview.findViewById(R.id.ttitle);
        hold.tprice=listview.findViewById(R.id.tprice);

        Picasso.with(c).load(fitems.getImage()).into(hold.iimage);
        hold.ttitle.setText(fitems.getTitle());
        hold.tprice.setText(fitems.getPrice());

        return listview;
    }

}
