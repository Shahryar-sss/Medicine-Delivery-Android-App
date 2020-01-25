package com.spark.medicinedelivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.w3c.dom.Text;

import java.sql.Struct;
import java.util.HashMap;
import java.util.Map;

public class AddToCartActivity extends AppCompatActivity {

    String chosen_shop, chosen_medicine, price_text;
    private TextView med_name, shop_name;
    private Button plus_btn, minus_btn, add_to_cart_btn;
    private TextView quantity_text, chosen_med_price_text;

    private DatabaseReference dbref, cartStoreRef;
    FirebaseAuth mAuth;
    private ProgressDialog cartUpdateProgress;

    int quantity = 1, price, total_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        Bundle extras = getIntent().getExtras();
        chosen_medicine = extras.getString("chosen_medicine");
        chosen_shop = extras.getString("chosen_shop");

        med_name = (TextView) findViewById(R.id.chosen_med_name);
        shop_name = (TextView) findViewById(R.id.chosen_shop_name);
        plus_btn = (Button) findViewById(R.id.increase_quantity_btn);
        minus_btn = (Button) findViewById(R.id.decrease_quantity_btn);
        add_to_cart_btn = (Button) findViewById(R.id.add_to_cart_btn);
        quantity_text = (TextView) findViewById(R.id.selected_quantity_text);
        chosen_med_price_text = (TextView) findViewById(R.id.chosen_med_price);

        med_name.setText(chosen_medicine);
        shop_name.setText(chosen_shop);

        cartUpdateProgress = new ProgressDialog(this);

        dbref = FirebaseDatabase.getInstance().getReference().child("Medicine").child(chosen_medicine).child("Info");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                price_text = dataSnapshot.child("Price").getValue().toString();
                chosen_med_price_text.setText(price_text);
                price = Integer.parseInt(price_text);
                total_price = price;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                quantity_text.setText(String.valueOf(quantity));
                total_price = price * quantity;
            }
        });

        minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity == 1) {
                    Toast.makeText(AddToCartActivity.this, "Minimum quantity : 1", Toast.LENGTH_SHORT).show();
                } else {
                    quantity--;
                    quantity_text.setText(String.valueOf(quantity));
                    total_price = price * quantity;
                }

            }
        });

        mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid();
        cartStoreRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(UID).child(chosen_medicine);


        add_to_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cartUpdateProgress.setTitle("Adding to Cart");
                cartUpdateProgress.setMessage("Please wait while we add your medicine to your cart");
                cartUpdateProgress.setCanceledOnTouchOutside(false);
                cartUpdateProgress.show();

                Map cart_info = new HashMap();
                cart_info.put("Shop_Name", chosen_shop);
                cart_info.put("Med_Name", chosen_medicine);
                cart_info.put("Quantity", String.valueOf(quantity));
                cart_info.put("Total_Price", String.valueOf(total_price));

                cartStoreRef.setValue(cart_info).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            cartUpdateProgress.dismiss();
                            Toast.makeText(AddToCartActivity.this, "Item added to cart", Toast.LENGTH_LONG).show();
                            Intent BuyerMainIntent = new Intent(AddToCartActivity.this, BuyerMainActivity.class);
                            startActivity(BuyerMainIntent);
                            finish();
                        }
                    }
                });

            }
        });

    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent parentIntent = NavUtils.getParentActivityIntent(AddToCartActivity.this);
        startActivity(parentIntent);
        finish();
    }
}
