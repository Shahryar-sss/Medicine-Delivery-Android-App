package com.spark.medicinedelivery;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewBuyersActivity extends AppCompatActivity {

    private Button prevpage_btn;
    private RecyclerView buyersview;

    ArrayList<BuyersCard> buyers;

    private DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_buyers);

        prevpage_btn = (Button)findViewById(R.id.viewbuyers_prev_page_btn);
        buyersview = (RecyclerView)findViewById(R.id.viewbuyers_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        buyersview.setLayoutManager(layoutManager);
        buyersview.setHasFixedSize(true);



        dbref = FirebaseDatabase.getInstance().getReference().child("Buyer");

        prevpage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent employeeMainIntent = new Intent(ViewBuyersActivity.this, EmployeeMainActivity.class);
                employeeMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(employeeMainIntent);
                finish();
            }
        });

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                buyers = new ArrayList<>();
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    BuyersCard buyersCard = ds.getValue(BuyersCard.class);
                    buyers.add(buyersCard);
                }
                BuyersCardAdapter adapter = new BuyersCardAdapter(buyers, ViewBuyersActivity.this);
                buyersview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent parentIntent = NavUtils.getParentActivityIntent(ViewBuyersActivity.this);
        startActivity(parentIntent);
        finish();
    }
}
