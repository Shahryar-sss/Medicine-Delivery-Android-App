package com.spark.medicinedelivery;

import android.app.Notification;
import android.content.Intent;
import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SubscriptionActivity extends AppCompatActivity {

    private TextView duration_tv, startdate_tv, discount_tv, totalprice_tv;
    private Button prevpage_btn, payment_btn;
    private RecyclerView medicineview;

    DatabaseReference dbref, transactref;
    FirebaseAuth mAuth;

    int totalprice;
    ArrayList<SubscriptionCard> medicines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        prevpage_btn = (Button) findViewById(R.id.subscription_screen_back_btn);
        duration_tv = (TextView)findViewById(R.id.subscription_screen_duration);
        startdate_tv = (TextView)findViewById(R.id.subscription_screen_start_date);
        discount_tv = (TextView)findViewById(R.id.subscription_screen_discount);
        totalprice_tv = (TextView)findViewById(R.id.subscription_screen_total_price);
        payment_btn = (Button)findViewById(R.id.subscription_screen_payment_btn);

        mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid();

        dbref = FirebaseDatabase.getInstance().getReference().child("Subscription").child(UID);

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                duration_tv.setText(dataSnapshot.child("Info").child("duration").getValue().toString());
                discount_tv.setText(dataSnapshot.child("Info").child("discount").getValue().toString());
                startdate_tv.setText(dataSnapshot.child("Info").child("start_date").getValue().toString());

                if(!dataSnapshot.child("Info").child("start_date").getValue().toString().equalsIgnoreCase("Pending"))
                    payment_btn.setVisibility(View.INVISIBLE);
                else
                    payment_btn.setVisibility(View.VISIBLE);

                totalprice = 0;

                for (DataSnapshot ds: dataSnapshot.child("Medicines").getChildren())
                {
                    totalprice = totalprice + Integer.parseInt(ds.child("Total_Price").getValue().toString());
                }
                totalprice_tv.setText("Rs. " + String.valueOf(totalprice));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        prevpage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent buyerMainIntent = new Intent(SubscriptionActivity.this, BuyerMainActivity.class);
                buyerMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(buyerMainIntent);
                finish();
            }
        });

        medicineview = (RecyclerView)findViewById(R.id.subscription_screen_medicine_recycerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        medicineview.setLayoutManager(layoutManager);
        medicineview.setHasFixedSize(true);

        dbref.child("Medicines").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                medicines = new ArrayList<>();
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    SubscriptionCard subscriptionCard = ds.getValue(SubscriptionCard.class);
                    medicines.add(subscriptionCard);
                }
                SubscriptionCardAdapter adapter = new SubscriptionCardAdapter(SubscriptionActivity.this, medicines);
                medicineview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        transactref = FirebaseDatabase.getInstance().getReference().child("Transaction");

        payment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String current_date = simpleDateFormat.format(date);

                Map date_map = new HashMap();
                date_map.put("start_date", current_date);

                dbref.child("Info").updateChildren(date_map).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        payment_btn.setVisibility(View.INVISIBLE);
                        Toast.makeText(SubscriptionActivity.this, "Payment Succesfull", Toast.LENGTH_LONG).show();
                    }
                });

                Notification notification = new Notification.Builder(getApplicationContext())
                        .setContentTitle("Subscription started !")
                        .setContentInfo("Your subscription has started on "+current_date)
                        .build();

                transactref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Date date = Calendar.getInstance().getTime();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String current_date = simpleDateFormat.format(date);
                        String current_month = current_date.substring(3,10);

                        if (dataSnapshot.hasChild(current_month))
                        {
                            int prev_amt = Integer.parseInt(dataSnapshot.child(current_month).child("Amount").getValue().toString());
                            int new_amt = prev_amt + totalprice;
                            transactref.child(current_month).child("Amount").setValue(String.valueOf(new_amt));

                        }
                        else
                            transactref.child(current_month).child("Amount").setValue(String.valueOf(totalprice));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent parentIntent = NavUtils.getParentActivityIntent(SubscriptionActivity.this);
        startActivity(parentIntent);
        finish();
    }
}
