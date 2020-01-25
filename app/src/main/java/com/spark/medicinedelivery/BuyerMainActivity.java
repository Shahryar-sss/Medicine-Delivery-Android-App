package com.spark.medicinedelivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.emergency.EmergencyNumber;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BuyerMainActivity extends AppCompatActivity {

    DatabaseReference dbref;
    RecyclerView recyclerView;
    ArrayList<MedicineCard> list;
    MedicineCardAdapter adapter;

    private Button profile_btn, cart_btn, subscription_btn, emergency_btn;
    private Button camera_btn;
    private TextView username;
    private CircleImageView dp_image;
    private CardView cardView;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_main);

        recyclerView = (RecyclerView)findViewById(R.id.buyer_mainactivity_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        username = (TextView)findViewById(R.id.buyer_mainactivity_username);
        dp_image = (CircleImageView)findViewById(R.id.buyer_mainactivity_dp_imageview);

        dbref = FirebaseDatabase.getInstance().getReference().child("Medicine");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<MedicineCard>();
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    MedicineCard medicineCard = ds.child("Info").getValue(MedicineCard.class);
                    list.add(medicineCard);
                }
                adapter = new MedicineCardAdapter(BuyerMainActivity.this, list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(BuyerMainActivity.this, "Connection Error. Reopen page", Toast.LENGTH_LONG).show();

            }
        });

        mAuth = FirebaseAuth.getInstance();
        final String UID = mAuth.getCurrentUser().getUid();

        userRef = FirebaseDatabase.getInstance().getReference().child("Buyer").child(UID);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user_name = dataSnapshot.child("username").getValue().toString();
                username.setText(user_name);
                String image = dataSnapshot.child("image").getValue().toString();
                if(!image.equalsIgnoreCase("default"))
                    Picasso.get().load(image).placeholder(R.drawable.male_avatar).into(dp_image);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String current_date = simpleDateFormat.format(date);

        userRef.child("last_login_date").setValue(current_date);



        profile_btn = (Button)findViewById(R.id.buyer_mainactivity_profile_btn);
        cart_btn = (Button)findViewById(R.id.buyer_mainactivity_cart_btn);
        subscription_btn = (Button)findViewById(R.id.buyer_mainactivity_subscription_btn);
        emergency_btn  = (Button)findViewById(R.id.buyer_mainactivity_emergency_btn);
        camera_btn = (Button)findViewById(R.id.buyer_mainactivity_image_upload_btn);

        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent textDetectionIntent = new Intent(BuyerMainActivity.this, TextDetectionActivity.class);
                textDetectionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(textDetectionIntent);
                finish();
            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(BuyerMainActivity.this, ProfileActivity.class);
                profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(profileIntent);
                finish();
            }
        });

        cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartIntent = new Intent(BuyerMainActivity.this, CartActivity.class);
                cartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(cartIntent);
                finish();
            }
        });

        subscription_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent subscriptionIntent = new Intent(BuyerMainActivity.this, SubscriptionActivity.class);
                subscriptionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(subscriptionIntent);
                finish();
            }
        });

        emergency_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emergencyIntent = new Intent(BuyerMainActivity.this, EmergencyActivity.class);
                emergencyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(emergencyIntent);
                finish();
            }
        });




    }
}
