package com.spark.medicinedelivery;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewSellersActivity extends AppCompatActivity {

    private Button prevpage_btn;
    private RecyclerView sellersview;

    ArrayList<SellerCard> sellers;

    private DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sellers);

        prevpage_btn = (Button)findViewById(R.id.viewsellers_prev_page_btn);
        sellersview = (RecyclerView)findViewById(R.id.viewsellers_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        sellersview.setLayoutManager(layoutManager);
        sellersview.setHasFixedSize(true);

        dbref = FirebaseDatabase.getInstance().getReference().child("Seller");

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sellers = new ArrayList<>();
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    SellerCard sellerCard = ds.getValue(SellerCard.class);
                    sellers.add(sellerCard);
                }
                SellerCardAdapter adapter = new SellerCardAdapter(sellers, ViewSellersActivity.this);
                sellersview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        prevpage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent employeeMainIntent = new Intent(ViewSellersActivity.this, EmployeeMainActivity.class);
                employeeMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(employeeMainIntent);
                finish();
            }
        });

    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent parentIntent = NavUtils.getParentActivityIntent(ViewSellersActivity.this);
        startActivity(parentIntent);
        finish();
    }
}
