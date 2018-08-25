package com.schmuck.www.schmuck;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.schmuck.www.schmuck.R;

import java.util.ArrayList;

public class MyCartActivity extends AppCompatActivity {

    ActionBar actionBar;
    ListView listView;
    ArrayList<FeaturedItems> cartitems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Window window=getWindow();
        window.setStatusBarColor(Color.parseColor("#047BD5"));

        listView=findViewById(R.id.lvcart);

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        final FirebaseUser user=auth.getCurrentUser();

        cartitems=new ArrayList<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp: dataSnapshot.child("Users").child(user.getUid()).child("My Cart").getChildren()){
                    String id=dsp.getKey();
                    String title=String.valueOf(dsp.child("name").getValue());
                    String price= String.valueOf(dsp.child("price").getValue());
                    String image= String.valueOf(dsp.child("image").getValue());

                    FeaturedItems items=new FeaturedItems(id,image,title,price);
                    cartitems.add(items);

                }

                CustomWishlistListViewAdapter customWishlistListViewAdapter=new CustomWishlistListViewAdapter(getApplicationContext(),cartitems);
                listView.setAdapter(customWishlistListViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
