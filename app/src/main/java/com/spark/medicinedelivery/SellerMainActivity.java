package com.spark.medicinedelivery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerMainActivity extends AppCompatActivity {

    private TextView shop_name;
    private Button addmed_btn, viewmed_btn, viewprofile_btn, logout_btn;

    FirebaseAuth mAuth;
    DatabaseReference dbref;

    CircleImageView shopimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main);

        addmed_btn = (Button)findViewById(R.id.seller_mainscreen_addmed_btn);
        viewmed_btn = (Button)findViewById(R.id.seller_mainscreen_viewmeds_btn);
        viewprofile_btn = (Button)findViewById(R.id.seller_mainscreen_update_btn);
        shopimage = (CircleImageView)findViewById(R.id.seller_mainscreen_shop_image);
        shop_name = (TextView)findViewById(R.id.seller_mainscreen_shop_name_text);
        logout_btn = (Button)findViewById(R.id.seller_mainscreen_logout_btn);

        mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid();

        dbref = FirebaseDatabase.getInstance().getReference().child("Seller").child(UID);
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shop_name.setText(dataSnapshot.child("Shop_Name").getValue().toString());

                String image = dataSnapshot.child("image").getValue().toString();
                if (!image.equalsIgnoreCase("default"))
                    Picasso.get().load(image).placeholder(R.drawable.shop_avatar).into(shopimage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String current_date = simpleDateFormat.format(date);

        dbref.child("last_login_date").setValue(current_date);



        addmed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddMedsIntent = new Intent(SellerMainActivity.this, AddMedicinesActivity.class);
                AddMedsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(AddMedsIntent);
                finish();
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent loginIntent = new Intent(SellerMainActivity.this, LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
                finish();
            }
        });

        viewmed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewMedsIntent = new Intent(SellerMainActivity.this, ViewMedicineActivity.class);
                viewMedsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(viewMedsIntent);
                finish();
            }
        });

        viewprofile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sellrProfileIntent = new Intent(SellerMainActivity.this, SellerProfileActivity.class);
                sellrProfileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(sellrProfileIntent);
                finish();
            }
        });

    }
}
