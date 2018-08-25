package com.schmuck.www.schmuck;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.schmuck.www.schmuck.R;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<FeaturedItems> productlist;
    ActionBar actionBar;
    RelativeLayout relativeLayout;
    DatabaseReference ref;
    Main2RecyclerViewAdapter adapter;
    public String contextname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        actionBar=getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //getting intents

        final String fromcontext=getIntent().getStringExtra("FromContext");
        contextname=getIntent().getStringExtra("ContextName");

        //products recycler view

        recyclerView=findViewById(R.id.rvmain2);

        productlist=new ArrayList<>();

        ref= FirebaseDatabase.getInstance().getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp: dataSnapshot.child(fromcontext).child(contextname).getChildren()) {

                    String id=String.valueOf(dsp.getKey());

                    String image=String.valueOf(dsp.child("image").getValue());
                    String name=String.valueOf(dsp.child("name").getValue());
                    String price=String.valueOf(dsp.child("price").getValue());

                    FeaturedItems fproduct= new FeaturedItems(id,image,name,price);
                    productlist.add(fproduct);
                }
                adapter=new Main2RecyclerViewAdapter(Main2Activity.this,productlist,contextname,fromcontext);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        FeaturedItems product1 =new FeaturedItems(id,"https://firebasestorage.googleapis.com/v0/b/kiara-d0cc2.appspot.com/o/Featured_Products%2Fhjhjh%2FBracelet.jpg?alt=media&token=f7b6a2c4-ed4b-4694-9599-0f24222bd5e8","New Image","400");
//        productlist.add(product1);
//        productlist.add(product1);
//        productlist.add(product1);
//        productlist.add(product1);
//        productlist.add(product1);
//        productlist.add(product1);
//        productlist.add(product1);

//
//        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(this,2);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());


//        MyAdapter adapter = new MyAdapter();
        final GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(adapter.getItemViewType(position)){
                    case 0:
                        return 2;
                    case 1:
                        return 1;
                    default:
                        return 0;
                }
            }
        });

        recyclerView.setLayoutManager(mLayoutManager);


        // recycler view end

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
