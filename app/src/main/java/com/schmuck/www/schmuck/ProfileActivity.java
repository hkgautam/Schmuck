package com.schmuck.www.schmuck;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    ImageView imageView;
    ActionBar bar;
    String pic,username;
    AlertDialog dialog;
    TextInputLayout edphno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bar=getSupportActionBar();
        bar.hide();

        Toolbar toolbar=findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //getting intents

        username=getIntent().getExtras().getString("username");
        pic=getIntent().getExtras().getString("pic");

//        setting image view

        ImageView imageView=findViewById(R.id.profilepic);
        Picasso.with(this).load(pic).into(imageView);

        TextView tvname =findViewById(R.id.tvname);
        final TextView tvphno=findViewById(R.id.tvphno);

        tvname.setText(username);

        //Setting up firebase

        final DatabaseReference myref=FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        final FirebaseUser user=auth.getCurrentUser();

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(user.getUid()).child("phoneno").exists()){
                    tvphno.setText(String.valueOf(dataSnapshot.child(user.getUid()).child("phoneno").getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // phone no edit

        ImageView ivphno=findViewById(R.id.phnoedit);
        ivphno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(ProfileActivity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v= inflater.inflate(R.layout.phonenoedit,null);
                builder.setView(v);

                edphno=v.findViewById(R.id.edphno);
                Button butadd=v.findViewById(R.id.tvadd);
                Button butcancel=v.findViewById(R.id.tvcancel);

                butadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        boolean vp = validatephoneno();
                        if (vp) {
                            myref.child("Users").child(user.getUid()).child("username").setValue(username);
                            myref.child("Users").child(user.getUid()).child("phoneno").setValue(String.valueOf(edphno.getEditText().getText()));
                        }else {
                            validatephoneno();
                        }
                        tvphno.setText(String.valueOf(edphno.getEditText().getText()));
                        dialog.dismiss();
                    }
                });

                butcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog=builder.create();
                dialog.show();
            }
        });

    }

    private  boolean validatephoneno(){
        if (edphno.getEditText().getText().toString().trim().equals("")){
            edphno.setError("Can't be null");
            return false;
        }else if (edphno.getEditText().getText().toString().trim().length()!=10) {
            edphno.setError("Invalid Phone number");
            return false;
        }else {
            edphno.setError(null);
            return true;
        }
    }
}
