package com.schmuck.www.schmuck;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.schmuck.www.schmuck.R;

public class SplashActivity extends AppCompatActivity {

    ActionBar bar;
    Animation alpha;
    ImageView ivlogo;
    TextView tvlogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        bar=getSupportActionBar();
        bar.hide();

        Window window=getWindow();
        window.setStatusBarColor(Color.parseColor(String.valueOf("#FFFFFF")));

        ivlogo=findViewById(R.id.ivlogo);
        tvlogo=findViewById(R.id.tvlogo);


        alpha= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha);
        ivlogo.setAnimation(alpha);

        final String[] string = new String[1];
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                string[0] =dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvlogo.setText(R.string.app_name);
                    alpha = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
                    tvlogo.setAnimation(alpha);

                }
            }, 1000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (string.equals("")) {
                     }else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, 2500);

    }
}
