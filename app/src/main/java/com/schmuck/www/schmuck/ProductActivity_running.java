package com.schmuck.www.schmuck;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class ProductActivity_running extends AppCompatActivity implements PaytmPaymentTransactionCallback{

    RecyclerView rvthumb;
    ArrayList<String> thumblist;
    ImageView ivlarge;
    TextView tvpname, tvpprice;
    ActionBar actionBar;
    AlertDialog dialog;
    TextInputLayout edphno;
    CallbackManager mCallbackManager;

    private FirebaseAuth mAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_product);

        actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Initialize Firebase Auth

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        //getting intent

        final String productid = getIntent().getStringExtra("Product id");
        final String fromcontent=getIntent().getStringExtra("FromContent");

        //recycler view of thumbnail

        thumblist = new ArrayList<>();


        rvthumb = findViewById(R.id.rvthumbnail);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, true);
        rvthumb.setLayoutManager(layoutManager);
        rvthumb.setItemAnimator(new DefaultItemAnimator());
//        rvthumb.smoothScrollToPosition(thumblist.size() - 1);


        //Setting pimage, pname, pprice

        ivlarge = findViewById(R.id.ivlarge);
        tvpname = findViewById(R.id.tvpname);
        tvpprice = findViewById(R.id.tvpprice);

        DatabaseReference ref =FirebaseDatabase.getInstance().getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (fromcontent.equals("Featured_Products")) {

                    String pname = String.valueOf(dataSnapshot.child(fromcontent).child(productid).child("name").getValue());
                    String pimage = String.valueOf(dataSnapshot.child(fromcontent).child(productid).child("image").getValue());
                    String pprice = String.valueOf(dataSnapshot.child(fromcontent).child(productid).child("price").getValue());

                    Picasso.with(ProductActivity_running.this).load(pimage).into(ivlarge);
                    tvpname.setText(pname);
                    tvpprice.setText(pprice);

                    //thumbnails

                    if (dataSnapshot.child(fromcontent).child(productid).child("thumbnail1").exists()) {
                        String thumb1 = String.valueOf(dataSnapshot.child(fromcontent).child(productid).child("thumbnail1").getValue());
                        thumblist.add(thumb1);
                    }
                    if (dataSnapshot.child(fromcontent).child(productid).child("thumbnail2").exists()) {
                        String thumb2 = String.valueOf(dataSnapshot.child(fromcontent).child(productid).child("thumbnail2").getValue());
                        thumblist.add(thumb2);
                    }
                    if (dataSnapshot.child(fromcontent).child(productid).child("thumbnail3").exists()) {
                        String thumb3 = String.valueOf(dataSnapshot.child(fromcontent).child(productid).child("thumbnail3").getValue());
                        thumblist.add(thumb3);
                    }
                    if (dataSnapshot.child(fromcontent).child(productid).child("thumbnail4").exists()) {
                        String thumb4 = String.valueOf(dataSnapshot.child(fromcontent).child(productid).child("thumbnail4").getValue());
                        thumblist.add(thumb4);
                    }

                }else{

                    String contextname=getIntent().getStringExtra("ContextName");

                    String pname = String.valueOf(dataSnapshot.child(fromcontent).child(contextname).child(productid).child("name").getValue());
                    String pimage = String.valueOf(dataSnapshot.child(fromcontent).child(contextname).child(productid).child("image").getValue());
                    String pprice = String.valueOf(dataSnapshot.child(fromcontent).child(contextname).child(productid).child("price").getValue());

                    Picasso.with(ProductActivity_running.this).load(pimage).into(ivlarge);
                    tvpname.setText(pname);
                    tvpprice.setText(pprice);

                    //thumbnails

                    if (dataSnapshot.child(fromcontent).child(contextname).child(productid).child("thumbnail1").exists()) {
                        String thumb1 = String.valueOf(dataSnapshot.child(fromcontent).child(contextname).child(productid).child("thumbnail1").getValue());
                        thumblist.add(thumb1);
                    }
                    if (dataSnapshot.child(fromcontent).child(contextname).child(productid).child("thumbnail2").exists()) {
                        String thumb2 = String.valueOf(dataSnapshot.child(fromcontent).child(contextname).child(productid).child("thumbnail2").getValue());
                        thumblist.add(thumb2);
                    }
                    if (dataSnapshot.child(fromcontent).child(contextname).child(productid).child("thumbnail3").exists()) {
                        String thumb3 = String.valueOf(dataSnapshot.child(fromcontent).child(contextname).child(productid).child("thumbnail3").getValue());
                        thumblist.add(thumb3);
                    }
                    if (dataSnapshot.child(fromcontent).child(contextname).child(productid).child("thumbnail4").exists()) {
                        String thumb4 = String.valueOf(dataSnapshot.child(fromcontent).child(contextname).child(productid).child("thumbnail4").getValue());
                        thumblist.add(thumb4);
                    }

                }

                ThumbnailRecyclerViewAdapter thumbnailRecyclerViewAdapter = new ThumbnailRecyclerViewAdapter(ProductActivity_running.this, thumblist);
                rvthumb.setAdapter(thumbnailRecyclerViewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        startActivity(new Intent(this,MerchantActivity.class));
//        finish();

//        if(1==1)
//            return;
        //thumbnail recycler view continued


        // buttons

        Button butbuy = findViewById(R.id.butbuy);

        butbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateCheckSum();
//                if(user!=null){
//
//                    final DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
//                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                            if(dataSnapshot.child("Users").child(user.getUid()).child("phoneno").exists()){
//
//                                generateCheckSum();
//
//                            }else {
//                                AlertDialog.Builder builder=new AlertDialog.Builder(ProductActivity.this);
//                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                                View viewv = inflater.inflate(R.layout.phonenoedit, null);
//                                builder.setView(viewv);
//
//                                edphno=viewv.findViewById(R.id.edphno);
//                                Button butadd=viewv.findViewById(R.id.tvadd);
//                                Button butcancel=viewv.findViewById(R.id.tvcancel);
//
//                                butadd.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//
//                                        boolean vp = validatephoneno();
//                                        if (vp) {
//                                            ref.child("Users").child(user.getUid()).child("username").setValue(user.getDisplayName());
//                                            ref.child("Users").child(user.getUid()).child("phoneno").setValue(String.valueOf(edphno.getEditText().getText()));
//                                        }else {
//                                            validatephoneno();
//                                        }
//                                        dialog.dismiss();
//
//                                        generateCheckSum();
//
//                                    }
//                                });
//
//                                butcancel.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        dialog.dismiss();
//                                    }
//                                });
//
//                                dialog=builder.create();
//                                dialog.show();
//
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                }else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
//                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    View view1 = inflater.inflate(R.layout.login, null);
//                    builder.setView(view1);
//
//                    // Facebook login
//
//                    mCallbackManager = CallbackManager.Factory.create();
//
//                    LoginButton fb = view1.findViewById(R.id.fbbtn);
//
//                    fb.setReadPermissions("email", "public_profile");
//                    fb.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//                        @Override
//                        public void onSuccess(LoginResult loginResult) {
////                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
//                            handleFacebookAccessToken(loginResult.getAccessToken());
//                            Toast.makeText(ProductActivity.this, "Successful", Toast.LENGTH_SHORT).show();
//
//                        }
//
//                        @Override
//                        public void onCancel() {
//                            Toast.makeText(ProductActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
//
////                        Log.d(TAG, "facebook:onCancel");
//                            // ...
//                        }
//
//                        @Override
//                        public void onError(FacebookException error) {
////                        Log.d(TAG, "facebook:onError", error);
//                            // ...
//                        }
//                    });
//
//
//                    dialog = builder.create();
//                    dialog.show();
//
//                    AccessToken accessToken = AccessToken.getCurrentAccessToken();
//                    boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
//                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
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

    private void handleFacebookAccessToken(AccessToken token) {
//        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(ProductActivity_running.this, "Successful", Toast.LENGTH_SHORT).show();

                            String username=user.getDisplayName();

                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(ProductActivity_running.this, "Signin failed.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
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






    //paytm payments


    private void generateCheckSum() {

        //getting the tax amount first.
        String txnAmount = tvpprice.getText().toString().trim();



        final Paytm paytm = new Paytm(
                Constants.M_ID,
                Constants.CHANNEL_ID,
                txnAmount,
                Constants.WEBSITE,
                Constants.CALLBACK_URL+"?ORDER_ID",
                Constants.INDUSTRY_TYPE_ID
        );

        paytm.callBackUrl= Constants.CALLBACK_URL+"?ORDER_ID="+paytm.orderId;
        startTxn(txnAmount);
       // initializePaytmPayment("NiggaBoii", paytm);



    }

    private void startTxn(String txnAmount)
    {

        PaytmPGService Service = PaytmPGService.getProductionService();
        HashMap<String, String> paramMap = new HashMap<String, String>();

        // these are mandatory parameters

        paramMap.put("CALLBACK_URL","https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=123456789");
        paramMap.put("CHANNEL_ID",Constants.CHANNEL_ID);
        paramMap.put("CHECKSUMHASH","muthafuka");
        paramMap.put("CUST_ID","CosutmerId");
        paramMap.put("INDUSTRY_TYPE_ID",Constants.INDUSTRY_TYPE_ID);
        paramMap.put("MID",Constants.M_ID);
        paramMap.put("ORDER_ID","123456789");
        paramMap.put("TXN_AMOUNT",txnAmount);
        paramMap.put("WEBSITE",Constants.WEBSITE);


        PaytmOrder Order = new PaytmOrder(paramMap);

		/*PaytmMerchant Merchant = new PaytmMerchant(
				"https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
				"https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");*/

        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

					/*@Override
					public void onTransactionSuccess(Bundle inResponse) {
						// After successful transaction this method gets called.
						// // Response bundle contains the merchant response
						// parameters.
						Log.d("LOG", "Payment Transaction is successful " + inResponse);
						Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
					}
					@Override
					public void onTransactionFailure(String inErrorMessage,
							Bundle inResponse) {
						// This method gets called if transaction failed. //
						// Here in this case transaction is completed, but with
						// a failure. // Error Message describes the reason for
						// failure. // Response bundle contains the merchant
						// response parameters.
						Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
						Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
					}*/

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        Log.e(TAG, "networkNotAvailable: " );
                        // method gets called.
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        Log.e(TAG, "clientAuthenticationFailed: "+inErrorMessage );
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {
                        Log.e(TAG, "onErrorLoadingWebPage : "+inErrorMessage );
                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        Toast.makeText(ProductActivity_running.this
                                ,"Back pressed. Transaction cancelled",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                });


    }

    String TAG="Patym";
    private void initializePaytmPayment(String checksumHash, Paytm paytm) {

        //getting paytm service
        PaytmPGService Service = PaytmPGService.getStagingService();

        //use this when using for production
        //PaytmPGService Service = PaytmPGService.getProductionService();

        //creating a hashmap and adding all the values required
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", Constants.M_ID);
        paramMap.put("ORDER_ID", paytm.getOrderId());
        paramMap.put("CUST_ID", paytm.getCustId());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put("CHECKSUMHASH", checksumHash);
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());


        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder(paramMap);

        //intializing the paytm service
        Service.initialize(order, null);

        Toast.makeText(this, "Starting payment service", Toast.LENGTH_SHORT).show();

        //finally starting the payment transaction
        Service.startPaymentTransaction(this, true, true, this);

    }

    //all these overriden method is to detect the payment result accordingly
    @Override
    public void onTransactionResponse(Bundle bundle) {

        Log.e("erf", "onTransactionResponse: "+bundle.toString() );
        Toast.makeText(this, bundle.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(this, "Back Pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Toast.makeText(this, s + bundle.toString(), Toast.LENGTH_LONG).show();
    }
}