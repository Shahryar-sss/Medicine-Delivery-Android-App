package com.spark.medicinedelivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.FocusFinder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddMedicinesActivity extends AppCompatActivity {

    TextInputEditText medname, medexpiry, medprice, medusage;
    Spinner med_type;
    Button addbtn, donebtn;

    String name, expiry, usage, price, type;
    String shop_name;

    DatabaseReference dbref, shopref;
    FirebaseAuth mAuth;


    private ProgressDialog uploadProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicines);

        medname = (TextInputEditText)findViewById(R.id.seller_addmeds_medname_text);
        medexpiry = (TextInputEditText)findViewById(R.id.seller_addmeds_expiry_text);
        medprice = (TextInputEditText)findViewById(R.id.seller_addmeds_price_text);
        medusage = (TextInputEditText)findViewById(R.id.seller_addmeds_usage_text);
        med_type = (Spinner)findViewById(R.id.seller_addmeds_medtype);
        addbtn = (Button)findViewById(R.id.seller_addmeds_addbtn);
        donebtn = (Button)findViewById(R.id.seller_addmeds_donebtn);

        uploadProgress = new ProgressDialog(this);


        dbref = FirebaseDatabase.getInstance().getReference().child("Medicine");
        mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid();

        shopref = FirebaseDatabase.getInstance().getReference().child("Seller").child(UID);
        final Map shop_info = new HashMap();
        final Map med_info = new HashMap();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.med_type_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        med_type.setAdapter(adapter);

        shopref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shop_info.put("Name", dataSnapshot.child("Shop_Name").getValue().toString());
                shop_info.put("image", dataSnapshot.child("image").getValue().toString());
                shop_name = dataSnapshot.child("Shop_Name").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sellerMainIntent = new Intent(AddMedicinesActivity.this, SellerMainActivity.class);
                sellerMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(sellerMainIntent);
                finish();
            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = medname.getText().toString();
                expiry = medexpiry.getText().toString();
                usage = medusage.getText().toString();
                price = medprice.getText().toString();
                type = med_type.getSelectedItem().toString();

                if (name.equalsIgnoreCase("") || expiry.equalsIgnoreCase("") ||
                        usage.equalsIgnoreCase("") || price.equalsIgnoreCase("") )
                    Toast.makeText(AddMedicinesActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
                else
                {
                    uploadProgress.setTitle("Adding Medicines");
                    uploadProgress.setMessage("Adding the new medicine to the database");
                    uploadProgress.setCanceledOnTouchOutside(false);
                    uploadProgress.show();

                    shop_info.put("Expiry", expiry);

                    med_info.put("Name", name);
                    med_info.put("Price", price);
                    med_info.put("Type", type);
                    med_info.put("usage", usage);

                    dbref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChild(name))
                            {
                                dbref.child(name).child("Info").setValue(med_info);
                                dbref.child(name).child("Shop").child(shop_name).setValue(shop_info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            uploadProgress.dismiss();
                                            Toast.makeText(AddMedicinesActivity.this, "Medicines added", Toast.LENGTH_LONG).show();
                                            medexpiry.setText("");
                                            medname.setText("");
                                            medprice.setText("");
                                            medusage.setText("");
                                        }
                                    }
                                });
                            }
                            else
                            {
                                dbref.child(name).child("Shop").child(shop_name).setValue(shop_info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            uploadProgress.dismiss();
                                            Toast.makeText(AddMedicinesActivity.this, "Medicines added", Toast.LENGTH_LONG).show();
                                            medexpiry.setText("");
                                            medname.setText("");
                                            medprice.setText("");
                                            medusage.setText("");
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            uploadProgress.dismiss();
                            Toast.makeText(AddMedicinesActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });


    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent parentIntent = NavUtils.getParentActivityIntent(AddMedicinesActivity.this);
        startActivity(parentIntent);
        finish();
    }
}
