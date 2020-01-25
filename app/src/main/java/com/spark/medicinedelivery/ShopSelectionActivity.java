package com.spark.medicinedelivery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.security.PrivateKey;
import java.util.ArrayList;

public class ShopSelectionActivity extends AppCompatActivity {

    private String medicine_name, chosen_shop_name;
    private TextView medname_textview;
    private RecyclerView recyclerView;
    private ArrayList<ShopCard> list;

    private DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_selection);

        medicine_name = getIntent().getExtras().getString("selected_medicine");
        medname_textview = (TextView)findViewById(R.id.shop_selection_medname_text);
        medname_textview.setText(medicine_name);

        recyclerView = (RecyclerView)findViewById(R.id.shop_selection_shop_list);

        list = new ArrayList<ShopCard>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        dbref = FirebaseDatabase.getInstance().getReference().child("Medicine").child(medicine_name).child("Shop");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    ShopCard shopCard = ds.getValue(ShopCard.class);
                    list.add(shopCard);
                }
                ShopCardAdapter adapter = new ShopCardAdapter(ShopSelectionActivity.this, list, medicine_name);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        BroadcastReceiver chosen_shop = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                chosen_shop_name = intent.getStringExtra("chosen_shop");
//                Toast.makeText(ShopSelectionActivity.this, chosen_shop_name, Toast.LENGTH_SHORT).show();
//            }
//        };

    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent parentIntent = NavUtils.getParentActivityIntent(ShopSelectionActivity.this);
        startActivity(parentIntent);
        finish();
    }
}
