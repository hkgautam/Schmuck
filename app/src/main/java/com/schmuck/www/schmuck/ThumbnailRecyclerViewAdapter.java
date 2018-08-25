package com.schmuck.www.schmuck;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.schmuck.www.schmuck.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Admin on 7/27/2018.
 */

public class ThumbnailRecyclerViewAdapter extends RecyclerView.Adapter<ThumbnailRecyclerViewAdapter.Holder>  {
    ArrayList<String> thumblist;
    Context context;
    LayoutInflater inflater;

    public class Holder extends RecyclerView.ViewHolder{
        ImageView ivthumb;

        public Holder(View itemView) {
            super(itemView);

            ivthumb=itemView.findViewById(R.id.ivthumbnail);
        }
    }

    ThumbnailRecyclerViewAdapter(Context context, ArrayList<String> thumblist){
        this.context=context;
        this.thumblist=thumblist;
    }

    @Override
    public ThumbnailRecyclerViewAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.product_thumbnail, null);
        return new ThumbnailRecyclerViewAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(ThumbnailRecyclerViewAdapter.Holder holder, int position) {
        Picasso.with(context).load(thumblist.get(position)).into(holder.ivthumb);

    }


    @Override
    public int getItemCount() {
        return thumblist.size();
    }

}
