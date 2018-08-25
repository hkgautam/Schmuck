package com.schmuck.www.schmuck;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
 * Created by Admin on 7/29/2018.
 */

public class Main2RecyclerViewAdapter extends RecyclerView.Adapter<Main2RecyclerViewAdapter.Holder> {
    ArrayList<FeaturedItems> featureditemlist;
    Context context;
    public LayoutInflater inflater;
    String contextname,fromcontext;

    public class Holder extends RecyclerView.ViewHolder{
        ImageView ivimage,ivbtnfav;
        TextView tvtitle,tvprice;
        RelativeLayout rlpro;

        public Holder(View itemView) {
            super(itemView);

            ivimage=itemView.findViewById(R.id.ivproimage);
            tvtitle=itemView.findViewById(R.id.tvprotitle);
            tvprice=itemView.findViewById(R.id.tvproprice);
            rlpro=itemView.findViewById(R.id.rlproduct);
            ivbtnfav=itemView.findViewById(R.id.btnfav);
        }
    }

    Main2RecyclerViewAdapter(Context context, ArrayList<FeaturedItems> featureditemlist,String contextname,String fromcontext){
        this.context=context;
        this.featureditemlist=featureditemlist;
        this.contextname=contextname;
        this.fromcontext=fromcontext;
    }

    @Override
    public Main2RecyclerViewAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.productgridview, null);
        return new Main2RecyclerViewAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(final Main2RecyclerViewAdapter.Holder holder, int position) {
        final FeaturedItems featuredItems=featureditemlist.get(position);
        Picasso.with(context).load(featuredItems.getImage()).into(holder.ivimage);
        holder.tvtitle.setText(featuredItems.getTitle());
        holder.tvprice.setText(featuredItems.getPrice());
        holder.rlpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context,ProductActivity.class);
                intent.putExtra("Product id",String.valueOf(featuredItems.getId()));
                intent.putExtra("FromContent",fromcontext);
                intent.putExtra("ContextName",String.valueOf(contextname));

                context.startActivity(intent);
            }
        });

        holder.ivbtnfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable.ConstantState constantState=holder.ivbtnfav.getDrawable().getConstantState();
                Drawable.ConstantState myDrawable = context.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp).getConstantState();

                if (constantState==myDrawable){
                    holder.ivbtnfav.setImageResource(R.drawable.ic_favorite_black_24dp);
                }else{
                    holder.ivbtnfav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return featureditemlist.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else return 1;
    }
}
