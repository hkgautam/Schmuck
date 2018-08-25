package com.schmuck.www.schmuck;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.schmuck.www.schmuck.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Admin on 7/26/2018.
 */

public class FeaturedRecyclerViewAdapter extends RecyclerView.Adapter<FeaturedRecyclerViewAdapter.Holder> {

    ArrayList<FeaturedItems> featureditemlist;
    Context context;
    public LayoutInflater inflater;

    public class Holder extends RecyclerView.ViewHolder{
        ImageView ivimage;
        TextView tvtitle,tvprice;
        RelativeLayout rlfeatured;
        Button butwishlist,butaddtocart;

        public Holder(View itemView) {
            super(itemView);

            ivimage=itemView.findViewById(R.id.ivimage);
            tvtitle=itemView.findViewById(R.id.tvtitle);
            tvprice=itemView.findViewById(R.id.tvprice);
            rlfeatured=itemView.findViewById(R.id.rlfeatured);
            butwishlist=itemView.findViewById(R.id.butwishlist);
            butaddtocart=itemView.findViewById(R.id.butcart);
        }
    }

    FeaturedRecyclerViewAdapter(Context context, ArrayList<FeaturedItems> featureditemlist){
        this.context=context;
        this.featureditemlist=featureditemlist;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.featureditems, null);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        final FeaturedItems featuredItems=featureditemlist.get(position);
        Picasso.with(context).load(featuredItems.getImage()).into(holder.ivimage);
        holder.tvtitle.setText(featuredItems.getTitle());
        holder.tvprice.setText(featuredItems.getPrice());
        holder.rlfeatured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ProductActivity.class);
                intent.putExtra("Product id",String.valueOf(featuredItems.getId()));
                intent.putExtra("FromContent","Featured_Products");
                context.startActivity(intent);
            }
        });


        final DatabaseReference ref=FirebaseDatabase.getInstance().getReference();

        FirebaseAuth auth=FirebaseAuth.getInstance();
        final FirebaseUser user=auth.getCurrentUser();

        final String name=featuredItems.getTitle();
        final String image=featuredItems.getImage();
        final String price=featuredItems.getPrice();

        holder.butwishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Drawable.ConstantState constantState=holder.butwishlist.getCompoundDrawables()[0].getConstantState();
                Drawable.ConstantState myDrawable = context.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp).getConstantState();

                if (constantState==myDrawable){
                  holder.butwishlist.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_favorite_black_24dp, 0, 0, 0);

                ref.child("Users").child(String.valueOf(user.getUid())).child("My Wishlist").child(featuredItems.getId()).child("image").setValue(image);
                ref.child("Users").child(String.valueOf(user.getUid())).child("My Wishlist").child(featuredItems.getId()).child("name").setValue(name);
                ref.child("Users").child(String.valueOf(user.getUid())).child("My Wishlist").child(featuredItems.getId()).child("price").setValue(price);

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp: dataSnapshot.child("Featured_Products").getChildren()){

                            if (dsp.child("thumbnail1").exists()) {
                                String thumb1 = String.valueOf(dsp.child("thumbnail1").getValue());
                                ref.child("Users").child(String.valueOf(user.getUid())).child("My Wishlist").child(featuredItems.getId()).child("thumbnail1").setValue(thumb1);
                            }
                            if (dsp.child("thumbnail2").exists()) {
                                String thumb2 = String.valueOf(dsp.child("thumbnail2").getValue());
                                ref.child("Users").child(String.valueOf(user.getUid())).child("My Wishlist").child(featuredItems.getId()).child("thumbnail2").setValue(thumb2);
                            }
                            if (dsp.child("thumbnail3").exists()) {
                                String thumb3 = String.valueOf(dsp.child("thumbnail3").getValue());
                                ref.child("Users").child(String.valueOf(user.getUid())).child("My Wishlist").child(featuredItems.getId()).child("thumbnail3").setValue(thumb3);
                            }
                            if (dsp.child("thumbnail4").exists()) {
                                String thumb4 = String.valueOf(dsp.child("thumbnail4").getValue());
                                ref.child("Users").child(String.valueOf(user.getUid())).child("My Wishlist").child(featuredItems.getId()).child("thumbnail4").setValue(thumb4);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                }else{
                    holder.butwishlist.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_favorite_border_black_24dp, 0, 0, 0);

                    ref.child("Users").child(String.valueOf(user.getUid())).child("My Cart").child(featuredItems.getId()).removeValue();

                }

            }
        });

        holder.butaddtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ref.child("Users").child(String.valueOf(user.getUid())).child("My Cart").child(featuredItems.getId()).child("image").setValue(image);
                ref.child("Users").child(String.valueOf(user.getUid())).child("My Cart").child(featuredItems.getId()).child("name").setValue(name);
                ref.child("Users").child(String.valueOf(user.getUid())).child("My Cart").child(featuredItems.getId()).child("price").setValue(price);

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp: dataSnapshot.child("Featured_Products").getChildren()){

                            if (dsp.child("thumbnail1").exists()) {
                                String thumb1 = String.valueOf(dsp.child("thumbnail1").getValue());
                                ref.child("Users").child(String.valueOf(user.getUid())).child("My Cart").child(featuredItems.getId()).child("thumbnail1").setValue(thumb1);
                            }
                            if (dsp.child("thumbnail2").exists()) {
                                String thumb2 = String.valueOf(dsp.child("thumbnail2").getValue());
                                ref.child("Users").child(String.valueOf(user.getUid())).child("My Cart").child(featuredItems.getId()).child("thumbnail2").setValue(thumb2);
                            }
                            if (dsp.child("thumbnail3").exists()) {
                                String thumb3 = String.valueOf(dsp.child("thumbnail3").getValue());
                                ref.child("Users").child(String.valueOf(user.getUid())).child("My Cart").child(featuredItems.getId()).child("thumbnail3").setValue(thumb3);
                            }
                            if (dsp.child("thumbnail4").exists()) {
                                String thumb4 = String.valueOf(dsp.child("thumbnail4").getValue());
                                ref.child("Users").child(String.valueOf(user.getUid())).child("My Cart").child(featuredItems.getId()).child("thumbnail4").setValue(thumb4);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }


    @Override
    public int getItemCount() {
        return featureditemlist.size();
    }
}
