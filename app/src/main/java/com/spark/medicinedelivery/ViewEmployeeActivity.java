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

public class ViewEmployeeActivity extends AppCompatActivity {

    private RecyclerView employeeview;
    private Button prev_page_btn;

    ArrayList<EmployeeCard> employee;

    private DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employee);

        employeeview = (RecyclerView) findViewById(R.id.viewemployee_recyclerview);
        prev_page_btn = (Button) findViewById(R.id.viewemployee_prev_page_btn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        employeeview.setLayoutManager(layoutManager);
        employeeview.setHasFixedSize(true);



        dbref = FirebaseDatabase.getInstance().getReference().child("Employee");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                employee = new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    EmployeeCard employeeCard = ds.getValue(EmployeeCard.class);
                    employee.add(employeeCard);
                }
                EmployeeCardAdapter adapter = new EmployeeCardAdapter(ViewEmployeeActivity.this, employee);
                employeeview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        prev_page_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent employeeMainIntent = new Intent(ViewEmployeeActivity.this, EmployeeMainActivity.class);
                employeeMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(employeeMainIntent);
                finish();
            }
        });

    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent parentIntent = NavUtils.getParentActivityIntent(ViewEmployeeActivity.this);
        startActivity(parentIntent);
        finish();
    }

}
