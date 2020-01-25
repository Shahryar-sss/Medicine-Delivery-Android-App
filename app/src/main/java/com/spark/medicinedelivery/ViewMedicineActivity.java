package com.spark.medicinedelivery;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.TextBlock;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewMedicineActivity extends AppCompatActivity {

    private Button prevpage_btn;
    private RecyclerView medicineview;
    private TextView shop_name;

    FirebaseAuth mAuth;
    DatabaseReference dbref;

    ArrayList<ViewMedCard> medlist;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medicine);

        prevpage_btn = (Button)findViewById(R.id.viewmeds_back_btn);
        medicineview = (RecyclerView)findViewById(R.id.viewmeds_recyclerview);
        shop_name = (TextView)findViewById(R.id.viewmeds_shop_name);

        mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        medicineview.setLayoutManager(layoutManager);
        medicineview.setHasFixedSize(true);

        dbref = FirebaseDatabase.getInstance().getReference();
        medlist = new ArrayList<>();

        dbref.child("Seller").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("Shop_Name").getValue().toString();
                shop_name.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dbref.child("Medicine").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    if(ds.child("Shop").hasChild(name))
                    {
                        ViewMedCard viewMedCard = ds.child("Info").getValue(ViewMedCard.class);
                        medlist.add(viewMedCard);
                    }
                }
                ViewMedCardAdapter adapter = new ViewMedCardAdapter(ViewMedicineActivity.this, medlist);
                medicineview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        prevpage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sellerMainIntent = new Intent(ViewMedicineActivity.this, SellerMainActivity.class);
                sellerMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(sellerMainIntent);
                finish();
            }
        });


    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent parentIntent = NavUtils.getParentActivityIntent(ViewMedicineActivity.this);
        startActivity(parentIntent);
        finish();
    }
}
