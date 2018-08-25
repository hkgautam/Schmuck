package com.schmuck.www.schmuck;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.schmuck.www.schmuck.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Admin on 7/26/2018.
 */

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.Holder> {

    ArrayList<Category> itemlist;
    Context context;
    public LayoutInflater inflater;

    public class Holder extends RecyclerView.ViewHolder{
        ImageView ivimage;
        TextView tvtitle,tvprice;
        RelativeLayout rlcategory;

        public Holder(View itemView) {
            super(itemView);

            ivimage=itemView.findViewById(R.id.ivimage);
            tvtitle=itemView.findViewById(R.id.tvtitle);
            rlcategory=itemView.findViewById(R.id.rlcat);
//            tvprice=itemView.findViewById(R.id.tvprice);
        }
    }

    CategoryRecyclerViewAdapter(Context context, ArrayList<Category> itemlist){
        this.context=context;
        this.itemlist=itemlist;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.categoryview, null);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final Category category =itemlist.get(position);
        Picasso.with(context).load(category.getPimage()).into(holder.ivimage);
        holder.tvtitle.setText(category.getPtitle());
        holder.rlcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,Main2Activity.class);
                intent.putExtra("FromContext","Categories");
                intent.putExtra("ContextName",category.getPtitle());
                context.startActivity(intent);
            }
        });
//        holder.tvprice.setText(category.getPprice());
    }


    @Override
    public int getItemCount() {
        return itemlist.size();
    }
}
