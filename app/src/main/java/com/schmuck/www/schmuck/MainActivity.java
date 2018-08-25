package com.schmuck.www.schmuck;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.schmuck.www.schmuck.R;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    CarouselView carouselView;
    ArrayList<Integer> images;
    RecyclerView recyclerView,rvfeatured;
    CategoryRecyclerViewAdapter categoryRecyclerViewAdapter;
    FeaturedRecyclerViewAdapter featuredRecyclerViewAdapter;
    ArrayList<Category> catlist;
    ArrayList<FeaturedItems> featuredItemslist;
    String fpid,title,price,image;
    FirebaseAuth auth;
    String username,phoneno;
    String pic,facebookUserId;
    TextView tvname,tvphno;
    ImageView ivpic;
    CallbackManager mCallbackManager;
    LoginButton fb;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        final FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser!=null) {
            username = currentUser.getDisplayName();
            for(UserInfo profile : currentUser.getProviderData()) {
                if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                    facebookUserId = profile.getUid();
                }
            }
            pic = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";

        }else{
            username="Unknown user";
            phoneno="";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Access a Cloud Firestore instance from your Activity

        auth=FirebaseAuth.getInstance();

        final DatabaseReference featuredref=FirebaseDatabase.getInstance().getReference();
        final StorageReference stref= FirebaseStorage.getInstance().getReference();

        //Database values

        final FirebaseUser user=auth.getCurrentUser();

        //carousel view

        carouselView=findViewById(R.id.carouselView);

        DatabaseReference carref=FirebaseDatabase.getInstance().getReference();

        final int[] count = {0};
        carref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.child("Carousel").getChildren()) {

                    final String title=String.valueOf(dsp.getKey());
                    final String image=String.valueOf(dataSnapshot.child("CarouselImage").child(String.valueOf(title+"Image")).getValue());

                    count[0] += 1;

                    ImageListener imageListener=new ImageListener() {
                        @Override
                        public void setImageForPosition(final int position, ImageView imageView) {
                            Picasso.with(MainActivity.this).load(image).into(imageView);
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                                    intent.putExtra("FromContext","Carousel");
                                    intent.putExtra("ContextName",title);
                                    startActivity(intent);
                                }
                            });
                        }
                    };

                    carouselView.setImageListener(imageListener);

                }
                carouselView.setPageCount(count[0]);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Categories recycler view

        catlist=new ArrayList<>();

        recyclerView=findViewById(R.id.recyclerview);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this,LinearLayout.HORIZONTAL,true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.child("Categories").getChildren()) {
                    String title=String.valueOf(dsp.getKey());
                    String image = String.valueOf(dataSnapshot.child("CategoriesImage").child(String.valueOf(title+"Image")).getValue());

                    Category category1 =new Category(image,title);
                    catlist.add(category1);

                }
                categoryRecyclerViewAdapter =new CategoryRecyclerViewAdapter(MainActivity.this,catlist);
                recyclerView.setAdapter(categoryRecyclerViewAdapter);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(catlist.size() - 1);
                        categoryRecyclerViewAdapter.notifyDataSetChanged();
                    }
                },100);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Featured recycler view

        rvfeatured=findViewById(R.id.rvfeatured);
        featuredItemslist=new ArrayList<>();

        featuredref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dsp : dataSnapshot.child("Featured_Products").getChildren()){
                    fpid=String.valueOf(dsp.getKey());
                    title=String.valueOf(dsp.child("name").getValue());
                    price= String.valueOf(dsp.child("price").getValue());
                    image = String.valueOf(dsp.child("image").getValue());

                    FeaturedItems fitems=new FeaturedItems(fpid,image,title,price);
                    featuredItemslist.add(fitems);
                }
                featuredRecyclerViewAdapter=new FeaturedRecyclerViewAdapter(getApplicationContext(),featuredItemslist);
                rvfeatured.setAdapter(featuredRecyclerViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvfeatured.setLayoutManager(linearLayoutManager);
//        rvfeatured.setAdapter(featuredRecyclerViewAdapter);

        //End of Code

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void handleFacebookAccessToken(AccessToken token) {
//        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();

                            Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();

                            String username=user.getDisplayName();
                            finish();
                            startActivity(getIntent());

                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Signin failed.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        FirebaseUser user=auth.getCurrentUser();
        LinearLayout llprofile = findViewById(R.id.llprofile);
        if (user!=null) {

            ivpic = findViewById(R.id.ivprofilepic);
            Picasso.with(this).load(pic).into(ivpic);
            tvname = findViewById(R.id.tvusername);
            tvname.setText(username);

//        tvphno=findViewById(R.id.tvphoneno);
//        tvphno.setText(phoneno);

            llprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.putExtra("pic", pic);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            });

        }else{
            llprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showlogin();
                }
            });

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        }else if (id == R.id.nav_logout){
            auth.signOut();
            LoginManager.getInstance().logOut();
            finish();
            startActivity(getIntent());
        }
        else if (id == R.id.nav_orders) {

        } else if (id == R.id.nav_cart) {
            Intent intent=new Intent(MainActivity.this,MyCartActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_wishlist) {
            Intent intent=new Intent(MainActivity.this,MyWishlistActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_all_jewellery) {

        } else if (id == R.id.nav_new_arrivals) {

        }else if (id == R.id.nav_jewellery_sets) {

        }else if (id == R.id.nav_neckpieces) {

        }else if (id == R.id.nav_rings) {

        }else if (id == R.id.nav_bracelets) {

        }else if (id == R.id.nav_rate_us) {

        }else if (id == R.id.nav_fb) {

        }else if (id == R.id.nav_insta) {

        }else if (id == R.id.nav_community) {

        }else if (id == R.id.nav_new_arrivals) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showlogin(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = inflater.inflate(R.layout.login, null);
        builder.setView(view1);

        // Facebook login

        mCallbackManager = CallbackManager.Factory.create();

        fb = view1.findViewById(R.id.fbbtn);

        fb.setReadPermissions("email", "public_profile");
        fb.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show();

//                        Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
//                        Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
